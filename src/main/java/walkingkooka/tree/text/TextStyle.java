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

package walkingkooka.tree.text;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link TextStyle} holds a {@link Map} of {@link TextStylePropertyName} and values.
 */
public abstract class TextStyle implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<Map<TextStylePropertyName<?>, Object>> {

    /**
     * A {@link TextStyle} with no textStyle.
     */
    public final static TextStyle EMPTY = EmptyTextStyle.instance();

    /**
     * Factory that creates a {@link TextStyle} from a {@link Map}.
     */
    public static TextStyle with(final Map<TextStylePropertyName<?>, Object> value) {
        return withTextStyleMap(TextStyleMap.with(value));
    }

    static TextStyle withTextStyleMap(final TextStyleMap map) {
        return map.isEmpty() ?
                EMPTY :
                NonEmptyTextStyle.with(map);
    }

    /**
     * Private ctor to limit sub classes.
     */
    TextStyle() {
        super();
    }

    /**
     * Returns true if the textStyle is empty.
     */
    public abstract boolean isEmpty();

    // setChildren......................................................................................................

    /**
     * Factory that returns a {@link TextStyleNode} with the given {@link TextNode}
     * and these textStyle.
     */
    public final TextNode setChildren(final List<TextNode> textNodes) {
        return TextStyleNode.with(textNodes, this.textStyleMap());
    }

    abstract TextStyleMap textStyleMap();

    // merge............................................................................................................

    /**
     * Merges the two {@link TextStyle}, with the value from this having priority when both have the same
     * {@link TextStylePropertyName}.
     */
    public final TextStyle merge(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");

        return this.merge0(textStyle);
    }

    abstract TextStyle merge0(final TextStyle textStyle);

    abstract TextStyle merge1(final NonEmptyTextStyle textStyle);

    // replace............................................................................................................

    /**
     * If empty returns the given {@link TextNode} otherwise wraps inside a {@link TextStyleNode}
     */
    public final TextNode replace(final TextNode textNode) {
        Objects.requireNonNull(textNode, "textNode");

        return this.replace0(textNode);
    }

    abstract TextNode replace0(final TextNode textNode);

    // get..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextStyle} with the new definition which may or may not
     * require creating a new {@link TextStyle}.
     */
    public final <V> Optional<V> get(final TextStylePropertyName<V> propertyName) {
        checkPropertyName(propertyName);

        return this.get0(propertyName);
    }

    abstract <V> Optional<V> get0(final TextStylePropertyName<V> propertyName);

    // set..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextStyle} with the new definition which may or may not
     * require creating a new {@link TextStyle}.
     */
    public final <V> TextStyle set(final TextStylePropertyName<V> propertyName, final V value) {
        checkPropertyName(propertyName);

        propertyName.check(value);
        return this.set0(propertyName, value);
    }

    abstract <V> TextStyle set0(final TextStylePropertyName<V> propertyName, final V value);

    // remove...........................................................................................................

    /**
     * Removes a possibly existing property returning a {@link TextStyle} without.
     */
    public final TextStyle remove(final TextStylePropertyName<?> propertyName) {
        checkPropertyName(propertyName);

        return this.remove0(propertyName);
    }

    abstract TextStyle remove0(final TextStylePropertyName<?> propertyName);

    private static void checkPropertyName(final TextStylePropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");
    }

    // TextStyleVisitor.................................................................................................

    abstract void accept(final TextStyleVisitor visitor);

    // Direction........................................................................................................

    abstract Border border(final Direction direction);

    abstract Margin margin(final Direction direction);

    abstract Padding padding(final Direction direction);


    // Object...........................................................................................................

    @Override
    abstract public int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    abstract boolean equals0(final TextStyle other);

    @Override
    abstract public String toString();

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json object holding the textStyle as a map.
     */
    public static TextStyle fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return fromJson0(node.objectOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static TextStyle fromJson0(final JsonObjectNode json) {
        final Map<TextStylePropertyName<?>, Object> properties = Maps.ordered();

        for (JsonNode child : json.children()) {
            final TextStylePropertyName<?> name = TextStylePropertyName.fromJsonNodeName(child);
            properties.put(name,
                    name.handler.fromJsonNode(child, name));
        }

        return with(properties);
    }

    static {
        HasJsonNode.register("text-textStyle", TextStyle::fromJsonNode, TextStyle.class,
                NonEmptyTextStyle.class,
                EmptyTextStyle.class);
    }
}
