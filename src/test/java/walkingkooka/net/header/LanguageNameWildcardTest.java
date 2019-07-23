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

import static org.junit.jupiter.api.Assertions.assertSame;

public final class LanguageNameWildcardTest extends LanguageNameTestCase<LanguageNameWildcard> {

    @Override
    public void testNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWith() {
        this.check(LanguageNameWildcard.INSTANCE,
                "*",
                LanguageName.NO_LOCALE);
    }

    @Test
    public void testWithCached() {
        assertSame(LanguageName.with("*"), LanguageName.with("*"));
    }

    @Test
    public void testTestEn() {
        this.testTrue(Language.with(LanguageName.with("en")));
    }

    @Test
    public void testTestFr() {
        this.testTrue(Language.with(LanguageName.with("fr")));
    }

    @Test
    public void testNonWildcard() {
        this.checkNotEquals(LanguageName.with("en"));
    }

    @Override
    public void testCompareLess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testCompareLessDifferentCase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDifferentText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LanguageNameWildcard createName(final String name) {
        return LanguageNameWildcard.INSTANCE;
    }

    @Override
    Class<LanguageNameWildcard> languageTagNameType() {
        return LanguageNameWildcard.class;
    }

    @Override
    public String nameText() {
        return "*";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "ES";
    }
}
