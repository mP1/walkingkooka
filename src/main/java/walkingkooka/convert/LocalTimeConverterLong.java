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
 * Converts a {@link LocalTime} into a {@link Long}.
 */
final class LocalTimeConverterLong extends LocalTimeConverter<Long> {

    /**
     * Singleton
     */
    final static LocalTimeConverterLong INSTANCE = new LocalTimeConverterLong();

    /**
     * Private ctor use singleton
     */
    private LocalTimeConverterLong() {
    }

    @Override
    Long convert2(final long seconds, final long nano, final LocalTime localTime) {
        if (0 != nano) {
            this.failConversion(localTime);
        }
        return Long.valueOf(seconds);
    }

    @Override
    Class<Long> targetType() {
        return Long.class;
    }
}