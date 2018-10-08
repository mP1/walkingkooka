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

package walkingkooka.predicate;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.compare.ComparisonRelation;

import static org.junit.Assert.assertEquals;

public final class ComparatorComparisonRelationPredicateTest extends PredicateTestCase<ComparableComparisonRelationPredicate<String>, String> {

    @Test(expected = NullPointerException.class)
    public void testWithNullComparableFails() {
        ComparableComparisonRelationPredicate.with(null, ComparisonRelation.EQ);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullComparisonResultFails() {
        ComparableComparisonRelationPredicate.with("A", null);
    }

    // EQ .................................................................................

    @Test
    public void testEQLess() {
        this.testFalse("A");
    }

    @Test
    public void testEQEqual() {
        this.testTrue("M");
    }

    @Test
    public void testEQGreater() {
        this.testFalse("Z");
    }

    // GT .................................................................................

    @Test
    public void testGTLess() {
        this.testTrue(ComparisonRelation.GT, "A");
    }

    @Test
    public void testGTGtual() {
        this.testFalse(ComparisonRelation.GT, "M");
    }

    @Test
    public void testGTGreater() {
        this.testFalse(ComparisonRelation.GT, "Z");
    }

    // GTE .................................................................................

    @Test
    public void testGTELess() {
        this.testTrue(ComparisonRelation.GTE, "A");
    }

    @Test
    public void testGTEEqeual() {
        this.testTrue(ComparisonRelation.GTE, "M");
    }

    @Test
    public void testGTEGreater() {
        this.testFalse(ComparisonRelation.GTE, "Z");
    }

    // LT .................................................................................

    @Test
    public void testLTLess() {
        this.testFalse(ComparisonRelation.LT, "A");
    }

    @Test
    public void testLTEqual() {
        this.testFalse(ComparisonRelation.LT, "M");
    }

    @Test
    public void testLTGreater() {
        this.testTrue(ComparisonRelation.LT, "Z");
    }

    // LTE .................................................................................

    @Test
    public void testLTELess() {
        this.testFalse(ComparisonRelation.LTE, "A");
    }

    @Test
    public void testLTELEqual() {
        this.testTrue(ComparisonRelation.LTE, "M");
    }

    @Test
    public void testLTEGreater() {
        this.testTrue(ComparisonRelation.LTE, "Z");
    }


    // NE .................................................................................

    @Test
    public void testNELess() {
        this.testTrue(ComparisonRelation.NE, "A");
    }

    @Test
    public void testNELEqual() {
        this.testFalse(ComparisonRelation.NE, "M");
    }

    @Test
    public void testNEGreater() {
        this.testTrue(ComparisonRelation.NE, "Z");
    }

    private void testTrue(final ComparisonRelation relation, final String value) {
        this.testTrue(this.createPredicate(relation), value);
    }

    private void testFalse(final ComparisonRelation relation, final String value) {
        this.testFalse(this.createPredicate(relation), value);
    }

    @Test
    public void testToString() {
        assertEquals("M EQ", this.createPredicate().toString());
    }

    @Override
    protected ComparableComparisonRelationPredicate<String> createPredicate() {
        return ComparableComparisonRelationPredicate.with("M", ComparisonRelation.EQ);
    }

    private ComparableComparisonRelationPredicate<String> createPredicate(final ComparisonRelation relation) {
        return ComparableComparisonRelationPredicate.with("M", relation);
    }

    @Override
    protected Class<ComparableComparisonRelationPredicate<String>> type() {
        return Cast.to(ComparableComparisonRelationPredicate.class);
    }
}
