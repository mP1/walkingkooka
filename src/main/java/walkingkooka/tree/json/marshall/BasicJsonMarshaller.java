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
 * A marshaller that converts values to {@link JsonNode} and back.
 */
abstract class BasicJsonMarshaller<T> {

    /**
     * All factory registrations for all {@link BasicJsonMarshaller marshaller}.
     */
    // @VisibleForTesting
    final static Map<String, BasicJsonMarshaller<?>> TYPENAME_TO_MARSHALLER = Maps.concurrent();

    /**
     * Returns the marshaller for the given {@link Class}.
     */
    static <T> BasicJsonMarshaller<T> marshaller(final Class<T> type) {
        return marshaller(
                List.class.isAssignableFrom(type) ? "list" :
                        Set.class.isAssignableFrom(type) ? "set" :
                                Map.class.isAssignableFrom(type) ? "marshall" :
                                        type.getName());
    }

    /**
     * Returns the {@link BasicJsonMarshaller} for the given type name.
     */
    static <T> BasicJsonMarshaller<T> marshaller(final String type) {
        final BasicJsonMarshaller<T> marshaller = Cast.to(TYPENAME_TO_MARSHALLER.get(type));
        if (null == marshaller) {
            throw new UnsupportedTypeJsonNodeException("Type " + CharSequences.quote(type) + " not supported, currently: " + TYPENAME_TO_MARSHALLER.keySet());
        }
        return marshaller;
    }

    // register.........................................................................................................

    /*
     * To avoid race conditions register {@link JsonNode} here, all instance methods create the singleton
     * rather than referencing private static which would be null at this time because their super class
     * this is now running.
     */
    static {
        Lists.of(BasicJsonMarshallerBoolean.instance(),
                BasicJsonMarshallerDouble.instance(),
                BasicJsonMarshallerNumber.instance(),
                BasicJsonMarshallerString.instance(),
                BasicJsonMarshallerTypedBigDecimal.instance(),
                BasicJsonMarshallerTypedBigInteger.instance(),
                BasicJsonMarshallerTypedCharacter.instance(),
                BasicJsonMarshallerTypedJsonNode.instance(),
                BasicJsonMarshallerTypedJsonNodeName.instance(),
                BasicJsonMarshallerTypedCollectionList.instance(),
                BasicJsonMarshallerTypedLocalDate.instance(),
                BasicJsonMarshallerTypedLocalDateTime.instance(),
                BasicJsonMarshallerTypedLocale.instance(),
                BasicJsonMarshallerTypedLocalTime.instance(),
                BasicJsonMarshallerTypedMap.instance(),
                BasicJsonMarshallerTypedMathContext.instance(),
                BasicJsonMarshallerTypedNumberByte.instance(),
                BasicJsonMarshallerTypedNumberShort.instance(),
                BasicJsonMarshallerTypedNumberInteger.instance(),
                BasicJsonMarshallerTypedNumberLong.instance(),
                BasicJsonMarshallerTypedNumberFloat.instance(),
                BasicJsonMarshallerTypedOptional.instance(),
                BasicJsonMarshallerTypedRange.instance(),
                BasicJsonMarshallerTypedRoundingMode.instance(),
                BasicJsonMarshallerTypedStringName.instance(),
                BasicJsonMarshallerTypedCollectionSet.instance()
        ).forEach(BasicJsonMarshaller::register);
    }

    /**
     * Registers the {@link BasicJsonMarshaller} for the given types.
     */
    static synchronized <T> Runnable register(final String typeName,
                                              final BiFunction<JsonNode, FromJsonNodeContext, T> from,
                                              final BiFunction<T, ToJsonNodeContext, JsonNode> to,
                                              final Class<T> type,
                                              final Class<? extends T>... types) {
        return BasicJsonMarshallerTypedGeneric.with(typeName, from, to, type, types)
                .registerGeneric();
    }

    /**
     * Returns the {@link Class} for the given type name.
     */
    static Optional<Class<?>> registeredType(final JsonStringNode name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(TYPENAME_TO_MARSHALLER.get(name.value())).map(m -> m.type());
    }

    /**
     * Returns the type name identifying the given {@link Class} providing it is registered.
     */
    static Optional<JsonStringNode> typeName(final Class<?> type) {
        Objects.requireNonNull(type, "type");

        return Optional.ofNullable(TYPENAME_TO_MARSHALLER.get(type.getName())).map(m -> JsonNode.string(m.toString()));
    }

    // instance.........................................................................................................

    /**
     * Package private to limit sub classing.
     */
    BasicJsonMarshaller() {
        super();
    }

    // register........................................................................................................

    /**
     * Registers this {@link BasicJsonMarshaller}.
     */
    abstract void register();

    abstract Class<T> type();

    abstract String typeName();

    final void registerTypeNameAndType() {
        registerWithTypeName(this.typeName());
        registerWithTypeName(this.type().getName());
    }

    /**
     * Registers a {@link BasicJsonMarshaller} for a {@link String type name}.
     */
    final void registerWithTypeName(final String typeName) {
        final BasicJsonMarshaller<?> previous = TYPENAME_TO_MARSHALLER.get(typeName);
        if (null != previous) {
            throw new IllegalArgumentException("Type " + CharSequences.quote(typeName) + " already registered to " + CharSequences.quoteAndEscape(previous.toString()) + " all=" + TYPENAME_TO_MARSHALLER.keySet());
        }

        TYPENAME_TO_MARSHALLER.putIfAbsent(typeName, this);
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
