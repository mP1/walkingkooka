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

package walkingkooka.net;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public final class AbsoluteUrlTest extends UrlTestCase<AbsoluteUrl> {

    // constants

    private final static UrlScheme SCHEME = UrlScheme.HTTP;
    private final static Optional<UrlCredentials> CREDENTIALS = UrlCredentials.NO_CREDENTIALS;
    private final static HostAddress HOST = HostAddress.with("host");
    private final static Optional<IpPort> PORT = Optional.of(IpPort.with(123));

    // tests

    @Test(expected = NullPointerException.class)
    public void testWithNullSchemeFails() {
        AbsoluteUrl.with(null,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullCredentialsFails() {
        AbsoluteUrl.with(SCHEME,
                null,
                HOST,
                PORT,
                PATH,
                QUERY,
                FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullHostFails() {
        AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                null,
                PORT,
                PATH,
                QUERY,
                FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void testWithoutPortFails() {
        AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                null,
                PATH,
                QUERY,
                FRAGMENT);
    }

    @Test
    @Override
    public void testWith() {
        final AbsoluteUrl url = AbsoluteUrl.with(SCHEME, CREDENTIALS, HOST, PORT, PATH, QUERY, FRAGMENT);
        assertSame("scheme", SCHEME, url.scheme());
        assertEquals("credentials", CREDENTIALS, url.credentials());
        assertEquals("host", HOST, url.host());
        assertEquals("port", PORT, url.port());
        assertEquals("path", PATH, url.path());
        assertEquals("query", QUERY, url.query());
        assertEquals("fragment", FRAGMENT, url.fragment());
    }

    @Test
    public void testHttps() {
        final AbsoluteUrl url = AbsoluteUrl.with(UrlScheme.HTTPS, CREDENTIALS, HOST, PORT, PATH, QUERY, FRAGMENT);
        assertEquals("https", UrlScheme.HTTPS, url.scheme());
        assertEquals("credentials", CREDENTIALS, url.credentials());
        assertEquals("host", HOST, url.host());
        assertEquals("port", PORT, url.port());
        assertEquals("path", PATH, url.path());
        assertEquals("query", QUERY, url.query());
        assertEquals("fragment", FRAGMENT, url.fragment());
    }

    @Test
    public void testHttpWithDefaultPort() {
        checkToString(AbsoluteUrl.with(SCHEME,
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
        checkToString(AbsoluteUrl.with(UrlScheme.HTTPS,
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
        checkToString(AbsoluteUrl.with(UrlScheme.HTTPS,
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
        checkToString(AbsoluteUrl.with(SCHEME,
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
        checkToString(AbsoluteUrl.with(SCHEME,
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
        checkToString(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                UrlQueryString.EMPTY,
                FRAGMENT), "http://host:123/path#fragment");
    }

    @Override
    public void testToStringWithoutFragment() {
        checkToString(AbsoluteUrl.with(SCHEME,
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
        checkToString(AbsoluteUrl.with(SCHEME,
                CREDENTIALS,
                HOST,
                PORT,
                PATH,
                UrlQueryString.EMPTY,
                UrlFragment.EMPTY), "http://host:123/path");
    }

    @Test
    public void testUrlWithCredentials() {
        checkToString(AbsoluteUrl.with(SCHEME,
                Optional.of(UrlCredentials.with("user123", "password456")),
                HOST,
                PORT,
                PATH,
                UrlQueryString.EMPTY,
                UrlFragment.EMPTY), "http://user123:password456@host:123/path");
    }

    @Override
    public void testIsAbsolute() {
        assertTrue(this.createUrl().isAbsolute());
    }

    @Override
    public void testIsRelative() {
        assertFalse(this.createUrl().isRelative());
    }

    @Test
    public void testRelativeUrlWithoutPathQueryOrFragment() {
        final UrlPath path = UrlPath.ROOT;
        final UrlQueryString query = UrlQueryString.EMPTY;
        final UrlFragment fragment = UrlFragment.EMPTY;
        final AbsoluteUrl url = AbsoluteUrl.with(UrlScheme.HTTPS, CREDENTIALS, HOST, PORT, path, query,
                fragment);
        final RelativeUrl relative = url.relativeUrl();
        assertEquals("url", RelativeUrl.with(path, query, fragment).value(), relative.value());
        assertSame("path", path, relative.path());
        assertSame("query", query, relative.query());
        assertSame("fragment", fragment, relative.fragment());
    }

    @Test
    public void testToString() {
        assertEquals("http://host:123/path?query=value#fragment", this.createUrl().toString());
    }

    @Test
    public void testToStringWithCredentials() {
        assertEquals("http://host:123/path?query=value#fragment", this.createUrl().toString());
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

    @Override
    protected Class<AbsoluteUrl> type() {
        return AbsoluteUrl.class;
    }
}
