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

import walkingkooka.Either;
import walkingkooka.datetime.DateTimeContext;

import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Converter} which uses a {@link DateTimeFormatter} in some part of the conversion process.
 */
abstract class DateTimeFormatterConverter<S, D> extends Converter2 {

    /**
     * Package private to limit sub classing.
     */
    DateTimeFormatterConverter(final Function<DateTimeContext, DateTimeFormatter> formatter) {
        Objects.requireNonNull(formatter, "formatter");
        this.formatter = formatter;
    }

    @Override
    public final boolean canConvert(final Object value,
                                    final Class<?> type,
                                    final ConverterContext context) {
        return this.sourceType().isInstance(value) &&
                this.targetType() == type;
    }

    abstract Class<S> sourceType();

    /**
     * Wraps the {@link #convert(Object, Class, ConverterContext)} in a try/catch any exceptions will become a failure
     * using the {@link Throwable#getMessage()} as the failure message.
     */
    @Override
    final <T> Either<T, String> convert0(final Object value,
                                         final Class<T> type,
                                         final ConverterContext context) {
        Either<T, String> result;
        try {
            result = Either.left(type.cast(this.convert1(this.sourceType().cast(value), context)));
        } catch (final IllegalArgumentException | DateTimeException cause) {
            result = this.failConversion(value, type, cause);
        }
        return result;
    }

    /**
     * Uses the {@link Locale} and {@link ConverterContext#twoDigitYear()} creating a {@link DateTimeFormatter}
     * if necessary and then calls {@link #parseOrFormat(Object, DateTimeFormatter)} wrapping any thrown {@link DateTimeException}
     */
    private D convert1(final S value,
                       final ConverterContext context) {
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

        return this.parseOrFormat(value, dateTimeFormatter);
    }

    /**
     * A factory which returns a {@link DateTimeFormatter} on demand using the {@link DateTimeContext}.
     */
    final Function<DateTimeContext, DateTimeFormatter> formatter;

    private transient DateTimeFormatterConverterCache cache;

    /**
     * Sub classes should parse or format the value using the {@link DateTimeContext} aware {@link DateTimeFormatter}.
     */
    abstract D parseOrFormat(final S value,
                             final DateTimeFormatter formatter) throws IllegalArgumentException, DateTimeException;

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.sourceType().getSimpleName() + "->" + this.targetType().getSimpleName();
    }

    /**
     * The {@link Class target type}, all sub classes produce a single target type.
     */
    abstract Class<D> targetType();
}
