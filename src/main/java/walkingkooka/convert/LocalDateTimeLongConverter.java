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
 * Creates a {@link Long} from a {@link LocalDateTime}
 */
final class LocalDateTimeLongConverter extends LocalDateTimeConverter2<Long> {

    /**
     * Singleton
     */
    final static LocalDateTimeLongConverter INSTANCE = new LocalDateTimeLongConverter();

    /**
     * Private ctor use singleton
     */
    private LocalDateTimeLongConverter() {
    }

    @Override
    Long convert3(final long days, final double time, final LocalDateTime localDateTime) {
        if(time != 0) {
            this.failConversion(localDateTime);
        }
        return Long.valueOf(days);
    }

    @Override
    Class<Long> targetType() {
        return Long.class;
    }
}
