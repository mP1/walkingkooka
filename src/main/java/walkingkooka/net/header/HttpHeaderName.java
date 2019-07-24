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

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of header.
 */
final public class HttpHeaderName<T> extends HeaderName2<T>
        implements HasHeaderScope,
        HttpRequestAttribute<T>,
        Comparable<HttpHeaderName<?>> {

    // constants........................................................................................................

    /**
     * A read only cache of already prepared {@link HttpHeaderName names}. The selected constants are taken from<br>
     * <a href="http://en.wikipedia.org/wiki/List_of_HTTP_headers"></a>.
     */
    final static Map<String, HttpHeaderName<?>> CONSTANTS = Maps.sorted(HttpHeaderName.CASE_SENSITIVITY.comparator());

    /**
     * Creates a header constant with a list of {@link ETag} values.
     */
    private static HttpHeaderName<List<ETag>> registerETagListConstant(final String header,
                                                                       final HttpHeaderNameScope scope) {
        return registerConstant(header, scope, HeaderValueHandler.eTagList());
    }

    /**
     * Creates a header constant with a list of {@link LocalDateTime} values.
     */
    private static HttpHeaderName<LocalDateTime> registerLocalDateTimeConstant(final String header,
                                                                               final HttpHeaderNameScope scope) {
        return registerConstant(header, scope, HeaderValueHandler.localDateTime());
    }

    /**
     * Creates a header constant with a list of {@link Long} values.
     */
    private static HttpHeaderName<Long> registerLongConstant(final String header,
                                                             final HttpHeaderNameScope scope) {
        return registerConstant(header, scope, HeaderValueHandler.longHandler());
    }

    /**
     * Creates a header constant with a list of {@link String} values.
     */
    private static HttpHeaderName<String> registerStringConstant(final String header,
                                                                 final HttpHeaderNameScope scope) {
        return registerConstant(header, scope, HeaderValueHandler.string());
    }

    /**
     * All content headers share this prefix.
     */
    private final static String CONTENT_HEADER_PREFIX = "content-";

    /**
     * Creates and adds a new {@link HttpHeaderName} to the cache being built.
     */
    private static <T> HttpHeaderName<T> registerConstant(final String header,
                                                          final HttpHeaderNameScope scope,
                                                          final HeaderValueHandler<T> headerValue) {

        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Conditional_requests
        boolean conditional;

        switch (header.toLowerCase()) {
            case "etag":
            case "if-match":
            case "if-none-match":
            case "if-modified-since":
            case "if-unmodified-since":
            case "if-range":
                conditional = true;
                break;
            default:
                conditional = false;
                break;
        }

        final HttpHeaderName<T> httpHeader = new HttpHeaderName<T>(header,
                scope,
                headerValue,
                conditional,
                CaseSensitivity.INSENSITIVE.startsWith(header, CONTENT_HEADER_PREFIX));
        HttpHeaderName.CONSTANTS.put(header, httpHeader);
        return httpHeader;
    }

    /**
     * A {@link HttpHeaderName} holding <code>Accept</code>
     * <pre>
     * // Multiple types, weighted with the quality value syntax:
     * Accept: text/html, application/xhtml+xml, application/xml;q=0.9, * /*;q=0.8
     * </pre>
     */
    public final static HttpHeaderName<List<MediaType>> ACCEPT = registerConstant("Accept",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.mediaTypeList());

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Charset</code>
     * <pre>
     * // Multiple types, weighted with the quality value syntax:
     * Accept-Charset: utf-8, iso-8859-1;q=0.5
     * </pre>
     */
    public final static HttpHeaderName<AcceptCharset> ACCEPT_CHARSET = registerConstant("Accept-Charset",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.acceptCharset());

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
    public final static HttpHeaderName<AcceptEncoding> ACCEPT_ENCODING = registerConstant("Accept-Encoding",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.acceptEncoding());

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
    public final static HttpHeaderName<AcceptLanguage> ACCEPT_LANGUAGE = registerConstant("Accept-Language",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.acceptLanguage());

    /**
     * A {@link HttpHeaderName} holding <code>Accept-Ranges</code>
     * <pre>
     * Accept-Ranges: bytes
     * Accept-Ranges: none
     * </pre>
     */
    public final static HttpHeaderName<RangeHeaderValueUnit> ACCEPT_RANGES = registerConstant("Accept-Ranges",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.rangeUnit());

    /**
     * A {@link HttpHeaderName} holding <code>Age</code>
     * <pre>
     * Age: <delta-seconds>
     * </pre>
     */
    public final static HttpHeaderName<Long> AGE = registerLongConstant("Age",
            HttpHeaderNameScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Allow</code>
     * <pre>
     * Allow: GET, POST, HEAD
     * </pre>
     */
    public final static HttpHeaderName<List<HttpMethod>> ALLOW = registerConstant("Allow",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.methodList());


    /**
     * A {@link HttpHeaderName} holding <code>Authorization</code>
     * <pre>
     * Authorization: <type> <credentials>
     * </pre>
     */
    public final static HttpHeaderName<String> AUTHORIZATION = registerStringConstant("Authorization",
            HttpHeaderNameScope.REQUEST);

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
    public final static HttpHeaderName<CacheControl> CACHE_CONTROL = registerConstant("Cache-Control",
            HttpHeaderNameScope.REQUEST_RESPONSE,
            HeaderValueHandler.cacheControl());

    /**
     * A {@link HttpHeaderName} holding <code>Connection</code>
     * <pre>
     * Connection: keep-alive
     * Connection: close
     * </pre>
     */
    public final static HttpHeaderName<String> CONNECTION = registerStringConstant("Connection",
            HttpHeaderNameScope.REQUEST);


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
            HttpHeaderNameScope.REQUEST_RESPONSE,
            HeaderValueHandler.contentDisposition());

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
    public final static HttpHeaderName<ContentEncoding> CONTENT_ENCODING = registerConstant("Content-Encoding",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.contentEncoding());

    /**
     * A {@link HttpHeaderName} holding <code>Content-Language</code>
     * <pre>
     * Content-Language: de-DE
     * Content-Language: en-US
     * Content-Language: de-DE, en-CA
     * </pre>
     */
    public final static HttpHeaderName<ContentLanguage> CONTENT_LANGUAGE = registerConstant("Content-Language",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.contentLanguage());

    /**
     * A {@link HttpHeaderName} holding <code>Content-Length</code>
     * <pre>
     * Content-Length: <length>
     * </pre>
     */
    public final static HttpHeaderName<Long> CONTENT_LENGTH = registerLongConstant("Content-Length",
            HttpHeaderNameScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Content-Location</code>
     * <pre>
     * Request header	Response header
     * Accept: application/json, text/json	Content-Location: /documents/foo.json
     * Accept: application/xml, text/xml	Content-Location: /documents/foo.xml
     * Accept: text/plain, text/*	Content-Location: /documents/foo.txt
     * </pre>
     */
    public final static HttpHeaderName<RelativeUrl> CONTENT_LOCATION = registerConstant("Content-Location",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.relativeUrl());

    /**
     * A {@link HttpHeaderName} holding <code>Content-Range</code>
     * <pre>
     * Content-Range: <unit> <range-start>-<range-end>/<size>
     * Content-Range: <unit> <range-start>-<range-end>/*
     * Content-Range: <unit> * /<size>
     * </pre>
     */
    public final static HttpHeaderName<ContentRange> CONTENT_RANGE = registerConstant("Content-Range",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.contentRange());

    /**
     * A {@link HttpHeaderName} holding <code>Content-Type</code>
     * <pre>
     * Content-Type: text/html; charset=utf-8
     * Content-Type: multipart/form-data; boundary=something
     * </pre>
     */
    public final static HttpHeaderName<MediaType> CONTENT_TYPE = registerConstant("Content-Type",
            HttpHeaderNameScope.REQUEST_RESPONSE_MULTIPART,
            HeaderValueHandler.mediaType());

    /**
     * A {@link HttpHeaderName} holding <code>Cookie</code>
     * <pre>
     * Cookie: <cookie-list>
     * Cookie: name=value
     * Cookie: name=value; name2=value2; name3=value3
     * </pre>
     */
    public final static HttpHeaderName<List<ClientCookie>> COOKIE = registerConstant("Cookie",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.clientCookieList());

    /**
     * A {@link HttpHeaderName} holding <code>Date</code>
     * <pre>
     * Date: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> DATE = registerLocalDateTimeConstant("Date",
            HttpHeaderNameScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>ETag</code>
     * <pre>
     * ETag: "33a64df551425fcc55e4d42a148795d9f25f89d4"
     * ETag: W/"0815"
     * </pre>
     */
    public final static HttpHeaderName<ETag> E_TAG = registerConstant("ETag",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.eTag());

    /**
     * A {@link HttpHeaderName} holding <code>EXPIRES</code>
     * <pre>
     * Expires: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> EXPIRES = registerLocalDateTimeConstant("Expires",
            HttpHeaderNameScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Expect</code>
     * <pre>
     * Expect: 100-continue
     * </pre>
     */
    public final static HttpHeaderName<String> EXPECT = registerStringConstant("Expect",
            HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>From</code>
     * <pre>
     * From: webmaster@example.org
     * </pre>
     */
    public final static HttpHeaderName<EmailAddress> FROM = registerConstant("From",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.emailAddress());

    /**
     * A {@link HttpHeaderName} holding <code>Host</code>
     * <pre>
     * Host: <host>:<port>
     * </pre>
     */
    public final static HttpHeaderName<String> HOST = registerStringConstant("Host",
            HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Match</code>
     * <pre>
     * If-Match: "bfc13a64729c4290ef5b2c2730249c88ca92d82d"
     * If-Match: W/"67ab43", "54ed21", "7892dd"
     * If-Match: *
     * </pre>
     */
    public final static HttpHeaderName<List<ETag>> IF_MATCH = registerETagListConstant("If-Match",
            HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Modified-Since</code>
     * <pre>
     * If-Modified-Since: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> IF_MODIFIED_SINCE = registerLocalDateTimeConstant("If-Modified-Since",
            HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-None-Match</code>
     * <pre>
     * If-None-Match: "<etag_value>"
     * If-None-Match: "<etag_value>", "<etag_value>", …
     * If-None-Match: *
     * </pre>
     */
    public final static HttpHeaderName<List<ETag>> IF_NONE_MATCHED = registerETagListConstant("If-None-Match",
            HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>If-Range</code>
     * <pre>
     * If-Range: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<IfRange<?>> IF_RANGE = registerConstant("If-Range",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.ifRange());

    /**
     * A {@link HttpHeaderName} holding <code>If-Unmodified-Since</code>
     * <pre>
     * If-Unmodified-Since: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> IF_UNMODIFIED_SINCE = registerLocalDateTimeConstant("If-Unmodified-Since",
            HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Keep-Alive</code>
     * <pre>
     * Keep-Alive: timeout=5, max=1000
     * </pre>
     */
    public final static HttpHeaderName<String> KEEP_ALIVE = registerStringConstant("Keep-Alive",
            HttpHeaderNameScope.REQUEST_RESPONSE);


    /**
     * A {@link HttpHeaderName} holding <code>Last-Modified</code>
     * <pre>
     * Last-Modified: Wed, 21 Oct 2015 07:28:00 GMT
     * </pre>
     */
    public final static HttpHeaderName<LocalDateTime> LAST_MODIFIED = registerLocalDateTimeConstant("Last-Modified",
            HttpHeaderNameScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Link</code>
     * <a href="http://www.rfc-editor.org/rfc/rfc5988.txt"></a>
     */
    public final static HttpHeaderName<List<Link>> LINK = registerConstant("Link",
            HttpHeaderNameScope.REQUEST_RESPONSE,
            HeaderValueHandler.link());

    /**
     * A {@link HttpHeaderName} holding <code>Location</code>
     * <pre>
     * A relative (to the request URL) or absolute URL.
     * ...
     * Location: /index.html
     * </pre>
     */
    public final static HttpHeaderName<Url> LOCATION = registerConstant("Location",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.url());

    /**
     * A {@link HttpHeaderName} holding <code>Pragma</code>
     * <pre>
     * Pragma: no-cache
     * </pre>
     */
    public final static HttpHeaderName<String> PRAGMA = registerStringConstant("Pragma",
            HttpHeaderNameScope.REQUEST_RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Range</code>
     * <pre>
     * Range: <unit>=<range-start>-
     * Range: <unit>=<range-start>-<range-end>
     * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>
     * Range: <unit>=<range-start>-<range-end>, <range-start>-<range-end>, <range-start>-<range-end>
     * </pre>
     */
    public final static HttpHeaderName<RangeHeaderValue> RANGE = registerConstant("Range",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.range());

    /**
     * A {@link HttpHeaderName} holding <code>Referer</code>
     * <pre>
     * Referer: https://developer.mozilla.org/en-US/docs/Web/JavaScript
     * </pre>
     */
    public final static HttpHeaderName<AbsoluteUrl> REFERER = registerConstant("Referer",
            HttpHeaderNameScope.REQUEST,
            HeaderValueHandler.absoluteUrl());

    /**
     * A {@link HttpHeaderName} holding <code>Retry-After</code>
     * <pre>
     * Retry-After: <http-date>
     * Retry-After: <delay-seconds>
     * </pre>
     */
    public final static HttpHeaderName<String> RETRY_AFTER = registerStringConstant("Retry-After",
            HttpHeaderNameScope.RESPONSE);

    /**
     * A {@link HttpHeaderName} holding <code>Server</code>
     * <pre>
     * Server: Apache/2.4.1 (Unix)
     * </pre>
     */
    public final static HttpHeaderName<String> SERVER = registerStringConstant("Server",
            HttpHeaderNameScope.RESPONSE);

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
    public final static HttpHeaderName<ServerCookie> SET_COOKIE = registerConstant("Set-Cookie",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.serverCookie());

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
    public final static HttpHeaderName<String> TE = registerStringConstant("TE", HttpHeaderNameScope.REQUEST);

    /**
     * A {@link HttpHeaderName} holding <code>Trailer</code>
     * <pre>
     * Trailer: header-names
     * </pre>
     */
    public final static HttpHeaderName<List<HttpHeaderName<?>>> TRAILER = registerConstant("Trailer",
            HttpHeaderNameScope.RESPONSE,
            HeaderValueHandler.httpHeaderNameList());

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
    public final static HttpHeaderName<String> TRANSFER_ENCODING = registerStringConstant("Transfer-Encoding", HttpHeaderNameScope.RESPONSE);

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
            HttpHeaderNameScope.REQUEST);

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
            HttpHeaderNameScope.REQUEST);

    /**
     * Factory that creates a {@link HttpHeaderName}.
     */
    public static HttpHeaderName<?> with(final String name) {
        Objects.requireNonNull(name, "name");

        final HttpHeaderName<?> httpHeaderName = CONSTANTS.get(name);
        return null != httpHeaderName ?
                httpHeaderName :
                new HttpHeaderName<>(checkName(name), HttpHeaderNameScope.UNKNOWN, HeaderValueHandler.string(), NOT_CONDITIONAL, NOT_CONTENT);
    }

    private static String checkName(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", PREDICATE);
        return name;
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
                           final HttpHeaderNameScope scope,
                           final HeaderValueHandler<T> handler,
                           final boolean conditional,
                           final boolean content) {
        super(name);
        this.scope = scope;
        this.handler = handler;
        this.conditional = conditional;
        this.content = content;
    }

    /**
     * This method will return a reference to {@link HttpHeaderName} with a type parameter of {@link String} otherwise
     * this will fail.
     */
    public HttpHeaderName<String> stringValues() {
        return this.handler.httpHeaderNameCast(this);
    }

    private final static boolean NOT_CONDITIONAL = false;

    /**
     * Only conditional headers return true.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Conditional_requests"></a>
     * <pre>
     * Conditional headersSection
     * Several HTTP headers, called conditional headers, lead to conditional requests. These are:
     *
     * If-Match
     * Succeeds if the ETag of the distant resource is equal to one listed in this header. By default, unless the etag is prefixed with 'W/', it performs a strong validation.
     * If-None-Match
     * Succeeds if the ETag of the distant resource is different to each listed in this header. By default, unless the etag is prefixed with 'W/', it performs a strong validation.
     * If-Modified-Since
     * Succeeds if the Last-Modified date of the distant resource is more recent than the one given in this header.
     * If-Unmodified-Since
     * Succeeds if the Last-Modified date of the distant resource is older or the same than the one given in this header.
     * If-Range
     * Similar to If-Match, or If-Unmodified-Since, but can have only one single etag, or one date. If it fails, the range request fails, and instead of a 206 Partial Content response, a 200 OK is sent with the complete resource.
     * </pre>
     */
    public boolean isConditional() {
        return this.conditional;
    }

    private final boolean conditional;

    private final static boolean NOT_CONTENT = false;

    /**
     * Only returns true if this is a content header. Content headers, are those that begin with "content-".
     */
    public boolean isContent() {
        return this.content;
    }

    private final boolean content;

    /**
     * Validates the value.
     */
    @Override
    public T checkValue(final Object value) {
        return this.handler.check(value, this);
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

        return this.handler.parse(value, this);
    }

    /**
     * Formats the given typed value into header value text.
     */
    public String headerText(final T value) {
        Objects.requireNonNull(value, "value");

        return this.handler.toText(value, this);
    }

    private final HeaderValueHandler<T> handler;

    // HttpRequestAttribute..............................................................................................

    /**
     * A typed getter that retrieves a value from a {@link HttpRequest}
     */
    @Override
    public Optional<T> parameterValue(final HttpRequest request) {
        return this.headerValue(request.headers());
    }

    // HasHttpHeaderNameScope ..........................................................................................

    @Override
    public final boolean isMultipart() {
        return this.scope.isMultipart();
    }

    @Override
    public final boolean isRequest() {
        return this.scope.isRequest();
    }

    @Override
    public final boolean isResponse() {
        return this.scope.isResponse();
    }

    private final HttpHeaderNameScope scope;

    // HeaderName2......................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof HttpHeaderName;
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final HttpHeaderName<?> other) {
        return this.compareTo0(other);
    }
}
