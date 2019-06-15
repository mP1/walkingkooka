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

package walkingkooka.tree.text;

import walkingkooka.tree.json.JsonNode;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link TextStylePropertyValueHandler} for {@link Enum} parameter values.
 */
final class EnumTextStylePropertyValueHandler<E extends Enum<E>> extends TextStylePropertyValueHandler<E> {

    /**
     * Factory that creates a new {@link EnumTextStylePropertyValueHandler}.
     */
    static <E extends Enum<E>> EnumTextStylePropertyValueHandler<E> with(final Function<String, E> factory,
                                                                               final Class<E> type) {
        Objects.requireNonNull(factory, "factory");
        Objects.requireNonNull(type, "type");

        return new EnumTextStylePropertyValueHandler<>(factory, type);
    }

    /**
     * Private ctor
     */
    private EnumTextStylePropertyValueHandler(final Function<String, E> factory,
                                              final Class<E> type) {
        super();
        this.factory = factory;
        this.type = type;
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        this.checkType(value, this.type, name);
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return this.type.getSimpleName();
    }

    private final Class<E> type;

    // fromJsonNode ....................................................................................................

    @Override
    E fromJsonNode(final JsonNode node, final TextStylePropertyName<?> name) {
        return this.factory.apply(node.stringValueOrFail());
    }

    private final Function<String, E> factory;

    @Override
    JsonNode toJsonNode(final E value) {
        return JsonNode.string(value.name());
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return this.type.getSimpleName();
    }
}
