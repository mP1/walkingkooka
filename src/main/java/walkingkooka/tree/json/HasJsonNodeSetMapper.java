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

import java.util.Set;

final class HasJsonNodeSetMapper extends HasJsonNodeCollectionMapper<Set<?>> {

    static HasJsonNodeSetMapper instance() {
        return new HasJsonNodeSetMapper();
    }

    private HasJsonNodeSetMapper() {
        super();
    }

    /**
     * Accepts a {@link Set} and creates a {@link JsonArrayNode} with elements converted to json with types.
     */
    static JsonNode toJsonNodeWithTypeSet0(final Set<?> set) {
        return INSTANCE.toJsonNodeWithTypeCollection0(set);
    }

    private final static HasJsonNodeSetMapper INSTANCE = instance();

    @Override
    final Set<?> fromJsonNode0(final JsonNode node) {
        return node.fromJsonNodeWithTypeSet();
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("set");
}
