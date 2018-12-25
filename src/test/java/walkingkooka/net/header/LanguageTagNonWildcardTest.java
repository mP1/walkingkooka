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

public final class LanguageTagNonWildcardTest extends LanguageTagTestCase<LanguageTagNonWildcard> {

    private final static String VALUE = "en";
    private final static Optional<Locale> LOCALE = Optional.of(Locale.forLanguageTag("en"));

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        LanguageTagNonWildcard.nonWildcard(null, LanguageTagNonWildcard.NO_PARAMETERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        LanguageTagNonWildcard.nonWildcard("", LanguageTagNonWildcard.NO_PARAMETERS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidLanguageTagFails() {
        LanguageTagNonWildcard.nonWildcard("\0xyz", LanguageTagNonWildcard.NO_PARAMETERS);
    }

    @Test
    public void testWith() {
        final LanguageTagNonWildcard languageTag = LanguageTagNonWildcard.nonWildcard(VALUE,
                LanguageTagNonWildcard.NO_PARAMETERS);
        this.check(languageTag);
    }

    private void check(final LanguageTag tag) {
        this.check(tag, VALUE, LOCALE, LanguageTagNonWildcard.NO_PARAMETERS);
    }

    @Override
    protected LanguageTagNonWildcard createHeaderValueWithParameters() {
        return LanguageTagNonWildcard.nonWildcard(VALUE, LanguageTagNonWildcard.NO_PARAMETERS);
    }

    @Override
    protected Class<LanguageTagNonWildcard> type() {
        return LanguageTagNonWildcard.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
