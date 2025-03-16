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
import walkingkooka.text.CharSequences;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CompareResultTest implements ClassTesting2<CompareResult>,
    PredicateTesting2<CompareResult, Integer> {

    // EQ...............................................................................................................

    @Test
    public void testTestEqNegative() {
        this.testFalse2(
            CompareResult.EQ,
            Integer.MIN_VALUE
        );
    }

    @Test
    public void testTestEqNegative2() {
        this.testFalse2(
            CompareResult.EQ,
            -1
        );
    }

    @Test
    public void testTestEqZero() {
        this.testTrue2(
            CompareResult.EQ,
            0
        );
    }

    @Test
    public void testTestEqPositive() {
        this.testFalse2(
            CompareResult.EQ,
            +1
        );
    }

    @Test
    public void testTestEqPositive2() {
        this.testFalse2(
            CompareResult.EQ,
            Integer.MAX_VALUE
        );
    }

    // GT...............................................................................................................

    @Test
    public void testTestGtNegative() {
        this.testFalse2(
            CompareResult.GT,
            Integer.MIN_VALUE
        );
    }

    @Test
    public void testTestGtNegative2() {
        this.testFalse2(
            CompareResult.GT,
            -1
        );
    }

    @Test
    public void testTestGtZero() {
        this.testFalse2(
            CompareResult.GT,
            0
        );
    }

    @Test
    public void testTestGtPositive() {
        this.testTrue2(
            CompareResult.GT,
            +1
        );
    }

    @Test
    public void testTestGtPositive2() {
        this.testTrue2(
            CompareResult.GT,
            Integer.MAX_VALUE
        );
    }

    // GTE..............................................................................................................

    @Test
    public void testTestGteNegative() {
        this.testFalse2(
            CompareResult.GTE,
            Integer.MIN_VALUE
        );
    }

    @Test
    public void testTestGteNegative2() {
        this.testFalse2(
            CompareResult.GTE,
            -1
        );
    }

    @Test
    public void testTestGteZero() {
        this.testTrue2(
            CompareResult.GTE,
            0
        );
    }

    @Test
    public void testTestGtePositive() {
        this.testTrue2(
            CompareResult.GTE,
            +1
        );
    }

    @Test
    public void testTestGtePositive2() {
        this.testTrue2(
            CompareResult.GTE, 
            Integer.MAX_VALUE
        );
    }

    // LT...............................................................................................................

    @Test
    public void testTestLtNegative() {
        this.testTrue2(
            CompareResult.LT,
            Integer.MIN_VALUE
        );
    }

    @Test
    public void testTestLtNegative2() {
        this.testTrue2(
            CompareResult.LT,
            -1
        );
    }

    @Test
    public void testTestLtZero() {
        this.testFalse2(
            CompareResult.LT,
            0
        );
    }

    @Test
    public void testTestLtPositive() {
        this.testFalse2(
            CompareResult.LT,
            +1
        );
    }

    @Test
    public void testTestLtPositive2() {
        this.testFalse2(
            CompareResult.LT,
            Integer.MAX_VALUE
        );
    }

    // LTE..............................................................................................................

    @Test
    public void testTestLteNegative() {
        this.testTrue2(
            CompareResult.LTE,
            Integer.MIN_VALUE
        );
    }

    @Test
    public void testTestLteNegative2() {
        this.testTrue2(
            CompareResult.LTE,
            -1
        );
    }

    @Test
    public void testTestLteZero() {
        this.testTrue2(
            CompareResult.LTE,
            0
        );
    }

    @Test
    public void testTestLtePositive() {
        this.testFalse2(
            CompareResult.LTE,
            +1
        );
    }

    @Test
    public void testTestLtePositive2() {
        this.testFalse2(
            CompareResult.LTE,
            Integer.MAX_VALUE
        );
    }

    // NE...............................................................................................................

    @Test
    public void testTestNeNegative() {
        this.testTrue2(
            CompareResult.NE,
            Integer.MIN_VALUE
        );
    }

    @Test
    public void testTestNeNegative2() {
        this.testTrue2(
            CompareResult.NE,
            -1
        );
    }

    @Test
    public void testTestNeZero() {
        this.testFalse2(
            CompareResult.NE,
            0
        );
    }

    @Test
    public void testTestNePositive() {
        this.testTrue2(
            CompareResult.NE,
            +1
        );
    }

    @Test
    public void testTestNePositive2() {
        this.testTrue2(
            CompareResult.NE, 
            Integer.MAX_VALUE
        );
    }

    // helpers..........................................................................................................

    private void testTrue2(final CompareResult relation, 
                           final int value) {
        this.testTrue3(relation, value);
        this.testFalse3(relation.invert(), value);
    }

    private void testFalse2(final CompareResult relation, 
                            final int value) {
        this.testFalse3(relation, value);
        this.testTrue3(relation.invert(), value);
    }

    // ??? Not sure why cant call default method...
    private void testTrue3(final Predicate<Integer> predicate,
                           final Integer value) {
        this.checkEquals(true,
            predicate.test(value),
            () -> predicate + " should match=" + CharSequences.quoteIfChars(value));

        this.checkEquals(true,
            predicate.test(value.intValue()),
            () -> predicate + " should match=" + CharSequences.quoteIfChars(value));
    }

    private void testFalse3(final Predicate<Integer> predicate,
                            final Integer value) {
        this.checkEquals(false,
            predicate.test(value),
            () -> predicate + " should not match=" + CharSequences.quoteIfChars(value));

        this.checkEquals(false,
            predicate.test(value.intValue()),
            () -> predicate + " should not match=" + CharSequences.quoteIfChars(value));
    }

    @Test
    public void testSymbolEQ() {
        this.symbolAndCheck(
            CompareResult.EQ,
            "="
        );
    }

    @Test
    public void testSymbolGT() {
        this.symbolAndCheck(
            CompareResult.GT,
            ">"
        );
    }

    @Test
    public void testSymbolGTE() {
        this.symbolAndCheck(
            CompareResult.GTE,
            ">=");
    }

    @Test
    public void testSymbolLT() {
        this.symbolAndCheck(
            CompareResult.LT,
            "<"
        );
    }

    @Test
    public void testSymbolLTE() {
        this.symbolAndCheck(
            CompareResult.LTE,
            "<="
        );
    }

    @Test
    public void testSymbolNE() {
        this.symbolAndCheck(
            CompareResult.NE,
            "!="
        );
    }

    private void symbolAndCheck(final CompareResult relation,
                                final String pattern) {
        this.checkEquals(
            pattern, 
            relation.symbol(),
            () -> "pattern for " + relation
        );
    }

    @Test
    public void testPredicate() {
        final Predicate<String> predicate = CompareResult.EQ.predicate("M");
        assertTrue(predicate.test("M"), "EQ \"M\"");
        assertFalse(predicate.test("Z"), "EQ \"Z\"");
    }

    @Test
    public void testInvert() {
        for (CompareResult relation : CompareResult.values()) {
            predicateAndInvertedPredicateTest(relation, 0, -1);
            predicateAndInvertedPredicateTest(relation, 0, 0);
            predicateAndInvertedPredicateTest(relation, 0, +1);
        }
    }

    private void predicateAndInvertedPredicateTest(final CompareResult relation,
                                                   final int value,
                                                   final int value2) {
        final boolean result = relation.predicate(value).test(value2);
        final CompareResult inverted = relation.invert();
        final boolean invertedResult = inverted.predicate(value).test(value2);

        this.checkNotEquals(
            result, 
            invertedResult,
            () -> result + " inverted " + inverted + " " + value + " " + value2
        );
    }

    @Test
    public void testInvertRoundTrip() {
        for (CompareResult relation : CompareResult.values()) {
            final CompareResult inverted = relation.invert();
            assertNotSame(inverted, relation);
            assertSame(relation, inverted.invert());
        }
    }

    @Test
    public void testSwap() {
        for (CompareResult relation : CompareResult.values()) {
            predicateAndSwappedPredicateTest(relation, 0, -1);
            predicateAndSwappedPredicateTest(relation, 0, 0);
            predicateAndSwappedPredicateTest(relation, 0, +1);
        }
    }

    private void predicateAndSwappedPredicateTest(final CompareResult relation,
                                                  final int value,
                                                  final int value2) {
        final boolean result = relation.predicate(value)
            .test(value2);
        final boolean swappedResult = relation.swap()
            .predicate(value2)
            .test(value);

        this.checkEquals(result, swappedResult, () -> value + " " + relation + " " + value2);
    }

    @Test
    public void testSwapRoundTrip() {
        for (CompareResult relation : CompareResult.values()) {
            assertSame(relation, relation.swap().swap());
        }
    }

    @Test
    public void testFindWithSymbolNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> CompareResult.findWithSymbol(null)
        );
    }

    @Test
    public void testFindWithSymbolUnknownFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> CompareResult.findWithSymbol("?")
        );
    }

    @Test
    public void testFindWithSymbol() {
        for (CompareResult relation : CompareResult.values()) {
            assertSame(relation, CompareResult.findWithSymbol(relation.symbol()));
        }
    }

    // intCompareResult.................................................................................................

    @Test
    public void testIntCompareResultZero() {
        this.intCompareResultAndCheck(
            0,
            CompareResult.EQ
        );
    }

    @Test
    public void testIntCompareResultMinus() {
        this.intCompareResultAndCheck(
            Integer.compare(0, 1),
            CompareResult.LT
        );
    }

    @Test
    public void testIntCompareResultPlus() {
        this.intCompareResultAndCheck(
            Integer.compare(1, 0),
            CompareResult.GT
        );
    }

    private void intCompareResultAndCheck(final int value,
                                          final CompareResult expected) {
        this.checkEquals(
            expected,
            CompareResult.intCompareResult(value),
            () -> "" + value
        );
    }

    // value............................................................................................................

    @Test
    public void testValueLess() {
        this.valueAndCheck(
            CompareResult.LT,
            "less",
            "eq",
            "greater",
            "less"
        );
    }

    @Test
    public void testValueEq() {
        this.valueAndCheck(
            CompareResult.EQ,
            "less",
            "eq",
            "greater",
            "eq"
        );
    }

    @Test
    public void testValueGt() {
        this.valueAndCheck(
            CompareResult.GT,
            "lt",
            "eq",
            "greater",
            "greater"
        );
    }

    @Test
    public void testValueLessNull() {
        this.valueAndCheck(
            CompareResult.LT,
            null,
            "eq",
            "greater",
            null
        );
    }

    @Test
    public void testValueEqNull() {
        this.valueAndCheck(
            CompareResult.EQ,
            "less",
            null,
            "greater",
            null
        );
    }

    @Test
    public void testValueGtNull() {
        this.valueAndCheck(
            CompareResult.GT,
            "lt",
            "eq",
            null,
            null
        );
    }

    private <T> void valueAndCheck(final CompareResult result,
                                   final T less,
                                   final T equal,
                                   final T greater,
                                   final T expected) {
        this.checkEquals(
            expected,
            result.value(
                less,
                equal,
                greater
            ),
            () -> result + " less: " + less + " equal: " + equal + " greater: " + greater
        );
    }

    @Test
    public void testValueLTEFails() {
        this.valueFails(
            CompareResult.LTE,
            1,
            2,
            3
        );
    }

    @Test
    public void testValueGTEFails() {
        this.valueFails(
            CompareResult.GTE,
            1,
            2,
            3
        );
    }

    @Test
    public void testValueNEFails() {
        this.valueFails(
            CompareResult.NE,
            1,
            2,
            3
        );
    }

    private <T> void valueFails(final CompareResult result,
                                final T less,
                                final T equal,
                                final T greater) {
        assertThrows(
            UnsupportedOperationException.class,
            () -> result.value(
                less,
                equal,
                greater
            )
        );
    }

    // get............................................................................................................

    @Test
    public void testGetLess() {
        this.getAndCheck(
            CompareResult.LT,
            () -> "less",
            () -> "eq",
            () -> "greater",
            "less"
        );
    }

    @Test
    public void testGetEq() {
        this.getAndCheck(
            CompareResult.EQ,
            () -> "less",
            () -> "eq",
            () -> "greater",
            "eq"
        );
    }

    @Test
    public void testGetGt() {
        this.getAndCheck(
            CompareResult.GT,
            () -> "lt",
            () -> "eq",
            () -> "greater",
            "greater"
        );
    }

    @Test
    public void testGetLessNull() {
        this.getAndCheck(
            CompareResult.LT,
            () -> null,
            () -> "eq",
            () -> "greater",
            null
        );
    }

    @Test
    public void testGetEqNull() {
        this.getAndCheck(
            CompareResult.EQ,
            () -> "less",
            () -> null,
            () -> "greater",
            null
        );
    }

    @Test
    public void testGetGtNull() {
        this.getAndCheck(
            CompareResult.GT,
            () -> "lt",
            () -> "eq",
            () -> null,
            null
        );
    }

    @Test
    public void testGetEqContra() {
        this.getAndCheck(
            CompareResult.EQ,
            () -> Optional.of(1.0),
            () -> Optional.of(2L),
            () -> Optional.of(3f),
            Optional.of(2L)
        );
    }

    private <T> void getAndCheck(final CompareResult result,
                                 final Supplier<? extends T> less,
                                 final Supplier<? extends T> equal,
                                 final Supplier<? extends T> greater,
                                 final T expected) {
        this.checkEquals(
            expected,
            result.get(
                less,
                equal,
                greater
            ),
            () -> result + " less: " + less + " equal: " + equal + " greater: " + greater
        );
    }

    @Test
    public void testGetLteFails() {
        this.getFails(
            CompareResult.LTE,
            () -> 1,
            () -> 2,
            () -> 3
        );
    }

    @Test
    public void testGetGteFails() {
        this.getFails(
            CompareResult.GTE,
            () -> 1,
            () -> 2,
            () -> 3
        );
    }

    @Test
    public void testGetNeFails() {
        this.getFails(
            CompareResult.NE,
            () -> 1,
            () -> 2,
            () -> 3
        );
    }

    private <T> void getFails(final CompareResult result,
                              final Supplier<? extends T> less,
                              final Supplier<? extends T> equal,
                              final Supplier<? extends T> greater) {
        assertThrows(
            UnsupportedOperationException.class,
            () -> result.get(
                less,
                equal,
                greater
            )
        );
    }

    // Object...........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(CompareResult.EQ, "EQ");
    }

    @Override
    public CompareResult createPredicate() {
        return CompareResult.EQ;
    }

    // class............................................................................................................

    @Override
    public Class<CompareResult> type() {
        return CompareResult.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

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
}
