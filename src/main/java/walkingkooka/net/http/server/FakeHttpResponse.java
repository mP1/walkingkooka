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
import walkingkooka.net.http.HttpStatus;
import walkingkooka.test.Fake;

import java.util.Map;
import java.util.Objects;

public class FakeHttpResponse implements HttpResponse, Fake {

    @Override
    public void setStatus(final HttpStatus status) {
        Objects.requireNonNull(status, "status");
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBody(final byte[] body) {
        Objects.requireNonNull(body, "body");
        throw new UnsupportedOperationException();
    }

    @Override
    public void setBodyText(final String body) {
        Objects.requireNonNull(body, "body");
        throw new UnsupportedOperationException();
    }
}
