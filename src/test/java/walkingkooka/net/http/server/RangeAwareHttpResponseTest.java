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
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpHeaderRange;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.cookie.Cookie;

import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class RangeAwareHttpResponseTest extends BufferingHttpResponseTestCase<RangeAwareHttpResponse> {

    @Test(expected = NullPointerException.class)
    public void testWithNullRequestFails() {
        RangeAwareHttpResponse.with(null, HttpResponses.fake());
    }

    @Test
    public void testWithoutRangeHeaderNotWrapped() {
        final HttpResponse response = HttpResponses.fake();
        assertSame(response, this.createResponseWithoutCast(null, response));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetBodyTextFails() {
        final HttpResponse response = this.createResponse(new FakeHttpResponse(){

            @Override
            public void setStatus(HttpStatus status) {
                // nop
            }

            @Override
            public void setBodyText(String body) {
                fail(RangeAwareHttpResponse.class.getSimpleName() + ".setBodyText should have thrown UOE");
            }
        });
        response.setStatus(HttpStatusCode.OK.status());
        response.setBodyText("Body123");
    }

    @Test
    public void testUnsatisfiableRanges() {
        this.makeUnsatisfiableRangesAndCheck("bytes=1-200");
    }

    @Test
    public void testUnsatisfiableRanges2() {
        this.makeUnsatisfiableRangesAndCheck("bytes=1-2,4-5,99-");
    }

    @Test
    public void testUnsatisfiableRanges3() {
        this.makeUnsatisfiableRangesAndCheck("bytes=1-2,99-150");
    }

    private void makeUnsatisfiableRangesAndCheck(final String ranges) {
        final byte[] body = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        this.makeResponseAndCheck(ranges, HttpStatusCode.OK, body, HttpStatusCode.REQUESTED_RANGE_NOT_SATISFIABLE, body);
    }

    @Test
    public void testOneRange() {
        this.makeSatisfiableRangesAndCheck("bytes=3-5", 3, 4, 5);
    }

    @Test
    public void testOneRange2() {
        this.makeSatisfiableRangesAndCheck("bytes=1-5", 1, 2, 3, 4, 5);
    }

    @Test
    public void testOneRange3() {
        this.makeSatisfiableRangesAndCheck("bytes=15-20", 15, 16, 17, 18, 19, 20);
    }

    @Test
    public void testRangeOpenEnd() {
        this.makeSatisfiableRangesAndCheck("bytes=15-", 15, 16, 17, 18, 19, 20);
    }

    @Test
    public void testSeveralRanges() {
        this.makeSatisfiableRangesAndCheck("bytes=1-2,5-6,10-12", 1, 2, 5, 6, 10, 11, 12);
    }

    @Test
    public void testSeveralRanges2() {
        this.makeSatisfiableRangesAndCheck("bytes=1-2,5-6,10-12,17-", 1, 2, 5, 6, 10, 11, 12, 17, 18, 19, 20);
    }

    private void makeSatisfiableRangesAndCheck(final String ranges,
                                               final int... expectedBody) {
        final byte[] expected = new byte[expectedBody.length];
        for (int i = 0; i < expectedBody.length; i++) {
            expected[i] = (byte) expectedBody[i];
        }

        this.makeResponseAndCheck(ranges,
                HttpStatusCode.OK,
                new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
                HttpStatusCode.PARTIAL_CONTENT,
                expected);
    }

    private void makeResponseAndCheck(final String ranges,
                                      final HttpStatusCode status,
                                      final byte[] body,
                                      final HttpStatusCode expectedStatus,
                                      final byte[] expectedBody) {
        final TestHttpResponse wrapped = new TestHttpResponse();
        final HttpRequest request = this.request(ranges);
        final HttpResponse response = RangeAwareHttpResponse.with(request, wrapped);
        response.setStatus(status.status());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.ANY_TEXT);
        headers.put(HttpHeaderName.CONTENT_LENGTH, 123L);
        headers.put(HttpHeaderName.SET_COOKIE, Cookie.parseServerHeader("cookie123=456,cookie789=abc;"));

        headers.entrySet()
                .stream()
                .forEach(headerAndValue -> response.addHeader(Cast.to(headerAndValue.getKey()), headerAndValue.getValue()));
        response.setBody(body);

        wrapped.check(request, expectedStatus.status(), headers, expectedBody);
    }

    @Override
    RangeAwareHttpResponse createResponse(final HttpResponse response) {
        return this.createResponse("bytes=1-200", response);
    }

    private RangeAwareHttpResponse createResponse(final String ranges, final HttpResponse response) {
        return Cast.to(this.createResponseWithoutCast(ranges, response));
    }

    private HttpResponse createResponseWithoutCast(final String ranges, final HttpResponse response) {
        return RangeAwareHttpResponse.with(this.request(ranges), response);
    }

    private HttpRequest request(final String ranges) {
        return new FakeHttpRequest() {
            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return null != ranges ?
                        Maps.one(HttpHeaderName.RANGE, HttpHeaderRange.parse(ranges)) :
                        Maps.empty();
            }

            @Override
            public String toString() {
                return this.headers().toString();
            }
        };
    }

    @Override
    protected Class<RangeAwareHttpResponse> type() {
        return RangeAwareHttpResponse.class;
    }
}
