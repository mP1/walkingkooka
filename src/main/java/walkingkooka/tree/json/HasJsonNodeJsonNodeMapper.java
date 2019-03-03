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

import walkingkooka.tree.Node;

final class HasJsonNodeJsonNodeMapper<T extends JsonNode> extends HasJsonNodeMapper2<T> {

    final static <T extends JsonNode> HasJsonNodeJsonNodeMapper<T> with(final Class<T> type) {
        return new HasJsonNodeJsonNodeMapper<>(type);
    }

    private HasJsonNodeJsonNodeMapper(final Class<T> type) {
        super();
        this.type = type;

        final String simple = type.getSimpleName();
        this.typeName = JsonStringNode.with(simple.substring(0, simple.length() - Node.class.getSimpleName().length()));
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
    JsonNode toJsonNodeObjectValue(final T value) {
        return value;
    }
}
