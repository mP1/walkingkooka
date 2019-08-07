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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatterConverterStringLocalTimeTest extends DateTimeFormatterConverterTestCase<DateTimeFormatterConverterStringLocalTime, String, LocalTime> {

    @Test
    public void testConvert() {
        this.convertAndCheck("59 58 12", LocalTime.of(12, 58, 59));
    }

    @Test
    public void testConvert2() {
        this.convertAndCheck(DateTimeFormatterConverterStringLocalTime.with(DateTimeFormatter.ofPattern("ss HH mm")),
                "59 12 58",
                LocalTime.class,
                LocalTime.of(12, 58, 59));
    }

    @Override
    protected DateTimeFormatterConverterStringLocalTime createConverter(final DateTimeFormatter formatter) {
        return DateTimeFormatterConverterStringLocalTime.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("ss mm HH");
    }

    @Override
    protected Class<LocalTime> onlySupportedType() {
        return LocalTime.class;
    }

    @Override
    public Class<DateTimeFormatterConverterStringLocalTime> type() {
        return DateTimeFormatterConverterStringLocalTime.class;
    }
}
