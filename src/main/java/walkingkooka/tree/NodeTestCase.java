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
import walkingkooka.naming.Name;
import walkingkooka.test.ClassTestCase;

import java.util.Optional;

abstract public class NodeTestCase<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        ClassTestCase<N> {

    protected NodeTestCase() {
        super();
    }

    @Test
    final public void testNameCached() {
        final N node = this.createNode();
        this.checkCached(node, "name", node.name(), node.name());
    }

    @Test
    final public void testParentCached() {
        final N node = this.createNode();
        this.checkCached(node, "parent", node.parent(), node.parent());
    }

    private <T> void checkCached(final N node, final String property, final T value, final T value2) {
        if (value != value2) {
            failNotSame(node + " did not cache " + property, value, value2);
        }
    }

    @Test
    final public void testChildrenIndices() {
        this.childrenCheck(this.createNode());
    }

    @Test
    public void testEqualsNull(){
        assertNotEquals(this.createNode(), null);
    }

    @Test
    public void testEqualsObject(){
        assertNotEquals(this.createNode(), new Object());
    }

    @Test
    public final void testEqualsSameInstance(){
        final N node = this.createNode();
        assertEquals(node, node);
    }

    @Test
    public void testEquals() {
        assertEquals(this.createNode(), this.createNode());
    }

    abstract protected N createNode();

    protected final void childrenCheck(final Node<?, ?, ?, ?> node) {
        final Optional<Node> nodeAsParent = Optional.of(node);

        int i = 0;
        for(Node<?, ?, ?, ?> child : node.children()){
            assertEquals("Incorrect index of " + child, i, child.index());
            assertEquals("Incorrect parent of child " + i + "=" + child, nodeAsParent, child.parent());

            this.childrenCheck(child);
            i++;
        }
    }
}
