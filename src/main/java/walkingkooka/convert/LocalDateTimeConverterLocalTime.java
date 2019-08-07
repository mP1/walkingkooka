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

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Converters {@link LocalDateTime} into {@link LocalTime} by simply returning the {@link LocalTime} component.
 */
final class LocalDateTimeConverterLocalTime extends LocalDateTimeConverter<LocalTime> {

    /**
     * Singleton
     */
    final static LocalDateTimeConverterLocalTime INSTANCE = new LocalDateTimeConverterLocalTime();

    /**
     * Private ctor use singleton
     */
    private LocalDateTimeConverterLocalTime() {
        super(0);
    }

    @Override
    Class<LocalTime> targetType() {
        return LocalTime.class;
    }

    @Override
    LocalTime convert3(final long days,
                       final double time,
                       final LocalDateTime localDateTime) {
        return localDateTime.toLocalTime();
    }

    @Override
    String toStringSuffix() {
        return "";
    }
}
