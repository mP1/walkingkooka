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


import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting2;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UrlSchemeTest implements ClassTesting2<UrlScheme>,
        NameTesting2<UrlScheme, UrlScheme>, SerializationTesting<UrlScheme> {

    @Override
    public void testTypeNaming() {
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
        assertEquals(nameWithSlashes, urlScheme.nameWithSlashes(), "nameWithSlashes");
    }

    // withHost...........................................................................................

    @Test
    public void testAndHostNullFails() {
        assertThrows(NullPointerException.class, () -> {
            UrlScheme.HTTP.andHost(null);
        });
    }

    @Test
    public void testAndHost() {
        final UrlScheme scheme = UrlScheme.HTTPS;
        final HostAddress address = HostAddress.with("example.com");
        final AbsoluteUrl url = scheme.andHost(address);
        assertSame(scheme, url.scheme(),"scheme");
        assertEquals(UrlCredentials.NO_CREDENTIALS, url.credentials(),"credentials");
        assertSame(address, url.host(),"host");
        assertEquals(IpPort.WITHOUT_PORT, url.port(),"port");
        assertEquals(UrlPath.EMPTY, url.path(),"path");
        assertEquals(UrlQueryString.EMPTY, url.query(),"queryString");
        assertEquals(UrlFragment.EMPTY, url.fragment(),"fragment");
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
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS + "+-.";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return CONTROL + BYTE_NON_ASCII;
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
