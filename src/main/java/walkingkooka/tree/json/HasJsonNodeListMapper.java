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

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class HasJsonNodeListMapper extends HasJsonNodeCollectionMapper<List<?>> {

    static HasJsonNodeListMapper instance() {
        return new HasJsonNodeListMapper();
    }

    private HasJsonNodeListMapper() {
        super();
    }

    /**
     * Accepts a {@link List} and creates a {@link JsonArrayNode} with elements converted to json with types.
     */
    static JsonNode toJsonNodeWithTypeList0(final List<?> list) {
        return INSTANCE.toJsonNodeWithTypeCollection0(list);
    }

    private final static HasJsonNodeListMapper INSTANCE = instance();

    @Override
    Collector<?, ?, List<?>> collector() {
        return Cast.to(Collectors.toList());
    }

    @Override
    JsonStringNode typeName() {
        return JSON_STRING_NODE;
    }

    private final JsonStringNode JSON_STRING_NODE = JsonStringNode.with("list");
}
