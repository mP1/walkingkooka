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

package walkingkooka.compare;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.predicate.PredicateTestCase;

public final class ComparisonRelationPredicateTest extends PredicateTestCase<ComparisonRelation, Integer> {

    @Test
    @Ignore
    @Override
    public void testCheckNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testClassVisibility() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
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
        this.testFalse2(ComparisonRelation.EQ,-1);
    }

    @Test
    public void testEqZero() {
        this.testTrue2(ComparisonRelation.EQ,0);
    }

    @Test
    public void testEqPositive() {
        this.testFalse2(ComparisonRelation.EQ,+1);
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

    @Override
    protected ComparisonRelation createPredicate() {
        return ComparisonRelation.EQ;
    }

    @Override
    protected Class<ComparisonRelation> type() {
        return ComparisonRelation.class;
    }
}
