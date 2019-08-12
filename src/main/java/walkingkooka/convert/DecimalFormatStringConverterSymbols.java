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

import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
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
    static DecimalFormatStringConverterSymbols with(final String currencySymbol,
                                                    final char decimalSeparator,
                                                    final char exponentSymbol,
                                                    final char groupingSeparator,
                                                    final char negativeSign,
                                                    final char percentageSymbol,
                                                    final char positiveSign) {
        return new DecimalFormatStringConverterSymbols(currencySymbol,
                decimalSeparator,
                exponentSymbol,
                groupingSeparator,
                negativeSign,
                percentageSymbol,
                positiveSign);
    }

    private DecimalFormatStringConverterSymbols(final String currencySymbol,
                                                final char decimalSeparator,
                                                final char exponentSymbol,
                                                final char groupingSeparator,
                                                final char negativeSign,
                                                final char percentageSymbol,
                                                final char positiveSign) {
        super();
        this.currencySymbol = currencySymbol;
        this.decimalSeparator = decimalSeparator;
        this.exponentSymbol = exponentSymbol;
        this.groupingSeparator = groupingSeparator;
        this.negativeSign = negativeSign;
        this.percentageSymbol = percentageSymbol;
        this.positiveSign = positiveSign;
    }

    DecimalFormatSymbols decimalFormatSymbols() {
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols();

        symbols.setCurrencySymbol(this.currencySymbol);
        symbols.setDecimalSeparator(this.decimalSeparator);
        symbols.setExponentSeparator(String.valueOf(this.exponentSymbol));
        symbols.setGroupingSeparator(this.groupingSeparator);
        symbols.setMinusSign(this.negativeSign);
        symbols.setPercent(this.percentageSymbol);

        return symbols;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.currencySymbol,
                this.decimalSeparator,
                this.exponentSymbol,
                this.groupingSeparator,
                this.negativeSign,
                this.percentageSymbol,
                this.positiveSign);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof DecimalFormatStringConverterSymbols &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final DecimalFormatStringConverterSymbols other) {
        return this.currencySymbol.equals(other.currencySymbol) &&
                this.decimalSeparator == other.decimalSeparator &&
                this.exponentSymbol == other.exponentSymbol &&
                this.groupingSeparator == other.groupingSeparator &&
                this.negativeSign == other.negativeSign &&
                this.percentageSymbol == other.percentageSymbol &&
                this.positiveSign == other.positiveSign;
    }

    private final String currencySymbol;

    private final char decimalSeparator;

    private final char exponentSymbol;

    private final char groupingSeparator;

    private final char negativeSign;

    private final char percentageSymbol;

    private final char positiveSign;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.currencySymbol)
                .value(this.decimalSeparator)
                .value(this.exponentSymbol)
                .value(this.groupingSeparator)
                .value(this.negativeSign)
                .value(this.percentageSymbol)
                .value(this.positiveSign)
                .build();
    }
}
