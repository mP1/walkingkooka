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

package walkingkooka.math;

import walkingkooka.ToStringBuilder;
import walkingkooka.text.CharSequences;

import java.math.MathContext;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link DecimalNumberContext} that sources most values from a {@link DecimalFormatSymbols}
 */
final class DecimalFormatSymbolsDecimalNumberContext implements DecimalNumberContext {

    static DecimalFormatSymbolsDecimalNumberContext with(final DecimalFormatSymbols symbols,
                                                         final char exponentSymbol,
                                                         final char plusSign,
                                                         final Locale locale,
                                                         final MathContext mathContext) {
        Objects.requireNonNull(symbols, "symbols");
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(mathContext, "mathContext");
        if (exponentSymbol == plusSign) {
            throw new IllegalArgumentException("Exponent symbol == plus sign " + CharSequences.quoteIfChars(exponentSymbol));
        }

        return new DecimalFormatSymbolsDecimalNumberContext(symbols,
                exponentSymbol,
                plusSign,
                locale,
                mathContext);
    }

    private DecimalFormatSymbolsDecimalNumberContext(final DecimalFormatSymbols symbols,
                                                     final char exponentSymbol,
                                                     final char plusSign,
                                                     final Locale locale,
                                                     final MathContext mathContext) {
        super();
        this.symbols = symbols;
        this.exponentSymbol = exponentSymbol;
        this.plusSign = plusSign;
        this.locale = locale;
        this.mathContext = mathContext;
    }

    @Override
    public String currencySymbol() {
        return this.symbols.getCurrencySymbol();
    }

    @Override
    public char decimalPoint() {
        return this.symbols.getDecimalSeparator();
    }

    @Override
    public char exponentSymbol() {
        return this.exponentSymbol;
    }

    private final char exponentSymbol;

    @Override
    public char groupingSeparator() {
        return this.symbols.getGroupingSeparator();
    }

    @Override
    public char percentageSymbol() {
        return this.symbols.getPercent();
    }

    @Override
    public char minusSign() {
        return this.symbols.getMinusSign();
    }

    private final DecimalFormatSymbols symbols;

    @Override
    public char plusSign() {
        return this.plusSign;
    }

    private final char plusSign;

    @Override
    public Locale locale() {
        return this.locale;
    }

    private final Locale locale;

    @Override
    public MathContext mathContext() {
        return this.mathContext;
    }

    private final MathContext mathContext;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.currencySymbol())
                .value(this.decimalPoint())
                .value(this.exponentSymbol())
                .value(this.groupingSeparator())
                .value(this.minusSign())
                .value(this.percentageSymbol())
                .value(this.plusSign())
                .value(this.locale())
                .value(this.mathContext())
                .build();
    }
}
