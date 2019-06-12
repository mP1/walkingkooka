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
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.HttpHeaderName;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The {@link Map#entrySet()} for {@link RouterHttpRequestParametersMap}.
 */
final class RouterHttpRequestParametersMapEntrySet extends AbstractSet<Entry<HttpRequestAttribute<?>, Object>> {

    static {
        Sets.registerImmutableType(RouterHttpRequestParametersMapEntrySet.class);
    }

    /**
     * Factory only called by {@link RouterHttpRequestParametersMap}.
     */
    static RouterHttpRequestParametersMapEntrySet with(final RouterHttpRequestParametersMap map) {
        return new RouterHttpRequestParametersMapEntrySet(map);
    }

    /**
     * Private ctor
     */
    private RouterHttpRequestParametersMapEntrySet(final RouterHttpRequestParametersMap map) {
        super();
        this.map = map;
    }

    @Override
    public Iterator<Entry<HttpRequestAttribute<?>, Object>> iterator() {
        // attributes, path, url-parameters, headers, cookies, request parameters
        final RouterHttpRequestParametersMap map = this.map;
        final HttpRequest request = map.request;

        final Iterator<Entry<HttpRequestAttribute<?>, Object>> attributes = RouterHttpRequestParametersMapHttpRequestAttributeEntryIterator.with(request);

        final Iterator<Entry<HttpRequestAttribute<?>, Object>> pathNames = RouterHttpRequestParametersMapPathComponentEntryIterator.with(map.pathNames());

        final Iterator<Entry<HttpRequestAttribute<?>, Object>> urlParameterNames = Cast.to(map.urlParameters().entrySet().iterator());

        final Iterator<Entry<HttpRequestAttribute<?>, Object>> headers = RouterHttpRequestParametersMapHttpHeaderEntryIterator.with(request.headers().entrySet().iterator());

        final Iterator<Entry<HttpRequestAttribute<?>, Object>> cookies = RouterHttpRequestParametersMapCookiesEntryIterator.with(HttpHeaderName.COOKIE.headerValue(request.headers()).orElse(ClientCookie.NO_COOKIES));

        final Iterator<Entry<HttpRequestAttribute<?>, Object>> parameters = Cast.to(request.parameters().entrySet().iterator());

        return Iterators.chain(attributes,
                Iterators.chain(pathNames,
                        Iterators.chain(urlParameterNames,
                                Iterators.chain(headers,
                                        Iterators.chain(cookies, parameters)))));
    }

    @Override
    public int size() {
        return this.map.size();
    }

    private final RouterHttpRequestParametersMap map;

    @Override
    public String toString() {
        return this.map.toString();
    }
}
