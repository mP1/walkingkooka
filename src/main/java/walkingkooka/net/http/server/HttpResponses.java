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
import walkingkooka.type.PublicStaticHelper;

import java.util.function.Function;

public final class HttpResponses implements PublicStaticHelper {

    /**
     * {@see AutoContentLengthHttpResponse}
     */
    public static HttpResponse contentLength(final HttpRequest request,
                                             final HttpResponse response) {
        return AutoContentLengthHttpResponse.with(request, response);
    }

    /**
     * {@see AutoGzipEncodingHttpResponse}
     */
    public static HttpResponse autoGzipEncoding(final HttpRequest request,
                                                final HttpResponse response) {
        return AutoGzipEncodingHttpResponse.with(request, response);
    }

    /**
     * {@see FakeHttpResponse}
     */
    public static HttpResponse fake() {
        return new FakeHttpResponse();
    }

    /**
     * {@see HttpHeaderScopeHttpResponse}
     */
    public static HttpResponse httpHeaderScope(final HttpResponse response) {
        return HttpHeaderScopeHttpResponse.with(response);
    }

    /**
     * {@see IfNoneMatchAwareHttpResponse}
     */
    public static HttpResponse ifNoneMatchAware(final HttpRequest request,
                                                final HttpResponse response,
                                                final Function<byte[], HttpETag> computer) {
        return IfNoneMatchAwareHttpResponse.with(request, response, computer);
    }

    /**
     * {@see LastModifiedAwareHttpResponse}
     */
    public static HttpResponse lastModifiedAware(final HttpRequest request,
                                                 final HttpResponse response) {
        return LastModifiedAwareHttpResponse.with(request, response);
    }

    /**
     * {@see RangeAwareHttpResponse}
     */
    static HttpResponse with(final HttpRequest request,
                             final HttpResponse response) {
        return RangeAwareHttpResponse.with(request, response);
    }

    /**
     * Stop creation
     */
    private HttpResponses() {
        throw new UnsupportedOperationException();
    }
}
