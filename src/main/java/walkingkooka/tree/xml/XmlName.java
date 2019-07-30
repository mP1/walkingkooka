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
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import java.io.Serializable;
import java.util.Map;

/**
 * A dom name, which comes in different flavours for the various dom node types.
 */
public final class XmlName implements Name,
        Comparable<XmlName>,
        Serializable {

    private final static Map<String, XmlName> constants = Maps.hash();

    final static XmlName CDATA_SECTION = registerConstant(XmlNodeKind.CDATA, "#cdata-section");

    final static XmlName COMMENT = registerConstant(XmlNodeKind.COMMENT, "#comment");

    final static XmlName DOCUMENT = registerConstant(XmlNodeKind.DOCUMENT, "#document");

    public static XmlName documentType(final String name) {
        return XmlNodeKind.DOCUMENT_TYPE.with(name);
    }

    public static XmlName element(final String name) {
        return XmlNodeKind.ELEMENT.with(name);
    }

    public static XmlName entity(final String name) {
        return XmlNodeKind.ENTITY.with(name);
    }

    public static XmlName entityReference(final String name) {
        return XmlNodeKind.ENTITY_REFERENCE.with(name);
    }

    public static XmlName notation(final String name) {
        return XmlNodeKind.NOTATION.with(name);
    }

    final static XmlName TEXT = registerConstant(XmlNodeKind.COMMENT, "#text");

    private static XmlName registerConstant(final XmlNodeKind kind, final String value) {
        final XmlName name = kind.with(value);
        constants.put(value, name);
        return name;
    }

    static XmlName with(final String name, final XmlNodeKind kind) {
        return new XmlName(name, kind);
    }

    private XmlName(final String name, final XmlNodeKind kind) {
        super();
        this.name = name;
        this.kind = kind;
    }

    /**
     * Returns the name as a {@link String}.
     */
    @Override
    public String value() {
        return this.name;
    }

    private final String name;
    private final XmlNodeKind kind;

    final XmlElement createElement(final Document document) {
        return this.kind.createElement(this, document);
    }

    final XmlElement createElement(final Document document,
                                   final String namespaceUri,
                                   final XmlNameSpacePrefix prefix) {
        return this.kind.createElement(namespaceUri, prefix, this, document);
    }

    final XmlElement failInvalidTagName() {
        throw new IllegalArgumentException("Invalid tag name " + CharSequences.quote(this.value()));
    }

    // Object..................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof XmlName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final XmlName other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final XmlName other) {
        int value = CASE_SENSITIVITY.comparator().compare(this.name, other.name);
        if (0 == value) {
            value = CASE_SENSITIVITY.comparator().compare(this.kind.name(), other.kind.name());
        }
        return value;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    // Serializable.....................................................................................................

    Object readResolve() {
        final XmlName constant = constants.get(this.name);
        return null != constant ? constant : this;
    }

    private final static long serialVersionUID = 1L;
}
