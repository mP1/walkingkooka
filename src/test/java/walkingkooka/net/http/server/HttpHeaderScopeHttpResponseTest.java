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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.NotAcceptableHeaderException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.cookie.Cookie;
import walkingkooka.test.Latch;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class HttpHeaderScopeHttpResponseTest extends HttpResponseTestCase<HttpHeaderScopeHttpResponse> {

    private final static HttpHeaderName<String> HEADER = HttpHeaderName.SERVER;
    private final static String VALUE = "Server 123";
    private final static String TOSTRING = HttpHeaderScopeHttpResponseTest.class.getSimpleName() + ".toString";


    @Test(expected = NullPointerException.class)
    public void testWithNullResponseFails() {
        HttpHeaderScopeHttpResponse.with(null);
    }

    @Test
    public void testSetStatus() {
        final Latch set = Latch.create();
        final HttpStatus status = HttpStatusCode.BAD_REQUEST.status();

        HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {

            @Override
            public void setStatus(final HttpStatus s) {
                set.set("Status already set to " + status);
                assertSame("status", status, s);
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.fake();
            }

        }).setStatus(status);

        assertEquals("status not set", true, set.value());
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testAddHeaderRequestScopeFails() {
        this.createResponse()
                .addHeader(HttpHeaderName.COOKIE, Cookie.parseClientHeader("cookie123=456"));
    }

    @Test
    public void testAddHeaderResponseHeader() {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();

        HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.fake();
            }

            @Override
            public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
                headers.put(name, value);
            }
        }).addHeader(HEADER, VALUE);

        assertEquals("header after addHeader", Maps.one(HEADER, VALUE), headers);
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testHeaderContainsKeyRequestHeaderScopeFails() {
        this.createResponse().headers().containsKey(HttpHeaderName.COOKIE);
    }

    @Test
    public void testHeaderContainsResponseHeader() {
        assertTrue(VALUE,
                HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {
                    @Override
                    public Map<HttpHeaderName<?>, Object> headers() {
                        return Maps.one(HEADER, VALUE);
                    }
                }).headers().containsKey(HEADER));
    }

    @Test(expected = NotAcceptableHeaderException.class)
    public void testHeaderGetRequestHeaderScopeFails() {
        this.createResponse().headers().get(HttpHeaderName.COOKIE);
    }

    @Test
    public void testHeaderGetResponseHeader() {
        assertSame(VALUE,
                HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {
                    @Override
                    public Map<HttpHeaderName<?>, Object> headers() {
                        return Maps.one(HEADER, VALUE);
                    }
                }).headers().get(HEADER));
    }

    @Test
    public void testSetBody() {
        final Latch set = Latch.create();
        final byte[] body = new byte[123];

        HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {

            @Override
            public void setBody(final byte[] b) {
                set.set("Body already set to " + body);
                assertSame("body", body, b);
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.fake();
            }

        }).setBody(body);

        assertEquals("body not set", true, set.value());
    }

    @Test
    public void testSetBodyText() {
        final Latch set = Latch.create();
        final String text = "body text 123";

        HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {

            @Override
            public void setBodyText(final String t) {
                set.set("Body already set to " + text);
                assertSame("text", text, t);
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.fake();
            }

        }).setBodyText(text);

        assertEquals("text not set", true, set.value());
    }

    @Test
    public void testToString() {
        assertEquals(TOSTRING, this.createResponse().toString());
    }

    @Override
    protected HttpHeaderScopeHttpResponse createResponse() {
        return HttpHeaderScopeHttpResponse.with(new FakeHttpResponse() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.fake();
            }

            public String toString() {
                return TOSTRING;
            }
        });
    }

    @Override
    protected Class<HttpHeaderScopeHttpResponse> type() {
        return HttpHeaderScopeHttpResponse.class;
    }
}
