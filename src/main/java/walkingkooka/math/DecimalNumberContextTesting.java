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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing testing interface for {@link DecimalNumberContext}
 */
public interface DecimalNumberContextTesting<C extends DecimalNumberContext> extends ContextTesting<C> {

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
        this.checkMathContext(this.createContext(), this.mathContext());
    }

    @Test
    default void testMinusSign() {
        this.checkMinusSign(this.createContext(), this.minusSign());
    }

    @Test
    default void testPercentageSymbol() {
        this.checkPercentageSymbol(this.createContext(), this.percentageSymbol());
    }

    @Test
    default void testPlusSign() {
        this.checkPlusSign(this.createContext(), this.plusSign());
    }

    String currencySymbol();

    char decimalPoint();

    char exponentSymbol();

    char groupingSeparator();

    MathContext mathContext();

    char minusSign();

    char percentageSymbol();

    char plusSign();

    @Override
    default String typeNameSuffix() {
        return DecimalNumberContext.class.getSimpleName();
    }

    default void checkCurrencySymbol(final DecimalNumberContext context, final String currencySymbol) {
        assertEquals(currencySymbol, context.currencySymbol(), "currencySymbol");
    }

    default void checkDecimalPoint(final DecimalNumberContext context, final char decimalPoint) {
        assertEquals(decimalPoint, context.decimalPoint(), "decimalPoint");
    }

    default void checkExponentSymbol(final DecimalNumberContext context, final char exponentSymbol) {
        assertEquals(exponentSymbol, context.exponentSymbol(), "exponentSymbol");
    }

    default void checkGroupingSeparator(final DecimalNumberContext context, final char groupingSeparator) {
        assertEquals(groupingSeparator, context.groupingSeparator(), "groupingSeparator");
    }

    default void checkMinusSign(final DecimalNumberContext context, final char minusSign) {
        assertEquals(minusSign, context.minusSign(), "minusSign");
    }

    default void checkPercentageSymbol(final DecimalNumberContext context, final char percentageSymbol) {
        assertEquals(percentageSymbol, context.percentageSymbol(), "percentageSymbol");
    }

    default void checkPlusSign(final DecimalNumberContext context, final char plusSign) {
        assertEquals(plusSign, context.plusSign(), "plusSign");
    }

    default void checkMathContext(final DecimalNumberContext context, final MathContext mathContext) {
        assertEquals(mathContext, context.mathContext(), "mathContext");
    }
}
