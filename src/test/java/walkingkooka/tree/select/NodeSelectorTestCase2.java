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

import org.junit.jupiter.api.Assertions;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class NodeSelectorTestCase2<S extends NodeSelector<TestNode, StringName, StringName, Object>>
extends NodeSelectorTestCase<S>{

    NodeSelectorTestCase2(){
        super();
    }

    final void acceptAndCheck0(final NodeSelector<TestNode, StringName, StringName, Object> selector,
                               final TestNode start,
                               final String... nodes) {
        this.acceptAndCheckRequiringOrder(selector, start, nodes);
        this.acceptAndCheckUsingContext(selector, start, nodes);
    }

    final void acceptAndCheckRequiringOrder(NodeSelector<TestNode, StringName, StringName, Object> selector, TestNode start, String[] nodes) {
        final List<String> expected = Lists.array();
        expected.addAll(Lists.of(nodes));

        selector.accept0(start, new FakeNodeSelectorContext<TestNode, StringName, StringName, Object>() {

            @Override
            public void potential(final TestNode node) {
                // nop
            }

            int i = 0;

            @Override
            public void selected(final TestNode node) {
                if(i == nodes.length) {
                    Assertions.fail("Unexpected matching node: " + node);
                }

                if(!nodes[i].equals(node.name().value())){
                    Assertions.fail("Unexpected node " + node + " expected "+ nodes[i]);
                }
                expected.remove(0);
                i++;
            }
        });

        assertEquals(Lists.empty(), expected, "Finished processing contains unselected nodes");
    }

    final NodeSelector<TestNode, StringName, StringName, Object> wrapped() {
        return NodeSelector.terminal();
    }
}
