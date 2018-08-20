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

import org.junit.Ignore;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class NumberLocalDateTimeConverterTest extends NumberConverterTestCase<NumberLocalDateTimeConverter, LocalDateTime> {

    private final static int VALUE = 123;
    private final static LocalTime MIDNIGHT = LocalTime.ofSecondOfDay(0);

    @Test
    public void testNonNumberTypeFails() {
        this.convertFails("fail!");
    }

    @Test
    public void testFromLocalDateTimeFails() {
        this.convertFails(LocalDateTime.of(1, 2, 3, 4, 5));
    }

    @Test
    public void testBigDecimal() {
        this.convertAndCheck(BigDecimal.valueOf(123), this.localDateTime(VALUE, MIDNIGHT));
    }

    @Test
    public void testBigDecimalWithFraction() {
        this.convertAndCheck(BigDecimal.valueOf(123.5), this.localDateTime(VALUE, 12, 0));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck(BigInteger.valueOf(VALUE), this.localDateTime(VALUE, MIDNIGHT));
    }

    @Test
    public void testDouble() {
        this.convertAndCheck(BigDecimal.valueOf(123.0), this.localDateTime(VALUE, MIDNIGHT));
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertAndCheck(BigDecimal.valueOf(123.5), this.localDateTime(VALUE, 12, 0));
    }

    @Test
    @Ignore
    public void testDoubleMaxFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testLong() {
        this.convertAndCheck(Long.valueOf(VALUE), this.localDateTime(123, MIDNIGHT));
    }

    @Override
    protected NumberLocalDateTimeConverter createConverter() {
        return NumberLocalDateTimeConverter.INSTANCE;
    }

    private LocalDateTime localDateTime(final int date, final int hours, final int minutes) {
        return this.localDateTime(date, LocalTime.of(hours, minutes));
    }

    private LocalDateTime localDateTime(final int date, final LocalTime time) {
        return this.localDateTime(LocalDate.ofEpochDay(date), time);
    }

    private LocalDateTime localDateTime(final LocalDate date, final LocalTime time) {
        return LocalDateTime.of(date, time);
    }

    @Override
    protected Class<LocalDateTime> onlySupportedType() {
        return LocalDateTime.class;
    }

    @Override
    protected Class<NumberLocalDateTimeConverter> type() {
        return NumberLocalDateTimeConverter.class;
    }
}
