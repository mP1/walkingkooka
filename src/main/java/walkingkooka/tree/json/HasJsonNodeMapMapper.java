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

import walkingkooka.collect.map.Maps;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

final class HasJsonNodeMapMapper extends HasJsonNodeMapper2<Map<?, ?>> {

    static HasJsonNodeMapMapper instance() {
        return new HasJsonNodeMapMapper();
    }

    /**
     * Accepts a {@link Map} and creates a {@link JsonArrayNode} with entries converted to json with types.
     */
    static JsonNode toJsonNodeWithTypeMap0(final Map<?, ?> map) {
        return null == map ?
                JsonNode.nullNode() :
                toJsonNodeWithTypeMap1(map);
    }

    private static JsonNode toJsonNodeWithTypeMap1(final Map<?, ?> map) {
        return JsonNode.array()
                .setChildren(map.entrySet()
                        .stream()
                        .map(HasJsonNodeMapMapper::toMapChildrenEntry)
                        .collect(Collectors.toList()));
    }

    private static JsonNode toMapChildrenEntry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(ENTRY_KEY, toJsonNodeWithTypeObject(entry.getKey()))
                .set(ENTRY_VALUE, toJsonNodeWithTypeObject(entry.getValue()));
    }

    private HasJsonNodeMapMapper() {
        super();
    }

    /**
     * Accepts an array of entry objects each holding the key and value.
     */
    @Override
    Map<?, ?> fromJsonNode0(final JsonNode node) {
        // container must be an array
        JsonArrayNode array;
        try {
            array = node.arrayOrFail();
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }

        final Map<?, ?> map = Maps.ordered();

        for (JsonNode child : array.children()) {
            JsonObjectNode childObject;
            try {
                childObject = child.objectOrFail();
            } catch (final JsonNodeException cause) {
                throw new IllegalArgumentException(cause.getMessage(), cause);
            }

            map.put(HasJsonNodeMapper.fromJsonNodeWithType(childObject.getOrFail(HasJsonNodeMapper.ENTRY_KEY)),
                    HasJsonNodeMapper.fromJsonNodeWithType(childObject.getOrFail(HasJsonNodeMapper.ENTRY_VALUE)));
        }

        return map;
    }

    @Override
    JsonStringNode typeName() {
        return JSON_STRING_NODE;
    }

    private final JsonStringNode JSON_STRING_NODE = JsonStringNode.with("map");

    @Override
    JsonNode toJsonNode0(final Map<?, ?> map) {
        return JsonNode.array()
                .setChildren(map.entrySet()
                        .stream()
                        .map(HasJsonNodeMapper::toJsonNodeWithTypeMapEntry)
                        .collect(Collectors.toList()));
    }
}
