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
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Name;
import walkingkooka.naming.PathSeparator;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.NodeSelectorContext;
import walkingkooka.tree.select.NodeSelectorContexts;
import walkingkooka.tree.visit.VisitableTestCase;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

abstract public class NodeTestCase<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        VisitableTestCase<N>
        implements HashCodeEqualsDefinedTesting<N> {

    protected NodeTestCase() {
        super();
    }

    @Test
    public void testCheckNaming() {
        this.checkNamingStartAndEnd(this.requiredNamePrefix(), Node.class);
    }

    abstract protected String requiredNamePrefix();

    @Test
    public final void testPathSeparatorConstant() throws Exception {
        final Field field = this.type().getField("PATH_SEPARATOR");

        assertEquals("PATH_SEPARATOR constant must be public=" + field, MemberVisibility.PUBLIC, MemberVisibility.get(field));
        assertEquals("PATH_SEPARATOR constant must be static=" + field, true, FieldAttributes.STATIC.is(field));
        assertEquals("PATH_SEPARATOR constant type=" + field, PathSeparator.class, field.getType());
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
        assertSame(node + " did not cache " + property, value, value2);
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
        final Set<N> selected = Sets.ordered();
        selector.accept(node,
                this.nodeSelectorContext(
                        (n)->{},
                        (n)-> selected.add(n)));
        assertEquals("Node's own select should have matched only itself", Sets.of(node), selected);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testSelectorPotentialFails() {
        final N node = this.createNode();
        final NodeSelector<N, NAME, ANAME, AVALUE> selector = node.selector();
        selector.accept(node,
                this.nodeSelectorContext(
                        (n) -> { throw new UnsupportedOperationException();},
                        (n)->{}));
    }

    private NodeSelectorContext<N, NAME, ANAME, AVALUE> nodeSelectorContext(final Consumer<N> potential,
                                                                            final Consumer<N> selected) {
        return NodeSelectorContexts.basic(
                potential,
                selected,
                (n) -> {throw new UnsupportedOperationException();},
                Converters.fake(),
                DecimalNumberContexts.fake());
    }

    @Test
    public void testPropertiesNeverReturnNull() throws Exception {
        propertiesNeverReturnNullCheck(this.createNode());
    }

    abstract protected N createNode();

    @Override
    public final N createObject() {
        return this.createNode();
    }

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
