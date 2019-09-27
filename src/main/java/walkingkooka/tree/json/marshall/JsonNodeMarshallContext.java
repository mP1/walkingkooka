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

import walkingkooka.Context;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * A {@link Context} that accompanies transforming an object into a {@link JsonNode}.
 */
public interface JsonNodeMarshallContext extends JsonNodeContext {

    /**
     * A {@link BiFunction processor} that simply returns the given object ignoring the value.
     */
    BiFunction<Object, JsonObjectNode, JsonObjectNode> OBJECT_PRE_PROCESSOR = (value, jsonObject) -> jsonObject;

    /**
     * Sets or replaces the {@link BiFunction object post processor} creating a new instance as necessary.
     */
    JsonNodeMarshallContext setObjectPostProcessor(final BiFunction<Object, JsonObjectNode, JsonObjectNode> processor);

    /**
     * Returns the {@link JsonNode} equivalent of this object. This is ideal for situations where the value is not dynamic.
     */
    JsonNode marshall(final Object value);

    /**
     * Creates a {@link JsonNode} that records the type name and the json representation of the given object.
     */
    JsonNode marshallWithType(final Object value);

    /**
     * Accepts a {@link List} of elements which are assumed to be the same supported type.
     */
    JsonNode marshallList(final List<?> list);

    /**
     * Accepts a {@link Set} of elements which are assumed to be the same supported type.
     */
    JsonNode marshallSet(final Set<?> set);

    /**
     * Accepts a {@link Set} of elements which are assumed to be supported.
     */
    JsonNode marshallMap(final Map<?, ?> map);

    /**
     * Accepts a {@link List} of elements which are assumed to be supported.
     */
    JsonNode marshallWithTypeList(final List<?> list);

    /**
     * Accepts a {@link Set} of elements which are assumed to be supported.
     */
    JsonNode marshallWithTypeSet(final Set<?> set);

    /**
     * Accepts a {@link Map} and returns its {@link JsonNode} equivalent.
     */
    JsonNode marshallWithTypeMap(final Map<?, ?> map);
}
