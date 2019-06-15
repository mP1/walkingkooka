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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.Value;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharacterConstant;

import java.util.Optional;

/**
 * The prefix component of a namespace.
 */
final public class XmlNameSpacePrefix implements Value<String>, UsesToStringBuilder, HashCodeEqualsDefined {

    /**
     * The separator character between a prefix and attribute.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with(':');

    final static String XMLNS_STRING = "xmlns";
    final static XmlNameSpacePrefix XMLNS = new XmlNameSpacePrefix(XmlNameSpacePrefix.XMLNS_STRING);

    static Optional<XmlNameSpacePrefix> wrap(final Node node) {
        final String prefix = node.getPrefix();
        return null == prefix ? Optional.empty() : Optional.ofNullable(new XmlNameSpacePrefix(prefix));
    }

    static XmlNameSpacePrefix with(final String prefix) {
        return XMLNS_STRING.equals(prefix) ? XMLNS : new XmlNameSpacePrefix(prefix);
    }

    /**
     * Private constructor use factory.
     */
    private XmlNameSpacePrefix(final String value) {
        super();
        this.value = value;
    }

    /**
     * Returns the prefix wrap an empty {@link String} indicating it is absent. Null is an invalid value.
     */
    @Override
    public String value() {
        return this.value;
    }

    final String value;

    /**
     * Factory that creates an attribute name.
     */
    public XmlAttributeName attributeName(final String name) {
        return XmlNode.attribute(name, Optional.of(this));
    }

    XmlElement createElement(final String namespaceUri,
                             final XmlName name,
                             final Document document) {
        return XmlElement.with(document.createElementNS(namespaceUri,
                this.qualifiedName(name)));
    }

    void setAttribute(final Element element, final XmlAttributeName name, final String value) {
        if (this == XMLNS) {
            element.setAttributeNS(XmlNode.XMLNS_URI,
                    this.qualifiedName(name),
                    value);
        } else {
            element.setAttributeNS(element.lookupNamespaceURI(this.value),
                    this.qualifiedName(name),
                    value);
        }
    }

    private String qualifiedName(final Name name) {
        return value + SEPARATOR.character() + name.value();
    }

    // Object .............................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof XmlNameSpacePrefix) && this.equals0((XmlNameSpacePrefix) other));
    }

    private boolean equals0(final XmlNameSpacePrefix other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.valueSeparator("");
        builder.separator("");
        builder.value(this.value);
        builder.value(XmlNameSpacePrefix.SEPARATOR);
    }
}
