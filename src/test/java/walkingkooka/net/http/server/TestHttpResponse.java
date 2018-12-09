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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;

import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * A response that records all parameters set up on it for later verification.
 */
final class TestHttpResponse implements HttpResponse {
    TestHttpResponse() {
        super();
        this.headers = Maps.ordered();
    }

    TestHttpResponse(final HttpStatus status,
                     final Map<HttpHeaderName<?>, Object> headers,
                     final byte[] body,
                     final String bodyText) {
        super();
        this.status = status;
        this.headers = headers;
        this.body = body;
        this.bodyText = bodyText;
    }

    @Override
    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    HttpStatus status;

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return Maps.readOnly(this.headers);
    }

    @Override
    public <T> void addHeader(final HttpHeaderName<T> name, final T value) {
        this.headers.put(name, value);
    }

    final Map<HttpHeaderName<?>, Object> headers;

    @Override
    public void setBody(final byte[] body) {
        this.body = body;
    }

    byte[] body;

    @Override
    public void setBodyText(final String bodyText) {
        this.bodyText = bodyText;
    }

    String bodyText;

    void check(final HttpRequest request,
               final HttpStatus status,
               final Map<HttpHeaderName<?>, Object> headers,
               final byte[] body) {
        this.check(request, status, headers, body, null);
    }

    void check(final HttpRequest request,
               final HttpStatus status,
               final Map<HttpHeaderName<?>, Object> headers,
               final String bodyText) {
        this.check(request, status, headers, null, bodyText);
    }

    void check(final HttpRequest request,
               final HttpStatus status,
               final Map<HttpHeaderName<?>, Object> headers,
               final byte[] body,
               final String bodyText) {
        assertEquals(request.toString(),
                new TestHttpResponse(status, headers, body, bodyText),
                this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.status, this.headers, this.body, this.bodyText);
    }

    public boolean equals(final Object other) {
        return this == other || other instanceof TestHttpResponse && this.equals0(Cast.to(other));
    }

    private boolean equals0(final TestHttpResponse other) {
        return Objects.equals(this.status, other.status) &&
                Objects.equals(this.headers, other.headers) &&
                Objects.equals(this.body, other.body) &&
                Objects.equals(this.bodyText, other.bodyText);
    }

    @Override
    public String toString() {
        return ToStringBuilder.create()
                .value(this.status)
                .value(this.headers)
                .value(this.body)
                .value(this.bodyText)
                .build();
    }
}
