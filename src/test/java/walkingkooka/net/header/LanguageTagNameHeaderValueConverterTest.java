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

public final class LanguageTagNameHeaderValueConverterTest extends
        HeaderValueConverterTestCase<LanguageTagNameHeaderValueConverter, LanguageTagName> {

    @Override
    protected String requiredPrefix() {
        return LanguageTagName.class.getSimpleName();
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck("en", LanguageTagName.with("en"));
    }

    @Override
    protected LanguageTagNameHeaderValueConverter converter() {
        return LanguageTagNameHeaderValueConverter.INSTANCE;
    }

    @Override
    protected LinkParameterName<LanguageTagName> name() {
        return LinkParameterName.HREFLANG;
    }

    @Override
    protected String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    protected LanguageTagName value() {
        return LanguageTagName.with("en");
    }

    @Override
    protected String converterToString() {
        return LanguageTagName.class.getSimpleName();
    }

    @Override
    protected Class<LanguageTagNameHeaderValueConverter> type() {
        return LanguageTagNameHeaderValueConverter.class;
    }
}
