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

    final static <T extends HasJsonNode> HasJsonNodeHasJsonNodeMapper<T> with(final String typeName,
                                                                              final Function<JsonNode, T> fromJsonNode) {
        return new HasJsonNodeHasJsonNodeMapper<>(typeName, fromJsonNode);
    }

    private HasJsonNodeHasJsonNodeMapper(final String typeName,
                                         final Function<JsonNode, T> fromJsonNode) {
        super();
        this.typeName = JsonStringNode.with(typeName);
        this.fromJsonNode=fromJsonNode;
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
    JsonNode toJsonNodeObjectValue(final T value) {
        return value.toJsonNode();
    }
}
