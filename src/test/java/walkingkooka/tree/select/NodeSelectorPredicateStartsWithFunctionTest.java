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

public final class NodeSelectorPredicateStartsWithFunctionTest extends NodeSelectorPredicateFunctionTestCase<NodeSelectorPredicateStartsWithFunction, Boolean> {

    @Test(expected = IllegalArgumentException.class)
    public void testZeroParametersFails() {
        this.apply2();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOneParametersFails() {
        this.apply2("a1");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testThreeParametersFails() {
        this.apply2("a1", 2, 3);
    }

    @Test
    public void testMissing() {
        this.applyAndCheck2(parameters("abc", "d"), false);
    }

    @Test
    public void testMissing2() {
        this.applyAndCheck2(parameters("abcd", "abx"), false);
    }

    @Test
    public void testMissingButEndsWith() {
        this.applyAndCheck2(parameters("abc", "bc"), false);
    }

    @Test
    public void testPresent() {
        this.applyAndCheck2(parameters("abc", "a"), true);
    }

    @Test
    public void testPresent2() {
        this.applyAndCheck2(parameters("abcd", "abc"), true);
    }

    @Test
    public void testPresentDifferentCase() {
        this.applyAndCheck2(parameters("abc", "A"), false);
    }

    @Test
    public void testContainsEmpty() {
        this.applyAndCheck2(parameters("abc", ""), true);
    }

    @Test
    public void testToString() {
        assertEquals("starts-with", this.createBiFunction().toString());
    }

    @Override
    protected NodeSelectorPredicateStartsWithFunction createBiFunction() {
        return NodeSelectorPredicateStartsWithFunction.INSTANCE;
    }

    @Override
    protected Class<NodeSelectorPredicateStartsWithFunction> type() {
        return NodeSelectorPredicateStartsWithFunction.class;
    }
}
