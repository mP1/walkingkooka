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
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Map;

/**
 * The {@link Name} of request or response header
 */
final public class HttpHeaderName implements Name, HashCodeEqualsDefined, Comparable<HttpHeaderName> {

    private final static long serialVersionUID = 1L;

    /**
     * Header names can never be empty.
     */
    public final static String INVALID = "";

    // constants

    /**
     * A read only cache of already prepared {@link HttpHeaderName names}. The selected constants are taken from <a href="http
     * ://en.wikipedia.org/wiki/List_of_HTTP_headers"></a>.
     */
    final static Map<String, HttpHeaderName> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built.
     */
    static HttpHeaderName registerConstant(final String header) {
        final HttpHeaderName httpHeader = new HttpHeaderName(header);
        HttpHeaderName.CONSTANTS.put(header, httpHeader);
        return httpHeader;
    }

    // Request headers

    /**
     * A {@link HttpHeaderName} holding <code>Accept</code>
     */
    public final static HttpHeaderName ACCEPT = HttpHeaderName.registerConstant("Accept");

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Charset</code>
     */
    public final static HttpHeaderName ACCEPT_CHARSET = HttpHeaderName.registerConstant("Accept-Charset");

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Encoding</code>
     */
    public final static HttpHeaderName ACCEPT_ENCODING = HttpHeaderName.registerConstant("Accept-Encoding");

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Language</code>
     */
    public final static HttpHeaderName ACCEPT_LANGUAGE = HttpHeaderName.registerConstant("Accept-Language");

    /**
     * A {@link HttpHeaderName} holding <code>Authorization</code>
     */
    public final static HttpHeaderName AUTHORIZATION = HttpHeaderName.registerConstant("Authorization");

    /**
     * A {@link HttpHeaderName} holding <code>Cache-Control</code>
     */
    public final static HttpHeaderName CACHE_CONTROL = HttpHeaderName.registerConstant("Cache-Control");

    /**
     * A {@link HttpHeaderName} holding <code>Connection</code>
     */
    public final static HttpHeaderName CONNECTION = HttpHeaderName.registerConstant("Connection");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Length</code>
     */
    public final static HttpHeaderName CONTENT_LENGTH = HttpHeaderName.registerConstant("Content-Length");

    /**
     * A {@link HttpHeaderName} holding <code>Content-MD5</code>
     */
    public final static HttpHeaderName CONTENT_MD5 = HttpHeaderName.registerConstant("Content-MD5");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Type</code>
     */
    public final static HttpHeaderName CONTENT_TYPE = HttpHeaderName.registerConstant("Content-Type");

    /**
     * A {@link HttpHeaderName} holding <code>Cookie</code>
     */
    public final static HttpHeaderName COOKIE = HttpHeaderName.registerConstant("Cookie");

    /**
     * A {@link HttpHeaderName} holding <code>Date</code>
     */
    public final static HttpHeaderName DATE = HttpHeaderName.registerConstant("Date");

    /**
     * A {@link HttpHeaderName} holding <code>Expect</code>
     */
    public final static HttpHeaderName EXPECT = HttpHeaderName.registerConstant("Expect");

    /**
     * A {@link HttpHeaderName} holding <code>From</code>
     */
    public final static HttpHeaderName FROM = HttpHeaderName.registerConstant("From");

    /**
     * A {@link HttpHeaderName} holding <code>Host</code>
     */
    public final static HttpHeaderName HOST = HttpHeaderName.registerConstant("Host");

    /**
     * A {@link HttpHeaderName} holding <code>If-Match</code>
     */
    public final static HttpHeaderName IF_MATCH = HttpHeaderName.registerConstant("If-Match");

    /**
     * A {@link HttpHeaderName} holding <code>If-Modified-Since</code>
     */
    public final static HttpHeaderName IF_MODIFIED_SINCE = HttpHeaderName.registerConstant("If-Modified-Since");

    /**
     * A {@link HttpHeaderName} holding <code>If-None-Match</code>
     */
    public final static HttpHeaderName IF_NONE_MATCHED = HttpHeaderName.registerConstant("If-None-Match");

    /**
     * A {@link HttpHeaderName} holding <code>If-Range</code>
     */
    public final static HttpHeaderName IF_RANGE = HttpHeaderName.registerConstant("If-Range");

    /**
     * A {@link HttpHeaderName} holding <code>If-Unmodified-Since</code>
     */
    public final static HttpHeaderName IF_UNMODIFIED_SINCE = HttpHeaderName.registerConstant("If-Unmodified-Since");

    /**
     * A {@link HttpHeaderName} holding <code>Max-Forwards</code>
     */
    public final static HttpHeaderName MAX_FORWARDS = HttpHeaderName.registerConstant("Max-Forwards");

    /**
     * A {@link HttpHeaderName} holding <code>Pragma</code>
     */
    public final static HttpHeaderName PRAGMA = HttpHeaderName.registerConstant("Pragma");

    /**
     * A {@link HttpHeaderName} holding <code>Proxy-Authorization</code>
     */
    public final static HttpHeaderName PROXY_AUTHORIZATION = HttpHeaderName.registerConstant("Proxy-Authorization");

    /**
     * A {@link HttpHeaderName} holding <code>Range</code>
     */
    public final static HttpHeaderName RANGE = HttpHeaderName.registerConstant("Range");

    /**
     * A {@link HttpHeaderName} holding <code>Referer</code>
     */
    public final static HttpHeaderName REFERER = HttpHeaderName.registerConstant("Referer");

    /**
     * A {@link HttpHeaderName} holding <code>TE</code>
     */
    public final static HttpHeaderName TE = HttpHeaderName.registerConstant("TE");

    /**
     * A {@link HttpHeaderName} holding <code>User-Agent</code>
     */
    public final static HttpHeaderName USER_AGENT = HttpHeaderName.registerConstant("User-Agent");

    /**
     * A {@link HttpHeaderName} holding <code>Via</code>
     */
    public final static HttpHeaderName VIA = HttpHeaderName.registerConstant("Via");

    /**
     * A {@link HttpHeaderName} holding <code>Warning</code>
     */
    public final static HttpHeaderName WARNING = HttpHeaderName.registerConstant("Warning");

    // Responses

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Ranges</code>
     */
    public final static HttpHeaderName ACCEPT_RANGES = HttpHeaderName.registerConstant("Accept-Ranges");

    /**
     * A {@link HttpHeaderName} holding <code>Age</code>
     */
    public final static HttpHeaderName AGE = HttpHeaderName.registerConstant("Age");

    /**
     * A {@link HttpHeaderName} holding <code>Allow</code>
     */
    public final static HttpHeaderName ALLOW = HttpHeaderName.registerConstant("Allow");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Encoding</code>
     */
    public final static HttpHeaderName CONTENT_ENCODING = HttpHeaderName.registerConstant("Content-Encoding");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Language</code>
     */
    public final static HttpHeaderName CONTENT_LANGUAGE = HttpHeaderName.registerConstant("Content-Language");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Location</code>
     */
    public final static HttpHeaderName CONTENT_LOCATION = HttpHeaderName.registerConstant("Content-Location");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Disposition</code>
     */
    public final static HttpHeaderName CONTENT_DISPOSITION = HttpHeaderName.registerConstant("Content-Disposition");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Range</code>
     */
    public final static HttpHeaderName CONTENT_RANGE = HttpHeaderName.registerConstant("Content-Range");

    /**
     * A {@link HttpHeaderName} holding <code>ETag</code>
     */
    public final static HttpHeaderName E_TAG = HttpHeaderName.registerConstant("ETag");

    /**
     * A {@link HttpHeaderName} holding <code>EXPIRES</code>
     */
    public final static HttpHeaderName EXPIRES = HttpHeaderName.registerConstant("Expires");

    /**
     * A {@link HttpHeaderName} holding <code>Last-Modified</code>
     */
    public final static HttpHeaderName LAST_MODIFIED = HttpHeaderName.registerConstant("Last-Modified");

    /**
     * A {@link HttpHeaderName} holding <code>Link</code>
     */
    public final static HttpHeaderName LINK = HttpHeaderName.registerConstant("Link");

    /**
     * A {@link HttpHeaderName} holding <code>Location</code>
     */
    public final static HttpHeaderName LOCATION = HttpHeaderName.registerConstant("Location");

    /**
     * A {@link HttpHeaderName} holding <code>P3P</code>
     */
    public final static HttpHeaderName P3P = HttpHeaderName.registerConstant("P3P");

    /**
     * A {@link HttpHeaderName} holding <code>Refresh</code>
     */
    public final static HttpHeaderName REFRESH = HttpHeaderName.registerConstant("Refresh");

    /**
     * A {@link HttpHeaderName} holding <code>Retry-After</code>
     */
    public final static HttpHeaderName RETRY_AFTER = HttpHeaderName.registerConstant("Retry-After");

    /**
     * A {@link HttpHeaderName} holding <code>Server</code>
     */
    public final static HttpHeaderName SERVER = HttpHeaderName.registerConstant("Server");

    /**
     * A {@link HttpHeaderName} holding <code>Set-Cookie</code>
     */
    public final static HttpHeaderName SET_COOKIE = HttpHeaderName.registerConstant("Set-Cookie");

    /**
     * A {@link HttpHeaderName} holding <code>Trailer</code>
     */
    public final static HttpHeaderName TRAILER = HttpHeaderName.registerConstant("Trailer");

    /**
     * A {@link HttpHeaderName} holding <code>Transfer-Encoding</code>
     */
    public final static HttpHeaderName TRANSFER_ENCODING = HttpHeaderName.registerConstant("Transfer-Encoding");

    /**
     * A {@link HttpHeaderName} holding <code>Vary</code>
     */
    public final static HttpHeaderName VARY = HttpHeaderName.registerConstant("Vary");

    /**
     * Factory that creates a {@link HttpHeaderName}
     */
    public static HttpHeaderName with(final String name) {
        CharSequences.failIfNullOrEmptyOrInitialAndPartFalse(name,  "name", HttpCharPredicates.httpHeaderName(),HttpCharPredicates.httpHeaderName());

        final HttpHeaderName httpHeaderName = CONSTANTS.get(name);
        return null != httpHeaderName ?
                httpHeaderName :
                new HttpHeaderName(name);
    }

    /**
     * Private constructor use factory.
     */
    private HttpHeaderName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Comparable

    @Override
    public int compareTo(final HttpHeaderName name) {
        return this.name.compareToIgnoreCase(name.name);
    }

    // Object

    @Override
    public int hashCode() {
        return CaseSensitivity.INSENSITIVE.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpHeaderName &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpHeaderName name) {
        return this.name.equalsIgnoreCase(name.name);
    }

    /**
     * Dumps the raw header name without quotes.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
