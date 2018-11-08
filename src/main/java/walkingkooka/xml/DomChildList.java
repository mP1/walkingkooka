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

import org.w3c.dom.NodeList;
import walkingkooka.collect.list.Lists;

import java.util.AbstractList;
import java.util.List;
import java.util.Optional;

/**
 * A read only {@link List} that lazily auto wraps children as they are fetched.
 */
final class DomChildList extends AbstractList<DomNode> {

    static List<DomNode> wrap(final org.w3c.dom.Node node, final DomNode parent) {
        final NodeList children = node.getChildNodes();
        return children.getLength() == 0 ? Lists.empty() : new DomChildList(parent, children.getLength());
    }

    private DomChildList(final DomNode parent, final int childCount) {
        this.parent = parent;
        this.children = new DomNode[childCount];
    }

    private final DomNode parent;

    /**
     * Initially the array will be filled with nulls and wrappers created lazily.
     */
    private final DomNode[] children;

    @Override
    public DomNode get(final int index) {
        final int count = this.size();
        if (index < 0 || index >= count) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " must be between 0 and " + count);
        }
        DomNode child = this.children[index];
        if (null == child) {
            child = DomNode.wrap(this.parent.node.getChildNodes().item(index));
            child.parent = Optional.of(this.parent);
            child.index = index;
            this.children[index] = child;
        }
        return child;
    }

    /**
     * This method exists and is only used by {@link DomDocument}.
     * It scans all children for the first node of the given node type,
     * using an existing wrapper or creating one as necessary.
     */
    final Optional<DomNode> getElementByNodeType(final int nodeType) {
        Optional<DomNode> result = Optional.empty();

        // while
        final NodeList nodeList = this.parent.node.getChildNodes();
        for (int i = 0; i < this.children.length; i++) {
            final org.w3c.dom.Node possible = nodeList.item(i);
            if (nodeType == possible.getNodeType()) {
                DomNode node = this.children[i];
                if (null == node) {
                    node = DomNode.wrap(possible);
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
