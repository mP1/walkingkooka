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
import walkingkooka.NeverError;
import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A {@link Value} including an enumeration of standards methods that contains the HTTP request methods.
 */
public final class HttpMethod implements Value<String>, HashCodeEqualsDefined {

    /**
     * A cache of all {@link HttpMethod versions}. Placed above constants so it is initialized before constants
     */
    private final static Map<String, HttpMethod> CACHE = new WeakHashMap<String, HttpMethod>();

    /**
     * The <code>HEAD</code> method
     */
    public final static HttpMethod HEAD = HttpMethod.createAndAdd("HEAD");

    /**
     * The <code>GET</code> method
     */
    public final static HttpMethod GET = HttpMethod.createAndAdd("GET");

    /**
     * The <code>POST</code> method
     */
    public final static HttpMethod POST = HttpMethod.createAndAdd("POST");

    /**
     * The <code>PUT</code> method
     */
    public final static HttpMethod PUT = HttpMethod.createAndAdd("PUT");

    /**
     * The <code>DELETE</code> method
     */
    public final static HttpMethod DELETE = HttpMethod.createAndAdd("DELETE");

    /**
     * The <code>TRACE</code> method
     */
    public final static HttpMethod TRACE = HttpMethod.createAndAdd("TRACE");

    /**
     * The <code>OPTIONS</code> method
     */
    public final static HttpMethod OPTIONS = HttpMethod.createAndAdd("OPTIONS"); //

    /**
     * The <code>CONNECT</code> method
     */
    public final static HttpMethod CONNECT = HttpMethod.createAndAdd("CONNECT");

    /**
     * The <code>PATCH</code> method
     */
    public final static HttpMethod PATCH = HttpMethod.createAndAdd("PATCH");

    /**
     * Creates and adds a new {@link HttpMethod} to the cache being built.
     */
    private static HttpMethod createAndAdd(final String header) {
        final HttpMethod httpHeader = new HttpMethod(header);
        if (null != HttpMethod.CACHE.put(header, httpHeader)) {
            throw new NeverError("Attempt to add duplicate constant=" + CharSequences.quoteAndEscape(header));
        }
        return httpHeader;
    }

    /**
     * Factory that creates a {@link HttpMethod} with the {@link String method name}.
     */
    public static HttpMethod with(final String method) {
        Whitespace.failIfNullOrEmptyOrWhitespace(method, "method");

        final String key = method.toUpperCase();
        HttpMethod httpMethod = HttpMethod.CACHE.get(key);
        if (null == httpMethod) {
            // verify method name
            final int length = method.length();
            for (int i = 0; i < length; i++) {
                final char c = method.charAt(i);
                if (false == Character.isLetter(c)) {
                    throw new IllegalArgumentException(
                            "Method includes invalid character " + CharSequences.quoteAndEscape(c) + "'="
                                    + CharSequences.quoteAndEscape(method));
                }
            }

            httpMethod = new HttpMethod(method);
        }

        return httpMethod;
    }

    /**
     * Private constructor use static factory.
     */
    private HttpMethod(final String value) {
        super();
        this.value = value;
    }

    /**
     * Getter that returns the method as a {@link String}.
     */
    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    /**
     * Tests if the method is a GET or HEAD a commonly performed test.
     */
    public boolean isGetOrHead() {
        return this == GET || this == HEAD;
    }

    // Object

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpMethod &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpMethod other) {
        return this.value.equalsIgnoreCase(other.value);
    }

    // Object

    /**
     * Dumps the method name.
     */
    @Override
    public String toString() {
        return this.value;
    }
}
