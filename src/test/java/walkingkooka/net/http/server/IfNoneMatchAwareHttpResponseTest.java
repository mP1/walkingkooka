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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpETag;
import walkingkooka.net.http.HttpETagValidator;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class IfNoneMatchAwareHttpResponseTest extends BufferingHttpResponseTestCase<IfNoneMatchAwareHttpResponse> {

    private final static List<HttpETag> IF_NONE_MATCH_ABSENT = null;
    private final static Function<byte[], HttpETag> COMPUTER = (b) -> HttpETagValidator.STRONG.setValue("X" + b.length);

    private final static byte[] BODY = new byte[]{1, 2, 3, 4};
    private final static byte[] NO_BODY = null;

    private final static String SERVER = "Server 123";
    private final static HttpETag E_TAG_ABSENT = null;

    // test.........................................................................................

    @Test(expected = NullPointerException.class)
    public void testWithRequestNullFails() {
        LastModifiedAwareHttpResponse.with(null,
                HttpResponses.fake());
    }

    @Test
    public void testWithDelete() {
        this.withAndNotWrappedCheck(HttpMethod.DELETE,
                ifNoneMatch());
    }

    @Test
    public void testWithPost() {
        this.withAndNotWrappedCheck(HttpMethod.POST,
                ifNoneMatch());
    }

    @Test
    public void testWithPut() {
        this.withAndNotWrappedCheck(HttpMethod.PUT,
                ifNoneMatch());
    }

    @Test
    public void testWithIfNoneMatchAbsent() {
        this.withAndNotWrappedCheck(HttpMethod.GET,
                IF_NONE_MATCH_ABSENT);
    }

    private void withAndNotWrappedCheck(final HttpMethod method,
                                        final List<HttpETag> ifNoneMatch) {
        final HttpResponse response = HttpResponses.fake();
        assertSame("method=" + method + " should have resulted in the response not being wrapped",
                response,
                this.createResponseWithoutCast(method,
                        ifNoneMatch,
                        response));
    }

    // response setBodyText fails.........................................................................

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBodyTextFails() {
        final IfNoneMatchAwareHttpResponse  response = this.createResponse(new FakeHttpResponse(){
            @Override
            public void setStatus(final HttpStatus status) {

            }

            @Override
            public void setBodyText(final String body) {
                fail("setBodyText should not have been called.");
            }
        });
        response.setStatus(HttpStatusCode.OK.status());
        response.setBodyText("body text");
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
                this.headersWithContentHeaders(E_TAG_ABSENT),
                BODY);
    }

    // incorrect etag.............................................................

    @Test
    public void testStatusOkIfNoneMatchFail() {
        this.responseOkAndCheck(E_TAG_ABSENT,
                this.etag(9),
                new byte[9]);
    }

    @Test
    public void testStatusOkComputedETagFailMatchesWeak() {
        this.responseOkAndCheck(E_TAG_ABSENT,
                this.etag(1),
                new byte[1]);
    }

    @Test
    public void testStatusOkETagIfNoneMatchFail() {
        final HttpETag etag = this.etag(9);
        this.responseOkAndCheck(etag,
                etag,
                new byte[9]);
    }

    private void responseOkAndCheck(final HttpETag etag,
                                               final HttpETag expectedETag,
                                               final byte[] body) {
        this.makeResponseAndCheck(HttpStatusCode.OK,
                this.headersWithContentHeaders(etag),
                body,
                HttpStatusCode.OK,
                this.headersWithContentHeaders(expectedETag),
                body);
    }

    // response  ...............................................................

    @Test
    public void testStatusOkEtagPrecomputedIfNoneMatchMatched() {
        final HttpETag etag = this.etag(2);
        this.responseOkAndNotModifiedCheck(etag,
                etag,
                new byte[2]);
    }

    @Test
    public void testStatusOkEtagIfNoneMatchMatched() {
        this.responseOkAndNotModifiedCheck(E_TAG_ABSENT,
                this.etag(2),
                new byte[2]);
    }

    @Test
    public void testStatusOkEtagIfNoneMatchMatched2() {
        this.responseOkAndNotModifiedCheck(E_TAG_ABSENT,
                this.etag(3),
                new byte[3]);
    }

    private void responseOkAndNotModifiedCheck(final HttpETag etag,
                                                           final HttpETag expectedETag,
                                                           final byte[] body) {
        this.makeResponseAndCheck(HttpStatusCode.OK,
                this.headersWithContentHeaders(etag),
                body,
                HttpStatusCode.NOT_MODIFIED,
                this.headers(expectedETag),
                NO_BODY);
    }

    // helpers.........................................................................................

    private Map<HttpHeaderName<?>, Object> headers(final HttpETag eTag) {
        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.SERVER, SERVER);
        if(null!=eTag) {
            headers.put(HttpHeaderName.E_TAG, eTag);
        }
        return headers;
    }

    private Map<HttpHeaderName<?>, Object> headersWithContentHeaders(final HttpETag eTag) {
        final Map<HttpHeaderName<?>, Object> headers = this.headers(eTag);
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.ANY_TEXT);
        headers.put(HttpHeaderName.CONTENT_LENGTH, 123L);
        return headers;
    }

    private void makeResponseAndCheck(final HttpStatusCode status,
                                      final Map<HttpHeaderName<?>, Object> headers,
                                      final byte[] body) {
        this.makeResponseAndCheck(status, headers, body, status, headers, body);
    }

    private void makeResponseAndCheck(final HttpStatusCode status,
                                      final Map<HttpHeaderName<?>, Object> headers,
                                      final byte[] body,
                                      final HttpStatusCode expectedStatus,
                                      final Map<HttpHeaderName<?>, Object> expectedHeaders,
                                      final byte[] expectedBody) {
        final TestHttpResponse wrapped = new TestHttpResponse();
        final HttpRequest request = this.request(HttpMethod.GET, this.ifNoneMatch());
        final HttpResponse response = IfNoneMatchAwareHttpResponse.with(request, wrapped, COMPUTER);
        assertEquals("not wrapped", IfNoneMatchAwareHttpResponse.class, response.getClass());

        response.setStatus(status.status());
        headers.entrySet()
                .stream()
                .forEach(headerAndValue -> response.addHeader(Cast.to(headerAndValue.getKey()), headerAndValue.getValue()));

        response.setBody(body);
        wrapped.check(request, expectedStatus.status(), expectedHeaders, expectedBody);

    }

    @Override
    IfNoneMatchAwareHttpResponse createResponse(final HttpResponse response) {
        return this.createResponse(HttpMethod.GET,
                this.ifNoneMatch(),
                response);
    }

    private IfNoneMatchAwareHttpResponse createResponse(final HttpMethod method,
                                                         final List<HttpETag> ifNoneMatch,
                                                         final HttpResponse response) {
        return Cast.to(this.createResponse(
                request(method, ifNoneMatch),
                response));
    }

    private IfNoneMatchAwareHttpResponse createResponse(final HttpRequest request,
                                                         final HttpResponse response) {
        return Cast.to(IfNoneMatchAwareHttpResponse.with(request, response, COMPUTER));
    }


    private static HttpResponse createResponseWithoutCast(final HttpMethod method,
                                                          final List<HttpETag> ifNoneMatch,
                                                          final HttpResponse response) {
        return IfNoneMatchAwareHttpResponse.with(
                request(method, ifNoneMatch),
                response,
                COMPUTER);
    }

    private static HttpRequest request(final HttpMethod method,
                                       final List<HttpETag> ifNoneMatch) {
        assertNotNull("method", method);

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        if (null != ifNoneMatch) {
            headers.put(HttpHeaderName.IF_NONE_MATCHED, ifNoneMatch);
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

    private List<HttpETag> ifNoneMatch() {
        return HttpETag.parseList("W/\"X1\",\"X2\",\"X3\"");
    }

    private HttpETag etag(final int value) {
        return HttpETagValidator.STRONG.setValue("X" + value);
    }

    @Override
    protected Class<IfNoneMatchAwareHttpResponse> type() {
        return IfNoneMatchAwareHttpResponse.class;
    }
}
