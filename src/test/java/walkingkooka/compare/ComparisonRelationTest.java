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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ComparisonRelationTest implements ClassTesting2<ComparisonRelation>,
        PredicateTesting2<ComparisonRelation, Integer> {

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testClassVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testAllMethodsVisibility() {
        throw new UnsupportedOperationException();
    }

    // EQ................................................................

    @Test
    public void testEqNegative() {
        this.testFalse2(ComparisonRelation.EQ, Integer.MIN_VALUE);
    }

    @Test
    public void testEqNegative2() {
        this.testFalse2(ComparisonRelation.EQ, -1);
    }

    @Test
    public void testEqZero() {
        this.testTrue2(ComparisonRelation.EQ, 0);
    }

    @Test
    public void testEqPositive() {
        this.testFalse2(ComparisonRelation.EQ, +1);
    }

    @Test
    public void testEqPositive2() {
        this.testFalse2(ComparisonRelation.EQ, Integer.MAX_VALUE);
    }

    // GT................................................................

    @Test
    public void testGtNegative() {
        this.testFalse2(ComparisonRelation.GT, Integer.MIN_VALUE);
    }

    @Test
    public void testGtNegative2() {
        this.testFalse2(ComparisonRelation.GT, -1);
    }

    @Test
    public void testGtZero() {
        this.testFalse2(ComparisonRelation.GT, 0);
    }

    @Test
    public void testGtPositive() {
        this.testTrue2(ComparisonRelation.GT, +1);
    }

    @Test
    public void testGtPositive2() {
        this.testTrue2(ComparisonRelation.GT, Integer.MAX_VALUE);
    }

    // GTE................................................................

    @Test
    public void testGteNegative() {
        this.testFalse2(ComparisonRelation.GTE, Integer.MIN_VALUE);
    }

    @Test
    public void testGteNegative2() {
        this.testFalse2(ComparisonRelation.GTE, -1);
    }

    @Test
    public void testGteZero() {
        this.testTrue2(ComparisonRelation.GTE, 0);
    }

    @Test
    public void testGtePositive() {
        this.testTrue2(ComparisonRelation.GTE, +1);
    }

    @Test
    public void testGtePositive2() {
        this.testTrue2(ComparisonRelation.GTE, Integer.MAX_VALUE);
    }

    // LT................................................................

    @Test
    public void testLtNegative() {
        this.testTrue2(ComparisonRelation.LT, Integer.MIN_VALUE);
    }

    @Test
    public void testLtNegative2() {
        this.testTrue2(ComparisonRelation.LT, -1);
    }

    @Test
    public void testLtZero() {
        this.testFalse2(ComparisonRelation.LT, 0);
    }

    @Test
    public void testLtPositive() {
        this.testFalse2(ComparisonRelation.LT, +1);
    }

    @Test
    public void testLtPositive2() {
        this.testFalse2(ComparisonRelation.LT, Integer.MAX_VALUE);
    }

    // LTE................................................................

    @Test
    public void testLteNegative() {
        this.testTrue2(ComparisonRelation.LTE, Integer.MIN_VALUE);
    }

    @Test
    public void testLteNegative2() {
        this.testTrue2(ComparisonRelation.LTE, -1);
    }

    @Test
    public void testLteZero() {
        this.testTrue2(ComparisonRelation.LTE, 0);
    }

    @Test
    public void testLtePositive() {
        this.testFalse2(ComparisonRelation.LTE, +1);
    }

    @Test
    public void testLtePositive2() {
        this.testFalse2(ComparisonRelation.LTE, Integer.MAX_VALUE);
    }

    // NE................................................................

    @Test
    public void testNeNegative() {
        this.testTrue2(ComparisonRelation.NE, Integer.MIN_VALUE);
    }

    @Test
    public void testNeNegative2() {
        this.testTrue2(ComparisonRelation.NE, -1);
    }

    @Test
    public void testNeZero() {
        this.testFalse2(ComparisonRelation.NE, 0);
    }

    @Test
    public void testNePositive() {
        this.testTrue2(ComparisonRelation.NE, +1);
    }

    @Test
    public void testNePositive2() {
        this.testTrue2(ComparisonRelation.NE, Integer.MAX_VALUE);
    }

    // helpers............................................................

    private void testTrue2(final ComparisonRelation relation, final int value) {
        this.testTrue(relation, value);
        this.testFalse(relation.invert(), value);
    }

    private void testFalse2(final ComparisonRelation relation, final int value) {
        this.testFalse(relation, value);
        this.testTrue(relation.invert(), value);
    }

    @Test
    public void testSymbolEQ() {
        this.symbolAndCheck(ComparisonRelation.EQ, "=");
    }

    @Test
    public void testSymbolGT() {
        this.symbolAndCheck(ComparisonRelation.GT, ">");
    }

    @Test
    public void testSymbolGTE() {
        this.symbolAndCheck(ComparisonRelation.GTE, ">=");
    }

    @Test
    public void testSymbolLT() {
        this.symbolAndCheck(ComparisonRelation.LT, "<");
    }

    @Test
    public void testSymbolLTE() {
        this.symbolAndCheck(ComparisonRelation.LTE, "<=");
    }

    @Test
    public void testSymbolNE() {
        this.symbolAndCheck(ComparisonRelation.NE, "!=");
    }

    private void symbolAndCheck(final ComparisonRelation relation, final String pattern) {
        assertEquals(pattern, relation.symbol(), () -> "pattern for " + relation);
    }

    @Test
    public void testPredicate() {
        final Predicate<String> predicate = ComparisonRelation.EQ.predicate("M");
        assertTrue(predicate.test("M"), "EQ \"M\"");
        assertFalse(predicate.test("Z"), "EQ \"Z\"");
    }

    @Test
    public void testInvert() {
        for (ComparisonRelation relation : ComparisonRelation.values()) {
            predicateAndInvertedPredicateTest(relation, 0, -1);
            predicateAndInvertedPredicateTest(relation, 0, 0);
            predicateAndInvertedPredicateTest(relation, 0, +1);
        }
    }

    private void predicateAndInvertedPredicateTest(final ComparisonRelation relation, final int value, final int value2) {
        final boolean result = relation.predicate(value).test(value2);
        final ComparisonRelation inverted = relation.invert();
        final boolean invertedResult = inverted.predicate(value).test(value2);

        assertNotEquals(result, invertedResult, () -> result + " inverted " + inverted + " " + value + " " + value2);
    }

    @Test
    public void testInvertRoundTrip() {
        for (ComparisonRelation relation : ComparisonRelation.values()) {
            final ComparisonRelation inverted = relation.invert();
            assertNotSame(inverted, relation);
            assertSame(relation, inverted.invert());
        }
    }

    @Test
    public void testSwap() {
        for (ComparisonRelation relation : ComparisonRelation.values()) {
            predicateAndSwappedPredicateTest(relation, 0, -1);
            predicateAndSwappedPredicateTest(relation, 0, 0);
            predicateAndSwappedPredicateTest(relation, 0, +1);
        }
    }

    private void predicateAndSwappedPredicateTest(final ComparisonRelation relation, final int value, final int value2) {
        final boolean result = relation.predicate(value).test(value2);
        final boolean swappedResult = relation.swap().predicate(value2).test(value);

        assertEquals(result, swappedResult, () -> value + " " + relation + " " + value2);
    }

    @Test
    public void testSwapRoundTrip() {
        for (ComparisonRelation relation : ComparisonRelation.values()) {
            assertSame(relation, relation.swap().swap());
        }
    }

    @Test
    public void testFindWithSymbolNullFails() {
        assertThrows(NullPointerException.class, () -> ComparisonRelation.findWithSymbol(null));
    }

    @Test
    public void testFindWithSymbolUnknownFails() {
        assertThrows(IllegalArgumentException.class, () -> ComparisonRelation.findWithSymbol("?"));
    }

    @Test
    public void testFindWithSymbol() {
        for (ComparisonRelation relation : ComparisonRelation.values()) {
            assertSame(relation, ComparisonRelation.findWithSymbol(relation.symbol()));
        }
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(ComparisonRelation.EQ, "EQ");
    }

    @Override
    public ComparisonRelation createPredicate() {
        return ComparisonRelation.EQ;
    }

    @Override
    public Class<ComparisonRelation> type() {
        return ComparisonRelation.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
