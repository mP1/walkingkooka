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

public final class NodeSelectorPredicateConcatFunctionTest extends NodeSelectorPredicateFunctionTestCase<NodeSelectorPredicateConcatFunction, String> {

    @Test(expected = IllegalArgumentException.class)
    public void testZeroParametersFails() {
        this.apply2();
    }

    @Test
    public void testOneString() {
        this.applyAndCheck2(parameters("a1"), "a1");
    }

    @Test
    public void testTwoStrings() {
        this.applyAndCheck2(parameters("a1", "b2"), "a1b2");
    }

    @Test
    public void testThreeStrings() {
        this.applyAndCheck2(parameters("a1", "b2", "c3"), "a1b2c3");
    }

    @Test
    public void testFourStrings() {
        this.applyAndCheck2(parameters("a1", "b2", "c3", "d4"), "a1b2c3d4");
    }

    @Test
    public void testBoolean() {
        this.applyAndCheck2(parameters("a1", true, false), "a1truefalse");
    }

    @Test
    public void testNumbers() {
        this.applyAndCheck2(parameters("a1", 1, 234L), "a11234");
    }

    @Test
    public void testToString() {
        assertEquals("concat", this.createBiFunction().toString());
    }

    @Override
    protected NodeSelectorPredicateConcatFunction createBiFunction() {
        return NodeSelectorPredicateConcatFunction.INSTANCE;
    }

    @Override
    protected Class<NodeSelectorPredicateConcatFunction> type() {
        return NodeSelectorPredicateConcatFunction.class;
    }
}
