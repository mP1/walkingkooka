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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.enumeration.Enumerations;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.MapTestCase;
import walkingkooka.net.header.ETag;
import walkingkooka.net.header.ETagValidator;
import walkingkooka.net.header.HttpHeaderName;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HttpServletRequestHttpRequestHeadersMapTest extends MapTestCase<HttpServletRequestHttpRequestHeadersMap,
        HttpHeaderName<?>, Object> {

    private final static HttpHeaderName<?> HEADER1 = HttpHeaderName.CONTENT_LENGTH;
    private final static Long VALUE1 = 111L;

    private final static HttpHeaderName<?> HEADER2 = HttpHeaderName.SERVER;
    private final static String VALUE2 = "Server2";

    @Test
    public void testContainsKey() {
        this.containsKeyAndCheck(HEADER1);
    }

    @Test
    public void testContainsKey2() {
        this.containsKeyAndCheck(HEADER2);
    }

    @Test
    public void testContainsValue() {
        this.containsValueAndCheck(VALUE1);
    }

    @Test
    public void testContainsValue2() {
        this.containsValueAndCheck(VALUE2);
    }

    @Test
    public void testGet() {
        this.getAndCheck(HEADER1, VALUE1);
    }

    @Test
    public void testGet2() {
        this.getAndCheck(HEADER2, VALUE2);
    }

    @Test
    public void testGetOrDefault() {
        final HttpServletRequestHttpRequestHeadersMap map = this.createMap();
        assertEquals(VALUE1,
                map.getOrDefault(HEADER1, "wrong"),
                "getOrDefault returned wrong value " + map);
    }

    @Test
    public void testGetOrDefaultKeyAbsent() {
        final HttpServletRequestHttpRequestHeadersMap map = this.createMap();
        final ETag etag = ETagValidator.STRONG.setValue("default-value-etag");

        assertEquals(etag,
                map.getOrDefault(HttpHeaderName.E_TAG, etag),
                "getOrDefault returned wrong value " + map);
    }

    @Test
    public void testKeySet() {
        final HttpServletRequestHttpRequestHeadersMap map = this.createMap();
        assertEquals(Lists.of(HEADER1, HEADER2), new ArrayList<>(map.keySet()));
    }

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createMap(), 2);
    }

    @Override
    protected HttpServletRequestHttpRequestHeadersMap createMap() {
        return HttpServletRequestHttpRequestHeadersMap.with(this.request());
    }

    private HttpServletRequest request() {
        return new FakeHttpServletRequest() {

            @Override
            public String getHeader(final String header) {
                if (HEADER1.value().equalsIgnoreCase(header)) {
                    return "" + VALUE1;
                }
                if (HEADER2.value().equalsIgnoreCase(header)) {
                    return "" + VALUE2;
                }
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return Enumerations.array(new Object[]{HEADER1.value(), HEADER2.value()});
            }
        };
    }

    @Override
    protected Class<HttpServletRequestHttpRequestHeadersMap> type() {
        return HttpServletRequestHttpRequestHeadersMap.class;
    }
}
