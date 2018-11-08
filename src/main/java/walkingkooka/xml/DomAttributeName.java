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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Objects;
import java.util.Optional;

/**
 * The {@link Name} of an {@link DomElement} attribute.
 */
final public class DomAttributeName implements Name, HasDomPrefix, UsesToStringBuilder, HashCodeEqualsDefined {

    /**
     * Contains a {@link String} which is invalid and fails if passed to the factory
     */
    public final static String INVALID = "1 invalid attribute name";

    private final static String XMLNS_STRING = "xmlns";

    public final static Optional<DomNameSpacePrefix> NO_PREFIX = Optional.empty();

    //root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:z", NSURI);
    public final static DomAttributeName XMLNS = new DomAttributeName(XMLNS_STRING, DomNode.NO_PREFIX);

    /**
     * Factory that creates a {@link DomAttributeName}
     */
    public static DomAttributeName with(final String name,
                                        final Optional<DomNameSpacePrefix> prefix) {
        //Predicates.failIfNullOrFalse(name, DomPredicates.attributeName(), "attribute name \"%s\"");
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(prefix, "prefix");

        return name.equals(XMLNS_STRING) ? XMLNS : new DomAttributeName(name, prefix);
    }

    static DomAttributeName wrap(final org.w3c.dom.Attr attr) {
        final Optional<DomNameSpacePrefix> prefix = DomNameSpacePrefix.wrap(attr);
        return new DomAttributeName(prefix.isPresent() ?
                attr.getLocalName() :
                attr.getName(),
                prefix);
    }

    /**
     * Private constructor
     */
    private DomAttributeName(final String name, final Optional<DomNameSpacePrefix> prefix) {
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
     * This method is only ever called by {@link DomElement} when creating a prefix result for a namespaceUri lookup.
     */
    @Override
    public Optional<DomNameSpacePrefix> prefix() {
        return prefix;
    }

    /**
     * Would be setter that returns an instance with the given {@link DomNameSpacePrefix}
     */
    public DomAttributeName setPrefix(final Optional<DomNameSpacePrefix> prefix) {
        Objects.requireNonNull(prefix, "prefix");
        return this.prefix.equals(prefix) ? this : new DomAttributeName(this.name, prefix);
    }

    /**
     * The prefix if one was present.
     */
    final Optional<DomNameSpacePrefix> prefix;

    // Object

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return (this == other) || other instanceof DomAttributeName && this.equals0((DomAttributeName) other);
    }

    private boolean equals0(final DomAttributeName other) {
        return this.name.equals(other.name) && this.prefix.equals(other.prefix);
    }

    // UsesToStringBuilder

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.labelSeparator(DomNameSpacePrefix.SEPARATOR.string());
        if (this.prefix.isPresent()) {
            builder.label(this.prefix.get().value());
        }
        builder.value(this.value());
    }
}
