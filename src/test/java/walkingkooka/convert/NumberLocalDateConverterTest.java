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

public final class NumberLocalDateConverterTest extends NumberConverterTestCase<NumberLocalDateConverter, LocalDate> {

    private final static int VALUE = 123;

    @Test
    public void testNonNumberTypeFails() {
        this.convertFails("fail!");
    }

    @Test
    public void testFromLocalDateFails() {
        this.convertFails(LocalDate.ofEpochDay(VALUE));
    }

    @Test
    public void testBigDecimal() {
        this.convertAndCheck(BigDecimal.valueOf(VALUE), LocalDate.ofEpochDay(VALUE));
    }

    @Test
    public void testBigDecimalWithFraction() {
        this.convertFails(BigDecimal.valueOf(123.5));
    }

    @Test
    public void testBigInteger() {
        this.convertAndCheck(BigInteger.valueOf(123), LocalDate.ofEpochDay(VALUE));
    }

    @Test
    public void testDouble() {
        this.convertAndCheck(Double.valueOf(VALUE), LocalDate.ofEpochDay(VALUE));
    }

    @Test
    public void testDoubleWithFraction() {
        this.convertFails(Double.valueOf(123.75));
    }

    @Test
    @Ignore
    public void testDoubleMaxFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testLong() {
        this.convertAndCheck(Long.valueOf(VALUE), LocalDate.ofEpochDay(VALUE));
    }

    @Override
    protected NumberLocalDateConverter createConverter() {
        return NumberLocalDateConverter.INSTANCE;
    }

    @Override
    protected Class<LocalDate> onlySupportedType() {
        return LocalDate.class;
    }

    @Override
    protected Class<NumberLocalDateConverter> type() {
        return NumberLocalDateConverter.class;
    }
}
