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

public final class ConverterNumberNumberTest extends ConverterTestCase2<ConverterNumberNumber> {

    // to Number........................................................................................................

    @Test
    public void testBigDecimalToNumber() {
        this.convertToNumberAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigIntegerToNumber() {
        this.convertToNumberAndCheck(this.bigInteger());
    }

    @Test
    public void testByteToNumber() {
        this.convertToNumberAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToNumber() {
        this.convertToNumberAndCheck(this.doubleValue());
    }

    @Test
    public void testFloatToNumber() {
        this.convertToNumberAndCheck(this.floatValue());
    }

    @Test
    public void testIntegerToNumber() {
        this.convertToNumberAndCheck(this.integerValue());
    }

    @Test
    public void testLongToNumber() {
        this.convertToNumberAndCheck(this.longValue());
    }

    @Test
    public void testShortToNumber() {
        this.convertToNumberAndCheck(this.shortValue());
    }

    private void convertToNumberAndCheck(final Number number) {
        this.convertAndCheck(number, Number.class, number);
    }

    // to same type.....................................................................................................

    @Test
    public void testBigDecimalToBigDecimal() {
        this.convertToSameTypeAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigIntegerToBigInteger() {
        this.convertToSameTypeAndCheck(this.bigInteger());
    }

    @Test
    public void testByteToByte() {
        this.convertToSameTypeAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToDouble() {
        this.convertToSameTypeAndCheck(this.doubleValue());
    }

    @Test
    public void testFloatToFloat() {
        this.convertToSameTypeAndCheck(this.floatValue());
    }

    @Test
    public void testIntegerToInteger() {
        this.convertToSameTypeAndCheck(this.integerValue());
    }

    @Test
    public void testLongToLong() {
        this.convertToSameTypeAndCheck(this.longValue());
    }

    @Test
    public void testShortToShort() {
        this.convertToSameTypeAndCheck(this.shortValue());
    }

    private void convertToSameTypeAndCheck(final Number number) {
        this.convertAndCheck(number, number.getClass(), number);
    }

    // toBigDecimal.....................................................................................................

    @Test
    public void testBigIntegerToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.bigInteger());
    }

    @Test
    public void testByteToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.byteValue());
    }
    
    @Test
    public void testDoubleToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.doubleValue());
    }

    @Test
    public void testFloatToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.floatValue());
    }
    
    @Test
    public void testIntegerToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.integerValue());
    }
    
    @Test
    public void testLongToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.longValue());
    }

    @Test
    public void testShortToBigDecimal() {
        this.convertToBigDecimalAndCheck(this.shortValue());
    }
    
    private void convertToBigDecimalAndCheck(final Number number) {
        this.convertAndCheck(number, BigDecimal.class, this.bigDecimal());
    }

    // toBigInteger.....................................................................................................

    @Test
    public void testBigDecimalToBigInteger() {
        this.convertToBigIntegerAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigDecimalToBigIntegerFails() {
        this.convertToBigIntegerFails(BigDecimal.valueOf(1.5));
    }

    @Test
    public void testByteToBigInteger() {
        this.convertToBigIntegerAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToBigInteger() {
        this.convertToBigIntegerAndCheck(this.doubleValue());
    }

    @Test
    public void testDoubleToBigIntegerFails() {
        this.convertToBigIntegerFails(1.5);
    }

    @Test
    public void testFloatToBigInteger() {
        this.convertToBigIntegerAndCheck(this.floatValue());
    }

    @Test
    public void testFloatToBigIntegerFails() {
        this.convertToBigIntegerFails(1.5f);
    }

    @Test
    public void testIntegerToBigInteger() {
        this.convertToBigIntegerAndCheck(this.integerValue());
    }

    @Test
    public void testLongToBigInteger() {
        this.convertToBigIntegerAndCheck(this.longValue());
    }

    @Test
    public void testShortToBigInteger() {
        this.convertToBigIntegerAndCheck(this.shortValue());
    }

    private void convertToBigIntegerAndCheck(final Number number) {
        this.convertAndCheck(number, BigInteger.class, this.bigInteger());
    }

    private void convertToBigIntegerFails(final Number number) {
        this.convertFails(number, BigInteger.class);
    }
    
    // toByte...........................................................................................................

    @Test
    public void testBigDecimalToByte() {
        this.convertToByteAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigDecimalToByteFails() {
        this.convertToByteFails(this.bigDecimalDoubleMax2());
    }

    @Test
    public void testBigDecimalToByteFails2() {
        this.convertToByteFails(BigDecimal.valueOf(0.5));
    }

    @Test
    public void testBigIntegerToByte() {
        this.convertToByteAndCheck(this.bigInteger());
    }

    @Test
    public void testBigIntegerToByteFails() {
        this.convertToByteFails(this.bigIntegerLongMax2());
    }

    @Test
    public void testDoubleToByte() {
        this.convertToByteAndCheck(this.doubleValue());
    }

    @Test
    public void testDoubleToByteFails() {
        this.convertToByteFails(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleToByteFails2() {
        this.convertToByteFails(0.5);
    }

    @Test
    public void testFloatToByte() {
        this.convertToByteAndCheck(this.floatValue());
    }

    @Test
    public void testFloatToByteFails() {
        this.convertToByteFails(Float.MAX_VALUE);
    }

    @Test
    public void testFloatToByteFails2() {
        this.convertToByteFails(2.5f);
    }

    @Test
    public void testIntegerToByte() {
        this.convertToByteAndCheck(this.integerValue());
    }

    @Test
    public void testIntegerToByteFails() {
        this.convertToByteFails(Integer.MAX_VALUE);
    }

    @Test
    public void testLongToByte() {
        this.convertToByteAndCheck(this.longValue());
    }

    @Test
    public void testLongToByteFails() {
        this.convertToByteFails(Long.MAX_VALUE);
    }

    @Test
    public void testShortToByte() {
        this.convertToByteAndCheck(this.shortValue());
    }

    @Test
    public void testShortToByteFails() {
        this.convertToByteFails(Short.MAX_VALUE);
    }

    private void convertToByteAndCheck(final Number number) {
        this.convertAndCheck(number, Byte.class, this.byteValue());
    }

    private void convertToByteFails(final Number number) {
        this.convertFails(number, Byte.class);
    }

    // toDouble........................................................................................................

    @Test
    public void testBigDecimalToDouble() {
        this.convertToDoubleAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigIntegerToDouble() {
        this.convertToDoubleAndCheck(this.bigInteger());
    }

    @Test
    public void testByteToDouble() {
        this.convertToDoubleAndCheck(this.byteValue());
    }

    @Test
    public void testFloatToDouble() {
        this.convertToDoubleAndCheck(this.floatValue());
    }

    @Test
    public void testLongToDouble() {
        this.convertToDoubleAndCheck(this.longValue());
    }

//    @Test // Long -> Double -> Long should be lossy but isnt at runtime.
//    public void testLongToDoubleFails() {
//        this.convertToDoubleFails(Long.MAX_VALUE);
//    }

    @Test
    public void testShortToDouble() {
        this.convertToDoubleAndCheck(this.shortValue());
    }

    private void convertToDoubleAndCheck(final Number number) {
        this.convertAndCheck(number, Double.class, this.doubleValue());
    }

    private void convertToDoubleFails(final Number number) {
        this.convertFails(number, Double.class);
    }

    // toFloat..........................................................................................................

    @Test
    public void testBigDecimalToFloat() {
        this.convertToFloatAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigDecimalToFloatFails() {
        this.convertToFloatFails(this.bigDecimalDoubleMax2());
    }

    @Test
    public void testBigIntegerToFloat() {
        this.convertToFloatAndCheck(this.bigInteger());
    }

    @Test
    public void testBigIntegerToFloatFails() {
        this.convertToFloatFails(this.bigIntegerLongMax2());
    }

    @Test
    public void testByteToFloat() {
        this.convertToFloatAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToFloat() {
        this.convertToFloatAndCheck(this.doubleValue());
    }

    @Test
    public void testDoubleToFloatFails() {
        this.convertToFloatFails(Double.MAX_VALUE);
    }

    @Test
    public void testLongToFloat() {
        this.convertToFloatAndCheck(this.longValue());
    }

    @Test
    public void testShortToFloat() {
        this.convertToFloatAndCheck(this.shortValue());
    }

    private void convertToFloatAndCheck(final Number number) {
        this.convertAndCheck(number, Float.class, this.floatValue());
    }

    private void convertToFloatFails(final Number number) {
        this.convertFails(number, Float.class);
    }

    // toInteger........................................................................................................

    @Test
    public void testBigDecimalToInteger() {
        this.convertToIntegerAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigDecimalToIntegerFails() {
        this.convertToIntegerFails(this.bigDecimalDoubleMax2());
    }

    @Test
    public void testBigDecimalToIntegerFails2() {
        this.convertToIntegerFails(BigDecimal.valueOf(0.5));
    }

    @Test
    public void testBigIntegerToInteger() {
        this.convertToIntegerAndCheck(this.bigInteger());
    }

    @Test
    public void testBigIntegerToIntegerFails() {
        this.convertToIntegerFails(this.bigIntegerLongMax2());
    }

    @Test
    public void testByteToInteger() {
        this.convertToIntegerAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToInteger() {
        this.convertToIntegerAndCheck(this.doubleValue());
    }

    @Test
    public void testDoubleToIntegerFails() {
        this.convertToIntegerFails(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleToIntegerFails2() {
        this.convertToIntegerFails(0.5);
    }

    @Test
    public void testFloatToInteger() {
        this.convertToIntegerAndCheck(this.floatValue());
    }

    @Test
    public void testFloatToIntegerFails() {
        this.convertToIntegerFails(Float.MAX_VALUE);
    }

    @Test
    public void testFloatToIntegerFails2() {
        this.convertToIntegerFails(2.5f);
    }

    @Test
    public void testLongToInteger() {
        this.convertToIntegerAndCheck(this.longValue());
    }

    @Test
    public void testLongToIntegerFails() {
        this.convertToIntegerFails(Long.MAX_VALUE);
    }

    @Test
    public void testShortToInteger() {
        this.convertToIntegerAndCheck(this.shortValue());
    }

    private void convertToIntegerAndCheck(final Number number) {
        this.convertAndCheck(number, Integer.class, this.integerValue());
    }

    private void convertToIntegerFails(final Number number) {
        this.convertFails(number, Integer.class);
    }

    // toLong...........................................................................................................

    @Test
    public void testBigDecimalToLong() {
        this.convertToLongAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigDecimalToLongFails() {
        this.convertToLongFails(this.bigDecimalDoubleMax2());
    }

    @Test
    public void testBigDecimalToLongFails2() {
        this.convertToLongFails(BigDecimal.valueOf(0.5));
    }

    @Test
    public void testBigIntegerToLong() {
        this.convertToLongAndCheck(this.bigInteger());
    }

    @Test
    public void testBigIntegerToLongFails() {
        this.convertToLongFails(this.bigIntegerLongMax2());
    }

    @Test
    public void testByteToLong() {
        this.convertToLongAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToLong() {
        this.convertToLongAndCheck(this.doubleValue());
    }

    @Test
    public void testDoubleToLongFails() {
        this.convertToLongFails(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleToLongFails2() {
        this.convertToLongFails(0.5);
    }

    @Test
    public void testFloatToLong() {
        this.convertToLongAndCheck(this.floatValue());
    }

    @Test
    public void testFloatToLongFails() {
        this.convertToLongFails(Float.MAX_VALUE);
    }

    @Test
    public void testFloatToLongFails2() {
        this.convertToLongFails(2.5f);
    }

    @Test
    public void testIntegerToLong() {
        this.convertToLongAndCheck(this.integerValue());
    }

    @Test
    public void testShortToLong() {
        this.convertToLongAndCheck(this.shortValue());
    }

    private void convertToLongAndCheck(final Number number) {
        this.convertAndCheck(number, Long.class, this.longValue());
    }

    private void convertToLongFails(final Number number) {
        this.convertFails(number, Long.class);
    }

    // toShort..........................................................................................................

    @Test
    public void testBigDecimalToShort() {
        this.convertToShortAndCheck(this.bigDecimal());
    }

    @Test
    public void testBigDecimalToShortFails() {
        this.convertToShortFails(this.bigDecimalDoubleMax2());
    }

    @Test
    public void testBigDecimalToShortFails2() {
        this.convertToShortFails(BigDecimal.valueOf(0.5));
    }

    @Test
    public void testBigIntegerToShort() {
        this.convertToShortAndCheck(this.bigInteger());
    }

    @Test
    public void testBigIntegerToShortFails() {
        this.convertToShortFails(this.bigIntegerLongMax2());
    }

    @Test
    public void testByteToShort() {
        this.convertToShortAndCheck(this.byteValue());
    }

    @Test
    public void testDoubleToShort() {
        this.convertToShortAndCheck(this.doubleValue());
    }

    @Test
    public void testDoubleToShortFails() {
        this.convertToShortFails(Double.MAX_VALUE);
    }

    @Test
    public void testDoubleToShortFails2() {
        this.convertToShortFails(0.5);
    }

    @Test
    public void testFloatToShort() {
        this.convertToShortAndCheck(this.floatValue());
    }

    @Test
    public void testFloatToShortFails() {
        this.convertToShortFails(Float.MAX_VALUE);
    }

    @Test
    public void testFloatToShortFails2() {
        this.convertToShortFails(2.5f);
    }

    @Test
    public void testIntegerToShort() {
        this.convertToShortAndCheck(this.integerValue());
    }

    @Test
    public void testIntegerToShortFails() {
        this.convertToShortFails(Integer.MAX_VALUE);
    }

    @Test
    public void testLongToShort() {
        this.convertToShortAndCheck(this.longValue());
    }

    @Test
    public void testLongToShortFails() {
        this.convertToShortFails(Long.MAX_VALUE);
    }

    private void convertToShortAndCheck(final Number number) {
        this.convertAndCheck(number, Short.class, this.shortValue());
    }

    private void convertToShortFails(final Number number) {
        this.convertFails(number, Short.class);
    }

    // helper............................................................................................................

    @Override
    public ConverterNumberNumber createConverter() {
        return ConverterNumberNumber.INSTANCE;
    }

    private final static Byte VALUE = 123;

    private BigDecimal bigDecimal() {
        return BigDecimal.valueOf(VALUE);
    }

    private BigDecimal bigDecimalDoubleMax2() {
        return BigDecimal.valueOf(Double.MAX_VALUE).multiply(BigDecimal.valueOf(Double.MAX_VALUE));
    }

    private BigInteger bigInteger() {
        return BigInteger.valueOf(VALUE);
    }

    private BigInteger bigIntegerLongMax2() {
        return BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(Long.MAX_VALUE));
    }

    private Byte byteValue() {
        return VALUE.byteValue();
    }

    private Double doubleValue() {
        return VALUE.doubleValue();
    }

    private Float floatValue() {
        return VALUE.floatValue();
    }

    private Integer integerValue() {
        return VALUE.intValue();
    }

    private Long longValue() {
        return VALUE.longValue();
    }

    private Short shortValue() {
        return VALUE.shortValue();
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterNumberNumber> type() {
        return ConverterNumberNumber.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return Converter.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return Number.class.getSimpleName() + Number.class.getSimpleName();
    }
}
