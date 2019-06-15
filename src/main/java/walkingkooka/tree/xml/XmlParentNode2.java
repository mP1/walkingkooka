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

import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Base class for both element and document root nodes which are the only nodes that allow children.
 */
abstract class XmlParentNode2 extends XmlParentNode {

    /**
     * Package private to limit sub classing.
     */
    XmlParentNode2(final org.w3c.dom.Node node) {
        super(node);
    }

    // children................................................................................................
    @Override
    XmlNode replaceChildren(final List<XmlNode> children) {
        final int childCount = children.size();

        final org.w3c.dom.Node replacedNode = this.nodeCloneAll();

        // remove the cloned nodes.
        final NodeList oldChildren = replacedNode.getChildNodes();
        final int oldChildCount = oldChildren.getLength();
        for (int i = 0; i < oldChildCount; i++) {
            replacedNode.removeChild(oldChildren.item(oldChildCount - 1 - i));
        }

        // adopt the clones of nodes of $children.
        final org.w3c.dom.Document document = replacedNode.getOwnerDocument();
        for (int i = 0; i < childCount; i++) {
            final org.w3c.dom.Node child = children.get(i).nodeCloneWithoutParentWithChildren();
            document.adoptNode(child);
            replacedNode.appendChild(child);
        }

        return this.wrap0(replacedNode);
    }

    @Override
    public final XmlDocument document() {
        XmlDocument document = this.document;
        if (null == document) {
            this.document = XmlDocument.with(this.documentNode());
        }
        return this.document;
    }

    private XmlDocument document;

    // XmlNode................................................................................................

    @Override
    final org.w3c.dom.Document documentNode0() {
        return this.documentNode1();
    }

    @Override
    final org.w3c.dom.Node nodeCloneAll() {
        return this.nodeCloneAll0();
    }
}
