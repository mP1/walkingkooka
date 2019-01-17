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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertSame;

public final class LanguageTagNameWildcardTest extends LanguageTagNameTestCase<LanguageTagNameWildcard> {

    @Test
    @Ignore
    @Override
    public void testNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWith() {
        this.check(LanguageTagNameWildcard.INSTANCE,
                "*",
                LanguageTagName.NO_LOCALE);
    }

    @Test
    public void testWithCached() {
        assertSame(LanguageTagName.with("*"), LanguageTagName.with("*"));
    }

    @Test
    public void testNonWildcard() {
        this.checkNotEquals(LanguageTagName.with("en"));
    }

    @Test
    @Ignore
    @Override
    public void testCompareLess() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testCompareLessDifferentCase() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testDifferentText() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected LanguageTagNameWildcard createName(final String name) {
        return LanguageTagNameWildcard.INSTANCE;
    }

    @Override
    Class<LanguageTagNameWildcard> languageTagNameType() {
        return LanguageTagNameWildcard.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    protected String nameText() {
        return "*";
    }

    @Override
    protected String differentNameText() {
        return "different";
    }

    @Override
    protected String nameTextLess() {
        return "ES";
    }
}
