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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * A {@link Parser} that uses a {@link DateTimeFormatter} to parse a {@link LocalTime}.
 */
final class LocalTimeDateTimeFormatterParser<C extends ParserContext> extends LocalDateTimeFormatterParser<LocalTimeParserToken, C>{

    static <C extends ParserContext> LocalTimeDateTimeFormatterParser<C> with(final DateTimeFormatter formatter, final String pattern) {
        return new LocalTimeDateTimeFormatterParser<>(formatter, pattern);
    }

    private LocalTimeDateTimeFormatterParser(DateTimeFormatter formatter, final String pattern) {
        super(formatter, pattern);
    }

    @Override
    void date(final char c, final String pattern) {
        this.failInvalidPattern(c, pattern);
    }

    @Override
    void time(final char c, final String pattern){
        // ok!
    }

    @Override
    LocalTimeParserToken createParserToken(final TemporalAccessor value, final String text) {
        return ParserTokens.localTime(LocalTime.from(value), text);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LocalTimeDateTimeFormatterParser;
    }
}
