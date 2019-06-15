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

import walkingkooka.collect.map.Maps;
import walkingkooka.tree.json.JsonNode;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A read only sorted view of attributes or text style to values that appear within a {@link TextStyleNode}.
 */
final class TextStyleMap extends AbstractMap<TextStylePropertyName<?>, Object> {

    static {
        Maps.registerImmutableType(TextStyleMap.class);
    }

    /**
     * An empty {@link TextStyleMap}.
     */
    static final TextStyleMap EMPTY = new TextStyleMap(TextStyleMapEntrySet.EMPTY);

    /**
     * Factory that takes a copy if the given {@link Map} is not a {@link TextStyleMap}.
     */
    static TextStyleMap with(final Map<TextStylePropertyName<?>, Object> map) {
        Objects.requireNonNull(map, "map");

        return map instanceof TextStyleMap ?
                TextStyleMap.class.cast(map) :
                with0(map);
    }

    private static TextStyleMap with0(final Map<TextStylePropertyName<?>, Object> map) {
        return with1(TextStyleMapEntrySet.with(map));
    }

    private static TextStyleMap with1(final TextStyleMapEntrySet entrySet) {
        return entrySet.isEmpty() ?
                EMPTY :
                withTextStyleMapEntrySet(entrySet);
    }

    static TextStyleMap withTextStyleMapEntrySet(final TextStyleMapEntrySet entrySet) {
        return new TextStyleMap(entrySet);
    }

    private TextStyleMap(final TextStyleMapEntrySet entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Set<Entry<TextStylePropertyName<?>, Object>> entrySet() {
        return this.entries;
    }

    final TextStyleMapEntrySet entries;

    // TextStyleVisitor.................................................................................................

    void accept(final TextStyleVisitor visitor) {
        this.entries.accept(visitor);
    }

    // HasJsonNode......................................................................................................

    static TextStyleMap fromJson(final JsonNode json) {
        return TextStyleMap.with1(TextStyleMapEntrySet.fromJson(json));
    }

    JsonNode toJson() {
        return this.entries.toJson();
    }
}
