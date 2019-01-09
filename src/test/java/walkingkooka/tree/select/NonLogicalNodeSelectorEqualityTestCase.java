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
import walkingkooka.naming.StringName;

public abstract class NonLogicalNodeSelectorEqualityTestCase<S extends NonLogicalNodeSelector<TestFakeNode,
        StringName,
        StringName,
        Object>> extends NodeSelectorEqualityTestCase<S> {

    NonLogicalNodeSelectorEqualityTestCase() {
        super();
    }

    @Test
    public final void testDifferentSelector() {
        this.checkNotEquals(this.createNodeSelector(NodeSelector.parent()));
    }

    @Override
    final S createNodeSelector() {
        return this.createNodeSelector(this.wrapped());
    }

    final NodeSelector<TestFakeNode,
            StringName,
            StringName,
            Object> wrapped() {
        return NodeSelector.terminal();
    }

    abstract S createNodeSelector(final NodeSelector<TestFakeNode,
            StringName,
            StringName,
            Object> selector);
}
