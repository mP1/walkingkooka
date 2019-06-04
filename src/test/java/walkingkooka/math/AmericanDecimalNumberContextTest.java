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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.MemberVisibility;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AmericanDecimalNumberContextTest implements ClassTesting2<AmericanDecimalNumberContext>,
        DecimalNumberContextTesting<AmericanDecimalNumberContext> {

    private final static MathContext MATH_CONTEXT = MathContext.DECIMAL32;

    @Test
    public void testWithNullMathContextFails() {
        assertThrows(NullPointerException.class, () -> {
            AmericanDecimalNumberContext.with(null);
        });
    }

    @Test
    public void testWithMathContext32() {
        withConstantAndCheck(MathContext.DECIMAL32);
    }

    @Test
    public void testWithMathContext64() {
        withConstantAndCheck(MathContext.DECIMAL64);
    }

    @Test
    public void testWithMathContext128() {
        withConstantAndCheck(MathContext.DECIMAL128);
    }

    @Test
    public void testWithMathContextUnlimited() {
        withConstantAndCheck(MathContext.UNLIMITED);
    }

    private void withConstantAndCheck(final MathContext mathContext) {
        assertSame(AmericanDecimalNumberContext.with(mathContext), AmericanDecimalNumberContext.with(mathContext));
        withAndCheck(mathContext);
    }

    @Test
    public void testWith() {
        this.withAndCheck(MATH_CONTEXT);
    }

    @Test
    public void testWithCustomMathContext() {
        this.withAndCheck(new MathContext(33));
    }

    private void withAndCheck(final MathContext mathContext) {
        final AmericanDecimalNumberContext context = AmericanDecimalNumberContext.with(mathContext);
        this.checkCurrencySymbol(context, "$");
        this.checkDecimalPoint(context, '.');
        this.checkExponentSymbol(context, 'E');
        this.checkGroupingSeparator(context, ',');
        this.checkMinusSign(context, '-');
        this.checkPercentageSymbol(context, '%');
        this.checkPlusSign(context, '+');
        this.checkMathContext(context, mathContext);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), "\"$\" '.' 'E' ',' '-' '%' '+' " + MATH_CONTEXT);
    }

    @Override
    public AmericanDecimalNumberContext createContext() {
        return AmericanDecimalNumberContext.with(MATH_CONTEXT);
    }

    @Override
    public Class<AmericanDecimalNumberContext> type() {
        return AmericanDecimalNumberContext.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}