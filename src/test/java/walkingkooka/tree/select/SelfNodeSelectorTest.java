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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

final public class SelfNodeSelectorTest
        extends UnaryNodeSelectorTestCase<SelfNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    @Test
    public void testSelf() {
        final TestFakeNode node = TestFakeNode.node("self");
        this.acceptAndCheck(node, node);
    }

    @Test
    public void testToString() {
        Assert.assertEquals(".", this.createSelector().toString());
    }

    @Override
    protected SelfNodeSelector<TestFakeNode, StringName, StringName, Object> createSelector() {
        return SelfNodeSelector.get();
    }

    @Override
    protected Class<SelfNodeSelector<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(SelfNodeSelector.class);
    }
}
