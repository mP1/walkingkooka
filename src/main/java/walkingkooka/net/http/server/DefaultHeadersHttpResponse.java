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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Unconditionally adds headers to the first entity added.
 */
final class DefaultHeadersHttpResponse extends WrapperHttpResponse {

    static HttpResponse with(final Map<HttpHeaderName<?>, Object> headers,
                             final HttpResponse response) {
        check(response);

        final Map<HttpHeaderName<?>, Object> copy = Maps.immutable(headers);
        return copy.isEmpty() ?
                response :
                new DefaultHeadersHttpResponse(copy, response);
    }

    private DefaultHeadersHttpResponse(final Map<HttpHeaderName<?>, Object> headers,
                                       final HttpResponse response) {
        super(response);
        this.headers = headers;
    }

    @Override
    public void setStatus(final HttpStatus status) {
        this.response.setStatus(status);
    }

    @Override
    public void addEntity(final HttpEntity entity) {
        HttpEntity add = entity;

        if (this.first) {
            this.first = false;

            // only add defaults that are absent.
            final Map<HttpHeaderName<?>, Object> headers = entity.headers();

            for (Entry<HttpHeaderName<?>, Object> headerAndValue : this.headers.entrySet()) {
                final HttpHeaderName<?> header = headerAndValue.getKey();
                final Object value = headerAndValue.getValue();
                if (!headers.containsKey(header)) {
                    add = add.addHeader(Cast.to(header), value);
                }
            }
        }
        this.response.addEntity(add);
    }

    /**
     * Only true so default headers are only added to the first entity.
     */
    private boolean first = true;
    private final Map<HttpHeaderName<?>, Object> headers;
}
