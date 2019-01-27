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
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.test.Fake;

import java.util.List;
import java.util.Map;

public class FakeHttpRequest implements HttpRequest, Fake {

    @Override
    public HttpTransport transport() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpMethod method() {
        throw new UnsupportedOperationException();
    }

    @Override
    public HttpProtocolVersion protocolVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RelativeUrl url() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] body() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<HttpRequestParameterName, List<String>> parameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> parameterValues(final HttpRequestParameterName parameterName) {
        throw new UnsupportedOperationException();
    }
}
