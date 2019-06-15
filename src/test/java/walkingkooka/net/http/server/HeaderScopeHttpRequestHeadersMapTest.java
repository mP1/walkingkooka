/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.http.server;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.MapTesting2;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.HttpHeaderScope;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HeaderScopeHttpRequestHeadersMapTest implements ClassTesting2<HeaderScopeHttpRequestHeadersMap>,
        MapTesting2<HeaderScopeHttpRequestHeadersMap, HttpHeaderName<?>, Object> {

    private final static HttpHeaderName<Long> HEADER = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADER_VALUE = 123L;
    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.of(HEADER, HEADER_VALUE);

    @Test
    public void testContainsKeyResponseScopeHeader() {
        assertThrows(NotAcceptableHeaderException.class, () -> {
            this.createMap().containsKey(HttpHeaderName.SET_COOKIE);
        });
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
        assertEquals(containsKey,
                this.createMap().containsKey(key),
                "request containsKey " + key + " returned wrong value");
    }

    @Test
    public void testGetResponseScopeHeader() {
        assertThrows(NotAcceptableHeaderException.class, () -> {
            this.createMap().get(HttpHeaderName.SET_COOKIE);
        });
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
        this.toStringAndCheck(this.createMap(), HEADERS.toString());
    }

    @Override
    public HeaderScopeHttpRequestHeadersMap createMap() {
        return HeaderScopeHttpRequestHeadersMap.with(HEADERS, HttpHeaderScope.REQUEST);
    }

    @Override
    public Class<HeaderScopeHttpRequestHeadersMap> type() {
        return HeaderScopeHttpRequestHeadersMap.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
