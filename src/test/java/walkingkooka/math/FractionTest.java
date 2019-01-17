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

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

public final class FractionTest extends ClassTestCase<Fraction> implements SerializationTesting<Fraction> {

    private final static BigInteger NUMERATOR = BigInteger.ONE;
    private final static BigInteger DENOMINATOR = BigInteger.TEN;

    @Test(expected = NullPointerException.class)
    public void testWithNullNumeratorFails() {
        Fraction.with(null, DENOMINATOR);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullDenominatorFails() {
        Fraction.with(NUMERATOR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroDenominatorFails() {
        Fraction.with(NUMERATOR, BigInteger.ZERO);
    }

    @Test
    public void testWith() {
        final Fraction fraction = Fraction.with(NUMERATOR, DENOMINATOR);
        assertEquals("numerator", NUMERATOR, fraction.numerator());
        assertEquals("denominator", DENOMINATOR, fraction.denominator());
    }

    @Test
    public void testToString() {
        assertEquals("1/10", Fraction.with(NUMERATOR, DENOMINATOR).toString());
    }

    @Override
    public Class<Fraction> type() {
        return Fraction.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Fraction serializableInstance() {
        return Fraction.with(BigInteger.ONE, BigInteger.TEN);
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
