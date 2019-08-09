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

package walkingkooka.text.cursor.parser;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Holds the {@link Locale} and {@link int twoDigitYear} that were used to build the {@link DateTimeFormatter}.
 */
final class DateTimeFormatterParserCache {

    static DateTimeFormatterParserCache with(final Locale locale,
                                             final int twoDigitYear,
                                             final DateTimeFormatter formatter) {
        return new DateTimeFormatterParserCache(locale, twoDigitYear, formatter);
    }

    private DateTimeFormatterParserCache(final Locale locale,
                                         final int twoDigitYear,
                                         final DateTimeFormatter formatter) {
        super();

        this.locale = locale;
        this.twoDigitYear = twoDigitYear;
        this.formatter = formatter;
    }

    final Locale locale;
    final int twoDigitYear;
    final DateTimeFormatter formatter;

    @Override
    public String toString() {
        return this.locale + " " + this.twoDigitYear + " " + this.formatter;
    }
}
