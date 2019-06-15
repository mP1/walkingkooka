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

package walkingkooka.tree.xml;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public abstract class XmlParentNodeTestCase<N extends XmlParentNode> extends XmlNodeTestCase<N> {

    XmlParentNodeTestCase() {
        super();
    }

    // toSearchNode.....................................................................................................

    @Test
    public final void testToSearchNodeWithoutChildren() {
        final N domNode = this.createNode();
        this.checkChildren("children", 0, domNode);
        this.toSearchNodeAndCheck(domNode, SearchNode.text("", "").setName(this.searchNodeName()));
    }

    abstract SearchNodeName searchNodeName();

    // toString.....................................................................................................


    final void checkChildren(final String label, final int count, final XmlNode parent) {
        if (null != parent.node) {
            assertEquals(count, parent.node.getChildNodes().getLength(), () -> label + " child node count=" + parent);
        }
        assertEquals(count, parent.children().size(), () -> label + " child XmlNode count=" + parent);

        final org.w3c.dom.Node parentNode = parent.node;

        final List<XmlNode> children = parent.children();
        for (int i = 0; i < count; i++) {
            final XmlNode child = children.get(i);
            assertSame(parent, child.parentOrFail(), () -> "child of " + label + " has wrong parent");
            if (null != parentNode) {
                assertSame(parentNode, child.node.getParentNode(), "parent node of child is wrong");
            }
            assertEquals(i, child.index(), "child index=" + child);
        }
        assertEquals(children, parent.children(), "children of " + label);
    }

    final void checkNotAdopted(final String label, final XmlNode node) {
        assertEquals(XmlNode.NO_PARENT, node.parent(), label + " should still have no parent");
        assertEquals(null, node.node.getParentNode(), label + " should still have no parent node");
    }
}
