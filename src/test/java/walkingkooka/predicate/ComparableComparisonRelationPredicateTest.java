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
import walkingkooka.compare.ComparisonRelation;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ComparableComparisonRelationPredicateTest extends PredicateTestCase<ComparableComparisonRelationPredicate<String>, String> {

    private static final String LESS = "Z";
    private static final String EQUALS = "M";
    private static final String MORE = "A";

    @Test
    public void testWithNullComparisonResultFails() {
        assertThrows(NullPointerException.class, () -> ComparableComparisonRelationPredicate.with(null, MORE));
    }

    @Test
    public void testWithNullRightComparableFails() {
        assertThrows(NullPointerException.class, () -> ComparableComparisonRelationPredicate.with(ComparisonRelation.EQ, null));
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
        this.testTrue(ComparisonRelation.GT, LESS);
    }

    @Test
    public void testGTGtual() {
        this.testFalse(ComparisonRelation.GT, EQUALS);
    }

    @Test
    public void testGTGreater() {
        this.testFalse(ComparisonRelation.GT, MORE);
    }

    // GTE .................................................................................

    @Test
    public void testGTELess() {
        this.testTrue(ComparisonRelation.GTE, LESS);
    }

    @Test
    public void testGTEEqeual() {
        this.testTrue(ComparisonRelation.GTE, EQUALS);
    }

    @Test
    public void testGTEGreater() {
        this.testFalse(ComparisonRelation.GTE, MORE);
    }

    // LT .................................................................................

    @Test
    public void testLTLess() {
        this.testFalse(ComparisonRelation.LT, LESS);
    }

    @Test
    public void testLTEqual() {
        this.testFalse(ComparisonRelation.LT, EQUALS);
    }

    @Test
    public void testLTGreater() {
        this.testTrue(ComparisonRelation.LT, MORE);
    }

    // LTE .................................................................................

    @Test
    public void testLTELess() {
        this.testFalse(ComparisonRelation.LTE, LESS);
    }

    @Test
    public void testLTELEqual() {
        this.testTrue(ComparisonRelation.LTE, EQUALS);
    }

    @Test
    public void testLTEGreater() {
        this.testTrue(ComparisonRelation.LTE, MORE);
    }

    // NE .................................................................................

    @Test
    public void testNELess() {
        this.testTrue(ComparisonRelation.NE, LESS);
    }

    @Test
    public void testNELEqual() {
        this.testFalse(ComparisonRelation.NE, EQUALS);
    }

    @Test
    public void testNEGreater() {
        this.testTrue(ComparisonRelation.NE, MORE);
    }

    // helpers .................................................................................

    private void testTrue(final ComparisonRelation relation, final String value) {
        this.testTrue(this.createPredicate(relation), value);
    }

    private void testFalse(final ComparisonRelation relation, final String value) {
        this.testFalse(this.createPredicate(relation), value);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), "EQ M");
    }

    @Override
    public ComparableComparisonRelationPredicate<String> createPredicate() {
        return this.createPredicate(ComparisonRelation.EQ);
    }

    private ComparableComparisonRelationPredicate<String> createPredicate(final ComparisonRelation relation) {
        return ComparableComparisonRelationPredicate.with(relation, EQUALS);
    }

    @Override
    public Class<ComparableComparisonRelationPredicate<String>> type() {
        return Cast.to(ComparableComparisonRelationPredicate.class);
    }
}
