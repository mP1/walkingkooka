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

/**
 * Interface implemented by objects that can be expressed or have a {@link JsonNode} equivalent.
 */
public interface HasJsonNode {

    /**
     * Shared function used to report a required property is missing within a static fromJsonNode.
     */
    static void requiredPropertyMissing(final JsonNodeName property,
                                        final JsonNode node) {
        throw new IllegalArgumentException("Required property " + property + " missing=" + node);
    }

    /**
     * Shared function used to report a required property is missing within a static fromJsonNode.
     */
    static void unknownPropertyPresent(final JsonNodeName property,
                                       final JsonNode node) {
        throw new IllegalArgumentException("Unknown property " + property + " present=" + node);
    }

    /**
     * Returns the {@link JsonNode} equivalent of this object.
     */
    JsonNode toJsonNode();
}
