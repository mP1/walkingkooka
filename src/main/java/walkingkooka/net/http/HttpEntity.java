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

package walkingkooka.net.http;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * A http entity containing headers and body.
 */
public final class HttpEntity implements HasHeaders, HashCodeEqualsDefined {

    /**
     * Creates a new {@link HttpEntity}
     */
    public static HttpEntity with(final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        return new HttpEntity(checkHeaders(headers), checkBody(body));
    }

    /**
     * Private ctor
     */
    private HttpEntity(final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        super();
        this.headers = headers;
        this.body = body;
    }
    // headers ...................................................................................

    @Override
    public Map<HttpHeaderName<?>, Object> headers() {
        return Maps.readOnly(this.headers);
    }

    /**
     * Would be setter that returns a {@link HttpEntity} with the given headers creating a new instance if necessary.
     */
    public HttpEntity setHeaders(final Map<HttpHeaderName<?>, Object> headers) {
        final Map<HttpHeaderName<?>, Object> copy = checkHeaders(headers);

        return this.headers.equals(copy) ?
                this :
                this.replace(headers, this.body);

    }

    private Map<HttpHeaderName<?>, Object> headers;

    private static Map<HttpHeaderName<?>, Object> checkHeaders(final Map<HttpHeaderName<?>, Object> headers) {
        Objects.requireNonNull(headers, "headers");

        final Map<HttpHeaderName<?>, Object> copy = Maps.ordered();
        for (Entry<HttpHeaderName<?>, Object> nameAndValue : headers.entrySet()) {
            final HttpHeaderName<?> name = nameAndValue.getKey();
            copy.put(name, name.checkValue(nameAndValue.getValue()));
        }
        return copy;
    }

    // body ...................................................................................

    public byte[] body() {
        return this.body;
    }

    /**
     * Would be setter that returns a {@link HttpEntity} with the given body creating a new instance if necessary.
     */
    public HttpEntity setBody(final byte[] body) {
        final byte[] copy = checkBody(body);

        return Arrays.equals(this.body, copy) ?
                this :
                this.replace(this.headers, body);
    }

    private final byte[] body;

    private static byte[] checkBody(final byte[] body) {
        Objects.requireNonNull(body, "body");
        return body.clone();
    }

    // replace....................................................................................................

    private HttpEntity replace(final Map<HttpHeaderName<?>, Object> headers, final byte[] body) {
        return new HttpEntity(headers, body);
    }

    // Object....................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.headers, Arrays.hashCode(this.body));
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpEntity &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpEntity other) {
        return this.headers.equals(other.headers) &&
                Arrays.equals(this.body, other.body);
    }

    @Override
    public String toString() {
        return this.headers() + " " + this.body.length;
    }
}
