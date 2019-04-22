/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.tree.json;

import java.util.function.Function;

final class HasJsonNodeHasJsonNodeMapper<T extends HasJsonNode> extends HasJsonNodeMapper2<T> {

    static <T extends HasJsonNode> HasJsonNodeHasJsonNodeMapper<T> with(final String typeName,
                                                                        final Function<JsonNode, T> fromJsonNode,
                                                                        final Class<T> type) {
        return new HasJsonNodeHasJsonNodeMapper<>(typeName,
                fromJsonNode,
                type);
    }

    private HasJsonNodeHasJsonNodeMapper(final String typeName,
                                         final Function<JsonNode, T> fromJsonNode,
                                         final Class<T> type) {
        super();
        this.typeName = JsonStringNode.with(typeName);
        this.fromJsonNode = fromJsonNode;
        this.type = type;
    }

    @Override
    Class<T> type() {
        return this.type;
    }

    private final Class<T> type;

    @Override
    T fromJsonNodeNull() {
        return null;
    }

    @Override
    T fromJsonNode0(final JsonNode node) {
        return this.fromJsonNode.apply(node);
    }

    private final Function<JsonNode, T> fromJsonNode;

    @Override
    JsonStringNode typeName() {
        return this.typeName;
    }

    private final JsonStringNode typeName;

    @Override
    JsonNode toJsonNode0(final T value) {
        return value.toJsonNode();
    }
}
