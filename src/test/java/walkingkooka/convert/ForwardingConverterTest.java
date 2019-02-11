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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ForwardingConverterTest extends FixedTypeConverterTestCase<ForwardingConverter<Number, BigDecimal>, Number> {

    private final static Class<Number> SOURCE_TYPE = Number.class;
    private final static Class<BigDecimal> TARGET_TYPE = BigDecimal.class;

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> {
            ForwardingConverter.with(null,
                    SOURCE_TYPE,
                    TARGET_TYPE);
        });
    }

    @Test
    public void testWithNullSourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            ForwardingConverter.with(Converters.localDateBigDecimal(Converters.JAVA_EPOCH_OFFSET),
                    null,
                    TARGET_TYPE);
        });
    }

    @Test
    public void testWithNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            ForwardingConverter.with(Converters.localDateBigDecimal(Converters.JAVA_EPOCH_OFFSET),
                    SOURCE_TYPE,
                    null);

        });
    }

    @Test
    public void testWrongTypeFails() {
        this.convertFails("string-must-fail!");
    }

    @Test
    public void testWrongTypeFails2() {
        this.convertFails(LocalDate.now(), BigDecimal.class);
    }

    @Test
    public void testConverted() {
        final int value = 123;
        this.convertAndCheck(LocalDate.ofEpochDay(value), Number.class, BigDecimal.valueOf(123));
    }

    @Test
    public void testToString(){
        this.toStringAndCheck(this.createConverter(), "LocalDate->Number");
    }

    @Override public ForwardingConverter<Number, BigDecimal> createConverter() {
        return ForwardingConverter.with(Converters.localDateBigDecimal(Converters.JAVA_EPOCH_OFFSET),
                SOURCE_TYPE,
                TARGET_TYPE);
    }

    @Override public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    protected Class<Number> onlySupportedType() {
        return Number.class;
    }

    @Override
    public Class<ForwardingConverter<Number, BigDecimal>> type() {
        return Cast.to(ForwardingConverter.class);
    }
}
