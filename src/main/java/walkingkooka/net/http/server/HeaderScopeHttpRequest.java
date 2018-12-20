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

import walkingkooka.net.RelativeUrl;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.HttpHeaderScope;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A wrapper {@link HttpRequest} that wraps the map of headers verifying the header keys used to test and fetch values.
 */
final class HeaderScopeHttpRequest implements HttpRequest {

    static HeaderScopeHttpRequest with(final HttpRequest request) {
        Objects.requireNonNull(request, "request");
        return new HeaderScopeHttpRequest(request);
    }

    private HeaderScopeHttpRequest(final HttpRequest request) {
        super();
        this.request = request;
        this.headers = HeaderScopeHttpRequestHeadersMap.with(request.headers(), HttpHeaderScope.REQUEST);
    }

    @Override
    public HttpTransport transport() {
        return this.request.transport();
    }

    @Override
    public HttpMethod method() {
        return this.request.method();
    }

    @Override
    public RelativeUrl url() {
        return this.request.url();
    }

    @Override
    public HttpProtocolVersion protocolVersion() {
        return this.request.protocolVersion();
    }

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return this.headers;
    }

    private final HeaderScopeHttpRequestHeadersMap headers;

    @Override
    public List<ClientCookie> cookies() {
        return this.request.cookies();
    }

    @Override
    public Map<HttpRequestParameterName, List<String>> parameters() {
        return this.request.parameters();
    }

    @Override
    public List<String> parameterValues(final HttpRequestParameterName parameterName) {
        return this.request.parameterValues(parameterName);
    }

    private HttpRequest request;

    @Override
    public String toString() {
        return this.request.toString();
    }
}
