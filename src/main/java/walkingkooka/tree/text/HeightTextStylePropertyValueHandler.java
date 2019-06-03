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
 * A {@link TextStylePropertyValueHandler} for {@link Height} parameter values.
 */
final class HeightTextStylePropertyValueHandler extends TextStylePropertyValueHandler<Height> {

    /**
     * Singleton
     */
    final static HeightTextStylePropertyValueHandler INSTANCE = new HeightTextStylePropertyValueHandler();

    /**
     * Private ctor
     */
    private HeightTextStylePropertyValueHandler() {
        super();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        this.checkType(value, Height.class, name);
    }

    // fromJsonNode ....................................................................................................

    @Override
    Height fromJsonNode(final JsonNode node) {
        return Height.fromJsonNode(node);
    }

    @Override
    JsonNode toJsonNode(final Height value) {
        return value.toJsonNode();
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return Height.class.getSimpleName();
    }
}
