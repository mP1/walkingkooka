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
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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

    private NonEmptyTextProperties(final TextPropertiesMap value) {
        super();
        this.value = value;
    }

    // Value..........................................................................................................

    @Override
    public Map<TextPropertyName<?>, Object> value() {
        return this.value;
    }

    final TextPropertiesMap value;

    // setChildren......................................................................................................

    @Override
    TextPropertiesMap textPropertiesMap() {
        return this.value;
    }

    // get..............................................................................................................

    @Override
    <V> Optional<V> get0(final TextPropertyName<V> propertyName) {
        return Optional.ofNullable(Cast.to(this.value.get(propertyName)));
    }

    // set..............................................................................................................

    @Override
    <V> TextProperties set0(final TextPropertyName<V> propertyName, final V value) {
        TextPropertiesMap map = this.value;
        final List<Entry<TextPropertyName<?>, Object>> list = Lists.array();

        int mode = 0; // new property added.

        for (Entry<TextPropertyName<?>, Object> propertyAndValue : map.entries) {
            final TextPropertyName<?> property = propertyAndValue.getKey();

            if (propertyName.equals(property)) {
                if (propertyAndValue.getValue().equals(value)) {
                    mode = 1; // no change
                    break;
                } else {
                    list.add(Maps.entry(property, value));
                    mode = 2; // replaced
                }
            } else {
                list.add(propertyAndValue);
            }
        }

        // replace didnt happen
        if (0 == mode) {
            list.add(Maps.entry(propertyName, value));
            TextPropertiesMapEntrySet.sort(list);
        }

        return 1 == mode ?
                this :
                new NonEmptyTextProperties(TextPropertiesMap.withTextPropertiesMapEntrySet(TextPropertiesMapEntrySet.withList(list)));
    }

    // remove...........................................................................................................

    @Override
    TextProperties remove0(final TextPropertyName<?> propertyName) {
        final List<Entry<TextPropertyName<?>, Object>> list = Lists.array();
        boolean removed = false;

        for (Entry<TextPropertyName<?>, Object> propertyAndValue : this.value.entries) {
            final TextPropertyName<?> property = propertyAndValue.getKey();
            if (propertyName.equals(property)) {
                removed = true;
            } else {
                list.add(propertyAndValue);
            }
        }

        return removed ?
                this.remove1(list) :
                this;
    }

    /**
     * Accepts a list after removing a property, special casing if the list is empty.
     */
    private TextProperties remove1(List<Entry<TextPropertyName<?>, Object>> list) {
        return list.isEmpty() ?
                TextProperties.EMPTY :
                new NonEmptyTextProperties(TextPropertiesMap.withTextPropertiesMapEntrySet(TextPropertiesMapEntrySet.withList(list))); // no need to sort after a delete
    }

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
