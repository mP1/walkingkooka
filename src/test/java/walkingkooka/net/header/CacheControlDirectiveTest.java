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
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class CacheControlDirectiveTest extends HeaderValueTestCase<CacheControlDirective<Long>> {

    // with..........................................................................

    @Test(expected = NullPointerException.class)
    public void testWithNameNullFails() {
        CacheControlDirective.with(null, Optional.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testWithParameterNullFails() {
        CacheControlDirective.with(CacheControlDirectiveName.PUBLIC, null);
    }

    @Test(expected = HeaderValueException.class)
    public void testWithParameterInvalidFails() {
        CacheControlDirective.with(CacheControlDirectiveName.MAX_AGE, Optional.empty());
    }

    @Test
    public void testWith() {
        final CacheControlDirectiveName<Long> name = CacheControlDirectiveName.MAX_AGE;
        final Optional<Long> parameter = Optional.of(123L);
        this.check(CacheControlDirective.with(name, parameter),
                name,
                parameter);
    }

    // setParameter..........................................................................

    @Test(expected = NullPointerException.class)
    public void testSetParameterNullFails() {
        CacheControlDirective.NO_STORE.setParameter(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testSetParameterInvalidFails() {
        CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L))
                .setParameter(Optional.empty());
    }

    @Test
    public void testSetParameterSame() {
        final CacheControlDirectiveName<Long> name = CacheControlDirectiveName.MAX_AGE;
        final Optional<Long> parameter = Optional.of(123L);
        final CacheControlDirective<Long> directive = name.setParameter(parameter);
        assertSame(directive, directive.setParameter(parameter));
    }

    @Test
    public void testSetParameterSame2() {
        final CacheControlDirective<Void> directive = CacheControlDirective.NO_CACHE;
        assertSame(directive, directive.setParameter(Optional.empty()));
    }

    @Test
    public void testSetParameterDifferent() {
        this.setParameterAndCheck(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                Optional.of(456L),
                CacheControlDirectiveName.MAX_AGE);
    }

    @Test
    public void testSetParameterMaxStaleDifferent() {
        this.setParameterAndCheck(CacheControlDirectiveName.MAX_STALE.setParameter(Optional.of(123L)),
                Optional.of(456L),
                CacheControlDirectiveName.MAX_STALE);
    }

    @Test
    public void testSetParameterMaxStaleDifferent2() {
        this.setParameterAndCheck(CacheControlDirective.MAX_STALE,
                Optional.of(123L),
                CacheControlDirectiveName.MAX_STALE);
    }

    @Test
    public void testSetParameterMaxStaleDifferent3() {
        this.setParameterAndCheck(CacheControlDirectiveName.MAX_STALE.setParameter(Optional.of(123L)),
                Optional.empty(),
                CacheControlDirectiveName.MAX_STALE);
    }

    private <T> void setParameterAndCheck(final CacheControlDirective<T> directive,
                                          final Optional<T> parameter,
                                          final CacheControlDirectiveName<T> name) {
        assertNotEquals("new parameter must be different from old",
                parameter,
                directive.parameter());
        final CacheControlDirective<T> different = directive.setParameter(parameter);
        assertNotSame("directive set parameter" + parameter + " must not return same",
                directive,
                different);

        this.check(different, name, parameter);
    }

    private <T> void check(final CacheControlDirective<T> directive,
                           final CacheControlDirectiveName<T> name,
                           final Optional<T> parameter) {
        assertEquals("value", name, directive.value());
        assertEquals("parameter", parameter, directive.parameter());
    }

    // scope ...............................................................................

    @Test
    public void testScopeMaxAge() {
        this.checkScope(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)));
    }

    @Test
    public void testScopeMaxStale() {
        this.checkScope(CacheControlDirectiveName.MAX_STALE.setParameter(Optional.of(123L)));
    }

    @Test
    public void testScopeOnlyIfCached() {
        this.checkScope(CacheControlDirective.ONLY_IF_CACHED);
    }

    private void checkScope(final CacheControlDirective<?> directive) {
        assertEquals(directive + " isMultipart",
                false,
                directive.isMultipart());
        assertEquals(directive + " isRequest",
                directive.value().isRequest(),
                directive.isRequest());
        assertEquals(directive + " isResponse",
                directive.value().isResponse(),
                directive.isResponse());
    }

    // wildcard...........................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // parse..........................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        CacheControlDirective.parse(null);
    }

    @Test
    public void testParse() {
        final String text = "no-cache, no-store, max-age=123";
        assertEquals("parse " + CharSequences.quote(text) + " failed",
                CacheControlDirective.parse(text),
                Lists.of(CacheControlDirective.NO_CACHE,
                        CacheControlDirective.NO_STORE,
                        CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L))));
    }

    // toHeaderText ...............................................................

    @Test
    public void testToHeaderTextMaxAge() {
        this.toHeaderTextAndCheck(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                "max-age=123");
    }

    @Test
    public void testToHeaderTextNoCache() {
        this.toHeaderTextAndCheck(CacheControlDirective.NO_CACHE, "no-cache");
    }

    // toHeaderTextList..........................................................................

    @Test(expected = NullPointerException.class)
    public void testToHeaderTextListNullFails() {
        CacheControlDirective.toHeaderTextList(null);
    }

    @Test
    public void testToHeaderTextListNoCache() {
        this.toHeaderTextListAndCheck("no-cache",
                CacheControlDirective.NO_CACHE);
    }

    @Test
    public void testToHeaderTextListMaxAge() {
        this.toHeaderTextListAndCheck("max-age=123",
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)));
    }

    @Test
    public void testToHeaderTextListMaxAgeNoCacheNoStore() {
        this.toHeaderTextListAndCheck("max-age=123, no-cache, no-store",
                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)),
                CacheControlDirective.NO_CACHE,
                CacheControlDirective.NO_STORE);
    }

    private void toHeaderTextListAndCheck(final String toString,
                                          final CacheControlDirective<?>... directives) {
        assertEquals("toHeaderTextList returned wrong toString " + Arrays.toString(directives),
                toString,
                CacheControlDirective.toHeaderTextList(Lists.of(directives)));
    }

    @Override
    protected CacheControlDirective<Long> createHeaderValue() {
        return CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L));
    }

    @Override
    protected boolean isMultipart() {
        return false;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<CacheControlDirective<Long>> type() {
        return Cast.to(CacheControlDirective.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
