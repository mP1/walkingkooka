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

import walkingkooka.type.PublicStaticHelper;

import javax.servlet.http.HttpServletRequest;

public final class HttpRequests implements PublicStaticHelper {

    /**
     * {@see FakeHttpRequest}
     */
    public static HttpRequest fake() {
        return new FakeHttpRequest();
    }

    /**
     * {@see HeaderScopeHttpRequest}
     */
    public static HttpRequest headerScope(final HttpRequest request) {
        return HeaderScopeHttpRequest.with(request);
    }

    /**
     * {@see HttpServletRequestHttpRequest}
     */
    public static HttpRequest httpServletRequest(final HttpServletRequest request) {
        return HttpServletRequestHttpRequest.with(request);
    }

    /**
     * Stop creation
     */
    private HttpRequests() {
        throw new UnsupportedOperationException();
    }
}
