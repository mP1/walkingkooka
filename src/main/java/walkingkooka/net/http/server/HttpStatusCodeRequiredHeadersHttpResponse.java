/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.http.server;

import walkingkooka.Cast;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;

import java.util.Map;

/**
 * A {@link HttpResponse} wrapper that fails if required headers for the set {@link HttpStatusCode} is missing.
 */
final class HttpStatusCodeRequiredHeadersHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link HttpStatusCodeRequiredHeadersHttpResponse} if the request has a range header.
     */
    static HttpStatusCodeRequiredHeadersHttpResponse with(final HttpResponse response) {
        check(response);

        return response instanceof HttpStatusCodeRequiredHeadersHttpResponse ?
                Cast.to(response) :
                new HttpStatusCodeRequiredHeadersHttpResponse(response);
    }

    /**
     * Private ctor use factory
     */
    private HttpStatusCodeRequiredHeadersHttpResponse(final HttpResponse response) {
        super(response);
    }

    @Override
    void addFirstEntity(final HttpStatus status,
                        final HttpEntity entity) {
        final HttpResponse response = this.response;

        final Map<HttpHeaderName<?>, Object> headers = entity.headers();
        this.ignore = false == status.value()
                .requiredHttpHeaders()
                .stream()
                .allMatch(headers::containsKey);
        if (this.ignore) {
            response.setStatus(HttpStatusCode.INTERNAL_SERVER_ERROR.status());
            response.addEntity(HttpEntity.EMPTY);
        } else {
            response.setStatus(status);
            response.addEntity(entity);
        }
    }

    @Override
    void addAdditionalEntity(final HttpEntity entity) {
        if (!this.ignore) {
            this.response.addEntity(entity);
        }
    }

    private boolean ignore;
}
