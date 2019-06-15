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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * A {@link NonEmptyTextStyle} holds a non empty {@link Map} of {@link TextStylePropertyName} and values.
 */
final class NonEmptyTextStyle extends TextStyle {

    /**
     * Factory that creates a {@link NonEmptyTextStyle} from a {@link TextStyleMap}.
     */
    static NonEmptyTextStyle with(final TextStyleMap value) {
        return new NonEmptyTextStyle(value);
    }

    private NonEmptyTextStyle(final TextStyleMap value) {
        super();
        this.value = value;
    }

    /**
     * Always returns false
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    // Value..........................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> value() {
        return this.value;
    }

    final TextStyleMap value;

    // merge............................................................................................................

    @Override
    TextStyle merge0(final TextStyle textStyle) {
        return textStyle.merge1(this);
    }

    @Override
    TextStyle merge1(final NonEmptyTextStyle textStyle) {
        final Map<TextStylePropertyName<?>, Object> otherBefore = this.value; // because of double dispatch params are reversed.
        final Map<TextStylePropertyName<?>, Object> before = textStyle.value;


        final Map<TextStylePropertyName<?>, Object> merged = Maps.sorted();
        merged.putAll(otherBefore);
        merged.putAll(before);

        return merged.equals(otherBefore) ?
                this :
                merged.equals(before) ?
                        textStyle :
                        new NonEmptyTextStyle(TextStyleMap.with(merged));
    }

    // replace..........................................................................................................

    @Override
    TextNode replace0(final TextNode textNode) {
        return textNode.setAttributesNonEmptyTextStyleMap(this.value);
    }

    // setChildren......................................................................................................

    @Override
    TextStyleMap textStyleMap() {
        return this.value;
    }

    // get..............................................................................................................

    @Override
    <V> Optional<V> get0(final TextStylePropertyName<V> propertyName) {
        return Optional.ofNullable(Cast.to(this.value.get(propertyName)));
    }

    // set..............................................................................................................

    @Override
    <V> TextStyle set0(final TextStylePropertyName<V> propertyName, final V value) {
        TextStyleMap map = this.value;
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();

        int mode = 0; // new property added.

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : map.entries) {
            final TextStylePropertyName<?> property = propertyAndValue.getKey();

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
            TextStyleMapEntrySet.sort(list);
        }

        return 1 == mode ?
                this :
                new NonEmptyTextStyle(TextStyleMap.withTextStyleMapEntrySet(TextStyleMapEntrySet.withList(list)));
    }

    // remove...........................................................................................................

    @Override
    TextStyle remove0(final TextStylePropertyName<?> propertyName) {
        final List<Entry<TextStylePropertyName<?>, Object>> list = Lists.array();
        boolean removed = false;

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entries) {
            final TextStylePropertyName<?> property = propertyAndValue.getKey();
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
    private TextStyle remove1(List<Entry<TextStylePropertyName<?>, Object>> list) {
        return list.isEmpty() ?
                TextStyle.EMPTY :
                new NonEmptyTextStyle(TextStyleMap.withTextStyleMapEntrySet(TextStyleMapEntrySet.withList(list))); // no need to sort after a delete
    }

    // TextStyleVisitor.................................................................................................

    @Override
    void accept(final TextStyleVisitor visitor) {
        this.value.accept(visitor);
    }

    // Direction........................................................................................................

    @Override
    Border border(final Direction direction) {
        return Border.with(direction, this);
    }

    @Override
    Margin margin(final Direction direction) {
        return Margin.with(direction, this);
    }

    @Override
    Padding padding(final Direction direction) {
        return Padding.with(direction, this);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof NonEmptyTextStyle;
    }

    @Override
    boolean equals0(final TextStyle other) {
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

        for (Entry<TextStylePropertyName<?>, Object> propertyAndValue : this.value.entrySet()) {
            final TextStylePropertyName<?> propertyName = propertyAndValue.getKey();
            final JsonNode value = propertyName.handler.toJsonNode(Cast.to(propertyAndValue.getValue()));

            json.add(value.setName(propertyName.toJsonNodeName()));
        }

        return JsonNode.object()
                .setChildren(json);
    }
}
