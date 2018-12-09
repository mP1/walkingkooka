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

import walkingkooka.net.http.HttpETag;
import walkingkooka.net.http.HttpETagValidator;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpStatusCodeCategory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link HttpResponse} wrapper that computes the etag for the body and compares that against the if-none-match
 * header if the response status code is 2xx and the request method a GET or HEAD, and if the test is true a
 * NOT_MODIFIED without body is sent.
 */
final class IfNoneMatchAwareHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link IfNoneMatchAwareHttpResponse} if the request was a GET or HEAD and
     * the {@link HttpHeaderName#IF_NONE_MATCHED} is present.
     */
    static HttpResponse with(final HttpRequest request,
                             final HttpResponse response,
                             final Function<byte[], HttpETag> computer) {
        check(request);
        check(response);
        Objects.requireNonNull(computer, "computer");

        HttpResponse result = response;

        final HttpMethod method = request.method();
        if (HttpMethod.GET == method || HttpMethod.HEAD == method) {
            final Map<HttpHeaderName<?>, Object> requestHeaders = request.headers();

            // if-none-matched must be absent
            final Optional<List<HttpETag>> maybeIfNoneMatch = HttpHeaderName.IF_NONE_MATCHED.headerValue(requestHeaders);
            if (maybeIfNoneMatch.isPresent()) {
                final List<HttpETag> ifNoneMatch = maybeIfNoneMatch.get()
                        .stream()
                        .filter(e -> e.validator() == HttpETagValidator.STRONG)
                        .collect(Collectors.toList());
                if (!ifNoneMatch.isEmpty()) {
                    result = new IfNoneMatchAwareHttpResponse(response, ifNoneMatch, computer);
                }
            }
        }

        return result;
    }

    /**
     * Private ctor use factory
     */
    private IfNoneMatchAwareHttpResponse(final HttpResponse response,
                                         final List<HttpETag> ifNoneMatch,
                                         final Function<byte[], HttpETag> computer) {
        super(response);
        this.ifNoneMatch = ifNoneMatch;
        this.computer = computer;
    }

    @Override
    void setBody(final HttpStatus status,
                 final Map<HttpHeaderName<?>, Object> headers,
                 final byte[] body) {
        boolean copyAll = true;

        HttpETag etag = null;
        if (status.value().category() == HttpStatusCodeCategory.SUCCESSFUL) {
            etag = contentETag(body);
            // if-modified-since should be evaluated first and if successful the status would not be 2xx.
            if (this.isNotModified(etag)) {
                this.response.setStatus(HttpStatusCode.NOT_MODIFIED.status());
                this.copyHeaders(headers, this.ignoreContentHeaders());
                this.response.addHeader(HttpHeaderName.E_TAG, etag);

                copyAll = false;
            }
        }

        if (copyAll) {
            this.response.setStatus(status);
            this.copyHeaders(headers);
            if (null != etag) {
                this.addHeader(HttpHeaderName.E_TAG, etag);
            }
            this.response.setBody(body);
        }
    }

    /**
     * Lazily computes an e-tag if a header value is not already set.
     */
    private HttpETag contentETag(final byte[] body) {
        final Optional<HttpETag> contentETag = HttpHeaderName.E_TAG.headerValue(this.headers());
        return contentETag.isPresent() ?
                contentETag.get() :
                this.computer.apply(body);
    }

    /**
     * Returns true if the body / content e-tag matches one of the request if-none-match etags.
     */
    private boolean isNotModified(final HttpETag contentETag) {
        return contentETag.validator() == HttpETagValidator.STRONG &&
                this.ifNoneMatch.stream()
                        .anyMatch(e -> e.equals(contentETag));
    }

    /**
     * The STRONG request e-tags.
     */
    private final List<HttpETag> ifNoneMatch;

    private final Function<byte[], HttpETag> computer;

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
