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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for all dom nodes except for {@link XmlElement}.
 */
abstract class XmlLeafNode extends XmlNode {

    XmlLeafNode(final org.w3c.dom.Node node) {
        super(node);
    }

    /**
     * Leaf nodes dont have children or child nodes.
     */
    @Override
    final XmlNode removeParent1() {
        return this.wrap0(this.nodeCloneWithoutParentWithoutChildren());
    }

    @Override
    final org.w3c.dom.Node nodeCloneWithoutParentWithChildren() {
        return this.nodeCloneWithoutParentWithoutChildren();
    }

    @Override
    final org.w3c.dom.Node nodeCloneAll() {
        return this.nodeCloneAll0();
    }

    @Override
    final org.w3c.dom.Document documentNode0() {
        return this.documentNode1();
    }

    // children.................................................................................................

    @Override
    public final List<XmlNode> children() {
        return NO_CHILDREN;
    }

    @Override
    public final XmlNode setChildren(final List<XmlNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<XmlNode> firstChild() {
        return NO_CHILD;
    }

    @Override
    public Optional<XmlNode> lastChild() {
        return NO_CHILD;
    }

    private final static Optional<XmlNode> NO_CHILD = Optional.empty();

    @Override
    public XmlNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }

    // attributes.................................................................................................

    @Override
    public final Map<XmlAttributeName, String> attributes() {
        return NO_ATTRIBUTES;
    }

    @Override
    public final XmlNode setAttributes(final Map<XmlAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");
        throw new UnsupportedOperationException();
    }

    @Override
    public final XmlDocument document() {
        XmlDocument document = this.document;
        if (null == document) {
            // TODO sometimes a XmlDocumentType may have no enclosing document.
            this.document = XmlDocument.with(this.documentNode());
        }
        return this.document;
    }

    private XmlDocument document;
}
