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
 * A {@link DecimalNumberContext} with all values set to what can broadly be described as american.
 * This is useful for as many internet standards also use the same symbols.
 */
final class AmericanDecimalNumberContext implements DecimalNumberContext {

    /**
     * Factory that returns a constant if a {@link MathContext} constant is given.
     */
    final static AmericanDecimalNumberContext with(final Locale locale,
                                                   final MathContext mathContext) {
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(mathContext, "mathContext");

        return new AmericanDecimalNumberContext(locale, mathContext);
    }

    /**
     * Private ctor use singleton.
     */
    private AmericanDecimalNumberContext(final Locale locale,
                                         final MathContext mathContext) {
        super();
        this.locale = locale;
        this.mathContext = mathContext;
    }

    // DecimalNumberContext.............................................................................................

    @Override
    public String currencySymbol() {
        return "$";
    }

    @Override
    public char decimalPoint() {
        return '.';
    }

    @Override
    public char exponentSymbol() {
        return 'E';
    }

    @Override
    public char groupingSeparator() {
        return ',';
    }

    @Override
    public char minusSign() {
        return '-';
    }

    @Override
    public char percentageSymbol() {
        return '%';
    }

    @Override
    public char plusSign() {
        return '+';
    }

    @Override
    public MathContext mathContext() {
        return this.mathContext;
    }

    private final MathContext mathContext;

    // Locale........................................................................................................

    @Override
    public Locale locale() {
        return this.locale;
    }

    private final Locale locale;

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
                .value(this.locale)
                .value(this.mathContext)
                .build();
    }
}
