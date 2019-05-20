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

public final class LanguageTagNameHeaderValueConverterTest extends
        NonStringHeaderValueConverterTestCase<LanguageTagNameHeaderValueConverter, LanguageTagName> {

    @Override
    public String typeNamePrefix() {
        return LanguageTagName.class.getSimpleName();
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck("en", LanguageTagName.with("en"));
    }

    @Override
    LanguageTagNameHeaderValueConverter converter() {
        return LanguageTagNameHeaderValueConverter.INSTANCE;
    }

    @Override
    LinkParameterName<LanguageTagName> name() {
        return LinkParameterName.HREFLANG;
    }

    @Override
    String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    LanguageTagName value() {
        return LanguageTagName.with("en");
    }

    @Override
    String valueType() {
        return this.valueType(LanguageTagName.class);
    }

    @Override
    String converterToString() {
        return LanguageTagName.class.getSimpleName();
    }

    @Override
    public Class<LanguageTagNameHeaderValueConverter> type() {
        return LanguageTagNameHeaderValueConverter.class;
    }
}
