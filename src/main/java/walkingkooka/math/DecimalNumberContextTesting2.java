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

import org.junit.jupiter.api.Test;
import walkingkooka.ContextTesting;

import java.math.MathContext;

/**
 * Mixing testing interface for {@link DecimalNumberContext}
 */
public interface DecimalNumberContextTesting2<C extends DecimalNumberContext> extends DecimalNumberContextTesting,
        ContextTesting<C> {

    @Test
    default void testCurrencySymbol() {
        this.checkCurrencySymbol(this.createContext(), this.currencySymbol());
    }

    @Test
    default void testDecimalPoint() {
        this.checkDecimalPoint(this.createContext(), this.decimalPoint());
    }

    @Test
    default void testExponentSymbol() {
        this.checkExponentSymbol(this.createContext(), this.exponentSymbol());
    }

    @Test
    default void testGroupingSeparator() {
        this.checkGroupingSeparator(this.createContext(), this.groupingSeparator());
    }

    @Test
    default void testMathContext() {
        this.hasMathContextAndCheck(this.createContext(), this.mathContext());
    }

    @Test
    default void testNegativeSign() {
        this.checkNegativeSign(this.createContext(), this.negativeSign());
    }

    @Test
    default void testPercentageSymbol() {
        this.checkPercentageSymbol(this.createContext(), this.percentageSymbol());
    }

    @Test
    default void testPositiveSign() {
        this.checkPositiveSign(this.createContext(), this.positiveSign());
    }

    String currencySymbol();

    char decimalPoint();

    char exponentSymbol();

    char groupingSeparator();

    MathContext mathContext();

    char negativeSign();

    char percentageSymbol();

    char positiveSign();

    @Override
    default String typeNameSuffix() {
        return DecimalNumberContext.class.getSimpleName();
    }
}
