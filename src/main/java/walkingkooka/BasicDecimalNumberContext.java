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

package walkingkooka;

import walkingkooka.build.tostring.ToStringBuilder;

/**
 * A {@link DecimalNumberContext} that holds constant properties.
 */
final class BasicDecimalNumberContext implements DecimalNumberContext {

    static BasicDecimalNumberContext with(final char decimalPoint,
                                          final char exponentSymbol,
                                          final char minusSign,
                                          final char plusSign) {
        return new BasicDecimalNumberContext(decimalPoint, exponentSymbol, minusSign, plusSign);
    }

    private BasicDecimalNumberContext(final char decimalPoint,
                                      final char exponentSymbol,
                                      final char minusSign,
                                      final char plusSign) {
        super();
        this.decimalPoint = decimalPoint;
        this.exponentSymbol = exponentSymbol;
        this.minusSign = minusSign;
        this.plusSign = plusSign;
    }

    @Override
    public char decimalPoint() {
        return this.decimalPoint;
    }

    private final char decimalPoint;

    @Override
    public char exponentSymbol() {
        return this.exponentSymbol;
    }
    private final char exponentSymbol;

    @Override
    public char minusSign() {
        return this.minusSign;
    }

    private final char minusSign;

    @Override
    public char plusSign() {
        return this.plusSign;
    }

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
