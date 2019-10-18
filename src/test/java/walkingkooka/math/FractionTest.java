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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.HashCodeEqualsDefinedTesting2;
import walkingkooka.test.ToStringTesting;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FractionTest implements ClassTesting2<Fraction>,
        HashCodeEqualsDefinedTesting2<Fraction>,
        ToStringTesting<Fraction> {

    private final static BigInteger NUMERATOR = BigInteger.ONE;
    private final static BigInteger DENOMINATOR = BigInteger.TEN;
    private final static BigInteger DIFFERENT = BigInteger.valueOf(2);

    @Test
    public void testWithNullNumeratorFails() {
        assertThrows(NullPointerException.class, () -> Fraction.with(null, DENOMINATOR));
    }

    @Test
    public void testWithNullDenominatorFails() {
        assertThrows(NullPointerException.class, () -> Fraction.with(NUMERATOR, null));
    }

    @Test
    public void testWithZeroDenominatorFails() {
        assertThrows(IllegalArgumentException.class, () -> Fraction.with(NUMERATOR, BigInteger.ZERO));
    }

    @Test
    public void testWith() {
        final Fraction fraction = Fraction.with(NUMERATOR, DENOMINATOR);
        assertEquals(NUMERATOR, fraction.numerator(), "numerator");
        assertEquals(DENOMINATOR, fraction.denominator(), "denominator");
    }

    @Test
    public void testEqualsDifferentNumerator() {
        this.checkNotEquals(Fraction.with(DIFFERENT, DENOMINATOR));
    }

    @Test
    public void testEqualsDifferentDenominator() {
        this.checkNotEquals(Fraction.with(NUMERATOR, DIFFERENT));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(Fraction.with(NUMERATOR, DENOMINATOR), "1/10");
    }

    @Override
    public Class<Fraction> type() {
        return Fraction.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Fraction createObject() {
        return Fraction.with(NUMERATOR, DENOMINATOR);
    }
}
