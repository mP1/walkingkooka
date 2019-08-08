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

import walkingkooka.datetime.DateTimeContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

/**
 * A {@link Parser} that uses a {@link DateTimeFormatter} to parse a {@link LocalDateTime}.
 */
final class DateTimeFormatterParserLocalDateTime<C extends ParserContext> extends DateTimeFormatterParserLocal<C> {

    static <C extends ParserContext> DateTimeFormatterParserLocalDateTime<C> with(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        return new DateTimeFormatterParserLocalDateTime<>(formatter);
    }

    private DateTimeFormatterParserLocalDateTime(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        super(formatter);
    }

    @Override
    final void date(final DateTimeFormatterParserDateTimeFormatterPatternVisitor visitor) {
    }

    @Override
    final void time(final DateTimeFormatterParserDateTimeFormatterPatternVisitor visitor) {
    }

    @Override
    LocalDateTimeParserToken createParserToken(final TemporalAccessor value, final String text) {
        return ParserTokens.localDateTime(LocalDateTime.from(value), text);
    }
}
