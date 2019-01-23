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

public final class EncodedTextHeaderValueConverterTest extends
        HeaderValueConverterTestCase<EncodedTextHeaderValueConverter, EncodedText> {

    @Override
    protected String requiredPrefix() {
        return EncodedText.class.getSimpleName();
    }

    @Test
    public void testFilename() {
        final String filename = this.value().toHeaderText();
        this.parseAndToTextAndCheck(filename, this.value());
    }

    @Override
    EncodedTextHeaderValueConverter converter() {
        return EncodedTextHeaderValueConverter.INSTANCE;
    }

    @Override
    ContentDispositionParameterName name() {
        return ContentDispositionParameterName.FILENAME;
    }

    @Override
    String invalidHeaderValue() {
        return "utf-8'";
    }

    @Override
    EncodedText value() {
        return EncodedText.with(CharsetName.UTF_8, EncodedText.NO_LANGUAGE, "abc 123");
    }

    @Override
    String valueType() {
        return this.valueType(EncodedText.class);
    }

    @Override
    String converterToString() {
        return EncodedText.class.getSimpleName();
    }

    @Override
    protected Class<EncodedTextHeaderValueConverter> type() {
        return EncodedTextHeaderValueConverter.class;
    }
}
