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

package walkingkooka.tree.select;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ExpressionNodeSelectorPredicateNotFunctionTest extends ExpressionNodeSelectorPredicateFunctionTestCase<ExpressionNodeSelectorPredicateNotFunction, Boolean> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFunctionFails() {
        ExpressionNodeSelectorPredicateNotFunction.with(null);
    }

    @Test
    public void testInverts() {
        this.applyAndCheck2(parameters("a1", "a"), false);
    }

    @Test
    public void testInverts2() {
        this.applyAndCheck2(parameters("a1", "z"), true);
    }

    @Test
    public void testToString() {
        assertEquals("not(" + ExpressionNodeSelectorPredicateFunction.CONTAINS + ")", this.createBiFunction().toString());
    }

    @Override
    protected ExpressionNodeSelectorPredicateNotFunction createBiFunction() {
        return ExpressionNodeSelectorPredicateNotFunction.with(ExpressionNodeSelectorPredicateFunction.CONTAINS);
    }

    @Override
    protected Class<ExpressionNodeSelectorPredicateNotFunction> type() {
        return ExpressionNodeSelectorPredicateNotFunction.class;
    }
}
