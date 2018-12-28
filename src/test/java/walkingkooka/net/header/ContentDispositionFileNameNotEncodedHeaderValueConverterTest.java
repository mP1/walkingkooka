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

public final class ContentDispositionFileNameNotEncodedHeaderValueConverterTest extends
        HeaderValueConverterTestCase<ContentDispositionFileNameNotEncodedHeaderValueConverter, ContentDispositionFileName> {
    @Override
    protected String requiredPrefix() {
        return ContentDispositionFileName.class.getSimpleName();
    }

    @Test
    public void testFilename() {
        final String filename = "readme.txt";
        this.parseAndToTextAndCheck(filename, ContentDispositionFileName.notEncoded(filename));
    }

    @Override
    protected ContentDispositionFileNameNotEncodedHeaderValueConverter converter() {
        return ContentDispositionFileNameNotEncodedHeaderValueConverter.INSTANCE;
    }

    @Override
    protected ContentDispositionParameterName name() {
        return ContentDispositionParameterName.FILENAME;
    }

    @Override
    protected String invalidHeaderValue() {
        return "\0";
    }

    @Override
    protected ContentDispositionFileName value() {
        return ContentDispositionFileName.notEncoded("readme.txt");
    }

    @Override
    protected String converterToString() {
        return ContentDispositionFileNameNotEncoded.class.getSimpleName();
    }

    @Override
    protected Class<ContentDispositionFileNameNotEncodedHeaderValueConverter> type() {
        return ContentDispositionFileNameNotEncodedHeaderValueConverter.class;
    }
}
