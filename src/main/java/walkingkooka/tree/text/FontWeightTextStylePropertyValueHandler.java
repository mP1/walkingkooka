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

package walkingkooka.tree.text;

import walkingkooka.tree.json.JsonNode;

/**
 * A {@link TextStylePropertyValueHandler} for {@link FontWeight} parameter values.
 */
final class FontWeightTextStylePropertyValueHandler extends TextStylePropertyValueHandler<FontWeight> {

    /**
     * Singleton
     */
    final static FontWeightTextStylePropertyValueHandler INSTANCE = new FontWeightTextStylePropertyValueHandler();

    /**
     * Private ctor
     */
    private FontWeightTextStylePropertyValueHandler() {
        super();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        this.checkType(value, FontWeight.class, name);
    }

    // fromJsonNode ....................................................................................................

    @Override
    FontWeight fromJsonNode(final JsonNode node) {
        return FontWeight.fromJsonNode(node);
    }

    @Override
    JsonNode toJsonNode(final FontWeight value) {
        return value.toJsonNode();
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return FontWeight.class.getSimpleName();
    }
}
