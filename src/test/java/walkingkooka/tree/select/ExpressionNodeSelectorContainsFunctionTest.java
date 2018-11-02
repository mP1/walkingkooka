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

public final class ExpressionNodeSelectorContainsFunctionTest extends ExpressionNodeSelectorFunctionTestCase<ExpressionNodeSelectorContainsFunction, Boolean> {

    @Test(expected = IllegalArgumentException.class)
    public void testZeroParametersFails() {
        this.apply2();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testOneParameterFails() {
        this.apply2("a1");
    }

    @Test
    public void testContains() {
        this.applyAndCheck2(parameters("xyz", "x"), true);
    }

    @Test
    public void testContains2() {
        this.applyAndCheck2(parameters("xyz", "z"), true);
    }

    @Test
    public void testContains3() {
        this.applyAndCheck2(parameters(123, 1), true);
    }

    @Test
    public void testMissing() {
        this.applyAndCheck2(parameters("xyz", "a"), false);
    }

    @Test
    public void testMissing2() {
        this.applyAndCheck2(parameters(123, 4), false);
    }

    @Test
    public void testMissingCaseSensitive() {
        this.applyAndCheck2(parameters("xyz", "Z"), false);
    }

    @Test
    public void testToString() {
        assertEquals("contains", this.createBiFunction().toString());
    }

    @Override
    protected ExpressionNodeSelectorContainsFunction createBiFunction() {
        return ExpressionNodeSelectorContainsFunction.INSTANCE;
    }

    @Override
    protected Class<ExpressionNodeSelectorContainsFunction> type() {
        return ExpressionNodeSelectorContainsFunction.class;
    }
}
