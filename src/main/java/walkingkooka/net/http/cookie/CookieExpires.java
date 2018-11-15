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

package walkingkooka.net.http.cookie;

import walkingkooka.Cast;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

/**
 * The <code>Expires</code> attribute of a {@link Cookie}
 */
final public class CookieExpires extends CookieDeletion {

    /**
     * Accepts the expires attribute value and creates a {@link CookieDeletion}
     */
    static Optional<CookieDeletion> parseExpires(final String text) {
        Objects.requireNonNull(text, "text");

        Optional<CookieDeletion> expires = Optional.empty();

        if (!text.isEmpty()) {
            try {
                expires = Optional.of(with(LocalDateTime.parse(text, FORMATTER)));
            } catch (final DateTimeParseException cause) {
                throw new IllegalArgumentException(cause.getMessage(), cause);
            }
        }

        return expires;
    }

    /**
     * Creates a new {@link CookieExpires}
     */
    public static CookieExpires with(final LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime");
        return new CookieExpires(dateTime);
    }

    /**
     * Private constructor use factory
     */
    private CookieExpires(final LocalDateTime dateTime) {
        super();
        this.dateTime = dateTime;
    }

    // CookieDeletion

    @Override
    public boolean isMaxAge() {
        return false;
    }

    @Override
    public boolean isExpires() {
        return true;
    }

    @Override
    int toMaxAgeSeconds(final LocalDateTime now) {
        return (int) (this.dateTime.toEpochSecond(OFFSET) - now.toEpochSecond(OFFSET));
    }

    private final static ZoneOffset OFFSET = ZoneOffset.UTC;

    /**
     * Returns when {@link LocalDateTime} the parent cookie expires.
     */
    public LocalDateTime dateTime() {
        return this.dateTime;
    }

    private final LocalDateTime dateTime;

    // Object

    @Override
    public int hashCode() {
        return this.dateTime.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof CookieExpires &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final CookieExpires other) {
        return this.dateTime.equals(other.dateTime);
    }

    @Override
    public String toString() {
        return Cookie.EXPIRES + Cookie.NAME_VALUE_SEPARATOR + FORMATTER.format(this.dateTime);
    }

    // https://tools.ietf.org/html/rfc7231#section-7.1.1.2
    // https://tools.ietf.org/html/rfc7231#section-7.1.1.1
    // An example of the preferred format is
    //
    //     Sun, 06 Nov 1994 08:49:37 GMT    ; IMF-fixdate
    private final static DateTimeFormatter FORMATTER =  formatter();

    private static DateTimeFormatter formatter(){
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
}
