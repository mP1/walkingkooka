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

package walkingkooka.tree.json.map;

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

final class BasicMapperTypedMap extends BasicMapperTyped<Map<?, ?>> {

    static BasicMapperTypedMap instance() {
        return new BasicMapperTypedMap();
    }

    private BasicMapperTypedMap() {
        super();
    }

    @Override
    void register() {
        this.registerTypeNameAndType();
    }

    @Override
    Class<Map<?, ?>> type() {
        return Cast.to(Map.class);
    }

    @Override
    String typeName() {
        return "map";
    }

    // from.............................................................................................................

    @Override
    Map<?, ?> fromJsonNodeNonNull(final JsonNode node,
                                  final FromJsonNodeContext context) {
        return context.fromJsonNodeWithTypeMap(node);
    }

    @Override
    Map<?, ?> fromJsonNodeNull(final FromJsonNodeContext context) {
        return null;
    }

    // to...............................................................................................................

    @Override
    JsonNode toJsonNodeNonNull(final Map<?, ?> map,
                               final ToJsonNodeContext context) {
        return JsonObjectNode.array()
                .setChildren(map.entrySet()
                        .stream()
                        .map(e -> entryWithType(e, context))
                        .collect(Collectors.toList()));
    }

    private static JsonNode entryWithType(final Entry<?, ?> entry,
                                          final ToJsonNodeContext context) {
        return JsonNode.object()
                .set(ENTRY_KEY, context.toJsonNodeWithType(entry.getKey()))
                .set(ENTRY_VALUE, context.toJsonNodeWithType(entry.getValue()));
    }

    final static JsonNodeName ENTRY_KEY = JsonNodeName.with("key");
    final static JsonNodeName ENTRY_VALUE = JsonNodeName.with("value");
}
