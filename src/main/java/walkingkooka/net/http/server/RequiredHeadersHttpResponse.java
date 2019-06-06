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

import walkingkooka.NeverError;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpStatusCodeCategory;

/**
 * A {@link HttpResponse} wrapper that fails with a bad request if required headers are absent.
 * <a href="https://en.wikipedia.org/wiki/List_of_HTTP_header_fields"></a>
 * <pre>
 * Mandatory since HTTP/1.1.[16] If the request is generated directly in HTTP/2, it should not be used.[17]
 * </pre>
 */
final class RequiredHeadersHttpResponse extends BufferingHttpResponse {

    /**
     * Conditionally creates a {@link RequiredHeadersHttpResponse} if the request has a range header.
     */
    static HttpResponse with(final HttpRequest request,
                             final HttpResponse response) {
        check(request);
        check(response);

        return response instanceof RequiredHeadersHttpResponse || request.protocolVersion() != HttpProtocolVersion.VERSION_1_1 ?
                response :
                new RequiredHeadersHttpResponse(response);
    }

    /**
     * Private ctor use factory
     */
    private RequiredHeadersHttpResponse(final HttpResponse response) {
        super(response);
    }

    @Override
    void addEntity(final HttpStatus status,
                   final HttpEntity entity) {
        final HttpResponse response = this.response;

        if (0 == this.entityCount++) {
            final HttpStatusCodeCategory codeCategory = status.value().category();
            switch (codeCategory) {
                case SUCCESSFUL:
                case REDIRECTION:
                    if (this.isServerPresent(entity)) {
                        response.setStatus(status);
                        response.addEntity(entity);
                    } else {
                        response.setStatus(HttpStatusCode.INTERNAL_SERVER_ERROR.status());
                        this.committed = true;
                    }
                    break;
                case INFORMATION:
                case CLIENT_ERROR:
                case SERVER_ERROR:
                    response.setStatus(status);
                    response.addEntity(entity);
                    break;
                default:
                    NeverError.unhandledCase(codeCategory, HttpStatusCodeCategory.values());
            }
        }
    }

    private int entityCount;

    private boolean isServerPresent(final HttpEntity entity) {
        return HttpHeaderName.SERVER.headerValue(entity.headers()).isPresent();
    }
}
