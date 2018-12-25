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
import walkingkooka.collect.map.Maps;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class LanguageTagTestCase<L extends LanguageTag> extends HeaderValueWithParametersTestCase<L,
        LanguageTagParameterName<?>> {

    LanguageTagTestCase() {
        super();
    }

    @Test
    public final void testSetParameterDifferent() {
        final LanguageTag languageTag = this.createHeaderValueWithParameters();
        final Map<LanguageTagParameterName<?>, Object> parameters = this.parametersWithQFactor();
        final LanguageTag different = languageTag.setParameters(parameters);
        this.check(different, languageTag.value(), languageTag.locale(), parameters);
    }

    @Override
    protected final boolean isMultipart() {
        return false;
    }

    @Override
    protected final boolean isRequest() {
        return true;
    }

    @Override
    protected final boolean isResponse() {
        return true;
    }

    final Map<LanguageTagParameterName<?>, Object> parametersWithQFactor() {
        return Maps.one(LanguageTagParameterName.Q_FACTOR, 0.75f);
    }

    final void check(final LanguageTag language,
                     final String value,
                     final Optional<Locale> locale,
                     final Map<LanguageTagParameterName<?>, Object> parameters) {
        assertEquals("value", value, language.value());
        assertEquals("locale", locale, language.locale());
        assertEquals("parameters", parameters, language.parameters());
    }
}
