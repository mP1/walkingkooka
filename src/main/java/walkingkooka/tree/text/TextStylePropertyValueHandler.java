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

import walkingkooka.Cast;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

import java.util.function.Function;

/**
 * Base converter that provides support for handling property values.
 */
abstract class TextStylePropertyValueHandler<T> {

    /**
     * {@see EnumTextStylePropertyValueHandler}
     */
    static <E extends Enum<E>> EnumTextStylePropertyValueHandler<E> enumTextPropertyValueHandler(final Function<String, E> factory,
                                                                                                 final Class<E> type) {
        return EnumTextStylePropertyValueHandler.with(factory, type);
    }

    /**
     * {@see HasJsonNodeTextStylePropertyValueHandler}
     */
    static <T extends HasJsonNode> TextStylePropertyValueHandler<T> hasJsonNode(final Class<T> type,
                                                                                final Function<JsonNode, T> fromJsonNode) {
        return HasJsonNodeTextStylePropertyValueHandler.with(type, fromJsonNode);
    }

    /**
     * {@see HasJsonNodeWithTypeTextStylePropertyValueHandler}
     */
    static HasJsonNodeWithTypeTextStylePropertyValueHandler hasJsonNodeWithType() {
        return HasJsonNodeWithTypeTextStylePropertyValueHandler.INSTANCE;
    }

    /**
     * {@see NoneLengthPixelLengthTextStylePropertyValueHandler}
     */
    static NoneLengthPixelLengthTextStylePropertyValueHandler noneLengthPixelLength() {
        return NoneLengthPixelLengthTextStylePropertyValueHandler.INSTANCE;
    }
    
    /**
     * {@see NormalLengthPixelLengthTextStylePropertyValueHandler}
     */
    static NormalLengthPixelLengthTextStylePropertyValueHandler normalLengthPixelLength() {
        return NormalLengthPixelLengthTextStylePropertyValueHandler.INSTANCE;
    }

    /**
     * {@see StringTextStylePropertyValueHandler}
     */
    static TextStylePropertyValueHandler<String> string() {
        return StringTextStylePropertyValueHandler.INSTANCE;
    }

    /**
     * Package private to limit sub classing.
     */
    TextStylePropertyValueHandler() {
        super();
    }

    // checkValue...........................................................

    final T check(final Object value, final TextStylePropertyName<?> name) {
        if (null == value) {
            throw new TextStylePropertyValueException("Property " + name.inQuotes() + " missing value");
        }

        this.check0(value, name);
        return Cast.to(value);
    }

    abstract void check0(final Object value, final TextStylePropertyName<?> name);

    /**
     * Checks the type of the given value and throws a {@link TextStylePropertyValueException} if this test fails.
     */
    final <U> U checkType(final Object value, final Class<U> type, final TextStylePropertyName<?> name) {
        if (!type.isInstance(value)) {
            final Class<?> valueType = value.getClass();

            String typeName = valueType.getName();
            if (textStylePropertyType(typeName) || hasJsonType(valueType)) {
                typeName = typeName.substring(1 + typeName.lastIndexOf('.'));
            }

            throw new TextStylePropertyValueException("Property " + name.inQuotes() + " value " + CharSequences.quoteIfChars(value) + "(" + typeName + ") is not a " + this.expectedTypeName(type));
        }
        return type.cast(value);
    }

    abstract String expectedTypeName(final Class<?> type);

    final boolean textStylePropertyType(final String type) {
        return type.startsWith(PACKAGE) && type.indexOf('.', 1 + PACKAGE.length()) == -1;
    }

    final boolean hasJsonType(final Class<?> type) {
        return HasJsonNode.typeName(type).isPresent();
    }

    private final static String PACKAGE = "walkingkooka.tree.text";

    // fromJsonNode ....................................................................................................

    /**
     * Transforms a {@link JsonNode} into a value.
     */
    abstract T fromJsonNode(final JsonNode node, final TextStylePropertyName<?> name);

    /**
     * Transforms a value into json, performing the inverse of {@link #fromJsonNode(JsonNode, TextStylePropertyName)}
     */
    abstract JsonNode toJsonNode(final T value);

    // Object .........................................................................................................

    @Override
    abstract public String toString();

    final String toStringType(final Class<?> type) {
        return type.getSimpleName();
    }
}
