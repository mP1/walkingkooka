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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class LocalDateTimeConverterLocalDateTest extends FixedTypeConverterTestCase<LocalDateTimeConverterLocalDate, LocalDate> {

    @Test
    public void testConvert() {
        final LocalDate date = LocalDate.of(2000, 1, 31);
        this.convertAndCheck(LocalDateTime.of(date, LocalTime.of(12, 59)), date);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalDateTime->LocalDate");
    }

    @Override
    public LocalDateTimeConverterLocalDate createConverter() {
        return LocalDateTimeConverterLocalDate.INSTANCE;
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    protected Class<LocalDate> onlySupportedType() {
        return LocalDate.class;
    }

    @Override
    public Class<LocalDateTimeConverterLocalDate> type() {
        return LocalDateTimeConverterLocalDate.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return LocalDateTime.class.getSimpleName() + Converter.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return this.onlySupportedType().getSimpleName();
    }
}
