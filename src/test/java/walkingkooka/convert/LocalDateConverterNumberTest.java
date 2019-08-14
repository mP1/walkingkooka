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
import java.math.BigInteger;
import java.time.LocalDate;

public final class LocalDateConverterNumberTest extends LocalDateConverterTestCase<LocalDateConverterNumber> {

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
        this.convertFails(LocalDate.MAX, type);
    }

    // pass.............................................................................................................

    private final byte BYTE_VALUE = 123;

    @Test
    public void testToBigDecimal() {
        this.convertAndCheck3(BigDecimal.valueOf(BYTE_VALUE));
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
    public void testToDouble() {
        this.convertAndCheck3(Double.valueOf(BYTE_VALUE));
    }

    private void convertAndCheck3(final Number expected) {
        this.convertAndCheck2(LocalDate.ofEpochDay(BYTE_VALUE), expected);
    }

    @Test
    public void testToNumber() {
        this.convertAndCheck(LocalDate.ofEpochDay(BYTE_VALUE), Number.class, Long.valueOf(BYTE_VALUE));
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), "LocalDate->Number");
    }

    // ConverterTesting.................................................................................................

    @Override
    public LocalDateConverterNumber createConverter() {
        return LocalDateConverterNumber.with(Converters.JAVA_EPOCH_OFFSET);
    }

    @Override
    public Class<LocalDateConverterNumber> type() {
        return LocalDateConverterNumber.class;
    }
}
