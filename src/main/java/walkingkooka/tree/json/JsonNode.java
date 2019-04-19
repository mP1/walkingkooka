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

package walkingkooka.tree.json;

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printer;
import walkingkooka.io.printer.Printers;
import walkingkooka.naming.Name;
import walkingkooka.naming.PathSeparator;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.HasText;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.json.JsonNodeParserContext;
import walkingkooka.text.cursor.parser.json.JsonNodeParserContexts;
import walkingkooka.text.cursor.parser.json.JsonNodeParserToken;
import walkingkooka.text.cursor.parser.json.JsonNodeParsers;
import walkingkooka.tree.Node;
import walkingkooka.tree.search.HasSearchNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Base class for all json nodes, all of which are immutable. Note that performing a seemingly mutable operation
 * actually returns a new graph of nodes as would be expected including all parents and the root.
 */
public abstract class JsonNode implements Node<JsonNode, JsonNodeName, Name, Object>,
        HasSearchNode,
        HasText,
        HasJsonNode,
        HashCodeEqualsDefined {

    /**
     * Simply returns the given {@link JsonNode}.
     */
    public static JsonNode fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");
        return node;
    }

    /**
     * Parsers the given json and returns its {@link JsonNode} equivalent.
     */
    public static JsonNode parse(final String text) {
        final JsonNodeParserToken token = PARSER.parse(TextCursors.charSequence(text),
                JsonNodeParserContexts.basic())
                .get()
                .cast();
         return token.toJsonNode().get();
    }

    /**
     * Parser that will consume json or report a parsing error.
     */
    private final static Parser<JsonNodeParserContext> PARSER = JsonNodeParsers.value()
            .orReport(ParserReporters.basic())
            .cast();

    public static JsonArrayNode array() {
        return JsonArrayNode.EMPTY;
    }

    public static JsonBooleanNode booleanNode(final boolean value) {
        return JsonBooleanNode.with(value);
    }

    public static JsonNullNode nullNode() {
        return JsonNullNode.INSTANCE;
    }

    public static JsonNumberNode number(final double value) {
        return JsonNumberNode.with(value);
    }

    public static JsonObjectNode object() {
        return JsonObjectNode.EMPTY;
    }

    public static JsonStringNode string(final String value) {
        return JsonStringNode.with(value);
    }

    /**
     * The {@link PathSeparator} for node selector paths.
     */
    public static final PathSeparator PATH_SEPARATOR = PathSeparator.notRequiredAtStart('/');

    private final static Optional<JsonNode> NO_PARENT = Optional.empty();

    /**
     * Package private ctor to limit sub classing.
     */
    JsonNode(final JsonNodeName name, final int index) {
        this.name = name;
        this.parent = NO_PARENT;
        this.index = index;
    }

    // Name ..............................................................................................

    @Override
    public final JsonNodeName name() {
        return this.name;
    }

    /**
     * Returns an instance with the given name, creating a new instance if necessary.
     */
    abstract public JsonNode setName(final JsonNodeName name);

    static void checkName(final JsonNodeName name) {
        Objects.requireNonNull(name, "name");
    }

    final JsonNode setName0(final JsonNodeName name) {
        return this.name.equals(name) ?
               this :
               this.replaceName(name);

    }

    final JsonNodeName name;

    /**
     * Returns a new instance with the given name.
     */
    private JsonNode replaceName(final JsonNodeName name) {
        return this.create(name, this.index);
    }

    @Override
    public final boolean hasUniqueNameAmongstSiblings() {
        return true;
    }

    // parent ..................................................................................................

    @Override
    public final Optional<JsonNode> parent() {
        return this.parent;
    }

    /**
     * Sub classes should call this and cast.
     */
    final JsonNode removeParent0() {
        return this.isRoot() ?
                this :
                this.create(this.defaultName(), NO_INDEX);
    }

    /**
     * This setter is used to recreate the entire graph including parents of parents receiving new children.
     * It is only ever called by a parent node and is used to adopt new children.
     */
    final JsonNode setParent(final Optional<JsonNode> parent,
                             final JsonNodeName name,
                             final int index) {
        final JsonNode copy = this.create(name, index);
        copy.parent = parent;
        return copy;
    }

    private Optional<JsonNode> parent;

    /**
     * Replaces this {@link JsonNode} with the given {@link JsonNode} providing its different, keeping the parent and siblings.
     * Note the replaced {@link JsonNode} will gain the name of the previous.
     */
    @Override
    public final JsonNode replace(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return this.isRoot() ?
                node.removeParent() :
                this.replace0(node);
    }

    private JsonNode replace0(final JsonNode node) {
        return this.parent()
                .map(p -> p.setChild(this.name, node).children().get(this.index))
                .orElse(node);
    }

//    abstract JsonNode

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract JsonNode setChild0(final JsonNode newChild, final int index);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final JsonNode replaceChild(final Optional<JsonNode> previousParent, final int index) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild0(this, index) :
                this;
    }

    // index............................................................................................................

    @Override
    public final int index() {
        return this.index;
    }

    final int index;

    /**
     * Factory method that creates a new sub class of {@link JsonLeafNode} that is the same type as this.
     */
    abstract JsonNode create(final JsonNodeName name, final int index);

    // attributes............................................................................................................

    @Override
    public final Map<Name, Object> attributes() {
        return Maps.empty();
    }

    @Override
    public final JsonNode setAttributes(final Map<Name, Object> attributes) {
        Objects.requireNonNull(attributes, "attributes");
        throw new UnsupportedOperationException();
    }

    // Value<Object>................................................................................................

    public abstract Object value();

    /**
     * If a {@link JsonBooleanNode} returns the boolean value or fails.
     */
    public final boolean booleanValueOrFail() {
        return this.valueOrFail(Boolean.class);
    }

    /**
     * If a {@link JsonNumberNode} returns the number value or fails.
     */
    public final Number numberValueOrFail() {
        return this.valueOrFail(Number.class);
    }

    /**
     * If a {@link JsonStringNode} returns the string value or fails.
     */
    public final String stringValueOrFail() {
        return this.valueOrFail(String.class);
    }

    private <V> V valueOrFail(final Class<V> type) {
        if (this.isNull()) {
            this.reportInvalidNode(type);
        }

        try {
            return type.cast(this.value());
        } catch (final ClassCastException | UnsupportedOperationException fail) {
            return this.reportInvalidNode(type);
        }
    }

    /**
     * Type safe cast that reports a nice message about the failing array.
     */
    public abstract JsonArrayNode arrayOrFail();

    /**
     * Type safe cast that reports a nice message about the failing object.
     */
    public abstract JsonObjectNode objectOrFail();

    /**
     * Reports a json node is not an object.
     */
    final <V> V reportInvalidNodeObject() {
        return this.reportInvalidNode(Object.class);
    }

    /**
     * Reports a json node is not an array.
     */
    final <V> V reportInvalidNodeArray() {
        return this.reportInvalidNode("Array");
    }

    /**
     * Reports a failed attempt to extract a value or cast a node.
     */
    final <V> V reportInvalidNode(final Class<?> type) {
        return reportInvalidNode(type.getSimpleName());
    }

    /**
     * Reports a failed attempt to extract a value or cast a node.
     */
    final <V> V reportInvalidNode(final String type) {
        throw new JsonNodeException("Node is not a " + type + "=" + this);
    }

    // isXXX............................................................................................................

    abstract public boolean isArray();

    abstract public boolean isBoolean();

    abstract public boolean isNumber();

    abstract public boolean isNull();

    abstract public boolean isObject();

    abstract public boolean isString();

    /**
     * Unsafe cast to a sub class of {@link JsonNode}.
     */
    public final <T extends JsonNode> T cast() {
        return Cast.to(this);
    }

    abstract void accept(final JsonNodeVisitor visitor);

    // functional methods ..........................................................................................

    /**
     * Returns an {@link Optional} holding this {@link JsonNode} if not a {@link JsonNullNode}.
     */
    public abstract <N extends JsonNode> Optional<N> optional();

    // HasJsonNode .......................................................................................................

    /**
     * Attempts to convert this node to the requested {@link Class type}.
     */
    public final <T> T fromJsonNode(final Class<T> type) {
        Objects.requireNonNull(type, "type");

        return this.fromJsonNode0(type);
    }

    abstract <T> T fromJsonNode0(final Class<T> type);

    /**
     * Assumes this json object is an array holding elements that will be converted to the requested element type, returning
     * a {@link Set} of them.
     */
    public final <T> List<T> fromJsonNodeList(final Class<T> elementType) {
        Objects.requireNonNull(elementType, "elementType");

        return this.fromJsonNodeList0(elementType);
    }

    abstract <T> List<T> fromJsonNodeList0(final Class<T> elementType);

    /**
     * Assumes this json object is an array holding elements that will be converted to the requested element type, returning
     * a {@link Set} of them.
     */
    public final <T> Set<T> fromJsonNodeSet(final Class<T> elementType) {
        Objects.requireNonNull(elementType, "elementType");

        return this.fromJsonNodeSet0(elementType);
    }

    abstract <T> Set<T> fromJsonNodeSet0(final Class<T> elementType);

    /**
     * Assumes this json object is an array holding elements holding elements of the requested element type, returning
     * a {@link Map} of them.
     */
    public final <K, V> Map<K, V> fromJsonNodeMap(final Class<K> keyType, final Class<V> valueType) {
        Objects.requireNonNull(keyType, "keyType");
        Objects.requireNonNull(valueType, "valueType");

        return this.fromJsonNodeMap0(keyType, valueType);
    }

    abstract <K, V> Map<K, V> fromJsonNodeMap0(final Class<K> keyType, final Class<V> valueType);

    // fromJsonNodeWithType.......................................................................................................

    /**
     * Assumes a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    public abstract <T> List<T> fromJsonNodeWithTypeList();

    /**
     * Assumes a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    public abstract <T> Set<T> fromJsonNodeWithTypeSet();

    /**
     * Assumes a {@link JsonArrayNode} holding entries of the {@link Map} tagged with type and values.
     */
    public abstract <K, V> Map<K, V> fromJsonNodeWithTypeMap();

    /**
     * Assumes a wrapper object with the type and value, basically the inverse of {@link HasJsonNode#toJsonNodeWithType()}.
     */
    public abstract <T> T fromJsonNodeWithType();

    // toJsonNode.......................................................................................................

    /**
     * Already a {@link JsonNode} remove the parent if necessary.
     */
    @Override
    public final JsonNode toJsonNode() {
        return this.isRoot() ?
                this :
                this.setParent(NO_PARENT, this.defaultName(), NO_INDEX);
    }

    abstract JsonNodeName defaultName();

    // Object .......................................................................................................

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
               this.canBeEqual(other) &&
               this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final JsonNode other) {
        return this.equalsAncestors(other) && this.equalsDescendants(other);
    }

    private boolean equalsAncestors(final JsonNode other) {
        boolean result = this.equalsNameAndValue(other);

        if(result) {
            final Optional<JsonNode> parent = this.parent();
            final Optional<JsonNode> otherParent = other.parent();
            final boolean hasParent = parent.isPresent();
            final boolean hasOtherParent = otherParent.isPresent();

            if (hasParent) {
                if (hasOtherParent) {
                    result = parent.get().equalsAncestors(otherParent.get());
                }
            } else {
                // result is only true if other is false
                result = !hasOtherParent;
            }
        }

        return result;
    }

    final boolean equalsNameValueAndDescendants(final JsonNode other) {
        return this.canBeEqual(other) &&
                this.equalsNameAndValue(other) &&
                this.equalsDescendants(other);
    }

    abstract boolean equalsDescendants(final JsonNode other);

    private boolean equalsNameAndValue(final JsonNode other) {
        return this.name.equals(other.name) &&
               this.equalsValue(other);
    }

    /**
     * Sub classes should do equals but ignore the parent and children properties.
     */
    abstract boolean equalsValue(final JsonNode other);
    /**
     * Pretty prints the entire json graph.
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.SYSTEM));
        this.printJson(printer);
        return b.toString();
    }

    /**
     * Prints this node to the printer.<br>
     * To control indentation amount try {@link IndentingPrinters#fixed(Printer, Indentation)}.
     * Other combinations of printers can be used to ignore printing all possible optional whitespace.
     */
    public final void printJson(final IndentingPrinter printer) {
        Objects.requireNonNull(printer, "printer");
        this.printJson0(printer);
        printer.flush();
    }

    abstract void printJson0(final IndentingPrinter printer);
}
