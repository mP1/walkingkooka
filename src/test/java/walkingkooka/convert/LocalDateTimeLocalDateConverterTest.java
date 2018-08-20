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

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public final class LocalDateTimeLocalDateConverterTest extends FixedTypeConverterTestCase<LocalDateTimeLocalDateConverter, LocalDate> {

    @Test
    public void testConvert() {
        final LocalDate date = LocalDate.of(2000, 1, 31);
        this.convertAndCheck(LocalDateTime.of(date, LocalTime.of(12, 59)), date);
    }

    @Test
    public void testToString() {
        assertEquals("LocalDateTime->LocalDate", this.createConverter().toString());
    }

    @Override
    protected LocalDateTimeLocalDateConverter createConverter() {
        return LocalDateTimeLocalDateConverter.INSTANCE;
    }

    @Override
    protected Class<LocalDate> onlySupportedType() {
        return LocalDate.class;
    }

    @Override
    protected Class<LocalDateTimeLocalDateConverter> type() {
        return LocalDateTimeLocalDateConverter.class;
    }
}
