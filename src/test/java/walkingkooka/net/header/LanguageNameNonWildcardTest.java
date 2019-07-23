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
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.NameTesting2;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LanguageNameNonWildcardTest extends LanguageNameTestCase<LanguageNameNonWildcard>
        implements NameTesting2<LanguageNameNonWildcard, LanguageName> {

    private final static String VALUE = "en";
    private final static Optional<Locale> LOCALE = Optional.of(Locale.forLanguageTag("en"));

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LanguageNameNonWildcard.nonWildcard(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LanguageNameNonWildcard.nonWildcard("");
        });
    }

    @Test
    public void testWithInvalidLanguageTagNameFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            LanguageNameNonWildcard.nonWildcard("\0xyz");
        });
    }

    @Test
    public void testWith() {
        final LanguageNameNonWildcard LanguageTagName = LanguageNameNonWildcard.nonWildcard(VALUE);
        this.check(LanguageTagName);
    }

    private void check(final LanguageName name) {
        this.check(name, VALUE, LOCALE);
    }

    @Test
    public void testConstant() {
        assertSame(LanguageNameNonWildcard.nonWildcard("en"),
                LanguageNameNonWildcard.nonWildcard("en"));
    }

    @Test
    public void testTestSameTrue() {
        this.testTrue(Language.with(LanguageName.with(this.nameText())));
    }

    @Test
    public void testTestSameTrueWithParameters() {
        this.testTrue(LanguageName.with(this.nameText()).setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 0.5f)),
                Language.with(LanguageName.with(this.nameText())));
    }

    @Test
    public void testTestDifferentFalse() {
        this.testFalse(Language.with(LanguageName.with(this.differentNameText())));
    }

    @Test
    public void testTestDifferentFalseWithParameters() {
        this.testFalse(LanguageName.with(this.nameText()).setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 0.5f)),
                Language.with(LanguageName.with(this.differentNameText())));
    }

    @Test
    public void testEqualsWildcard() {
        this.checkNotEquals(LanguageName.WILDCARD);
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkEquals(LanguageNameNonWildcard.nonWildcard("EN"));
    }

    @Test
    public void testCompareToArraySort() {
        final LanguageName ar = LanguageName.with("ar");
        final LanguageName br = LanguageName.with("br");
        final LanguageName cz = LanguageName.with("cz");
        final LanguageName de = LanguageName.with("de");
        final LanguageName wildcard = LanguageName.WILDCARD;

        this.compareToArraySortAndCheck(de, ar, cz, wildcard, br,
                wildcard, ar, br, cz, de);
    }

    @Override
    public LanguageNameNonWildcard createName(final String name) {
        return LanguageNameNonWildcard.nonWildcard(name);
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
    Class<LanguageNameNonWildcard> languageTagNameType() {
        return LanguageNameNonWildcard.class;
    }
}
