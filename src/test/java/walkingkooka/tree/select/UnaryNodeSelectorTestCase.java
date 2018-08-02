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

import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UnaryNodeSelectorTestCase<S extends NodeSelector<TestFakeNode, StringName, StringName, Object>>
extends NodeSelectorTestCase<S>{

    UnaryNodeSelectorTestCase(){
        super();
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
        final List<String> actual = selector.accept(start, selector.nulObserver())
                .stream()
                .map(n -> n.name().value())
                .collect(Collectors.toList());
        assertEquals("names of matched nodes", Lists.of(nodes), actual);
    }
}
