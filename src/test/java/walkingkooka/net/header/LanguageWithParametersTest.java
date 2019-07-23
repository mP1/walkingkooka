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

public final class LanguageWithParametersTest extends HeaderValueWithParametersTestCase<LanguageWithParameters,
        LanguageParameterName<?>>
        implements ParseStringTesting<LanguageWithParameters>,
        PredicateTesting2<LanguageWithParameters, LanguageName> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LanguageWithParameters.with(null);
        });
    }

    @Test
    public void testWith() {
        final LanguageWithParameters language = LanguageWithParameters.with(this.en());
        this.check(language);
    }

    @Test
    public void testWithCached() {
        assertSame(LanguageWithParameters.with(LanguageName.WILDCARD), LanguageWithParameters.with(LanguageName.WILDCARD));
    }

    //setValue..........................................................................................................

    @Test
    public void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LanguageWithParameters.WILDCARD.setValue(null);
        });
    }

    @Test
    public void testSetValueSame() {
        final LanguageWithParameters language = LanguageWithParameters.WILDCARD;
        assertSame(language, language.setValue(LanguageName.WILDCARD));
    }

    @Test
    public void testSetValueSame2() {
        final LanguageWithParameters language = LanguageWithParameters.with(this.en());
        assertSame(language, language.setValue(this.en()));
    }

    @Test
    public void testSetValueDifferent() {
        final LanguageWithParameters language = LanguageWithParameters.WILDCARD;
        final LanguageName name = this.fr();
        final LanguageWithParameters different = language.setValue(name);
        assertNotSame(language, different);

        this.check(different, name, LanguageWithParameters.NO_PARAMETERS);
    }

    @Test
    public void testSetValueDifferent2() {
        final LanguageWithParameters language = LanguageWithParameters.with(this.en());
        final LanguageName name = this.fr();
        final LanguageWithParameters different = language.setValue(name);
        assertNotSame(language, different);

        this.check(different, name, LanguageWithParameters.NO_PARAMETERS);
    }

    @Test
    public void testSetValueDifferentWithParameters() {
        final LanguageWithParameters language = LanguageWithParameters.with(this.en()).setParameters(this.parametersWithQFactor());
        final LanguageName name = this.fr();
        final LanguageWithParameters different = language.setValue(name);
        assertNotSame(language, different);
        this.check(different, name, this.parametersWithQFactor());
    }

    private void check(final LanguageWithParameters language) {
        this.check(language, this.en(), LanguageWithParameters.NO_PARAMETERS);
    }

    // setParameters....................................................................................................

    @Test
    public final void testSetParameterDifferent() {
        final LanguageWithParameters language = this.createHeaderValueWithParameters();
        final Map<LanguageParameterName<?>, Object> parameters = this.parametersWithQFactor();
        final LanguageWithParameters different = language.setParameters(parameters);
        this.check(different, this.en(), parameters);
    }

    final Map<LanguageParameterName<?>, Object> parametersWithQFactor() {
        return Maps.of(LanguageParameterName.Q_FACTOR, 0.75f);
    }

    @Test
    public void testSetParametersDifferentAndBack() {
        assertSame(LanguageWithParameters.WILDCARD,
                LanguageWithParameters.WILDCARD
                        .setParameters(this.parametersWithQFactor())
                        .setParameters(LanguageWithParameters.NO_PARAMETERS));
    }

    final void check(final LanguageWithParameters language,
                     final LanguageName value,
                     final Map<LanguageParameterName<?>, Object> parameters) {
        assertEquals(value, language.value(), "value");
        this.checkParameters(language, parameters);
    }

    // ParseStringTesting ..............................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("en",
                LanguageWithParameters.with(this.en()));
    }

    @Test
    public void testParseWithParameters() {
        this.parseAndCheck("en; abc=123",
                LanguageWithParameters.with(this.en())
                        .setParameters(Maps.of(LanguageParameterName.with("abc"), "123")));
    }

    @Override
    public LanguageWithParameters parse(final String text) {
        return LanguageWithParameters.parse(text);
    }

    // toHeaderTextList................................................................................................

    @Test
    public void testToHeaderTextList() {
        this.toHeaderTextAndCheck(LanguageWithParameters.with(this.en()), "en");
    }

    @Test
    public void testToHeaderTextListWithParameters() {
        this.toHeaderTextListAndCheck("en; q=0.75",
                this.en()
                        .setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 0.75f)));
    }

    @Test
    public void testToHeaderTextListWildcard() {
        this.toHeaderTextListAndCheck("*",
                LanguageWithParameters.WILDCARD);
    }

    // test ............................................................................................................

    @Test
    public void testTestWildcard() {
        this.testTrue(LanguageWithParameters.WILDCARD, this.en());
    }

    @Test
    public void testTestWildcardNonWildcardFails2() {
        this.testTrue(LanguageWithParameters.WILDCARD, this.fr());
    }

    @Test
    public void testTestNonWildcardNonWildcard() {
        this.testTrue(LanguageWithParameters.with(this.en()), this.en());
    }

    @Test
    public void testTestNonWildcardNonWildcard2() {
        this.testFalse(LanguageWithParameters.with(this.en()), this.fr());
    }

    @Override
    public LanguageWithParameters createHeaderValueWithParameters() {
        return LanguageWithParameters.with(this.en());
    }

    private LanguageName en() {
        return LanguageName.with("en");
    }

    private LanguageName fr() {
        return LanguageName.with("fr");
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
    public Class<LanguageWithParameters> type() {
        return LanguageWithParameters.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // PredicateTesting2................................................................................................

    @Override
    public LanguageWithParameters createPredicate() {
        return this.createHeaderValueWithParameters();
    }

    // TypeNameTesting...................................................................................................

    @Override
    public String typeNamePrefix() {
        return "Language";
    }

    @Override
    public String typeNameSuffix() {
        return ""; // No "Predicate" suffix.
    }
}
