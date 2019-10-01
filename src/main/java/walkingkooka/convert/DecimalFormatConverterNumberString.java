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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.function.Function;

/**
 * A {@link Converter} that parses {@link String} into {@link Number numbers}.
 */
final class DecimalFormatConverterNumberString extends DecimalFormatConverter {

    static DecimalFormatConverterNumberString with(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        check(decimalFormat);
        return new DecimalFormatConverterNumberString(decimalFormat);
    }

    private DecimalFormatConverterNumberString(final Function<DecimalNumberContext, DecimalFormat> decimalFormat) {
        super(decimalFormat);
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final ConverterContext context) {
        return (value instanceof Byte ||
                value instanceof Short ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Float ||
                value instanceof Double ||
                value instanceof BigDecimal ||
                value instanceof BigInteger) &&
                type == String.class;
    }

    @Override
    <T> Either<T, String> convertWithDecimalFormat(final DecimalFormat decimalFormat,
                                                   final Object value,
                                                   final Class<T> type,
                                                   final ConverterContext context) {
        return Either.left(type.cast(decimalFormat.format(value)));
    }
}
