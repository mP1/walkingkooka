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

public final class LanguageTagHeaderValueConverterTest extends
        NonStringHeaderValueConverterTestCase<LanguageTagHeaderValueConverter, LanguageTag> {

    @Override
    public String typeNamePrefix() {
        return LanguageTag.class.getSimpleName();
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck("en; q=0.5",
                LanguageTag.with(LanguageTagName.with("en"))
                        .setParameters(Maps.of(LanguageTagParameterName.Q_FACTOR, 0.5f)));
    }

    @Override
    LanguageTagHeaderValueConverter converter() {
        return LanguageTagHeaderValueConverter.INSTANCE;
    }

    @Override
    HttpHeaderName<LanguageTag> name() {
        return HttpHeaderName.CONTENT_LANGUAGE;
    }

    @Override
    String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    LanguageTag value() {
        return LanguageTag.parse("en; q=0.5");
    }

    @Override
    String valueType() {
        return this.valueType(LanguageTag.class);
    }

    @Override
    String converterToString() {
        return LanguageTag.class.getSimpleName();
    }

    @Override
    public Class<LanguageTagHeaderValueConverter> type() {
        return LanguageTagHeaderValueConverter.class;
    }
}
