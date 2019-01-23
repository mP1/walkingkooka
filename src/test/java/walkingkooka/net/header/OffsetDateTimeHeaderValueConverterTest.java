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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public final class OffsetDateTimeHeaderValueConverterTest extends
        HeaderValueConverterTestCase<OffsetDateTimeHeaderValueConverter, OffsetDateTime> {

    @Test(expected = HeaderValueException.class)
    public void testyParseEmptyFails() {
        this.parse("");
    }

    @Test(expected = HeaderValueException.class)
    public void testyParseMissingOpeningDoubleQuoteFails() {
        this.parse("abc\"");
    }

    @Test(expected = HeaderValueException.class)
    public void testyParseMissingClosingDoubleQuoteFails() {
        this.parse("\"abc");
    }

    @Override
    protected String requiredPrefix() {
        return OffsetDateTime.class.getSimpleName();
    }

    @Test(expected = HeaderValueException.class)
    public void testDateWithGmtFails() {
        OffsetDateTimeHeaderValueConverter.INSTANCE.parse("\"Wed, 21 Oct 2015 07:28:00 GMT\"",
                ContentDispositionParameterName.CREATION_DATE);
    }

    @Test
    public void testContentDispositionCreationDateNegativeOffset() {
        this.parseAndToTextAndCheck("\"Wed, 21 Oct 2015 07:28:00 -0500\"",
                OffsetDateTime.of(2015,
                        10,
                        21,
                        7,
                        28,
                        0,
                        0,
                        ZoneOffset.ofHours(-5)));
    }

    @Test
    public void testContentDispositionCreationDatePositiveOffset() {
        this.parseAndToTextAndCheck("\"Wed, 21 Oct 2015 07:28:00 +0500\"",
                OffsetDateTime.of(2015,
                        10,
                        21,
                        7,
                        28,
                        0,
                        0,
                        ZoneOffset.ofHours(+5)));
    }

    @Override
    OffsetDateTimeHeaderValueConverter converter() {
        return OffsetDateTimeHeaderValueConverter.INSTANCE;
    }

    @Override
    ContentDispositionParameterName<OffsetDateTime> name() {
        return ContentDispositionParameterName.CREATION_DATE;
    }

    @Override
    String invalidHeaderValue() {
        return "abc";
    }

    @Override
    OffsetDateTime value() {
        return OffsetDateTime.of(2000,
                12,
                31,
                6,
                28,
                29,
                0,
                ZoneOffset.ofHours(+10));
    }

    @Override
    String converterToString() {
        return OffsetDateTime.class.getSimpleName();
    }

    @Override
    protected Class<OffsetDateTimeHeaderValueConverter> type() {
        return OffsetDateTimeHeaderValueConverter.class;
    }
}
