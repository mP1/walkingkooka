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

import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;

import java.util.function.Function;

/**
 * Base converter that provides support for converting header text to values and back.
 */
abstract class TextPropertyValueConverter<T> {

    /**
     * {@see ColorTextPropertyValueConverter}
     */
    static TextPropertyValueConverter<Color> color() {
        return ColorTextPropertyValueConverter.INSTANCE;
    }

    /**
     * {@see EnumTextPropertyValueConverter}
     */
    static <E extends Enum<E>> EnumTextPropertyValueConverter<E> enumTextPropertyValueConverter(final Function<String, E> factory,
                                                                                                final Class<E> type) {
        return EnumTextPropertyValueConverter.with(factory, type);
    }

    /**
     * {@see FontFamilyNameTextPropertyValueConverter}
     */
    static TextPropertyValueConverter<FontFamilyName> fontFamilyName() {
        return FontFamilyNameTextPropertyValueConverter.INSTANCE;
    }

    /**
     * {@see FontSizeTextPropertyValueConverter}
     */
    static TextPropertyValueConverter<FontSize> fontSize() {
        return FontSizeTextPropertyValueConverter.INSTANCE;
    }

    /**
     * {@see FontWeightTextPropertyValueConverter}
     */
    static TextPropertyValueConverter<FontWeight> fontWeight() {
        return FontWeightTextPropertyValueConverter.INSTANCE;
    }
    
    /**
     * {@see StringTextPropertyValueConverter}
     */
    static TextPropertyValueConverter<String> string() {
        return StringTextPropertyValueConverter.INSTANCE;
    }

    /**
     * Package private to limit sub classing.
     */
    TextPropertyValueConverter() {
        super();
    }

    // checkValue...........................................................

    final T check(final Object value, final TextPropertyName<?> name) {
        if (null == value) {
            throw new TextPropertyValueException("Property " + name.inQuotes() + " missing value");
        }

        this.check0(value, name);
        return Cast.to(value);
    }

    abstract void check0(final Object value, final TextPropertyName<?> name);

    /**
     * Checks the type of the given value and throws a {@link TextPropertyValueException} if this test fails.
     */
    final <U> U checkType(final Object value, final Class<U> type, final TextPropertyName<?> name) {
        if (!type.isInstance(value)) {
            throw new TextPropertyValueException("Property " + name.inQuotes() + " value " + CharSequences.quoteIfChars(value) + " is not a " + type.getName());
        }
        return type.cast(value);
    }

    // fromJsonNode ....................................................................................................

    /**
     * Transforms a {@link JsonNode} into a value.
     */
    abstract T fromJsonNode(final JsonNode node);

    /**
     * Transforms a value into json, performing the inverse of {@link #fromJsonNode(JsonNode)}
     */
    abstract JsonNode toJsonNode(final T value);

    // Object .........................................................................................................

    @Override
    abstract public String toString();

    final String toStringType(final Class<?> type) {
        return type.getSimpleName();
    }
}
