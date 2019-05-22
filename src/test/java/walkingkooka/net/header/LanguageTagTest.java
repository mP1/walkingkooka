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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LanguageTagTest extends HeaderValueWithParametersTestCase<LanguageTag,
        LanguageTagParameterName<?>>
        implements ParseStringTesting<LanguageTag>,
        PredicateTesting<LanguageTag, LanguageTag> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LanguageTag.with(null);
        });
    }

    @Test
    public void testWith() {
        final LanguageTag languageTag = LanguageTag.with(this.value());
        this.check(languageTag);
    }

    @Test
    public void testWithCached() {
        assertSame(LanguageTag.with(LanguageTagName.WILDCARD), LanguageTag.with(LanguageTagName.WILDCARD));
    }

    @Test
    public void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LanguageTag.WILDCARD.setValue(null);
        });
    }

    @Test
    public void testSetValueSame() {
        final LanguageTag tag = LanguageTag.WILDCARD;
        assertSame(tag, tag.setValue(tag.value()));
    }

    @Test
    public void testSetValueSame2() {
        final LanguageTag tag = LanguageTag.with(this.value());
        assertSame(tag, tag.setValue(this.value()));
    }

    @Test
    public void testSetValueDifferent() {
        final LanguageTag tag = LanguageTag.WILDCARD;
        final LanguageTagName name = LanguageTagName.with("fr");
        final LanguageTag different = tag.setValue(name);
        assertNotSame(tag, different);

        this.check(different, name, LanguageTag.NO_PARAMETERS);
    }

    @Test
    public void testSetValueDifferent2() {
        final LanguageTag tag = LanguageTag.with(this.value());
        final LanguageTagName name = LanguageTagName.with("fr");
        final LanguageTag different = tag.setValue(name);
        assertNotSame(tag, different);

        this.check(different, name, LanguageTag.NO_PARAMETERS);
    }

    @Test
    public void testSetValueDifferent3() {
        final LanguageTag tag = LanguageTag.with(this.value()).setParameters(this.parametersWithQFactor());
        final LanguageTagName name = LanguageTagName.with("fr");
        final LanguageTag different = tag.setValue(name);
        assertNotSame(tag, different);
        this.check(different, name, this.parametersWithQFactor());
    }

    private void check(final LanguageTag tag) {
        this.check(tag, this.value(), LanguageTag.NO_PARAMETERS);
    }

    private LanguageTagName value() {
        return LanguageTagName.with("en");
    }

    @Test
    public final void testSetParameterDifferent() {
        final LanguageTag languageTag = this.createHeaderValueWithParameters();
        final Map<LanguageTagParameterName<?>, Object> parameters = this.parametersWithQFactor();
        final LanguageTag different = languageTag.setParameters(parameters);
        this.check(different, languageTag.value(), parameters);
    }

    final Map<LanguageTagParameterName<?>, Object> parametersWithQFactor() {
        return Maps.of(LanguageTagParameterName.Q_FACTOR, 0.75f);
    }

    @Test
    public void testSetParametersDifferentAndBack() {
        assertSame(LanguageTag.WILDCARD,
                LanguageTag.WILDCARD
                        .setParameters(this.parametersWithQFactor())
                        .setParameters(LanguageTag.NO_PARAMETERS));
    }

    final void check(final LanguageTag language,
                     final LanguageTagName value,
                     final Map<LanguageTagParameterName<?>, Object> parameters) {
        assertEquals(value, language.value(), "value");
        this.checkParameters(language, parameters);
    }

    // ParseStringTesting ........................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("en",
                LanguageTag.with(LanguageTagName.with("en")));
    }

    @Test
    public void testParseWithParameters() {
        this.parseAndCheck("en; abc=123",
                LanguageTag.with(LanguageTagName.with("en"))
                        .setParameters(Maps.of(LanguageTagParameterName.with("abc"), "123")));
    }

    @Override
    public LanguageTag parse(final String text) {
        return LanguageTag.parse(text);
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
                LanguageTag.WILDCARD);
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
        this.testTrue(LanguageTag.WILDCARD, this.en075WithParameters());
    }

    @Test
    public void testTestWildcardWildcardFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            LanguageTag.WILDCARD.test(LanguageTag.WILDCARD);
        });
    }

    @Test
    public void testTestWildcardNonWildcardFails() {
        this.testTrue(LanguageTag.WILDCARD,
                this.en075WithParameters());
    }

    @Test
    public void testTestWildcardNonWildcardFails2() {
        this.testTrue(LanguageTag.WILDCARD,
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

    private LanguageTag en075WithParameters() {
        return this.en()
                .setParameters(Maps.of(LanguageTagParameterName.Q_FACTOR, 0.75f));
    }

    private LanguageTag en() {
        return LanguageTag.with(LanguageTagName.with("en"));
    }

    private LanguageTag fr() {
        return LanguageTag.with(LanguageTagName.with("fr"));
    }

    @Override
    public LanguageTag createHeaderValueWithParameters() {
        return LanguageTag.WILDCARD;
    }

    @Override
    LanguageTagParameterName<?> parameterName() {
        return LanguageTagParameterName.with("xyz");
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
    public Class<LanguageTag> type() {
        return LanguageTag.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // PredicateTesting................................................................................................

    @Override
    public LanguageTag createPredicate() {
        return en();
    }

    // TypeNameTesting................................................................................................

    @Override
    public String typeNamePrefix() {
        return "LanguageTag";
    }

    @Override
    public String typeNameSuffix() {
        return ""; // No "Predicate" suffix.
    }
}
