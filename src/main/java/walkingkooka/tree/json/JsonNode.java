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
import walkingkooka.text.CharSequences;
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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
        return PARSER.parse(TextCursors.charSequence(text),
                JsonNodeParserContexts.basic())
                .get()
                .toJsonNode().get();
    }

    /**
     * Parser that will consume json or report a parsing error.
     */
    private final static Parser<JsonNodeParserToken, JsonNodeParserContext> PARSER = JsonNodeParsers.value()
            .orReport(ParserReporters.basic())
            .cast();

    /**
     * Accepts a value and if its supported returns a {@link JsonNode}. {@link Optional} are also unwrapped.
     */
    public static Optional<JsonNode> wrap(final Object value) {
        return Optional.ofNullable(wrap0(value));
    }

    static JsonNode wrapOrFail(final Object value) {
        final JsonNode node = wrap0(value);
        if (null == node) {
            throw new UnsupportedTypeJsonNodeException("Unsupported type " + value.getClass() + "=" + value);
        }
        return node;
    }

    private static JsonNode wrap0(final Object value) {
        JsonNode jsonNode;

        do {
            if (null == value) {
                jsonNode = JsonNode.nullNode();
                break;
            }
            if (value instanceof Optional) {
                final Optional<?> optional = Optional.class.cast(value);
                jsonNode = optional.isPresent() ?
                        wrap0(optional.get()) :
                        null;
                break;
            }
            if (value instanceof JsonNode) {
                jsonNode = JsonNode.class.cast(value);
                break;
            }
            if (value instanceof HasJsonNode) {
                jsonNode = HasJsonNode.class.cast(value).toJsonNode();
                break;
            }
            if (value instanceof Boolean) {
                jsonNode = booleanNode(Boolean.class.cast(value));
                break;
            }
            if (value instanceof Number) {
                if(value instanceof Long) {
                    throw new IllegalArgumentException("Longs will lose precision use wrapLong=" + value);
                }
                jsonNode = number(Number.class.cast(value).doubleValue());
                break;
            }
            if (value instanceof String) {
                jsonNode = string(String.class.cast(value));
                break;
            }

            // unsupported type answer is Optional.empty
            jsonNode = null;
        } while (false);

        return jsonNode;
    }

    /**
     * Special case for {@link long} values which exceed the precision of a {@link JsonNumberNode}, and so must
     * be encoded as a {@link String}
     */
    public static JsonNode wrapLong(final long value) {
        return string(LONG_PREFIX + Long.toHexString(value).toLowerCase());
    }

    /**
     * The inverse of {@link #wrapLong(long)}
     */
    public static long fromJsonNodeLong(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            final String text = node.stringValueOrFail();
            if (!text.startsWith(LONG_PREFIX)) {
                throw new IllegalArgumentException("Long string missing prefix " + CharSequences.quote(LONG_PREFIX) + "=" + node);
            }
            return Long.parseLong(text.substring(2), 16);
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Prefix included at the start of all longs encoded as a string.
     */
    private final static String LONG_PREFIX = "0x";

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

    final static Optional<JsonNode> NO_PARENT = Optional.empty();

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

    Optional<JsonNode> parent;

//    abstract JsonNode

    /**
     * Sub classes must create a new copy of the parent and replace the identified child using its index or similar,
     * and also sets its parent after creation, returning the equivalent child at the same index.
     */
    abstract JsonNode setChild(final JsonNode newChild, final int index);

    /**
     * Only ever called after during the completion of a setChildren, basically used to recreate the parent graph
     * containing this child.
     */
    final JsonNode replaceChild(final Optional<JsonNode> previousParent, final int index) {
        return previousParent.isPresent() ?
                previousParent.get()
                        .setChild(this, index) :
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
     * Already a {@link JsonNode} remove the parent if necessary.
     */
    @Override
    public JsonNode toJsonNode() {
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
        return null != other &&
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
