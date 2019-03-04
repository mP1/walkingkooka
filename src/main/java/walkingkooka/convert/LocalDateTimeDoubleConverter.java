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

import java.time.LocalDateTime;

/**
 * Creates a {@link Double} from a {@link LocalDateTime}
 */
final class LocalDateTimeDoubleConverter extends LocalDateTimeConverter2<Double> {

    /**
     * Creates a new instance with the given date offset.
     * A value of zero = 1/1/1970.
     */
    static LocalDateTimeDoubleConverter with(final long offset) {
        return new LocalDateTimeDoubleConverter(offset);
    }

    /**
     * Private ctor use factory
     */
    private LocalDateTimeDoubleConverter(final long offset) {
        super(offset);
    }

    @Override
    Double convert3(final long days, final double time, final LocalDateTime localDateTime) {
        return Double.valueOf(days + time);
    }

    @Override
    Class<Double> targetType() {
        return Double.class;
    }
}
