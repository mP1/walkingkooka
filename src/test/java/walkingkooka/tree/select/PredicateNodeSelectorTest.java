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
 */

package walkingkooka.tree.select;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;


final public class PredicateNodeSelectorTest extends
        NonLogicalNodeSelectorTestCase<PredicateNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    // constants

    private final static Predicate<TestFakeNode> PREDICATE = (n)-> n.name().value().equals("self");

    @Test(expected = NullPointerException.class)
    public void testWithNullPredicateFails() {
        PredicateNodeSelector.with(null);
    }

    @Test
    public void testMatch() {
        final TestFakeNode self = TestFakeNode.node("self");
        this.acceptAndCheck(self, self);
    }

    @Test
    public void testIgnoresNonSelfNodes() {
        final TestFakeNode siblingBefore = TestFakeNode.node("siblingBefore");
        final TestFakeNode self = TestFakeNode.node("self", TestFakeNode.node("child"));
        final TestFakeNode siblingAfter = TestFakeNode.node("siblingAfter");
        final TestFakeNode parent = TestFakeNode.node("parent", siblingBefore, self, siblingAfter);

        this.acceptAndCheck(parent.child(1), self);
    }

    @Test
    public void testToString() {
        assertEquals("*[" + PREDICATE.toString() + "]", this.createSelector().toString());
    }

    @Override
    protected PredicateNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return PredicateNodeSelector.with(PREDICATE);
    }

    @Override
    protected Class<PredicateNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(PredicateNodeSelector.class);
    }
}
