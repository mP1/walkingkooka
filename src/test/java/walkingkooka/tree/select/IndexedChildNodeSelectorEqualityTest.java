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

public final class IndexedChildNodeSelectorEqualityTest extends NonLogicalNodeSelectorEqualityTestCase<IndexedChildNodeSelector<TestFakeNode, StringName, StringName, Object>> {

    private final static int INDEX = 2;

    @Test
    public void testDifferentIndex() {
        this.createNodeSelector(INDEX + 1, this.wrapped());
    }

    @Override
    IndexedChildNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return this.createNodeSelector(INDEX, selector);
    }

    private IndexedChildNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector(final int index,
                                                                                                      final NodeSelector<TestFakeNode, StringName, StringName, Object> selector) {
        return Cast.to(IndexedChildNodeSelector.<TestFakeNode, StringName, StringName, Object>with(index).append(selector));
    }
}
