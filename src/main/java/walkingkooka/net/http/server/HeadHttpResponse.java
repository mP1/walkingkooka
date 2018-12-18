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
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpStatus;

/**
 * If the request was a HEAD, wraps the response which will only accept the first entity and its headers. All further
 * entities will be ignored.
 */
final class HeadHttpResponse extends WrapperHttpRequestHttpResponse {

    static HttpResponse with(final HttpRequest request, final HttpResponse response) {
        check(request, response);

        return request.method() == HttpMethod.HEAD ?
                new HeadHttpResponse(request, response) :
                response;
    }

    private HeadHttpResponse(HttpRequest request, HttpResponse response) {
        super(request, response);
    }

    @Override
    public void setStatus(final HttpStatus status) {
        this.response.setStatus(status);
    }

    @Override
    public void addEntity(final HttpEntity entity) {
        if(this.first) {
            this.first = false;
            this.response.addEntity(entity.setBody(HttpEntity.NO_BODY));
        }
    }

    private boolean first = true;
}
