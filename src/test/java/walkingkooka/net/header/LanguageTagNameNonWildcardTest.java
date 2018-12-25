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
import walkingkooka.type.MemberVisibility;

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertSame;

public final class LanguageTagNameNonWildcardTest extends LanguageTagNameTestCase<LanguageTagNameNonWildcard> {

    private final static String VALUE = "en";
    private final static Optional<Locale> LOCALE = Optional.of(Locale.forLanguageTag("en"));

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        LanguageTagNameNonWildcard.nonWildcard(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        LanguageTagNameNonWildcard.nonWildcard("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidLanguageTagNameFails() {
        LanguageTagNameNonWildcard.nonWildcard("\0xyz");
    }

    @Test
    public void testWith() {
        final LanguageTagNameNonWildcard LanguageTagName = LanguageTagNameNonWildcard.nonWildcard(VALUE);
        this.check(LanguageTagName);
    }

    private void check(final LanguageTagName name) {
        this.check(name, VALUE, LOCALE);
    }

    @Test
    public void testConstant() {
        assertSame(LanguageTagNameNonWildcard.nonWildcard("en"),
                LanguageTagNameNonWildcard.nonWildcard("en"));
    }

    @Override
    protected LanguageTagNameNonWildcard createName(final String name) {
        return LanguageTagNameNonWildcard.nonWildcard(name);
    }

    @Override
    protected Class<LanguageTagNameNonWildcard> type() {
        return LanguageTagNameNonWildcard.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
