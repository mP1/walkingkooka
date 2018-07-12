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
package walkingkooka.tree;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.test.PublicClassTestCase;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class NodeTest extends PublicClassTestCase<Node> {

    @Test
    public void testRoot() {
        final TestFakeNode child1 = new TestFakeNode("child1");
        final TestFakeNode child2 = new TestFakeNode("child2");
        final TestFakeNode parent1 = new TestFakeNode("parent1", child1, child2);
        final TestFakeNode parent2 = new TestFakeNode("parent2");
        final TestFakeNode root = new TestFakeNode("root", parent1, parent2);

        assertEquals(root, child1.root());
        assertEquals(root, child2.root());
        assertEquals(root, parent2.root());
        assertEquals(root, root);
    }

    static final class TestFakeNode extends FakeNode<TestFakeNode, StringName, Name, Object>{

        TestFakeNode(final String name, final TestFakeNode...children) {
            this.name = Names.string(name);
            this.children = Lists.of(children);
            this.children.stream().forEach( n -> n.parent = Optional.of(this));
        }

        public StringName name() {
            return this.name;
        }

        private final StringName name;

        @Override
        public Optional<TestFakeNode> parent() {
            return this.parent;
        }

        private Optional<TestFakeNode> parent = Optional.empty();

        public List<TestFakeNode> children() {
            return this.children;
        }

        private List<TestFakeNode> children;

        public int hashCode() {
            return Objects.hash(this.name(), this.children());
        }

        public boolean equals(final Object other) {
            return this == other || other instanceof TestFakeNode && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestFakeNode other) {
            return this.name.equals(other.name) && this.children.equals(other.children);
        }

        public String toString() {
            return this.name.toString();
        }
    }

    @Override
    protected Class<Node> type() {
        return Node.class;
    }
}
