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

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeAttributeName;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An immutable element.
 */
public final class XmlElement extends XmlParentNode2 implements HasXmlNameSpacePrefix {

    static XmlElement with(final Node node) {
        return new XmlElement(node);
    }

    private XmlElement(final Node node) {
        super(node);
    }

    private Element elementNode() {
        return Cast.to(this.node);
    }

    // HasXmlNameSpacePrefix................................................................................

    @Override
    public Optional<XmlNameSpacePrefix> prefix() {
        if (null == this.prefix) {
            this.prefix = XmlNameSpacePrefix.wrap(this.elementNode());
        }
        return this.prefix;
    }

    private Optional<XmlNameSpacePrefix> prefix;

    public XmlElement setPrefix(final Optional<XmlNameSpacePrefix> prefix) {
        Objects.requireNonNull(prefix, "prefix");

        return this.prefix().equals(prefix) ?
                this :
                this.replacePrefix(prefix);
    }

    private XmlElement replacePrefix(final Optional<XmlNameSpacePrefix> prefix) {
        final Element element = Cast.to(this.nodeCloneAll());
        element.setPrefix(prefix.map((p)-> p.value()).orElse(null));
        return new XmlElement(element);
    }

    // namespaceUri .....................................................................................

    public Optional<String> nameSpaceUri() {
        if (null == this.nameSpaceUri) {
            this.nameSpaceUri = Optional.ofNullable(this.elementNode().getNamespaceURI());
        }
        return this.nameSpaceUri;
    }

    private Optional<String> nameSpaceUri;

    // children................................................................................................

    @Override
    public XmlElement setChildren(final List<XmlNode> children) {
        return this.setChildren0(children).cast();
    }

    @Override
    public XmlElement appendChild(final XmlNode child) {
        return super.appendChild(child).cast();
    }

    @Override
    public XmlElement removeChild(final int child) {
        return super.removeChild(child).cast();
    }

    // attributes................................................................................
    // TODO Attr.getSpecified ???

    @Override
    public Map<XmlAttributeName, String> attributes() {
        if (null == this.attributes) {
            this.attributes = XmlAttributeMap.from(this.elementNode().getAttributes());
        }
        return this.attributes;
    }

    private Map<XmlAttributeName, String> attributes;

    @Override
    public XmlElement setAttributes(final Map<XmlAttributeName, String> attributes) {
        Objects.requireNonNull(attributes, "attributes");

        return this.attributes().equals(attributes) ? this : this.replaceAttributes(attributes);
    }

    private XmlElement replaceAttributes(final Map<XmlAttributeName, String> attributes) {
        final Element element = Cast.to(this.nodeCloneAll());

        final NamedNodeMap attributeNodes = element.getAttributes();
        final int count = attributeNodes.getLength();
        for (int i = 0; i < count; i++) {
            element.removeAttributeNode(Cast.to(attributeNodes.item(i)));
        }

        for (Entry<XmlAttributeName, String> nameAndValue : attributes.entrySet()) {
            final XmlAttributeName name = nameAndValue.getKey();
            final Optional<XmlNameSpacePrefix> prefix = name.prefix();
            final String value = nameAndValue.getValue();

            if (prefix.isPresent()) {
                prefix.get().setAttribute(element, name, value);
            } else {
                element.setAttribute(name.value(), value);
            }
        }

        return this.wrap0(element);
    }

    // parent................................................................................................

    @Override
    public XmlElement removeParent() {
        return this.removeParent0().cast();
    }

    // XmlNode................................................................................................

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.ELEMENT;
    }

    @Override
    XmlElement wrap0(final Node node) {
        return new XmlElement(node);
    }

    @Override
    public boolean isElement() {
        return true;
    }

    @Override
    final SearchNode toSearchNode0() {
        return this.toSearchNode1().setAttributes(this.attributes().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> SearchNodeAttributeName.with(e.getKey().value()),
                        e -> e.getValue())));
    }

    @Override
    SearchNodeName searchNodeName() {
        return SearchNodeName.with(this.name().value());
    }

    // Object................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.name(), this.children(), this.attributes());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlElement;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final XmlNode other) {
        return equalsIgnoringParentAndChildren0(Cast.to(other));
    }

    private boolean equalsIgnoringParentAndChildren0(final XmlElement other) {
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
        final XmlName elementName = this.name();

        this.prefix()
                .ifPresent((v) -> builder.label(v.value()));

        builder.value(elementName);

        // attributes
        builder.separator(" ");
        builder.enable(ToStringBuilderOption.QUOTE);
        builder.labelSeparator("=");
        builder.value(this.attributes());

        // empty tag if no children.
        final List<XmlNode> children = this.children();
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
