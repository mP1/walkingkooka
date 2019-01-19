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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.StringName;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public abstract class NonLogicalNodeSelectorTestCase<S extends NodeSelector<TestFakeNode, StringName, StringName, Object>>
        extends NodeSelectorTestCase<S>{

    NonLogicalNodeSelectorTestCase(){
        super();
    }

    @Test
    public final void testEqualsDifferentSelector() {
        this.checkNotEquals(this.createSelector(NodeSelector.parent()));
    }

    final void acceptAndCheck0(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector,
                               final TestFakeNode start,
                               final String... nodes) {
        this.acceptAndCheckRequiringOrder(selector, start, nodes);
        this.acceptAndCheckUsingContext(selector, start, nodes);
    }

    final void acceptAndCheckRequiringOrder(final NodeSelector<TestFakeNode, StringName, StringName, Object> selector,
                                            final TestFakeNode start,
                                            final String[] nodes) {
        final Set<TestFakeNode> selected = Sets.ordered();
        selector.accept(start, context((n)->{}, (n)->selected.add(n)));
        final List<String> selectedNames = selected
                .stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals("names of selected nodes", Lists.of(nodes), selectedNames);
    }

    final NodeSelector<TestFakeNode,
            StringName,
            StringName,
            Object> wrapped() {
        return NodeSelector.terminal();
    }

    final S createSelector(final NodeSelector<TestFakeNode,
            StringName,
            StringName,
            Object> selector) {
        return Cast.to(this.createSelector().append(selector));
    }
}
