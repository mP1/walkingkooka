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

final class HasJsonNode2Registration {

    static HasJsonNode2Registration with(final String type, final Function<JsonNode, ?> from) {
        return new HasJsonNode2Registration(type, from);
    }

    private HasJsonNode2Registration(final String type,
                                     final Function<JsonNode, ?> from) {
        super();
        this.from = from;
        this.type = type;
    }

    /**
     * A {@link Function} which typically calls a static fromJsonNode method.
     */
    final Function<JsonNode, ?> from;

    /**
     * The {@link JsonObjectNode} holding type=$typename must be created lazily after all registration. Attempts to create
     * during registration will result in exceptions when the {@link JsonObjectNode} is created and the TYPE property set.
     */
    JsonObjectNode objectWithType() {
        if (null == this.objectWithType) {
            this.objectWithType = JsonNode.object()
                    .set(HasJsonNode2.TYPE, JsonNode.string(this.type));
        }
        return this.objectWithType;
    }

    /**
     * The fully qualified type name.
     */
    private final String type;

    private JsonObjectNode objectWithType;

    @Override
    public String toString() {
        return this.from.toString();
    }
}
