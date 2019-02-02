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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class LocalDateTimeConverterTestCase2<C extends FixedSourceTypeTargetTypeConverter<LocalDateTime, T>, T> extends FixedTypeConverterTestCase<C, T> {

    final static int VALUE = 123;
    final static LocalDate DAY = LocalDate.ofEpochDay(VALUE);
    final static LocalTime MIDNIGHT = LocalTime.ofSecondOfDay(0);
    final static LocalTime QUARTER_DAY = LocalTime.of(6, 0);

    @Test
    public final void testLocalDateTime() {
        this.convertAndCheck(LocalDateTime.of(DAY, MIDNIGHT), this.value(VALUE));
    }

    public final void testLocalDateTimeExcelOffset() {
        this.convertAndCheck(this.createConverter(Converters.EXCEL_OFFSET),
                LocalDateTime.of(DAY, MIDNIGHT),
                LocalDateTime.class,
                this.value(VALUE));
    }

    public final void testLocalDateTimeExcelOffset2() {
        final int value = 123;
        this.convertAndCheck(this.createConverter(value + Converters.EXCEL_OFFSET),
                LocalDateTime.of(DAY, MIDNIGHT),
                LocalDateTime.class,
                this.value(value));
    }

    @Test
    public final void testConverterRoundTrip() {
        final LocalDateTime localDateTime = LocalDateTime.of(DAY, MIDNIGHT);
        final Object value = Cast.to(this.convertAndCheck(localDateTime, this.value(VALUE)));
        this.convertAndCheck(Converters.numberLocalDateTime(Converters.JAVA_EPOCH_OFFSET),
                value,
                LocalDateTime.class,
                localDateTime);
    }

    @Test
    public final void testToString() {
        assertEquals(this.defaultToString(), this.createConverter().toString());
    }

    @Test
    public final void testToStringNegativeOffset() {
        assertEquals(this.defaultToString() + "(-123)", this.createConverter(-123).toString());
    }

    @Test
    public final void testToStringPositiveOffset() {
        assertEquals(this.defaultToString() + "(+123)", this.createConverter(+123).toString());
    }

    private String defaultToString() {
        return "LocalDateTime->" + this.onlySupportedType().getSimpleName();
    }

    @Override
    final protected C createConverter() {
        return this.createConverter(Converters.JAVA_EPOCH_OFFSET);
    }

    abstract C createConverter(final long offset);

    @Override
    protected final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    abstract T value(final long value);
}
