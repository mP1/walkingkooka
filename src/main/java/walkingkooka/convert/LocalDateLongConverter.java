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

package walkingkooka.convert;

/**
 * Converts {@link java.time.LocalDate} to {@link Long}
 */
final class LocalDateLongConverter extends LocalDateConverter<Long> {

    /**
     * Factory that creates a new instance with the given date offset.
     * A value of zero = 1/1/1970.
     */
    static LocalDateLongConverter with(final long offset) {
        return new LocalDateLongConverter(offset);
    }

    /**
     * Private ctor use factory
     */
    private LocalDateLongConverter(final long offset) {
        super(offset);
    }

    @Override
    Class<Long> targetType() {
        return Long.class;
    }

    @Override
    Long convert3(final long value) {
        return Long.valueOf(value);
    }
}
