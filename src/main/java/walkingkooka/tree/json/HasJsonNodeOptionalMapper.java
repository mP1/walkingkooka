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

import java.util.List;
import java.util.Optional;

/**
 * Handles transforming Optionals and the contained value to and from json.
 */
final class HasJsonNodeOptionalMapper extends HasJsonNodeTypedMapper<Optional<?>> {

    static HasJsonNodeOptionalMapper instance() {
        return new HasJsonNodeOptionalMapper();
    }

    /**
     * Private ctor use factory.
     */
    private HasJsonNodeOptionalMapper() {
        super();
    }

    @Override
    Class<Optional<?>> type() {
        return TYPE;
    }

    private final static Class<Optional<?>> TYPE = Cast.to(Optional.class);

    @Override
    Optional<?> fromJsonNodeNull() {
        return null;
    }

    @Override
    Optional<?> fromJsonNodeNonNull(final JsonNode node) {
        return fromJsonNodeNonNull0(node.arrayOrFail());
    }

    private Optional<?> fromJsonNodeNonNull0(final JsonArrayNode array) {
        final List<JsonNode> children = array.children();
        return children.isEmpty() ?
                Optional.empty() :
                this.fromJsonNodeNonNull1(children, array);
    }

    private Optional<?> fromJsonNodeNonNull1(final List<JsonNode> children, final JsonNode parent) {
        if(children.size() > 1) {
            throw new FromJsonNodeException("Optional expected only 0/1 children but got " + children, parent);
        }

        return Optional.of(fromJsonNodeWithType(children.get(0)));
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("optional");

    /**
     * Returns an array which will hold either a single value or nothing when the optional is empty.
     */
    @Override
    JsonNode toJsonNodeNonNull(final Optional<?> value) {
        return value.map(HasJsonNodeOptionalMapper::toJsonNodeNonNullValue)
                .orElse(JsonNode.array());
    }

    private static JsonArrayNode toJsonNodeNonNullValue(final Object value) {
        return JsonNode.array().appendChild(HasJsonNodeMapMapper.toJsonNodeWithTypeObject(value));
    }
}
