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

import org.w3c.dom.Entity;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link XmlNode} that holds an entityDefinition.
 */
final public class XmlEntity extends XmlParentNode2 implements HasXmlPublicId, HasXmlSystemId {

    public final static String OPEN = "<!ENTITY";
    public final static String CLOSE = ">";

    static XmlEntity with(final Node node) {
        return new XmlEntity(node);
    }

    private XmlEntity(final Node node) {
        super(node);
    }

    private Entity entityNode() {
        return Cast.to(this.node);
    }

    // HasPublicId .............................................................................................

    @Override
    public Optional<XmlPublicId> publicId() {
        if (null == this.publicId) {
            this.publicId = XmlPublicId.with(this.entityNode().getPublicId());
        }
        return this.publicId;
    }

    private Optional<XmlPublicId> publicId;

    // HasSystemId ...................................................................................................

    @Override
    public Optional<XmlSystemId> systemId() {
        if (null == this.systemId) {
            this.systemId = XmlSystemId.with(this.entityNode().getSystemId());
        }
        return this.systemId;
    }

    private Optional<XmlSystemId> systemId;

    // notation ...................................................................................................

    public XmlName notation() {
        if (null == this.notation) {
            this.notation = XmlNodeKind.NOTATION.with(this.entityNode().getNotationName());
        }
        return this.notation;
    }

    private XmlName notation;

    // inputEncoding ...................................................................................................

    public Optional<String> inputEncoding() {
        if (null == this.inputEncoding) {
            this.inputEncoding = Optional.ofNullable(this.entityNode().getInputEncoding());
        }
        return this.inputEncoding;
    }

    private Optional<String> inputEncoding;

    // xmlEncoding ...................................................................................................

    public Optional<String> xmlEncoding() {
        if (null == this.xmlEncoding) {
            this.xmlEncoding = Optional.ofNullable(this.entityNode().getXmlEncoding());
        }
        return this.xmlEncoding;
    }

    private Optional<String> xmlEncoding;

    // xmlVersion ...................................................................................................

    public Optional<String> xmlVersion() {
        if (null == this.xmlVersion) {
            this.xmlVersion = Optional.ofNullable(this.entityNode().getXmlVersion());
        }
        return this.xmlVersion;
    }

    private Optional<String> xmlVersion;

    // parent................................................................................................

    @Override
    public XmlEntity removeParent() {
        return this.removeParent0().cast();
    }

    // children................................................................................................

    @Override
    public XmlEntity setChildren(final List<XmlNode> children) {
        throw new UnsupportedOperationException();
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

    // XmlNode........................................................................

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.ENTITY_REFERENCE;
    }

    @Override
    XmlEntity wrap0(final Node node) {
        return new XmlEntity(node);
    }

    /**
     * Always returns true.
     */
    @Override
    public boolean isEntity() {
        return true;
    }

    @Override
    SearchNode toSearchNode0() {
        return this.toSearchNode1();
    }

    @Override
    SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("Entity");

    // Object...................................................................................................

    @Override
    public int hashCode() {
        return this.name().hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlEntity;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final XmlNode other) {
        return equalsIgnoringParentAndChildren0(other.cast());
    }

    private boolean equalsIgnoringParentAndChildren0(final XmlEntity other) {
        return this.name().equals(other.name()) && //
                this.publicId().equals(other.publicId()) && //
                this.systemId().equals(other.systemId()) && //
                this.notation().equals(other.notation()) && //
                this.inputEncoding().equals(other.inputEncoding()) && //
                this.xmlEncoding().equals(other.xmlEncoding()) && //
                this.xmlVersion().equals(other.xmlVersion());
    }

    // UsesToStringBuilder...........................................................................................

    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.append(XmlEntity.OPEN);

        builder.separator(" ");
        builder.append(' '); // YUCK
        builder.value(this.name().value());

        buildToString(this.publicId(), this.systemId(), builder);

        builder.append(XmlEntity.CLOSE);
    }
}
