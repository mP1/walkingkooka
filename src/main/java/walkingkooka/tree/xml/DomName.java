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
 *
 */

package walkingkooka.tree.xml;

import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import java.io.Serializable;
import java.util.Map;

/**
 * A dom name, which comes in different flavours for the various dom node types.
 */
public final class DomName implements Name, HashCodeEqualsDefined,
        Serializable {

    private final static Map<String, DomName> constants = Maps.hash();

    final static DomName CDATA_SECTION = registerConstant(DomNodeKind.CDATA, "#cdata-section");

    final static DomName COMMENT = registerConstant(DomNodeKind.COMMENT, "#comment");

    final static DomName DOCUMENT = registerConstant(DomNodeKind.DOCUMENT, "#document");

    public static DomName documentType(final String name) {
        return DomNodeKind.DOCUMENT_TYPE.with(name);
    }

    public static DomName element(final String name) {
        return DomNodeKind.ELEMENT.with(name);
    }

    public static DomName entity(final String name) {
        return DomNodeKind.ENTITY.with(name);
    }

    public static DomName entityReference(final String name) {
        return DomNodeKind.ENTITY_REFERENCE.with(name);
    }

    public static DomName notation(final String name) {
        return DomNodeKind.NOTATION.with(name);
    }

    final static DomName TEXT = registerConstant(DomNodeKind.COMMENT, "#text");

    private static DomName registerConstant(final DomNodeKind kind, final String value) {
        final DomName name = kind.with(value);
        constants.put(value, name);
        return name;
    }

    DomName(final String name, final DomNodeKind kind) {
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

    final String name;
    final DomNodeKind kind;

    final DomElement createElement(final org.w3c.dom.Document document) {
        return this.kind.createElement(this, document);
    }

    final DomElement createElement(final org.w3c.dom.Document document,
                                   final String namespaceUri,
                                   final DomNameSpacePrefix prefix) {
        return this.kind.createElement(namespaceUri, prefix, this, document);
    }

    final DomElement failInvalidTagName() {
        throw new IllegalArgumentException("Invalid tag name " + CharSequences.quote(this.value()));
    }

    // Object

    @Override
    public final int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return (this == other) || other instanceof DomName && this.equals0((DomName) other);
    }

    private boolean equals0(final DomName other) {
        return this.name.equals(other.name) && this.kind == other.kind;
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Serializable

    Object readResolve() {
        final DomName constant = constants.get(this.name);
        return null != constant ? constant : this;
    }

    private final static long serialVersionUID = 1L;
}
