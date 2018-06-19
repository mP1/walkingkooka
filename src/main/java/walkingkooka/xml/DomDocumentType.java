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

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The root node of a document tree.
 */
public final class DomDocumentType extends DomLeafNode implements HasDomPublicId, HasDomSystemId{

    DomDocumentType(final org.w3c.dom.Node documentType) {
        super(documentType);
    }

    final org.w3c.dom.DocumentType documentTypeNode() {
        return Cast.to(this.node);
    }

    /**
     * A {@link Map} containing the general entities, both external and internal, declared in the DTD.
     */
    public Map<DomName, DomEntity> entities() {
        if(null==this.entities) {
            final DocumentType documentType = this.documentTypeNode();
            this.entities = DomEntityMap.from(null != documentType ? documentType.getEntities() : null);
        }
        return this.entities;
    }

    private Map<DomName, DomEntity> entities;


    /**
     * A {@link Map} view of notations present.
     */
    public Map<DomName, DomNotation> notations() {
        if(null==this.notations) {
            final DocumentType documentType = this.documentTypeNode();
            this.notations = DomNotationMap.from(null != documentType ? documentType.getNotations() : null);
        }
        return this.notations;
    }

    private Map<DomName, DomNotation> notations;

    // HasPublicId .........................................................................................................

    @Override
    public Optional<DomPublicId> publicId() {
        if(null == this.publicId) {
            final DocumentType documentType = this.documentTypeNode();
            this.publicId = null != documentType ? DomPublicId.with(documentType.getPublicId()) : NO_PUBLIC_ID;
        }
        return this.publicId;
    }

    Optional<DomPublicId> publicId;

    // HasSystemId .........................................................................................................

    @Override
    public Optional<DomSystemId> systemId() {
        if(null == this.systemId) {
            final DocumentType documentType = this.documentTypeNode();
            this.systemId = null != documentType ? DomSystemId.with(documentType.getSystemId()) : NO_SYSTEM_ID;
        }
        return this.systemId;
    }

    Optional<DomSystemId> systemId;

    // internal subset .........................................................................................................

    public String internalSubset() {
        return this.documentTypeNode().getInternalSubset();
    }

    // Parent ......................................................................................................

    @Override
    public boolean isRoot() {
        return true;
    }

    // DomNode......................................................................................................

    @Override DomNode wrap0(final Node node) {
        throw new UnsupportedOperationException();
    }

    @Override
    DomNodeKind kind() {
        return DomNodeKind.DOCUMENT_TYPE;
    }

    @Override
    public DomDocumentType asDocumentType() {
        return this;
    }

    @Override
    public boolean isDocumentType() {
        return true;
    }

    // Node .........................................................................................................

    @Override
    public Optional<DomNode> previousSibling() {
        return Optional.empty();
    }

    @Override
    public Optional<DomNode> nextSibling() {
        return Optional.empty();
    }

    // Object...............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.name());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DomNode;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final DomNode other) {
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
        builder.append(this.name().value());
        builder.append(" ");

        builder.separator(" ");

        this.buildToString(this.publicId(),this.systemId(), builder);

        // entities...
//        <!DOCTYPE
//        ex SYSTEM "ex.dtd" [ <!ENTITY foo "foo"> <!ENTITY bar
//        "bar"> <!ENTITY bar "bar2"> <!ENTITY % baz "baz">
//        ]> <ex/>

        builder.surroundValues("[", "]");
        builder.value(this.entities());

        builder.value(this.notations());

        builder.value(this.children());

        builder.append(">");
    }
}
