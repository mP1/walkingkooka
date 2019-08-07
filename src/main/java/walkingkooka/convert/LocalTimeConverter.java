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

import java.time.LocalTime;

/**
 * Converts a {@link LocalTime} to a given type.
 */
abstract class LocalTimeConverter<T> extends FixedSourceTypeTargetTypeConverter<LocalTime, T> {

    /**
     * Package private to limit sub classing.
     */
    LocalTimeConverter() {
    }

    @Override
    Class<LocalTime> sourceType() {
        return LocalTime.class;
    }

    @Override
    T convert1(final LocalTime value, final ConverterContext context) {
        return this.convert2(value.toSecondOfDay(), value.getNano(), value);
    }

    abstract T convert2(final long seconds, final long nano, final LocalTime localTime);

    @Override
    String toStringSuffix() {
        return "";
    }
}
