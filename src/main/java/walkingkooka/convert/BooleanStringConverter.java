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

import java.util.Objects;

/**
 * A {@link Converter} that knows how to convert {@link Boolean} to {@link String}.
 * Requests for all other types will fail.
 */
final class BooleanStringConverter extends FixedTypeConverter<String> {

    final static BooleanStringConverter with(final String trueValue, final String falseValue){
        Objects.requireNonNull(trueValue, "trueValue");
        Objects.requireNonNull(falseValue, "falseValue");

        return new BooleanStringConverter(trueValue, falseValue);
    }

    private BooleanStringConverter(final String trueValue, final String falseValue) {
        super();
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return value instanceof Boolean && String.class == type;
    }

    @Override
    String convert1(final Object value, final Class<String> type) {
        return Boolean.TRUE.equals(value) ?
                this.trueValue :
                this.falseValue;
    }

    private final String trueValue;
    private final String falseValue;

    @Override
    Class<String> targetType() {
        return String.class;
    }

    @Override
    public String toString() {
        return "Boolean->String";
    }
}
