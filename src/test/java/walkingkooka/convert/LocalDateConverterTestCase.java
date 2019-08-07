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

import java.time.LocalDate;

public abstract class LocalDateConverterTestCase<C extends FixedSourceTypeTargetTypeConverter<LocalDate, T>, T> extends FixedTypeConverterTestCase<C, T> {

    LocalDateConverterTestCase() {
        super();
    }

    @Test
    public final void testLocalDateZeroOffset() {
        final int value = 123;
        this.convertAndCheck(LocalDate.ofEpochDay(value), this.value(value));
    }

    @Test
    public final void testLocalDateExcelOffset() {
        final int value = 0;
        this.convertAndCheck(this.createConverter(Converters.EXCEL_OFFSET),
                LocalDate.ofEpochDay(value - Converters.EXCEL_OFFSET),
                this.onlySupportedType(),
                this.value(value));
    }

    @Test
    public final void testLocalDateExcelOffset2() {
        final int value = 123;
        this.convertAndCheck(this.createConverter(Converters.EXCEL_OFFSET),
                LocalDate.ofEpochDay(-Converters.EXCEL_OFFSET + value),
                this.onlySupportedType(),
                this.value(value));
    }

    @Test
    public final void testToString() {
        this.toStringAndCheck(this.defaultToString(), this.createConverter().toString());
    }

    @Test
    public final void testToStringWithPositiveOffset() {
        this.toStringAndCheck(this.createConverter(123), this.defaultToString() + "(+123)");
    }

    @Test
    public final void testToStringWithNegativeOffset() {
        this.toStringAndCheck(this.createConverter(-123), this.defaultToString() + "(-123)");
    }

    private String defaultToString() {
        return "LocalDate->" + this.onlySupportedType().getSimpleName();
    }

    @Override
    public final C createConverter() {
        return this.createConverter(Converters.JAVA_EPOCH_OFFSET);
    }

    abstract C createConverter(final long offset);

    @Override
    public final ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    abstract T value(long value);

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return LocalDate.class.getSimpleName() + Converter.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return this.onlySupportedType().getSimpleName();
    }
}
