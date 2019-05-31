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

import walkingkooka.tree.json.JsonNode;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A read only sorted view of attributes or text property to values that appear within a {@link TextPropertiesNode}.
 */
final class TextPropertiesMap extends AbstractMap<TextPropertyName<?>, Object> {

    /**
     * Factory that takes a copy if the given {@link Map} is not a {@link TextPropertiesMap}.
     */
    static TextPropertiesMap with(final Map<TextPropertyName<?>, Object> map) {
        Objects.requireNonNull(map, "map");

        return map instanceof TextPropertiesMap ?
                TextPropertiesMap.class.cast(map) :
                  new TextPropertiesMap(TextPropertiesMapEntrySet.with(map));
    }

    private TextPropertiesMap(final TextPropertiesMapEntrySet entries) {
        super();
        this.entries = entries;
    }

    @Override
    public Set<Entry<TextPropertyName<?>, Object>> entrySet() {
        return this.entries;
    }

    private final TextPropertiesMapEntrySet entries;

    // HasJsonNode......................................................................................................

    static TextPropertiesMap fromJson(final JsonNode json) {
        return new TextPropertiesMap(TextPropertiesMapEntrySet.fromJson(json));
    }

    JsonNode toJson() {
        return this.entries.toJson();
    }
}
