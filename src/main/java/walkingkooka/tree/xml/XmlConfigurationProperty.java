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

import org.w3c.dom.DOMErrorHandler;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link org.w3c.dom.DOMConfiguration} equivalents, used by {@link XmlDocument} to get, set values.
 */
enum XmlConfigurationProperty {
    CANONICAL_FORM("canonical-form"),
    CDATA_SECTIONS("cdata-sections"),
    CHECK_CHARACTER_NORMALIZATIONS("check-character-normalization"),
    COMMENTS("comments"),
    DATATYPE_NORMALIZATION("datatype-normalization"),
    ELEMENT_CONTENT_WHITESPACE("element-content-whitespace"),
    ENTITIES("entities"),
    ERROR_HANDLER("error-handler"),
    INFOSET("infoset"),
    NAMESPACES("namespaces"),
    NAMESPACE_DECLARATION("namespace-declarations"),
    NORMALIZE_CHARACTERS("normalize-characters"),
    SCHEMA_LOCATION("schema-location"),
    SCHEMA_TYPE("schema-type"),
    SPLIT_CDATA_SECTION("split-cdata-sections"),
    VALIDATE("validate"),
    VALIDATE_IF_SCHEMA("validate-if-schema"),
    WELL_FORMED("well-formed");

    XmlConfigurationProperty(final String name) {
        this.name = name;
    }

    final String name;

    // boolean ..............................................................................................

    final boolean booleanValue(final XmlDocument document) {
        return Boolean.TRUE.equals(this.getAndCast(document, Boolean.class));
    }

    final XmlDocument setBooleanValue(final XmlDocument document, final boolean value) {
        return Objects.equals(value, this.booleanValue(document)) ?
                document :
                replace(document, value);
    }

    // errorHandler ..............................................................................................

    final Optional<DOMErrorHandler> errorHandler(final XmlDocument document) {
        return Optional.ofNullable((DOMErrorHandler) document.documentNode().getDomConfig().getParameter(this.name));
    }

    final XmlDocument setErrorHandler(final XmlDocument document, final Optional<? extends DOMErrorHandler> value) {
        return Objects.equals(value, this.errorHandler(document)) ?
                document :
                replace(document, value);
    }

    // string ..............................................................................................

    final Optional<String> string(final XmlDocument document) {
        return Optional.ofNullable(this.getAndCast(document, String.class));
    }

    final XmlDocument setString(final XmlDocument document, final Optional<String> value) {
        return Objects.equals(value, this.string(document)) ?
                document :
                replace(document, value);
    }

    // helpers ..............................................................................................

    private <T> T getAndCast(final XmlDocument document, final Class<T> cast) {
        return cast.cast(this.get(document));
    }

    final Object get(final XmlDocument document) {
        return document.documentNode().getDomConfig().getParameter(this.name);
    }

    private XmlDocument replace(final XmlDocument document, final Optional<?> value) {
        return this.replace(document, value.orElse(null));
    }

    private XmlDocument replace(final XmlDocument document, final Object value) {
        try {
            final Document documentNode = document.nodeCloneAll();
            documentNode.getDomConfig().setParameter(this.name, value);
            return XmlDocument.with(documentNode);
        } catch (final DOMException cause) {
            throw new XmlException("Unable to set " + this.name + " with " + value + ", " + cause.getMessage(), cause);
        }
    }
}
