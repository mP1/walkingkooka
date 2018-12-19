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

package walkingkooka.net.http.cookie;

import org.junit.Test;
import walkingkooka.net.http.HttpHeaderName;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class ServerCookieTest extends CookieTestCase<ServerCookie> {

    // constants
    private final static LocalDateTime NOW = LocalDateTime.of(2000, 12, 31, 12, 58, 59);

    private final static Optional<String> DOMAIN = Optional.of("example.com");
    private final static Optional<String> PATH = Optional.of("/PATH/TO");
    private final static Optional<String> COMMENT = Optional.of("// comment");
    private final static Optional<CookieDeletion> MAXAGE = CookieDeletion.maxAge(123);
    private final static Optional<CookieDeletion> EXPIRES = Optional.of(CookieDeletion.expires(NOW));
    private final static CookieSecure SECURE = CookieSecure.PRESENT;
    private final static CookieHttpOnly HTTPONLY = CookieHttpOnly.PRESENT;
    private final static CookieVersion VERSION = CookieVersion.VERSION_0;

    // tests

    @Test(expected = NullPointerException.class)
    public void testWithNullPathFails() {
        ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                null,
                COMMENT,
                MAXAGE,
                SECURE,
                HTTPONLY,
                VERSION);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullCommentFails() {
        ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                null,
                MAXAGE,
                SECURE,
                HTTPONLY,
                VERSION);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullDeletionFails() {
        ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                null,
                SECURE,
                HTTPONLY,
                VERSION);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullSecureFails() {
        ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                MAXAGE,
                null,
                HTTPONLY,
                VERSION);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullHttpOnlyFails() {
        ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                MAXAGE,
                SECURE,
                null,
                VERSION);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullVersionFails() {
        ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                MAXAGE,
                SECURE,
                HTTPONLY,
                null);
    }

    @Test
    public void testWith() {
        final ServerCookie cookie = ServerCookie.with(NAME,
                VALUE,
                DOMAIN,
                PATH,
                COMMENT,
                MAXAGE,
                SECURE,
                HTTPONLY,
                VERSION);
        checkName(cookie);
        checkValue(cookie);
        checkDomain(cookie);
        checkPath(cookie);
        checkComment(cookie);
        checkDeletion(cookie);
        checkSecure(cookie);
        checkHttpOnly(cookie);
        checkVersion(cookie);
    }

    @Test
    public void testWithValueInQuotes() {
        final String value = "\"123\"";
        final ServerCookie cookie = ServerCookie.with(NAME,
                value,
                DOMAIN,
                PATH,
                COMMENT,
                MAXAGE,
                SECURE,
                HTTPONLY,
                VERSION);
        checkName(cookie);
        checkValue(cookie, value);
        checkDomain(cookie);
        checkPath(cookie);
        checkComment(cookie);
        checkDeletion(cookie);
        checkSecure(cookie);
        checkHttpOnly(cookie);
        checkVersion(cookie);
    }

    @Test
    public void testWithoutAttributes() {
        final ServerCookie cookie = this.createCookieWithoutAttributes(NAME, VALUE);
        checkName(cookie);
        checkValue(cookie);
        checkDomain(cookie, ServerCookie.NO_DOMAIN);
        checkPath(cookie, ServerCookie.NO_PATH);
        checkComment(cookie, ServerCookie.NO_COMMENT);
        checkDeletion(cookie, ServerCookie.NO_DELETION);
        checkSecure(cookie, CookieSecure.ABSENT);
        checkHttpOnly(cookie, CookieHttpOnly.ABSENT);
        checkVersion(cookie);
    }

    @Test
    public void testSetNameDifferent() {
        final ServerCookie cookie = this.createCookieWithoutAttributes(NAME, VALUE);
        final CookieName name = CookieName.with("different");
        final ServerCookie different = cookie.setName(name);

        checkName(different, name);
        checkValue(different);
        checkDomain(different, ServerCookie.NO_DOMAIN);
        checkPath(different, ServerCookie.NO_PATH);
        checkComment(different, ServerCookie.NO_COMMENT);
        checkDeletion(different, ServerCookie.NO_DELETION);
        checkSecure(different, CookieSecure.ABSENT);
        checkHttpOnly(different, CookieHttpOnly.ABSENT);
        checkVersion(different);
    }

    @Test
    public void testSetValueDifferent() {
        final ServerCookie cookie = this.createCookieWithoutAttributes(NAME, VALUE);
        final String value = "different";
        final ServerCookie different = cookie.setValue(value);

        checkName(different);
        checkValue(different, value);
        checkDomain(different, ServerCookie.NO_DOMAIN);
        checkPath(different, ServerCookie.NO_PATH);
        checkComment(different, ServerCookie.NO_COMMENT);
        checkDeletion(different, ServerCookie.NO_DELETION);
        checkSecure(different, CookieSecure.ABSENT);
        checkHttpOnly(different, CookieHttpOnly.ABSENT);
        checkVersion(different);
    }

    // setDomain ...........................................................................................

    @Test
    public void testSetDomainSame() {
        final ServerCookie cookie = this.createCookie();
        assertSame(cookie, cookie.setDomain(DOMAIN));
    }

    @Test
    public void testSetDomainDifferent() {
        final ServerCookie cookie = this.createCookie();
        final Optional<String> domain = Optional.of("different.example.com");
        final ServerCookie different = cookie.setDomain(domain);

        checkName(different);
        checkValue(different);
        checkDomain(different, domain);
        checkPath(different);
        checkComment(different);
        checkDeletion(different);
        checkSecure(different);
        checkHttpOnly(different);
        checkVersion(different);
    }

    // setPath ...........................................................................................

    @Test
    public void testSetPathSame() {
        final ServerCookie cookie = this.createCookie();
        assertSame(cookie, cookie.setPath(PATH));
    }

    @Test
    public void testSetPathDifferent() {
        final ServerCookie cookie = this.createCookie();
        final Optional<String> path = Optional.of("/different/path");
        final ServerCookie different = cookie.setPath(path);

        checkName(different);
        checkValue(different);
        checkDomain(different);
        checkPath(different, path);
        checkComment(different);
        checkDeletion(different);
        checkSecure(different);
        checkHttpOnly(different);
        checkVersion(different);
    }

    // setDomain ...........................................................................................

    @Test
    public void testSetCommentSame() {
        final ServerCookie cookie = this.createCookie();
        assertSame(cookie, cookie.setComment(COMMENT));
    }

    @Test
    public void testSetCommentDifferent() {
        final ServerCookie cookie = this.createCookie();
        final Optional<String> comment = Optional.of("different comment");
        final ServerCookie different = cookie.setComment(comment);

        checkName(different);
        checkValue(different);
        checkDomain(different);
        checkPath(different);
        checkComment(different, comment);
        checkDeletion(different);
        checkSecure(different);
        checkHttpOnly(different);
        checkVersion(different);
    }

    // setDeletion ...........................................................................................

    @Test
    public void testSetDeletionSame() {
        final ServerCookie cookie = this.createCookie();
        assertSame(cookie, cookie.setDeletion(MAXAGE));
    }

    @Test
    public void testSetDeletionDifferent() {
        final ServerCookie cookie = this.createCookie();
        final Optional<CookieDeletion> deletion = CookieDeletion.maxAge(999);
        final ServerCookie different = cookie.setDeletion(deletion);

        checkName(different);
        checkValue(different);
        checkDomain(different);
        checkPath(different);
        checkComment(different);
        checkDeletion(different, deletion);
        checkSecure(different);
        checkHttpOnly(different);
        checkVersion(different);
    }

    // setSecure ...........................................................................................

    @Test
    public void testSetSecureSame() {
        final ServerCookie cookie = this.createCookie();
        assertSame(cookie, cookie.setSecure(SECURE));
    }

    @Test
    public void testSetSecureDifferent() {
        final ServerCookie cookie = this.createCookie();
        final CookieSecure secure = CookieSecure.ABSENT;
        assertNotSame(secure, SECURE);
        final ServerCookie different = cookie.setSecure(secure);

        checkName(different);
        checkValue(different);
        checkDomain(different);
        checkPath(different);
        checkComment(different);
        checkDeletion(different);
        checkSecure(different, secure);
        checkHttpOnly(different);
        checkVersion(different);
    }

    // setHttpOnly ...........................................................................................

    @Test
    public void testSetHttpOnlySame() {
        final ServerCookie cookie = this.createCookie();
        assertSame(cookie, cookie.setHttpOnly(HTTPONLY));
    }

    @Test
    public void testSetHttpOnlyDifferent() {
        final ServerCookie cookie = this.createCookie();
        final CookieHttpOnly httpOnly = CookieHttpOnly.ABSENT;
        assertNotSame(HTTPONLY, httpOnly);
        final ServerCookie different = cookie.setHttpOnly(httpOnly);

        checkName(different);
        checkValue(different);
        checkDomain(different);
        checkPath(different);
        checkComment(different);
        checkDeletion(different);
        checkSecure(different);
        checkHttpOnly(different, httpOnly);
        checkVersion(different);
    }
    // setVersion ...........................................................................................

    @Test
    public void testSetVersionDifferent() {
        final ServerCookie cookie = this.createCookie();
        final CookieVersion version = CookieVersion.VERSION_1;
        assertNotSame(version, VERSION);
        final ServerCookie different = cookie.setVersion(version);

        checkName(different);
        checkValue(different);
        checkDomain(different);
        checkPath(different);
        checkComment(different);
        checkDeletion(different);
        checkSecure(different);
        checkHttpOnly(different);
        checkVersion(different, version);
    }

    // isXXX.................................................................

    @Test
    public void testIsClient() {
        final ServerCookie cookie = this.createCookie();
        assertFalse(cookie.isClient());
    }

    @Test
    public void testIsServer() {
        final ServerCookie cookie = this.createCookie();
        assertTrue(cookie.isServer());
    }

    // from javax.servlet.http.Cookie......................................................................

    @Test(expected = NullPointerException.class)
    public void testFromNullCookieFails() {
        ServerCookie.from(null);
    }

    @Test
    public void testFromJavaxServletHttpCookie() {
        final int seconds = 123;
        final javax.servlet.http.Cookie servletCookie = new javax.servlet.http.Cookie(NAME.value(), VALUE);
        servletCookie.setPath(PATH.get());
        servletCookie.setComment(COMMENT.get());
        servletCookie.setDomain(DOMAIN.get());
        servletCookie.setMaxAge(seconds);
        servletCookie.setSecure(SECURE.toJavaxServletCookieSecure());
        servletCookie.setVersion(VERSION.value());

        final ServerCookie cookie = ServerCookie.from(servletCookie);
        checkName(cookie);
        checkValue(cookie);
        checkDomain(cookie);
        checkPath(cookie);
        checkComment(cookie);
        checkDeletion(cookie);
        checkSecure(cookie);
        checkHttpOnly(cookie, CookieHttpOnly.ABSENT);
        checkVersion(cookie);
    }

    @Test
    public void testFromCookieWithOnlyNameAndValue() {
        final ServerCookie cookie = ServerCookie.from(new javax.servlet.http.Cookie(NAME.value(), VALUE));
        checkName(cookie);
        checkValue(cookie);
        checkDomain(cookie, ServerCookie.NO_DOMAIN);
        checkPath(cookie, ServerCookie.NO_PATH);
        checkComment(cookie, ServerCookie.NO_COMMENT);
        checkDeletion(cookie, ServerCookie.NO_DELETION);
        checkSecure(cookie, CookieSecure.ABSENT);
        checkHttpOnly(cookie, CookieHttpOnly.DEFAULT);
        checkVersion(cookie, CookieVersion.VERSION_0);
    }

    @Test(expected = NullPointerException.class)
    public void testToJavaxServletHttpCookieWithNullNowFails() {
        this.createCookie().toJavaxServletCookie(null);
    }

    @Test
    public void testToJavaxServletHttpCookie() {
        final javax.servlet.http.Cookie cookie = this.createCookie()
                .toJavaxServletCookie(NOW);
        checkName(cookie);
        checkValue(cookie);
        checkDomain(cookie);
        checkPath(cookie);
        checkComment(cookie);
        checkDeletion(cookie);
        checkSecure(cookie);
        checkVersion(cookie);
    }

    @Test
    public void testToJavaxServletHttpCookieWithOnlyNameAndValue() {
        final javax.servlet.http.Cookie cookie = ServerCookie.with(NAME,
                VALUE,
                ServerCookie.NO_DOMAIN,
                ServerCookie.NO_PATH,
                ServerCookie.NO_COMMENT,
                ServerCookie.NO_DELETION,
                CookieSecure.ABSENT,
                CookieHttpOnly.ABSENT,
                VERSION)
                .toJavaxServletCookie(NOW);
        checkName(cookie);
        checkValue(cookie);
        checkDomain(cookie, null);
        checkPath(cookie, null);
        checkComment(cookie, null);
        checkDeletion(cookie, -1);
        checkSecure(cookie, CookieSecure.ABSENT);
        checkHttpOnly(cookie, CookieHttpOnly.ABSENT);
        checkVersion(cookie);
    }

    // parseServerHeader.......................................................................

    @Test(expected = NullPointerException.class)
    public void testParseServerHeaderNullFails() {
        Cookie.parseServerHeader(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testParseServerHeaderEmptyFails() {
        Cookie.parseServerHeader("");
    }

    @Test
    public void testParseServerHeaderNameAndValue() {
        this.parseHeaderAndCheck("cookie123=value456;", createCookieWithoutAttributes(NAME, VALUE));
    }

    @Test
    public void testParseServerHeaderNameAndValue3() {
        this.parseHeaderAndCheck("cookie123=;", createCookieWithoutAttributes(NAME, ""));
    }

    @Test
    public void testParseServerHeaderDomain() {
        this.parseHeaderAndCheck("cookie123=value456;domain=example.com;",
                NAME,
                VALUE,
                DOMAIN,
                ServerCookie.NO_PATH);
    }

    @Test
    public void testParseServerHeaderDomain2() {
        this.parseHeaderAndCheck("cookie123=value456;DOMAIN=example.com;",
                NAME,
                VALUE,
                DOMAIN,
                ServerCookie.NO_PATH);
    }

    @Test
    public void testParseServerHeaderDomain3() {
        this.parseHeaderAndCheck("cookie123=value456;DOMAIN=example.com",
                NAME,
                VALUE,
                DOMAIN,
                ServerCookie.NO_PATH);
    }

    @Test
    public void testParseServerHeaderPath() {
        this.parseHeaderAndCheck("cookie123=value456;path=/PATH/TO;",
                NAME,
                VALUE,
                ServerCookie.NO_DOMAIN,
                PATH);
    }

    @Test
    public void testParseServerHeaderPath2() {
        this.parseHeaderAndCheck("cookie123=value456;Path=/PATH/TO;",
                NAME,
                VALUE,
                ServerCookie.NO_DOMAIN,
                PATH);
    }

    @Test
    public void testParseServerHeaderPath3() {
        this.parseHeaderAndCheck("cookie123=value456;Path=/PATH/TO",
                NAME,
                VALUE,
                ServerCookie.NO_DOMAIN,
                PATH);
    }

    @Test
    public void testParseServerHeaderDomainAndPath() {
        this.parseHeaderAndCheck("cookie123=value456;domain=example.com;path=/PATH/TO;",
                NAME,
                VALUE,
                DOMAIN,
                PATH);
    }

    @Test
    public void testParseServerHeaderDomainAndPath2() {
        this.parseHeaderAndCheck("cookie2=value2;domain=example.com;path=/PATH/TO;",
                "cookie2",
                "value2",
                DOMAIN,
                PATH);
    }

    private void parseHeaderAndCheck(final String header,
                                     final String name,
                                     final String value,
                                     final Optional<String> domain,
                                     final Optional<String> path) {
        this.parseHeaderAndCheck(header, CookieName.with(name), value, domain, path);
    }

    private void parseHeaderAndCheck(final String header,
                                     final CookieName name,
                                     final String value,
                                     final Optional<String> domain,
                                     final Optional<String> path) {
        this.parseHeaderAndCheck(header,
                ServerCookie.with(name,
                        value,
                        domain,
                        path,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        CookieVersion.VERSION_0));
    }

    @Test
    public void testParseServerHeaderMaxAge() {
        this.parseHeaderAndCheck("cookie123=value456;max-age=123;", NAME, VALUE,
                MAXAGE);
    }

    @Test
    public void testParseServerHeaderMaxAge2() {
        this.parseHeaderAndCheck("cookie123=value456;MAX-AGE=123;", NAME, VALUE,
                MAXAGE);
    }

    @Test
    public void testParseServerHeaderMaxAge3() {
        this.parseHeaderAndCheck("cookie123=value456;max-age=123", NAME, VALUE,
                MAXAGE);
    }

    @Test
    public void testParseServerHeaderExpires() {
        this.parseHeaderAndCheck("cookie123=value456;expires=Sun, 31 Dec 2000 12:58:59 GMT;",
                NAME,
                VALUE,
                EXPIRES);
    }

    @Test
    public void testParseServerHeaderExpires2() {
        this.parseHeaderAndCheck("cookie123=value456;EXPIRES=Sun, 31 Dec 2000 12:58:59 GMT;",
                NAME,
                VALUE,
                EXPIRES);
    }

    @Test
    public void testParseServerHeaderExpires3() {
        this.parseHeaderAndCheck("cookie456=value789;expires=Sun, 31 Dec 2000 12:58:59 GMT",
                "cookie456",
                "value789",
                EXPIRES);
    }

    // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Set-Cookie
    // For other browsers, if both (Expires and Max-Age) are set, Max-Age will have precedence.

    @Test
    public void testParseServerHeaderMaxAgeExpires() {
        this.parseHeaderAndCheck("cookie123=value456;max-age=123;expires=Sun, 31 Dec 2000 12:58:59 GMT;",
                NAME,
                VALUE,
                MAXAGE);
    }

    @Test
    public void testParseServerHeaderExpiresMaxAge() {
        this.parseHeaderAndCheck("cookie123=value456;expires=Sun, 31 Dec 2000 12:58:59 GMT;max-age=123;",
                NAME,
                VALUE,
                MAXAGE);
    }

    private void parseHeaderAndCheck(final String header,
                                     final String name,
                                     final String value,
                                     final Optional<CookieDeletion> deletion) {
        this.parseHeaderAndCheck(header,
                CookieName.with(name),
                value,
                deletion);
    }

    private void parseHeaderAndCheck(final String header,
                                     final CookieName name,
                                     final String value,
                                     final Optional<CookieDeletion> deletion) {
        this.parseHeaderAndCheck(header,
                ServerCookie.with(name,
                        value,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        deletion,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        CookieVersion.VERSION_0));
    }

    @Test
    public void testParseServerHeaderSecure() {
        this.parseHeaderAndCheck("cookie123=value456;secure", NAME, VALUE, CookieSecure.PRESENT);
    }

    @Test
    public void testParseServerHeaderSecure2() {
        this.parseHeaderAndCheck("cookie2=value2;secure", "cookie2", "value2", CookieSecure.PRESENT);
    }

    private void parseHeaderAndCheck(final String header, final String name, final String value,
                                     final CookieSecure secure) {
        this.parseHeaderAndCheck(header, CookieName.with(name), value, secure);
    }

    private void parseHeaderAndCheck(final String header, final CookieName name, final String value,
                                     final CookieSecure secure) {
        this.parseHeaderAndCheck(header,
                ServerCookie.with(name,
                        value,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        secure,
                        CookieHttpOnly.ABSENT,
                        CookieVersion.VERSION_0));
    }

    @Test
    public void testParseServerHeaderAllAttributes() {
        this.parseHeaderAndCheck("cookie123=value456;domain=example.com;path=/PATH/TO;expires=Sun, 31 Dec 2000 12:58:59 GMT;secure;", //
                ServerCookie.with(NAME,
                        VALUE,
                        DOMAIN,
                        PATH,
                        ServerCookie.NO_COMMENT,
                        EXPIRES,
                        CookieSecure.PRESENT,
                        CookieHttpOnly.ABSENT,
                        VERSION));
    }

    @Test
    public void testParseServerHeaderAllAttributesWhitespace() {
        this.parseHeaderAndCheck("cookie123=value456; domain=example.com; path=/PATH/TO; expires=Sun, 31 Dec 2000 12:58:59 GMT; secure;", //
                ServerCookie.with(NAME,
                        VALUE,
                        DOMAIN,
                        PATH,
                        ServerCookie.NO_COMMENT,
                        EXPIRES,
                        CookieSecure.PRESENT,
                        CookieHttpOnly.ABSENT,
                        VERSION));
    }

    @Test
    public void testParseServerHeaderHttpOnly() {
        this.parseHeaderAndCheck("cookie123=value456; httpOnly;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        HTTPONLY,
                        VERSION));
    }

    private void parseHeaderAndCheck(final String header, final Cookie cookie) {
        final ServerCookie parsed = ServerCookie.parseHeader(header);
        checkName(parsed, cookie.name());
        checkValue(parsed, cookie.value());
        assertEquals(cookie, parsed);
    }

    @Test
    public void testToClientCookie() {
        final ServerCookie server = ServerCookie.with(NAME,
                VALUE,
                ServerCookie.NO_DOMAIN,
                ServerCookie.NO_PATH,
                COMMENT,
                MAXAGE,
                CookieSecure.ABSENT,
                CookieHttpOnly.ABSENT,
                VERSION);
        final ClientCookie client = server.toClient();
        checkName(client);
        checkValue(client);
    }

    // header ....................................................................................

    @Test
    public void testHeader() {
        assertEquals(HttpHeaderName.SET_COOKIE, this.createCookie().header());
    }

    // toHeaderValue ....................................................................................

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck(ServerCookie.with(NAME,
                VALUE,
                ServerCookie.NO_DOMAIN,
                ServerCookie.NO_PATH,
                ServerCookie.NO_COMMENT,
                ServerCookie.NO_DELETION,
                CookieSecure.ABSENT,
                CookieHttpOnly.ABSENT,
                VERSION),
                "cookie123=value456;");
    }

    // toString.....................................................................................

    @Test
    public void testToString() {
        assertEquals("cookie123=value456;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION)
                        .toString());
    }

    @Test
    public void testToStringDomain() {
        assertEquals("cookie123=value456;domain=example.com;", //
                ServerCookie.with(NAME,
                        VALUE,
                        DOMAIN,
                        ServerCookie.NO_PATH,
                        COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION)
                        .toString());
    }

    @Test
    public void testToStringPath() {
        assertEquals("cookie123=value456;path=/PATH/TO;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION)
                        .toString());
    }

    @Test
    public void testToStringDomainPath() {
        assertEquals("cookie123=value456;domain=example.com;path=/PATH/TO;", //
                ServerCookie.with(NAME,
                        VALUE,
                        DOMAIN,
                        PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION)
                        .toString());
    }


    @Test
    public void testToStringMaxAge() {
        assertEquals("cookie123=value456;max-age=123;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        COMMENT,
                        MAXAGE,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION).toString());
    }

    @Test
    public void testToStringExpires() {
        assertEquals("cookie123=value456;expires=Sun, 31 Dec 2000 12:58:59 GMT;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        COMMENT,
                        EXPIRES,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION).toString());
    }

    @Test
    public void testToStringDomainPathExpires() {
        assertEquals("cookie123=value456;domain=example.com;path=/PATH/TO;expires=Sun, 31 Dec 2000 12:58:59 GMT;", //
                ServerCookie.with(NAME,
                        VALUE,
                        DOMAIN,
                        PATH,
                        COMMENT,
                        EXPIRES,
                        CookieSecure.ABSENT,
                        CookieHttpOnly.ABSENT,
                        VERSION).toString());
    }

    @Test
    public void testToStringSecure() {
        assertEquals("cookie123=value456;secure;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        SECURE,
                        CookieHttpOnly.ABSENT,
                        VERSION).toString());
    }

    @Test
    public void testToStringHttpOnly() {
        assertEquals("cookie123=value456;httpOnly;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        CookieSecure.ABSENT,
                        HTTPONLY,
                        VERSION).toString());
    }

    @Test
    public void testToStringSecureHttpOnly() {
        assertEquals("cookie123=value456;secure;httpOnly;", //
                ServerCookie.with(NAME,
                        VALUE,
                        ServerCookie.NO_DOMAIN,
                        ServerCookie.NO_PATH,
                        ServerCookie.NO_COMMENT,
                        ServerCookie.NO_DELETION,
                        SECURE,
                        HTTPONLY,
                        VERSION).toString());
    }

    @Override
    ServerCookie createCookie(final CookieName name, final String value) {
        return ServerCookie.with(name, value, DOMAIN, PATH, COMMENT, MAXAGE, SECURE, HTTPONLY, VERSION);
    }

    private ServerCookie createCookieWithoutAttributes(final CookieName name, final String value) {
        return ServerCookie.with(name,
                value,
                ServerCookie.NO_DOMAIN,
                ServerCookie.NO_PATH,
                ServerCookie.NO_COMMENT,
                ServerCookie.NO_DELETION,
                CookieSecure.ABSENT,
                CookieHttpOnly.ABSENT,
                VERSION);
    }

    private void checkDomain(final ServerCookie cookie) {
        checkDomain(cookie, DOMAIN);
    }

    private void checkDomain(final ServerCookie cookie, final Optional<String> expected) {
        assertEquals(cookie + " domain", expected, cookie.domain());
    }

    private void checkPath(final ServerCookie cookie) {
        checkPath(cookie, PATH);
    }

    private void checkPath(final ServerCookie cookie, final Optional<String> expected) {
        assertEquals(cookie + " path", expected, cookie.path());
    }

    private void checkComment(final ServerCookie cookie) {
        checkComment(cookie, COMMENT);
    }

    private void checkComment(final ServerCookie cookie, final Optional<String> expected) {
        assertEquals(cookie + " comment", expected, cookie.comment());
    }

    private void checkDeletion(final ServerCookie cookie) {
        checkDeletion(cookie, MAXAGE);
    }

    private void checkDeletion(final ServerCookie cookie, final Optional<CookieDeletion> expected) {
        assertEquals(cookie + " deletion", expected, cookie.deletion());
    }

    private void checkSecure(final ServerCookie cookie) {
        checkSecure(cookie, SECURE);
    }

    private void checkSecure(final ServerCookie cookie, final CookieSecure expected) {
        assertEquals(cookie + " secure", expected, cookie.secure());
    }

    private void checkHttpOnly(final ServerCookie cookie) {
        checkHttpOnly(cookie, HTTPONLY);
    }

    private void checkHttpOnly(final ServerCookie cookie, final CookieHttpOnly expected) {
        assertEquals(cookie + " httpOnly", expected, cookie.httpOnly());
    }

    private void checkVersion(final ServerCookie cookie) {
        checkVersion(cookie, VERSION);
    }

    private void checkVersion(final ServerCookie cookie, final CookieVersion expected) {
        assertEquals(cookie + " version", expected, cookie.version());
    }

    final void checkName(final javax.servlet.http.Cookie cookie) {
        checkName(cookie, NAME);
    }

    final void checkName(final javax.servlet.http.Cookie cookie, final CookieName name) {
        assertEquals("name", name.value(), cookie.getName());
    }

    final void checkValue(final javax.servlet.http.Cookie cookie) {
        checkValue(cookie, VALUE);
    }

    final void checkValue(final javax.servlet.http.Cookie cookie, final String value) {
        assertEquals("value", value, cookie.getValue());
    }

    private void checkDomain(final javax.servlet.http.Cookie cookie) {
        checkDomain(cookie, DOMAIN.get());
    }

    private void checkDomain(final javax.servlet.http.Cookie cookie, final String expected) {
        assertEquals(cookie + " domain", expected, cookie.getDomain());
    }

    private void checkPath(final javax.servlet.http.Cookie cookie) {
        checkPath(cookie, PATH.get());
    }

    private void checkPath(final javax.servlet.http.Cookie cookie, final String expected) {
        assertEquals(cookie + " path", expected, cookie.getPath());
    }

    private void checkComment(final javax.servlet.http.Cookie cookie) {
        checkComment(cookie, COMMENT.get());
    }

    private void checkComment(final javax.servlet.http.Cookie cookie, final String expected) {
        assertEquals(cookie + " comment", expected, cookie.getComment());
    }

    private void checkDeletion(final javax.servlet.http.Cookie cookie) {
        checkDeletion(cookie, MAXAGE.get().toMaxAgeSeconds(NOW));
    }

    private void checkDeletion(final javax.servlet.http.Cookie cookie, final int expected) {
        assertEquals(cookie + " deletion", expected, cookie.getMaxAge());
    }

    private void checkSecure(final javax.servlet.http.Cookie cookie) {
        checkSecure(cookie, SECURE);
    }

    private void checkSecure(final javax.servlet.http.Cookie cookie, final CookieSecure expected) {
        assertEquals(cookie + " secure", expected.toJavaxServletCookieSecure(), cookie.getSecure());
    }

    private void checkHttpOnly(final javax.servlet.http.Cookie cookie) {
        checkHttpOnly(cookie, HTTPONLY);
    }

    private void checkHttpOnly(final javax.servlet.http.Cookie cookie, final CookieHttpOnly expected) {
        assertEquals(cookie + " httpOnly", expected.toJavaxServletCookieHttpOnly(), cookie.isHttpOnly());
    }

    final void checkVersion(final javax.servlet.http.Cookie cookie) {
        checkVersion(cookie, VERSION);
    }

    final void checkVersion(final javax.servlet.http.Cookie cookie, final CookieVersion version) {
        assertEquals("version", version.value(), cookie.getVersion());
    }

    @Override
    protected ServerCookie createHeaderValue() {
        return this.createCookie();
    }

    @Override
    protected boolean isRequest() {
        return false;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<ServerCookie> type() {
        return ServerCookie.class;
    }
}
