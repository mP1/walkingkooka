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

/**
 * Base class for all parsers that handle offset times, date/times.
 * It contains the shared logic to complain/fail if the pattern contains a timezone or is missing a offset.
 */
abstract class DateTimeFormatterParserOffset<C extends ParserContext> extends DateTimeFormatterParser<C> {

    DateTimeFormatterParserOffset(final DateTimeFormatter formatter, final String pattern) {
        super(formatter, pattern);
    }

    @Override
    void zoneId(final char c, final String pattern) {
        this.failInvalidPattern(c, pattern);
    }

    @Override
    void zoneName(final char c, final String pattern) {
        this.failInvalidPattern(c, pattern);
    }

    @Override
    void localisedZoneOffset(final char c, final String pattern) {
        // ok.
    }

    @Override
    void zoneOffset(final char c, final String pattern) {
        // ok
    }

    @Override
    void zOrZoneOffset(final char c, final String pattern) {
        // ok
    }
}
