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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class BasicJsonNodeUnmarshallContext extends BasicJsonNodeContext implements JsonNodeUnmarshallContext {

    /**
     * Singleton
     */
    final static BasicJsonNodeUnmarshallContext INSTANCE = new BasicJsonNodeUnmarshallContext(JsonNodeUnmarshallContext.OBJECT_PRE_PROCESSOR);

    /**
     * Private ctor
     */
    private BasicJsonNodeUnmarshallContext(final BiFunction<JsonObjectNode, Class<?>, JsonObjectNode> processor) {
        super();
        this.processor = processor;
    }

    @Override
    public JsonNodeUnmarshallContext setObjectPreProcessor(final BiFunction<JsonObjectNode, Class<?>, JsonObjectNode> processor) {
        Objects.requireNonNull(processor, "processor");

        return this.processor.equals(processor) ?
                this :
                new BasicJsonNodeUnmarshallContext(processor);
    }

    // from.............................................................................................................

    /**
     * Attempts to convert this node to the requested {@link Class type}.
     */
    @Override
    public <T> T unmarshall(final JsonNode node,
                            final Class<T> type) {
        return type.cast(BasicJsonMarshaller.marshaller(type).unmarshall(this.preProcess(node, type), this));
    }

    /**
     * Assumes this json object is an array holding elements that will be converted to the requested element type, returning
     * a {@link List} of them.
     */
    @Override
    public <T> List<T> unmarshallList(final JsonNode node,
                                      final Class<T> elementType) {
        return this.unmarshallCollection(node,
                elementType,
                Collectors.toList());
    }

    /**
     * Assumes this json object is an array holding elements that will be converted to the requested element type, returning
     * a {@link Set} of them.
     */
    @Override
    public <T> Set<T> unmarshallSet(final JsonNode node,
                                    final Class<T> elementType) {
        return this.unmarshallCollection(node,
                elementType,
                Collectors.toCollection(Sets::ordered));
    }

    private <C extends Collection<T>, T> C unmarshallCollection(final JsonNode from,
                                                                final Class<T> elementType,
                                                                final Collector<T, ?, C> collector) {
        final BasicJsonMarshaller<T> marshaller = BasicJsonMarshaller.marshaller(elementType);
        return from.children()
                .stream()
                .map(c -> marshaller.unmarshall(this.preProcess(c, elementType), this))
                .collect(collector);
    }

    /**
     * Assumes this json object is an array holding elements holding elements of the requested element type, returning
     * a {@link Map} of them.
     */
    @Override
    public <K, V> Map<K, V> unmarshallMap(final JsonNode node,
                                          final Class<K> keyType,
                                          final Class<V> valueType) {
        fromArrayCheck(node, Map.class);

        final BasicJsonMarshaller<K> keyMapper = BasicJsonMarshaller.marshaller(keyType);
        final BasicJsonMarshaller<V> valueMapper = BasicJsonMarshaller.marshaller(valueType);

        final Map<K, V> map = Maps.ordered();

        try {
            for (JsonNode entry : node.children()) {
                final JsonObjectNode entryObject = entry.objectOrFail();

                map.put(keyMapper.unmarshall(this.preProcess(entryObject.getOrFail(BasicJsonMarshallerTypedMap.ENTRY_KEY), keyType), this),
                        valueMapper.unmarshall(this.preProcess(entryObject.getOrFail(BasicJsonMarshallerTypedMap.ENTRY_VALUE), valueType), this));
            }
            return map;

        } catch (final NullPointerException | JsonNodeUnmarshallException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw cause;
        }
    }

    // unmarshallWithType.............................................................................................

    /**
     * Assumes a wrapper object with the type and value, basically the inverse of {@link JsonNodeMarshallContext#marshallWithType(Object)}.
     */
    @Override
    public <T> T unmarshallWithType(final JsonNode node) {
        return BasicJsonNodeUnmarshallContextJsonNodeVisitor.value(node, this);
    }

    /**
     * Assumes a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    @Override
    public <T> List<T> unmarshallWithTypeList(final JsonNode node) {
        return this.unmarshallCollectionWithType(node,
                List.class,
                Collectors.toList());
    }

    /**
     * Assumes a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    @Override
    public <T> Set<T> unmarshallWithTypeSet(final JsonNode node) {
        return this.unmarshallCollectionWithType(node,
                Set.class,
                Collectors.toCollection(Sets::ordered));
    }

    private <C extends Collection<T>, T> C unmarshallCollectionWithType(final JsonNode from,
                                                                        final Class<?> label,
                                                                        final Collector<T, ?, C> collector) {
        return this.unmarshallCollection0(from,
                label,
                this::unmarshallWithType,
                collector);
    }

    /**
     * Assumes a {@link JsonArrayNode} holding entries of the {@link Map} tagged with type and values.
     */
    @Override
    public <K, V> Map<K, V> unmarshallWithTypeMap(final JsonNode node) {
        fromArrayCheck(node, Map.class);

        final Map<K, V> map = Maps.ordered();

        for (JsonNode child : node.children()) {
            final JsonObjectNode childObject = child.objectOrFail();

            map.put(this.unmarshallWithType(childObject.getOrFail(BasicJsonMarshallerTypedMap.ENTRY_KEY)),
                    this.unmarshallWithType(childObject.getOrFail(BasicJsonMarshallerTypedMap.ENTRY_VALUE)));
        }

        return map;
    }

    // from helpers......................................................................................................

    private static void fromArrayCheck(final JsonNode node,
                                       final Class<?> label) {
        if (null != node && !node.isArray()) {
            throw new JsonNodeUnmarshallException("Required array for " + label.getSimpleName(), node);
        }
    }

    /**
     * Turns all the children nodes into a {@link Collection}.
     */
    private <C extends Collection<T>, T> C unmarshallCollection0(final JsonNode from,
                                                                 final Class<?> label,
                                                                 final Function<JsonNode, T> element,
                                                                 final Collector<T, ?, C> collector) {
        fromArrayCheck(from, label);

        return from.children()
                .stream()
                .map(element)
                .collect(collector);
    }

    /**
     * If the {@link JsonNode} is an object executes the {@link #processor}.
     */
    private JsonNode preProcess(final JsonNode node,
                                final Class<?> type) {
        return node.isObject() ?
                this.processor.apply(node.objectOrFail(), type) :
                node;
    }

    private final BiFunction<JsonObjectNode, Class<?>, JsonObjectNode> processor;
}
