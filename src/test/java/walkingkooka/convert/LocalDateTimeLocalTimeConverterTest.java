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

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class LocalDateTimeLocalTimeConverterTest extends FixedTypeConverterTestCase<LocalDateTimeLocalTimeConverter, LocalTime> {

    @Test
    public void testConvert() {
        final LocalTime time = LocalTime.of(12, 59);
        this.convertAndCheck(LocalDateTime.of(LocalDate.of(2000, 1, 31), time), time);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalDateTime->LocalTime");
    }

    @Override
    protected LocalDateTimeLocalTimeConverter createConverter() {
        return LocalDateTimeLocalTimeConverter.INSTANCE;
    }

    @Override
    protected Class<LocalTime> onlySupportedType() {
        return LocalTime.class;
    }

    @Override
    protected ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    public Class<LocalDateTimeLocalTimeConverter> type() {
        return LocalDateTimeLocalTimeConverter.class;
    }
}
