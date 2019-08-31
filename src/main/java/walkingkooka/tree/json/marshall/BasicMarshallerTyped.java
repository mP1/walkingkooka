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

import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;

/**
 * A {@link BasicMarshaller} that includes a json object with the type name and the actual jsonized value.
 * This is mostly used for in built JDK types that cannot be altered.
 */
abstract class BasicMarshallerTyped<T> extends BasicMarshaller<T> {

    BasicMarshallerTyped() {
        super();
    }

    final void registerTypes(final List<Class<?>> types) {
        types.stream()
                .filter(t -> t != this.type())
                .map(Class::getName)
                .forEach(this::registerWithTypeName);
    }

    @Override
    final JsonNode toJsonNodeWithTypeNonNull(final T value,
                                             final ToJsonNodeContext context) {
        return this.objectWithType()
                .set(BasicJsonNodeContext.VALUE, context.toJsonNode(value));
    }

    /**
     * The {@link JsonObjectNode} holding type=$typename must be created lazily after all registration. Attempts to create
     * during registration will result in exceptions when the {@link JsonObjectNode} is created and the TYPE property set.
     */
    final JsonObjectNode objectWithType() {
        if (null == this.objectWithType) {
            this.objectWithType = JsonNode.object()
                    .set(BasicJsonNodeContext.TYPE, JsonNode.string(this.toString()));
        }
        return this.objectWithType;
    }

    private JsonObjectNode objectWithType;
}
