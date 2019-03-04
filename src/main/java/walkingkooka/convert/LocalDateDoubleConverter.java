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
 * Converts {@link java.time.LocalDate} to {@link Double}
 */
final class LocalDateDoubleConverter extends LocalDateConverter<Double> {

    /**
     * Factory that creates a new instance with the given date offset.
     * A value of zero = 1/1/1970.
     */
    static LocalDateDoubleConverter with(final long offset) {
        return new LocalDateDoubleConverter(offset);
    }

    /**
     * Private ctor use factory
     */
    private LocalDateDoubleConverter(final long offset) {
        super(offset);
    }

    @Override
    Class<Double> targetType() {
        return Double.class;
    }

    @Override
    Double convert3(final long value) {
        final double doubleValue = (double)value;
        if(value!=doubleValue){
            this.failConversion(value);
        }
        return Double.valueOf(value);
    }
}
