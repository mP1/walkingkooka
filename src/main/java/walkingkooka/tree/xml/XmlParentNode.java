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
import walkingkooka.tree.search.SearchNode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Base class for both element and document root nodes which are the only nodes that allow children.
 */
abstract class XmlParentNode extends XmlNode {

    /**
     * Package private to limit sub classing.
     */
    XmlParentNode(final org.w3c.dom.Node node) {
        super(node);
        this.children = XmlNodeChildList.wrap(node, this);
    }

    // parent................................................................................................

    @Override
    final XmlNode removeParent1() {
        return this.wrap0(this.nodeCloneWithoutParentWithChildren());
    }

    // children................................................................................................

    @Override
    public final List<XmlNode> children() {
        return this.children;
    }

    final List<XmlNode> children;

    final XmlNode setChildren0(final List<XmlNode> children) {
        Objects.requireNonNull(children, "children");

        // if the new children are the same (ignoring this as parent) do nothing.
        return this.equalsDescendants0(children) ? this : this.replaceChildren(children);
    }

    abstract XmlNode replaceChildren(final List<XmlNode> children);

    // XmlNode..............................................................................................

    /**
     * Copies the node, ignoring its parent but cloning its children.
     */
    final org.w3c.dom.Node nodeCloneWithoutParentWithChildren() {
        final org.w3c.dom.Node copy = this.node.cloneNode(false);
        cloneChildren(this.node, copy);
        return copy;
    }

    private static org.w3c.dom.Node cloneWithoutParentWithChildren(final org.w3c.dom.Node source) {
        final org.w3c.dom.Node copy = source.cloneNode(false);
        cloneChildren(source, copy);
        return copy;
    }

    private static void cloneChildren(final org.w3c.dom.Node source, final org.w3c.dom.Node dest) {
        final NodeList children = source.getChildNodes();
        final int count = children.getLength();
        for (int i = 0; i < count; i++) {
            dest.appendChild(cloneWithoutParentWithChildren(children.item(i)));
        }
    }

    // toSearchNode...............................................................................................

    /**
     * Creates a {@link SearchNode#sequence(List)} with all the children converted.
     */
    final SearchNode toSearchNode1() {
        return this.children.isEmpty() ?
                EMPTY_SEARCH_NODE :
                SearchNode.sequence(this.children.stream()
                        .map(c -> c.toSearchNode())
                        .collect(Collectors.toList()));
    }

    private final static SearchNode EMPTY_SEARCH_NODE = SearchNode.text("", "");
}
