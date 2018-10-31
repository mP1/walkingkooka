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

public final class NodeSelectorPredicateSubstringBeforeFunctionTest extends NodeSelectorPredicateFunctionTestCase<NodeSelectorPredicateSubstringBeforeFunction, String> {

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
        this.applyAndCheck2(list("abcdef", "z"), "");
    }

    @Test
    public void testMissingWrongCase() {
        this.applyAndCheck2(list("abcdef", "A"), "");
    }

    @Test
    public void testPresent() {
        this.applyAndCheck2(list("abc", "c"), "ab");
    }

    @Test
    public void testPresent2() {
        this.applyAndCheck2(list("abcdef", "de"), "abc");
    }

    @Test
    public void testPresent3() {
        this.applyAndCheck2(list("abcdef", "bcde"), "a");
    }

    @Test
    public void testPresentFirst() {
        this.applyAndCheck2(list("abcd", "a"), "");
    }

    @Test
    public void testToString() {
        assertEquals("substring-before", this.createBiFunction().toString());
    }

    @Override
    protected NodeSelectorPredicateSubstringBeforeFunction createBiFunction() {
        return NodeSelectorPredicateSubstringBeforeFunction.INSTANCE;
    }

    @Override
    protected Class<NodeSelectorPredicateSubstringBeforeFunction> type() {
        return NodeSelectorPredicateSubstringBeforeFunction.class;
    }
}
