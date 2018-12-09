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

import walkingkooka.compare.Range;
import walkingkooka.compare.RangeBound;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpHeaderRange;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpStatusCodeCategory;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link HttpResponse} wrapper that honours any range header ranges for 2xx responses.
 */
final class RangeAwareHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link RangeAwareHttpResponse} if the request has a range header.
     */
    static HttpResponse with(final HttpRequest request,
                             final HttpResponse response) {
        check(request);
        check(response);

        HttpResponse result = response;

        final Map<HttpHeaderName<?>, Object> requestHeaders = request.headers();

        // range must be absent
        final Optional<HttpHeaderRange> maybeRange = HttpHeaderName.RANGE.headerValue(requestHeaders);
        if (maybeRange.isPresent()) {
            result = new RangeAwareHttpResponse(response, maybeRange.get());
        }

        return result;
    }

    /**
     * Private ctor use factory
     */
    private RangeAwareHttpResponse(final HttpResponse response,
                                   final HttpHeaderRange range) {
        super(response);
        this.range = range;
    }

    @Override
    void setBody(final HttpStatus status,
                 final Map<HttpHeaderName<?>, Object> headers,
                 final byte[] body) {
        HttpStatus finalStatus = status;
        byte[] finalBody = body;

        if (status.value().category() == HttpStatusCodeCategory.SUCCESSFUL) {

            if (this.canSatisfyRange(body.length)) {
                finalStatus = HttpStatusCode.PARTIAL_CONTENT.status();
                finalBody = this.partialContent(body);
            } else {
                finalStatus = HttpStatusCode.REQUESTED_RANGE_NOT_SATISFIABLE.status();
            }
        }

        this.response.setStatus(finalStatus);
        this.copyHeaders(headers);
        this.response.setBody(finalBody);
    }

    /**
     * Will return true if all ranges address bytes within 0 and the content length.
     */
    private boolean canSatisfyRange(final int contentLength) {
        return this.range.value()
                .stream()
                .allMatch(r -> this.canSatisfyRange0(contentLength, r));
    }

    private static boolean canSatisfyRange0(final int contentLength, final Range<Long> range) {
        return canSatisfyRange1(contentLength, range.lowerBound()) &&
                canSatisfyRange1(contentLength, range.upperBound());
    }

    private static boolean canSatisfyRange1(final int contentLength, final RangeBound<Long> bound) {
        return bound.isAll() || bound.value().get() <= contentLength;
    }

    /**
     * Copy the requested ranges in the header to the target, even if the ranges are not in order.
     */
    private byte[] partialContent(final byte[] body) {
        final byte[] target = body.clone();

        int bytesCopied = 0;

        for (Range<Long> range : this.range.value()) {
            final RangeBound<Long> lowerBound = range.lowerBound();
            final int from = lowerBound.isAll() ? 0 : lowerBound.value().get().intValue();

            final RangeBound<Long> upperBound = range.upperBound();
            final int to = upperBound.isAll() ? body.length : 1 + upperBound.value().get().intValue();

            final int copyCount = to - from;

            System.arraycopy(body, from, target, bytesCopied, copyCount);

            bytesCopied += copyCount;
        }

        return Arrays.copyOfRange(target, 0, bytesCopied);
    }

    /**
     * The requested ranges.
     */
    private final HttpHeaderRange range;

    /**
     * Always throws a {@link UnsupportedOperationException}, assumes that all text should have been converted to bytes.
     */
    @Override
    void setBodyText(final HttpStatus status,
                     final Map<HttpHeaderName<?>, Object> headers,
                     final String bodyText) {
        throw new UnsupportedOperationException();
    }
}
