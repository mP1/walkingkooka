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
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A {@link JsonNodeMarshallContext} that uses a registered type primarily its {@link java.util.function.BiFunction} to
 * turn objects or values into {@link JsonNode}.
 */
final class BasicJsonNodeMarshallContext extends BasicJsonNodeContext implements JsonNodeMarshallContext {

    /**
     * Singleton
     */
    final static BasicJsonNodeMarshallContext INSTANCE = new BasicJsonNodeMarshallContext(JsonNodeMarshallContext.OBJECT_PRE_PROCESSOR);

    /**
     * Private ctor
     */
    private BasicJsonNodeMarshallContext(final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor) {
        super();
        this.processor = processor;
    }

    // marshall. .....................................................................................................

    public JsonNodeMarshallContext setObjectPostProcessor(final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor) {
        Objects.requireNonNull(processor, "processor");

        return this.processor.equals(processor) ?
                this :
                new BasicJsonNodeMarshallContext(processor);
    }

    // marshall. .....................................................................................................

    /**
     * Accepts an {@link Object} and creates a {@link JsonNode} equivalent.
     */
    @Override
    public JsonNode marshall(final Object value) {
        return null == value ?
                JsonNode.nullNode() :
                this.marshallNonNull(value);
    }

    private JsonNode marshallNonNull(final Object value) {
        final JsonNode json = BasicJsonMarshaller.marshaller(value.getClass())
                .marshall(Cast.to(value), this);
        return json.isObject() ?
                this.processor.apply(value, json.objectOrFail()) :
                json;
    }

    private final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor;

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode marshallList(final List<?> list) {
        return marshallCollection(list);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode marshallSet(final Set<?> set) {
        return marshallCollection(set);
    }

    private JsonNode marshallCollection(final Collection<?> collection) {
        return null == collection ?
                JsonNode.nullNode() :
                JsonObjectNode.array()
                        .setChildren(collection.stream()
                                .map(this::marshall)
                                .collect(Collectors.toList()));
    }

    /**
     * Accepts a {@link Map} of elements creating a {@link JsonArrayNode} holding the entries with the raw key and values,
     * without recording the types for either.
     */
    @Override
    public JsonNode marshallMap(final Map<?, ?> map) {
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
                .set(BasicJsonMarshallerTypedMap.ENTRY_KEY, this.marshall(entry.getKey()))
                .set(BasicJsonMarshallerTypedMap.ENTRY_VALUE, this.marshall(entry.getValue()));
    }

    // marshallWithType...............................................................................................

    /**
     * Wraps the {@link JsonNode} with a type name declaration.
     */
    @Override
    public JsonNode marshallWithType(final Object value) {
        return null == value ?
                JsonNode.nullNode() :
                BasicJsonMarshaller.marshaller(value.getClass())
                        .marshallWithType(Cast.to(value), this);
    }

    /**
     * Accepts a {@link List} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode marshallWithTypeList(final List<?> list) {
        return BasicJsonMarshallerTypedCollectionList.instance().marshall(list, this);
    }

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same type and creates a {@link JsonArrayNode}.
     */
    @Override
    public JsonNode marshallWithTypeSet(final Set<?> set) {
        return BasicJsonMarshallerTypedCollectionSet.instance().marshall(set, this);
    }

    /**
     * Accepts a {@link Map} and returns its {@link JsonNode} equivalent.
     */
    @Override
    public JsonNode marshallWithTypeMap(final Map<?, ?> map) {
        return BasicJsonMarshallerTypedMap.instance().marshall(map, this);
    }
}
