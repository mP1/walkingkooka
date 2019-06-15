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

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
import java.util.Set;

/**
 * A read only parameter map view of the request from a {@link javax.servlet.http.HttpServletRequest}.
 */
final class HttpServletRequestHttpRequestHeadersMap extends AbstractMap<HttpHeaderName<?>, Object> {

    static {
        Maps.registerImmutableType(HttpServletRequestHttpRequestHeadersMap.class);
    }

    static HttpServletRequestHttpRequestHeadersMap with(final HttpServletRequest request) {
        return new HttpServletRequestHttpRequestHeadersMap(request);
    }

    private HttpServletRequestHttpRequestHeadersMap(final HttpServletRequest request) {
        super();
        this.request = request;
    }

    @Override
    public boolean containsKey(final Object key) {
        return key instanceof HttpHeaderName<?> &&
                this.containsHeader(Cast.to(key));
    }

    private boolean containsHeader(final HttpHeaderName<?> key) {
        return null != this.request.getHeader(key.value());
    }

    @Override
    public Set<Entry<HttpHeaderName<?>, Object>> entrySet() {
        if (null == this.entrySet) {
            this.entrySet = HttpServletRequestHttpRequestHeadersMapEntrySet.with(this.request);
        }
        return this.entrySet;
    }

    private HttpServletRequestHttpRequestHeadersMapEntrySet entrySet;

    @Override
    public Object get(final Object key) {
        return this.getOrDefault(key, NO_HEADER_VALUES);
    }

    private final static Object NO_HEADER_VALUES = null;

    @Override
    public Object getOrDefault(final Object key, final Object defaultValue) {
        return key instanceof HttpHeaderName<?> ?
                this.getHeaderOrDefaultValue(Cast.to(key), defaultValue) :
                defaultValue;
    }

    private Object getHeaderOrDefaultValue(final HttpHeaderName<?> header, final Object defaultValue) {
        final String value = this.request.getHeader(header.value());
        return null != value ?
                header.toValue(value) :
                defaultValue;
    }

    /**
     * The request from the original request.
     */
    private final HttpServletRequest request;

    @Override
    public String toString() {
        return this.request.toString();
    }
}
