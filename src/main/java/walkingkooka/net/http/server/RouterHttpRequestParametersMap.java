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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.ClientCookie;
import walkingkooka.net.header.HttpHeaderName;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Map} view of a {@link HttpRequest}.
 */
final class RouterHttpRequestParametersMap extends AbstractMap<HttpRequestAttribute<?>, Object> {

    /**
     * Factory that creates a map of parameters from a {@link HttpRequest}.
     */
    static RouterHttpRequestParametersMap with(final HttpRequest request) {
        return new RouterHttpRequestParametersMap(request);
    }

    /**
     * Private ctor use factory.
     */
    private RouterHttpRequestParametersMap(final HttpRequest request) {
        super();
        this.request = request;
    }

    @Override
    public boolean containsKey(final Object key) {
        return null != this.get(key);
    }

    @Override
    public Object get(final Object key) {
        Object value = null;

        if(key instanceof HttpRequestAttribute) {
            if(key instanceof UrlPathNameHttpRequestAttribute) {
                value = this.pathNameOrNull(UrlPathNameHttpRequestAttribute.class.cast(key).index);
            } else {
                value = HttpRequestAttribute.class.cast(key).parameterValue(this.request).orElse(null);
            }
        }

        return value;
    }

    /**
     * Lazily splits the path component into {@link UrlPathName name components}.
     */
    private UrlPathName pathNameOrNull(final int index) {
        final UrlPathName[] pathNames = this.pathNames();
        return index < pathNames.length ?
                pathNames[index] :
                null;
    }

    /**
     * Lazily gets the path names from the request url.
     */
    UrlPathName[] pathNames() {
        if (null == this.pathNames) {
            final List<UrlPathName> names = Lists.array();
            final Iterator<UrlPathName> i = this.url().path().iterator();

            while (i.hasNext()) {
                names.add(i.next());
            }
            this.pathNames = names.toArray(new UrlPathName[names.size()]);
        }
        return this.pathNames;
    }

    private transient UrlPathName[] pathNames;

    /**
     * The query string url parameters.
     */
    Map<UrlParameterName, List<String>> urlParameters() {
        return this.url().query().parameters();
    }

    @Override
    public Set<Entry<HttpRequestAttribute<?>, Object>> entrySet() {
        return this.entrySet;
    }

    private final RouterHttpRequestParametersMapEntrySet entrySet = RouterHttpRequestParametersMapEntrySet.with(this);

    @Override
    public int size() {
        if(-1 == this.size) {
            this.size = HttpRequestAttributes.size() +
                    this.pathNames().length +
                    this.urlParameters().size() +
                    this.cookieCount() +
                    this.headers().size() +
                    this.parameters().size();
        }
        return this.size;
    }

    /**
     * Caches the number of parameter values.
     */
    private int size = -1;

    private int cookieCount() {
        return HttpHeaderName.COOKIE.headerValue(this.headers()).orElse(ClientCookie.NO_COOKIES).size();
    }

    private Map<HttpHeaderName<?>, Object> headers() {
        return this.request.headers();
    }

    private Map<HttpRequestParameterName, List<String>> parameters() {
        return this.request.parameters();
    }

    private RelativeUrl url() {
        return this.request.url();
    }

    // Object ............................................................................................................

    @Override
    public final String toString() {
        return this.request.toString();
    }

    // shared
    final HttpRequest request;
}
