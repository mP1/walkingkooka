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

import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import walkingkooka.Cast;
import walkingkooka.Throwables;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.text.LineEnding;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A dom document, that holds nodes such as a doctype, document element and a tree of nodes.
 */
public final class DomDocument extends DomParentNode{

    DomDocument(final org.w3c.dom.Node node) {
        super(node);
    }

    @Override
    public DomDocument document() {
        return this;
    }

    @Override
    public boolean isDocument() {
        return true;
    }

    // documentType ...................................................................................................

    /**
     * Returns the doc type if one is present.
     */
    public Optional<DomDocumentType> documentType() {
        if(null == this.documentType) {
            this.documentType = this.getChildByNodeType(Node.DOCUMENT_TYPE_NODE);
        }
        return this.documentType;
    }

    Optional<DomDocumentType> documentType;

    // element .........................................................................................................

    /**
     * Returns the root document element if one is present.
     */
    public Optional<DomElement> element() {
        if(null == this.element) {
            this.element = this.getChildByNodeType(Node.ELEMENT_NODE);
        }
        return this.element;
    }

    Optional<DomElement> element;

    <T extends DomNode> Optional<T> getChildByNodeType(final int nodeType){
        return this.children.isEmpty() ? Optional.empty() : this.getChildByNodeType0(nodeType);
    }

    private <T extends DomNode> Optional<T> getChildByNodeType0(final int nodeType){
        final DomChildList children = Cast.to(this.children);
        return Cast.to(children.getElementByNodeType(nodeType));
    }

    /**
     * Would be setter that returns a document with the given element.
     */
    public DomDocument setElement(final Optional<DomElement> element) {
        Objects.requireNonNull(element, "element");

        final Optional<DomElement> previous = this.element();

        return previous.isPresent() && element.isPresent() ? this.replaceElement(previous.get(), element.get()) :
               previous.isPresent() && ! element.isPresent() ? this.removeElement(previous.get()) :
               element.isPresent() ? this.addElement(element.get()) :// previous no element, adding element
               this; // previous no element, new no element -> no change

    }

    private DomDocument replaceElement(final DomElement previous, final DomElement next) {
        return this.replaceChild(previous, next).document();
    }

    private DomDocument removeElement(final DomElement previous) {
        return this.removeChild(previous).document();
    }

    private DomDocument addElement(final DomElement element) {
        return this.appendChild(element);
    }

    
    // canonical form .......................................................................................
    
    public boolean canonicalForm() {
        return DomConfigurationProperty.CANONICAL_FORM.booleanValue(this);
    }
    
    public DomDocument setCanonicalForm(final boolean canonicalForm) {
        return DomConfigurationProperty.CANONICAL_FORM.setBooleanValue(this, canonicalForm);
    }

    // cdata sections .......................................................................................

    public boolean cDataSections() {
        return DomConfigurationProperty.CDATA_SECTIONS.booleanValue(this);
    }

    public DomDocument setCDataSections(final boolean cDataSections) {
        return DomConfigurationProperty.CDATA_SECTIONS.setBooleanValue(this, cDataSections);
    }

    // check character normalizations .......................................................................................

    public boolean checkCharacterNormalizations() {
        return DomConfigurationProperty.CHECK_CHARACTER_NORMALIZATIONS.booleanValue(this);
    }

    public DomDocument setCheckCharacterNormalizations(final boolean checkCharacterNormalizations) {
        return DomConfigurationProperty.CHECK_CHARACTER_NORMALIZATIONS.setBooleanValue(this, checkCharacterNormalizations);
    }

    // comments .......................................................................................

    public boolean comments() {
        return DomConfigurationProperty.COMMENTS.booleanValue(this);
    }

    public DomDocument setComments(final boolean comments) {
        return DomConfigurationProperty.COMMENTS.setBooleanValue(this, comments);
    }
    
    // datatype normalizations .......................................................................................

    public boolean datatypeNormalizations() {
        return DomConfigurationProperty.DATATYPE_NORMALIZATION.booleanValue(this);
    }

    public DomDocument setDatatypeNormalizations(final boolean datatypeNormalizations) {
        return DomConfigurationProperty.DATATYPE_NORMALIZATION.setBooleanValue(this, datatypeNormalizations);
    }
    
    // documentUri .....................................................................................................

    /**
     * Returns the document uri if known.
     */
    public Optional<String> documentUri() {
        if(null==this.documentUri){
            this.documentUri = Optional.ofNullable(this.documentNode().getDocumentURI());
        }
        return this.documentUri;
    }

    private Optional<String> documentUri;

    public DomDocument setDocumentUri(final Optional<String> documentUri) {
        Objects.requireNonNull(documentUri, "documentUri");
        return this.documentUri().equals(documentUri) ? this : this.replaceDocumentUri(documentUri.get());
    }

    private DomDocument replaceDocumentUri(final String documentUri) {
        final org.w3c.dom.Document document = this.nodeCloneAll();
        document.setDocumentURI(documentUri);
        return new DomDocument(document);
    }

    // element content whitespace .......................................................................................

    public boolean elementContentWhitespace() {
        return DomConfigurationProperty.DATATYPE_NORMALIZATION.booleanValue(this);
    }

    public DomDocument setElementContentWhitespace(final boolean elementContentWhitespace) {
        return DomConfigurationProperty.DATATYPE_NORMALIZATION.setBooleanValue(this, elementContentWhitespace);
    }

    // entities .................................................................................................

    public boolean entities() {
        return DomConfigurationProperty.ENTITIES.booleanValue(this);
    }

    public DomDocument setEntities(final boolean entities) {
        return DomConfigurationProperty.ENTITIES.setBooleanValue(this, entities);
    }

    // errorHandler .................................................................................................

    public Optional<DOMErrorHandler> errorHandler() {
        return DomConfigurationProperty.ERROR_HANDLER.errorHandler(this);
    }

    public DomDocument setErrorHandler(final Optional<? extends DOMErrorHandler> errorHandler) {
        return DomConfigurationProperty.ERROR_HANDLER.setErrorHandler(this, errorHandler);
    }

    // infoset .................................................................................................

    public boolean infoset() {
        return DomConfigurationProperty.INFOSET.booleanValue(this);
    }

    public DomDocument setInfoset(final boolean infoset) {
        return DomConfigurationProperty.INFOSET.setBooleanValue(this, infoset);
    }
    
    // inputEncoding ..............................................................................................
    
    /**
     * Returns the input encoding if known.
     */
    public Optional<String> inputEncoding() {
        if(null==this.inputEncoding){
            this.inputEncoding = Optional.ofNullable(this.documentNode().getInputEncoding());
        }
        return this.inputEncoding;
    }

    private Optional<String> inputEncoding;

    // namespaces .................................................................................................

    public boolean namespaces() {
        return DomConfigurationProperty.NAMESPACES.booleanValue(this);
    }

    public DomDocument setNamespaces(final boolean namespaces) {
        return DomConfigurationProperty.NAMESPACES.setBooleanValue(this, namespaces);
    }

    // namespaceDeclaration .................................................................................................

    public boolean namespaceDeclaration() {
        return DomConfigurationProperty.NAMESPACE_DECLARATION.booleanValue(this);
    }

    public DomDocument setNamespaceDeclaration(final boolean namespaceDeclaration) {
        return DomConfigurationProperty.NAMESPACE_DECLARATION.setBooleanValue(this, namespaceDeclaration);
    }

    // normalizeCharacters .................................................................................................

    public boolean normalizeCharacters() {
        return DomConfigurationProperty.NORMALIZE_CHARACTERS.booleanValue(this);
    }

    public DomDocument setNormalizeCharacters(final boolean normalizeCharacters) {
        return DomConfigurationProperty.NORMALIZE_CHARACTERS.setBooleanValue(this, normalizeCharacters);
    }

    // schemaLocation .................................................................................................

    public Optional<String> schemaLocation() {
        return DomConfigurationProperty.SCHEMA_LOCATION.string(this);
    }

    public DomDocument setSchemaLocation(final Optional<String> schemaLocation) {
        return DomConfigurationProperty.SCHEMA_LOCATION.setString(this, schemaLocation);
    }

    // schemaType .................................................................................................

    public Optional<String> schemaType() {
        return DomConfigurationProperty.SCHEMA_TYPE.string(this);
    }

    public DomDocument setSchemaType(final Optional<String> schemaType) {
        return DomConfigurationProperty.SCHEMA_TYPE.setString(this, schemaType);
    }

    // split CData section .................................................................................................

    public boolean splitCDataSection() {
        return DomConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public DomDocument setSplitCDataSection(final boolean splitCDataSection) {
        return DomConfigurationProperty.WELL_FORMED.setBooleanValue(this, splitCDataSection);
    }

    // strict error checking .................................................................................................

    /**
     * Tests if strict error checking is enabled.
     */
    public boolean strictErrorChecking() {
        return this.documentNode().getStrictErrorChecking();
    }

    /**
     * Would be setter that returns a {@link DomDocument} with the provided strict error checking, creating a copy if
     * necessary.
     */
    public DomDocument setStrictErrorChecking(final boolean strictErrorChecking) {
        return this.strictErrorChecking() == strictErrorChecking ?
                this :
                replaceSetStrictErrorChecking(strictErrorChecking);
    }

    private DomDocument replaceSetStrictErrorChecking(final boolean strictErrorChecking) {
        final org.w3c.dom.Document document = this.nodeCloneAll();
        document.setStrictErrorChecking(strictErrorChecking);
        return new DomDocument(document);
    }

    // validate .................................................................................................

    public boolean validate() {
        return DomConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public DomDocument setValidate(final boolean validate) {
        return DomConfigurationProperty.WELL_FORMED.setBooleanValue(this, validate);
    }

    // validateIfSchema .................................................................................................

    public boolean validateIfSchema() {
        return DomConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public DomDocument setValidateIfSchema(final boolean validateIfSchema) {
        return DomConfigurationProperty.WELL_FORMED.setBooleanValue(this, validateIfSchema);
    }

    // wellFormed .................................................................................................

    public boolean wellFormed() {
        return DomConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public DomDocument setWellFormed(final boolean wellFormed) {
        return DomConfigurationProperty.WELL_FORMED.setBooleanValue(this, wellFormed);
    }

    // xml encoding .................................................................................................

    /**
     * Returns the xml encoding if known.
     */
    public Optional<String> xmlEncoding() {
        if(null==this.xmlEncoding){
            this.xmlEncoding = Optional.ofNullable(this.documentNode().getXmlEncoding());
        }
        return this.xmlEncoding;
    }

    private Optional<String> xmlEncoding;

    /**
     * Tests if this is a xmlstandalone.
     */
    public boolean xmlStandalone() {
        return this.documentNode().getXmlStandalone();
    }

    public DomDocument setXmlStandalone(final boolean xmlStandalone) {
        return this.xmlStandalone() == xmlStandalone ? this : replaceXmlStandalone(xmlStandalone);
    }

    private DomDocument replaceXmlStandalone(final boolean xmlStandalone) {
        final org.w3c.dom.Document document = this.nodeCloneAll();
        document.setXmlStandalone(xmlStandalone);
        return new DomDocument(document);
    }

    /**
     * Returns the xml version if known.
     */
    public Optional<String> xmlVersion() {
        if(null==this.xmlVersion){
            this.xmlVersion = Optional.ofNullable(this.documentNode().getXmlVersion());
        }
        return this.xmlVersion;
    }

    private Optional<String> xmlVersion;

    /**
     * Sets or replaces the xml version creating a new document if the new value is different.
     */
    public DomDocument setXmlVersion(final Optional<String> xmlVersion) {
        Objects.requireNonNull(xmlVersion, "xmlVersion");
        return this.xmlVersion().equals(xmlVersion) ? this : this.replaceXmlVersion(xmlVersion.get());
    }

    private DomDocument replaceXmlVersion(final String xmlVersion) {
        final org.w3c.dom.Document document = this.nodeCloneAll();
        document.setXmlVersion(xmlVersion);
        return new DomDocument(document);
    }

    // children......................................................................................................

    @Override
    public DomDocument setChildren(final List<DomNode> children) {
        return this.setChildren0(children).document();
    }

    public DomDocument appendChild(final DomNode child) {
        return super.appendChild(child).document();
    }

    @Override
    public DomDocument removeChild(final DomNode child) {
        return super.removeChild(child).cast();
    }

    @Override
    public DomDocument removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    /**
     * A custom replace children, that only supports replacing element nodes. The new nodes
     * must be of the same type and equal to the existing.
     */
    @Override
    DomNode replaceChildren(final List<DomNode> children) {
        try {
            final int newChildCount = children.size();

            final org.w3c.dom.Document replacedDocument = this.nodeCloneAll();

            final NodeList oldChildren = replacedDocument.getChildNodes();
            final int oldChildCount = oldChildren.getLength();

            int oldIndex = 0;
            int newIndex = 0;

            while(oldIndex < oldChildCount) {
                final org.w3c.dom.Node oldNode = oldChildren.item(oldIndex);
                oldIndex++;
                if (newIndex >= newChildCount) {
                    // $oldChild is about to be removed, complain if it is not the document element.
                    if (oldNode.getNodeType() != Node.ELEMENT_NODE) {
                        throw new DomException("Removing anything but the document element is not supported");
                    }
                    replacedDocument.removeChild(oldNode);
                    continue;
                }
                final DomNode newChild = children.get(newIndex);
                final org.w3c.dom.Node newNode = newChild.node;
                newIndex++;

                if(oldNode.isEqualNode(newNode)) {
                    continue;
                }
                // $oldChild is about to be removed, complain if it is not the document element.
                if (oldNode.getNodeType() != Node.ELEMENT_NODE) {
                    throw new DomException("Removing anything but the document element is not supported");
                }
                replacedDocument.adoptNode(newNode);
                replacedDocument.replaceChild(newNode, oldNode);
            }

            // append remaining newChildren, impl will complain if they arent elements...
            while(newIndex < newChildCount){
                final org.w3c.dom.Node newNode = children.get(newIndex).nodeCloneWithoutParentWithChildren();
                newIndex++;
                replacedDocument.adoptNode(newNode);
                replacedDocument.appendChild(newNode);
            }

            return this.wrap0(replacedDocument);
        } catch (final org.w3c.dom.DOMException cause) {
            throw new DomException(Throwables.message("Failed to replace children", cause));
        }
    }

    // attributes......................................................................................................

    @Override
    public Map<DomAttributeName, String> attributes() {
        return NO_ATTRIBUTES;
    }

    @Override
    public DomNode setAttributes(final Map<DomAttributeName, String> attributes) {
        throw new UnsupportedOperationException();
    }

    // DomNode ....................................................................................................

    @Override
    final org.w3c.dom.Document documentNode0() {
        return Cast.to(this.node);
    }

    @Override
    final org.w3c.dom.Document nodeCloneAll() {
        return Cast.to(this.node.cloneNode(true));
    }

    /**
     * Factory method that returns a new instance of the same type with an updated {@link org.w3c.dom.Node node}
     */
    @Override
    DomNode wrap0(final Node node) {
        return new DomDocument(node);
    }

    @Override
    DomNodeKind kind() {
        return DomNodeKind.DOCUMENT;
    }

    // Node ....................................................................................................

    /**
     * Documents never have siblings.
     */
    @Override
    public Optional<DomNode> previousSibling() {
        return Optional.empty();
    }

    /**
     * Documents never have siblings.
     */
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

    private final static String PROPERTIES_SURROUND_BEFORE = DomComment.OPEN;
    private final static String PROPERTIES_SURROUND_AFTER = DomComment.CLOSE + LineEnding.SYSTEM;

    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.separator("");
        builder.enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);

        builder.surroundValues(PROPERTIES_SURROUND_BEFORE, PROPERTIES_SURROUND_AFTER);
        builder.value(new Object[]{this.configurationPropertiesToString()});
        builder.surroundValues("", "");

        builder.valueSeparator("\n");
        builder.value(this.children());
    }

    /**
     * Creates something like a comma separated string holding all truthy configuration properties.
     */
    private String configurationPropertiesToString(){
        final ToStringBuilder builder = ToStringBuilder.create();
        builder.separator(", ");

        for(DomConfigurationProperty property : DomConfigurationProperty.values()) {
            final Object value = property.get(this);
            if(value instanceof Boolean) {
                if(Boolean.TRUE.equals(value)){
                    builder.value(property.name);
                }
                continue;
            }
            builder.label(property.name)
                    .value(value);
        }
        return builder.build();
    }
}
