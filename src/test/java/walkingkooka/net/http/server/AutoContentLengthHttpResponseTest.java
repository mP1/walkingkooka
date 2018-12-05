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
import walkingkooka.net.header.HeaderValueToken;
import walkingkooka.net.header.NotAcceptableHeaderValueException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.test.Latch;

import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class AutoContentLengthHttpResponseTest extends WrapperHttpResponseTestCase<AutoContentLengthHttpResponse> {

    @Test
    public void testAddHeaderContentLength() {
        this.addHeaderAndCheck(HttpRequests.fake(),
                HttpHeaderName.CONTENT_LENGTH,
                123L);
    }

    @Test
    public void testAddHeaderContentLengthTwice() {
        this.addHeaderAndCheck(HttpRequests.fake(),
                HttpHeaderName.CONTENT_LENGTH,
                123L,
                HttpHeaderName.CONTENT_LENGTH,
                345L);
    }

    @Test
    public void testSetBodyContentLengthAbsent() {
        this.setBodyAndCheck(null,
                new byte[]{1, 2, 3});
    }

    @Test
    public void testSetBodyContentLengthSetBefore() {
        final byte[] bytes = new byte[]{1, 2, 3};
        this.setBodyAndCheck(Long.valueOf(bytes.length),
                bytes);
    }

    @Test(expected = NotAcceptableHeaderValueException.class)
    public void testSetBodyContentLengthIncorrectFail() {
        this.setBodyAndCheck(999L,
                new byte[]{1, 2, 3});
    }

    private void setBodyAndCheck(final Long contentLength,
                                 final byte[] bytes) {
        final Latch set = Latch.create();
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        final HttpResponse response = this.createResponse(
                new FakeHttpResponse() {

                    @Override
                    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
                        headers.put(name, name.checkValue(value));
                    }

                    public Map<HttpHeaderName<?>, Object> headers() {
                        return Maps.readOnly(headers);
                    }

                    @Test
                    public void setBody(final byte[] b) {
                        assertArrayEquals("bytes", bytes, b);
                        set.set("setBody");
                    }
                });
        if (null != contentLength) {
            response.addHeader(HttpHeaderName.CONTENT_LENGTH,
                    contentLength);
        }
        response.setBody(bytes);
        assertTrue("wrapped response setBody(bytes) not called", set.value());
        assertEquals("headers",
                Maps.one(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(bytes.length)),
                response.headers());
    }

    @Test
    public void testSetBodyText() {
        final Latch set = Latch.create();
        final String text = "anc123";
        this.createResponse(new FakeHttpResponse() {
            @Test
            public void setBodyText(final String t) {
                assertSame("text", text, t);
                set.set("setBodyText");
            }
        }).setBodyText(text);
        assertTrue("wrapped response setBodyText(text) not called", set.value());
    }

    @Override
    AutoContentLengthHttpResponse createResponse(final HttpRequest request, final HttpResponse response) {
        return AutoContentLengthHttpResponse.with(request, response);
    }

    @Override
    HttpRequest request() {
        return this.request(Maps.ordered());
    }

    private HttpRequest request(final String acceptEncoding) {
        return this.request(Maps.one(HttpHeaderName.ACCEPT_ENCODING, HeaderValueToken.parseList(acceptEncoding)));
    }

    private HttpRequest request(final Map<HttpHeaderName<?>, Object> headers) {
        return new FakeHttpRequest() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.readOnly(headers);
            }
        };
    }

    @Override
    protected Class<AutoContentLengthHttpResponse> type() {
        return AutoContentLengthHttpResponse.class;
    }
}
