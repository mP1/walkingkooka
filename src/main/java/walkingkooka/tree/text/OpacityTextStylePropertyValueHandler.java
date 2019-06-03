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
 * A {@link TextStylePropertyValueHandler} for {@link Opacity} parameter values.
 */
final class OpacityTextStylePropertyValueHandler extends TextStylePropertyValueHandler<Opacity> {

    /**
     * Singleton
     */
    final static OpacityTextStylePropertyValueHandler INSTANCE = new OpacityTextStylePropertyValueHandler();

    /**
     * Private ctor
     */
    private OpacityTextStylePropertyValueHandler() {
        super();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        this.checkType(value, Opacity.class, name);
    }

    // fromJsonNode ....................................................................................................

    @Override
    Opacity fromJsonNode(final JsonNode node) {
        return Opacity.fromJsonNode(node);
    }

    @Override
    JsonNode toJsonNode(final Opacity value) {
        return value.toJsonNode();
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return Opacity.class.getSimpleName();
    }
}
