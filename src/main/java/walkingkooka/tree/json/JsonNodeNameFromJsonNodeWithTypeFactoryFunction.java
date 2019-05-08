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

import java.util.Objects;
import java.util.function.Function;

/**
 * A factory that lazily retrieves the type for a {@link JsonNode} to return a java object.
 */
final class JsonNodeNameFromJsonNodeWithTypeFactoryFunction<T> implements Function<JsonNode, T> {

    /**
     * Factory called only by {@link JsonNodeName#fromJsonNodeWithTypeFactory}
     */
    static <T> JsonNodeNameFromJsonNodeWithTypeFactoryFunction<T> with(final JsonNodeName property,
                                                                       final JsonObjectNode source,
                                                                       final Class<T> superType) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(superType, "superType");

        return new JsonNodeNameFromJsonNodeWithTypeFactoryFunction<>(property, source);
    }

    /**
     * Private ctor use factory or public {@link JsonNodeName#fromJsonNodeWithTypeFactory(JsonObjectNode, Class)}.
     */
    private JsonNodeNameFromJsonNodeWithTypeFactoryFunction(final JsonNodeName property,
                                                            final JsonObjectNode source) {
        super();
        this.property = property;
        this.source = source;
    }

    @Override
    public T apply(final JsonNode node) {
        return this.mapper().fromJsonNode(node);
    }

    /**
     * Retrieves the mapper for the type name at {@link #property}.
     */
    private HasJsonNodeMapper<T> mapper() {
        if (null == this.mapper) {
            this.mapper = HasJsonNodeMapper.mapperOrFail(this.source.getOrFail(this.property).stringValueOrFail());
        }
        return this.mapper;
    }

    /**
     * A lazily resolved mapper for the type pointed by the property in the source object.
     */
    // VisibleForTesting
    HasJsonNodeMapper<T> mapper;

    private final JsonNodeName property;
    private final JsonObjectNode source;

    @Override
    public String toString() {
        return this.property + " in " + this.source;
    }
}
