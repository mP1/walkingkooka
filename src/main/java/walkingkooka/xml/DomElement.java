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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

/**
 * An immutable element.
 */
public final class DomElement extends DomParentNode2 implements HasDomPrefix{

    DomElement(final org.w3c.dom.Node node) {
        super(node);
    }

    private org.w3c.dom.Element elementNode() {
        return Cast.to(this.node);
    }

    // HasDomPrefix................................................................................

    @Override
    public Optional<DomNameSpacePrefix> prefix() {
        if(null==this.prefix) {
            this.prefix = DomNameSpacePrefix.wrap(this.elementNode());
        }
        return this.prefix;
    }

    private Optional<DomNameSpacePrefix> prefix;

    public DomElement setPrefix(final Optional<DomNameSpacePrefix> prefix) {
        Objects.requireNonNull(prefix, "prefix");

        return this.prefix().equals(prefix) ?
                this :
                this.replacePrefix(prefix);
    }

    private DomElement replacePrefix(final Optional<DomNameSpacePrefix> prefix) {
        final org.w3c.dom.Element element = Cast.to(this.nodeCloneAll());
        element.setPrefix(valueOrNull0(prefix));
        return new DomElement(element);
    }

    // namespaceUri .....................................................................................

    public Optional<String> nameSpaceUri() {
        if(null == this.nameSpaceUri) {
            this.nameSpaceUri = Optional.ofNullable(this.elementNode().getNamespaceURI());
        }
        return this.nameSpaceUri;
    }

    private Optional<String> nameSpaceUri;

    // children................................................................................................

    @Override
    public DomElement setChildren(final List<DomNode> children) {
        return this.setChildren0(children).asElement();
    }

    @Override
    public DomElement appendChild(final DomNode child) {
        return super.appendChild(child).asElement();
    }

    // attributes................................................................................
    // TODO Attr.getSpecified ???

    @Override
    public Map<DomAttributeName, String> attributes() {
        if(null == this.attributes) {
            this.attributes = DomAttributeMap.from(this.elementNode().getAttributes());
        }
        return this.attributes;
    }

    private Map<DomAttributeName, String> attributes;

    @Override
    public DomElement setAttributes(final Map<DomAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        return this.attributes().equals(attributes) ? this : this.replaceAttributes(attributes);
    }

    private DomElement replaceAttributes(final Map<DomAttributeName, String> attributes) {
        final org.w3c.dom.Element element = Cast.to(this.nodeCloneAll());

        final NamedNodeMap attributeNodes = element.getAttributes();
        final int count = attributeNodes.getLength();
        for(int i = 0; i < count; i++) {
            element.removeAttributeNode(Cast.to(attributeNodes.item(i)));
        }

        for(Entry<DomAttributeName, String> nameAndValue : attributes.entrySet()) {
            final DomAttributeName name = nameAndValue.getKey();
            final Optional<DomNameSpacePrefix> prefix = name.prefix();
            final String value = nameAndValue.getValue();

            if(prefix.isPresent()){
                prefix.get().setAttribute(element, name, value);
            }else{
                element.setAttribute(name.value() , value);
            }
        }

        return this.wrap0(element);
    }

    // DomNode................................................................................................

    @Override
    DomNodeKind kind() {
        return DomNodeKind.ELEMENT;
    }

    @Override
    DomElement wrap0(final Node node) {
        return new DomElement(node);
    }

    @Override
    public DomElement asElement() {
        return this;
    }

    @Override
    public boolean isElement() {
        return true;
    }

    // Object................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.name(), this.children(), this.attributes());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DomElement;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final DomNode other) {
        return equalsIgnoringParentAndChildren0(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren0(final DomElement other) {
        return this.name().equals(other.name()) && this.attributes().equals(other.attributes());
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * Returns the elementNode name, attributes looking like valid XML.
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.valueSeparator(" ");

        // start tag
        builder.append('<');
        final DomName elementName = this.name();

        final Optional<DomNameSpacePrefix> prefix = this.prefix();
        if (prefix.isPresent()) {
            builder.label(prefix.get().value());
        }
        builder.value(elementName);

        // attributes
        builder.separator(" ");
        builder.enable(ToStringBuilderOption.QUOTE);
        builder.labelSeparator("=");
        builder.value(this.attributes());

        // empty tag if no children.
        final List<DomNode> children = this.children();
        if (children.isEmpty()) {
            builder.append("/>");
        } else {
            builder.append('>');
            builder.separator("");
            builder.value(children);

            // end tag
            builder.append("</");
            builder.value(elementName);
            builder.append('>');
        }
    }
}
