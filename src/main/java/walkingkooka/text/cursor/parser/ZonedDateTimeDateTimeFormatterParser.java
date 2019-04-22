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

package walkingkooka.text.cursor.parser;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * A {@link Parser} that uses a {@link DateTimeFormatter} to parse a {@link ZonedDateTime}.
 */
final class ZonedDateTimeDateTimeFormatterParser<C extends ParserContext> extends DateTimeFormatterParser<C> {

    static <C extends ParserContext> ZonedDateTimeDateTimeFormatterParser<C> with(final DateTimeFormatter formatter, final String pattern) {
        return new ZonedDateTimeDateTimeFormatterParser<>(formatter, pattern);
    }

    private ZonedDateTimeDateTimeFormatterParser(DateTimeFormatter formatter, final String pattern) {
        super(formatter, pattern);
    }

    @Override
    void date(final char c, final String pattern) {
        // ok!
    }

    @Override
    void time(final char c, final String pattern) {
        // ok!
    }

    @Override
    void zoneId(final char c, final String pattern) {
        // ok!
    }

    @Override
    void zoneName(final char c, final String pattern) {
        // ok!
    }

    @Override
    void localisedZoneOffset(final char c, final String pattern) {
        // ok!
    }

    @Override
    void zoneOffset(final char c, final String pattern) {
        // ok!
    }

    @Override
    void zOrZoneOffset(final char c, final String pattern) {
        // ok!
    }

    @Override
    ZonedDateTimeParserToken createParserToken(final TemporalAccessor value, final String text) {
        return ParserTokens.zonedDateTime(ZonedDateTime.from(value), text);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ZonedDateTimeDateTimeFormatterParser;
    }
}
