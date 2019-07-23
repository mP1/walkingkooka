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
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LanguageTest extends HeaderValueWithParametersTestCase<Language,
        LanguageParameterName<?>>
        implements ParseStringTesting<Language>,
        PredicateTesting2<Language, Language> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Language.with(null);
        });
    }

    @Test
    public void testWith() {
        final Language languageTag = Language.with(this.value());
        this.check(languageTag);
    }

    @Test
    public void testWithCached() {
        assertSame(Language.with(LanguageName.WILDCARD), Language.with(LanguageName.WILDCARD));
    }

    @Test
    public void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Language.WILDCARD.setValue(null);
        });
    }

    @Test
    public void testSetValueSame() {
        final Language language = Language.WILDCARD;
        assertSame(language, language.setValue(language.value()));
    }

    @Test
    public void testSetValueSame2() {
        final Language language = Language.with(this.value());
        assertSame(language, language.setValue(this.value()));
    }

    @Test
    public void testSetValueDifferent() {
        final Language language = Language.WILDCARD;
        final LanguageName name = LanguageName.with("fr");
        final Language different = language.setValue(name);
        assertNotSame(language, different);

        this.check(different, name, Language.NO_PARAMETERS);
    }

    @Test
    public void testSetValueDifferent2() {
        final Language language = Language.with(this.value());
        final LanguageName name = LanguageName.with("fr");
        final Language different = language.setValue(name);
        assertNotSame(language, different);

        this.check(different, name, Language.NO_PARAMETERS);
    }

    @Test
    public void testSetValueDifferent3() {
        final Language language = Language.with(this.value()).setParameters(this.parametersWithQFactor());
        final LanguageName name = LanguageName.with("fr");
        final Language different = language.setValue(name);
        assertNotSame(language, different);
        this.check(different, name, this.parametersWithQFactor());
    }

    private void check(final Language language) {
        this.check(language, this.value(), Language.NO_PARAMETERS);
    }

    private LanguageName value() {
        return LanguageName.with("en");
    }

    @Test
    public final void testSetParameterDifferent() {
        final Language languageTag = this.createHeaderValueWithParameters();
        final Map<LanguageParameterName<?>, Object> parameters = this.parametersWithQFactor();
        final Language different = languageTag.setParameters(parameters);
        this.check(different, languageTag.value(), parameters);
    }

    final Map<LanguageParameterName<?>, Object> parametersWithQFactor() {
        return Maps.of(LanguageParameterName.Q_FACTOR, 0.75f);
    }

    @Test
    public void testSetParametersDifferentAndBack() {
        assertSame(Language.WILDCARD,
                Language.WILDCARD
                        .setParameters(this.parametersWithQFactor())
                        .setParameters(Language.NO_PARAMETERS));
    }

    final void check(final Language language,
                     final LanguageName value,
                     final Map<LanguageParameterName<?>, Object> parameters) {
        assertEquals(value, language.value(), "value");
        this.checkParameters(language, parameters);
    }

    // ParseStringTesting ........................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("en",
                Language.with(LanguageName.with("en")));
    }

    @Test
    public void testParseWithParameters() {
        this.parseAndCheck("en; abc=123",
                Language.with(LanguageName.with("en"))
                        .setParameters(Maps.of(LanguageParameterName.with("abc"), "123")));
    }

    @Override
    public Language parse(final String text) {
        return Language.parse(text);
    }

    // toHeaderTextList.......................................................................................

    @Test
    public void testToHeaderTextListListOfOne() {
        this.toHeaderTextListAndCheck("fr",
                this.fr());
    }

    @Test
    public void testToHeaderTextListListOfOneWithParameters() {
        this.toHeaderTextListAndCheck("en; q=0.75",
                this.en075WithParameters());
    }

    @Test
    public void testToHeaderTextListListOfOneWildcard() {
        this.toHeaderTextListAndCheck("*",
                Language.WILDCARD);
    }

    @Test
    public void testToHeaderTextListListOfMany() {
        this.toHeaderTextListAndCheck("en; q=0.75, fr",
                this.en075WithParameters(),
                this.fr());
    }

    // test ...........................................................................................................

    @Test
    public void testTest() {
        this.testTrue(Language.WILDCARD, this.en075WithParameters());
    }

    @Test
    public void testTestWildcardWildcardFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Language.WILDCARD.test(Language.WILDCARD);
        });
    }

    @Test
    public void testTestWildcardNonWildcardFails() {
        this.testTrue(Language.WILDCARD,
                this.en075WithParameters());
    }

    @Test
    public void testTestWildcardNonWildcardFails2() {
        this.testTrue(Language.WILDCARD,
                this.fr());
    }

    @Test
    public void testTestNonWildcardNonWildcard() {
        this.testTrue(this.en075WithParameters(),
                this.en());
    }

    @Test
    public void testTestNonWildcardNonWildcard2() {
        this.testFalse(this.en075WithParameters(),
                this.fr());
    }

    private Language en075WithParameters() {
        return this.en()
                .setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 0.75f));
    }

    private Language en() {
        return Language.with(LanguageName.with("en"));
    }

    private Language fr() {
        return Language.with(LanguageName.with("fr"));
    }

    @Override
    public Language createHeaderValueWithParameters() {
        return Language.WILDCARD;
    }

    @Override
    LanguageParameterName<?> parameterName() {
        return LanguageParameterName.with("xyz");
    }

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    @Override
    public Class<Language> type() {
        return Language.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // PredicateTesting2................................................................................................

    @Override
    public Language createPredicate() {
        return en();
    }

    // TypeNameTesting................................................................................................

    @Override
    public String typeNamePrefix() {
        return "Language";
    }

    @Override
    public String typeNameSuffix() {
        return ""; // No "Predicate" suffix.
    }
}
