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


import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class CacheControlDirectiveNameTest extends HeaderName2TestCase<CacheControlDirectiveName<?>,
        CacheControlDirectiveName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithControlCharacterFails() {
        CacheControlDirectiveName.with("x\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithSpaceFails() {
        CacheControlDirectiveName.with("x ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTabFails() {
        CacheControlDirectiveName.with("x\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNonAsciiFails() {
        CacheControlDirectiveName.with("x\u0100;");
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
        assertEquals(name.toString(), expected, name.isExtension());
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

        assertEquals(directiveName + " isRequest",
                scopesSet.contains(HttpHeaderScope.REQUEST),
                directiveName.isRequest());
        assertEquals(directiveName + " isResponse",
                scopesSet.contains(HttpHeaderScope.RESPONSE),
                directiveName.isResponse());
    }

    // setParameter .......................................................

    @Test(expected = NullPointerException.class)
    public void testSetParameterNullFails() {
        CacheControlDirectiveName.MAX_AGE.setParameter(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testSetParameterInvalidFails() {
        CacheControlDirectiveName.MAX_AGE.setParameter(Optional.empty());
    }

    @Test
    public void testSetParameter() {
        final CacheControlDirectiveName<Long> name = CacheControlDirectiveName.MAX_AGE;
        final Optional<Long> parameter = Optional.of(123L);
        final CacheControlDirective<Long> directive = name.setParameter(parameter);
        assertEquals("value", name, directive.value());
        assertEquals("parameter", parameter, directive.parameter());
    }

    // toString.................................................................................

    @Test
    public void testToString() {
        final String name = "X-custom";
        assertEquals(name, CacheControlDirectiveName.with(name).toString());
    }

    @Override
    protected CacheControlDirectiveName<Object> createName(final String name) {
        return Cast.to(CacheControlDirectiveName.with(name));
    }

    @Override
    protected String nameText() {
        return "X-Custom";
    }

    @Override
    protected String differentNameText() {
        return "X-different";
    }

    @Override
    protected String nameTextLess() {
        return CacheControlDirectiveName.NO_CACHE.value();
    }

    @Override
    protected Class<CacheControlDirectiveName<?>> type() {
        return Cast.to(CacheControlDirectiveName.class);
    }
}
