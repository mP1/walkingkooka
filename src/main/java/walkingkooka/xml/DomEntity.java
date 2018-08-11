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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A {@link DomNode} that holds an entityDefinition.
 */
final public class DomEntity extends DomParentNode2 implements HasDomPublicId, HasDomSystemId {

    public final static String OPEN = "<!ENTITY";
    public final static String CLOSE = ">";

    DomEntity(final org.w3c.dom.Node node) {
        super(node);
    }

    org.w3c.dom.Entity entityNode() {
        return Cast.to(this.node);
    }

    // HasPublicId .............................................................................................

    @Override
    public Optional<DomPublicId> publicId() {
        if(null==this.publicId) {;
            this.publicId = DomPublicId.with(this.entityNode().getPublicId());
        }
        return this.publicId;
    }

    Optional<DomPublicId> publicId;

    // HasSystemId ...................................................................................................

    @Override
    public Optional<DomSystemId> systemId() {
        if(null==this.systemId) {;
            this.systemId = DomSystemId.with(this.entityNode().getSystemId());
        }
        return this.systemId;
    }

    Optional<DomSystemId> systemId;

    // notation ...................................................................................................

    public DomName notation() {
        if(null==this.notation) {
            this.notation = DomNodeKind.NOTATION.with(this.entityNode().getNotationName());
        }
        return this.notation;
    }

    private DomName notation;

    // inputEncoding ...................................................................................................

    public Optional<String> inputEncoding() {
        if(null==this.inputEncoding) {
            this.inputEncoding = Optional.ofNullable(this.entityNode().getInputEncoding());
        }
        return this.inputEncoding;
    }

    private Optional<String> inputEncoding;

    // xmlEncoding ...................................................................................................

    public Optional<String> xmlEncoding() {
        if(null==this.xmlEncoding) {
            this.xmlEncoding = Optional.ofNullable(this.entityNode().getXmlEncoding());
        }
        return this.xmlEncoding;
    }

    private Optional<String> xmlEncoding;

    // xmlVersion ...................................................................................................

    public Optional<String> xmlVersion() {
        if(null==this.xmlVersion) {
            this.xmlVersion = Optional.ofNullable(this.entityNode().getXmlVersion());
        }
        return this.xmlVersion;
    }

    private Optional<String> xmlVersion;

    // children................................................................................................

    @Override
    public DomEntity setChildren(final List<DomNode> children) {
        throw new UnsupportedOperationException();
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

    // DomNode........................................................................

    @Override
    DomNodeKind kind(){
        return DomNodeKind.ENTITY_REFERENCE;
    }

    @Override
    DomEntity wrap0(final org.w3c.dom.Node node) {
        return new DomEntity(node);
    }

    /**
     * Always returns this.
     */
    @Override
    public boolean isEntity() {
        return true;
    }

    // Object...................................................................................................

    @Override
    public int hashCode() {
        return this.name().hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DomEntity;
    }

    @Override boolean equalsIgnoringParentAndChildren(final DomNode other) {
        return equalsIgnoringParentAndChildren0(other.cast());
    }

    private boolean equalsIgnoringParentAndChildren0(final DomEntity other) {
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
        builder.append(DomEntity.OPEN);

        builder.separator(" ");
        builder.append(' '); // YUCK
        builder.value(this.name().value());

        buildToString(this.publicId(),this.systemId(), builder);

        builder.append(DomEntity.CLOSE);
    }
}
