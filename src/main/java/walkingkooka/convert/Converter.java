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

package walkingkooka.convert;

import walkingkooka.text.CharSequences;

/**
 * Converts object instances to a requested target {@link Class type}.
 */
public interface Converter {

    /**
     * Queries whether this converter supports converting to the requested type.
     */
    boolean canConvert(final Object value, Class<?> type);

    /**
     * Converts the object to the request type.
     */
    <T> T convert(final Object value, final Class<T> type);

    default void failIfUnsupportedType(final Object value, final Class<?> target) {
        if(!this.canConvert(value, target)){
            this.failConversion(value, target);
        }
    }

    default <TT> TT failConversion(final Object value, final Class<TT> target) {
        throw new ConversionException("Failed to convert " + value.getClass().getName() + "=" + CharSequences.quoteIfChars(value) + " to " + target.getName());
    }
}
