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

import org.junit.jupiter.api.Test;
import walkingkooka.naming.Name;
import walkingkooka.naming.PathSeparator;
import walkingkooka.test.BeanPropertiesTesting;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.select.NodeSelectorTesting;
import walkingkooka.tree.visit.VisitableTestCase;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

abstract public class NodeTestCase<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        VisitableTestCase<N>
        implements HashCodeEqualsDefinedTesting<N>,
        NodeSelectorTesting<N, NAME, ANAME, AVALUE>,
        ToStringTesting<N>,
        TypeNameTesting<N> {

    protected NodeTestCase() {
        super();
    }

    @Test
    public final void testPathSeparatorConstant() throws Exception {
        final Field field = this.type().getField("PATH_SEPARATOR");

        assertEquals(MemberVisibility.PUBLIC, MemberVisibility.get(field), () -> "PATH_SEPARATOR constant must be public=" + field);
        assertEquals(true, FieldAttributes.STATIC.is(field), () -> "PATH_SEPARATOR constant must be static=" + field);
        assertEquals(PathSeparator.class, field.getType(), () -> "PATH_SEPARATOR constant type=" + field);
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
        assertSame(value, value2, ()-> node + " did not cache " + property);
    }

    @Test
    final public void testRootWithoutParent() {
        final N node = this.createNode();
        assertEquals(Optional.empty(), node.parent(), "node must have no parent");
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
           assertEquals(Optional.empty(), first, "childless node must not have a first child.");
        } else {
           assertEquals(Optional.of(children.get(0)), first, "node with children must have a first child.");
        }
    }

    @Test
    public void testLastChild() {
        final N node = this.createNode();
        final List<N> children = node.children();
        final Optional<N> last = node.lastChild();
        if(children.isEmpty()){
            assertEquals(Optional.empty(), last, "childless node must not have a last child.");
        } else {
            assertEquals(Optional.of(children.get(children.size()-1)), last, "node with children must have a last child.");
        }
    }

    @Test
    public void testPropertiesNeverReturnNull() throws Exception {
        BeanPropertiesTesting.allPropertiesNeverReturnNullCheck(this.createNode());
    }

    abstract public N createNode();

    @Override
    public final N createObject() {
        return this.createNode();
    }

    protected final void childrenCheck(final Node<?, ?, ?, ?> node) {
        final Optional<Node> nodeAsParent = Optional.of(node);

        int i = 0;
        for(Node<?, ?, ?, ?> child : node.children()){
            assertEquals(i, child.index(), () -> "Incorrect index of " + child);
            final int j = i;
            assertEquals(nodeAsParent, child.parent(), () -> "Incorrect parent of child " + j + "=" + child);

            this.childrenCheck(child);
            i++;
        }
    }

    protected final void checkWithoutParent(final N node) {
        assertEquals(Optional.empty(), node.parent(), "parent");
        assertEquals(true, node.isRoot(), "root");
    }

    protected final void checkWithParent(final N node) {
        assertNotEquals(Optional.empty(), node.parent(), "parent");
        assertEquals(false, node.isRoot(), "root");
    }

    @Override
    public String typeNameSuffix() {
        return Node.class.getSimpleName();
    }
}
