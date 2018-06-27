package walkingkooka.xml;

import org.w3c.dom.DOMErrorHandler;

import java.util.Objects;
import java.util.Optional;

/**
 * {@link org.w3c.dom.DOMConfiguration} equivalents, used by {@link DomDocument} to get, set values.
 */
enum DomConfigurationProperty {
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

    DomConfigurationProperty(final String name) {
        this.name = name;
    }

    final String name;

    // boolean ..............................................................................................

    final boolean booleanValue(final DomDocument document) {
        return Boolean.TRUE.equals(this.getAndCast(document, Boolean.class));
    }

    final DomDocument setBooleanValue(final DomDocument document, final boolean value) {
        return Objects.equals(value, this.booleanValue(document)) ?
                document :
                replace(document, value);
    }

    // errorHandler ..............................................................................................

    final Optional<DOMErrorHandler> errorHandler(final DomDocument document) {
        return Optional.ofNullable((DOMErrorHandler) document.documentNode().getDomConfig().getParameter(this.name));
    }

    final DomDocument setErrorHandler(final DomDocument document, final Optional<? extends DOMErrorHandler> value) {
        return Objects.equals(value, this.errorHandler(document)) ?
                document :
                replace(document, value);
    }

    // string ..............................................................................................

    final Optional<String> string(final DomDocument document) {
        return Optional.ofNullable(this.getAndCast(document, String.class));
    }

    final DomDocument setString(final DomDocument document, final Optional<String> value) {
        return Objects.equals(value, this.string(document)) ?
                document :
                replace(document, value);
    }

    // helpers ..............................................................................................

    private <T> T getAndCast(final DomDocument document, final Class<T> cast) {
        return cast.cast(this.get(document));
    }

    final Object get(final DomDocument document) {
        return document.documentNode().getDomConfig().getParameter(this.name);
    }

    private DomDocument replace(final DomDocument document, final Optional<?> value) {
        return this.replace(document, value.isPresent() ? value.get() : null);
    }

    private DomDocument replace(final DomDocument document, final Object value) {
        try {
            final org.w3c.dom.Document documentNode = document.nodeCloneAll();
            documentNode.getDomConfig().setParameter(this.name, value);
            return new DomDocument(documentNode);
        } catch (final org.w3c.dom.DOMException cause) {
            throw new DomException("Unable to set " + this.name + " with " + value + ", " + cause.getMessage(), cause);
        }
    }
}
