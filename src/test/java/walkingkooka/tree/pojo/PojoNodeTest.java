/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
package walkingkooka.tree.pojo;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.NodeSelectorTesting;
import walkingkooka.type.JavaVisibility;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PojoNodeTest implements ClassTesting2<PojoNode>,
        NodeSelectorTesting<PojoNode, PojoName, PojoNodeAttributeName, Object> {

    @Test
    public void testSelectorNodeByClassName() {
        final TestBean bean = new TestBean("1", "2", 99, "3");

        final NodeSelector<PojoNode, PojoName, PojoNodeAttributeName, Object> selector = PojoNode.absoluteNodeSelector()
                .descendant()
                .attributeValueEquals(PojoNodeAttributeName.CLASS, String.class.getName());

        final PojoNode node = PojoNode.wrap(PojoName.property("TestBean"),
                bean,
                new ReflectionPojoNodeContext());
        final List<PojoNode> selected = this.selectorApplyAndCollect(node, selector);
        assertEquals(Sets.of("1", "2", "3"),
                selected.stream()
                        .map(n -> n.value())
                        .collect(Collectors.toCollection(TreeSet::new)));
    }

    @Override
    public PojoNode createNode() {
        return PojoNode.wrap(PojoName.property("TestBean"),
                new TestBean("1", "2", 99, "3"),
                new ReflectionPojoNodeContext());
    }

    @Override
    public Class<PojoNode> type() {
        return PojoNode.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    private class TestBean {

        TestBean(final Object x, final Object... list) {
            this.x = x;
            this.list = Lists.of(list);
        }

        final Object x;

        public Object getX() {
            return x;
        }

        final List<Object> list;

        public Object getList() {
            return this.list;
        }

        public String toString() {
            return this.getX().toString();
        }
    }
}
