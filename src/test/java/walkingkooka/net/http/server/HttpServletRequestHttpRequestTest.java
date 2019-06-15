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
import walkingkooka.collect.enumeration.Enumerations;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.Url;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpServletRequestHttpRequestTest implements ClassTesting2<HttpServletRequestHttpRequest>,
        HttpRequestTesting<HttpServletRequestHttpRequest> {

    private final static HttpProtocolVersion PROTOCOL_VERSION = HttpProtocolVersion.VERSION_1_1;
    private final static HttpMethod METHOD = HttpMethod.POST;
    private final static String URL = "/path/file?abc=123";

    private final static HttpHeaderName<?> HEADER1 = HttpHeaderName.CONTENT_LENGTH;
    private final static Long HEADERVALUE1 = 111L;

    private final static HttpHeaderName<?> HEADER2 = HttpHeaderName.SERVER;
    private final static String HEADERVALUE2 = "Server2";

    private final static String PARAMETER1 = "parameter1";
    private final static String VALUE1A = "value1a";
    private final static String VALUE1B = "value1b";

    private final static String PARAMETER2 = "parameter2";
    private final static String VALUE2 = "value2";

    private final static String COOKIENAME = "cookie123";
    private final static String COOKIEVALUE = "cookievalue456";

    private final static byte[] BYTES = new byte[]{1, 2, 3};

    @Test
    public void testWithNullHttpServletRequestFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpServletRequestHttpRequest.with(null);
        });
    }

    @Test
    public void testTransport() {
        assertSame(HttpTransport.SECURED,
                this.createRequest().transport());
    }

    @Test
    public void testProtocolVersion() {
        assertSame(PROTOCOL_VERSION,
                this.createRequest().protocolVersion());
    }

    @Test
    public void testMethod() {
        assertSame(METHOD,
                this.createRequest().method());
    }

    @Test
    public void testUrl() {
        assertEquals(Url.parseRelative(URL),
                this.createRequest().url());
    }

    @Test
    public void testHeaders() {
        assertEquals(Maps.of(HEADER1, HEADERVALUE1, HEADER2, HEADERVALUE2),
                this.createRequest().headers());
    }

    @Test
    public void testBody() {
        assertArrayEquals(BYTES, this.createRequest().body());
        assertArrayEquals(BYTES, this.createRequest().body());
    }

    @Test
    public void testParameters() {
        final Map<HttpRequestParameterName, List<String>> parameters = this.createRequest().parameters();
        assertEquals(Lists.of(VALUE1A, VALUE1B),
                parameters.get(HttpRequestParameterName.with(PARAMETER1)));
    }

    @Test
    public void testParameterValues() {
        assertEquals(Lists.of(VALUE1A, VALUE1B),
                this.createRequest().parameterValues(HttpRequestParameterName.with(PARAMETER1)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createRequest(),
                "SECURED\n" +
                        "POST /path/file?abc=123 HTTP/1.1\n" +
                        "Content-Length=111\n" +
                        "Server=Server2\n" +
                        "parameter1=value1a\n" +
                        "value1b\n" +
                        "parameter2=value2");
    }

    @Override
    public HttpServletRequestHttpRequest createRequest() {
        return HttpServletRequestHttpRequest.with(new FakeHttpServletRequest() {

            @Override
            public boolean isSecure() {
                return true;
            }

            @Override
            public String getProtocol() {
                return PROTOCOL_VERSION.value();
            }

            @Override
            public String getMethod() {
                return METHOD.value();
            }

            @Override
            public String getRequestURI() {
                return URL;
            }

            @Override
            public String getHeader(final String header) {
                if (HEADER1.value().equals(header)) {
                    return "" + HEADERVALUE1;
                }
                if (HEADER2.value().equals(header)) {
                    return HEADERVALUE2;
                }
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return Enumerations.array(new Object[]{HEADER1.value(), HEADER2.value()});
            }

            @Override
            public Cookie[] getCookies() {
                return new Cookie[]{new Cookie(COOKIENAME, COOKIEVALUE)};
            }

            @Override
            public ServletInputStream getInputStream() {
                final ByteArrayInputStream bytes = new ByteArrayInputStream(BYTES);

                return new ServletInputStream() {
                    @Override
                    public boolean isFinished() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public boolean isReady() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void setReadListener(final ReadListener readListener) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public int read() throws IOException {
                        return bytes.read();
                    }
                };
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return Maps.of(PARAMETER1, array(VALUE1A, VALUE1B), PARAMETER2, array(VALUE2));
            }

            private String[] array(final String... values) {
                return values;
            }
        });
    }

    @Override
    public Class<HttpServletRequestHttpRequest> type() {
        return HttpServletRequestHttpRequest.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
