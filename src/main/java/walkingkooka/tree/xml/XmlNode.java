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

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.DocumentType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.PathSeparator;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;
import walkingkooka.text.Whitespace;
import walkingkooka.tree.TraversableHasTextOffset;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.search.HasSearchNode;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.tree.select.parser.NodeSelectorExpressionParserToken;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Base class for all dom {@link walkingkooka.tree.Node nodes}.
 * <p>
 * An immutable xml document, where all apparent mutator methods return a new document or node. As an example,
 * changing attributes on an element, returns a new element within an entire document.
 */
public abstract class XmlNode implements walkingkooka.tree.Node<XmlNode, XmlName, XmlAttributeName, String>,
        HasSearchNode,
        HasXmlNode,
        HasText,
        TraversableHasTextOffset<XmlNode>,
        UsesToStringBuilder {

    final static String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
    public final static XmlNameSpacePrefix XMLNS = XmlNameSpacePrefix.XMLNS;
    public final static Optional<XmlNameSpacePrefix> XMLNS_PRESENT = Optional.of(XMLNS);

    public final static XmlName NO_NAME = null;
    public final static Optional<XmlNode> NO_PARENT = Optional.empty();
    public final static List<XmlNode> NO_CHILDREN = Lists.empty();
    public final static Map<XmlAttributeName, String> NO_ATTRIBUTES = Maps.ordered();
    public final static Optional<XmlNameSpacePrefix> NO_PREFIX = Optional.empty();
    public final static Optional<XmlPublicId> NO_PUBLIC_ID = Optional.empty();
    public final static Optional<XmlSystemId> NO_SYSTEM_ID = Optional.empty();
    public final static Optional<XmlDocumentType> NO_DOCUMENT_TYPE = Optional.empty();
    public final static Optional<XmlElement> NO_ELEMENT = Optional.empty();

    /**
     * {@see DomAtttributeName}
     */
    public static XmlAttributeName attribute(final String name, final Optional<XmlNameSpacePrefix> prefix) {
        return XmlAttributeName.with(name, prefix);
    }

    static void checkCDataSectionText(final String text) {
        Objects.requireNonNull(text, "text");
        if (text.contains(XmlCDataSection.CLOSE)) {
            throw new IllegalArgumentException("CDataSection cannot contain " + CharSequences.quote(XmlCDataSection.CLOSE) + " is " + CharSequences.quoteAndEscape(text));
        }
    }

    static void checkCommentText(final String text) {
        Objects.requireNonNull(text, "text");
        if (text.contains(XmlComment.ILLEGAL_CONTENT)) {
            throw new IllegalArgumentException("Comments cannot contain " + CharSequences.quote(XmlComment.ILLEGAL_CONTENT) + " is " + CharSequences.quoteAndEscape(text));
        }
    }

    static void checkTextText(final String text) {
        Objects.requireNonNull(text, "text");
    }

    /**
     * {@see XmlNameSpacePrefix}
     */
    public static XmlNameSpacePrefix prefix(final String prefix) {
        return XmlNameSpacePrefix.with(prefix);
    }

    /**
     * Creates a new public id
     */
    public static Optional<XmlPublicId> publicId(final String value) {
        return XmlPublicId.with(value);
    }

    /**
     * The {@link PathSeparator} for node selector paths.
     */
    public static final PathSeparator PATH_SEPARATOR = PathSeparator.requiredAtStart('/');

    /**
     * Creates a new system id
     */
    public static Optional<XmlSystemId> systemId(final String value) {
        return XmlSystemId.with(value);
    }

    /**
     * Reads a xml document into a {@link XmlDocument}. The provided {@link DocumentBuilder} should have
     * any validating and namespaces set prior to calling this method.
     */
    public static XmlDocument fromXml(final DocumentBuilder builder, final Reader reader) throws IOException, SAXException {
        Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(reader, "reader");

        return XmlDocument.with(XmlNode.documentFromXml(builder, reader));
    }

    // @VisibleForTesting
    static org.w3c.dom.Document documentFromXml(final DocumentBuilder builder, final Reader reader) throws IOException, SAXException {
        final InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(reader);
        return builder.parse(inputSource);
    }

    /**
     * Factory that creates a new empty {@link XmlDocument} from the provided {@link DocumentBuilder},
     * without any children, that is no doctype or document element.
     */
    public static XmlDocument createDocument(final DocumentBuilder builder) {
        Objects.requireNonNull(builder, "builder");
        return XmlDocument.with(builder.newDocument());
    }

    public final static Optional<String> NO_NAMESPACE_URI = Optional.empty();

    /**
     * Factory that creates a {@link XmlDocument} from the provided {@link DocumentBuilder}
     * with a doc type and document element.
     */
    public static XmlDocument createDocument(final DocumentBuilder builder,
                                             final Optional<String> namespaceUri,
                                             final XmlName type,
                                             final Optional<XmlPublicId> publicId,
                                             final Optional<XmlSystemId> systemId) {
        Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(namespaceUri, "namespaceUri");
        Objects.requireNonNull(type, "element");
        Objects.requireNonNull(publicId, "publicId");
        Objects.requireNonNull(systemId, "systemId");

        final DOMImplementation impl = builder.getDOMImplementation();
        final String typeName = type.value();
        final DocumentType documentType = impl.createDocumentType(typeName,
                publicId.map(v -> v.value()).orElse(null),
                systemId.map(v -> v.value()).orElse(null));
        return XmlDocument.with(impl.createDocument(namespaceUri.orElse(null),
                typeName,
                documentType));
    }

    /**
     * Accepts a {@link org.w3c.dom.Node} and returns the appropriate wrapper.
     */
    static XmlNode wrap(final org.w3c.dom.Node node) {
        XmlNode result;

        final int type = node.getNodeType();
        switch (type) {
            case org.w3c.dom.Node.ELEMENT_NODE:
                result = XmlElement.with(node);
                break;
            case org.w3c.dom.Node.TEXT_NODE:
                result = XmlText.with(node);
                break;
            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                result = XmlCDataSection.with(node);
                break;
            case org.w3c.dom.Node.ENTITY_REFERENCE_NODE:
                result = XmlEntityReference.with(node);
                break;
            case org.w3c.dom.Node.ENTITY_NODE:
                result = XmlEntity.with(node);
                break;
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                result = XmlProcessingInstruction.with(node);
                break;
            case org.w3c.dom.Node.COMMENT_NODE:
                result = XmlComment.with(node);
                break;
            case org.w3c.dom.Node.DOCUMENT_TYPE_NODE:
                result = XmlDocumentType.with(node);
                break;
            case org.w3c.dom.Node.NOTATION_NODE:
                result = XmlNotation.with(node);
                break;
            case org.w3c.dom.Node.DOCUMENT_NODE:
                result = XmlDocument.with(node);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported node type " + type + "=" + node);
        }
        return result;
    }

    /**
     * Package private to limit sub classing.
     */
    XmlNode(final org.w3c.dom.Node node) {
        this.node = node;
    }

    /**
     * The real wrapped node. Not all other walkingkooka.xml.* types are lazily created.
     */
    final org.w3c.dom.Node node;

    /**
     * Clones this node without its parent or children.
     */
    final org.w3c.dom.Node nodeCloneWithoutParentWithoutChildren() {
        return this.node.cloneNode(false);
    }

    /**
     * Takes a clone of this node, without its parent but copies any children if any are present.
     * This is used when replacing child nodes.
     */
    abstract org.w3c.dom.Node nodeCloneWithoutParentWithChildren();

    /**
     * Takes a clone of this node, including all ancestors and descendants.
     */
    abstract org.w3c.dom.Node nodeCloneAll();

    /**
     * The default cloneAll except for document.
     */
    final org.w3c.dom.Node nodeCloneAll0() {
        final int index = this.index();
        return NO_INDEX == index ? this.node.cloneNode(true) : nodeCloneAll2(this.node, index);
    }

    private static org.w3c.dom.Node nodeCloneAll1(final org.w3c.dom.Node node) {
        return nodeCloneAll2(node, index0(node));
    }

    private static org.w3c.dom.Node nodeCloneAll2(final org.w3c.dom.Node node, final int index) {
        if (NO_INDEX == index) {
            return node.cloneNode(true); // eventually stops when document reached.
        } else {
            final org.w3c.dom.Node parent = nodeCloneAll1(node.getParentNode());
            return parent.getChildNodes().item(index);
        }
    }

    /**
     * Factory that creates a wrapper with the provided node.
     */
    abstract XmlNode wrap0(org.w3c.dom.Node node);

    abstract XmlNodeKind kind();

    // need a way to create attributes... for fetching and addition to an elementNode.

    public final XmlCDataSection createCDataSection(final String text) {
        checkCDataSectionText(text);
        return XmlCDataSection.with(this.documentNode().createCDATASection(text));
    }

    public final XmlComment createComment(final String text) {
        checkCommentText(text);
        return XmlComment.with(this.documentNode().createComment(text));
    }

    public final XmlElement createElement(final XmlName tagName) {
        return tagName.createElement(this.documentNode());
    }

    public final XmlElement createElement(final String namespaceUri,
                                          final XmlNameSpacePrefix prefix,
                                          final XmlName tagName) {
        Whitespace.failIfNullOrEmptyOrWhitespace(namespaceUri, "namespaceUri");
        Objects.requireNonNull(prefix, "prefix");

        return tagName.createElement(this.documentNode(), namespaceUri, prefix);
    }

    public final XmlProcessingInstruction createProcessingInstruction(final String target, final String data) {
        return XmlProcessingInstruction.with(this.documentNode().createProcessingInstruction(target, data));
    }

    public final XmlText createText(final String text) {
        checkTextText(text);
        return XmlText.with(this.documentNode().createTextNode(text));
    }

    /**
     * Document getter that succeeds or fails but never returns null.
     */
    final org.w3c.dom.Document documentNode() {
        final org.w3c.dom.Document document = this.documentNode0();
        if (null == document) {
            throw new XmlException("Node without owner document");
        }
        return document;
    }

    abstract org.w3c.dom.Document documentNode0();

    /**
     * The mostly default which simply fetches the owner document for a given node.
     */
    final org.w3c.dom.Document documentNode1() {
        return this.node.getOwnerDocument();
    }

    /**
     * Returns the document holding this node.
     */
    public abstract XmlDocument document();

    /**
     * Returns this if a {@link XmlCDataSection} .
     */
    public boolean isCDataSection() {
        return false;
    }

    /**
     * Returns this if a {@link XmlComment} .
     */
    public boolean isComment() {
        return false;
    }

    /**
     * Returns this if a {@link XmlDocument} .
     */
    public boolean isDocument() {
        return false;
    }

    /**
     * Returns this if a {@link XmlDocumentType} .
     */
    public boolean isDocumentType() {
        return false;
    }

    /**
     * Returns this if a {@link XmlElement} .
     */
    public boolean isElement() {
        return false;
    }

    /**
     * Returns this if a {@link XmlEntity} .
     */
    public boolean isEntity() {
        return false;
    }

    /**
     * Returns this if a {@link XmlEntityReference} .
     */
    public boolean isEntityReference() {
        return false;
    }

    /**
     * Returns this if a {@link XmlNotation} .
     */
    public boolean isNotation() {
        return false;
    }

    /**
     * Returns this if a {@link XmlProcessingInstruction} .
     */
    public boolean isProcessingInstruction() {
        return false;
    }

    /**
     * Returns this if a {@link XmlText} .
     */
    public boolean isText() {
        return false;
    }

    final <T extends XmlNode> T cast() {
        return Cast.to(this);
    }

    // text.......................................................................................................

    /**
     * Returns all the text in this node including its child nodes.
     */
    public final String text() {
        return this.kind().text(this.node);
    }

    // normalize.......................................................................................................

    /**
     * Normalises a document and returns the result.
     */
    public final XmlNode normalize() {
        return this.node.getChildNodes().getLength() == 0 ?
                this :
                this.normalize0();
    }

    private XmlNode normalize0() {
        final org.w3c.dom.Node normalized = this.nodeCloneAll();
        normalized.normalize();
        return this.wrap0(normalized);
    }

    // toXml.........................................................................................................

    /**
     * Writes the entire document to the provided {@link Writer}.
     */
    public final void toXml(final Transformer transformer, final Writer writer) throws TransformerException {
        Objects.requireNonNull(transformer, "transformer");
        Objects.requireNonNull(writer, "writer");

        final StreamResult result = new StreamResult(writer);
        DOMSource source = new DOMSource(this.node);
        transformer.transform(source, result);
    }

    // toXmlNode.........................................................................................................

    /**
     * Already an {@link XmlNode}.
     */
    @Override
    public final XmlNode toXmlNode() {
        return this;
    }

    // Node.name.....................................................................................................

    @Override
    public final XmlName name() {
        if (null == this.name) {
            this.name = this.kind().name(this);
        }
        return this.name;
    }

    XmlName name;

    // Node.parent........................................................................................................

    /**
     * Returns the parent for this node.
     */
    @Override
    public final Optional<XmlNode> parent() {
        if (null == this.parent) {
            if (null == this.node) {
                this.parent = NO_PARENT;
            } else {
                final org.w3c.dom.Node parent = this.node.getParentNode();
                this.parent = null != parent ? Optional.of(wrap(parent)) : NO_PARENT;
            }
        }
        return this.parent;
    }

    Optional<XmlNode> parent;

    /**
     * Sub classes should call this and cast to their type.
     */
    final XmlNode removeParent0() {
        return this.isRoot() ?
                this :
                this.removeParent1();
    }

    /**
     * Sub classes should clone the {@link org.w3c.dom.Node} but not the parent.
     */
    abstract XmlNode removeParent1();

    // Node.children........................................................................................................

    /**
     * Returns the child index when its has a parent or -1.
     */
    @Override
    public final int index() {
        if (INDEX_UNKNOWN == this.index) {
            this.index = index0(this.node);
        }
        return this.index;
    }

    // @VisibleForTesting
    int index = INDEX_UNKNOWN;

    /**
     * Magic index value that indicates the index has not been determined and cached.
     */
    private final static int INDEX_UNKNOWN = Integer.MIN_VALUE;

    /**
     * Accepts any node and counts the previous siblings to compute its child index.
     */
    private static int index0(org.w3c.dom.Node node) {
        int index = NO_INDEX;

        if (null != node.getParentNode()) {
            index = 0;
            for (; ; ) {
                node = node.getPreviousSibling();
                if (null == node) {
                    break;
                }
                index++;
            }
        }
        return index;
    }

    // toSearchNode...............................................................................................

    @Override
    public final SearchNode toSearchNode() {
        return this.toSearchNode0()
                .setName(this.searchNodeName());
    }

    abstract SearchNode toSearchNode0();

    abstract SearchNodeName searchNodeName();

    static SearchNode textSearchNode(final String text) {
        return SearchNode.text(text, text);
    }

    // Object.......................................................................................................

    @Override
    abstract public int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final XmlNode other) {
        return this.equalsAncestors(other) && this.equalsDescendants0(other.children());
    }

    private boolean equalsAncestors(final XmlNode other) {
        boolean result = this.equalsIgnoringParentAndChildren(other);

        if (result) {
            final Optional<XmlNode> parent = this.parent();
            final Optional<XmlNode> otherParent = other.parent();
            final boolean hasParent = parent.isPresent();
            final boolean hasOtherParent = otherParent.isPresent();

            if (hasParent) {
                if (hasOtherParent) {
                    result = parent.get().equalsAncestors(otherParent.get());
                }
            } else {
                // result is only true if other is false
                result = !hasOtherParent;
            }
        }

        return result;
    }

    private boolean equalsDescendants(final XmlNode other) {
        return this.equalsIgnoringParentAndChildren(other) &&
                this.equalsDescendants0(other.children());
    }

    /**
     * Only returns true if the descendants of this node and the given children are equal ignoring the parents.
     */
    final boolean equalsDescendants0(final List<XmlNode> otherChildren) {
        final List<XmlNode> children = this.children();
        final int count = children.size();
        boolean equals = count == otherChildren.size();

        if (equals) {
            for (int i = 0; equals && i < count; i++) {
                equals = children.get(i).equalsDescendants(otherChildren.get(i));
            }
        }

        return equals;
    }

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsIgnoringParentAndChildren(final XmlNode other);

    // UsesToStringBuilder...........................................................................................

    @Override
    public final String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    /**
     * Sets some defaults for {@link ToStringBuilder} like no quoting or escaping.
     */
    @Override
    final public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.disable(ToStringBuilderOption.ESCAPE);
        builder.separator("");
        builder.labelSeparator(" ");
        builder.valueSeparator(" ");

        this.buildDomNodeToString(builder);
    }

    /**
     * Sub classes override this and do their stringbuilding...
     */
    abstract void buildDomNodeToString(final ToStringBuilder builder);

    /**
     * Generic helper that appends any public id and system id.
     */
    static void buildToString(final Optional<XmlPublicId> publicId,
                              final Optional<XmlSystemId> systemId,
                              final ToStringBuilder builder) {
        builder.enable(ToStringBuilderOption.QUOTE);
        builder.separator(" ");

        if (publicId.isPresent()) {
            builder.label(PUBLIC);
            builder.value(publicId);
            builder.value(systemId);
        } else {
            builder.label(SYSTEM);
            builder.value(systemId);
        }
    }

    final static String SYSTEM = "SYSTEM";
    final static String PUBLIC = "PUBLIC";

    // NodeSelector .......................................................................................................

    /**
     * {@see NodeSelector#absolute}
     */
    public static NodeSelector<XmlNode, XmlName, XmlAttributeName, String> absoluteNodeSelector() {
        return NodeSelector.absolute();
    }

    /**
     * {@see NodeSelector#relative}
     */
    public static NodeSelector<XmlNode, XmlName, XmlAttributeName, String> relativeNodeSelector() {
        return NodeSelector.relative();
    }

    /**
     * Creates a {@link NodeSelector} for {@link XmlNode} from a {@link NodeSelectorExpressionParserToken}.
     */
    public static NodeSelector<XmlNode, XmlName, XmlAttributeName, String> nodeSelectorExpressionParserToken(final NodeSelectorExpressionParserToken token,
                                                                                                             final Predicate<ExpressionNodeName> functions) {
        return NodeSelector.parserToken(token,
                n -> XmlName.element(n.value()),
                functions,
                XmlNode.class);
    }
}
