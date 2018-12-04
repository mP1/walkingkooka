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
import walkingkooka.net.header.CharsetHeaderValue;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.NotAcceptableHeaderValueException;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.test.Latch;

import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class AutoTextEncodingHttpResponseTest extends WrapperHttpResponseTestCase<AutoTextEncodingHttpResponse> {

    @Test
    public void testAddHeader() {
        this.addHeaderAndCheck(
                HttpRequests.fake(),
                HttpHeaderName.CONTENT_LENGTH, 123L);
    }

    @Test(expected = NotAcceptableHeaderValueException.class)
    public void testAddHeaderContentTypeMissingCharsetFails() {
        this.createResponse()
                .addHeader(HttpHeaderName.CONTENT_TYPE, MediaType.ANY_TEXT);
    }

    @Test(expected = NotAcceptableHeaderValueException.class)
    public void testAddHeaderContentTypeAcceptCharsetUnmatchedFails() {
        final Charset utf8 = Charset.forName("utf8");

        final Charset unsupported = Charset.availableCharsets()
                .values()
                .stream()
                .filter(c -> !utf8.contains(c))
                .findFirst()
                .get();

        this.createResponse(utf8.name(), HttpResponses.fake())
                .addHeader(HttpHeaderName.CONTENT_TYPE, this.mediaType(unsupported.name()));
    }

    @Test
    public void testAddHeaderContentTypeAcceptCharsetWildcard() {
        this.addHeaderAndCheck(
                this.request("*"),
                HttpHeaderName.CONTENT_TYPE,
                this.mediaType("utf8"));
    }

    @Test
    public void testAddHeaderContentTypeAcceptCharsetMatched() {
        this.addHeaderAndCheck(
                this.request("utf16, utf8"),
                HttpHeaderName.CONTENT_TYPE,
                this.mediaType("utf8"));
    }

    @Test
    public void testSetBody() {
        final Latch set = Latch.create();
        final byte[] bytes = new byte[]{1, 2, 3};
        this.createResponse(new FakeHttpResponse() {
            @Test
            public void setBody(final byte[] b) {
                assertSame(bytes, b);
                set.set("setBody");
            }
        }).setBody(bytes);
        assertTrue("wrapped response setBody(bytes) not called", set.value());
    }

    @Test(expected = HeaderValueException.class)
    public void testSetBodyTextWithoutContentTypeFails() {
        this.createResponse(new FakeHttpResponse() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.empty();
            }
        }).setBodyText("text");
    }

    @Test
    public void testSetBodyTextUtf8() {
        this.setBodyTextAndCheck("abc123", "utf8");
    }

    @Test
    public void testSetBodyTextUtf16() {
        this.setBodyTextAndCheck("abc123", "utf16");
    }

    private void setBodyTextAndCheck(final String text, final String charsetName) {
        final Latch set = Latch.create();
        final Charset charset = Charset.forName(charsetName);

        final HttpResponse response = this.createResponse(
                "*",
                new FakeHttpResponse() {

                    @Override
                    public Map<HttpHeaderName<?>, Object> headers() {
                        return Maps.readOnly(this.headers);
                    }

                    @Override
                    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
                        this.headers.put(name, value);
                    }

                    private final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();

                    @Test
                    public void setBody(final byte[] body) {
                        assertArrayEquals("setBody", text.getBytes(charset), body);
                        set.set("setBody");
                    }
                });
        response.addHeader(HttpHeaderName.CONTENT_TYPE, this.mediaType(charsetName));
        response.setBodyText(text);

        assertTrue("wrapped response setBody() not called with encoded text", set.value());
    }

    private AutoTextEncodingHttpResponse createResponse(final String acceptCharset, final HttpResponse response) {
        return AutoTextEncodingHttpResponse.with(
                this.request(acceptCharset),
                response);
    }

    @Override
    AutoTextEncodingHttpResponse createResponse(final HttpRequest request, final HttpResponse response) {
        return AutoTextEncodingHttpResponse.with(request, response);
    }

    @Override
    HttpRequest request() {
        return HttpRequests.fake();
    }

    private HttpRequest request(final String acceptCharset) {
        return new FakeHttpRequest() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.one(HttpHeaderName.ACCEPT_CHARSET, CharsetHeaderValue.parse(acceptCharset));
            }
        };
    }

    private MediaType mediaType(final String charset) {
        return MediaType.parse("text/*;charset=" + charset);
    }

    @Override
    protected Class<AutoTextEncodingHttpResponse> type() {
        return AutoTextEncodingHttpResponse.class;
    }
}
