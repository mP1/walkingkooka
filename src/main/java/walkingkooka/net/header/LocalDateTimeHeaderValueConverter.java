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

import walkingkooka.naming.Name;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

/**
 * A {@link HeaderValueConverter} that parses a header value into a {@link LocalDateTime}.
 * Sample Date/Time
 * <pre>
 * Fri, 07 Nov 2014 23:59:59 GMT
 * </pre>
 * <a href="https://en.wikipedia.org/wiki/List_of_HTTP_header_fields"></a>
 */
final class LocalDateTimeHeaderValueConverter extends HeaderValueConverter2<LocalDateTime> {

    /**
     * Singleton
     */
    final static LocalDateTimeHeaderValueConverter INSTANCE = new LocalDateTimeHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private LocalDateTimeHeaderValueConverter() {
        super();
    }

    @Override
    LocalDateTime parse0(final String text, final Name name) {
        return LocalDateTime.parse(text, FORMATTER);
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, LocalDateTime.class);
    }

    @Override
    String toText0(final LocalDateTime value, final Name name) {
        return FORMATTER.format(value);
    }

    // https://tools.ietf.org/html/rfc7231#section-7.1.1.2
    // https://tools.ietf.org/html/rfc7231#section-7.1.1.1
    // An example of the preferred format is
    //
    //     Sun, 06 Nov 1994 08:49:37 GMT    ; IMF-fixdate
    private final static DateTimeFormatter FORMATTER = formatter();

    // copy of CookieExpires
    private static DateTimeFormatter formatter() {
        final Map<Long, String> weekday = new HashMap<>();
        weekday.put(1L, "Mon");
        weekday.put(2L, "Tue");
        weekday.put(3L, "Wed");
        weekday.put(4L, "Thu");
        weekday.put(5L, "Fri");
        weekday.put(6L, "Sat");
        weekday.put(7L, "Sun");

        final Map<Long, String> month = new HashMap<>();
        month.put(1L, "Jan");
        month.put(2L, "Feb");
        month.put(3L, "Mar");
        month.put(4L, "Apr");
        month.put(5L, "May");
        month.put(6L, "Jun");
        month.put(7L, "Jul");
        month.put(8L, "Aug");
        month.put(9L, "Sep");
        month.put(10L, "Oct");
        month.put(11L, "Nov");
        month.put(12L, "Dec");

        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .parseLenient()
                .optionalStart()
                .appendText(DAY_OF_WEEK, weekday)
                .appendLiteral(", ")
                .optionalEnd()
                .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral(' ')
                .appendText(MONTH_OF_YEAR, month)
                .appendLiteral(' ')
                .appendValue(YEAR, 4)  // 2 digit year not handled
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalEnd()
                .appendLiteral(" GMT")
                .toFormatter();
    }

    @Override
    public String toString() {
        return toStringType(LocalDateTime.class);
    }
}
