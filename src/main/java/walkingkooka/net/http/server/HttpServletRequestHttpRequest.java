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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.net.http.cookie.ClientCookie;
import walkingkooka.net.http.cookie.Cookie;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An adapter that presents a {@link HttpRequest} from a {@link HttpServletRequest}.
 */
final class HttpServletRequestHttpRequest implements HttpRequest {

    static HttpServletRequestHttpRequest with(final HttpServletRequest request) {
        Objects.requireNonNull(request, "request");

        return new HttpServletRequestHttpRequest(request);
    }

    private HttpServletRequestHttpRequest(final HttpServletRequest request) {
        super();
        this.request = request;
        this.headers = HttpServletRequestHttpRequestHeadersMap.with(request);
        this.parameters = HttpServletRequestHttpRequestParametersMap.with(request.getParameterMap());
    }

    @Override
    public HttpTransport transport() {
        return this.request.isSecure() ?
                HttpTransport.SECURED :
                HttpTransport.UNSECURED;
    }

    @Override
    public HttpProtocolVersion protocolVersion() {
        return HttpProtocolVersion.with(this.request.getProtocol());
    }

    /**
     * Lazily parses the request URL into a {@link RelativeUrl}.
     */
    @Override
    public RelativeUrl url() {
        if (null == this.url) {
            this.url = RelativeUrl.parse(this.request.getRequestURL().toString());
        }
        return this.url;
    }

    private RelativeUrl url;

    @Override
    public HttpMethod method() {
        if (null == this.method) {
            this.method = HttpMethod.with(this.request.getMethod());
        }
        return this.method;
    }

    private HttpMethod method;

    /**
     * The header map view
     */
    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return this.headers;
    }

    private HttpServletRequestHttpRequestHeadersMap headers;

    /**
     * Lazily creates the list of {@link ClientCookie}.
     */
    @Override
    public List<ClientCookie> cookies() {
        if (null == this.cookies) {
            this.cookies = Arrays.stream(this.request.getCookies())
                    .map(Cookie::clientFrom)
                    .collect(Collectors.toList());
        }
        return this.cookies;
    }

    private List<ClientCookie> cookies;

    @Override
    public Map<HttpRequestParameterName, List<String>> parameters() {
        return this.parameters;
    }

    private final HttpServletRequestHttpRequestParametersMap parameters;

    @Override
    public List<String> parameterValues(final HttpRequestParameterName parameterName) {
        return this.parameters().get(parameterName);
    }

    private final HttpServletRequest request;

    /**
     * The {@link String} returned looks something similar to a http request in text form.
     */
    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.disable(ToStringBuilderOption.QUOTE);
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.separator(" ");

        b.value(this.transport());
        b.append('\n');

        b.value(this.method()).value(this.url()).value(this.protocolVersion());
        b.append('\n');

        b.valueSeparator("\n");
        b.value(this.headers());
        b.append('\n');

        b.value(this.parameters());

        return b.build();
    }
}
