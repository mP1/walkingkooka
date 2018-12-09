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
import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpETag;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public final class LastModifiedAwareHttpResponseTest extends BufferingHttpResponseTestCase<LastModifiedAwareHttpResponse> {

    private final static List<HttpETag> IF_NONE_MATCH_ABSENT = null;

    private final static LocalDateTime LAST_MODIFIED = LocalDateTime.of(2000, 12, 31, 6, 28, 29);
    private final static LocalDateTime LAST_MODIFIED_ABSENT = null;

    private final static byte[] BODY = new byte[]{1, 2, 3, 4};
    private final static byte[] NO_BODY = null;

    private final static String BODY_TEXT = "<html><body>hello</body></html>";
    private final static String NO_BODY_TEXT = null;

    private final static String SERVER = "Server 123";

    // test.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithRequestNullFails() {
        LastModifiedAwareHttpResponse.with(null,
                HttpResponses.fake());
    }

    @Test
    public void testWithDelete() {
        this.withAndNotWrappedCheck(HttpMethod.DELETE,
                null,
                null);
    }

    @Test
    public void testWithPost() {
        this.withAndNotWrappedCheck(HttpMethod.POST,
                null,
                null);
    }

    @Test
    public void testWithPut() {
        this.withAndNotWrappedCheck(HttpMethod.PUT,
                null,
                null);
    }

    @Test
    public void testWithIfNoneMatch() {
        this.withAndNotWrappedCheck(HttpMethod.GET,
                this.etag(),
                null);
    }

    @Test
    public void testWithIfNoneMatchRequestLastModified() {
        this.withAndNotWrappedCheck(HttpMethod.GET,
                this.etag(),
                LAST_MODIFIED);
    }

    @Test
    public void testWithIfLastModifiedAbsent() {
        this.withAndNotWrappedCheck(HttpMethod.GET,
                IF_NONE_MATCH_ABSENT,
                LAST_MODIFIED_ABSENT);
    }

    private List<HttpETag> etag() {
        return Lists.of(HttpETag.wildcard());
    }

    private void withAndNotWrappedCheck(final HttpMethod method,
                                        final List<HttpETag> ifNoneMatch,
                                        final LocalDateTime lastModified) {
        final HttpResponse response = HttpResponses.fake();
        assertSame("method=" + method + " should have resulted in the response not being wrapped",
                response,
                this.createResponseWithoutCast(method,
                        ifNoneMatch,
                        lastModified,
                        response));
    }

    // server response body status code.........................................................................

    @Test
    public void testStatusContinueBody() {
        this.statusBodyAndCheck(HttpStatusCode.CONTINUE);
    }

    @Test
    public void testStatusRedirectBody() {
        this.statusBodyAndCheck(HttpStatusCode.TEMPORARY_REDIRECT);
    }

    @Test
    public void testStatusClientErrorBody() {
        this.statusBodyAndCheck(HttpStatusCode.BAD_REQUEST);
    }

    @Test
    public void testStatusServerErrorBody() {
        this.statusBodyAndCheck(HttpStatusCode.SERVICE_UNAVAILABLE);
    }

    private void statusBodyAndCheck(final HttpStatusCode status) {
        this.makeResponseAndCheck(status,
                this.headersWithContentHeaders(LAST_MODIFIED_ABSENT),
                BODY,
                NO_BODY_TEXT);
    }

    // server response body text status code.........................................................................

    @Test
    public void testStatusContinueBodyText() {
        this.statusBodyTextAndCheck(HttpStatusCode.CONTINUE);
    }

    @Test
    public void testStatusRedirectBodyText() {
        this.statusBodyTextAndCheck(HttpStatusCode.TEMPORARY_REDIRECT);
    }

    @Test
    public void testStatusClientErrorBodyText() {
        this.statusBodyTextAndCheck(HttpStatusCode.BAD_REQUEST);
    }

    @Test
    public void testStatusServerErrorBodyText() {
        this.statusBodyTextAndCheck(HttpStatusCode.SERVICE_UNAVAILABLE);
    }

    private void statusBodyTextAndCheck(final HttpStatusCode status) {
        this.makeResponseAndCheck(status,
                this.headersWithContentHeaders(LAST_MODIFIED_ABSENT),
                NO_BODY,
                BODY_TEXT);
    }

    // server last modified incorrect last modified body..............................................................

    private final static LocalDateTime NO_LAST_MODIFIED = null;

    @Test
    public void testStatusOkMissingLastModifiedBody() {
        this.serverOkLastModifiedBodyAndCheck(NO_LAST_MODIFIED);
    }

    @Test
    public void testStatusOkLastModifiedAfterRequestLastModifiedBody() {
        this.serverOkLastModifiedBodyAndCheck(LAST_MODIFIED.plusYears(1));
    }

    private void serverOkLastModifiedBodyAndCheck(final LocalDateTime lastModified) {
        this.responseOkLastModifiedAndCheck(lastModified,
                BODY,
                NO_BODY_TEXT);
    }

    @Test
    public void testStatusOkMissingLastModifiedBodyText() {
        this.serverOkLastModifiedBodyTextAndCheck(NO_LAST_MODIFIED);
    }

    @Test
    public void testStatusOkLastModifiedAfterRequestLastModifiedBodyText() {
        this.serverOkLastModifiedBodyTextAndCheck(LAST_MODIFIED.plusYears(1));
    }

    private void serverOkLastModifiedBodyTextAndCheck(final LocalDateTime lastModified) {
        this.responseOkLastModifiedAndCheck(lastModified,
                BODY,
                NO_BODY_TEXT);
    }

    private void responseOkLastModifiedAndCheck(final LocalDateTime lastModified,
                                                final byte[] body,
                                                final String bodyText) {
        this.makeResponseAndCheck(HttpStatusCode.OK,
                this.headersWithContentHeaders(lastModified),
                body,
                bodyText);
    }

    // response not modified ...........................................................

    @Test
    public void testStatusOkLastModifiedRequestLastModifiedBeforeBody() {
        this.responseOkLastModifiedAndNotModifiedCheckBody(LAST_MODIFIED.minusYears(1));
    }

    @Test
    public void testStatusOkLastModifiedRequestLastModifiedBody() {
        this.responseOkLastModifiedAndNotModifiedCheckBody(LAST_MODIFIED);
    }

    private void responseOkLastModifiedAndNotModifiedCheckBody(final LocalDateTime lastModified) {
        this.responseOkLastModifiedAndNotModifiedCheck(lastModified,
                BODY,
                NO_BODY_TEXT);
    }

    @Test
    public void testStatusOkLastModifiedRequestLastModifiedBeforeBodyText() {
        this.responseOkLastModifiedAndNotModifiedCheckBodyText(LAST_MODIFIED.minusYears(1));
    }

    @Test
    public void testStatusOkLastModifiedRequestLastModifiedBodyText() {
        this.responseOkLastModifiedAndNotModifiedCheckBodyText(LAST_MODIFIED);
    }

    private void responseOkLastModifiedAndNotModifiedCheckBodyText(final LocalDateTime lastModified) {
        this.responseOkLastModifiedAndNotModifiedCheck(lastModified,
                NO_BODY,
                BODY_TEXT);
    }

    private void responseOkLastModifiedAndNotModifiedCheck(final LocalDateTime lastModified,
                                                           final byte[] body,
                                                           final String bodyText) {
        this.makeResponseAndCheck(HttpStatusCode.OK,
                this.headersWithContentHeaders(lastModified),
                body,
                bodyText,
                HttpStatusCode.NOT_MODIFIED,
                this.headers(lastModified),
                NO_BODY,
                NO_BODY_TEXT);
    }

    // helpers.........................................................................................

    private Map<HttpHeaderName<?>, Object> headers(final LocalDateTime lastModified) {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.SERVER, SERVER);
        if (null != lastModified) {
            headers.put(HttpHeaderName.LAST_MODIFIED, lastModified);
        }
        return headers;
    }

    private Map<HttpHeaderName<?>, Object> headersWithContentHeaders(final LocalDateTime lastModified) {
        final Map<HttpHeaderName<?>, Object> headers = this.headers(lastModified);
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.ANY_TEXT);
        headers.put(HttpHeaderName.CONTENT_LENGTH, 123L);
        return headers;
    }

    private void makeResponseAndCheck(final HttpStatusCode status,
                                      final Map<HttpHeaderName<?>, Object> headers,
                                      final byte[] body,
                                      final String bodyText) {
        this.makeResponseAndCheck(status, headers, body, bodyText, status, headers, body, bodyText);
    }

    private void makeResponseAndCheck(final HttpStatusCode status,
                                      final Map<HttpHeaderName<?>, Object> headers,
                                      final byte[] body,
                                      final String bodyText,
                                      final HttpStatusCode expectedStatus,
                                      final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                      final byte[] expectedBody,
                                      final String expectedBodyText) {
        final TestHttpResponse wrapped = new TestHttpResponse();
        final HttpRequest request = this.request(HttpMethod.GET,
                IF_NONE_MATCH_ABSENT,
                LAST_MODIFIED);
        final HttpResponse response = LastModifiedAwareHttpResponse.with(request, wrapped);
        response.setStatus(status.status());
        headers.entrySet()
                .stream()
                .forEach(headerAndValue -> response.addHeader(Cast.to(headerAndValue.getKey()), headerAndValue.getValue()));

        if (null != body) {
            response.setBody(body);
            wrapped.check(request, expectedStatus.status(), expectedHeaders, expectedBody);
        }
        if (null != bodyText) {
            response.setBodyText(bodyText);
            wrapped.check(request, expectedStatus.status(), expectedHeaders, expectedBodyText);
        }
    }

    /**
     * A response that records all parameters set up on it for later verification.
     */
    static private class TestHttpResponse extends FakeHttpResponse {

        TestHttpResponse() {
            super();
            this.headers = Maps.ordered();
        }

        TestHttpResponse(final HttpStatus status,
                         final Map<HttpHeaderName<?>, Object> headers,
                         final byte[] body,
                         final String bodyText) {
            super();
            this.status = status;
            this.headers = headers;
            this.body = body;
            this.bodyText = bodyText;
        }

        @Override
        public void setStatus(final HttpStatus status) {
            this.status = status;
        }

        HttpStatus status;

        @Override
        public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
            this.headers.put(name, value);
        }

        final Map<HttpHeaderName<?>, Object> headers;

        @Override
        public void setBody(final byte[] body) {
            this.body = body;
        }

        byte[] body;

        @Override
        public void setBodyText(final String bodyText) {
            this.bodyText = bodyText;
        }

        String bodyText;

        void check(final HttpRequest request,
                   final HttpStatus status,
                   final Map<HttpHeaderName<?>, Object> headers,
                   final byte[] body) {
            this.check(request, status, headers, body, null);
        }

        void check(final HttpRequest request,
                   final HttpStatus status,
                   final Map<HttpHeaderName<?>, Object> headers,
                   final String bodyText) {
            this.check(request, status, headers, null, bodyText);
        }

        void check(final HttpRequest request,
                   final HttpStatus status,
                   final Map<HttpHeaderName<?>, Object> headers,
                   final byte[] body,
                   final String bodyText) {
            assertEquals(request.toString(),
                    new TestHttpResponse(status, headers, body, bodyText),
                    this);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.status, this.headers, this.body, this.bodyText);
        }

        public boolean equals(final Object other) {
            return this == other || other instanceof TestHttpResponse && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestHttpResponse other) {
            return Objects.equals(this.status, other.status) &&
                    Objects.equals(this.headers, other.headers) &&
                    Objects.equals(this.body, other.body) &&
                    Objects.equals(this.bodyText, other.bodyText);
        }

        @Override
        public String toString() {
            return ToStringBuilder.create()
                    .value(this.status)
                    .value(this.headers)
                    .value(this.body)
                    .value(this.bodyText)
                    .build();
        }
    }

    @Override
    LastModifiedAwareHttpResponse createResponse(final HttpResponse response) {
        return this.createResponse(HttpMethod.GET,
                IF_NONE_MATCH_ABSENT,
                LAST_MODIFIED,
                response);
    }

    private LastModifiedAwareHttpResponse createResponse(final HttpMethod method,
                                                         final List<HttpETag> ifNoneMatch,
                                                         final LocalDateTime lastModified,
                                                         final HttpResponse response) {
        return Cast.to(this.createResponse(
                request(method, ifNoneMatch, lastModified),
                response));
    }

    private LastModifiedAwareHttpResponse createResponse(final HttpRequest request,
                                                         final HttpResponse response) {
        return Cast.to(LastModifiedAwareHttpResponse.with(request, response));
    }


    private static HttpResponse createResponseWithoutCast(final HttpMethod method,
                                                          final List<HttpETag> ifNoneMatch,
                                                          final LocalDateTime lastModified,
                                                          final HttpResponse response) {
        return LastModifiedAwareHttpResponse.with(
                request(method, ifNoneMatch, lastModified),
                response);
    }

    private static HttpRequest request(final HttpMethod method,
                                       final List<HttpETag> ifNoneMatch,
                                       final LocalDateTime lastModified) {
        assertNotNull("method", method);

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        if (null != ifNoneMatch) {
            headers.put(HttpHeaderName.IF_NONE_MATCHED, ifNoneMatch);
        }
        if (null != lastModified) {
            headers.put(HttpHeaderName.LAST_MODIFIED, lastModified);
        }

        return new FakeHttpRequest() {
            @Override
            public HttpMethod method() {
                return method;
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return Maps.readOnly(headers);
            }

            @Override
            public String toString() {
                return this.method() + " " + this.headers();
            }

        };
    }

    @Override
    protected Class<LastModifiedAwareHttpResponse> type() {
        return LastModifiedAwareHttpResponse.class;
    }
}
