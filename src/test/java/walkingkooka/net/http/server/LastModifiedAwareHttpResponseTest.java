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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpETag;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public final class LastModifiedAwareHttpResponseTest extends BufferingHttpResponseTestCase<LastModifiedAwareHttpResponse> {

    private final static List<HttpETag> IF_NONE_MATCH_ABSENT = null;

    private final static LocalDateTime LAST_MODIFIED = LocalDateTime.of(2000, 12, 31, 6, 28, 29);
    private final static LocalDateTime LAST_MODIFIED_ABSENT = null;

    private final static byte[] BODY = new byte[]{1, 2, 3, 4};
    private final static byte[] NO_BODY = new byte[0];

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
        this.setStatusLastModifiedAbsentBodyAndCheck(HttpStatusCode.CONTINUE);
    }

    @Test
    public void testStatusRedirectBody() {
        this.setStatusLastModifiedAbsentBodyAndCheck(HttpStatusCode.TEMPORARY_REDIRECT);
    }

    @Test
    public void testStatusClientErrorBody() {
        this.setStatusLastModifiedAbsentBodyAndCheck(HttpStatusCode.BAD_REQUEST);
    }

    @Test
    public void testStatusServerErrorBody() {
        this.setStatusLastModifiedAbsentBodyAndCheck(HttpStatusCode.SERVICE_UNAVAILABLE);
    }

    private void setStatusLastModifiedAbsentBodyAndCheck(final HttpStatusCode status) {
        this.setStatusAddEntityAndCheck(status,
                this.headersWithContentHeaders(LAST_MODIFIED_ABSENT),
                BODY);
    }

    // server last modified incorrect last modified body..............................................................

    private final static LocalDateTime NO_LAST_MODIFIED = null;

    @Test
    public void testStatusOkMissingLastModifiedBody() {
        this.setStatusOkLastModifiedBodyAndCheck(NO_LAST_MODIFIED);
    }

    @Test
    public void testStatusOkLastModifiedAfterRequestLastModifiedBody() {
        this.setStatusOkLastModifiedBodyAndCheck(LAST_MODIFIED.plusYears(1));
    }

    private void setStatusOkLastModifiedBodyAndCheck(final LocalDateTime lastModified) {
        this.setStatusOkLastModifiedAndCheck(lastModified, BODY);
    }

    private void setStatusOkLastModifiedAndCheck(final LocalDateTime lastModified,
                                                 final byte[] body) {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK,
                this.headersWithContentHeaders(lastModified),
                body);
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
                BODY);
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
                NO_BODY);
    }

    private void responseOkLastModifiedAndNotModifiedCheck(final LocalDateTime lastModified,
                                                           final byte[] body) {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK,
                this.headersWithContentHeaders(lastModified),
                body,
                HttpStatusCode.NOT_MODIFIED,
                this.headers(lastModified),
                body);
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

    private void setStatusAddEntityAndCheck(final HttpStatusCode status,
                                            final Map<HttpHeaderName<?>, Object> headers,
                                            final byte[] body) {
        this.setStatusAddEntityAndCheck(status, headers, body, status, headers, body);
    }

    private void setStatusAddEntityAndCheck(final HttpStatusCode status,
                                            final Map<HttpHeaderName<?>, Object> headers,
                                            final byte[] body,
                                            final HttpStatusCode expectedStatus,
                                            final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                            final byte[] expectedBody) {
        this.setStatusAddEntityAndCheck(
                createRequest(),
                status.status(),
                HttpEntity.with(headers, body),
                expectedStatus.status(),
                HttpEntity.with(expectedHeaders, expectedBody));
    }

    @Override
    LastModifiedAwareHttpResponse createResponse(final HttpResponse response) {
        return this.createResponse(this.createRequest(), response);
    }

    @Override
    LastModifiedAwareHttpResponse createResponse(final HttpRequest request,
                                                 final HttpResponse response) {
        return Cast.to(LastModifiedAwareHttpResponse.with(request, response));
    }


    private HttpResponse createResponseWithoutCast(final HttpMethod method,
                                                          final List<HttpETag> ifNoneMatch,
                                                          final LocalDateTime lastModified,
                                                          final HttpResponse response) {
        return LastModifiedAwareHttpResponse.with(
                createRequest(method, ifNoneMatch, lastModified),
                response);
    }

    @Override
    HttpRequest createRequest() {
        return this.createRequest(HttpMethod.GET,
                IF_NONE_MATCH_ABSENT,
                LAST_MODIFIED);
    }

    private HttpRequest createRequest(final HttpMethod method,
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
