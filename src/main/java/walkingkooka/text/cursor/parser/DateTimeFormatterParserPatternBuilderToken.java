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

import walkingkooka.text.CharSequences;

import java.time.format.DateTimeFormatter;

/**
 * A token representation for the various types of tokens for {@link DateTimeFormatter} patterns.
 * Rather than represent each type of token as a separate enum value or class, a single class(this) is used
 * with an integer being the tag.
 * <p>
 * Values under {@link Character#MAX_VALUE} are tokens for that required character literal, values over are
 * required classes of characters.
 */
final class DateTimeFormatterParserPatternBuilderToken {

    DateTimeFormatterParserPatternBuilderToken(final int pattern) {
        this.pattern = pattern;
        this.count = 1;
    }

    DateTimeFormatterParserPatternBuilderToken finish() {
        switch (this.pattern) {
            case DateTimeFormatterParser.TEXT_DIGITS:
                this.finishTextDigits();
                break;
            default:
                break;
        }
        return this;
    }

    boolean requiresNew(final int c) {
        return c <= Character.MAX_VALUE || c != this.pattern;
    }

    // Number/Text: If the count of pattern letters is 3 or greater, use the Text rules above. Otherwise use the Number rules above.
    private void finishTextDigits() {
        this.pattern = this.count >= 3 ?
                DateTimeFormatterParser.TEXT :
                DateTimeFormatterParser.DIGITS;
    }

    boolean isDifferent(final DateTimeFormatterParserPatternBuilderToken other) {
        return this.pattern < Character.MAX_VALUE || this.pattern != other.pattern;
    }

    int pattern;

    void increaseCount() {
        this.count++;
    }

    private int count;

    @Override
    public String toString() {
        String toString;

        switch (this.pattern) {
            case DateTimeFormatterParser.TEXT:
                toString = "TEXT";
                break;
            case DateTimeFormatterParser.DIGITS:
                toString = "DIGITS";
                break;
            case DateTimeFormatterParser.FRACTION:
                toString = "FRACTION";
                break;
            case DateTimeFormatterParser.TEXT_DIGITS:
                toString = "TEXT_DIGITS";
                break;
            case DateTimeFormatterParser.WHITESPACE:
                toString = "WHITESPACE";
                break;
            case DateTimeFormatterParser.ZONEID:
                toString = "ZONEID";
                break;
            case DateTimeFormatterParser.ZONENAME:
                toString = "ZONENAME";
                break;
            case DateTimeFormatterParser.LOCALISED_ZONE_OFFSET:
                toString = "LOCALISED_ZONE_OFFSET";
                break;
            case DateTimeFormatterParser.ZONE_OFFSET:
                toString = "ZONE_OFFSET";
                break;
            case DateTimeFormatterParser.Z_OR_ZONE_OFFSET:
                toString = "Z_OR_ZONE_OFFSET";
                break;
            default:
                toString = CharSequences.quoteIfChars((char) this.pattern).toString();
                break;
        }

        return toString;
    }
}
