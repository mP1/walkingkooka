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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public final class StringLocalTimeLocaleDateTimeFormatterConverterTest extends StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverterTestCase<StringLocalTimeLocaleDateTimeFormatterConverter, LocalTime> {

    @Override
    StringLocalTimeLocaleDateTimeFormatterConverter createConverter(final List<DateTimeFormatter> formatters) {
        return StringLocalTimeLocaleDateTimeFormatterConverter.with(formatters);
    }

    @Override
    Class<LocalTime> targetType() {
        return LocalTime.class;
    }

    @Override
    LocalTime value() {
        return LocalTime.of(12, 58, 59);
    }

    @Override
    DateTimeFormatter formatter1() {
        return DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    @Override
    DateTimeFormatter formatter2() {
        return DateTimeFormatter.ofPattern("ss:mm:HH");
    }

    @Override
    String format(final DateTimeFormatter formatter, final Locale locale) {
        return value().format(formatter.withLocale(locale));
    }

    @Override
    public Class<StringLocalTimeLocaleDateTimeFormatterConverter> type() {
        return StringLocalTimeLocaleDateTimeFormatterConverter.class;
    }
}
