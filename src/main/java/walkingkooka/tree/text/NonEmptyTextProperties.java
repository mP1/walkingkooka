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
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link NonEmptyTextProperties} holds a non empty {@link Map} of {@link TextPropertyName} and values.
 */
final class NonEmptyTextProperties extends TextProperties {

    /**
     * Factory that creates a {@link NonEmptyTextProperties} from a {@link TextPropertiesMap}.
     */
    static NonEmptyTextProperties with(final TextPropertiesMap value) {
        return new NonEmptyTextProperties(value);
    }

    private NonEmptyTextProperties(final Map<TextPropertyName<?>, Object> value) {
        super();
        this.value = value;
    }

    // Value..........................................................................................................

    @Override
    public Map<TextPropertyName<?>, Object> value() {
        return this.value;
    }

    final Map<TextPropertyName<?>, Object> value;

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof NonEmptyTextProperties;
    }

    @Override
    boolean equals0(final TextProperties other) {
        return this.value.equals(other.value());
    }

    @Override
    public final String toString() {
        return this.value.toString();
    }

    // HasJsonNode......................................................................................................

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
}
