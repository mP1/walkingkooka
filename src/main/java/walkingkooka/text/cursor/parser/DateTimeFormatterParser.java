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

import walkingkooka.Cast;
import walkingkooka.NeverError;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} that turns text from the cursor into a token using a {@link DateTimeFormatter}.
 * Because {@link DateTimeFormatter} only accepts {@link CharSequence} a preliminary phase attempts to consume enough text
 * using the pattern. This is necessary otherwise it would be impossible to determine the characters within the
 * {@link TextCursor} that may be passed to {@link DateTimeFormatter#parse(CharSequence)}.
 * <br>
 * The pattern that created the {@link DateTimeFormatter} must be given to the factory so the preliminary phase can
 * try its simple parsing.
 */
abstract class DateTimeFormatterParser<C extends ParserContext> extends Parser2<C> {

    // values between 0 -> Character.MAX are literal required characters. values beginning with TEXT are character classes.
    final static int TEXT = 100000;
    final static int DIGITS = TEXT + 1;
    final static int FRACTION = DIGITS + 1;
    final static int TEXT_DIGITS = FRACTION + 1;
    final static int WHITESPACE = TEXT_DIGITS + 1;
    final static int ZONEID = WHITESPACE + 1;
    final static int ZONENAME = ZONEID + 1;
    final static int LOCALISED_ZONE_OFFSET = ZONENAME + 1;
    final static int ZONE_OFFSET = LOCALISED_ZONE_OFFSET + 1;
    final static int Z_OR_ZONE_OFFSET = ZONE_OFFSET + 1;

    final static char QUOTE = '\'';

    /**
     * Package private to limit subclassing.
     */
    DateTimeFormatterParser(final DateTimeFormatter formatter, final String pattern) {
        Objects.requireNonNull(formatter, "formatter");
        Whitespace.failIfNullOrEmptyOrWhitespace(pattern, "pattern");

        this.formatter = formatter;
        this.pattern = pattern;

        final DateTimeFormatterParserPatternBuilder patterns = new DateTimeFormatterParserPatternBuilder();
        char previous = 0;
        boolean insideQuotes = false;

        for (char c : pattern.toCharArray()) {
            if (insideQuotes) {
                if (QUOTE == c) {
                    // escaped quote
                    if (QUOTE == previous) {
                        patterns.constant(c);
                        previous = 0;
                        continue;
                    }
                    // finish inside quotes...
                    previous = 0;
                    insideQuotes = false;
                    continue;
                }

                patterns.constant(c);
                previous = c;
                continue;
            }

            switch (c) {
                case 'G': // era
                    this.date(c, pattern);
                    patterns.text();
                    break;
                case 'u': // year
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'y': // year of era year
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'D': // day of year number
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'M':
                case 'L': // month-of-year number/text
                    this.date(c, pattern);
                    patterns.textOrDigits();
                    break;
                case 'd': // day-of-month   number
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'Q': //quarter-of-year' number/text
                case 'q':
                    this.date(c, pattern);
                    patterns.textOrDigits();
                    break;
                case 'Y': // week-based-year
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'w': //week-of-week-based-year     number
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'W': // W       week-of-month               number
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'E': // E       day-of-week                 text
                    this.date(c, pattern);
                    patterns.text();
                    break;
                case 'e': // e/c     localized day-of-week       number/text
                    this.date(c, pattern);
                    patterns.textOrDigits();
                    break;
                case 'F': // F       week-of-month               number
                    this.date(c, pattern);
                    patterns.digits();
                    break;
                case 'a': // a       am-pm-of-day                text
                    this.time(c, pattern);
                    patterns.text();
                    break;
                case 'h': // h       clock-hour-of-am-pm (1-12)  number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'K': // K       hour-of-am-pm (0-11)        number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'k': // k       clock-hour-of-am-pm (1-24)  number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'H': // H       hour-of-day (0-23)          number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'm': // m       minute-of-hour              number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 's': // s       second-of-minute            number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'S': // S       fraction-of-second          fraction
                    this.time(c, pattern);
                    patterns.fraction();
                    break;
                case 'A': // A       milli-of-day                number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'n': // n       nano-of-second              number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'N': // N       nano-of-day                 number
                    this.time(c, pattern);
                    patterns.digits();
                    break;
                case 'V': // V       time-zone ID                zone-id
                    this.zoneId(c, pattern);
                    patterns.zoneId();
                    break;
                case 'z': // z       time-zone name              zone-name
                    this.zoneName(c, pattern);
                    patterns.zoneName();
                    break;
                case 'O': // O       localized zone-offset       offset-O
                    this.localisedZoneOffset(c, pattern);
                    patterns.localisedZoneOffset();
                    break;
                case 'X': // X       zone-offset 'Z' for zero    offset-X
                    this.zOrZoneOffset(c, pattern);
                    patterns.zoneOffset();
                    break;
                case 'x': // x       zone-offset                 offset-x
                    this.zOrZoneOffset(c, pattern);
                    patterns.zOrZoneOffset();
                    break;
                case 'Z': // Z       zone-offset                 offset-Z
                    this.zoneOffset(c, pattern);
                    patterns.zoneOffset();
                    break;
                case 'p': // p       pad next
                    patterns.whitespace();
                    break;
                case QUOTE: // '       escape for text             delimiter
                    insideQuotes = true;
                    break;
                case '[': // [       optional section start
                case ']': // ]       optional section end
                case '#': // #       reserved for future use
                case '{': // {       reserved for future use
                case '}': // }       reserved for future use
                    throw new IllegalArgumentException("DateTimeFormatter contains invalid char " + CharSequences.quoteIfChars(c) + " =" + formatter);
                default:
                    patterns.constant(c);
                    break;
            }
        }

        this.patterns = patterns.build();
    }

    abstract void date(final char c, final String pattern);

    abstract void time(final char c, final String pattern);

    abstract void zoneId(final char c, final String pattern);

    abstract void zoneName(final char c, final String pattern);

    abstract void localisedZoneOffset(final char c, final String pattern);

    abstract void zoneOffset(final char c, final String pattern);

    abstract void zOrZoneOffset(final char c, final String pattern);

    /**
     * Sub classes will use this to report invalid patterns, eg a pattern with a time for a date only parser.
     */
    final void failInvalidPattern(final char c, final String pattern) {
        throw new IllegalArgumentException("Pattern should not contain " + CharSequences.quoteIfChars(c) + "=" + CharSequences.quote(pattern));
    }

    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        try {
            Optional<ParserToken> result;

            int patternIndex = 0;
            int pattern = this.patterns[patternIndex];
            int textLength = 0;

            for (; ; ) {
                if (cursor.isEmpty()) {
                    result = this.createParseTokenOrFail(patternIndex, textLength, cursor, start);
                    break;
                }
                final char c = cursor.at();
                if (isMatch(c, pattern)) {
                    cursor.next();
                    textLength++;
                    continue;
                }
                patternIndex++;
                if (this.patterns.length == patternIndex) {
                    result = this.createParseTokenOrFail(patternIndex, textLength, cursor, start);
                    break;
                }
                pattern = this.patterns[patternIndex];
                if (isMatch(c, pattern)) {
                    cursor.next();
                    textLength++;
                    continue;
                }

                result = this.createParseTokenOrFail(patternIndex, textLength, cursor, start);
                break;
            }

            return result;
        } catch (final DateTimeParseException cause) {
            throw new ParserException(cause.getMessage(), cause);
        }
    }

    private boolean isMatch(final char c, final int pattern) {
        boolean match;

        switch (pattern) {
            case TEXT:
                match = isText(c);
                break;
            case DIGITS:
                match = isDigit(c);
                break;
            case FRACTION:
                match = isFraction(c);
                break;
            case TEXT_DIGITS:
                NeverError.unhandledCase(pattern, TEXT, DIGITS, FRACTION, WHITESPACE, ZONEID, ZONENAME, LOCALISED_ZONE_OFFSET, ZONE_OFFSET, Z_OR_ZONE_OFFSET);
            case WHITESPACE:
                match = isWhitespace(c);
                break;
            case ZONEID:
                match = isZoneId(c);
                break;
            case ZONENAME:
                match = isZoneName(c);
                break;
            case LOCALISED_ZONE_OFFSET:
                match = isLocalisedZoneOffset(c);
                break;
            case ZONE_OFFSET:
                match = isZoneOffset(c);
                break;
            case Z_OR_ZONE_OFFSET:
                match = isZOrZoneOffset(c);
                break;
            default:
                match = pattern == c;
                break;
        }
        return match;
    }

    private static boolean isText(final char c) {
        return Character.isLetter(c);
    }

    private static boolean isDigit(final char c) {
        return Character.isDigit(c);
    }

    private boolean isFraction(final char c) {
        return isDigit(c) || this.formatter.getDecimalStyle().getDecimalSeparator() == c;
    }

    private static boolean isWhitespace(final char c) {
        return Character.isWhitespace(c);
    }

    private boolean isZoneId(final char c) {
        return isZoneId(c, this.positive(), this.negative());
    }

    // @VisibleForTesting
    static boolean isZoneId(final char c, final char positive, final char negative) {
        return isText(c) || '/' == c || ':' == c || '_' == c || isZoneOffset(c, positive, negative);
    }

    private boolean isZoneName(final char c) {
        return isZoneName(c, this.positive(), this.negative());
    }

    // @VisibleForTesting
    static boolean isZoneName(final char c, final char positive, final char negative) {
        return isZoneId(c, positive, negative) || isWhitespace(c);
    }

    private boolean isLocalisedZoneOffset(final char c) {
        return isText(c) || isDigit(c) || c == ':' || this.positive() == c || this.negative() == c;
    }

    private boolean isZOrZoneOffset(final char c) {
        return 'Z' == c || this.isZoneOffset(c);
    }

    private boolean isZoneOffset(final char c) {
        return isZoneOffset(c, this.positive(), this.negative());
    }

    private static boolean isZoneOffset(final char c, final char positive, final char negative) {
        return isDigit(c) || c == ':' || positive == c || negative == c;
    }

    private char positive() {
        return this.formatter.getDecimalStyle().getPositiveSign();
    }

    private char negative() {
        return this.formatter.getDecimalStyle().getNegativeSign();
    }

    private final int[] patterns;

    /**
     * Tests if the cursor has advanced over a string of characters which consumes the entire pattern. If so
     * {@link DateTimeFormatter#parse(CharSequence)} will be called.
     */
    private Optional<ParserToken> createParseTokenOrFail(final int pos, final int textLength, final TextCursor cursor, final TextCursorSavePoint save) {
        // need to see if remaining nodes could have been consumed... eg SSS could follow previous ss.
        boolean create = true;
        if (pos < this.patterns.length) {
            int pattern = this.patterns[pos];
            int i = pos + 1;

            while (create && i < this.patterns.length) {
                final int next = this.patterns[i];
                switch (next) {
                    case TEXT:
                        create = TEXT_DIGITS == pattern;
                        break;
                    case DIGITS:
                        create = TEXT_DIGITS == pattern;
                        break;
                    case FRACTION:
                        create = TEXT_DIGITS == pattern || DIGITS == pattern;
                        break;
                    case TEXT_DIGITS:
                        NeverError.unhandledCase(next, "TEXT_DIGITS");
                    case WHITESPACE:
                        create = false;
                        break;
                    case ZONEID:
                        create = false;
                        break;
                    case ZONENAME:
                        create = false;
                        break;
                    case LOCALISED_ZONE_OFFSET:
                        create = false;
                        break;
                    case ZONE_OFFSET:
                        create = false;
                        break;
                    case Z_OR_ZONE_OFFSET:
                        create = false;
                        break;
                    default:
                        create = pattern == next;
                        break;
                }
                i++;
                pattern = next;
            }
        }

        // finished matching last char create token...
        return pos > 1 || create ?
                this.createParserToken(textLength, cursor, save) :
                Optional.empty();
    }

    private Optional<ParserToken> createParserToken(final int textLength, final TextCursor cursor, final TextCursorSavePoint save) {
        save.restore();
        final TemporalAccessor parsed = this.formatter.parse(new DateTimeFormatterParserTextCursorCharSequence(cursor, textLength));
        return Optional.of(this.createParserToken(parsed,
                save.textBetween().toString()));
    }

    /**
     * Factory that creates a {@link ParserToken} with the date or time or date time value.
     */
    abstract ParserToken createParserToken(final TemporalAccessor temporalAccessor, final String text);

    // Object.............................................................................................................

    @Override
    public final int hashCode() {
        return this.pattern.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    /**
     * Sub classes should perform an instanceof check of the parameter.
     */
    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final DateTimeFormatterParser<?> other) {
        return this.formatter.equals(other.formatter) &&
                this.pattern.equals(other.pattern);
    }

    @Override
    public String toString() {
        return this.pattern;
    }

    private final DateTimeFormatter formatter;
    private final String pattern;
}
