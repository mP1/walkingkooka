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
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link Parser} that turns text from the cursor into a token using a {@link DateTimeFormatter}.
 * Because {@link DateTimeFormatter} only accepts {@link CharSequence} a preliminary phase attempts to consume enough text
 * using the pattern. This is necessary otherwise it would be impossible to determine the characters within the
 * {@link TextCursor} that may be passed to {@link DateTimeFormatter#parse(CharSequence)}.
 * <br>
 * The pattern that created the {@link DateTimeFormatter} must be given to the factory so the preliminary phase can
 * try its simple parsing.
 */
abstract class DateTimeFormatterParser<C extends ParserContext> extends Parser2<C> implements HashCodeEqualsDefined {

    /**
     * Package private to limit subclassing.
     */
    DateTimeFormatterParser(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        Objects.requireNonNull(formatter, "formatter");

        this.formatter = formatter;
    }

    final static int INITIAL_LENGTH_GUESS = 20;

    @Override
    Optional<ParserToken> tryParse0(final TextCursor cursor,
                                    final C context,
                                    final TextCursorSavePoint save) {
        ParserToken token;

        final Locale locale = context.locale();
        final int twoDigitYear = context.twoDigitYear();

        DateTimeFormatterParserCache cache = this.cache;
        DateTimeFormatter dateTimeFormatter;
        if (null == cache) {
            dateTimeFormatter = this.formatter.apply(context);
            this.cache = DateTimeFormatterParserCache.with(locale,
                    twoDigitYear,
                    dateTimeFormatter);
        } else {
            if (false == cache.locale.equals(locale) || cache.twoDigitYear != twoDigitYear) {
                dateTimeFormatter = this.formatter.apply(context);
                cache = DateTimeFormatterParserCache.with(locale,
                        twoDigitYear,
                        dateTimeFormatter);
                this.cache = cache;
            }

            dateTimeFormatter = cache.formatter;
        }

        final ParsePosition position = new ParsePosition(0);
        final StringBuilder chars = new StringBuilder();
        long fill = INITIAL_LENGTH_GUESS;

        for (; ; ) {
            position.setErrorIndex(-1);
            position.setIndex(0);

            while (fill >= 0 && !cursor.isEmpty()) {
                chars.append(cursor.at());
                cursor.next();
                fill--;
            }
            fill = 15;

            try {
                final TemporalAccessor parsed = dateTimeFormatter.parse(chars, position);
                int read = position.getIndex();
                token = this.createParserToken(parsed, chars.substring(0, read));

                save.restore();

                while (read > 0 && !cursor.isEmpty()) {
                    cursor.next();
                    read--;
                }
                break;
            } catch (final DateTimeParseException parse) {
                final Throwable cause = parse.getCause();
                if(cause instanceof DateTimeException) {
                    throw new ParserException(cause.getMessage(), cause);
                }
                if (cursor.isEmpty()) {
                    token = null;
                    save.restore();
                    break;
                }

                // try again!
            } catch (final DateTimeException invalid) {
                // must be reporting an invalid component within a date/time.
                throw new ParserException(invalid.getMessage(), invalid);
            } catch (final RuntimeException failed) {
                if (cursor.isEmpty()) {
                    token = null;
                    save.restore();
                    break;
                }
            }
        }

        return Optional.ofNullable(token);
    }

    final Function<DateTimeContext, DateTimeFormatter> formatter;

    DateTimeFormatterParserCache cache;

    /**
     * Factory that creates a {@link ParserToken} with the date or time or date time value.
     */
    abstract ParserToken createParserToken(final TemporalAccessor temporalAccessor,
                                           final String text);

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.formatter.toString();
    }
}
