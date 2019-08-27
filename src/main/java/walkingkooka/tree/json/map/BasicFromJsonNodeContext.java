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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

final class BasicFromJsonNodeContext extends BasicJsonNodeContext implements FromJsonNodeContext {

    /**
     * Singleton
     */
    final static BasicFromJsonNodeContext INSTANCE = new BasicFromJsonNodeContext();

    /**
     * Private ctor
     */
    private BasicFromJsonNodeContext() {
        super();
    }

    // from.............................................................................................................

    /**
     * Attempts to convert this node to the requested {@link Class type}.
     */
    @Override
    public <T> T fromJsonNode(final JsonNode node,
                              final Class<T> type) {
        return type.cast(BasicMapper.mapperOrFail(type).fromJsonNode(node, this));
    }

    /**
     * Assumes this json object is an array holding elements that will be converted to the requested element type, returning
     * a {@link List} of them.
     */
    @Override
    public <T> List<T> fromJsonNodeList(final JsonNode node,
                                        final Class<T> elementType) {
        return this.fromJsonNodeCollection(node,
                List.class,
                elementType,
                Collectors.toList());
    }

    /**
     * Assumes this json object is an array holding elements that will be converted to the requested element type, returning
     * a {@link Set} of them.
     */
    @Override
    public <T> Set<T> fromJsonNodeSet(final JsonNode node,
                                      final Class<T> elementType) {
        return this.fromJsonNodeCollection(node,
                Set.class,
                elementType,
                Collectors.toCollection(Sets::ordered));
    }

    private <C extends Collection<T>, T> C fromJsonNodeCollection(final JsonNode from,
                                                                  final Class<?> label,
                                                                  final Class<T> elementType,
                                                                  final Collector<T, ?, C> collector) {
        final BasicMapper<T> mapper = BasicMapper.mapperOrFail(elementType);
        return from.children()
                .stream()
                .map(c -> mapper.fromJsonNode(c, this))
                .collect(collector);
    }

    /**
     * Assumes this json object is an array holding elements holding elements of the requested element type, returning
     * a {@link Map} of them.
     */
    @Override
    public <K, V> Map<K, V> fromJsonNodeMap(final JsonNode node,
                                            final Class<K> keyType,
                                            final Class<V> valueType) {
        fromArrayCheck(node, Map.class);

        final BasicMapper<K> keyMapper = BasicMapper.mapperOrFail(keyType);
        final BasicMapper<V> valueMapper = BasicMapper.mapperOrFail(valueType);

        final Map<K, V> map = Maps.ordered();

        try {
            for (JsonNode entry : node.children()) {
                final JsonObjectNode entryObject = entry.objectOrFail();

                map.put(keyMapper.fromJsonNode(entryObject.getOrFail(BasicMapperTypedMap.ENTRY_KEY), this),
                        valueMapper.fromJsonNode(entryObject.getOrFail(BasicMapperTypedMap.ENTRY_VALUE), this));
            }
            return map;

        } catch (final NullPointerException | FromJsonNodeException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw cause;
        }
    }

    // fromJsonNodeWithType.............................................................................................

    /**
     * Assumes a wrapper object with the type and value, basically the inverse of {@link ToJsonNodeContext#toJsonNodeWithType(Object)}.
     */
    @Override
    public <T> T fromJsonNodeWithType(final JsonNode node) {
        return BasicFromJsonNodeContextJsonNodeVisitor.value(node, this);
    }

    /**
     * Assumes a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    @Override
    public <T> List<T> fromJsonNodeWithTypeList(final JsonNode node) {
        return this.fromJsonNodeCollectionWithType(node,
                List.class,
                Collectors.toList());
    }

    /**
     * Assumes a {@link JsonArrayNode} holding objects tagged with type and values.
     */
    @Override
    public <T> Set<T> fromJsonNodeWithTypeSet(final JsonNode node) {
        return this.fromJsonNodeCollectionWithType(node,
                Set.class,
                Collectors.toCollection(Sets::ordered));
    }

    private <C extends Collection<T>, T> C fromJsonNodeCollectionWithType(final JsonNode from,
                                                                          final Class<?> label,
                                                                          final Collector<T, ?, C> collector) {
        return this.fromJsonNodeCollection0(from,
                label,
                this::fromJsonNodeWithType,
                collector);
    }

    /**
     * Assumes a {@link JsonArrayNode} holding entries of the {@link Map} tagged with type and values.
     */
    @Override
    public <K, V> Map<K, V> fromJsonNodeWithTypeMap(final JsonNode node) {
        fromArrayCheck(node, Map.class);

        final Map<K, V> map = Maps.ordered();

        for (JsonNode child : node.children()) {
            final JsonObjectNode childObject = child.objectOrFail();

            map.put(this.fromJsonNodeWithType(childObject.getOrFail(BasicMapperTypedMap.ENTRY_KEY)),
                    this.fromJsonNodeWithType(childObject.getOrFail(BasicMapperTypedMap.ENTRY_VALUE)));
        }

        return map;
    }

    // from helpers......................................................................................................

    private static void fromArrayCheck(final JsonNode node,
                                       final Class<?> label) {
        if (null != node && !node.isArray()) {
            throw new FromJsonNodeException("Required array for " + label.getSimpleName(), node);
        }
    }

    /**
     * Turns all the children nodes into a {@link Collection}.
     */
    private <C extends Collection<T>, T> C fromJsonNodeCollection0(final JsonNode from,
                                                                   final Class<?> label,
                                                                   final Function<JsonNode, T> element,
                                                                   final Collector<T, ?, C> collector) {
        fromArrayCheck(from, label);

        return from.children()
                .stream()
                .map(element)
                .collect(collector);
    }
}