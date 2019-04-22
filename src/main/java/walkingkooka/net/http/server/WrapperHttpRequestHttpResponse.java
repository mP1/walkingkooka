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

/**
 * Base class for any response, that requires a request and wraps a response.
 */
abstract class WrapperHttpRequestHttpResponse extends WrapperHttpResponse {

    static void check(final HttpRequest request,
                      final HttpResponse response) {
        check(request);
        check(response);
    }

    WrapperHttpRequestHttpResponse(final HttpRequest request,
                                   final HttpResponse response) {
        super(response);
        this.request = request;
    }

    final HttpRequest request;
}
