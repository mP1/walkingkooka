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

import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.compare.RangeBound;
import walkingkooka.net.header.ContentRange;
import walkingkooka.net.header.ETag;
import walkingkooka.net.header.IfRange;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeBoundary;
import walkingkooka.net.header.RangeHeaderValue;
import walkingkooka.net.header.RangeHeaderValueUnit;
import walkingkooka.net.http.HasHeaders;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpStatusCodeCategory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A {@link HttpResponse} wrapper that honours any range header ranges for 2xx responses, creating multi parts for each range.
 */
final class RangeAwareHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link RangeAwareHttpResponse} if the request has a range header.
     */
    static HttpResponse with(final HttpRequest request,
                             final HttpResponse response,
                             final Supplier<Byte> boundaryCharacters) {
        check(request);
        check(response);
        Objects.requireNonNull(boundaryCharacters, "boundaryCharacters");

        HttpResponse result = response;

        final Map<HttpHeaderName<?>, Object> requestHeaders = request.headers();

        // range must be absent
        final Optional<RangeHeaderValue> maybeRange = HttpHeaderName.RANGE.headerValue(requestHeaders);
        if (maybeRange.isPresent()) {
            result = new RangeAwareHttpResponse(response,
                    maybeRange.get(),
                    HttpHeaderName.IF_RANGE.headerValue(requestHeaders).orElse(null),
                    boundaryCharacters);
        }

        return result;
    }

    /**
     * Private ctor use factory
     */
    private RangeAwareHttpResponse(final HttpResponse response,
                                   final RangeHeaderValue range,
                                   final IfRange<?> ifRange,
                                   final Supplier<Byte> boundaryCharacters) {
        super(response);
        this.range = range;
        this.ifRange = ifRange;
        this.boundaryCharacters = boundaryCharacters;
    }

    @Override
    void addEntity(final HttpStatus status,
                   final HttpEntity entity) {

        // only range chunk 2xx responses...
        if (status.value().category() == HttpStatusCodeCategory.SUCCESSFUL) {

            // if-range is absent
            // if-range etag is satisifed by response etag
            // if-range/lastmodified == response.lastmodified
            final IfRange<?> ifRange = this.ifRange;
            if (null == ifRange ||
                    ifRange.isETag() && this.isETagSatisified(ifRange, entity) ||
                    ifRange.isLastModified() && this.isLastModifiedSatisified(ifRange, entity)) {

                final byte[] body = entity.body();
                if (this.canSatisfyRange(body.length)) {
                    this.addMultipartEntities(entity);
                } else {
                    this.response.setStatus(HttpStatusCode.REQUESTED_RANGE_NOT_SATISFIABLE.status());
                    this.response.addEntity(entity.setBody(HttpEntity.NO_BODY)); // need to remove content-headers.
                }
            } else {
                this.response.setStatus(status);
                this.response.addEntity(entity);
            }
        } else {
            this.response.setStatus(status);
            this.response.addEntity(entity);
        }
    }

    private boolean isETagSatisified(final IfRange<?> ifRange,
                                     final HasHeaders response) {
        final Optional<ETag> etag = HttpHeaderName.E_TAG.headerValue(response.headers());
        return etag.isPresent() &&
                ifRange.etag().value().isMatch(etag.get());
    }

    private boolean isLastModifiedSatisified(final IfRange<?> ifRange,
                                             final HasHeaders response) {
        final Optional<LocalDateTime> lastModified = HttpHeaderName.LAST_MODIFIED.headerValue(response.headers());
        return lastModified.isPresent() &&
                ifRange.lastModified().value().equals(lastModified.get());
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
     * ~# curl -s -D - -H "Range: bytes=100-200, 300-400" http://www.example.com
     * HTTP/1.1 206 Partial Content
     * Accept-Ranges: bytes
     * Content-Type: multipart/byteranges; boundary=3d6b6a416f9b5
     * Content-Length: 385
     * Server: ECS (fll/0761)
     * <p>
     * <p>
     * --3d6b6a416f9b5
     * Content-Type: text/html
     * Content-Range: bytes 100-200/1270
     * <p>
     * eta http-equiv="Content-type" content="text/html; charset=utf-8" />
     * <meta name="vieport" content
     * --3d6b6a416f9b5
     * Content-Type: text/html
     * Content-Range: bytes 300-400/1270
     * <p>
     * -color: #f0f0f2;
     * margin: 0;
     * padding: 0;
     * font-family: "Open Sans", "Helvetica
     * --3d6b6a416f9b5--
     */
    private void addMultipartEntities(final HttpEntity entity) {
        final byte[] body = entity.body();
        final Optional<Long> contentLength = Optional.of(Long.valueOf(body.length));

        final MediaTypeBoundary boundary = MediaTypeBoundary.generate(body, this.boundaryCharacters);

        this.response.setStatus(HttpStatusCode.PARTIAL_CONTENT.status());
        this.response.addEntity(
                entity.removeHeader(HttpHeaderName.CONTENT_TYPE)
                        .addHeader(HttpHeaderName.CONTENT_TYPE, boundary.multipartByteRanges())
                .setBody(HttpEntity.NO_BODY)
        );

        final MediaType contentType = HttpHeaderName.CONTENT_TYPE.headerValueOrFail(entity.headers());
        final ContentRange contentRange = ContentRange.with(RangeHeaderValueUnit.BYTES, ContentRange.NO_RANGE, contentLength);

        for (Range<Long> range : this.range.value()) {
            final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
            headers.put(HttpHeaderName.CONTENT_TYPE, contentType);
            headers.put(HttpHeaderName.CONTENT_RANGE, contentRange.setRange(Optional.of(this.replaceUpperBoundsIfWildcard(range, body.length))));

            this.response.addEntity(entity.extractRange(range).setHeaders(headers));
        }
    }

    private Range<Long> replaceUpperBoundsIfWildcard(final Range<Long> range, final long contentLength) {
        return range.upperBound().isAll() ?
                Range.greaterThanEquals(range.lowerBound().value().get()).and(Range.lessThanEquals(contentLength -1)) :
                range;
    }

    /**
     * The requested ranges.
     */
    private final RangeHeaderValue range;

    private final IfRange<?> ifRange;

    private final Supplier<Byte> boundaryCharacters;
}
