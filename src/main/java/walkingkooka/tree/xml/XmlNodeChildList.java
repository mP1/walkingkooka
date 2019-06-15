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
import walkingkooka.collect.list.Lists;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;

/**
 * A read only {@link List} that lazily auto wraps children as they are fetched.
 */
final class XmlNodeChildList extends AbstractList<XmlNode> {

    static {
        Lists.registerImmutableType(XmlNodeChildList.class);
    }

    static List<XmlNode> wrap(final org.w3c.dom.Node node, final XmlNode parent) {
        final NodeList children = node.getChildNodes();
        return children.getLength() == 0 ? Lists.empty() : new XmlNodeChildList(parent, children.getLength());
    }

    private XmlNodeChildList(final XmlNode parent, final int childCount) {
        this.parent = parent;
        this.children = new XmlNode[childCount];
    }

    private final XmlNode parent;

    /**
     * Initially the array will be filled with nulls and wrappers created lazily.
     */
    private final XmlNode[] children;

    @Override
    public XmlNode get(final int index) {
        final int count = this.size();
        if (index < 0 || index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " must be between 0 and " + count);
        }
        XmlNode child = this.children[index];
        if (null == child) {
            child = XmlNode.wrap(this.parent.node.getChildNodes().item(index));
            child.parent = Optional.of(this.parent);
            child.index = index;
            this.children[index] = child;
        }
        return child;
    }

    /**
     * This method exists and is only used by {@link XmlDocument}.
     * It scans all children for the first node of the given node type,
     * using an existing wrapper or creating one as necessary.
     */
    final Optional<XmlNode> getElementByNodeType(final int nodeType) {
        Optional<XmlNode> result = Optional.empty();

        // while
        final NodeList nodeList = this.parent.node.getChildNodes();
        for (int i = 0; i < this.children.length; i++) {
            final org.w3c.dom.Node possible = nodeList.item(i);
            if (nodeType == possible.getNodeType()) {
                XmlNode node = this.children[i];
                if (null == node) {
                    node = XmlNode.wrap(possible);
                    node.parent = Optional.of(this.parent);
                    node.index = i;
                    this.children[i] = node;
                }
                result = Optional.of(node);
            }
        }

        return result;
    }

    @Override
    public int size() {
        return this.children.length;
    }
}
