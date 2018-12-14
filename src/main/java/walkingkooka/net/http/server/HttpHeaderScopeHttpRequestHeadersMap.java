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
import walkingkooka.net.http.HttpHeaderName;
import walkingkooka.net.http.HttpHeaderScope;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Map} that checks the header (or key) passed to {@link #containsKey(Object)} and {@link #get(Object)}.
 */
final class HttpHeaderScopeHttpRequestHeadersMap extends AbstractMap<HttpHeaderName<?>, Object> {

    static HttpHeaderScopeHttpRequestHeadersMap with(final Map<HttpHeaderName<?>, Object> map,
                                                     final HttpHeaderScope scope) {
        return new HttpHeaderScopeHttpRequestHeadersMap(map, scope);
    }

    private HttpHeaderScopeHttpRequestHeadersMap(final Map<HttpHeaderName<?>, Object> map,
                                                 final HttpHeaderScope scope) {
        super();
        this.map = map;
        this.scope = scope;
    }

    @Override
    public boolean containsKey(final Object key) {
        return key instanceof HttpHeaderName &&
                this.map.containsKey(this.checkHeader(key));
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public Object get(final Object key) {
        return key instanceof HttpHeaderName ?
                this.map.get(this.checkHeader(key)) :
                null;
    }

    @Override
    public Set<HttpHeaderName<?>> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override
    public Set<Entry<HttpHeaderName<?>, Object>> entrySet() {
        return this.map.entrySet();
    }

    private HttpHeaderName<?> checkHeader(final Object key) {
        final HttpHeaderName<?> header = Cast.to(key);
        this.scope.check(header);
        return header;
    }

    private final HttpHeaderScope scope;

    private final Map<HttpHeaderName<?>, Object> map;

    @Override
    public String toString() {
        return this.map.toString();
    }
}
