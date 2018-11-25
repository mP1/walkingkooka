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
import walkingkooka.compare.Range;
import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.cookie.ClientCookie;
import walkingkooka.net.media.MediaType;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of request or response header
 */
final public class HttpHeaderName<T> implements Name, HashCodeEqualsDefined, Comparable<HttpHeaderName<?>> {

    // constants

    /**
     * A read only cache of already prepared {@link HttpHeaderName names}. The selected constants are taken from <a href="http
     * ://en.wikipedia.org/wiki/List_of_HTTP_headers"></a>.
     */
    final static Map<String, HttpHeaderName> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link AbsoluteUrl} header values.
     */
    private static HttpHeaderName<AbsoluteUrl> registerAbsoluteUrlConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.absoluteUrl());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link ClientCookie} header values.
     */
    private static HttpHeaderName<List<ClientCookie>> registerClientCookieListConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.clientCookieList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link EmailAddress} header values.
     */
    private static HttpHeaderName<EmailAddress> registerEmailAddressConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.emailAddress());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link HttpHeaderName} header values.
     */
    private static HttpHeaderName<List<HttpHeaderName<?>>> registerHttpHeaderNameListConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.httpHeaderNameList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link LocalDateTime} header values.
     */
    private static HttpHeaderName<LocalDateTime> registerLocalDateTimeConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.localDateTime());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link Long} header values.
     */
    private static HttpHeaderName<Long> registerLongConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.longConverter());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link HttpMethod} header values.
     */
    private static HttpHeaderName<List<HttpMethod>> registerMethodListConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.methodList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link MediaType} header values.
     */
    private static HttpHeaderName<MediaType> registerOneMediaTypeConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.oneMediaType());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link MediaType} header values.
     */
    private static HttpHeaderName<List<MediaType>> registerManyMediaTypeConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.manyMediaType());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link Range} header values.
     */
    private static HttpHeaderName<HttpHeaderRange> registerRangeConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.range());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link RelativeUrl} header values.
     */
    private static HttpHeaderName<RelativeUrl> registerRelativeUrlConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.relativeUrl());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link String} header values.
     */
    private static HttpHeaderName<String> registerStringConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.string());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link String} header values.
     */
    private static HttpHeaderName<List<HttpHeaderToken>> registerTokenListConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.tokenList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link Url} header values.
     */
    private static HttpHeaderName<Url> registerUrlConstant(final String header) {
        return registerConstant(header, HttpHeaderValueConverter.url());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built.
     */
    private static <T> HttpHeaderName<T> registerConstant(final String header, final HttpHeaderValueConverter<T> headerValue) {
        final HttpHeaderName<T> httpHeader = new HttpHeaderName<T>(header, headerValue);
        HttpHeaderName.CONSTANTS.put(header, httpHeader);
        return httpHeader;
    }

    // Request headers

    /**
     * A {@link HttpHeaderName} holding <code>Accept</code>
     * <pre>
     * // Multiple types, weighted with the quality value syntax:
     * Accept: text/html, application/xhtml+xml, application/xml;q=0.9, * /*;q=0.8
     * </pre>
     */
    public final static HttpHeaderName<List<MediaType>> ACCEPT = registerManyMediaTypeConstant("Accept");

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Charset</code>
     * <pre>
     * // Multiple types, weighted with the quality value syntax:
     * Accept-Charset: utf-8, iso-8859-1;q=0.5
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderToken>> ACCEPT_CHARSET = registerTokenListConstant("Accept-Charset");

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Encoding</code>
     * <pre>
     * Accept-Encoding: gzip
     * Accept-Encoding: compress
     * Accept-Encoding: deflate
     * Accept-Encoding: br
     * Accept-Encoding: identity
     * Accept-Encoding: *
     *
     * // Multiple algorithms, weighted with the quality value syntax:
     * Accept-Encoding: deflate, gzip;q=1.0, *;q=0.5
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderToken>> ACCEPT_ENCODING = registerTokenListConstant("Accept-Encoding");

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Language</code>
     * <pre>
     * Accept-Language: <language>
     * Accept-Language: <locale>
     * Accept-Language: *
     *
     * // Multiple types, weighted with the quality value syntax:
     * Accept-Language: fr-CH, fr;q=0.9, en;q=0.8, de;q=0.7, *;q=0.5
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderToken>> ACCEPT_LANGUAGE = registerTokenListConstant("Accept-Language");

    /**
     * A {@link HttpHeaderName} holding <code>Authorization</code>
     * <pre>
     * Authorization: <type> <credentials>
     * </pre>
     */
    public final static HttpHeaderName<String> AUTHORIZATION = registerStringConstant("Authorization");

    /**
     * A {@link HttpHeaderName} holding <code>Cache-Control</code>
     * <pre>
     * Cache-Control: must-revalidate
     * Cache-Control: no-cache
     * Cache-Control: no-store
     * Cache-Control: no-transform
     * Cache-Control: public
     * Cache-Control: private
     * Cache-Control: proxy-revalidate
     * Cache-Control: max-age=<seconds>
     * Cache-Control: s-maxage=<seconds>
     * </pre>
     */
    public final static HttpHeaderName<String> CACHE_CONTROL = registerStringConstant("Cache-Control");

    /**
     * A {@link HttpHeaderName} holding <code>Connection</code>
     * <pre>
     * Connection: keep-alive
     * Connection: close
     * </pre>
     */
    public final static HttpHeaderName<String> CONNECTION = registerStringConstant("Connection");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Length</code>
     * <pre>
     * Content-Length: <length>
     * </pre>
     */
    public final static HttpHeaderName<Long> CONTENT_LENGTH = registerLongConstant("Content-Length");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Type</code>
     * <pre>
     * Content-Type: text/html; charset=utf-8
     * Content-Type: multipart/form-data; boundary=something
     * </pre>
     */
    public final static HttpHeaderName<MediaType> CONTENT_TYPE = registerOneMediaTypeConstant("Content-Type");

    /**
     * A {@link HttpHeaderName} holding <code>Cookie</code>
     * <pre>
     * Cookie: <cookie-list>
     * Cookie: name=value
     * Cookie: name=value; name2=value2; name3=value3
     * </pre>
     */
    public final static HttpHeaderName<List<ClientCookie>> COOKIE = registerClientCookieListConstant("Cookie");

    /**
     * A {@link HttpHeaderName} holding <code>Date</code>
     * <pre>
     * Date: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> DATE = registerLocalDateTimeConstant("Date");

    /**
     * A {@link HttpHeaderName} holding <code>Expect</code>
     * <pre>
     * Expect: 100-continue
     * </pre>
     */
    public final static HttpHeaderName<String> EXPECT = registerStringConstant("Expect");

    /**
     * A {@link HttpHeaderName} holding <code>From</code>
     * <pre>
     * From: webmaster@example.org
     * </pre>
     */
    public final static HttpHeaderName<EmailAddress> FROM = registerEmailAddressConstant("From");

    /**
     * A {@link HttpHeaderName} holding <code>Host</code>
     * <pre>
     * Host: <host>:<port>
     * </pre>
     */
    public final static HttpHeaderName<String> HOST = registerStringConstant("Host");

    /**
     * A {@link HttpHeaderName} holding <code>If-Match</code>
     * <pre>
     * If-Match: "bfc13a64729c4290ef5b2c2730249c88ca92d82d"
     * If-Match: W/"67ab43", "54ed21", "7892dd"
     * If-Match: *
     * </pre>
     */
    public final static HttpHeaderName<String> IF_MATCH = registerStringConstant("If-Match");

    /**
     * A {@link HttpHeaderName} holding <code>If-Modified-Since</code>
     * <pre>
     * If-Modified-Since: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> IF_MODIFIED_SINCE = registerLocalDateTimeConstant("If-Modified-Since");

    /**
     * A {@link HttpHeaderName} holding <code>If-None-Match</code>
     * <pre>
     * If-None-Match: "<etag_value>"
     * If-None-Match: "<etag_value>", "<etag_value>", …
     * If-None-Match: *
     * </pre>
     */
    public final static HttpHeaderName<String> IF_NONE_MATCHED = registerStringConstant("If-None-Match");

    /**
     * A {@link HttpHeaderName} holding <code>If-Range</code>
     * <pre>
     * If-Range: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<String> IF_RANGE = registerStringConstant("If-Range");

    /**
     * A {@link HttpHeaderName} holding <code>If-Unmodified-Since</code>
     * <pre>
     * If-Unmodified-Since: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> IF_UNMODIFIED_SINCE = registerLocalDateTimeConstant("If-Unmodified-Since");

    /**
     * A {@link HttpHeaderName} holding <code>Keep-Alive</code>
     * <pre>
     * Keep-Alive: timeout=5, max=1000
     * </pre>
     */
    public final static HttpHeaderName<String> KEEP_ALIVE = registerStringConstant("Keep-Alive");

    /**
     * A {@link HttpHeaderName} holding <code>Pragma</code>
     * <pre>
     * Pragma: no-cache
     * </pre>
     */
    public final static HttpHeaderName<String> PRAGMA = registerStringConstant("Pragma");

    /**
     * A {@link HttpHeaderName} holding <code>Range</code>
     * <pre>
     * Range: <unit>=<range-start>-
     * Range: <unit>=<range-start>-<range-end>
     * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>
     * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>, <range-start>-<range-end>
     * </pre>
     */
    public final static HttpHeaderName<HttpHeaderRange> RANGE = registerRangeConstant("Range");

    /**
     * A {@link HttpHeaderName} holding <code>Referer</code>
     * <pre>
     * Referer: https://developer.mozilla.org/en-US/docs/Web/JavaScript
     * </pre>
     */
    public final static HttpHeaderName<AbsoluteUrl> REFERER = registerAbsoluteUrlConstant("Referer");

    /**
     * A {@link HttpHeaderName} holding <code>TE</code>
     * <pre>
     * TE: compress
     * TE: deflate
     * TE: gzip
     * TE: trailers
     *
     * // Multiple directives, weighted with the quality value syntax:
     * TE: trailers, deflate;q=0.5
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderToken>> TE = registerTokenListConstant("TE");

    /**
     * A {@link HttpHeaderName} holding <code>User-Agent</code>
     * <pre>
     * User-Agent: <product> / <product-version> <comment>
     *
     * Common format for web browsers:
     *
     * User-Agent: Mozilla/<version> (<system-information>) <platform> (<platform-details>) <extensions>
     * </pre>
     */
    public final static HttpHeaderName<String> USER_AGENT = registerStringConstant("User-Agent");

    /**
     * A {@link HttpHeaderName} holding <code>Warning</code>
     * <pre>
     * Warning: 110 anderson/1.3.37 "Response is stale"
     *
     * Date: Wed, 21 Oct 2015 07:28:00 GMT
     * Warning: 112 - "cache down" "Wed, 21 Oct 2015 07:28:00 GMT"
     * </pre>
     */
    public final static HttpHeaderName<String> WARNING = registerStringConstant("Warning");

    // Responses

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Ranges</code>
     * <pre>
     * Accept-Ranges: bytes
     * Accept-Ranges: none
     * </pre>
     */
    public final static HttpHeaderName<String> ACCEPT_RANGES = registerStringConstant("Accept-Ranges");

    /**
     * A {@link HttpHeaderName} holding <code>Age</code>
     * <pre>
     * Age: <delta-seconds>
     * </pre>
     */
    public final static HttpHeaderName<Long> AGE = registerLongConstant("Age");

    /**
     * A {@link HttpHeaderName} holding <code>Allow</code>
     * <pre>
     * Allow: GET, POST, HEAD
     * </pre>
     */
    public final static HttpHeaderName<List<HttpMethod>> ALLOW = registerMethodListConstant("Allow");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Disposition</code>
     * <pre>
     * Content-Disposition: inline
     * Content-Disposition: attachment
     * Content-Disposition: attachment; filename="filename.jpg"
     *
     * Content-Disposition: form-data
     * Content-Disposition: form-data; name="fieldName"
     * Content-Disposition: form-data; name="fieldName"; filename="filename.jpg"
     * </pre>
     */
    public final static HttpHeaderName<String> CONTENT_DISPOSITION = registerStringConstant("Content-Disposition");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Encoding</code>
     * <pre>
     * Content-Encoding: gzip
     * Content-Encoding: compress
     * Content-Encoding: deflate
     * Content-Encoding: identity
     * Content-Encoding: br
     *
     * // Multiple, in the order in which they were applied
     * Content-Encoding: gzip, identity
     * Content-Encoding: deflate, gzip
     * </pre>
     */
    public final static HttpHeaderName<String> CONTENT_ENCODING = registerStringConstant("Content-Encoding");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Language</code>
     * <pre>
     * Content-Language: de-DE
     * Content-Language: en-US
     * Content-Language: de-DE, en-CA
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderToken>> CONTENT_LANGUAGE = registerTokenListConstant("Content-Language");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Location</code>
     * <pre>
     * Request header	Response header
     * Accept: application/json, text/json	Content-Location: /documents/foo.json
     * Accept: application/xml, text/xml	Content-Location: /documents/foo.xml
     * Accept: text/plain, text/*	Content-Location: /documents/foo.txt
     * </pre>
     */
    public final static HttpHeaderName<RelativeUrl> CONTENT_LOCATION = registerRelativeUrlConstant("Content-Location");

    /**
     * A {@link HttpHeaderName} holding <code>Content-Range</code>
     * <pre>
     * Content-Range: <unit> <range-start>-<range-end>/<size>
     * Content-Range: <unit> <range-start>-<range-end>/*
     * Content-Range: <unit> * /<size>
     * </pre>
     */
    public final static HttpHeaderName<String> CONTENT_RANGE = registerStringConstant("Content-Range");

    /**
     * A {@link HttpHeaderName} holding <code>ETag</code>
     * <pre>
     * ETag: "33a64df551425fcc55e4d42a148795d9f25f89d4"
     * ETag: W/"0815"
     * </pre>
     */
    public final static HttpHeaderName<String> E_TAG = registerStringConstant("ETag");

    /**
     * A {@link HttpHeaderName} holding <code>EXPIRES</code>
     * <pre>
     * Expires: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> EXPIRES = registerLocalDateTimeConstant("Expires");

    /**
     * A {@link HttpHeaderName} holding <code>Last-Modified</code>
     * <pre>
     * Last-Modified: Wed, 21 Oct 2015 07:28:00 GMT    
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> LAST_MODIFIED = registerLocalDateTimeConstant("Last-Modified");

    /**
     * A {@link HttpHeaderName} holding <code>Location</code>
     * <pre>
     * A relative (to the request URL) or absolute URL.
     * ...
     * Location: /index.html
     * </pre>
     */
    public final static HttpHeaderName<Url> LOCATION = registerUrlConstant("Location");

    /**
     * A {@link HttpHeaderName} holding <code>Retry-After</code>
     * <pre>
     * Retry-After: <http-date>
     * Retry-After: <delay-seconds>
     * </pre>
     */
    public final static HttpHeaderName<String> RETRY_AFTER = registerStringConstant("Retry-After");

    /**
     * A {@link HttpHeaderName} holding <code>Server</code>
     * <pre>
     * Server: Apache/2.4.1 (Unix)
     * </pre>
     */
    public final static HttpHeaderName<String> SERVER = registerStringConstant("Server");

    /**
     * A {@link HttpHeaderName} holding <code>Set-Cookie</code>
     * <pre>
     * Set-Cookie: <cookie-name>=<cookie-value>
     * Set-Cookie: <cookie-name>=<cookie-value>; Expires=<date>
     * Set-Cookie: <cookie-name>=<cookie-value>; Max-Age=<non-zero-digit>
     * Set-Cookie: <cookie-name>=<cookie-value>; Domain=<domain-value>
     * Set-Cookie: <cookie-name>=<cookie-value>; Path=<path-value>
     * Set-Cookie: <cookie-name>=<cookie-value>; Secure
     * Set-Cookie: <cookie-name>=<cookie-value>; HttpOnly
     *
     * Set-Cookie: <cookie-name>=<cookie-value>; SameSite=Strict
     * Set-Cookie: <cookie-name>=<cookie-value>; SameSite=Lax
     *
     * // Multiple directives are also possible, for example:
     * Set-Cookie: <cookie-name>=<cookie-value>; Domain=<domain-value>; Secure; HttpOnly
     * </pre>
     */
    public final static HttpHeaderName<String> SET_COOKIE = registerStringConstant("Set-Cookie");

    /**
     * A {@link HttpHeaderName} holding <code>Trailer</code>
     * <pre>
     * Trailer: header-names
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderName<?>>> TRAILER = registerHttpHeaderNameListConstant("Trailer");

    /**
     * A {@link HttpHeaderName} holding <code>Transfer-Encoding</code>
     * <pre>
     * Transfer-Encoding: chunked
     * Transfer-Encoding: compress
     * Transfer-Encoding: deflate
     * Transfer-Encoding: gzip
     * Transfer-Encoding: identity
     *
     * // Several values can be listed, separated by a comma
     * Transfer-Encoding: gzip, chunked
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderToken>> TRANSFER_ENCODING = registerTokenListConstant("Transfer-Encoding");

    /**
     * Factory that creates a {@link HttpHeaderName}. If the {@link #headerValue(HttpRequest)} is not a constant the
     * {@link #headerValue(HttpRequest)} will return {@link String}.
     */
    public static HttpHeaderName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", HttpCharPredicates.httpHeaderName());

        final HttpHeaderName httpHeaderName = CONSTANTS.get(name);
        return null != httpHeaderName ?
                httpHeaderName :
                new HttpHeaderName<String>(name, HttpHeaderValueConverter.string());
    }

    /**
     * Private constructor use factory.
     */
    private HttpHeaderName(final String name, final HttpHeaderValueConverter<T> headerValueConverter) {
        this.name = name;
        this.headerValueConverter = headerValueConverter;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Returns a {@link HttpHeaderName} that always and does not attempt to convert values to {@link String}.
     * If the header already returns {@link String string values} this will return this.
     */
    public HttpHeaderName<String> stringHeaderValues() {
        return this.headerValueConverter.isString() ?
               Cast.to(this) :
               new HttpHeaderName<String>(this.name, HttpHeaderValueConverter.string());
    }

    /**
     * A type safe getter that converts any present header values to the header name type.
     */
    public Optional<T> headerValue(final HttpRequest request) {
        Objects.requireNonNull(request, "request");

        final String value = request.headers().get(this);
        return null != value ?
                Optional.of(this.headerValue0(value)) :
                Optional.empty();
    }

    /**
     * Unconditionally converts the {@link String value} to the appropriate type for this header.
     */
    public T headerValue(final String value) {
        Objects.requireNonNull(value, "value");
        return this.headerValue0(value);
    }

    private T headerValue0(final String value) {
        return this.headerValueConverter.parse(value, this);
    }

    private final HttpHeaderValueConverter<T> headerValueConverter;

    // Comparable

    @Override
    public int compareTo(final HttpHeaderName<?> name) {
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
