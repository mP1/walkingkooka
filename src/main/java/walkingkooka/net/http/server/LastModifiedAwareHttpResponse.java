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

import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpStatusCodeCategory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * A {link HttpResponse} wrapper that sends a {@link HttpStatusCode#NOT_MODIFIED} if the request is a GET or HEAD,
 * without a if-none-matched header, and the response last-modified is before or equal to the request last-modified.
 */
final class LastModifiedAwareHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link LastModifiedAwareHttpResponse} if the request was a GET or HEAD and
     * the {@link HttpHeaderName#IF_NONE_MATCHED} is absent and the {@link HttpHeaderName#LAST_MODIFIED} is present.
     */
    static HttpResponse with(final HttpRequest request,
                             final HttpResponse response) {
        check(request);
        check(response);

        HttpResponse result = response;

        if (request.method().isGetOrHead()) {
            final Map<HttpHeaderName<?>, Object> requestHeaders = request.headers();

            // if-none-matched must be absent

            if (!HttpHeaderName.IF_NONE_MATCHED.headerValue(requestHeaders).isPresent()) {
                final Optional<LocalDateTime> maybeLastModified = HttpHeaderName.LAST_MODIFIED.headerValue(requestHeaders);
                if (maybeLastModified.isPresent()) {
                    result = new LastModifiedAwareHttpResponse(response, maybeLastModified.get());
                }
            }
        }

        return result;
    }

    /**
     * Private ctor use factory
     */
    private LastModifiedAwareHttpResponse(final HttpResponse response,
                                          final LocalDateTime lastModified) {
        super(response);
        this.lastModified = lastModified;
    }

    /**
     * If the response status code is a successful, and the last modified in the response is less than or equal to
     * the request last modified, then a {@linkl HttpStatusCode#NO_CONTENT} is sent instead and TRUE returned.
     */
    @Override
    void addEntity(final HttpStatus status,
                   final HttpEntity entity) {
        HttpStatus finalStatus = status;
        HttpEntity addEntity = entity;

        //boolean notModified = false;

        if (status.value().category() == HttpStatusCodeCategory.SUCCESSFUL) {
            final Optional<LocalDateTime> maybeLastModified = HttpHeaderName.LAST_MODIFIED.headerValue(entity.headers());
            if (maybeLastModified.isPresent()) {
                final LocalDateTime lastModified = maybeLastModified.get();
                if (lastModified.compareTo(this.lastModified) <= 0) {
                    finalStatus = HttpStatusCode.NOT_MODIFIED.status();
                    addEntity = this.removeContentHeaders(entity);
                }
            }
        }

        this.response.setStatus(finalStatus);
        this.response.addEntity(addEntity);
    }

    /**
     * The request last modified.
     */
    private final LocalDateTime lastModified;
}
