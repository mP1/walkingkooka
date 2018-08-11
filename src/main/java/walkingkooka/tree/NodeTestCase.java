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
import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.visit.VisitableTestCase;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

abstract public class NodeTestCase<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        VisitableTestCase<N> {

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
    final public void testRootWithoutParent() {
        final N node = this.createNode();
        assertEquals("node must have no parent", Optional.empty(), node.parent());
        assertSame(node, node.root());
    }

    @Test
    final public void testChildrenIndices() {
        this.childrenCheck(this.createNode());
    }

    @Test
    public void testFirstChild() {
        final N node = this.createNode();
        final List<N> children = node.children();
        final Optional<N> first = node.firstChild();
        if(children.isEmpty()){
           assertEquals("childless node must not have a first child.", Optional.empty(), first);
        } else {
           assertEquals("node with children must have a first child.", Optional.of(children.get(0)), first);
        }
    }

    @Test
    public void testLastChild() {
        final N node = this.createNode();
        final List<N> children = node.children();
        final Optional<N> last = node.lastChild();
        if(children.isEmpty()){
            assertEquals("childless node must not have a last child.", Optional.empty(), last);
        } else {
            assertEquals("node with children must have a last child.", Optional.of(children.get(children.size()-1)), last);
        }
    }

    @Test
    public final void testSelector() {
        final N node = this.createNode();
        final NodeSelector<N, NAME, ANAME, AVALUE> selector = node.selector();
        final Set<N> matches = selector.accept(node, selector.nulObserver());
        assertEquals("Node's own selector should have matched only itself", Sets.of(node), matches);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testSelectorObserverThrow() {
        final N node = this.createNode();
        final NodeSelector<N, NAME, ANAME, AVALUE> selector = node.selector();
        selector.accept(node, (n) -> { throw new UnsupportedOperationException();});
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

    protected final void checkWithoutParent(final N node) {
        assertEquals("parent", Optional.empty(), node.parent());
        assertEquals("root", true, node.isRoot());
    }

    protected final void checkWithParent(final N node) {
        assertNotEquals("parent", Optional.empty(), node.parent());
        assertEquals("root", false, node.isRoot());
    }
}
