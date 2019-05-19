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
import java.time.LocalTime;

public final class LocalTimeBigDecimalConverterTest extends LocalTimeConverterTestCase<LocalTimeBigDecimalConverter, BigDecimal> {

    private final static int VALUE = 123;

    @Test
    public void testLocalTime() {
        this.convertAndCheck(LocalTime.ofSecondOfDay(VALUE), BigDecimal.valueOf(VALUE));
    }

    @Test
    public void testLocalTimeWithNanos() {
        this.convertAndCheck(LocalTime.ofSecondOfDay(VALUE).plusNanos(Converters.NANOS_PER_SECOND / 2), BigDecimal.valueOf(VALUE + 0.5));
    }

    @Test
    public void testConverterRoundTrip() {
        final LocalTime localTime = LocalTime.ofSecondOfDay(VALUE);
        final BigDecimal bigDecimal = Cast.to(this.convertAndCheck(localTime, BigDecimal.valueOf(VALUE)));
        this.convertAndCheck(Converters.numberLocalTime(), bigDecimal, LocalTime.class, localTime);
    }

    @Test
    public void testConverterRoundTripWithNanos() {
        final LocalTime localTime = LocalTime.ofSecondOfDay(VALUE)
                .plusNanos(Converters.NANOS_PER_SECOND / 2);
        final BigDecimal bigDecimal = Cast.to(this.convertAndCheck(localTime, BigDecimal.valueOf(VALUE + 0.5)));
        this.convertAndCheck(Converters.numberLocalTime(), bigDecimal, LocalTime.class, localTime);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalTime->BigDecimal");
    }

    @Override
    public LocalTimeBigDecimalConverter createConverter() {
        return LocalTimeBigDecimalConverter.INSTANCE;
    }

    @Override
    protected Class<BigDecimal> onlySupportedType() {
        return BigDecimal.class;
    }

    @Override
    public Class<LocalTimeBigDecimalConverter> type() {
        return LocalTimeBigDecimalConverter.class;
    }
}
