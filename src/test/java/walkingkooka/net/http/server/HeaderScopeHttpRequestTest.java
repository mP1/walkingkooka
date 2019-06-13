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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HeaderScopeHttpRequestTest implements ClassTesting2<HeaderScopeHttpRequest>,
        HttpRequestTesting<HeaderScopeHttpRequest> {

    private final static HttpTransport TRANSPORT = HttpTransport.SECURED;
    private final static HttpMethod METHOD = HttpMethod.POST;
    private final static HttpProtocolVersion VERSION = HttpProtocolVersion.VERSION_1_1;
    private final static RelativeUrl URL = Url.parseRelative("/path/file");
    private final static HttpHeaderName<Long> HEADER = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADER_VALUE = 123L;
    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.of(HEADER, HEADER_VALUE);
    private final static byte[] BYTES = new byte[]{1, 2, 3};
    private final static Map<HttpRequestParameterName, List<String>> PARAMETERS = Maps.fake();
    private final static String TOSTRING = HeaderScopeHttpRequestTest.class.getSimpleName() + ".toString";

    @Test
    public void testTransport() {
        assertSame(TRANSPORT, this.createRequest().transport());
    }

    @Test
    public void testMethod() {
        assertSame(METHOD, this.createRequest().method());
    }

    @Test
    public void testProtocolVersion() {
        assertSame(VERSION, this.createRequest().protocolVersion());
    }

    @Test
    public void testUrl() {
        assertSame(URL, this.createRequest().url());
    }

    @Test
    public void testHeaders() {
        assertNotSame(HEADERS, this.createRequest().url());
    }

    @Test
    public void testHeadersContainsKeyResponseScopeHeader() {
        assertThrows(NotAcceptableHeaderException.class, () -> {
            this.createRequest().headers().containsKey(HttpHeaderName.SET_COOKIE);
        });
    }

    @Test
    public void testHeadersContainsKey() {
        this.containsKeyAndCheck(this.createRequest(),
                HEADER,
                true);
    }

    @Test
    public void testHeadersContainsKeyAbsent() {
        this.containsKeyAndCheck(this.createRequest(),
                HttpHeaderName.COOKIE,
                false);
    }

    private void containsKeyAndCheck(final HeaderScopeHttpRequest request,
                                     final Object key,
                                     final boolean containsKey) {
        assertEquals(containsKey,
                request.headers().containsKey(key),
                "request containsKey " + key + " returned wrong value");
    }

    @Test
    public void testHeadersGetResponseScopeHeader() {
        assertThrows(NotAcceptableHeaderException.class, () -> {
            this.createRequest().headers().get(HttpHeaderName.SET_COOKIE);
        });
    }

    @Test
    public void testHeadersGet() {
        this.getAndCheck(this.createRequest(),
                HEADER,
                HEADER_VALUE);
    }

    @Test
    public void testHeadersGetAbsent() {
        this.getAndCheck(this.createRequest(),
                HttpHeaderName.COOKIE,
                null);
    }

    private void getAndCheck(final HeaderScopeHttpRequest request,
                             final Object key,
                             final Object value) {
        assertEquals(value,
                request.headers().get(key),
                "request get " + key + " returned wrong value");
    }

    @Test
    public void testBody() {
        assertArrayEquals(BYTES, this.createRequest().body());
    }

    @Test
    public void testParameters() {
        assertSame(PARAMETERS, this.createRequest().parameters());
    }

    @Test
    public void testToString() {
        assertSame(TOSTRING, this.createRequest().toString());
    }

    @Override
    public HeaderScopeHttpRequest createRequest() {
        return HeaderScopeHttpRequest.with(new FakeHttpRequest() {
            @Override
            public HttpTransport transport() {
                return TRANSPORT;
            }

            @Override
            public HttpMethod method() {
                return METHOD;
            }

            @Override
            public HttpProtocolVersion protocolVersion() {
                return VERSION;
            }

            @Override
            public RelativeUrl url() {
                return URL;
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return HEADERS;
            }

            @Override
            public byte[] body() {
                return BYTES;
            }

            @Override
            public Map<HttpRequestParameterName, List<String>> parameters() {
                return PARAMETERS;
            }

            @Override
            public List<String> parameterValues(final HttpRequestParameterName parameterName) {
                return PARAMETERS.get(parameterName);
            }

            @Override
            public String toString() {
                return TOSTRING;
            }
        });
    }

    @Override
    public Class<HeaderScopeHttpRequest> type() {
        return HeaderScopeHttpRequest.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
