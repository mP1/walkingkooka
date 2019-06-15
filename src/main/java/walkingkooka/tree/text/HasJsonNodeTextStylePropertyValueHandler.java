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

import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

import java.util.function.Function;

/**
 * A {@link TextStylePropertyValueHandler} that acts as  bridge to a type that also implements {@link walkingkooka.tree.json.HasJsonNode}
 */
final class HasJsonNodeTextStylePropertyValueHandler<H extends HasJsonNode> extends TextStylePropertyValueHandler<H> {

    /**
     * Factory
     */
    static <T extends HasJsonNode> HasJsonNodeTextStylePropertyValueHandler<T> with(final Class<T> type,
                                                                                          final Function<JsonNode, T> fromJsonNode) {
        return new HasJsonNodeTextStylePropertyValueHandler<>(type, fromJsonNode);
    }

    /**
     * Private ctor
     */
    private HasJsonNodeTextStylePropertyValueHandler(final Class<H> type,
                                                     final Function<JsonNode, H> fromJsonNode) {
        super();
        this.fromJsonNode = fromJsonNode;
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

    // fromJsonNode ....................................................................................................

    @Override
    H fromJsonNode(final JsonNode node, final TextStylePropertyName<?> name) {
        return this.fromJsonNode.apply(node);
    }

    private final Function<JsonNode, H> fromJsonNode;

    @Override
    JsonNode toJsonNode(final H value) {
        return value.toJsonNode();
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return this.type.getSimpleName();
    }

    private final Class<H> type;
}
