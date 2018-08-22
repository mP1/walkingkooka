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

/**
 * Converter that returns the value if the requested target type is the same.
 */
final class SimpleConverter implements Converter{

    /**
     * Singleton
     */
    static final SimpleConverter INSTANCE = new SimpleConverter();

    /**
     * Private ctor
     */
    private SimpleConverter() {
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return type.isInstance(value);
    }

    @Override
    public <T> T convert(final Object value, final Class<T> type) {
        try {
            return type.cast(value);
        } catch (final ClassCastException fail) {
            return this.failConversion(value, type);
        }
    }

    @Override
    public String toString() {
        return "value instanceof target type.";
    }
}
