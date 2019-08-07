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

package walkingkooka.convert;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatterConverterLocalDateStringTest extends DateTimeFormatterConverterTestCase<DateTimeFormatterConverterLocalDateString, LocalDate, String> {

    @Test
    public void testConvert() {
        this.convertAndCheck(LocalDate.of(2000, 1, 31), "2000-01-31");
    }

    @Override
    protected DateTimeFormatterConverterLocalDateString createConverter(final DateTimeFormatter formatter) {
        return DateTimeFormatterConverterLocalDateString.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ISO_LOCAL_DATE;
    }

    @Override
    Class onlySupportedType() {
        return String.class;
    }

    @Override
    public Class<DateTimeFormatterConverterLocalDateString> type() {
        return DateTimeFormatterConverterLocalDateString.class;
    }
}
