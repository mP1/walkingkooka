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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public final class StringLocalDateLocaleDateTimeFormatterConverterTest extends StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverterTestCase<StringLocalDateLocaleDateTimeFormatterConverter, LocalDate> {

    @Override
    StringLocalDateLocaleDateTimeFormatterConverter createConverter(final List<DateTimeFormatter> formatters) {
        return StringLocalDateLocaleDateTimeFormatterConverter.with(formatters);
    }

    @Override
    Class<LocalDate> targetType() {
        return LocalDate.class;
    }

    @Override
    LocalDate value() {
        return LocalDate.of(2000, 12, 31);
    }

    @Override
    DateTimeFormatter formatter1() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd");
    }

    @Override
    DateTimeFormatter formatter2() {
        return DateTimeFormatter.ofPattern("MM/yyyy/dd");
    }

    @Override
    String format(final DateTimeFormatter formatter, final Locale locale) {
        return value().format(formatter.withLocale(locale));
    }

    @Override
    public Class<StringLocalDateLocaleDateTimeFormatterConverter> type() {
        return StringLocalDateLocaleDateTimeFormatterConverter.class;
    }
}
