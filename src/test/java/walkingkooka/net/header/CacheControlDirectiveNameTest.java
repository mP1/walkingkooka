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

package walkingkooka.net.header;


import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.NameTesting2;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CacheControlDirectiveNameTest extends HeaderName2TestCase<CacheControlDirectiveName<?>,
        CacheControlDirectiveName<?>>
        implements NameTesting2<CacheControlDirectiveName<?>, CacheControlDirectiveName<?>> {

    @Test
    public void testWithControlCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CacheControlDirectiveName.with("x\u0001;");
        });
    }

    @Test
    public void testWithSpaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CacheControlDirectiveName.with("x ");
        });
    }

    @Test
    public void testWithTabFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CacheControlDirectiveName.with("x\t");
        });
    }

    @Test
    public void testWithNonAsciiFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CacheControlDirectiveName.with("x\u0100;");
        });
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Extension");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(CacheControlDirectiveName.MAX_AGE,
                CacheControlDirectiveName.with(CacheControlDirectiveName.MAX_AGE.value()));
    }

    @Test
    public void testConstantNameReturnsConstantCaseIgnored() {
        assertSame(CacheControlDirectiveName.MAX_AGE, CacheControlDirectiveName.with("max-AGE"));
    }

    // isExtension..................................................................................

    @Test
    public void testIsExtensionConstant() {
        this.isExtensionAndCheck(CacheControlDirectiveName.PUBLIC, true);
    }

    @Test
    public void testIsExtensionCustom() {
        this.isExtensionAndCheck(CacheControlDirectiveName.with("Custom"), false);
    }

    private void isExtensionAndCheck(final CacheControlDirectiveName<?> name, final boolean expected) {
        assertEquals(expected, name.isExtension(), name.toString());
    }

    // scope .............................................................................................

    @Test
    public void testMaxAge() {
        this.checkScope(CacheControlDirectiveName.MAX_AGE,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testMaxStale() {
        this.checkScope(CacheControlDirectiveName.MAX_STALE,
                HttpHeaderScope.REQUEST);
    }

    @Test
    public void testMinFresh() {
        this.checkScope(CacheControlDirectiveName.MIN_FRESH,
                HttpHeaderScope.REQUEST);
    }

    @Test
    public void testMustRevalidate() {
        this.checkScope(CacheControlDirectiveName.MUST_REVALIDATE,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testNoCache() {
        this.checkScope(CacheControlDirectiveName.NO_CACHE,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testNoStore() {
        this.checkScope(CacheControlDirectiveName.NO_STORE,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testNoTransform() {
        this.checkScope(CacheControlDirectiveName.NO_TRANSFORM,
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testOnlyIfCached() {
        this.checkScope(CacheControlDirectiveName.ONLY_IF_CACHED,
                HttpHeaderScope.REQUEST);
    }

    @Test
    public void testPrivate() {
        this.checkScope(CacheControlDirectiveName.PRIVATE,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testProxyRevalidate() {
        this.checkScope(CacheControlDirectiveName.PROXY_REVALIDATE,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testPublic() {
        this.checkScope(CacheControlDirectiveName.PUBLIC,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testSMaxage() {
        this.checkScope(CacheControlDirectiveName.S_MAXAGE,
                HttpHeaderScope.RESPONSE);
    }

    @Test
    public void testScopeRequestUnknown() {
        this.checkScope(CacheControlDirectiveName.with("extension"),
                HttpHeaderScope.REQUEST,
                HttpHeaderScope.RESPONSE);
    }

    private void checkScope(final CacheControlDirectiveName directiveName, final HttpHeaderScope... scopes) {
        final Set<HttpHeaderScope> scopesSet = Sets.of(scopes);

        assertEquals(scopesSet.contains(HttpHeaderScope.REQUEST),
                directiveName.isRequest(),
                directiveName + " isRequest");
        assertEquals(scopesSet.contains(HttpHeaderScope.RESPONSE),
                directiveName.isResponse(),
                directiveName + " isResponse");
    }

    // setParameter .......................................................

    @Test
    public void testSetParameterNullFails() {
        assertThrows(NullPointerException.class, () -> {
            CacheControlDirectiveName.MAX_AGE.setParameter(null);
        });
    }

    @Test
    public void testSetParameterInvalidFails() {
        assertThrows(HeaderValueException.class, () -> {
            CacheControlDirectiveName.MAX_AGE.setParameter(Optional.empty());
        });
    }

    @Test
    public void testSetParameter() {
        final CacheControlDirectiveName<Long> name = CacheControlDirectiveName.MAX_AGE;
        final Optional<Long> parameter = Optional.of(123L);
        final CacheControlDirective<Long> directive = name.setParameter(parameter);
        assertEquals(name, directive.value(), "value");
        assertEquals(parameter, directive.parameter(), "parameter");
    }

    @Override
    public CacheControlDirectiveName<Object> createName(final String name) {
        return Cast.to(CacheControlDirectiveName.with(name));
    }

    @Override
    public String nameText() {
        return "X-Custom";
    }

    @Override
    public String differentNameText() {
        return "X-different";
    }

    @Override
    public String nameTextLess() {
        return CacheControlDirectiveName.NO_CACHE.value();
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
                ASCII_LETTERS_DIGITS :
                RFC2045;
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return CONTROL + BYTE_NON_ASCII;
    }

    @Override
    public Class<CacheControlDirectiveName<?>> type() {
        return Cast.to(CacheControlDirectiveName.class);
    }
}
