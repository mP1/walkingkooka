/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.test.PublicClassTestCase;

import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class HttpEntityTest extends PublicClassTestCase<HttpEntity> {

    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.one(HttpHeaderName.CONTENT_LENGTH, 123L);
    private final static Map<HttpHeaderName<?>, Object> INVALID_HEADERS = Maps.one(HttpHeaderName.CONTENT_LENGTH, "invalid");
    private final static byte[] BODY = new byte[123];

    // with ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNullHeadersFails() {
        HttpEntity.with(null, BODY);
    }

    @Test(expected = HeaderValueException.class)
    public void testWithInvalidHeaderFails() {
        HttpEntity.with(INVALID_HEADERS, BODY);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullBodyFails() {
        HttpEntity.with(HEADERS, null);
    }

    @Test
    public void testWith() {
        this.check(HttpEntity.with(HEADERS, BODY));
    }

    // setHeaders ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetHeaderNullFails() {
        this.create().setHeaders(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testSetHeaderInvalidFails() {
        this.create().setHeaders(INVALID_HEADERS);
    }

    @Test
    public void testSetHeadersSame() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.setHeaders(HEADERS));
    }

    @Test
    public void testSetHeadersDifferent() {
        final HttpEntity entity = this.create();
        final Map<HttpHeaderName<?>, Object> headers = Maps.one(HttpHeaderName.CONTENT_LENGTH, 456L);
        final HttpEntity different = entity.setHeaders(headers);
        this.check(different, headers, BODY);
    }

    // setBody ....................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetBodyNullFails() {
        this.create().setBody(null);
    }

    @Test
    public void testSetBodySame() {
        final HttpEntity entity = this.create();
        assertSame(entity, entity.setBody(BODY));
    }

    @Test
    public void testSetBodyDifferent() {
        final HttpEntity entity = this.create();
        final byte[] body = new byte[456];
        final HttpEntity different = entity.setBody(body);
        this.check(different, HEADERS, body);
    }

    // toString ....................................................................................................

    @Test
    public void testToString() {
        assertEquals(HEADERS + " 123", this.create().toString());
    }

    private HttpEntity create() {
        return HttpEntity.with(HEADERS, BODY);
    }

    private void check(final HttpEntity entity) {
        check(entity, HEADERS, BODY);
    }

    private void check(final HttpEntity entity, final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        assertEquals("headers", headers, entity.headers());
        assertArrayEquals("body", body, entity.body());
    }

    @Override
    protected Class<HttpEntity> type() {
        return HttpEntity.class;
    }
}
