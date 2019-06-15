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

abstract class HasJsonNodeMapper2<T> extends HasJsonNodeMapper<T> {

    HasJsonNodeMapper2() {
        super();
    }

    abstract T fromJsonNode0(final JsonNode node);

    @Override
    final JsonNode toJsonNodeWithType0(final T value) {
        return this.objectWithType()
                .set(JsonObjectNode.VALUE, this.toJsonNode0(value));
    }

    /**
     * The {@link JsonObjectNode} holding type=$typename must be created lazily after all registration. Attempts to create
     * during registration will result in exceptions when the {@link JsonObjectNode} is created and the TYPE property set.
     */
    final JsonObjectNode objectWithType() {
        if (null == this.objectWithType) {
            this.objectWithType = JsonNode.object()
                    .set(JsonObjectNode.TYPE, this.typeName());
        }
        return this.objectWithType;
    }

    private JsonObjectNode objectWithType;
}
