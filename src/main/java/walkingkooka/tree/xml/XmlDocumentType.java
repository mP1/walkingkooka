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

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The root node of a document tree.
 */
public final class XmlDocumentType extends XmlLeafNode implements HasXmlPublicId, HasXmlSystemId {

    static XmlDocumentType with(final Node documentType) {
        return new XmlDocumentType(documentType);
    }

    private XmlDocumentType(final Node documentType) {
        super(documentType);
    }

    final org.w3c.dom.DocumentType documentTypeNode() {
        return Cast.to(this.node);
    }

    /**
     * A {@link Map} containing the general entities, both external and internal, declared in the DTD.
     */
    public Map<XmlName, XmlEntity> entities() {
        if (null == this.entities) {
            final DocumentType documentType = this.documentTypeNode();
            this.entities = XmlEntityMap.from(null != documentType ? documentType.getEntities() : null);
        }
        return this.entities;
    }

    private Map<XmlName, XmlEntity> entities;


    /**
     * A {@link Map} view of notations present.
     */
    public Map<XmlName, XmlNotation> notations() {
        if (null == this.notations) {
            final DocumentType documentType = this.documentTypeNode();
            this.notations = XmlNotationMap.from(null != documentType ? documentType.getNotations() : null);
        }
        return this.notations;
    }

    private Map<XmlName, XmlNotation> notations;

    // HasPublicId .........................................................................................................

    @Override
    public Optional<XmlPublicId> publicId() {
        if (null == this.publicId) {
            final DocumentType documentType = this.documentTypeNode();
            this.publicId = null != documentType ? XmlPublicId.with(documentType.getPublicId()) : NO_PUBLIC_ID;
        }
        return this.publicId;
    }

    Optional<XmlPublicId> publicId;

    // HasSystemId .........................................................................................................

    @Override
    public Optional<XmlSystemId> systemId() {
        if (null == this.systemId) {
            final DocumentType documentType = this.documentTypeNode();
            this.systemId = null != documentType ? XmlSystemId.with(documentType.getSystemId()) : NO_SYSTEM_ID;
        }
        return this.systemId;
    }

    Optional<XmlSystemId> systemId;

    // internal subset .........................................................................................................

    public String internalSubset() {
        return this.documentTypeNode().getInternalSubset();
    }

    // Parent ......................................................................................................

    @Override
    public XmlDocumentType removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    // XmlNode......................................................................................................

    @Override
    XmlNode wrap0(final Node node) {
        throw new UnsupportedOperationException();
    }

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.DOCUMENT_TYPE;
    }

    @Override
    public boolean isDocumentType() {
        return true;
    }

    // Node .........................................................................................................

    @Override
    public Optional<XmlNode> previousSibling() {
        return Optional.empty();
    }

    @Override
    public Optional<XmlNode> nextSibling() {
        return Optional.empty();
    }

    // toSearchNode...............................................................................................

    @Override
    SearchNode toSearchNode0() {
        final List<SearchNode> searchNodes = Lists.array();

        this.publicId.ifPresent((p) -> {
            searchNodes.add(p.toSearchNode());
        });
        this.systemId.ifPresent((s) -> {
            searchNodes.add(s.toSearchNode());
        });

        return SearchNode.sequence(searchNodes);
    }

    @Override
    SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("DocType");

    // Object...............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.name());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlNode;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final XmlNode other) {
        return this.node.isEqualNode(other.node);
    }

    /**
     * <pre>
     * <?xml version="1.0"?>
     * <!DOCTYPE root [
     *         <!ELEMENT root (#PCDATA)>
     *         <!ENTITY magic "Abc 123">
     *         ]>
     * <root>&magic;</root>
     * </pre>
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.append("<!DOCTYPE ");
        builder.value(this.name().value());

        buildToString(this.publicId(), this.systemId(), builder);

        // entities...
//        <!DOCTYPE
//        ex SYSTEM "ex.dtd" [ <!ENTITY foo "foo"> <!ENTITY bar
//        "bar"> <!ENTITY bar "bar2"> <!ENTITY % baz "baz">
//        ]> <ex/>

        builder.surroundValues("[", "]");
        builder.value(this.entities());

        builder.surroundValues("[", "]");
        builder.value(this.notations());

        builder.value(this.children());

        builder.append(">");
    }
}
