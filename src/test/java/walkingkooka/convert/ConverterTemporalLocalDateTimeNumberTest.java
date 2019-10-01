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
import walkingkooka.Cast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ConverterTemporalLocalDateTimeNumberTest extends ConverterTemporalLocalDateTimeTestCase<ConverterTemporalLocalDateTimeNumber, Number> {

    // fail(overflow)....................................................................................................

    @Test
    public void testToByteOverflowFails() {
        this.convertFails3(Byte.class);
    }

    @Test
    public void testToShortOverflowFails() {
        this.convertFails3(Byte.class);
    }

    private void convertFails3(final Class<? extends Number> type) {
        this.convertFails(LocalDateTime.MAX, type);
    }

    // pass.............................................................................................................

    private final static byte BYTE_VALUE = 12;

    @Test
    public void testToBigDecimal() {
        this.convertAndCheck3(BigDecimal.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToBigDecimalQuarter() {
        this.convertAndCheck4(BigDecimal.valueOf(BYTE_VALUE).add(BigDecimal.valueOf(0.25)));
    }

    @Test
    public void testToBigInteger() {
        this.convertAndCheck3(BigInteger.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToByte() {
        this.convertAndCheck3(BYTE_VALUE);
    }

    @Test
    public void testToShort() {
        this.convertAndCheck3(Short.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToInteger() {
        this.convertAndCheck3(Integer.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToLong() {
        this.convertAndCheck3(Long.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToFloat() {
        this.convertAndCheck3(Float.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToFloatQuarter() {
        this.convertAndCheck4(Float.valueOf(BYTE_VALUE + 0.25f));
    }

    @Test
    public void testToDouble() {
        this.convertAndCheck3(Double.valueOf(BYTE_VALUE));
    }

    @Test
    public void testToDoubleQuarter() {
        this.convertAndCheck4(Double.valueOf(BYTE_VALUE + 0.25));
    }

    private void convertAndCheck3(final Number expected) {
        this.convertAndCheck2(LocalDateTime.of(LocalDate.ofEpochDay(BYTE_VALUE), LocalTime.MIDNIGHT),
                expected);
    }

    private void convertAndCheck4(final Number expected) {
        this.convertAndCheck2(LocalDateTime.of(LocalDate.ofEpochDay(BYTE_VALUE), LocalTime.of(6, 0)),
                expected);
    }

    @Test
    public void testToNumber() {
        this.convertAndCheck(LocalDateTime.of(LocalDate.ofEpochDay(BYTE_VALUE), LocalTime.MIDNIGHT),
                Number.class,
                Double.valueOf(BYTE_VALUE));
    }

    // withOffset.......................................................................................................

    private final static int OFFSET = 100;

    @Test
    public void testWithOffsetToBigDecimal() {
        this.convertWithOffsetAndCheck3(BigDecimal.valueOf(OFFSET + BYTE_VALUE));
    }

    @Test
    public void testWithOffsetToBigInteger() {
        this.convertWithOffsetAndCheck3(BigInteger.valueOf(OFFSET + BYTE_VALUE));
    }

    @Test
    public void testWithOffsetToByte() {
        this.convertWithOffsetAndCheck3((byte) (OFFSET + BYTE_VALUE));
    }

    @Test
    public void testWithOffsetToShort() {
        this.convertWithOffsetAndCheck3(Short.valueOf((byte) (OFFSET + BYTE_VALUE)));
    }

    @Test
    public void testWithOffsetToInteger() {
        this.convertWithOffsetAndCheck3(Integer.valueOf(OFFSET + BYTE_VALUE));
    }

    @Test
    public void testWithOffsetToLong() {
        this.convertWithOffsetAndCheck3(Long.valueOf(OFFSET + BYTE_VALUE));
    }

    @Test
    public void testWithOffsetToFloat() {
        this.convertWithOffsetAndCheck3(Float.valueOf(OFFSET + BYTE_VALUE));
    }

    @Test
    public void testWithOffsetToDouble() {
        this.convertWithOffsetAndCheck3(Double.valueOf(OFFSET + BYTE_VALUE));
    }

    private void convertWithOffsetAndCheck3(final Number expected) {
        this.convertAndCheck(walkingkooka.convert.ConverterTemporalLocalDateTimeNumber.with(OFFSET),
                LocalDateTime.of(LocalDate.ofEpochDay(BYTE_VALUE), LocalTime.MIDNIGHT),
                Cast.to(expected.getClass()),
                expected);
    }

    @Test
    public void testWithOffsetToNumber() {
        this.convertAndCheck(walkingkooka.convert.ConverterTemporalLocalDateTimeNumber.with(OFFSET),
                LocalDateTime.of(LocalDate.ofEpochDay(BYTE_VALUE), LocalTime.MIDNIGHT),
                Number.class,
                Double.valueOf(BYTE_VALUE + OFFSET));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalDateTime->Number");
    }

    // ConverterTesting.................................................................................................

    @Override
    public ConverterTemporalLocalDateTimeNumber createConverter() {
        return walkingkooka.convert.ConverterTemporalLocalDateTimeNumber.with(Converters.JAVA_EPOCH_OFFSET);
    }

    @Override
    Class<Number> targetType() {
        return Number.class;
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterTemporalLocalDateTimeNumber> type() {
        return ConverterTemporalLocalDateTimeNumber.class;
    }
}
