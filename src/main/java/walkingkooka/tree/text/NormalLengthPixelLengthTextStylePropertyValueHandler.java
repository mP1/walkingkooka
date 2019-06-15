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
import walkingkooka.tree.json.JsonNode;

/**
 * A {@link TextStylePropertyValueHandler} that only allows {@link NormalLength} and {@link PixelLength} values.
 */
final class NormalLengthPixelLengthTextStylePropertyValueHandler extends TextStylePropertyValueHandler<Length<?>> {

    /**
     * Singleton
     */
    final static NormalLengthPixelLengthTextStylePropertyValueHandler INSTANCE = new NormalLengthPixelLengthTextStylePropertyValueHandler();

    /**
     * Private ctor
     */
    private NormalLengthPixelLengthTextStylePropertyValueHandler() {
        super();
    }

    @Override
    void check0(final Object value, final TextStylePropertyName<?> name) {
        final Length<?> length = this.checkType(value, Length.class, name);
        if(length.isNormal() || length.isPixel()) {
            // ok
        } else {
            final Class<?> valueType = value.getClass();

            String typeName = valueType.getName();
            if (textStylePropertyType(typeName) || hasJsonType(valueType)) {
                typeName = typeName.substring(1 + typeName.lastIndexOf('.'));
            }

            throw new TextStylePropertyValueException("Property " + name.inQuotes() + " value " + CharSequences.quoteIfChars(value) + "(" + typeName + ") is not a " + this.toString());
        }
    }

    @Override
    String expectedTypeName(final Class<?> type) {
        return "NormalLength|PixelLength";
    }

    // fromJsonNode ....................................................................................................

    @Override
    Length<?> fromJsonNode(final JsonNode node, final TextStylePropertyName<?> name) {
        final Length<?> length = Length.fromJsonNode(node);
        this.check0(length, name);
        return length;
    }

    @Override
    JsonNode toJsonNode(final Length<?> value) {
        return value.toJsonNode();
    }

    // Object ..........................................................................................................

    @Override
    public String toString() {
        return NormalLength.class.getSimpleName() + "|" + PixelLength.class.getSimpleName();
    }
}
