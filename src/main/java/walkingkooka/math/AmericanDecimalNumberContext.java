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

package walkingkooka.math;

import walkingkooka.build.tostring.ToStringBuilder;

/**
 * A {@link DecimalNumberContext} with all values set to what can broadly be described as american.
 * This is useful for as many internet standards also use the same symbols.
 */
final class AmericanDecimalNumberContext implements DecimalNumberContext {

    /**
     * Singleton
     */
    final static AmericanDecimalNumberContext INSTANCE = new AmericanDecimalNumberContext();

    /**
     * Private ctor use singleton.
     */
    private AmericanDecimalNumberContext() {
        super();
    }

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
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.currencySymbol())
                .value(this.decimalPoint())
                .value(this.exponentSymbol())
                .value(this.groupingSeparator())
                .value(this.minusSign())
                .value(this.percentageSymbol())
                .value(this.plusSign())
                .build();
    }
}
