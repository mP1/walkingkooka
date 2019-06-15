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

package walkingkooka.net;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AbsoluteUrlTest extends AbsoluteOrRelativeUrlTestCase<AbsoluteUrl> {

    // constants

    private final static UrlScheme SCHEME = UrlScheme.HTTP;
    private final static Optional<UrlCredentials> CREDENTIALS = UrlCredentials.NO_CREDENTIALS;
    private final static HostAddress HOST = HostAddress.with("host");
    private final static Optional<IpPort> PORT = Optional.of(IpPort.with(123));

    // tests

    @Test
    public void testWithNullSchemeFails() {
        assertThrows(NullPointerException.class, () -> {
            AbsoluteUrl.with(null,
                    CREDENTIALS,
                    HOST,
                    PORT,
                    PATH,
                    QUERY,
                    FRAGMENT);
        });
    }

    @Test
    public void testWithNullCredentialsFails() {
        assertThrows(NullPointerException.class, () -> {
            AbsoluteUrl.with(SCHEME,
                    null,
                    HOST,
                    PORT,
                    PATH,
                    QUERY,
                    FRAGMENT);
        });
    }

    @Test
    public void testWithNullHostFails() {
        assertThrows(NullPointerException.class, () -> {
            AbsoluteUrl.with(SCHEME,
                    CREDENTIALS,
                    null,
                    PORT,
                    PATH,
                    QUERY,
                    FRAGMENT);
        });
    }

    @Test
    public void testWithoutPortFails() {
        assertThrows(NullPointerException.class, () -> {
            AbsoluteUrl.with(SCHEME,
                    CREDENTIALS,
                    HOST,
                    null,
                    PATH,
                    QUERY,
                    FRAGMENT);
        });
    }

    @Test
    @Override
    public void testWith() {
        final AbsoluteUrl url = AbsoluteUrl.with(SCHEME, CREDENTIALS, HOST, PORT, PATH, QUERY, FRAGMENT);

        this.checkScheme(url, SCHEME);
        this.checkCredentials(url, CREDENTIALS);
        this.checkHost(url, HOST);
        this.checkPort(url, PORT);
        this.checkPath(url, PATH);
        this.checkQueryString(url, QUERY);
        this.checkFragment(url, FRAGMENT);
    }

    @Test
    public void testHttps() {
        final AbsoluteUrl url = AbsoluteUrl.with(UrlScheme.HTTPS, CREDENTIALS, HOST, PORT, PATH, QUERY, FRAGMENT);

        this.checkScheme(url, UrlScheme.HTTPS);
        this.checkCredentials(url, CREDENTIALS);
        this.checkHost(url, HOST);
        this.checkPort(url, PORT);
        this.checkPath(url, PATH);
        this.checkQueryString(url, QUERY);
        this.checkFragment(url, FRAGMENT);
    }

    @Test
    public void testHttpWithDefaultPort() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                Optional.of(IpPort.HTTP),
                PATH,
                QUERY,
                FRAGMENT),
                "http://host:80/path?query=value#fragment");
    }

    @Test
    public void testHttpsWithDefaultPort() {
        toStringAndCheck(AbsoluteUrl.with(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                Optional.of(IpPort.HTTPS),
                PATH,
                QUERY,
                FRAGMENT),
                "https://host:443/path?query=value#fragment");
    }

    @Test
    public void testHttpsWithNonDefaultPort() {
        toStringAndCheck(AbsoluteUrl.with(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT),
                "https://host:123/path?query=value#fragment");
    }

    @Test
    public void testToStringEmptyPath() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                UrlPath.EMPTY,
                UrlQueryString.EMPTY,
                UrlFragment.EMPTY),
                "http://host:123"
        );
    }

    public void testToStringUrl() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT), "http://host:123/path?query=value#fragment"
        );
    }

    @Override
    public void testToStringWithoutQuery() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                UrlQueryString.EMPTY,
                FRAGMENT), "http://host:123/path#fragment");
    }

    @Override
    public void testToStringWithoutFragment() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                UrlFragment.EMPTY), "http://host:123/path?query=value"
        );
    }

    @Override
    public void testToStringWithoutQueryAndFragment() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                UrlQueryString.EMPTY,
                UrlFragment.EMPTY), "http://host:123/path");
    }

    @Test
    public void testUrlWithCredentials() {
        toStringAndCheck(AbsoluteUrl.with(SCHEME,
                Optional.of(UrlCredentials.with("user123", "password456")),
                HOST,
                PORT,
                PATH,
                UrlQueryString.EMPTY,
                UrlFragment.EMPTY), "http://user123:password456@host:123/path");
    }

    @Test
    public void testRelativeUrlWithoutPathQueryOrFragment() {
        final UrlPath path = UrlPath.ROOT;
        final UrlQueryString query = UrlQueryString.EMPTY;
        final UrlFragment fragment = UrlFragment.EMPTY;
        final AbsoluteUrl url = AbsoluteUrl.with(UrlScheme.HTTPS, CREDENTIALS, HOST, PORT, path, query,
                fragment);
        final RelativeUrl relative = url.relativeUrl();
        assertEquals(RelativeUrl.with(path, query, fragment).value(), relative.value(), "url");
        assertSame(path, relative.path(), "path");
        assertSame(query, relative.query(), "query");
        assertSame(fragment, relative.fragment(), "fragment");
    }

    // parseAbsolute..........................................................................................

    @Test
    public void testParseMissingSchemeFails() {
        this.parseFails("example.com", IllegalArgumentException.class);
    }

    @Test
    public void testParseSchemeHost() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://example.com");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.EMPTY);
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHost2() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("https://example.com");
        this.checkScheme(url, UrlScheme.HTTPS);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.EMPTY);
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHostSlash() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("https://example.com/");
        this.checkScheme(url, UrlScheme.HTTPS);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.parse("/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHostPort() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://example.com:789");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPort(url, IpPort.with(789));
        this.checkPath(url, UrlPath.EMPTY);
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHostPortSlash() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://example.com:789/");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPort(url, IpPort.with(789));
        this.checkPath(url, UrlPath.parse("/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeCredentialsHost() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://abc:def@example.com");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentials(url, UrlCredentials.with("abc", "def"));
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.EMPTY);
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHostPath() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://example.com/path/to/file");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.parse("/path/to/file"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHostPathEndsSlash() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://example.com/path/to/file/");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.parse("/path/to/file/"));
        this.checkQueryString(url, UrlQueryString.EMPTY);
        this.checkFragment(url, UrlFragment.EMPTY);
    }

    @Test
    public void testParseSchemeHostPathQueryStringFragment() {
        final AbsoluteUrl url = AbsoluteUrl.parseAbsolute0("http://example.com/path123?query456#fragment789");
        this.checkScheme(url, UrlScheme.HTTP);
        this.checkCredentialsAbsent(url);
        this.checkHost(url, HostAddress.with("example.com"));
        this.checkPortAbsent(url);
        this.checkPath(url, UrlPath.parse("path123"));
        this.checkQueryString(url, UrlQueryString.with("query456"));
        this.checkFragment(url, UrlFragment.with("fragment789"));
    }

    @Test
    public void testTryParseFails() {
        assertEquals(Optional.empty(), AbsoluteUrl.tryParse("abc"));
    }

    @Test
    public void testTryParseSuccess() {
        final String url = "http://example.com/path123?query456#fragment789";
        assertEquals(Optional.of(AbsoluteUrl.parseAbsolute0(url)), AbsoluteUrl.tryParse(url));
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentScheme() {
        this.checkNotEquals(Url.absolute(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT));
    }

    @Test
    public void testEqualsDifferentHost() {
        this.checkNotEquals(Url.absolute(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT));
    }

    @Test
    public void testEqualsDifferentPort() {
        this.checkNotEquals(Url.absolute(UrlScheme.HTTPS,
                CREDENTIALS,
                HOST,
                Optional.of(IpPort.with(9999)),
                PATH,
                QUERY,
                FRAGMENT));
    }

    // UrlVisitor......................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final AbsoluteUrl url = this.createUrl();

        new FakeUrlVisitor() {
            @Override
            protected Visiting startVisit(final Url u) {
                assertSame(url, u);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Url u) {
                assertSame(url, u);
                b.append("2");
            }

            @Override
            protected void visit(final AbsoluteUrl u) {
                assertSame(url, u);
                b.append("5");
            }
        }.accept(url);
        assertEquals("152", b.toString());
    }

    // toString..........................................................................................

    @Test
    public void testToString() {
        assertEquals("http://host:123/path?query=value#fragment", this.createUrl().toString());
    }

    @Test
    public void testToStringWithCredentials() {
        assertEquals("http://host:123/path?query=value#fragment", this.createUrl().toString());
    }

    @Test
    public void testToStringUpperCasedScheme() {
        assertEquals("test://host:123",
                AbsoluteUrl.with(UrlScheme.with("TEST"),
                        AbsoluteUrl.NO_CREDENTIALS,
                        HOST,
                        PORT,
                        UrlPath.EMPTY,
                        UrlQueryString.EMPTY,
                        UrlFragment.EMPTY).toString());
    }

    // factory

    @Override
    protected AbsoluteUrl createUrl(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        return AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                path,
                query,
                fragment);
    }

    private void checkScheme(final AbsoluteUrl url, final UrlScheme scheme) {
        assertEquals(scheme, url.scheme(), "scheme");
    }

    private void checkCredentials(final AbsoluteUrl url, final UrlCredentials credentials) {
        this.checkCredentials(url, Optional.of(credentials));
    }

    private void checkCredentialsAbsent(final AbsoluteUrl url) {
        this.checkCredentials(url, AbsoluteUrl.NO_CREDENTIALS);
    }

    private void checkCredentials(final AbsoluteUrl url, final Optional<UrlCredentials> credentials) {
        assertEquals(credentials, url.credentials(), "credentials");
    }

    private void checkHost(final AbsoluteUrl url, final HostAddress host) {
        assertEquals(host, url.host(), "host");
    }

    private void checkPort(final AbsoluteUrl url, final IpPort port) {
        this.checkPort(url, Optional.of(port));
    }

    private void checkPortAbsent(final AbsoluteUrl url) {
        this.checkPort(url, AbsoluteUrl.NO_PORT);
    }

    private void checkPort(final AbsoluteUrl url, final Optional<IpPort> port) {
        assertEquals(port, url.port(), "port");
    }

    // ClassTesting ....................................................................................................

    @Override
    public Class<AbsoluteUrl> type() {
        return AbsoluteUrl.class;
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public AbsoluteUrl fromJsonNode(final JsonNode node) {
        return Url.fromJsonNodeAbsolute(node);
    }

    // ParseStringTesting ..............................................................................................

    @Override
    public AbsoluteUrl parse(final String text) {
        return AbsoluteUrl.parseAbsolute0(text);
    }

    // SerializationTesting.............................................................................................

    @Override
    public AbsoluteUrl serializableInstance() {
        return Url.absolute(UrlScheme.HTTP, //
                Optional.of(UrlCredentials.with("user", "pass")), //
                HostAddress.with("host"), //
                Optional.of(IpPort.HTTP), //
                UrlPath.parse("/path"), //
                UrlQueryString.with("query"),//
                UrlFragment.with("fragment123"));
    }
}
