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

package walkingkooka.tree.json.marshall;

import walkingkooka.Cast;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link ToJsonNodeContext} that uses a registered type primarily its {@link java.util.function.BiFunction} to
 * turn objects or values into {@link JsonNode}.
 */
final class BasicToJsonNodeContext extends BasicJsonNodeContext implements ToJsonNodeContext {

    /**
     * Singleton
     */
    final static BasicToJsonNodeContext INSTANCE = new BasicToJsonNodeContext();

    /**
     * Private ctor
     */
    private BasicToJsonNodeContext() {
        super();
    }

    // toJsonNode. .....................................................................................................

    /**
     * Accepts an {@link Object} and creates a {@link JsonNode} equivalent.
     */
    @Override
    public JsonNode toJsonNode(final Object value) {
        return null == value ?
                JsonNode.nullNode() :
                BasicMarshaller.marshaller(value.getClass())
                        .toJsonNode(Cast.to(value), this);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode toJsonNodeList(final List<?> list) {
        return toJsonNodeCollection(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode toJsonNodeSet(final Set<?> set) {
        return toJsonNodeCollection(set);
    }

    private JsonNode toJsonNodeCollection(final Collection<?> collection) {
        return null == collection ?
                JsonNode.nullNode() :
                JsonObjectNode.array()
                        .setChildren(collection.stream()
                                .map(this::toJsonNode)
                                .collect(Collectors.toList()));
    }

    /**
     * Accepts a {@link Map} of elements creating a {@link JsonArrayNode} holding the entries with the raw key and values,
     * without recording the types for either.
     */
    @Override
    public JsonNode toJsonNodeMap(final Map<?, ?> map) {
        return null == map ?
                JsonNode.nullNode() :
                JsonObjectNode.array()
                        .setChildren(map.entrySet()
                                .stream()
                                .map(this::entry)
                                .collect(Collectors.toList()));
    }

    private JsonNode entry(final Entry<?, ?> entry) {
        return JsonNode.object()
                .set(BasicMarshallerTypedMap.ENTRY_KEY, this.toJsonNode(entry.getKey()))
                .set(BasicMarshallerTypedMap.ENTRY_VALUE, this.toJsonNode(entry.getValue()));
    }

    // toJsonNodeWithType...............................................................................................

    /**
     * Wraps the {@link JsonNode} with a type name declaration.
     */
    @Override
    public JsonNode toJsonNodeWithType(final Object value) {
        return null == value ?
                JsonNode.nullNode() :
                BasicMarshaller.marshaller(value.getClass())
                        .toJsonNodeWithType(Cast.to(value), this);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode toJsonNodeWithTypeList(final List<?> list) {
        return BasicMarshallerTypedCollectionList.instance().toJsonNode(list, this);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode toJsonNodeWithTypeSet(final Set<?> set) {
        return BasicMarshallerTypedCollectionSet.instance().toJsonNode(set, this);
    }

    /**
     * Accepts a {@link Map} and returns its {@link JsonNode} equivalent.
     */
    @Override
    public JsonNode toJsonNodeWithTypeMap(final Map<?, ?> map) {
        return BasicMarshallerTypedMap.instance().toJsonNode(map, this);
    }
}
