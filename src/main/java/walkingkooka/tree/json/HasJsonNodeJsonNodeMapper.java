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

package walkingkooka.tree.json;

import walkingkooka.Cast;

final class HasJsonNodeJsonNodeMapper<T extends JsonNode> extends HasJsonNodeMapper2<JsonNode> {

    static <T extends JsonNode> HasJsonNodeJsonNodeMapper<T> with(final Class<T> type,
                                                                  final String typeName) {
        return new HasJsonNodeJsonNodeMapper<>(type, typeName);
    }

    private HasJsonNodeJsonNodeMapper(final Class<T> type, final String typeName) {
        super();
        this.type = type;
        this.typeName = JsonStringNode.with(typeName);
    }

    @Override
    Class<JsonNode> type() {
        return Cast.to(this.type);
    }

    @Override
    JsonNode fromJsonNodeNull() {
        return Cast.to(JsonNode.nullNode());
    }

    @Override
    T fromJsonNode0(final JsonNode node) {
        return this.type.cast(node.toJsonNode());
    }

    private final Class<T> type;

    @Override
    JsonStringNode typeName() {
        return this.typeName;
    }

    private final JsonStringNode typeName;

    @Override
    JsonNode toJsonNode0(final JsonNode value) {
        return this.type.cast(value);
    }
}
