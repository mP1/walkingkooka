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
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Cookie} that is sent by servers to a client. Holds a cookie including all its properties. Note that values are verified to contain correct
 * characters but no attempt is made to encode values with incorrect values.
 */
final public class ServerCookie extends Cookie {

    public final static Optional<String> NO_DOMAIN = Optional.empty();
    public final static Optional<String> NO_PATH = Optional.empty();
    public final static Optional<String> NO_COMMENT = Optional.empty();
    public final static Optional<CookieDeletion> NO_DELETION = Optional.empty();

    /**
     * Server cookie
     *
     * <pre>
     * Set-Cookie: LSID=DQAAAK�Eaem_vYg; Domain=docs.foo.com; Path=/accounts; Expires=Wed, 13-Jan-2021 22:23:01 GMT; Secure; HttpOnly
     * </pre>
     */
    static ServerCookie parseHeader(final String header) {
        Objects.requireNonNull(header, "header");

        final String[] tokens = header.split(PARAMETER_SEPARATOR.string());
        final int length = tokens.length;
        if (length < 1) {
            throw new IllegalArgumentException(
                    "Server header missing cookie name/value=" + CharSequences.quoteAndEscape(header));
        }

        // first two tokens must be name and value.
        final String nameAndValue = tokens[0];
        final int nameEnd = nameAndValue.indexOf(PARAMETER_NAME_VALUE_SEPARATOR.character());

        final CookieName name = CookieName.with(nameAndValue.substring(0, -1 == nameEnd ? nameAndValue.length() : nameEnd).trim());
        final String value = extractValue(nameAndValue);

        Optional<String> domain = NO_DOMAIN;
        Optional<String> path = NO_PATH;
        Optional<String> comment = NO_COMMENT;
        Optional<CookieDeletion> deletion = NO_DELETION;
        CookieSecure secure = CookieSecure.ABSENT;
        CookieHttpOnly httpOnly = CookieHttpOnly.ABSENT;
        CookieVersion version = CookieVersion.DEFAULT;

        for (int i = 1; i < length; i++) {
            final String token = tokens[i].trim();

            if (CaseSensitivity.INSENSITIVE.startsWith(token, Cookie.DOMAIN + NAME_VALUE_SEPARATOR)) {
                domain = Optional.ofNullable(extractValueOrNull(token));
                continue;
            }
            if (CaseSensitivity.INSENSITIVE.startsWith(token, Cookie.PATH + NAME_VALUE_SEPARATOR)) {
                path = Optional.ofNullable(extractValueOrNull(token));
                continue;
            }
            if (CaseSensitivity.INSENSITIVE.startsWith(token, Cookie.EXPIRES + NAME_VALUE_SEPARATOR)) {
                //  For other browsers, if both (Expires and Max-Age) are set, Max-Age will have precedence.
                if (!deletion.isPresent()) {
                    deletion = CookieExpires.parseExpires(extractValue(token));
                }
                continue;
            }
            if (CaseSensitivity.INSENSITIVE.startsWith(token, Cookie.MAX_AGE + NAME_VALUE_SEPARATOR)) {
                deletion = CookieDeletion.maxAge(Integer.parseInt(extractValue(token)));
                continue;
            }
            if (token.equalsIgnoreCase(Cookie.SECURE)) {
                secure = CookieSecure.PRESENT;
                continue;
            }
            if (token.equalsIgnoreCase(Cookie.HTTP_ONLY)) {
                httpOnly = CookieHttpOnly.PRESENT;
                continue;
            }
        }

        return new ServerCookie(name, value, domain, path, comment, deletion, secure, httpOnly, version);
    }

    private final static char NAME_VALUE_SEPARATOR = '=';

    /**
     * Converts a {@link javax.servlet.http.Cookie} into a {@link ServerCookie}.
     */
    static ServerCookie from(final javax.servlet.http.Cookie cookie) {
        Objects.requireNonNull(cookie, "cookie");

        return new ServerCookie(CookieName.with(cookie.getName()), //
                emptyToNull(cookie.getValue()), //
                toOptional(cookie.getDomain()), //
                toOptional(cookie.getPath()), //
                toOptional(cookie.getComment()), //
                CookieDeletion.maxAge(cookie.getMaxAge()), // auto expires
                CookieSecure.fromJavaxSecureCookie(cookie.getSecure()), //
                CookieHttpOnly.fromJavaxSecureCookie(cookie.isHttpOnly()), //
                CookieVersion.from(cookie.getVersion()));
    }

    /**
     * Converts empty to nulls.
     */
    private static Optional<String> toOptional(final String value) {
        return Optional.ofNullable(emptyToNull(value));
    }

    /**
     * Converts empty to nulls.
     */
    private static String emptyToNull(final String value) {
        return CharSequences.isNullOrEmpty(value) ? null : value;
    }

    /**
     * Factory that creates a {@link ServerCookie}
     */
    static ServerCookie with(final CookieName name,
                             final String value,
                             final Optional<String> domain,
                             final Optional<String> path,
                             final Optional<String> comment,
                             final Optional<CookieDeletion> deletion,
                             final CookieSecure secure,
                             final CookieHttpOnly httpOnly,
                             final CookieVersion version) {
        checkName(name);
        checkValue(value);
        checkComment(comment);
        checkPath(path);
        checkDeletion(deletion);
        checkSecure(secure);
        checkHttpOnly(httpOnly);
        checkVersion(version);

        return new ServerCookie(name, //
                value, //
                domain, //
                path, //
                comment, //
                deletion, //
                secure, //
                httpOnly, //
                version);
    }

    /**
     * Private constructor use static factory
     */
    private ServerCookie(final CookieName name,
                         final String value,
                         final Optional<String> domain,
                         final Optional<String> path,
                         final Optional<String> comment,
                         final Optional<CookieDeletion> deletion,
                         final CookieSecure secure,
                         final CookieHttpOnly httpOnly,
                         final CookieVersion version) {
        super(name, value);
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.comment = comment;
        this.deletion = deletion;
        this.httpOnly = httpOnly;
        this.version = version;
    }

    @Override
    public ServerCookie setName(final CookieName name) {
        return this.setName0(name).cast();
    }

    @Override
    public ServerCookie setValue(final String value) {
        return this.setValue0(value).cast();
    }

    @Override
    Cookie replace(final CookieName name, final String value) {
        return new ServerCookie(name, value, this.domain, this.path, this.comment, this.deletion, this.secure, this.httpOnly, this.version);
    }

    // domain ........................................................................................................

    /**
     * Getter that returns the domain for this cookie.
     */
    public Optional<String> domain() {
        return this.domain;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given domain, creating a new instance if necessary.
     */
    public ServerCookie setDomain(final Optional<String> domain) {
        checkDomain(domain);

        return this.domain.equals(domain) ?
                this :
                new ServerCookie(this.name, this.value, domain, this.path, this.comment, this.deletion, this.secure, this.httpOnly, this.version);
    }

    private final Optional<String> domain;

    private static void checkDomain(final Optional<String> domain) {
        Objects.requireNonNull(domain, "domain");
    }

    // path ........................................................................................................

    /**
     * Getter that returns the path for this cookie.
     */
    public Optional<String> path() {
        return this.path;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given path, creating a new instance if necessary.
     */
    public ServerCookie setPath(final Optional<String> path) {
        checkPath(path);

        return this.path.equals(path) ?
                this :
                new ServerCookie(this.name, this.value, this.domain, path, this.comment, this.deletion, this.secure, this.httpOnly, this.version);
    }

    private final Optional<String> path;

    private static void checkPath(final Optional<String> path) {
        Objects.requireNonNull(path, "path");
    }

    // comment ........................................................................................................

    /**
     * Getter that returns the comments for this cookie.
     */
    public Optional<String> comment() {
        return this.comment;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given comment, creating a new instance if necessary.
     */
    public ServerCookie setComment(final Optional<String> comment) {
        checkComment(comment);

        return this.comment.equals(comment) ?
                this :
                new ServerCookie(this.name, this.value, this.domain, this.path, comment, this.deletion, this.secure, this.httpOnly, this.version);
    }

    private final Optional<String> comment;

    private static void checkComment(final Optional<String> comment) {
        Objects.requireNonNull(comment, "comment");
    }

    // deletion ........................................................................................................

    /**
     * Returns the deletion attribute.
     */
    public Optional<CookieDeletion> deletion() {
        return this.deletion;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given deletion, creating a new instance if necessary.
     */
    public ServerCookie setDeletion(final Optional<CookieDeletion> deletion) {
        checkDeletion(deletion);

        return this.deletion.equals(deletion) ?
                this :
                new ServerCookie(this.name, this.value, this.domain, this.path, this.comment, deletion, this.secure, this.httpOnly, this.version);
    }

    private final Optional<CookieDeletion> deletion;

    private static void checkDeletion(final Optional<CookieDeletion> deletion) {
        Objects.requireNonNull(deletion, "deletion");
    }

    // secure ........................................................................................................

    /**
     * Tests if this cookie secure
     */
    public CookieSecure secure() {
        return this.secure;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given secure, creating a new instance if necessary.
     */
    public ServerCookie setSecure(final CookieSecure secure) {
        checkSecure(secure);

        return this.secure.equals(secure) ?
                this :
                new ServerCookie(this.name, this.value, this.domain, this.path, this.comment, this.deletion, secure, this.httpOnly, this.version);
    }

    private final CookieSecure secure;

    private static void checkSecure(final CookieSecure secure) {
        Objects.requireNonNull(secure, "secure");
    }

    // httpOnly ........................................................................................................

    /**
     * Tests if this cookie httpOnly
     */
    public CookieHttpOnly httpOnly() {
        return this.httpOnly;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given httpOnly, creating a new instance if necessary.
     */
    public ServerCookie setHttpOnly(final CookieHttpOnly httpOnly) {
        checkHttpOnly(httpOnly);

        return this.httpOnly.equals(httpOnly) ?
                this :
                new ServerCookie(this.name, this.value, this.domain, this.path, this.comment, this.deletion, this.secure, httpOnly, this.version);
    }

    private final CookieHttpOnly httpOnly;

    private static void checkHttpOnly(final CookieHttpOnly httpOnly) {
        Objects.requireNonNull(httpOnly, "httpOnly");
    }

    // version.........................................................................................................

    /**
     * Returns the cookie version.
     */
    public CookieVersion version() {
        return this.version;
    }

    /**
     * Would be setter that returns a {@link ServerCookie} with the given deletion, creating a new instance if necessary.
     */
    public ServerCookie setVersion(final CookieVersion version) {
        checkVersion(version);
        return this.version.equals(version) ?
                this :
                new ServerCookie(this.name, this.value, this.domain, this.path, this.comment, deletion, this.secure, this.httpOnly, version);
    }

    private final CookieVersion version;

    private static void checkVersion(final CookieVersion version) {
        Objects.requireNonNull(version, "version");
    }

    // isXXX..........................................................................................................

    /**
     * Always returns false.
     */
    @Override
    public boolean isClient() {
        return false;
    }

    /**
     * Always returns true.
     */
    @Override
    public boolean isServer() {
        return true;
    }

    /**
     * Always returns {@link HttpHeaderName#SET_COOKIE}.
     */
    @Override
    public HttpHeaderName header() {
        return HttpHeaderName.SET_COOKIE;
    }

    /**
     * Returns this cookie in header value form.
     */
    @Override
    public String toHeaderText() {
        return this.toString();
    }

    /**
     * Converts this cookie into a {@link javax.servlet.http.Cookie}.
     * The {@link LocalDateTime} is only used if a max age needs to be calculated from a set deletion.
     */
    public javax.servlet.http.Cookie toJavaxServletCookie(final LocalDateTime now) {
        Objects.requireNonNull(now, "now");

        final javax.servlet.http.Cookie cookie = this.createJavaxServletCookieWithNameAndValue();

        this.domain.ifPresent((d) -> cookie.setDomain(d));
        this.path.ifPresent((p) -> cookie.setPath(p));
        this.comment.ifPresent((c) -> cookie.setComment(c));
        this.deletion.ifPresent((d) -> cookie.setMaxAge(d.toMaxAgeSeconds(now)));

        cookie.setSecure(this.secure.toJavaxServletCookieSecure());
        cookie.setHttpOnly(this.httpOnly.toJavaxServletCookieHttpOnly());

        cookie.setVersion(this.version.value());
        return cookie;
    }

    /**
     * Creates a {@link ClientCookie} from this {@link ServerCookie} which shares the name and value.
     */
    public ClientCookie toClient() {
        return ClientCookie.from(this);
    }

    /**
     * Returns true if this a session cookie.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies">Cookies</a>
     * <pre>
     * Session cookiesSection
     * The cookie created above is a session cookie: it is deleted when the client shuts down, because it didn't specify an Expires or Max-Age directive. However, web browsers may use session restoring, which makes most session cookies permanent, as if the browser was never closed.
     * </pre>
     */
    public boolean isSession() {
        return !this.isPermanent();
    }

    /**
     * Returns true if this a permanent cookie, basically the inverse of session.
     * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Cookies">Cookies</a>
     * <pre>
     * Permanent cookiesSection
     * Instead of expiring when the client closes, permanent cookies expire at a specific date (Expires) or after a specific length of time (Max-Age).
     *
     * Set-Cookie: id=a3fWa; Expires=Wed, 21 Oct 2015 07:28:00 GMT;
     * </pre>
     */
    public boolean isPermanent() {
        return this.deletion().isPresent();
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public boolean isRequest() {
        return false;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // Object ..........................................................................

    /**
     * Includes special logic to handle null paths.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.name, //
                this.value,
                this.hashDomain(), //
                hashPath(), //
                this.comment, //
                this.deletion, //
                this.secure, //
                this.httpOnly, //
                this.version);
    }

    private int hashDomain() {
        return this.domain
                .map(DOMAIN_CASE_SENSITIVITY::hash)
                .orElse(0);
    }

    private int hashPath() {
        return 0; // skip the normalizing step.
    }

    // Object .................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ServerCookie;
    }

    @Override
    boolean equals1(final Cookie other) {
        return this.equals2(Cast.to(other));
    }

    /**
     * Normalizes paths before testing equality them for equality.
     */
    private boolean equals2(final ServerCookie other) {
        return domainEquals(this.domain, other.domain) && //
                this.path.equals(other.path) && //
                this.comment.equals(other.comment) && //
                this.deletion.equals(other.deletion) && //
                this.secure == other.secure && //
                this.httpOnly == other.httpOnly && //
                this.version == other.version;
    }

    /**
     * Domains are compared ignoring case, hence the special case.
     */
    private static boolean domainEquals(final Optional<String> domain, final Optional<String> otherDomain) {
        final boolean domainPresent = domain.isPresent();
        return domainPresent == otherDomain.isPresent() &&
                (!domainPresent || DOMAIN_CASE_SENSITIVITY.equals(domain.get(), otherDomain.get()));
    }

    private final static CaseSensitivity DOMAIN_CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Does nothing client cookies only have name and value which is already added.
     */
    @Override
    void appendAttributes(final ToStringBuilder builder) {
        // was disabled previously in Cookie so values=empty string arent skipped.
        builder.enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        builder.label(Cookie.DOMAIN).value(this.domain);
        builder.label(Cookie.PATH).value(this.path);
        builder.label("").value(this.deletion);

        this.secure.appendToCookieToString(builder);
        this.httpOnly.appendToCookieToString(builder);
    }
}
