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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import java.util.Map;
import java.util.Optional;

/**
 * A {@link TextStyle} with no textStyle and values.
 */
final class EmptyTextStyle extends TextStyle {

    /**
     * Singleton necessary to avoid race conditions to a init'd static field
     */
    static EmptyTextStyle instance() {
        if (null == instance) {
            instance = new EmptyTextStyle();
        }
        return instance;
    }

    private static EmptyTextStyle instance;

    /**
     * Private ctor
     */
    private EmptyTextStyle() {
        super();
    }

    /**
     * Always returns true
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    // Value............................................................................................................

    @Override
    public Map<TextStylePropertyName<?>, Object> value() {
        return Maps.empty();
    }

    // merge............................................................................................................

    @Override
    TextStyle merge0(final TextStyle other) {
        return other;
    }

    @Override
    TextStyle merge1(final NonEmptyTextStyle textStyle) {
        return textStyle; // EMPTY merge NOTEMPTY -> NOTEMPTY
    }

    // replace..........................................................................................................

    @Override
    TextNode replace0(final TextNode textNode) {
        return textNode.setAttributesEmptyTextStyleMap();
    }

    // setChildren......................................................................................................

    @Override
    TextStyleMap textStyleMap() {
        return TextStyleMap.EMPTY;
    }

    // get/set/remove...................................................................................................

    @Override
    <V> Optional<V> get0(final TextStylePropertyName<V> propertyName) {
        return Optional.empty();
    }

    @Override
    <V> TextStyle set0(final TextStylePropertyName<V> propertyName, final V value) {
        return NonEmptyTextStyle.with(TextStyleMap.withTextStyleMapEntrySet(TextStyleMapEntrySet.withList(Lists.of(Maps.entry(propertyName, value)))));
    }

    @Override
    TextStyle remove0(final TextStylePropertyName<?> propertyName) {
        return this;
    }

    // TextStyleVisitor.................................................................................................

    @Override
    void accept(final TextStyleVisitor visitor) {
        // no properties
    }

    // Direction........................................................................................................

    @Override
    Border border(final Direction direction) {
        return direction.emptyBorder;
    }

    @Override
    Margin margin(final Direction direction) {
        return direction.emptyMargin;
    }

    @Override
    Padding padding(final Direction direction) {
        return direction.emptyPadding;
    }

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    final boolean canBeEquals(final Object other) {
        return other instanceof EmptyTextStyle;
    }

    @Override
    boolean equals0(final TextStyle other) {
        return true; // singleton
    }

    @Override
    public String toString() {
        return "";
    }

    // HasJsonNode......................................................................................................

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.object();
    }
}
