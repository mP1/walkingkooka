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

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * Base class for all parsers that handle offset times, date/times.
 * It contains the shared logic to complain/fail if the pattern contains a timezone or is missing a offset.
 */
abstract class DateTimeFormatterParserOffset<C extends ParserContext> extends DateTimeFormatterParser<C> {

    DateTimeFormatterParserOffset(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        super(formatter);
    }

    @Override
    final void time(final DateTimeFormatterParserDateTimeFormatterPatternVisitor visitor) {
    }

    @Override
    final void timeZone(final DateTimeFormatterParserDateTimeFormatterPatternVisitor visitor) {
        visitor.invalidPatternLetter();
    }

    @Override
    final void timeZoneOffset(final DateTimeFormatterParserDateTimeFormatterPatternVisitor visitor) {
    }
}
