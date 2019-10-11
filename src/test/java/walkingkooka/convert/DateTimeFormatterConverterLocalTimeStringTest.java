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
import walkingkooka.datetime.DateTimeContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class DateTimeFormatterConverterLocalTimeStringTest extends DateTimeFormatterConverterTestCase2<DateTimeFormatterConverterLocalTimeString, LocalTime, String> {

    @Test
    public void testLocaleChange() {
        final DateTimeFormatterConverterLocalTimeString converter = this.createConverter();
        final LocalTime source = this.source();

        this.convertAndCheck2(converter,
                source,
                this.createContext(),
                this.converted());

        this.convertAndCheck2(converter,
                source,
                this.createContext2(),
                "12:58:59 nachm.");
    }

    @Override
    protected DateTimeFormatterConverterLocalTimeString createConverter(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return DateTimeFormatterConverterLocalTimeString.with(formatter);
    }

    @Override
    DateTimeFormatter formatter() {
        return DateTimeFormatter.ofPattern("hh:mm:ss a");
    }

    @Override
    Class<String> targetType() {
        return String.class;
    }

    @Override
    LocalTime source() {
        return LocalTime.of(12, 58, 59);
    }

    @Override
    String converted() {
        return "12:58:59 PM";
    }

    @Override
    public Class<DateTimeFormatterConverterLocalTimeString> type() {
        return DateTimeFormatterConverterLocalTimeString.class;
    }
}
