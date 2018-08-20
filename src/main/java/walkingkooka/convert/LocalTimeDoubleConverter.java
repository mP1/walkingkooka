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

import java.time.LocalTime;

/**
 * Converts a {@link LocalTime} into a {@link Double}.
 */
final class LocalTimeDoubleConverter extends LocalTimeConverter<Double> {

    /**
     * Singleton
     */
    final static LocalTimeDoubleConverter INSTANCE = new LocalTimeDoubleConverter();

    /**
     * Private ctor use singleton
     */
    private LocalTimeDoubleConverter() {
    }

    @Override
    Double convert3(final long seconds, final long nano, final LocalTime localTime) {
        return Double.valueOf((double)seconds + (double)nano / Converters.NANOS_PER_SECOND);
    }

    @Override
    Class<Double> targetType() {
        return Double.class;
    }
}
