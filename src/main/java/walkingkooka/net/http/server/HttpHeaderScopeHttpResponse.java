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

import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpHeaderScope;
import walkingkooka.net.http.HttpStatus;

import java.util.Map;

/**
 * A {@link HttpResponse} that checks the scope correctness of any header names when adding new headers, or reading
 * existing headers.
 */
final class HttpHeaderScopeHttpResponse extends WrapperHttpResponse {

    static HttpHeaderScopeHttpResponse with(final HttpResponse response) {
        check(response);
        return new HttpHeaderScopeHttpResponse(response);
    }
    
    private HttpHeaderScopeHttpResponse(final HttpResponse response) {
        super(response);
        this.headers = HttpHeaderScopeHttpRequestHttpResponseHeadersMap.with(response.headers(), HttpHeaderScope.RESPONSE);
    }

    @Override
    public void setStatus(final HttpStatus status) {
        this.response.setStatus(status);
    }

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return this.headers;
    }

    private final HttpHeaderScopeHttpRequestHttpResponseHeadersMap headers;

    @Override
    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
        HttpHeaderScope.RESPONSE.check(name, value);
        this.response.addHeader(name, value);
    }

    @Override
    public void setBody(final byte[] body) {
        this.response.setBody(body);
    }

    @Override
    public void setBodyText(final String text) {
        this.response.setBodyText(text);
    }
}
