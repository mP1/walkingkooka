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

import org.junit.Ignore;
import org.junit.Test;

public final class MediaTypeBoundaryHeaderValueConverterTest extends
        HeaderValueConverterTestCase<MediaTypeBoundaryHeaderValueConverter, MediaTypeBoundary> {

    private final static String TEXT = "abc123";

    @Override
    protected String requiredPrefix() {
        return MediaTypeBoundary.class.getSimpleName();
    }

    @Test
    @Ignore
    public void testInvalidHeaderValueFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseWithQuotes() {
        this.parseAndCheck("\"abcdef\"",
                MediaTypeBoundary.with0("abcdef", "\"abcdef\""));
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck(TEXT, this.value());
    }

    @Test
    public void testRoundtrip2() {
        final String text = "abcdef1234567890'()+_,-./:=?";
        this.parseAndToTextAndCheck(text, MediaTypeBoundary.with(text));
    }

    @Override
    protected MediaTypeBoundaryHeaderValueConverter converter() {
        return MediaTypeBoundaryHeaderValueConverter.INSTANCE;
    }

    @Override
    protected MediaTypeParameterName<MediaTypeBoundary> name() {
        return MediaTypeParameterName.BOUNDARY;
    }

    @Override
    protected String invalidHeaderValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected MediaTypeBoundary value() {
        return MediaTypeBoundary.with(TEXT);
    }

    @Override
    protected String converterToString() {
        return MediaTypeBoundary.class.getSimpleName();
    }

    @Override
    protected Class<MediaTypeBoundaryHeaderValueConverter> type() {
        return MediaTypeBoundaryHeaderValueConverter.class;
    }
}
