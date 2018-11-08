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

import walkingkooka.test.SkipPropertyNeverReturnsNullCheck;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for all dom nodes except for {@link DomElement}.
 */
abstract class DomLeafNode extends DomNode {

    DomLeafNode(final org.w3c.dom.Node node) {
        super(node);
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
    public final List<DomNode> children() {
        return NO_CHILDREN;
    }

    @Override
    public final DomNode setChildren(final List<DomNode> children) {
        Objects.requireNonNull(children, "children");
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<DomNode> firstChild() {
        return NO_CHILD;
    }

    @Override
    public Optional<DomNode> lastChild() {
        return NO_CHILD;
    }

    private final static Optional<DomNode> NO_CHILD = Optional.empty();

    @Override
    public DomNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }

    // attributes.................................................................................................

    @Override
    public final Map<DomAttributeName, String> attributes() {
        return NO_ATTRIBUTES;
    }

    @Override
    public final DomNode setAttributes(final Map<DomAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");
        throw new UnsupportedOperationException();
    }

    @SkipPropertyNeverReturnsNullCheck(DomDocumentType.class)
    @Override
    public final DomDocument document() {
        DomDocument document = this.document;
        if (null == document) {
            // TODO sometimes a DomDocumentType may have no enclosing document.
            this.document = new DomDocument(this.documentNode());
        }
        return this.document;
    }

    private DomDocument document;
}
