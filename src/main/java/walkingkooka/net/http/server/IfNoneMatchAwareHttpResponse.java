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

import walkingkooka.Binary;
import walkingkooka.net.header.ETag;
import walkingkooka.net.header.ETagValidator;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
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
                             final Function<byte[], ETag> computer) {
        check(request);
        check(response);
        Objects.requireNonNull(computer, "computer");

        HttpResponse result = response;

        if (request.method().isGetOrHead()) {
            final Map<HttpHeaderName<?>, Object> requestHeaders = request.headers();

            // if-none-matched must be absent
            final Optional<List<ETag>> maybeIfNoneMatch = HttpHeaderName.IF_NONE_MATCHED.headerValue(requestHeaders);
            if (maybeIfNoneMatch.isPresent()) {
                final List<ETag> ifNoneMatch = maybeIfNoneMatch.get()
                        .stream()
                        .filter(e -> e.validator() == ETagValidator.STRONG)
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
                                         final List<ETag> ifNoneMatch,
                                         final Function<byte[], ETag> computer) {
        super(response);
        this.ifNoneMatch = ifNoneMatch;
        this.computer = computer;
    }

    @Override
    void addEntity(final HttpStatus status,
                   final HttpEntity entity) {
        HttpStatus finalStatus = status;
        HttpEntity addEntity = entity;

        if (status.value().category() == HttpStatusCodeCategory.SUCCESSFUL) {
            final Binary body = entity.body();
            ETag etag = contentETag(body, entity.headers());

            // if-modified-since should be evaluated first and if successful the status would not be 2xx.
            if (this.isNotModified(etag)) {
                finalStatus = HttpStatusCode.NOT_MODIFIED.status();
                addEntity = this.removeContentHeaders(entity)
                        .addHeader(HttpHeaderName.E_TAG, etag);

            } else {
                addEntity = entity.addHeader(HttpHeaderName.E_TAG, etag);
            }
        }

        this.response.setStatus(finalStatus);
        this.response.addEntity(addEntity);
    }

    /**
     * Lazily computes an e-tag if a header value is not already set.
     */
    private ETag contentETag(final Binary body, final Map<HttpHeaderName<?>, Object> headers) {
        final Optional<ETag> contentETag = HttpHeaderName.E_TAG.headerValue(headers);
        return contentETag.isPresent() ?
                contentETag.get() :
                this.computer.apply(body.value());
    }

    /**
     * Returns true if the body / content e-tag matches one of the request if-none-match etags.
     */
    private boolean isNotModified(final ETag contentETag) {
        return contentETag.validator() == ETagValidator.STRONG &&
                this.ifNoneMatch.stream()
                        .anyMatch(e -> e.equals(contentETag));
    }

    /**
     * The STRONG request e-tags.
     */
    private final List<ETag> ifNoneMatch;

    private final Function<byte[], ETag> computer;
}
