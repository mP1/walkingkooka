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
 * This is useful for as many internet standards also use the same symbols. Note the {@link #locale()} throws
 * {@link UnsupportedOperationException}.
 */
final class AmericanDecimalNumberContext implements DecimalNumberContext {

    /**
     * Factory that returns a constant if a {@link MathContext} constant is given.
     */
    static AmericanDecimalNumberContext with(final MathContext mathContext) {
        Objects.requireNonNull(mathContext, "mathContext");

        return UNLIMITED.mathContext.equals(mathContext) ?
                UNLIMITED :
                DECIMAL32.mathContext.equals(mathContext) ?
                        DECIMAL32 :
                        DECIMAL64.mathContext.equals(mathContext) ?
                                DECIMAL64 :
                                DECIMAL128.mathContext.equals(mathContext) ?
                                        DECIMAL128 :
                                        new AmericanDecimalNumberContext(mathContext);
    }

    private final static AmericanDecimalNumberContext UNLIMITED = new AmericanDecimalNumberContext(MathContext.UNLIMITED);
    private final static AmericanDecimalNumberContext DECIMAL32 = new AmericanDecimalNumberContext(MathContext.DECIMAL32);
    private final static AmericanDecimalNumberContext DECIMAL64 = new AmericanDecimalNumberContext(MathContext.DECIMAL64);
    private final static AmericanDecimalNumberContext DECIMAL128 = new AmericanDecimalNumberContext(MathContext.DECIMAL128);

    /**
     * Private ctor use singleton.
     */
    private AmericanDecimalNumberContext(final MathContext mathContext) {
        super();
        this.mathContext = mathContext;
    }

    @Override
    public String currencySymbol() {
        return "$";
    }

    @Override
    public char decimalSeparator() {
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
    public char negativeSign() {
        return '-';
    }

    @Override
    public char percentageSymbol() {
        return '%';
    }

    @Override
    public char positiveSign() {
        return '+';
    }

    @Override
    public Locale locale() {
        return Locale.US;
    }

    @Override
    public MathContext mathContext() {
        return this.mathContext;
    }

    private final MathContext mathContext;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.currencySymbol())
                .value(this.decimalSeparator())
                .value(this.exponentSymbol())
                .value(this.groupingSeparator())
                .value(this.negativeSign())
                .value(this.percentageSymbol())
                .value(this.positiveSign())
                .value(this.mathContext())
                .build();
    }
}
