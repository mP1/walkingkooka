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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class LanguageTagNameTestCase<L extends LanguageTagName> extends ClassTestCase<L>
        implements NameTesting<L, LanguageTagName> {

    LanguageTagNameTestCase() {
        super();
    }

    @Test
    @Override
    public final void testNaming() {
        this.checkNamingStartAndEnd(LanguageTagName.class.getSimpleName(), "");
    }

    @Override
    public void testPublicClass() {
        // ignore
    }

    @Test
    public final void testSetParameters() {
        final LanguageTagName name = this.createName("en");
        final Map<LanguageTagParameterName<?>, Object> parameters = Maps.one(LanguageTagParameterName.Q_FACTOR, 0.5f);
        final LanguageTag tag = name.setParameters(parameters);
        assertSame(name, tag.value(), "value");
        assertEquals(parameters, tag.parameters(), "parameters");
    }

    final void check(final LanguageTagName name,
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
    protected final Class<L> type() {
        return Cast.to(this.languageTagNameType());
    }

    abstract Class<L> languageTagNameType();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
