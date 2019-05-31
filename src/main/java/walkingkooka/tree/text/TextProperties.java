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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * A {@link TextProperties} holds a {@link Map} of {@link TextPropertyName} and values.
 */
public final class TextProperties implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<Map<TextPropertyName<?>, Object>> {

    /**
     * A {@link TextProperties} with no properties.
     */
    public static TextProperties EMPTY = new TextProperties(Maps.empty());

    /**
     * Factory that creates a {@link TextProperties} from a {@link Map}.
     */
    public static TextProperties with(final Map<TextPropertyName<?>, Object> value) {
        final TextPropertiesMap map = TextPropertiesMap.with(value);
        return map.isEmpty() ?
                EMPTY :
                new TextProperties(map);
    }

    private TextProperties(final Map<TextPropertyName<?>, Object> value) {
        super();
        this.value = value;
    }

    @Override
    public Map<TextPropertyName<?>, Object> value() {
        return this.value;
    }

    final Map<TextPropertyName<?>, Object> value;

    // Object..........................................................................................................

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof TextProperties &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final TextProperties other) {
        return this.value.equals(other.value);
    }

    @Override
    public final String toString() {
        return this.value.toString();
    }

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

    /**
     * Creates a json-object where the properties are strings, and the value without types.
     */
    @Override
    public JsonNode toJsonNode() {
        final List<JsonNode> json = Lists.array();

        for (Entry<TextPropertyName<?>, Object> propertyAndValue : this.value.entrySet()) {
            final TextPropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = propertyName.handler.toJsonNode(Cast.to(propertyAndValue.getValue()));

            json.add(value.setName(propertyName.toJsonNodeName()));
        }

        return JsonNode.object()
                .setChildren(json);
    }

    static {
        HasJsonNode.register("text-properties", TextProperties::fromJsonNode, TextProperties.class);
    }
}
