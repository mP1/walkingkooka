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

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public final class LocalDateTimeConverterLongTest extends LocalDateTimeConverterTestCase<LocalDateTimeConverterLong, Long> {

    @Test
    public void testLocalDateTimeNonMidnightTimeFails() {
        this.convertFails(LocalDateTime.of(DAY, QUARTER_DAY));
    }

    @Override
    LocalDateTimeConverterLong createConverter(final long offset) {
        return LocalDateTimeConverterLong.with(offset);
    }

    @Override
    protected Class<Long> onlySupportedType() {
        return Long.class;
    }

    @Override
    final Long value(final long value) {
        return Long.valueOf(value);
    }

    @Override
    public Class<LocalDateTimeConverterLong> type() {
        return LocalDateTimeConverterLong.class;
    }
}