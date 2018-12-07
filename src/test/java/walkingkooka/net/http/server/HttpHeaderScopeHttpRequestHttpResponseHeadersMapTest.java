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

package walkingkooka.net.http.server;

import org.junit.Test;
import walkingkooka.collect.map.MapTestCase;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpHeaderScope;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public final class HttpHeaderScopeHttpRequestHttpResponseHeadersMapTest extends MapTestCase<HttpHeaderScopeHttpRequestHttpResponseHeadersMap,
        HttpHeaderName<?>,
        Object> {

    private final static HttpHeaderName<Long> HEADER = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADER_VALUE = 123L;
    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.one(HEADER, HEADER_VALUE);

    @Test(expected = NotAcceptableHeaderException.class)
    public void testContainsKeyResponseScopeHeader() {
        this.createMap().containsKey(HttpHeaderName.SET_COOKIE);
    }

    @Test
    public void testContainsKey() {
        this.containsKeyAndCheck(HEADER,
                true);
    }

    @Test
    public void testContainsKeyAbsent() {
        this.containsKeyAndCheck(HttpHeaderName.COOKIE,
                false);
    }

    private void containsKeyAndCheck(final Object key,
                                     final boolean containsKey) {
        assertEquals("request containsKey " + key + " returned wrong value",
                containsKey,
                this.createMap().containsKey(key));
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testGetResponseScopeHeader() {
        this.createMap().get(HttpHeaderName.SET_COOKIE);
    }

    @Test
    public void testGet() {
        this.getAndCheck(HEADER,
                HEADER_VALUE);
    }

    @Test
    public void testGetAbsentWrongKeyType() {
        this.getAndCheckAbsent(this);
    }

    @Test
    public void testGetAbsent() {
        this.getAndCheckAbsent(HttpHeaderName.COOKIE);
    }

    @Test
    public void testKeySet() {
        assertEquals(HEADERS.keySet(), this.createMap().keySet());
    }

    @Test
    public void testEntrySet() {
        assertEquals(HEADERS.entrySet(), this.createMap().entrySet());
    }

    @Test
    public void testValues() {
        assertEquals(HEADERS.values(), this.createMap().values());
    }

    @Test
    public void testToString() {
        assertEquals(HEADERS.toString(), this.createMap().toString());
    }

    @Override
    protected HttpHeaderScopeHttpRequestHttpResponseHeadersMap createMap() {
        return HttpHeaderScopeHttpRequestHttpResponseHeadersMap.with(HEADERS, HttpHeaderScope.REQUEST);
    }

    @Override
    protected Class<HttpHeaderScopeHttpRequestHttpResponseHeadersMap> type() {
        return HttpHeaderScopeHttpRequestHttpResponseHeadersMap.class;
    }
}
