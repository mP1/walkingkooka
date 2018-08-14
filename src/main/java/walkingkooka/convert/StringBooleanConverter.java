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
 * A {@link Converter} that knows how to convert {@link String} to {@link Boolean}.
 * Requests for all other types will fail.
 */
final class StringBooleanConverter extends FixedTypeConverter<Boolean> {

    final static StringBooleanConverter INSTANCE = new StringBooleanConverter();

    private StringBooleanConverter() {
        super();
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return value instanceof String && Boolean.class == type;
    }

    @Override
    Boolean convert1(final Object value, final Class<Boolean> type) {
        if(false==value instanceof String){
            failConversion(value);
        }
        return Boolean.valueOf((String) value);
    }

    @Override
    Class<Boolean> targetType() {
        return Boolean.class;
    }

    @Override
    public String toString() {
        return "String->Boolean";
    }
}
