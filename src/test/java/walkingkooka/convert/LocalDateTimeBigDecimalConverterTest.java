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
import walkingkooka.Cast;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class LocalDateTimeBigDecimalConverterTest extends LocalDateTimeConverterTestCase2<LocalDateTimeBigDecimalConverter, BigDecimal> {

    @Test
    public void testLocalDateTimeWithNonMidnightTime() {
        this.convertAndCheck(LocalDateTime.of(DAY, QUARTER_DAY), BigDecimal.valueOf(VALUE + 0.25));
    }

    @Test
    public void testConverterRoundTripWithNonMidnightTime() {
        final LocalDateTime localDateTime = LocalDateTime.of(DAY, QUARTER_DAY);
        final BigDecimal bigDecimal = Cast.to(this.convertAndCheck(localDateTime, BigDecimal.valueOf(VALUE + 0.25)));
        this.convertAndCheck(Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET),
                bigDecimal,
                LocalDateTime.class,
                localDateTime);
    }

    @Override
    final LocalDateTimeBigDecimalConverter createConverter(final long offset) {
        return LocalDateTimeBigDecimalConverter.with(offset);
    }

    @Override
    protected Class<BigDecimal> onlySupportedType() {
        return BigDecimal.class;
    }

    @Override
    final BigDecimal value(final long value) {
        return BigDecimal.valueOf(value);
    }

    @Override
    protected Class<LocalDateTimeBigDecimalConverter> type() {
        return LocalDateTimeBigDecimalConverter.class;
    }
}
