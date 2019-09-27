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

import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.ClassAttributes;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A {@link BasicJsonMarshaller} that handles non built in mappings, using two
 * {@link BiFunction} one for reading from a {@link JsonNode} and the other to do the inverse.
 */
final class BasicJsonMarshallerTypedGeneric<T> extends BasicJsonMarshallerTyped<T> {

    static <T> BasicJsonMarshallerTypedGeneric<T> with(final String typeName,
                                                       final BiFunction<JsonNode, JsonNodeUnmarshallContext, T> from,
                                                       final BiFunction<T, JsonNodeMarshallContext, JsonNode> to,
                                                       final Class<T> type,
                                                       final Class<? extends T>... types) {
        CharSequences.failIfNullOrEmpty(typeName, "typeName");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(types, "types");

        Class<?> first = type;
        final List<Class<?>> all = Stream.concat(Stream.of(type), Stream.of(types))
                .peek(t -> Objects.requireNonNull(t, "null type"))
                .collect(Collectors.toList());

        if (all.size() > 1) {
            if (false == ClassAttributes.ABSTRACT.is(first)) {
                throw new IllegalArgumentException("Class " + CharSequences.quoteAndEscape(first.getName()) + " must be abstract");
            }
            final String notSubclasses = all.stream()
                    .skip(1)
                    .filter(t -> !first.isAssignableFrom(t))
                    .map(t -> CharSequences.quoteAndEscape(t.getName()))
                    .collect(Collectors.joining(", "));
            if (!notSubclasses.isEmpty()) {
                throw new IllegalArgumentException("Several classes " + notSubclasses + " are not sub classes of " + CharSequences.quoteAndEscape(first.getName()));
            }
        }

        return new BasicJsonMarshallerTypedGeneric<>(typeName, from, to, type, all);
    }

    private BasicJsonMarshallerTypedGeneric(final String typeName,
                                            final BiFunction<JsonNode, JsonNodeUnmarshallContext, T> from,
                                            final BiFunction<T, JsonNodeMarshallContext, JsonNode> to,
                                            final Class<T> type,
                                            final List<Class<?>> types) {
        super();
        this.typeName = typeName;
        this.from = from;
        this.to = to;
        this.type = type;
        this.types = types;
    }

    @Override
    void register() {
        throw new UnsupportedOperationException();
    }

    /**
     * Registers this {@link BasicJsonMarshallerTypedGeneric} returning a {@link Runnable} which can be used to remove this mapping.
     */
    synchronized Runnable registerGeneric() {
        this.registerTypeNameAndType();
        this.registerTypes(this.types);

        return BasicJsonMarshallerTypedGenericRunnable.with(this);
    }

    /**
     * Eventually called by {@link BasicJsonMarshallerTypedGenericRunnable#run()}
     */
    void remove() {
        TYPENAME_TO_MARSHALLER.remove(this.typeName());
        TYPENAME_TO_MARSHALLER.remove(this.type().getName());

        this.types.stream()
                .map(Class::getName)
                .forEach(TYPENAME_TO_MARSHALLER::remove);
    }

    private final List<Class<?>> types;

    @Override
    Class<T> type() {
        return this.type;
    }

    private final Class<T> type;

    @Override
    String typeName() {
        return this.typeName;
    }

    private final String typeName;

    @Override
    T unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }

    @Override
    T unmarshallNonNull(final JsonNode node,
                          final JsonNodeUnmarshallContext context) {
        return this.from.apply(node, context);
    }

    private final BiFunction<JsonNode, JsonNodeUnmarshallContext, T> from;

    @Override
    JsonNode marshallNonNull(final T value,
                               final JsonNodeMarshallContext context) {
        return to.apply(value, context);
    }

    final BiFunction<T, JsonNodeMarshallContext, JsonNode> to;
}
