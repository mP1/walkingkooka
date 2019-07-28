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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public final class StringLocalDateTimeLocaleDateTimeFormatterConverterTest extends StringLocalDateLocaleDateTimeLocalTimeLocaleDateTimeFormatterConverterTestCase<StringLocalDateTimeLocaleDateTimeFormatterConverter, LocalDateTime> {

    @Override
    StringLocalDateTimeLocaleDateTimeFormatterConverter createConverter(final List<DateTimeFormatter> formatters) {
        return StringLocalDateTimeLocaleDateTimeFormatterConverter.with(formatters);
    }

    @Override
    Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }

    @Override
    LocalDateTime value() {
        return LocalDateTime.of(2000, 12, 31, 12, 58, 59);
    }

    @Override
    DateTimeFormatter formatter1() {
        return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    }

    @Override
    DateTimeFormatter formatter2() {
        return DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
    }

    @Override
    String format(final DateTimeFormatter formatter, final Locale locale) {
        return value().format(formatter.withLocale(locale));
    }

    @Override
    public Class<StringLocalDateTimeLocaleDateTimeFormatterConverter> type() {
        return StringLocalDateTimeLocaleDateTimeFormatterConverter.class;
    }
}
