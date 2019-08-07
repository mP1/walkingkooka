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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Accepts {@link LocalDateTime} and returns the {@link java.time.LocalDate}
 */
final class LocalDateTimeLocalDateConverter extends LocalDateTimeConverter2<LocalDate> {

    /**
     * Singleton
     */
    final static LocalDateTimeLocalDateConverter INSTANCE = new LocalDateTimeLocalDateConverter();

    /**
     * Private ctor use singleton
     */
    private LocalDateTimeLocalDateConverter() {
        super(0);
    }

    @Override
    Class<LocalDate> targetType() {
        return LocalDate.class;
    }

    @Override
    LocalDate convert3(final long days, final double time, final LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    @Override
    String toStringSuffix() {
        return "";
    }
}
