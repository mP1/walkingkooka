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

import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A dom document, that holds nodes such as a doctype, document element and a tree of nodes.
 */
public final class XmlDocument extends XmlParentNode {

    static XmlDocument with(final Node node) {
        return new XmlDocument(node);
    }

    private XmlDocument(final Node node) {
        super(node);
    }

    @Override
    public XmlDocument document() {
        return this;
    }

    // parent................................................................................................

    @Override
    public XmlDocument removeParent() {
        return this.removeParent0().cast();
    }

    // isX.........................................................................................

    @Override
    public boolean isDocument() {
        return true;
    }

    // documentType ...................................................................................................

    /**
     * Returns the doc type if one is present.
     */
    public Optional<XmlDocumentType> documentType() {
        if (null == this.documentType) {
            this.documentType = this.getChildByNodeType(Node.DOCUMENT_TYPE_NODE);
        }
        return this.documentType;
    }

    private Optional<XmlDocumentType> documentType;

    // element .........................................................................................................

    /**
     * Returns the root document element if one is present.
     */
    public Optional<XmlElement> element() {
        if (null == this.element) {
            this.element = this.getChildByNodeType(Node.ELEMENT_NODE);
        }
        return this.element;
    }

    private Optional<XmlElement> element;

    private <T extends XmlNode> Optional<T> getChildByNodeType(final int nodeType) {
        return this.children.isEmpty() ? Optional.empty() : this.getChildByNodeType0(nodeType);
    }

    private <T extends XmlNode> Optional<T> getChildByNodeType0(final int nodeType) {
        final XmlNodeChildList children = Cast.to(this.children);
        return Cast.to(children.getElementByNodeType(nodeType));
    }

    /**
     * Would be setter that returns a document with the given element.
     */
    public XmlDocument setElement(final Optional<XmlElement> element) {
        Objects.requireNonNull(element, "element");

        final Optional<XmlElement> previous = this.element();

        return previous.isPresent() && element.isPresent() ? this.replaceElement(previous.get(), element.get()) :
                previous.isPresent() && !element.isPresent() ? this.removeElement(previous.get()) :
                        element.isPresent() ? this.addElement(element.get()) :// previous no element, adding element
                                this; // previous no element, new no element -> no change

    }

    private XmlDocument replaceElement(final XmlElement previous, final XmlElement next) {
        return this.replaceChild(previous, next).document();
    }

    private XmlDocument removeElement(final XmlElement previous) {
        return this.removeChild(previous.index()).document();
    }

    private XmlDocument addElement(final XmlElement element) {
        return this.appendChild(element);
    }


    // canonical form .......................................................................................

    public boolean canonicalForm() {
        return XmlConfigurationProperty.CANONICAL_FORM.booleanValue(this);
    }

    public XmlDocument setCanonicalForm(final boolean canonicalForm) {
        return XmlConfigurationProperty.CANONICAL_FORM.setBooleanValue(this, canonicalForm);
    }

    // cdata sections .......................................................................................

    public boolean cDataSections() {
        return XmlConfigurationProperty.CDATA_SECTIONS.booleanValue(this);
    }

    public XmlDocument setCDataSections(final boolean cDataSections) {
        return XmlConfigurationProperty.CDATA_SECTIONS.setBooleanValue(this, cDataSections);
    }

    // check character normalizations .......................................................................................

    public boolean checkCharacterNormalizations() {
        return XmlConfigurationProperty.CHECK_CHARACTER_NORMALIZATIONS.booleanValue(this);
    }

    public XmlDocument setCheckCharacterNormalizations(final boolean checkCharacterNormalizations) {
        return XmlConfigurationProperty.CHECK_CHARACTER_NORMALIZATIONS.setBooleanValue(this, checkCharacterNormalizations);
    }

    // comments .......................................................................................

    public boolean comments() {
        return XmlConfigurationProperty.COMMENTS.booleanValue(this);
    }

    public XmlDocument setComments(final boolean comments) {
        return XmlConfigurationProperty.COMMENTS.setBooleanValue(this, comments);
    }

    // datatype normalizations .......................................................................................

    public boolean datatypeNormalizations() {
        return XmlConfigurationProperty.DATATYPE_NORMALIZATION.booleanValue(this);
    }

    public XmlDocument setDatatypeNormalizations(final boolean datatypeNormalizations) {
        return XmlConfigurationProperty.DATATYPE_NORMALIZATION.setBooleanValue(this, datatypeNormalizations);
    }

    // documentUri .....................................................................................................

    /**
     * Returns the document uri if known.
     */
    public Optional<String> documentUri() {
        if (null == this.documentUri) {
            this.documentUri = Optional.ofNullable(this.documentNode().getDocumentURI());
        }
        return this.documentUri;
    }

    private Optional<String> documentUri;

    public XmlDocument setDocumentUri(final Optional<String> documentUri) {
        Objects.requireNonNull(documentUri, "documentUri");
        return this.documentUri().equals(documentUri) ? this : this.replaceDocumentUri(documentUri.get());
    }

    private XmlDocument replaceDocumentUri(final String documentUri) {
        final Document document = this.nodeCloneAll();
        document.setDocumentURI(documentUri);
        return new XmlDocument(document);
    }

    // element content whitespace .......................................................................................

    public boolean elementContentWhitespace() {
        return XmlConfigurationProperty.DATATYPE_NORMALIZATION.booleanValue(this);
    }

    public XmlDocument setElementContentWhitespace(final boolean elementContentWhitespace) {
        return XmlConfigurationProperty.DATATYPE_NORMALIZATION.setBooleanValue(this, elementContentWhitespace);
    }

    // entities .................................................................................................

    public boolean entities() {
        return XmlConfigurationProperty.ENTITIES.booleanValue(this);
    }

    public XmlDocument setEntities(final boolean entities) {
        return XmlConfigurationProperty.ENTITIES.setBooleanValue(this, entities);
    }

    // errorHandler .................................................................................................

    public Optional<DOMErrorHandler> errorHandler() {
        return XmlConfigurationProperty.ERROR_HANDLER.errorHandler(this);
    }

    public XmlDocument setErrorHandler(final Optional<? extends DOMErrorHandler> errorHandler) {
        return XmlConfigurationProperty.ERROR_HANDLER.setErrorHandler(this, errorHandler);
    }

    // infoset .................................................................................................

    public boolean infoset() {
        return XmlConfigurationProperty.INFOSET.booleanValue(this);
    }

    public XmlDocument setInfoset(final boolean infoset) {
        return XmlConfigurationProperty.INFOSET.setBooleanValue(this, infoset);
    }

    // inputEncoding ..............................................................................................

    /**
     * Returns the input encoding if known.
     */
    public Optional<String> inputEncoding() {
        if (null == this.inputEncoding) {
            this.inputEncoding = Optional.ofNullable(this.documentNode().getInputEncoding());
        }
        return this.inputEncoding;
    }

    private Optional<String> inputEncoding;

    // namespaces .................................................................................................

    public boolean namespaces() {
        return XmlConfigurationProperty.NAMESPACES.booleanValue(this);
    }

    public XmlDocument setNamespaces(final boolean namespaces) {
        return XmlConfigurationProperty.NAMESPACES.setBooleanValue(this, namespaces);
    }

    // namespaceDeclaration .................................................................................................

    public boolean namespaceDeclaration() {
        return XmlConfigurationProperty.NAMESPACE_DECLARATION.booleanValue(this);
    }

    public XmlDocument setNamespaceDeclaration(final boolean namespaceDeclaration) {
        return XmlConfigurationProperty.NAMESPACE_DECLARATION.setBooleanValue(this, namespaceDeclaration);
    }

    // normalizeCharacters .................................................................................................

    public boolean normalizeCharacters() {
        return XmlConfigurationProperty.NORMALIZE_CHARACTERS.booleanValue(this);
    }

    public XmlDocument setNormalizeCharacters(final boolean normalizeCharacters) {
        return XmlConfigurationProperty.NORMALIZE_CHARACTERS.setBooleanValue(this, normalizeCharacters);
    }

    // schemaLocation .................................................................................................

    public Optional<String> schemaLocation() {
        return XmlConfigurationProperty.SCHEMA_LOCATION.string(this);
    }

    public XmlDocument setSchemaLocation(final Optional<String> schemaLocation) {
        return XmlConfigurationProperty.SCHEMA_LOCATION.setString(this, schemaLocation);
    }

    // schemaType .................................................................................................

    public Optional<String> schemaType() {
        return XmlConfigurationProperty.SCHEMA_TYPE.string(this);
    }

    public XmlDocument setSchemaType(final Optional<String> schemaType) {
        return XmlConfigurationProperty.SCHEMA_TYPE.setString(this, schemaType);
    }

    // split CData section .................................................................................................

    public boolean splitCDataSection() {
        return XmlConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public XmlDocument setSplitCDataSection(final boolean splitCDataSection) {
        return XmlConfigurationProperty.WELL_FORMED.setBooleanValue(this, splitCDataSection);
    }

    // strict error checking .................................................................................................

    /**
     * Tests if strict error checking is enabled.
     */
    public boolean strictErrorChecking() {
        return this.documentNode().getStrictErrorChecking();
    }

    /**
     * Would be setter that returns a {@link XmlDocument} with the provided strict error checking, creating a copy if
     * necessary.
     */
    public XmlDocument setStrictErrorChecking(final boolean strictErrorChecking) {
        return this.strictErrorChecking() == strictErrorChecking ?
                this :
                replaceSetStrictErrorChecking(strictErrorChecking);
    }

    private XmlDocument replaceSetStrictErrorChecking(final boolean strictErrorChecking) {
        final Document document = this.nodeCloneAll();
        document.setStrictErrorChecking(strictErrorChecking);
        return new XmlDocument(document);
    }

    // validate .................................................................................................

    public boolean validate() {
        return XmlConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public XmlDocument setValidate(final boolean validate) {
        return XmlConfigurationProperty.WELL_FORMED.setBooleanValue(this, validate);
    }

    // validateIfSchema .................................................................................................

    public boolean validateIfSchema() {
        return XmlConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public XmlDocument setValidateIfSchema(final boolean validateIfSchema) {
        return XmlConfigurationProperty.WELL_FORMED.setBooleanValue(this, validateIfSchema);
    }

    // wellFormed .................................................................................................

    public boolean wellFormed() {
        return XmlConfigurationProperty.WELL_FORMED.booleanValue(this);
    }

    public XmlDocument setWellFormed(final boolean wellFormed) {
        return XmlConfigurationProperty.WELL_FORMED.setBooleanValue(this, wellFormed);
    }

    // xml encoding .................................................................................................

    /**
     * Returns the xml encoding if known.
     */
    public Optional<String> xmlEncoding() {
        if (null == this.xmlEncoding) {
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

    public XmlDocument setXmlStandalone(final boolean xmlStandalone) {
        return this.xmlStandalone() == xmlStandalone ? this : replaceXmlStandalone(xmlStandalone);
    }

    private XmlDocument replaceXmlStandalone(final boolean xmlStandalone) {
        final Document document = this.nodeCloneAll();
        document.setXmlStandalone(xmlStandalone);
        return new XmlDocument(document);
    }

    /**
     * Returns the xml version if known.
     */
    public Optional<String> xmlVersion() {
        if (null == this.xmlVersion) {
            this.xmlVersion = Optional.ofNullable(this.documentNode().getXmlVersion());
        }
        return this.xmlVersion;
    }

    private Optional<String> xmlVersion;

    /**
     * Sets or replaces the xml version creating a new document if the new value is different.
     */
    public XmlDocument setXmlVersion(final Optional<String> xmlVersion) {
        Objects.requireNonNull(xmlVersion, "xmlVersion");
        return this.xmlVersion().equals(xmlVersion) ? this : this.replaceXmlVersion(xmlVersion.get());
    }

    private XmlDocument replaceXmlVersion(final String xmlVersion) {
        final Document document = this.nodeCloneAll();
        document.setXmlVersion(xmlVersion);
        return new XmlDocument(document);
    }

    // children......................................................................................................

    @Override
    public XmlDocument setChildren(final List<XmlNode> children) {
        return this.setChildren0(children).document();
    }

    public XmlDocument appendChild(final XmlNode child) {
        return super.appendChild(child).document();
    }

    @Override
    public XmlDocument removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    /**
     * A custom replace children, that only supports replacing element nodes. The new nodes
     * must be of the same type and equal to the existing.
     */
    @Override
    XmlNode replaceChildren(final List<XmlNode> children) {
        try {
            final int newChildCount = children.size();

            final Document replacedDocument = this.nodeCloneAll();

            final NodeList oldChildren = replacedDocument.getChildNodes();
            final int oldChildCount = oldChildren.getLength();

            int oldIndex = 0;
            int newIndex = 0;

            while (oldIndex < oldChildCount) {
                final Node oldNode = oldChildren.item(oldIndex);
                oldIndex++;
                if (newIndex >= newChildCount) {
                    // $oldChild is about to be removed, complain if it is not the document element.
                    if (oldNode.getNodeType() != Node.ELEMENT_NODE) {
                        throw new XmlException("Removing anything but the document element is not supported");
                    }
                    replacedDocument.removeChild(oldNode);
                    continue;
                }
                final XmlNode newChild = children.get(newIndex);
                final Node newNode = newChild.node;
                newIndex++;

                if (oldNode.isEqualNode(newNode)) {
                    continue;
                }
                // $oldChild is about to be removed, complain if it is not the document element.
                if (oldNode.getNodeType() != Node.ELEMENT_NODE) {
                    throw new XmlException("Removing anything but the document element is not supported");
                }
                replacedDocument.adoptNode(newNode);
                replacedDocument.replaceChild(newNode, oldNode);
            }

            // append remaining newChildren, impl will complain if they arent elements...
            while (newIndex < newChildCount) {
                final Node newNode = children.get(newIndex).nodeCloneWithoutParentWithChildren();
                newIndex++;
                replacedDocument.adoptNode(newNode);
                replacedDocument.appendChild(newNode);
            }

            return this.wrap0(replacedDocument);
        } catch (final DOMException cause) {
            throw new XmlException("Failed to replace children", cause);
        }
    }

    // attributes......................................................................................................

    @Override
    public Map<XmlAttributeName, String> attributes() {
        return NO_ATTRIBUTES;
    }

    @Override
    public XmlNode setAttributes(final Map<XmlAttributeName, String> attributes) {
        throw new UnsupportedOperationException();
    }

    // XmlNode ....................................................................................................

    @Override
    final Document documentNode0() {
        return Cast.to(this.node);
    }

    @Override
    final Document nodeCloneAll() {
        return Cast.to(this.node.cloneNode(true));
    }

    /**
     * Factory method that returns a new instance of the same type with an updated {@link Node node}
     */
    @Override
    XmlNode wrap0(final Node node) {
        return new XmlDocument(node);
    }

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.DOCUMENT;
    }

    // Node ....................................................................................................

    /**
     * Documents never have siblings.
     */
    @Override
    public Optional<XmlNode> previousSibling() {
        return Optional.empty();
    }

    /**
     * Documents never have siblings.
     */
    @Override
    public Optional<XmlNode> nextSibling() {
        return Optional.empty();
    }

    // toSearchNode...............................................................................................

    @Override
    final SearchNode toSearchNode0() {
        return this.toSearchNode1();
    }

    @Override
    SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("Document");

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

    private final static String PROPERTIES_SURROUND_BEFORE = XmlComment.OPEN;
    private final static String PROPERTIES_SURROUND_AFTER = XmlComment.CLOSE + LineEnding.SYSTEM;

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
    private String configurationPropertiesToString() {
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.separator(", ");

        for (XmlConfigurationProperty property : XmlConfigurationProperty.values()) {
            final Object value = property.get(this);
            if (value instanceof Boolean) {
                if (Boolean.TRUE.equals(value)) {
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
