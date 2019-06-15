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

package walkingkooka.tree.text;

import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

/**
 * A {@link TextStylePropertyValueHandler} that acts as  bridge to a type that also implements {@link HasJsonNode} and also
 * records the type.
 */
final class HasJsonNodeWithTypeTextStylePropertyValueHandler extends TextStylePropertyValueHandler<Object> {

    /**
     * Singleton
     */
    static HasJsonNodeWithTypeTextStylePropertyValueHandler INSTANCE = new HasJsonNodeWithTypeTextStylePropertyValueHandler();

    /**
     * Private ctor
     */
    private HasJsonNodeWithTypeTextStylePropertyValueHandler() {
        super();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        HasJsonNode.typeName(value.getClass())
                .orElseThrow(() -> new TextStylePropertyValueException("Property " + name.inQuotes() + " value " + CharSequences.quoteIfChars(value) + " is not a supported type"));
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return "String";
    }

    // fromJsonNode ....................................................................................................

    @Override
    Object fromJsonNode(final JsonNode node, final TextStylePropertyName<?> name) {
        return node.objectOrFail().fromJsonNodeWithType();
    }

    @Override
    JsonNode toJsonNode(final Object value) {
        return HasJsonNode.toJsonNodeWithType(value);
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return "HasJsonNodeWithType";
    }
}
