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

import java.util.Objects;

/**
 * Base class for any response, that wraps another {@link HttpResponse}.
 */
abstract class WrapperHttpResponse implements HttpResponse {

    static void check(final HttpRequest request) {
        Objects.requireNonNull(request, "request");
    }

    static void check(final HttpResponse response) {
        Objects.requireNonNull(response, "response");
    }

    WrapperHttpResponse(final HttpResponse response) {
        super();
        this.response = response;
    }

    final HttpResponse response;

    @Override
    public final String toString() {
        return this.response.toString();
    }
}
