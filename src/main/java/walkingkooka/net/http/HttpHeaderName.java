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
import walkingkooka.net.header.CharsetHeaderValue;
import walkingkooka.net.header.ContentDisposition;
import walkingkooka.net.header.HeaderName;
import walkingkooka.net.header.HeaderValueConverter;
import walkingkooka.net.header.HeaderValueConverters;
import walkingkooka.net.header.HeaderValueException;
import walkingkooka.net.header.HeaderValueToken;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.cookie.ClientCookie;
import walkingkooka.net.http.cookie.ServerCookie;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of request or response header
 */
final public class HttpHeaderName<T> implements HeaderName<T>,
        Comparable<HttpHeaderName<?>>,
        HasHttpHeaderScope {

    // constants

    /**
     * A read only cache of already prepared {@link HttpHeaderName names}. The selected constants are taken from<br>
     * <a href="http://en.wikipedia.org/wiki/List_of_HTTP_headers"></a>.
     */
    final static Map<String, HttpHeaderName<?>> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link AbsoluteUrl} header values.
     */
    private static HttpHeaderName<AbsoluteUrl> registerAbsoluteUrlConstant(final String header,
                                                                           final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.absoluteUrl());
    }



    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link List<CharsetHeaderValue>} header values.
     */
    private static HttpHeaderName<List<CacheControlDirective<?>>> registerCacheControlDirectiveListConstant(final String header,
                                                                                                            final HttpHeaderScope scope) {
        return registerConstant(header,
                scope,
                HttpHeaderValueConverter.cacheControlDirectiveList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link List<CharsetHeaderValue>} header values.
     */
    private static HttpHeaderName<List<CharsetHeaderValue>> registerCharsetHeaderValueListConstant(final String header,
                                                                                                   final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.charsetHeaderValueList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link ClientCookie} header values.
     */
    private static HttpHeaderName<List<ClientCookie>> registerClientCookieListConstant(final String header,
                                                                                       final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.clientCookieList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link EmailAddress} header values.
     */
    private static HttpHeaderName<EmailAddress> registerEmailAddressConstant(final String header,
                                                                             final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.emailAddress());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache that handles header values of type {@link HeaderValueToken}.
     */
    private static HttpHeaderName<HeaderValueToken> registerHeaderValueTokenConstant(final String header,
                                                                                     final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.headerValueToken());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache that handles header values that include a list of {@link HeaderValueToken}.
     */
    private static HttpHeaderName<List<HeaderValueToken>> registerHeaderValueTokenListConstant(final String header,
                                                                                               final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.headerValueTokenList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link HttpETag} header values.
     */
    private static HttpHeaderName<HttpETag> registerHttpETagConstant(final String header,
                                                                     final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.httpETag());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles lists of {@link HttpETag} header values.
     */
    private static HttpHeaderName<List<HttpETag>> registerHttpETagListConstant(final String header,
                                                                               final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.httpETagList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link HttpHeaderName} header values.
     */
    private static HttpHeaderName<List<HttpHeaderName<?>>> registerHttpHeaderNameListConstant(final String header,
                                                                                              final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.httpHeaderNameList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link LocalDateTime} header values.
     */
    private static HttpHeaderName<LocalDateTime> registerLocalDateTimeConstant(final String header,
                                                                               final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.localDateTime());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link Long} header values.
     */
    private static HttpHeaderName<Long> registerLongConstant(final String header,
                                                             final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.longConverter());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles list of {@link HttpMethod} header values.
     */
    private static HttpHeaderName<List<HttpMethod>> registerMethodListConstant(final String header,
                                                                               final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.methodList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link MediaType} header values.
     */
    private static HttpHeaderName<MediaType> registerOneMediaTypeConstant(final String header,
                                                                          final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.mediaType());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link MediaType} header values.
     */
    private static HttpHeaderName<List<MediaType>> registerManyMediaTypeConstant(final String header,
                                                                                 final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.mediaTypeList());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link Range} header values.
     */
    private static HttpHeaderName<HttpHeaderRange> registerRangeConstant(final String header,
                                                                         final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.range());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link RelativeUrl} header values.
     */
    private static HttpHeaderName<RelativeUrl> registerRelativeUrlConstant(final String header,
                                                                           final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.relativeUrl());
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link ServerCookie} header values.
     */
    private static HttpHeaderName<ServerCookie> registerServerCookieConstant(final String header,
                                                                             final HttpHeaderScope scope) {
        return registerConstant(header, scope, HttpHeaderValueConverter.serverCookie());
    }

    /**
     * A {@link HeaderValueConverter} that accepts any printable ascii characters.
     */
    private final static HeaderValueConverter<String> ASCII_PRINTABLE_STRING_HEADER_VALUE_CONVERTER =
            HeaderValueConverters.string(CharPredicates.asciiPrintable());

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link String} header values.
     */
    private static HttpHeaderName<String> registerStringConstant(final String header,
                                                                 final HttpHeaderScope scope) {
        return registerConstant(header, scope, ASCII_PRINTABLE_STRING_HEADER_VALUE_CONVERTER);
    }

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built that handles {@link Url} header values.
     */
    private static HttpHeaderName<Url> registerUrlConstant(final String header, final HttpHeaderScope scope) {
        return registerConstant(header, scope, HeaderValueConverters.url());
    }

    /**
     * All content headers share this prefix.
     */
    private final static String CONTENT_HEADER_PREFIX = "content-";

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built.
     */
    private static <T> HttpHeaderName<T> registerConstant(final String header,
                                                          final HttpHeaderScope scope,
                                                          final HeaderValueConverter<T> headerValue) {
        final HttpHeaderName<T> httpHeader = new HttpHeaderName<T>(header,
                scope,
                headerValue,
                CaseSensitivity.INSENSITIVE.startsWith(header, CONTENT_HEADER_PREFIX));
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
    public final static HttpHeaderName<List<MediaType>> ACCEPT = registerManyMediaTypeConstant("Accept",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Charset</code>
     * <pre>
     * // Multiple types, weighted with the quality value syntax:
     * Accept-Charset: utf-8, iso-8859-1;q=0.5
     * </pre>
     */
    public final static HttpHeaderName<List<CharsetHeaderValue>> ACCEPT_CHARSET = registerCharsetHeaderValueListConstant("Accept-Charset",
            HttpHeaderScope.REQUEST);

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
    public final static HttpHeaderName<List<HeaderValueToken>> ACCEPT_ENCODING = registerHeaderValueTokenListConstant("Accept-Encoding",
            HttpHeaderScope.REQUEST);

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
    public final static HttpHeaderName<List<HeaderValueToken>> ACCEPT_LANGUAGE = registerHeaderValueTokenListConstant("Accept-Language",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Authorization</code>
     * <pre>
     * Authorization: <type> <credentials>
     * </pre>
     */
    public final static HttpHeaderName<String> AUTHORIZATION = registerStringConstant("Authorization",
            HttpHeaderScope.REQUEST);

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
    public final static HttpHeaderName<List<CacheControlDirective<?>>> CACHE_CONTROL = registerCacheControlDirectiveListConstant("Cache-Control",
            HttpHeaderScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Connection</code>
     * <pre>
     * Connection: keep-alive
     * Connection: close
     * </pre>
     */
    public final static HttpHeaderName<String> CONNECTION = registerStringConstant("Connection",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Content-Length</code>
     * <pre>
     * Content-Length: <length>
     * </pre>
     */
    public final static HttpHeaderName<Long> CONTENT_LENGTH = registerLongConstant("Content-Length",
            HttpHeaderScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Content-Type</code>
     * <pre>
     * Content-Type: text/html; charset=utf-8
     * Content-Type: multipart/form-data; boundary=something
     * </pre>
     */
    public final static HttpHeaderName<MediaType> CONTENT_TYPE = registerOneMediaTypeConstant("Content-Type",
            HttpHeaderScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Cookie</code>
     * <pre>
     * Cookie: <cookie-list>
     * Cookie: name=value
     * Cookie: name=value; name2=value2; name3=value3
     * </pre>
     */
    public final static HttpHeaderName<List<ClientCookie>> COOKIE = registerClientCookieListConstant("Cookie",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Date</code>
     * <pre>
     * Date: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> DATE = registerLocalDateTimeConstant("Date",
            HttpHeaderScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Expect</code>
     * <pre>
     * Expect: 100-continue
     * </pre>
     */
    public final static HttpHeaderName<String> EXPECT = registerStringConstant("Expect",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>From</code>
     * <pre>
     * From: webmaster@example.org
     * </pre>
     */
    public final static HttpHeaderName<EmailAddress> FROM = registerEmailAddressConstant("From",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Host</code>
     * <pre>
     * Host: <host>:<port>
     * </pre>
     */
    public final static HttpHeaderName<String> HOST = registerStringConstant("Host",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Match</code>
     * <pre>
     * If-Match: "bfc13a64729c4290ef5b2c2730249c88ca92d82d"
     * If-Match: W/"67ab43", "54ed21", "7892dd"
     * If-Match: *
     * </pre>
     */
    public final static HttpHeaderName<List<HttpETag>> IF_MATCH = registerHttpETagListConstant("If-Match",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Modified-Since</code>
     * <pre>
     * If-Modified-Since: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> IF_MODIFIED_SINCE = registerLocalDateTimeConstant("If-Modified-Since",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-None-Match</code>
     * <pre>
     * If-None-Match: "<etag_value>"
     * If-None-Match: "<etag_value>", "<etag_value>", …
     * If-None-Match: *
     * </pre>
     */
    public final static HttpHeaderName<List<HttpETag>> IF_NONE_MATCHED = registerHttpETagListConstant("If-None-Match",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Range</code>
     * <pre>
     * If-Range: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<String> IF_RANGE = registerStringConstant("If-Range",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Unmodified-Since</code>
     * <pre>
     * If-Unmodified-Since: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> IF_UNMODIFIED_SINCE = registerLocalDateTimeConstant("If-Unmodified-Since",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Keep-Alive</code>
     * <pre>
     * Keep-Alive: timeout=5, max=1000
     * </pre>
     */
    public final static HttpHeaderName<String> KEEP_ALIVE = registerStringConstant("Keep-Alive",
            HttpHeaderScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Pragma</code>
     * <pre>
     * Pragma: no-cache
     * </pre>
     */
    public final static HttpHeaderName<String> PRAGMA = registerStringConstant("Pragma",
            HttpHeaderScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Range</code>
     * <pre>
     * Range: <unit>=<range-start>-
     * Range: <unit>=<range-start>-<range-end>
     * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>
     * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>, <range-start>-<range-end>
     * </pre>
     */
    public final static HttpHeaderName<HttpHeaderRange> RANGE = registerRangeConstant("Range",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Referer</code>
     * <pre>
     * Referer: https://developer.mozilla.org/en-US/docs/Web/JavaScript
     * </pre>
     */
    public final static HttpHeaderName<AbsoluteUrl> REFERER = registerAbsoluteUrlConstant("Referer",
            HttpHeaderScope.REQUEST);

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
    public final static HttpHeaderName<List<HeaderValueToken>> TE = registerHeaderValueTokenListConstant("TE",
            HttpHeaderScope.REQUEST);

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
    public final static HttpHeaderName<String> USER_AGENT = registerStringConstant("User-Agent",
            HttpHeaderScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Warning</code>
     * <pre>
     * Warning: 110 anderson/1.3.37 "Response is stale"
     *
     * Date: Wed, 21 Oct 2015 07:28:00 GMT
     * Warning: 112 - "cache down" "Wed, 21 Oct 2015 07:28:00 GMT"
     * </pre>
     */
    public final static HttpHeaderName<String> WARNING = registerStringConstant("Warning",
            HttpHeaderScope.REQUEST);

    // Responses

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Ranges</code>
     * <pre>
     * Accept-Ranges: bytes
     * Accept-Ranges: none
     * </pre>
     */
    public final static HttpHeaderName<String> ACCEPT_RANGES = registerStringConstant("Accept-Ranges",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Age</code>
     * <pre>
     * Age: <delta-seconds>
     * </pre>
     */
    public final static HttpHeaderName<Long> AGE = registerLongConstant("Age",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Allow</code>
     * <pre>
     * Allow: GET, POST, HEAD
     * </pre>
     */
    public final static HttpHeaderName<List<HttpMethod>> ALLOW = registerMethodListConstant("Allow",
            HttpHeaderScope.RESPONSE);

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
    public final static HttpHeaderName<ContentDisposition> CONTENT_DISPOSITION = registerConstant("Content-Disposition",
            HttpHeaderScope.REQUEST_RESPONSE,
            HeaderValueConverters.contentDisposition());

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
    public final static HttpHeaderName<HeaderValueToken> CONTENT_ENCODING = registerHeaderValueTokenConstant("Content-Encoding",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Content-Language</code>
     * <pre>
     * Content-Language: de-DE
     * Content-Language: en-US
     * Content-Language: de-DE, en-CA
     * </pre>
     */
    public final static HttpHeaderName<List<HeaderValueToken>> CONTENT_LANGUAGE = registerHeaderValueTokenListConstant("Content-Language",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Content-Location</code>
     * <pre>
     * Request header	Response header
     * Accept: application/json, text/json	Content-Location: /documents/foo.json
     * Accept: application/xml, text/xml	Content-Location: /documents/foo.xml
     * Accept: text/plain, text/*	Content-Location: /documents/foo.txt
     * </pre>
     */
    public final static HttpHeaderName<RelativeUrl> CONTENT_LOCATION = registerRelativeUrlConstant("Content-Location",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Content-Range</code>
     * <pre>
     * Content-Range: <unit> <range-start>-<range-end>/<size>
     * Content-Range: <unit> <range-start>-<range-end>/*
     * Content-Range: <unit> * /<size>
     * </pre>
     */
    public final static HttpHeaderName<String> CONTENT_RANGE = registerStringConstant("Content-Range",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>ETag</code>
     * <pre>
     * ETag: "33a64df551425fcc55e4d42a148795d9f25f89d4"
     * ETag: W/"0815"
     * </pre>
     */
    public final static HttpHeaderName<HttpETag> E_TAG = registerHttpETagConstant("ETag",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>EXPIRES</code>
     * <pre>
     * Expires: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> EXPIRES = registerLocalDateTimeConstant("Expires",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Last-Modified</code>
     * <pre>
     * Last-Modified: Wed, 21 Oct 2015 07:28:00 GMT    
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> LAST_MODIFIED = registerLocalDateTimeConstant("Last-Modified",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Location</code>
     * <pre>
     * A relative (to the request URL) or absolute URL.
     * ...
     * Location: /index.html
     * </pre>
     */
    public final static HttpHeaderName<Url> LOCATION = registerUrlConstant("Location",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Retry-After</code>
     * <pre>
     * Retry-After: <http-date>
     * Retry-After: <delay-seconds>
     * </pre>
     */
    public final static HttpHeaderName<String> RETRY_AFTER = registerStringConstant("Retry-After",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Server</code>
     * <pre>
     * Server: Apache/2.4.1 (Unix)
     * </pre>
     */
    public final static HttpHeaderName<String> SERVER = registerStringConstant("Server",
            HttpHeaderScope.RESPONSE);

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
    public final static HttpHeaderName<ServerCookie> SET_COOKIE = registerServerCookieConstant("Set-Cookie",
            HttpHeaderScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Trailer</code>
     * <pre>
     * Trailer: header-names
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderName<?>>> TRAILER = registerHttpHeaderNameListConstant("Trailer",
            HttpHeaderScope.RESPONSE);

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
    public final static HttpHeaderName<List<HeaderValueToken>> TRANSFER_ENCODING = registerHeaderValueTokenListConstant("Transfer-Encoding",
            HttpHeaderScope.RESPONSE);

    /**
     * Factory that creates a {@link HttpHeaderName}.
     */
    public static HttpHeaderName<?> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", PREDICATE);

        final HttpHeaderName<?> httpHeaderName = CONSTANTS.get(name);
        return null != httpHeaderName ?
                httpHeaderName :
                new HttpHeaderName<String>(name, HttpHeaderScope.UNKNOWN, ASCII_PRINTABLE_STRING_HEADER_VALUE_CONVERTER, NOT_CONTENT);
    }

    /**
     * A {@link CharPredicate} that match any character in a header name
     * <a href="https://www.ietf.org/rfc/rfc822.txt">RFC822</a>
     * <pre>
     * The field-name must be composed of printable ASCII characters (position.e., characters that have values between 33. and 126., decimal, except colon).
     * </pre>
     */
    private final static CharPredicate PREDICATE = CharPredicates.builder()//
            .or(CharPredicates.ascii())//
            .and(CharPredicates.range((char) 33, (char) 126))//
            .andNot(CharPredicates.any(":."))
            .toString("http header name")//
            .build();

    /**
     * Private constructor use factory.
     */
    private HttpHeaderName(final String name,
                           final HttpHeaderScope scope,
                           final HeaderValueConverter<T> valueConverter,
                           final boolean content) {
        this.name = name;
        this.scope = scope;
        this.valueConverter = valueConverter;
        this.content = content;
    }

    private final static boolean NOT_CONTENT = false;

    /**
     * Only returns true if this is a content header. Content headers, are those that begin with "content-".
     */
    public boolean isContent() {
        return this.content;
    }

    private final boolean content;

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    /**
     * Returns the valid scope for this header name.
     */
    public HttpHeaderScope httpHeaderScope() {
        return this.scope;
    }

    private final HttpHeaderScope scope;

    /**
     * Validates the value.
     */
    @Override
    public T checkValue(final Object value) {
        return this.valueConverter.check(value);
    }

    /**
     * A type safe getter that retrieves this header from the headers.
     */
    public Optional<T> headerValue(final Map<HttpHeaderName<?>, Object> headers) {
        Objects.requireNonNull(headers, "headers");

        return Optional.ofNullable(Cast.to(headers.get(this)));
    }

    /**
     * Retrieves the value or throws a {@link HeaderValueException} if absent.
     */
    public T headerValueOrFail(Map<HttpHeaderName<?>, Object> headers) {
        final Optional<T> value = this.headerValue(headers);
        if (!value.isPresent()) {
            throw new HeaderValueException("Required value is absent for " + this);
        }
        return value.get();
    }

    /**
     * Unconditionally converts the {@link String value} to the appropriate type for this header.
     */
    @Override
    public T toValue(final String value) {
        Objects.requireNonNull(value, "value");

        return this.valueConverter.parse(value, this);
    }

    /**
     * Sets the value upon the {@link HttpResponse}
     */
    public void addHeaderValue(final T value, final HttpResponse response) {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(response, "response");

        HttpHeaderScope.RESPONSE.check(this, value);
        response.addHeader(this, value);
    }

    /**
     * Formats the given typed value into header value text.
     */
    public String headerText(final T value) {
        Objects.requireNonNull(value, "value");

        return this.valueConverter.toText(value, this);
    }

    private final HeaderValueConverter<T> valueConverter;

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
