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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.predicate.Predicates;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An abstract {@link WrapperHttpResponse} that buffers any status or headers, until {@link #setBody(byte[])} or {@link #setBodyText(String)},
 * and then calls matching abstract methods for each to control copying to the wrapped response.
 */
abstract class BufferingHttpResponse extends WrapperHttpResponse {

    /**
     * Package private to limit sub classing.
     */
    BufferingHttpResponse(final HttpResponse response) {
        super(response);
    }

    @Override
    public final void setStatus(final HttpStatus status) {
        Objects.requireNonNull(status, "status");

        if(this.comitted) {
            this.response.setStatus(status);
        } else {
            this.status = status;
        }
    }

    @Override
    public final <T> void addHeader(final HttpHeaderName<T> name, final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if(this.comitted) {
            this.response.addHeader(name, value);
        } else {
            this.headers.put(name, value);
        }
    }

    @Override
    public final Map<HttpHeaderName<?>, Object> headers() {
        return this.comitted ?
                this.response.headers() :
                this.readOnlyHeaders;
    }

    @Override
    public final void setBody(final byte[] body) {
        Objects.requireNonNull(body, "body");

        if(this.comitted) {
            this.response.setBody(body);
        } else {
            this.comitted = true;
            this.setBody(this.status, this.headers, body);
        }
    }

    @Override
    public final void setBodyText(final String bodyText) {
        Objects.requireNonNull(bodyText, "bodyText");

        if(this.comitted) {
            this.response.setBodyText(bodyText);
        } else {
            this.comitted = true;
            this.setBodyText(this.status, this.headers, bodyText);
        }
    }

    private HttpStatus status;
    private final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
    private final Map<HttpHeaderName<?>, Object> readOnlyHeaders = Maps.readOnly(this.headers);

    /**
     * When false all headers and status code is buffered and not set upon the wrapped {@link HttpResponse}.
     */
    boolean comitted = false;

    abstract void setBody(HttpStatus status,
                          final Map<HttpHeaderName<?>, Object> headers,
                          final byte[] body);

    abstract void setBodyText(HttpStatus status,
                              final Map<HttpHeaderName<?>, Object> headers,
                              final String bodyText);

    /**
     * Copies all headers to the response.
     */
    final void copyHeaders(final Map<HttpHeaderName<?>, Object> headers){
        this.copyHeaders(headers, Predicates.always());
    }

    /**
     * Copy all headers that are matched by the filter.
     */
    final void copyHeaders(final Map<HttpHeaderName<?>, Object> headers, final Predicate<HttpHeaderName<?>> filter){
        headers.entrySet()
                .stream()
                .filter(headerAndValue -> filter.test(headerAndValue.getKey()))
                .forEach(headerAndValue -> this.response.addHeader(Cast.to(headerAndValue.getKey()), headerAndValue.getValue()));
    }

    /**
     * A {@link Predicate} that matches all headers except for content headers.
     */
    final Predicate<HttpHeaderName<?>> ignoreContentHeaders() {
        return (h) -> !h.isContent();
    }
}
