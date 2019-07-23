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
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.map.Maps;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LanguageParameterNameTest extends HeaderParameterNameTestCase<LanguageParameterName<?>,
        LanguageParameterName<?>> {

    @Test
    public void testWithIncludesWhitespaceFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            LanguageParameterName.with("paramet er");
        });
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc123");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(LanguageParameterName.Q_FACTOR,
                LanguageParameterName.with(LanguageParameterName.Q_FACTOR.value()));
    }

    @Test
    public void testConstantNameCaseInsensitiveReturnsConstant() {
        final String differentCase = LanguageParameterName.Q_FACTOR.value().toUpperCase();
        assertNotEquals(differentCase, LanguageParameterName.Q_FACTOR.value());
        assertSame(LanguageParameterName.Q_FACTOR, LanguageParameterName.with(differentCase));
    }

    // parameter value......................................................................................

    @Test
    public void testParameterValueAbsent() {
        this.parameterValueAndCheckAbsent(LanguageParameterName.Q_FACTOR,
                this.languageTag());
    }

    @Test
    public void testParameterValuePresent() {
        final LanguageParameterName<Float> parameter = LanguageParameterName.Q_FACTOR;
        final Float value = 0.75f;

        this.parameterValueAndCheckPresent(parameter,
                this.languageTag().setParameters(Maps.of(parameter, value)),
                value);
    }

    private Language languageTag() {
        return Language.WILDCARD;
    }

    @Override
    public LanguageParameterName<Object> createName(final String name) {
        return Cast.to(LanguageParameterName.with(name));
    }

    @Override
    public Class<LanguageParameterName<?>> type() {
        return Cast.to(LanguageParameterName.class);
    }
}
