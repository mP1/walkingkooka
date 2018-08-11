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

package walkingkooka.xml;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public abstract class DomParentNodeTestCase<N extends DomParentNode> extends DomNodeTestCase<N> {

    final void checkChildren(final String label, final int count, final DomNode parent) {
        if(null!=parent.node) {
            assertEquals(label + " child node count=" + parent, count, parent.node.getChildNodes().getLength());
        }
        assertEquals(label + " child DomNode count=" + parent, count, parent.children().size());

        final org.w3c.dom.Node parentNode = parent.node;

        final List<DomNode> children= parent.children();
        for(int i = 0; i <count; i++) {
            final DomNode child = children.get(i);
            assertSame("child of "+ label+ " has wrong parent", parent, child.parent().get());
            if(null!=parentNode) {
                assertSame("parent node of child is wrong", parentNode, child.node.getParentNode());
            }
            assertEquals("child index=" + child, i, child.index());
        }
        assertEquals("children of "+ label, children, parent.children());
    }

    final void checkNotAdopted(final String label, final DomNode node) {
        assertEquals(label + " should still have no parent", DomNode.NO_PARENT, node.parent());
        assertEquals(label + " should still have no parent node", null, node.node.getParentNode());
    }
}
