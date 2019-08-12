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

package walkingkooka.convert;

import walkingkooka.datetime.DateTimeContext;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Converter} which uses a {@link DateTimeFormatter}.
 */
abstract class DateTimeFormatterConverter<S, T> extends FixedSourceTypeTargetTypeConverter<S, T> {

    /**
     * Package private to limit sub classing.
     */
    DateTimeFormatterConverter(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        Objects.requireNonNull(formatter, "formatter");
        this.formatter = formatter;
    }

    /**
     * A factory which returns a {@link DateTimeFormatter} on demand using the {@link DateTimeContext}.
     */
    final Function<DateTimeContext, DateTimeFormatter> formatter;

    /**
     * Uses the {@link Locale} and {@link ConverterContext#twoDigitYear()} creating a {@link DateTimeFormatter}
     * if necessary and then calls {@link #parseOrFormat(Object, DateTimeFormatter)} wrapping any thrown {@link DateTimeException}
     */
    @Override
    final T convert1(final S value, final ConverterContext context) {
        final Locale locale = context.locale();
        final int twoDigitYear = context.twoDigitYear();

        DateTimeFormatterConverterCache cache = this.cache;
        DateTimeFormatter dateTimeFormatter;
        if (null == cache) {
            dateTimeFormatter = this.formatter.apply(context);
            this.cache = DateTimeFormatterConverterCache.with(locale,
                    twoDigitYear,
                    dateTimeFormatter);
        } else {
            if (false == cache.locale.equals(locale) || cache.twoDigitYear != twoDigitYear) {
                dateTimeFormatter = this.formatter.apply(context)
                        .withDecimalStyle(DecimalStyle.of(locale)
                                .withPositiveSign(context.positiveSign())
                                .withNegativeSign(context.negativeSign())
                                .withDecimalSeparator(context.decimalSeparator()));
                cache = DateTimeFormatterConverterCache.with(locale,
                        twoDigitYear,
                        dateTimeFormatter);
                this.cache = cache;
            }

            dateTimeFormatter = cache.formatter;
        }

        try {
            return this.parseOrFormat(value, dateTimeFormatter);
        } catch (final Exception cause) {
            return this.failConversion(value, cause);
        }
    }

    private transient DateTimeFormatterConverterCache cache;

    /**
     * Sub classes should parse or format the value using the {@link DateTimeContext} aware {@link DateTimeFormatter}.
     */
    abstract T parseOrFormat(final S value,
                             final DateTimeFormatter formatter) throws IllegalArgumentException, DateTimeException;

    @Override
    final String toStringSuffix() {
        return "";
    }
}
