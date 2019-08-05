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

package walkingkooka.datetime;

/**
 * An enum exists for each unique {@link java.time.format.DateTimeFormatter} component pattern/letter
 */
enum DateTimeFormatterPatternComponent {

    ERA {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitEra);
        }
    },
    YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverse(pattern,
                    position,
                    MAX_YEAR,
                    visitor::visitYear);
        }
    },
    YEAR_OF_ERA {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverse(pattern,
                    position,
                    MAX_YEAR,
                    visitor::visitYearOfEra);
        }
    },
    DAY_OF_YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_OF_YEAR,
                    visitor::visitDayOfYear);
        }
    },
    MONTH_OF_YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitMonthOfYear);
        }
    },
    MONTH_OF_YEAR_STANDALONE {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitStandaloneMonthOfYear);
        }
    },
    DAY_OF_MONTH {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitDayOfMonth);
        }
    },
    QUARTER_OF_YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitQuarterOfYear);
        }
    },
    STANDALONE_QUARTER_OF_YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitStandaloneQuarterOfYear);
        }
    },
    WEEK_BASED_YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverse(pattern,
                    position,
                    MAX_YEAR,
                    visitor::visitWeekBasedYear);
        }
    },
    WEEK_OF_WEEK_BASED_YEAR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_WEEK_OF_WEEK_BASED_YEAR,
                    visitor::visitWeekOfWeekBasedYear);
        }
    },
    WEEK_OF_MONTH {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_WEEK_OF_MONTH,
                    visitor::visitWeekOfMonthW);
        }
    },
    DAY_OF_WEEK {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitDayOfWeek);
        }
    },
    LOCALIZED_DAY_OF_WEEK {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitLocalizedDayOfWeek);
        }
    },
    STANDALONE_LOCALIZED_DAY_OF_WEEK {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::traverseStandaloneLocalizedDayOfWeek);
        }
    },
    WEEK_OF_MONTH_F {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_WEEK_OF_MONTH,
                    visitor::traverseWeekOfMonthF);
        }
    },
    AMPM_OF_DAY {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseText(pattern,
                    position,
                    MAX_AMPM,
                    visitor::visitAmpmOfDay);
        }
    },
    CLOCK_HOUR_OF_AMPM12 {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitClockHourOfAmpm12);
        }
    },
    HOUR_OF_AMPM11 {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitHourOfAmpm11);
        }
    },
    CLOCK_HOUR_OF_AMPM24 {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitClockHourOfAmpm24);
        }
    },
    HOUR_OF_DAY {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitHourOfDay23);
        }
    },
    MINUTE_OF_HOUR {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitMinuteOfHour);
        }
    },
    SECOND_OF_MINUTE {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_DAY_HOUR_MINUTE_SEC,
                    visitor::visitSecondOfMinute);
        }
    },
    FRACTION_OF_SECOND {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_FRACTION_OF_SECOND,
                    visitor::visitFractionOfSecond);
        }
    },
    MILLI_OF_DAY {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    Integer.MAX_VALUE,
                    visitor::visitMilliOfDay);
        }
    },
    NANO_OF_SECOND {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    Integer.MAX_VALUE,
                    visitor::visitNanoOfSecond);
        }
    },
    NANO_OF_DAY {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    Integer.MAX_VALUE,
                    visitor::visitNanoOfDay);
        }
    },
    TIMEZONE_ID {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_NARROW,
                    visitor::traverseTimezoneId);
        }
    },
    TIMEZONE_NAME {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            /**
             * <pre>
             * Zone names: This outputs the display name of the time-zone ID. If the count of letters is one, two or three,
             * then the short name is output. If the count of letters is four, then the full name is output. Five or more letters throws IllegalArgumentException.
             * </pre>
             */
            return visitor.traverseText(pattern,
                    position,
                    MAX_TIMEZONE_NAME,
                    visitor::visitTimeZoneName);
        }
    },
    LOCALIZED_ZONE_OFFSET {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumberOrText(pattern,
                    position,
                    MAX_NARROW,
                    visitor::traverseLocalizedZoneOffset);
        }
    },
    ZONE_OFFSET_BIGX {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitZoneOffsetBigX);
        }
    },
    ZONE_OFFSET_SMALLX {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitZoneOffsetSmallX);
        }
    },
    ZONE_OFFSET_Z {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitZoneOffsetZ);
        }
    },
    PAD {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseNumber(pattern,
                    position,
                    MAX_NARROW,
                    visitor::visitPad);
        }
    },
    ESCAPE {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseEscaped(pattern, position);
        }
    },
    OPTIONAL_START {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverse(pattern,
                    position,
                    MAX_OPTIONAL,
                    visitor::visitOptionalStart);
        }
    },
    OPTIONAL_END {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverse(pattern,
                    position,
                    MAX_OPTIONAL,
                    visitor::visitOptionalEnd);
        }
    },
    RESERVED_HASH {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseIllegal(pattern, position);
        }
    },
    RESERVED_BRACE_OPEN {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseIllegal(pattern, position);
        }
    },
    RESERVED_BRACE_CLOSE {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseIllegal(pattern, position);
        }
    },
    ILLEGAL {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseIllegal(pattern, position);
        }
    },
    LITERAL {
        @Override
        int traverse(final String pattern,
                     final int position,
                     final DateTimeFormatterPatternVisitor visitor) {
            return visitor.traverseLiteral(pattern, position);
        }
    };

    abstract int traverse(final String pattern,
                          final int position,
                          final DateTimeFormatterPatternVisitor visitor);

    // factory..........................................................................................................

    /**
     * Returns the {@link DateTimeFormatterPatternComponent} for the given character.
     */
    static DateTimeFormatterPatternComponent ofCharacter(final char c) {
        DateTimeFormatterPatternComponent component;

        switch (c) {
            case 'G':
                component = ERA;
                break;
            case 'u':
                component = YEAR;
                break;
            case 'y':
                component = YEAR_OF_ERA;
                break;
            case 'D':
                component = DAY_OF_YEAR;
                break;
            case 'M':
                component = MONTH_OF_YEAR;
                break;
            case 'L':
                component = MONTH_OF_YEAR_STANDALONE;
                break;
            case 'd':
                component = DAY_OF_MONTH;
                break;
            case 'Q':
                component = QUARTER_OF_YEAR;
                break;
            case 'q':
                component = STANDALONE_QUARTER_OF_YEAR;
                break;
            case 'Y':
                component = WEEK_BASED_YEAR;
                break;
            case 'w':
                component = WEEK_OF_WEEK_BASED_YEAR;
                break;
            case 'W':
                component = WEEK_OF_MONTH;
                break;
            case 'E':
                component = DAY_OF_WEEK;
                break;
            case 'e':
                component = LOCALIZED_DAY_OF_WEEK;
                break;
            case 'c':
                component = STANDALONE_LOCALIZED_DAY_OF_WEEK;
                break;
            case 'F':
                component = WEEK_OF_MONTH_F;
                break;
            case 'a':
                component = AMPM_OF_DAY;
                break;
            case 'h':
                component = CLOCK_HOUR_OF_AMPM12;
                break;
            case 'K':
                component = HOUR_OF_AMPM11;
                break;
            case 'k':
                component = CLOCK_HOUR_OF_AMPM24;
                break;
            case 'H':
                component = HOUR_OF_DAY;
                break;
            case 'm':
                component = MINUTE_OF_HOUR;
                break;
            case 's':
                component = SECOND_OF_MINUTE;
                break;
            case 'S':
                component = FRACTION_OF_SECOND;
                break;
            case 'A':
                component = MILLI_OF_DAY;
                break;
            case 'n':
                component = NANO_OF_SECOND;
                break;
            case 'N':
                component = NANO_OF_DAY;
                break;
            case 'V':
                component = TIMEZONE_ID;
                break;
            case 'z':
                component = TIMEZONE_NAME;
                break;
            case 'O':
                component = LOCALIZED_ZONE_OFFSET;
                break;
            case 'X':
                component = ZONE_OFFSET_BIGX;
                break;
            case 'x':
                component = ZONE_OFFSET_SMALLX;
                break;
            case 'Z':
                component = ZONE_OFFSET_Z;
                break;
            case 'p':
                component = PAD;
                break;
            case '\'':
                component = ESCAPE;
                break;
            case '[':
                component = OPTIONAL_START;
                break;
            case ']':
                component = OPTIONAL_END;
                break;
            case '#':
                component = RESERVED_HASH;
                break;
            case '{':
                component = RESERVED_BRACE_OPEN;
                break;
            case '}':
                component = RESERVED_BRACE_CLOSE;
                break;
            case 'B':
            case 'C':
            case 'I':
            case 'J':
            case 'P':
            case 'R':
            case 'T':
            case 'U':
            case 'b':
            case 'f':
            case 'g':
            case 'i':
            case 'j':
            case 'l':
            case 'o':
            case 'r':
            case 't':
            case 'v':
                component = ILLEGAL;
                break;
            default:
                component = LITERAL;
                break;
        }

        return component;
    }

    // MAX are inclusive, anything above is illegal.

    final static int MAX_NARROW = 5;

    /**
     * Up to two letters of 'd', 'H', 'h', 'K', 'k', 'm', and 's' can be specified
     */
    final static int MAX_DAY_HOUR_MINUTE_SEC = 2;

    final static int MAX_FRACTION_OF_SECOND = 9;

    final static int MAX_WEEK_OF_MONTH = 1;

    // 52 weeks in a year...
    final static int MAX_WEEK_OF_WEEK_BASED_YEAR = 2;

    final static int MAX_YEAR = Integer.MAX_VALUE;

    final static int MAX_OPTIONAL = Integer.MAX_VALUE;

    // 1 is ok
    final static int MAX_AMPM = 1;

    final static int MAX_DAY_OF_YEAR = 3; // Up to three letters of 'D' can be specified.

    /**
     * <pre>
     * Zone names: This outputs the display name of the time-zone ID. If the count of letters is one, two or three,
     * then the short name is output. If the count of letters is four, then the full name is output.
     * Five or more letters throws IllegalArgumentException.
     * </pre>
     */
    final static int MAX_TIMEZONE_NAME = 4;
}
