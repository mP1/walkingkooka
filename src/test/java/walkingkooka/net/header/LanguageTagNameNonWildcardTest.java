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

import walkingkooka.naming.NameTesting2;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LanguageTagNameNonWildcardTest extends LanguageTagNameTestCase<LanguageTagNameNonWildcard>
        implements NameTesting2<LanguageTagNameNonWildcard, LanguageTagName> {

    private final static String VALUE = "en";
    private final static Optional<Locale> LOCALE = Optional.of(Locale.forLanguageTag("en"));

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LanguageTagNameNonWildcard.nonWildcard(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LanguageTagNameNonWildcard.nonWildcard("");
        });
    }

    @Test
    public void testWithInvalidLanguageTagNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LanguageTagNameNonWildcard.nonWildcard("\0xyz");
        });
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

    @Test
    public void testEqualsWildcard() {
        this.checkNotEquals(LanguageTagName.WILDCARD);
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkEquals(LanguageTagNameNonWildcard.nonWildcard("EN"));
    }

    @Override
    public LanguageTagNameNonWildcard createName(final String name) {
        return LanguageTagNameNonWildcard.nonWildcard(name);
    }

    @Override
    public String nameText() {
        return "en";
    }

    @Override
    public String differentNameText() {
        return "fr";
    }

    @Override
    public String nameTextLess() {
        return "de";
    }

    @Override
    public int minLength() {
        return 2;
    }

    @Override
    public int maxLength() {
        return 7;
    }

    @Override
    public String possibleValidChars(final int position) {
        return ASCII_LETTERS + "_";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return CONTROL + WHITESPACE;
    }

    @Override
    Class<LanguageTagNameNonWildcard> languageTagNameType() {
        return LanguageTagNameNonWildcard.class;
    }
}
