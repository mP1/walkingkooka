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


import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class UrlSchemeTest extends ClassTestCase<UrlScheme>
        implements NameTesting<UrlScheme, UrlScheme>, SerializationTesting<UrlScheme> {

    @Test
    @Ignore
    public void testNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testHttpConstants() {
        assertSame(UrlScheme.HTTP, UrlScheme.with("http"));
    }

    @Test
    public void testHttpsConstants() {
        assertSame(UrlScheme.HTTPS, UrlScheme.with("https"));
    }

    @Test
    public void testHttpsUpperCaseUnimportantConstants() {
        assertSame(UrlScheme.HTTPS, UrlScheme.with("HTTPS"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFirstCharFails() {
        this.createName("1http");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCharFails() {
        this.createName("ab\u100cd");
    }

    @Test
    public void testIncludesPlusMinusDot() {
        this.createName("A123456789+-.abcABCxyzXYZ");
    }

    @Test
    public void testNameWithSlashes() {
        this.createNameAndCheckWithSlashes("http", "http://");
    }

    @Test
    public void testNameWithSlashes2() {
        this.createNameAndCheckWithSlashes("HTTP", "http://");
    }

    private void createNameAndCheckWithSlashes(final String value,
                                               final String nameWithSlashes) {
        final UrlScheme urlScheme = UrlScheme.with(value);
        assertEquals("nameWithSlashes", nameWithSlashes, urlScheme.nameWithSlashes());
    }

    // withHost...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testAndHostNullFails() {
        UrlScheme.HTTP.andHost(null);
    }

    @Test
    public void testAndHost() {
        final UrlScheme scheme = UrlScheme.HTTPS;
        final HostAddress address = HostAddress.with("example.com");
        final AbsoluteUrl url = scheme.andHost(address);
        assertSame("scheme", scheme, url.scheme());
        assertEquals("credentials", UrlCredentials.NO_CREDENTIALS, url.credentials());
        assertSame("host", address, url.host());
        assertEquals("port", IpPort.WITHOUT_PORT, url.port());
        assertEquals("path", UrlPath.EMPTY, url.path());
        assertEquals("queryString", UrlQueryString.EMPTY, url.query());
        assertEquals("fragment", UrlFragment.EMPTY, url.fragment());
    }

    @Override
    public UrlScheme createName(final String name) {
        return UrlScheme.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public String nameText() {
        return "https";
    }

    @Override
    public String differentNameText() {
        return "ftp";
    }

    @Override
    public String nameTextLess() {
        return "http";
    }

    @Override
    public Class<UrlScheme> type() {
        return UrlScheme.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public UrlScheme serializableInstance() {
        return UrlScheme.with("custom");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
