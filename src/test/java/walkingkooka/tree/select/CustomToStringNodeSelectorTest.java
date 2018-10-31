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
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class CustomToStringNodeSelectorTest
        extends NodeSelectorTestCase2<CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    private final static String TOSTRING = "CustomToString";

    @Test(expected = NullPointerException.class)
    public void testWithSelectorNullFails() {
        CustomToStringNodeSelector.with(null, TOSTRING);
    }

    @Test(expected = NullPointerException.class)
    public void testWithToStringNullFails() {
        CustomToStringNodeSelector.with(this.wrapped(), null);
    }

    @Test
    public void testUnnecessaryWrap() {
        final FakeNodeSelector wrapped = this.wrapped();
        assertSame(wrapped, CustomToStringNodeSelector.with(wrapped, wrapped.toString()));
    }

    @Test
    public void testWith() {
        final FakeNodeSelector wrapped = this.wrapped();
        final CustomToStringNodeSelector custom = this.createSelector(wrapped);
        assertEquals("selector", wrapped, custom.selector);
    }

    @Test
    public void testDoubleWrap() {
        final FakeNodeSelector wrapped = this.wrapped();
        final CustomToStringNodeSelector custom = this.createSelector(wrapped, TOSTRING);

        final String toString2 = "CustomToString2";
        final CustomToStringNodeSelector again = this.createSelector(wrapped, toString2);
        assertEquals("selector", wrapped, again.selector);
        assertEquals("toString", toString2, again.toString());
    }

    @Test
    public void testToString() {
        assertEquals(TOSTRING, this.createSelector().toString());
    }

    @Override
    protected CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return createSelector(this.wrapped());
    }

    private CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object> wrap) {
        return this.createSelector(wrap, TOSTRING);
    }

    private CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object> wrap,
                                                                                                    final String toString) {
        return Cast.to(CustomToStringNodeSelector.with(wrap, toString));
    }

    private FakeNodeSelector wrapped() {
        return new FakeNodeSelector() {
            @Override
            NodeSelector<TestFakeNode, StringName, StringName, Object> unwrapIfCustomToStringNodeSelector() {
                return this;
            }
        };
    }

    @Override
    protected Class<CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(CustomToStringNodeSelector.class);
    }
}
