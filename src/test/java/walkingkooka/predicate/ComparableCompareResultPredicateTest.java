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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.compare.CompareResult;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ComparableCompareResultPredicateTest extends PredicateTestCase<ComparableCompareResultPredicate<String>, String> {

    private static final String LESS = "Z";
    private static final String EQUALS = "M";
    private static final String MORE = "A";

    @Test
    public void testWithNullCompareResultFails() {
        assertThrows(
            NullPointerException.class,
            () -> ComparableCompareResultPredicate.with(null, MORE)
        );
    }

    @Test
    public void testWithNullRightComparableFails() {
        assertThrows(
            NullPointerException.class,
            () -> ComparableCompareResultPredicate.with(CompareResult.EQ, null)
        );
    }

    // EQ .................................................................................

    @Test
    public void testEQLess() {
        this.testFalse(MORE);
    }

    @Test
    public void testEQEqual() {
        this.testTrue(EQUALS);
    }

    @Test
    public void testEQGreater() {
        this.testFalse(LESS);
    }

    // GT .................................................................................

    @Test
    public void testGTLess() {
        this.testTrue(CompareResult.GT, LESS);
    }

    @Test
    public void testGTGtual() {
        this.testFalse(CompareResult.GT, EQUALS);
    }

    @Test
    public void testGTGreater() {
        this.testFalse(CompareResult.GT, MORE);
    }

    // GTE .................................................................................

    @Test
    public void testGTELess() {
        this.testTrue(CompareResult.GTE, LESS);
    }

    @Test
    public void testGTEEqeual() {
        this.testTrue(CompareResult.GTE, EQUALS);
    }

    @Test
    public void testGTEGreater() {
        this.testFalse(CompareResult.GTE, MORE);
    }

    // LT .................................................................................

    @Test
    public void testLTLess() {
        this.testFalse(CompareResult.LT, LESS);
    }

    @Test
    public void testLTEqual() {
        this.testFalse(CompareResult.LT, EQUALS);
    }

    @Test
    public void testLTGreater() {
        this.testTrue(CompareResult.LT, MORE);
    }

    // LTE .................................................................................

    @Test
    public void testLTELess() {
        this.testFalse(CompareResult.LTE, LESS);
    }

    @Test
    public void testLTELEqual() {
        this.testTrue(CompareResult.LTE, EQUALS);
    }

    @Test
    public void testLTEGreater() {
        this.testTrue(CompareResult.LTE, MORE);
    }

    // NE .................................................................................

    @Test
    public void testNELess() {
        this.testTrue(CompareResult.NE, LESS);
    }

    @Test
    public void testNELEqual() {
        this.testFalse(CompareResult.NE, EQUALS);
    }

    @Test
    public void testNEGreater() {
        this.testTrue(CompareResult.NE, MORE);
    }

    // helpers .................................................................................

    private void testTrue(final CompareResult relation, final String value) {
        this.testTrue(this.createPredicate(relation), value);
    }

    private void testFalse(final CompareResult relation, final String value) {
        this.testFalse(this.createPredicate(relation), value);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), "EQ M");
    }

    @Override
    public ComparableCompareResultPredicate<String> createPredicate() {
        return this.createPredicate(CompareResult.EQ);
    }

    private ComparableCompareResultPredicate<String> createPredicate(final CompareResult relation) {
        return ComparableCompareResultPredicate.with(relation, EQUALS);
    }

    @Override
    public Class<ComparableCompareResultPredicate<String>> type() {
        return Cast.to(ComparableCompareResultPredicate.class);
    }
}
