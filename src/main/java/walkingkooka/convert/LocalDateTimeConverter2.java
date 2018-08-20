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
 * Converts an object from a {@link LocalDateTime}
 */
abstract class LocalDateTimeConverter2<T> extends FixedTypeConverter2<LocalDateTime, T>{

    /**
     * Package private to limit sub classing.
     */
    LocalDateTimeConverter2() {
    }

    @Override
    Class<LocalDateTime> sourceType() {
        return LocalDateTime.class;
    }

    @Override
    T convert2(final LocalDateTime value) {
        return this.convert3(value.toLocalDate().toEpochDay(),
                (double)value.toLocalTime().toNanoOfDay() / Converters.NANOS_PER_DAY,
                value);
    }

    abstract T convert3(final long days, final double time, final LocalDateTime localDateTime);
}
