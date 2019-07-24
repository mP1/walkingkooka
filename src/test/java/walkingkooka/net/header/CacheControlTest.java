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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CacheControlTest extends HeaderValueTestCase<CacheControl>
        implements ParseStringTesting<CacheControl> {

    // with..............................................................................................................

    @Test
    public void testWithullFails() {
        assertThrows(NullPointerException.class, () -> {
            CacheControl.with(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            CacheControl.with(Lists.empty());
        });
    }

    @Test
    public void testWith() {
        final List<CacheControlDirective<?>> directives = Lists.of(CacheControlDirective.NO_CACHE);
        this.check(CacheControl.with(directives), directives);
    }

    // setValues........................................................................................................

    @Test
    public void testSetValueNullFails() {
        final CacheControl cacheControl = this.createHeaderValue();

        assertThrows(NullPointerException.class, () -> {
            cacheControl.setValue(null);
        });
    }

    @Test
    public void testSetValuesEmptyFails() {
        final CacheControl cacheControl = this.createHeaderValue();

        assertThrows(IllegalArgumentException.class, () -> {
            cacheControl.setValue(Lists.empty());
        });
    }

    @Test
    public void testSetValuesSame() {
        final CacheControl cacheControl = this.createHeaderValue();
        assertSame(cacheControl, cacheControl.setValue(this.directives()));
    }

    @Test
    public void testSetValuesDifferent() {
        final CacheControl cacheControl = this.createHeaderValue();

        final List<CacheControlDirective<?>> values = Lists.of(CacheControlDirective.MAX_STALE);
        final CacheControl different = cacheControl.setValue(values);

        assertNotSame(cacheControl, different);

        check(different, values);
        check(cacheControl, this.directives());
    }

    private <T> void check(final CacheControl cacheControl,
                           final List<CacheControlDirective<?>> directives) {
        assertEquals(directives, cacheControl.value(), "directives");
    }

    // wildcard.........................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // parse............................................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("no-cache, no-store, max-age=123",
                CacheControl.with(
                        Lists.of(CacheControlDirective.NO_CACHE,
                                CacheControlDirective.NO_STORE,
                                CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)))));
    }

    // toHeaderText .....................................................................................................

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck(CacheControl.with(Lists.of(CacheControlDirectiveName.MAX_AGE.setParameter(Optional.of(123L)))), "max-age=123");
    }

    @Test
    public void testToHeaderText2() {
        this.toHeaderTextAndCheck(CacheControl.with(Lists.of(CacheControlDirective.NO_CACHE, CacheControlDirective.NO_STORE)), "no-cache, no-store");
    }

    // equals ..........................................................................................................

    @Test
    public void testEqualsDifferentDirectives() {
        this.checkNotEquals(CacheControl.with(Lists.of(CacheControlDirective.NO_TRANSFORM)));
    }

    @Test
    public void testEqualsDifferentOrder() {
        this.checkNotEquals(CacheControl.with(Lists.of(CacheControlDirective.NO_STORE, CacheControlDirective.NO_CACHE)));
    }

    @Override
    public CacheControl createHeaderValue() {
        return CacheControl.with(this.directives());
    }

    private List<CacheControlDirective<?>> directives() {
        return Lists.of(CacheControlDirective.NO_CACHE, CacheControlDirective.NO_STORE);
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    // https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html
    // The Cache-Control general-header field is used to specify directives that MUST be obeyed by all caching mechanisms along the request/response chain.

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }
    // ClassTesting .....................................................................................................

    @Override
    public Class<CacheControl> type() {
        return CacheControl.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting ..............................................................................................

    @Override
    public CacheControl parse(final String text) {
        return CacheControl.parse(text);
    }
}
