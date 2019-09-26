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

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * A factory that lazily retrieves the type of a given {@link JsonNode} from a given property on another object.
 */
final class FromJsonNodeWithTypePropertyBiFunction<T> implements BiFunction<JsonNode, FromJsonNodeContext, T> {

    /**
     * Factory called only by {@link FromJsonNodeContext#fromJsonNodeWithType(JsonNodeName, JsonObjectNode, Class)}
     */
    static <T> FromJsonNodeWithTypePropertyBiFunction<T> with(final JsonNodeName property,
                                                              final JsonObjectNode source,
                                                              final Class<T> superType) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(superType, "superType");

        return new FromJsonNodeWithTypePropertyBiFunction<>(property, source);
    }

    private FromJsonNodeWithTypePropertyBiFunction(final JsonNodeName property,
                                                   final JsonObjectNode source) {
        super();
        this.property = property;
        this.source = source;
    }

    @Override
    public T apply(final JsonNode node,
                   final FromJsonNodeContext context) {
        final JsonNodeName property = this.property;
        final JsonObjectNode source = this.source;

        try {
            final JsonNode typeName = source.getOrFail(property);
            if (!typeName.isString()) {
                throw new FromJsonNodeException("Property " + property + " contains invalid type name", source);
            }
            final JsonStringNode stringTypeName = typeName.cast();
            final Class<?> type = context.registeredType(stringTypeName)
                    .orElseThrow(() -> new FromJsonNodeException("Unknown type " + CharSequences.quoteAndEscape(stringTypeName.value()), this.source));

            return Cast.to(type.cast(context.fromJsonNode(node, type)));
        } catch (final IllegalArgumentException cause) {
            throw new FromJsonNodeException(cause.getMessage(), node);
        }
    }

    private final JsonNodeName property;
    private final JsonObjectNode source;

    @Override
    public String toString() {
        return this.property + " in " + this.source;
    }
}
