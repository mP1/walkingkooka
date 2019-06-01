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
 * A {@link TextProperties} holds a {@link Map} of {@link TextPropertyName} and values.
 */
public abstract class TextProperties implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<Map<TextPropertyName<?>, Object>> {

    /**
     * A {@link TextProperties} with no properties.
     */
    public final static TextProperties EMPTY = EmptyTextProperties.instance();

    /**
     * Factory that creates a {@link TextProperties} from a {@link Map}.
     */
    public static TextProperties with(final Map<TextPropertyName<?>, Object> value) {
        return withTextPropertiesMap(TextPropertiesMap.with(value));
    }

    static TextProperties withTextPropertiesMap(final TextPropertiesMap map) {
        return map.isEmpty() ?
                EMPTY :
                NonEmptyTextProperties.with(map);
    }

    /**
     * Private ctor to limit sub classes.
     */
    TextProperties() {
        super();
    }

    /**
     * Returns true if the properties is empty.
     */
    abstract boolean isEmpty();

    // setChildren......................................................................................................

    /**
     * Factory that returns a {@link TextPropertiesNode} with the given {@link TextNode}
     * and these properties.
     */
    public final TextPropertiesNode setChildren(final List<TextNode> textNodes) {
        return TextPropertiesNode.with(textNodes, this.textPropertiesMap());
    }

    abstract TextPropertiesMap textPropertiesMap();

    // merge............................................................................................................

    /**
     * Merges the two {@link TextProperties}, with the value from this having priority when both have the same
     * {@link TextPropertyName}.
     */
    public final TextProperties merge(final TextProperties textProperties) {
        Objects.requireNonNull(textProperties, "textProperties");

        return this.merge0(textProperties);
    }

    abstract TextProperties merge0(final TextProperties textProperties);

    abstract TextProperties merge1(final NonEmptyTextProperties textProperties);

    // replace............................................................................................................

    /**
     * If empty returns the given {@link TextNode} otherwise wraps inside a {@link TextPropertiesNode}
     */
    public final TextNode replace(final TextNode textNode) {
        Objects.requireNonNull(textNode, "textNode");

        return this.replace0(textNode);
    }

    abstract TextNode replace0(final TextNode textNode);

    // get..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextProperties} with the new definition which may or may not
     * require creating a new {@link TextProperties}.
     */
    public final <V> Optional<V> get(final TextPropertyName<V> propertyName) {
        checkPropertyName(propertyName);

        return this.get0(propertyName);
    }

    abstract <V> Optional<V> get0(final TextPropertyName<V> propertyName);

    // set..............................................................................................................

    /**
     * Sets a possibly new property returning a {@link TextProperties} with the new definition which may or may not
     * require creating a new {@link TextProperties}.
     */
    public final <V> TextProperties set(final TextPropertyName<V> propertyName, final V value) {
        checkPropertyName(propertyName);

        propertyName.check(value);
        return this.set0(propertyName, value);
    }

    abstract <V> TextProperties set0(final TextPropertyName<V> propertyName, final V value);

    // remove...........................................................................................................

    /**
     * Removes a possibly existing property returning a {@link TextProperties} without.
     */
    public final TextProperties remove(final TextPropertyName<?> propertyName) {
        checkPropertyName(propertyName);

        return this.remove0(propertyName);
    }

    abstract TextProperties remove0(final TextPropertyName<?> propertyName);

    private static void checkPropertyName(final TextPropertyName<?> propertyName) {
        Objects.requireNonNull(propertyName, "propertyName");
    }

    // Object..........................................................................................................

    @Override
    abstract public int hashCode();

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    abstract boolean equals0(final TextProperties other);

    @Override
    abstract public String toString();

    // HasJsonNode......................................................................................................

    /**
     * Accepts a json object holding the properties as a map.
     */
    public static TextProperties fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        try {
            return fromJson0(node.objectOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    private static TextProperties fromJson0(final JsonObjectNode json) {
        final Map<TextPropertyName<?>, Object> properties = Maps.ordered();

        for (JsonNode child : json.children()) {
            final TextPropertyName name = TextPropertyName.fromJsonNodeName(child);
            properties.put(name,
                    name.handler.fromJsonNode(child));
        }

        return with(properties);
    }

    static {
        HasJsonNode.register("text-properties", TextProperties::fromJsonNode, TextProperties.class,
                NonEmptyTextProperties.class,
                EmptyTextProperties.class);
    }
}
