/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.convert;

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.test.HashCodeEqualsDefined;

import java.text.DecimalFormatSymbols;
import java.util.Objects;

/**
 * Used as a key to {@link java.text.DecimalFormat}.
 */
final class DecimalFormatStringConverterSymbols implements HashCodeEqualsDefined {

    /**
     * Factory only called by {@link DecimalFormatStringConverter}
     */
    static DecimalFormatStringConverterSymbols with(final char decimalPoint,
                                                    final char exponentSymbol,
                                                    final char minusSign,
                                                    final char plusSign) {
        return new DecimalFormatStringConverterSymbols(decimalPoint, exponentSymbol, minusSign, plusSign);
    }

    private DecimalFormatStringConverterSymbols(final char decimalPoint,
                                                final char exponentSymbol,
                                                final char minusSign,
                                                final char plusSign) {
        super();
        this.decimalPoint = decimalPoint;
        this.exponentSymbol = exponentSymbol;
        this.minusSign = minusSign;
        this.plusSign = plusSign;
    }

    DecimalFormatSymbols decimalFormatSymbols() {
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        symbols.setDecimalSeparator(this.decimalPoint);
        symbols.setExponentSeparator(String.valueOf(this.exponentSymbol));
        symbols.setMinusSign(this.minusSign);

        return symbols;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.decimalPoint, this.exponentSymbol, this.minusSign, this.plusSign);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
               other instanceof DecimalFormatStringConverterSymbols &&
               this.equals0(Cast.to(other));
    }

    private boolean equals0(final DecimalFormatStringConverterSymbols other) {
        return this.decimalPoint == other.decimalPoint &&
               this.exponentSymbol == other.exponentSymbol &&
               this.minusSign == other.minusSign &&
               this.plusSign == other.plusSign;
    }

    private final char decimalPoint;

    private final char exponentSymbol;

    private final char minusSign;

    private final char plusSign;

    @Override
    public String toString() {
        return ToStringBuilder.create()
                .value(this.decimalPoint)
                .value(this.exponentSymbol)
                .value(this.minusSign)
                .value(this.plusSign)
                .build();
    }
}
