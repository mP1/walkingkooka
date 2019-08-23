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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.json.UnsupportedTypeJsonNodeException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * A mapper that converts values to {@link JsonNode} and back.
 */
abstract class BasicMapper<T> {

    /**
     * All factory registrations for all {@link HasJsonNode}.
     */
    // @VisibleForTesting
    final static Map<String, BasicMapper<?>> TYPENAME_TO_FACTORY = Maps.concurrent();

    /**
     * Returns the mapper for the given {@link Class}.
     */
    static <T> BasicMapper<T> mapperOrFail(final Class<T> type) {
        return mapperOrFail(
                List.class.isAssignableFrom(type) ? "list" :
                        Set.class.isAssignableFrom(type) ? "set" :
                                Map.class.isAssignableFrom(type) ? "map" :
                                        type.getName());
    }

    /**
     * Returns the mapper for the given type name.
     */
    static <T> BasicMapper<T> mapperOrFail(final String type) {
        final BasicMapper<T> mapper = Cast.to(TYPENAME_TO_FACTORY.get(type));
        if (null == mapper) {
            throw new UnsupportedTypeJsonNodeException("Type " + CharSequences.quote(type) + " not supported, currently: " + TYPENAME_TO_FACTORY.keySet());
        }
        return mapper;
    }

    // register.........................................................................................................

    /*
     * To avoid race conditions register {@link JsonNode} here, all instance methods create the singleton
     * rather than referencing private static which would be null at this time because their super class
     * this is now running.
     */
    static {
        Lists.of(BasicMapperBoolean.instance(),
                BasicMapperDouble.instance(),
                BasicMapperNumber.instance(),
                BasicMapperString.instance(),
                BasicMapperTypedBigDecimal.instance(),
                BasicMapperTypedBigInteger.instance(),
                BasicMapperTypedCharacter.instance(),
                BasicMapperTypedJsonNode.instance(),
                BasicMapperTypedJsonNodeName.instance(),
                BasicMapperTypedCollectionList.instance(),
                BasicMapperTypedLocalDate.instance(),
                BasicMapperTypedLocalDateTime.instance(),
                BasicMapperTypedLocale.instance(),
                BasicMapperTypedLocalTime.instance(),
                BasicMapperTypedMap.instance(),
                BasicMapperTypedMathContext.instance(),
                BasicMapperTypedNumberByte.instance(),
                BasicMapperTypedNumberShort.instance(),
                BasicMapperTypedNumberInteger.instance(),
                BasicMapperTypedNumberLong.instance(),
                BasicMapperTypedNumberFloat.instance(),
                BasicMapperTypedOptional.instance(),
                BasicMapperTypedRoundingMode.instance(),
                BasicMapperTypedCollectionSet.instance()).forEach(BasicMapper::register);
    }

    /**
     * Registers a factory for a type that implements {@link HasJsonNode}.
     */
    static synchronized <T> Runnable register(final String typeName,
                                                              final BiFunction<JsonNode, FromJsonNodeContext, T> from,
                                                              final BiFunction<T, ToJsonNodeContext, JsonNode> to,
                                                              final Class<T> type,
                                                              final Class<? extends T>... types) {
        return BasicMapperTypedGeneric.with(typeName, from, to, type, types)
                .registerGeneric();
    }

    /**
     * Returns the {@link Class} for the given type name.
     */
    static Optional<Class<?>> registeredType(final JsonStringNode name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(TYPENAME_TO_FACTORY.get(name.value())).map(m -> m.type());
    }

    /**
     * Returns the type name identifying the given {@link Class} providing it is registered.
     */
    static Optional<JsonStringNode> typeName(final Class<?> type) {
        Objects.requireNonNull(type, "type");

        return Optional.ofNullable(TYPENAME_TO_FACTORY.get(type.getName())).map(m -> JsonNode.string(m.toString()));
    }

    // instance.........................................................................................................

    /**
     * Package private to limit sub classing.
     */
    BasicMapper() {
        super();
    }

    // register........................................................................................................

    /**
     * Registers this {@link BasicMapper}.
     */
    abstract void register();

    abstract Class<T> type();

    abstract String typeName();

    final void registerTypeNameAndType() {
        registerWithTypeName(this.typeName());
        registerWithTypeName(this.type().getName());
    }

    /**
     * Registers a mapper for a type name.
     */
    final void registerWithTypeName(final String typeName) {
        final BasicMapper<?> previous = TYPENAME_TO_FACTORY.get(typeName);
        if (null != previous) {
            throw new IllegalArgumentException("Type " + CharSequences.quote(typeName) + " already registered to " + CharSequences.quoteAndEscape(previous.toString()) + " all=" + TYPENAME_TO_FACTORY.keySet());
        }

        TYPENAME_TO_FACTORY.putIfAbsent(typeName, this);
    }

    // fromJsonNode.....................................................................................................

    /**
     * Returns the value from its {@link JsonNode} representation.
     */
    final T fromJsonNode(final JsonNode node,
                         final FromJsonNodeContext context) {
        try {
            return node.isNull() ?
                    this.fromJsonNodeNull(context) :
                    this.fromJsonNodeNonNull(node, context);
        } catch (final FromJsonNodeException | NullPointerException cause) {
            throw cause;
        } catch (final RuntimeException cause) {
            throw new FromJsonNodeException(cause.getMessage(), node, cause);
        }
    }

    abstract T fromJsonNodeNull(final FromJsonNodeContext context);

    abstract T fromJsonNodeNonNull(final JsonNode node,
                                   final FromJsonNodeContext context);

    // toJsonNode.......................................................................................................

    /**
     * Creates the {@link JsonNode} representation of the given value without any type/value enclosing object.
     */
    final JsonNode toJsonNode(final T value,
                              final ToJsonNodeContext context) {
        return null == value ?
                JsonNode.nullNode() :
                this.toJsonNodeNonNull(value, context);
    }

    abstract JsonNode toJsonNodeNonNull(final T value,
                                        final ToJsonNodeContext context);

    /**
     * Creates the {@link JsonNode} with the type representation of the given value.
     */
    final JsonNode toJsonNodeWithType(final T value,
                                      final ToJsonNodeContext context) {
        return null == value ?
                JsonNode.nullNode() :
                this.toJsonNodeWithTypeNonNull(value, context);
    }

    abstract JsonNode toJsonNodeWithTypeNonNull(final T value,
                                                final ToJsonNodeContext context);

    // toString.........................................................................................................

    @Override
    public final String toString() {
        return this.typeName();
    }
}
