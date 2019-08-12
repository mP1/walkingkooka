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

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class LocalDateTimeConverterNumberTest extends LocalDateTimeConverterTestCase<LocalDateTimeConverterNumber, Number> {

    @Test
    public void testLocalDateTimeWithNonMidnightTime() {
        this.convertAndCheck(LocalDateTime.of(DAY, QUARTER_DAY), BigDecimal.valueOf(VALUE + 0.25));
    }

    @Test
    public void testConverterRoundTripWithNonMidnightTime() {
        final LocalDateTime localDateTime = LocalDateTime.of(DAY, QUARTER_DAY);
        final Number bigDecimal = this.convertAndCheck(localDateTime, BigDecimal.valueOf(VALUE + 0.25));
        this.convertAndCheck(Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET),
                bigDecimal,
                LocalDateTime.class,
                localDateTime);
    }

    @Override
    final LocalDateTimeConverterNumber createConverter(final long offset) {
        return LocalDateTimeConverterNumber.with(offset);
    }

    @Override
    protected Class<Number> onlySupportedType() {
        return Number.class;
    }

    @Override
    final BigDecimal value(final long value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    public Class<LocalDateTimeConverterNumber> type() {
        return LocalDateTimeConverterNumber.class;
    }
}