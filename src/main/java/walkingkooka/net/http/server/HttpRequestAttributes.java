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

import walkingkooka.collect.map.Maps;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpTransport;

import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link HttpRequestAttribute} to be used as a key for several misc {@link HttpRequest} attributes.
 */
public final class HttpRequestAttributes<T> implements HttpRequestAttribute<T> {

    /**
     * A {@link HttpRequestAttribute} for {@link HttpRequest#method()}
     */
    public final static HttpRequestAttributes<HttpMethod> METHOD = new HttpRequestAttributes<HttpMethod>("METHOD",
            HttpRequest::method);

    /**
     * A {@link HttpRequestAttribute} for {@link HttpRequest#protocolVersion()}
     */
    public final static HttpRequestAttributes<HttpProtocolVersion> HTTP_PROTOCOL_VERSION = new HttpRequestAttributes<HttpProtocolVersion>("PROTOCOL_VERSION",
            HttpRequest::protocolVersion);

    /**
     * A {@link HttpRequestAttribute} for {@link HttpRequest#transport()}
     */
    public final static HttpRequestAttributes<HttpTransport> TRANSPORT = new HttpRequestAttributes<HttpTransport>("TRANSPORT",
            HttpRequest::transport);

    /**
     * {@see UrlPathNameHttpRequestAttribute}
     */
    public static HttpRequestAttribute<UrlPathName> pathComponent(final int index) {
        return UrlPathNameHttpRequestAttribute.with(index);
    }

    /**
     * Private ctor use constant.
     */
    private HttpRequestAttributes(final String name,
                                  final Function<HttpRequest, T> request) {
        super();
        this.name = name;
        this.request = request;
    }

    @Override
    public Optional<T> parameterValue(final HttpRequest request) {
        return Optional.ofNullable(this.request.apply(request));
    }

    private final Function<HttpRequest, T> request;

    /**
     * The number of values
     */
    static int size() {
        return VALUES.length;
    }

    /**
     * Returns an entry holding this attribute as the key and the actual request value as the value.
     */
    static Entry<HttpRequestAttribute<?>, Object> entry(final int position, final HttpRequest request) {
        if (position >= VALUES.length) {
            throw new NoSuchElementException();
        }
        final HttpRequestAttributes key = VALUES[position];
        return Maps.entry(key, key.parameterValue(request).get());
    }

    /**
     * Returns the {@link #toString()} for the iterator entry.
     */
    static String iteratorEntryToString(final int position, final HttpRequest request) {
        return position < VALUES.length ?
                VALUES[position] + "=" + VALUES[position].parameterValue(request).get() :
                "";
    }

    /**
     * Constants an array to facilitate access via index.
     */
    private final static HttpRequestAttributes<?>[] VALUES = new HttpRequestAttributes<?>[]{TRANSPORT,
            METHOD,
            HTTP_PROTOCOL_VERSION};

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.name;
    }

    private final String name;
}
