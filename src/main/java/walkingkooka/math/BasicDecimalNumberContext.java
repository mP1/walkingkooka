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

import java.math.MathContext;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link DecimalNumberContext} that holds constant properties.
 */
final class BasicDecimalNumberContext implements DecimalNumberContext {

    static BasicDecimalNumberContext with(final String currencySymbol,
                                          final char decimalSeparator,
                                          final char exponentSymbol,
                                          final char groupingSeparator,
                                          final char negativeSign,
                                          final char percentageSymbol,
                                          final char positiveSign,
                                          final Locale locale,
                                          final MathContext mathContext) {
        Objects.requireNonNull(currencySymbol, "currencySymbol");
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(mathContext, "mathContext");

        return new BasicDecimalNumberContext(currencySymbol,
                decimalSeparator,
                exponentSymbol,
                groupingSeparator,
                negativeSign,
                percentageSymbol,
                positiveSign,
                locale,
                mathContext);
    }

    private BasicDecimalNumberContext(final String currencySymbol,
                                      final char decimalSeparator,
                                      final char exponentSymbol,
                                      final char groupingSeparator,
                                      final char negativeSign,
                                      final char percentageSymbol,
                                      final char positiveSign,
                                      final Locale locale,
                                      final MathContext mathContext) {
        super();
        this.currencySymbol = currencySymbol;
        this.decimalSeparator = decimalSeparator;
        this.exponentSymbol = exponentSymbol;
        this.groupingSeparator = groupingSeparator;
        this.negativeSign = negativeSign;
        this.percentageSymbol = percentageSymbol;
        this.positiveSign = positiveSign;
        
        this.locale = locale;
        
        this.mathContext = mathContext;
    }

    @Override
    public String currencySymbol() {
        return this.currencySymbol;
    }

    private final String currencySymbol;

    @Override
    public char decimalSeparator() {
        return this.decimalSeparator;
    }

    private final char decimalSeparator;

    @Override
    public char exponentSymbol() {
        return this.exponentSymbol;
    }

    private final char exponentSymbol;

    @Override
    public char groupingSeparator() {
        return this.groupingSeparator;
    }

    private final char groupingSeparator;

    @Override
    public char negativeSign() {
        return this.negativeSign;
    }

    private final char negativeSign;

    @Override
    public char percentageSymbol() {
        return this.percentageSymbol;
    }

    private final char percentageSymbol;

    @Override
    public char positiveSign() {
        return this.positiveSign;
    }

    private final char positiveSign;

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
                .value(this.locale)
                .value(this.mathContext)
                .build();
    }
}
