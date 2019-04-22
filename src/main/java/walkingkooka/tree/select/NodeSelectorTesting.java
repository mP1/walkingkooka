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
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.tree.Node;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface NodeSelectorTesting<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE> {

    @Test
    default void testSelectorSelf() {
        final N node = this.createNode();
        this.selectorAcceptAndCheckCount(node, node.selector(), 1);
    }

    @Test
    default void testSelectorPotentialFails() {
        final N node = this.createNode();
        final NodeSelector<N, NAME, ANAME, AVALUE> selector = node.selector();
        assertThrows(UnsupportedOperationException.class, () -> {
            selector.accept(node,
                    this.nodeSelectorContext(
                            (n) -> {
                                throw new UnsupportedOperationException();
                            },
                            (n) -> {
                            }));
        });
    }

    N createNode();

    default void selectorAcceptAndCheck(final N node,
                                        final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                        final N... expected) {
        final List<N> selected = this.selectorAcceptAndCollect(node, selector);
        assertEquals(Sets.of(expected),
                new LinkedHashSet<>(selected),
                "incorrect nodes selected, selector:\n" + selector);
    }

    default void selectorAcceptAndCheckCount(final N node,
                                             final NodeSelector<N, NAME, ANAME, AVALUE> selector,
                                             final int count) {
        final List<N> selected = this.selectorAcceptAndCollect(node, selector);
        assertEquals(count,
                selected.size(),
                "incorrect number of matched node for selector\n" + selected);
    }

    default List<N> selectorAcceptAndCollect(final N node,
                                             final NodeSelector<N, NAME, ANAME, AVALUE> selector) {
        final Set<N> selected = Sets.ordered();
        selector.accept(node,
                this.nodeSelectorContext(
                        (n) -> {
                        }, // ignore potentials
                        (n) -> selected.add(n))); // capture selecteds

        return selected.stream()
                .collect(Collectors.toList());
    }

    default NodeSelectorContext<N, NAME, ANAME, AVALUE> nodeSelectorContext(final Consumer<N> potential,
                                                                            final Consumer<N> selected) {
        return new FakeNodeSelectorContext<N, NAME, ANAME, AVALUE>() {
            @Override
            public void potential(final N node) {
                potential.accept(node);
            }

            @Override
            public void selected(final N node) {
                selected.accept(node);
            }
        };
    }
}
