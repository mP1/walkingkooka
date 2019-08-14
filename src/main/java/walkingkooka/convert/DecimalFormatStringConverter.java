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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Uses a {@link String pattern} to create a {@link DecimalFormat}. Individual {@link DecimalFormat} are used for each thread.
 */
final class DecimalFormatStringConverter extends FixedTargetTypeConverter<String> {

    /**
     * Creates a new {@link DecimalFormatStringConverter}.
     */
    static DecimalFormatStringConverter with(final String pattern) {
        Objects.requireNonNull(pattern, "pattern");

        new DecimalFormat(pattern);
        return new DecimalFormatStringConverter(pattern);
    }

    /**
     * Private ctor use factory
     */
    private DecimalFormatStringConverter(final String pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    Class<String> targetType() {
        return String.class;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type, final ConverterContext context) {
        return String.class == type && value instanceof Number;
    }

    @Override
    String convert1(final Object value, final Class<String> type, final ConverterContext context) {
        final ThreadLocal<Map<DecimalFormatStringConverterSymbols, DecimalFormat>> symbolToDecimalFormat = this.symbolToDecimalFormat;

        Map<DecimalFormatStringConverterSymbols, DecimalFormat> map = symbolToDecimalFormat.get();
        if (null == map) {
            map = new HashMap<>();
            symbolToDecimalFormat.set(map);
        }

        final DecimalFormatStringConverterSymbols symbols = DecimalFormatStringConverterSymbols.with(
                context.currencySymbol(),
                context.decimalSeparator(),
                context.exponentSymbol(),
                context.groupingSeparator(),
                context.negativeSign(),
                context.percentageSymbol(),
                context.positiveSign());
        DecimalFormat format = map.get(symbols);
        if (null == format) {
            format = new DecimalFormat(this.pattern, symbols.decimalFormatSymbols());
            map.put(symbols, format);
        }

        return format.format(value);
    }

    private final ThreadLocal<Map<DecimalFormatStringConverterSymbols, DecimalFormat>> symbolToDecimalFormat = new ThreadLocal<>();

    private final String pattern;

    @Override
    public String toString() {
        return this.pattern;
    }
}
