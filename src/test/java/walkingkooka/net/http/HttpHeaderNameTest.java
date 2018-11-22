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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.NameTestCase;
import walkingkooka.net.media.MediaType;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class HttpHeaderNameTest extends NameTestCase<HttpHeaderName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testControlCharacterFails() {
        HttpHeaderName.with("header\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceFails() {
        HttpHeaderName.with("header ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabFails() {
        HttpHeaderName.with("header\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAsciiFails() {
        HttpHeaderName.with("header\u0100;");
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(HttpHeaderName.ACCEPT, HttpHeaderName.with(HttpHeaderName.ACCEPT.value()));
    }

    @Test(expected = NullPointerException.class)
    public void testHeaderValueNullRequestFails() {
        HttpHeaderName.ALLOW.headerValue((HttpRequest)null);
    }

    @Test
    public void testHeaderValueAcceptRequest() {
        this.headerValueRequestAndCheck(HttpHeaderName.ACCEPT,
                "text/html, application/xhtml+xml",
                Optional.of(Lists.of(
                        MediaType.with("text", "html"),
                        MediaType.with("application", "xhtml+xml"))));
    }

    @Test
    public void testHeaderValueContentLengthRequest() {
        this.headerValueRequestAndCheck(HttpHeaderName.CONTENT_LENGTH,
                "123",
                Optional.of(123L));
    }

    @Test
    public void testHeaderValueContentLengthAbsentRequest() {
        this.headerValueRequestAndCheck(HttpHeaderName.CONTENT_LENGTH,
                null,
                Optional.empty());
    }

    private <T> void headerValueRequestAndCheck(final HttpHeaderName<T> headerName,
                                                final String headerValue,
                                                final Optional<T> value) {
        assertEquals(headerName + "=" + headerValue,
                value,
                headerName.headerValue(new FakeHttpRequest() {
                    @Override
                    public Map<HttpHeaderName<?>, String> headers() {
                        return Maps.one(headerName, headerValue);
                    }
                }));
    }

    @Test(expected = NullPointerException.class)
    public void testHeaderValueNullStringFails() {
        HttpHeaderName.ALLOW.headerValue((String) null);
    }

    @Test
    public void testHeaderValueAcceptString() {
        this.headerValueStringAndCheck(HttpHeaderName.ACCEPT,
                "text/html, application/xhtml+xml",
                Lists.of(
                        MediaType.with("text", "html"),
                        MediaType.with("application", "xhtml+xml")));
    }

    @Test
    public void testHeaderValueContentLengthString() {
        this.headerValueStringAndCheck(HttpHeaderName.CONTENT_LENGTH,
                "123",
                123L);
    }

    private <T> void headerValueStringAndCheck(final HttpHeaderName<T> headerName,
                                               final String headerValue,
                                               final T value) {
        assertEquals(headerName + "=" + headerValue,
                value,
                headerName.headerValue(headerValue));
    }

    @Test
    public void testToString() {
        final String name = "X-custom";
        assertEquals(name, HttpHeaderName.with(name).toString());
    }

    @Override
    protected HttpHeaderName createName(final String name) {
        return HttpHeaderName.with(name);
    }

    @Override
    protected Class<HttpHeaderName<?>> type() {
        return Cast.to(HttpHeaderName.class);
    }
}
