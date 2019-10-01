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
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.text.CharSequences;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Base class for a {@link Converter} that parses or formats using a {@link DecimalFormat}.
 */
abstract class DecimalFormatConverter extends Converter2 {

    static void check(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        Objects.requireNonNull(decimalFormat, "decimalFormat");
    }

    /**
     * Private ctor use factory
     */
    DecimalFormatConverter(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        super();
        this.decimalFormat = decimalFormat;
    }

    @Override
    final <T> Either<T, String> convert0(final Object value,
                                         final Class<T> type,
                                         final ConverterContext context) {
        Either<T, String> result;
        try {
            final ThreadLocal<Map<ConverterContext, DecimalFormat>> cache = this.cache;

            Map<ConverterContext, DecimalFormat> map = cache.get();
            if (null == map) {
                map = new HashMap<>();
                this.cache.set(map);
            }

            DecimalFormat format = map.get(context);
            if (null == format) {
                format = this.decimalFormat.apply(context);
                format.setParseBigDecimal(true);

                final Locale locale = context.locale();
                try {
                    format.setCurrency(Currency.getInstance(locale));
                } catch (final IllegalArgumentException cause) {
                    throw new ConversionException("Unable to set currency, probably an invalid locale " + CharSequences.quoteAndEscape(locale.toLanguageTag()));
                }

                final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                symbols.setCurrencySymbol(context.currencySymbol());
                symbols.setDecimalSeparator(context.decimalSeparator());
                symbols.setExponentSeparator(String.valueOf(context.exponentSymbol()));
                symbols.setGroupingSeparator(context.groupingSeparator());
                symbols.setMinusSign(context.negativeSign());
                symbols.setPercent(context.percentageSymbol());

                format.setDecimalFormatSymbols(symbols);

                map.put(context, format);
            }

            result = this.convertWithDecimalFormat(format,
                    value,
                    type,
                    context);
        } catch (final RuntimeException cause) {
            result = this.failConversion(value, type, cause);
        }
        return result;
    }

    private final ThreadLocal<Map<ConverterContext, DecimalFormat>> cache = new ThreadLocal<>();

    private final Function<DecimalNumberContext, DecimalFormat> decimalFormat;

    abstract <T> Either<T, String> convertWithDecimalFormat(final DecimalFormat decimalFormat,
                                                            final Object value,
                                                            final Class<T> type,
                                                            final ConverterContext context);

    @Override
    public final String toString() {
        return this.decimalFormat.toString();
    }
}
