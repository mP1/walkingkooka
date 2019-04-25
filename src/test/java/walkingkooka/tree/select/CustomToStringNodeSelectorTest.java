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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CustomToStringNodeSelectorTest
        extends NodeSelectorTestCase2<CustomToStringNodeSelector<TestNode, StringName, StringName, Object>> {

    private final static String TOSTRING = "CustomToString";

    @Test
    public void testWithSelectorNullFails() {
        assertThrows(NullPointerException.class, () -> {
            CustomToStringNodeSelector.with(null, TOSTRING);
        });
    }

    @Test
    public void testWithToStringNullFails() {
        assertThrows(NullPointerException.class, () -> {
            CustomToStringNodeSelector.with(this.wrapped(), null);
        });
    }

    @Test
    public void testUnnecessaryWrap() {
        final NodeSelector<TestNode, StringName, StringName, Object> wrapped = this.wrapped();
        assertSame(wrapped, CustomToStringNodeSelector.with(wrapped, wrapped.toString()));
    }

    @Test
    public void testWith() {
        final NodeSelector<TestNode, StringName, StringName, Object> wrapped = this.wrapped();
        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> custom = this.createSelector(wrapped);
        assertEquals(wrapped, custom.selector, "selector");
    }

    @Test
    public void testDoubleWrap() {
        final NodeSelector<TestNode, StringName, StringName, Object> wrapped = this.wrapped();
        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> custom = this.createSelector(wrapped, TOSTRING);

        final String toString2 = "CustomToString2";
        final CustomToStringNodeSelector<TestNode, StringName, StringName, Object> again = this.createSelector(custom, toString2);
        assertEquals(wrapped, again.selector, "selector");
        assertEquals(toString2, again.toString(), "toString");
    }

    @Test
    public void testMap() {
        final TestNode parent = TestNode.with("parent",
                TestNode.with("child1"), TestNode.with("child2"), TestNode.with("child3"));

        this.acceptMapAndCheck(NodeSelector.<TestNode, StringName, StringName, Object>firstChild().setToString(TOSTRING),
                parent,
                parent.setChild(0, TestNode.with("child1*0")));
    }

    @Test
    public void testEqualsDifferentWrappedNodeSelector() {
        this.checkNotEquals(CustomToStringNodeSelector.with(NodeSelector.<TestNode, StringName, StringName, Object>self(), TOSTRING));
    }

    @Test
    public void testEqualsDifferentCustomToString() {
        this.checkNotEquals(CustomToStringNodeSelector.with(this.wrapped(), "different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createSelector(), TOSTRING);
    }

    @Override
    CustomToStringNodeSelector<TestNode, StringName, StringName, Object> createSelector() {
        return createSelector(this.wrapped());
    }

    private CustomToStringNodeSelector<TestNode, StringName, StringName, Object> createSelector(final NodeSelector<TestNode, StringName, StringName, Object> wrap) {
        return this.createSelector(wrap, TOSTRING);
    }

    private CustomToStringNodeSelector<TestNode, StringName, StringName, Object> createSelector(final NodeSelector<TestNode, StringName, StringName, Object> wrap,
                                                                                                final String toString) {
        return Cast.to(CustomToStringNodeSelector.with(wrap, toString));
    }

    @Override
    public Class<CustomToStringNodeSelector<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(CustomToStringNodeSelector.class);
    }
}
