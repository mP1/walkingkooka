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

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of an {@link XmlElement} attribute.
 */
final public class XmlAttributeName implements Name,
        Comparable<XmlAttributeName>,
        HasXmlNameSpacePrefix,
        UsesToStringBuilder {

    /**
     * Contains a {@link String} which is invalid and fails if passed to the factory
     */
    public final static String INVALID = "1 invalid attribute name";

    private final static String XMLNS_STRING = "xmlns";

    public final static Optional<XmlNameSpacePrefix> NO_PREFIX = Optional.empty();

    //root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:z", NSURI);
    public final static XmlAttributeName XMLNS = new XmlAttributeName(XMLNS_STRING, XmlNode.NO_PREFIX);

    /**
     * Factory that creates a {@link XmlAttributeName}
     */
    public static XmlAttributeName with(final String name,
                                        final Optional<XmlNameSpacePrefix> prefix) {
        CharSequences.failIfNullOrEmpty(name, "name");
        Objects.requireNonNull(prefix, "prefix");

        return name.equals(XMLNS_STRING) ? XMLNS : new XmlAttributeName(name, prefix);
    }

    static XmlAttributeName wrap(final org.w3c.dom.Attr attr) {
        final Optional<XmlNameSpacePrefix> prefix = XmlNameSpacePrefix.wrap(attr);
        return new XmlAttributeName(prefix.isPresent() ?
                attr.getLocalName() :
                attr.getName(),
                prefix);
    }

    /**
     * Private constructor
     */
    private XmlAttributeName(final String name, final Optional<XmlNameSpacePrefix> prefix) {
        super();
        this.name = name;
        this.prefix = prefix;
    }

    @Override
    public String value() {
        return this.name;
    }

    final String name;

    /**
     * This method is only ever called by {@link XmlElement} when creating a prefix result for a namespaceUri lookup.
     */
    @Override
    public Optional<XmlNameSpacePrefix> prefix() {
        return prefix;
    }

    /**
     * Would be setter that returns an instance with the given {@link XmlNameSpacePrefix}
     */
    public XmlAttributeName setPrefix(final Optional<XmlNameSpacePrefix> prefix) {
        Objects.requireNonNull(prefix, "prefix");
        return this.prefix.equals(prefix) ? this : new XmlAttributeName(this.name, prefix);
    }

    /**
     * The prefix if one was present.
     */
    final Optional<XmlNameSpacePrefix> prefix;

    // Object

    @Override
    public final int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public final boolean equals(final Object other) {
        return (this == other) || other instanceof XmlAttributeName && this.equals0((XmlAttributeName) other);
    }

    private boolean equals0(final XmlAttributeName other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public int compareTo(final XmlAttributeName other) {
        int value = CASE_SENSITIVITY.comparator().compare(this.name, other.name);
        if (0 == value) {
            value = CASE_SENSITIVITY.comparator().compare(
                    this.prefixValue(this.prefix),
                    this.prefixValue(other.prefix));
        }
        return value;
    }

    private String prefixValue(final Optional<XmlNameSpacePrefix> prefix) {
        return prefix.isPresent() ? prefix.get().value : "";
    }

    // UsesToStringBuilder

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.labelSeparator(XmlNameSpacePrefix.SEPARATOR.string());

        this.prefix()
                .ifPresent((v) -> builder.label(v.value()));

        builder.value(this.value());
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
