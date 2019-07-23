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
import walkingkooka.naming.NameTesting;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.JavaVisibility;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class LanguageNameTestCase<L extends LanguageName> implements ClassTesting2<L>,
        NameTesting<L, LanguageName>,
        PredicateTesting2<L, LanguageName>,
        TypeNameTesting<L> {

    LanguageNameTestCase() {
        super();
    }

    @Override
    public void testPublicClass() {
        // ignore
    }

    @Test
    public final void testSetParameters() {
        final L name = this.createName(this.nameText());
        final Map<LanguageParameterName<?>, Object> parameters = Maps.of(LanguageParameterName.Q_FACTOR, 0.5f);
        final LanguageWithParameters language = name.setParameters(parameters);
        assertSame(name, language.value(), "value");
        assertEquals(parameters, language.parameters(), "parameters");
    }

    @Test
    public final void testWildcardFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createPredicate().test(LanguageName.WILDCARD);
        });
    }

    final void check(final LanguageName name,
                     final String value,
                     final Optional<Locale> locale) {
        assertEquals(value, name.value(), "value");
        assertEquals(locale, name.locale(), "locale");
    }

    @Override
    public final CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public final Class<L> type() {
        return this.languageTagNameType();
    }

    abstract Class<L> languageTagNameType();

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return LanguageName.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }

    // PredicateTesting2 ................................................................................................

    @Override
    public final L createPredicate() {
        return this.createName(this.nameText());
    }
}
