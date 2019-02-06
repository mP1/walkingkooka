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

public final class MediaTypeBoundaryHeaderValueConverterTest extends
        HeaderValueConverterTestCase<MediaTypeBoundaryHeaderValueConverter, MediaTypeBoundary> {

    private final static String TEXT = "abc123";

    @Override
    protected String requiredPrefix() {
        return MediaTypeBoundary.class.getSimpleName();
    }

    @Override
    public void testInvalidHeaderValueFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseWithQuotes() {
        this.parseAndCheck("\"abcdef\"",
                MediaTypeBoundary.with("abcdef"));
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck(TEXT, this.value());
    }

    @Test
    public void testRoundtripRequiresQuotes() {
        final String text = "abcdef1234567890'()+_,-./:=?";
        this.parseAndToTextAndCheck('"' + text + '"',
                MediaTypeBoundary.with(text));
    }

    @Override
    MediaTypeBoundaryHeaderValueConverter converter() {
        return MediaTypeBoundaryHeaderValueConverter.INSTANCE;
    }

    @Override
    MediaTypeParameterName<MediaTypeBoundary> name() {
        return MediaTypeParameterName.BOUNDARY;
    }

    @Override
    String invalidHeaderValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    MediaTypeBoundary value() {
        return MediaTypeBoundary.with(TEXT);
    }

    @Override
    String valueType() {
        return this.valueType(MediaTypeBoundary.class);
    }

    @Override
    String converterToString() {
        return MediaTypeBoundary.class.getSimpleName();
    }

    @Override
    public Class<MediaTypeBoundaryHeaderValueConverter> type() {
        return MediaTypeBoundaryHeaderValueConverter.class;
    }
}
