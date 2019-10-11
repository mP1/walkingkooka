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
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ChainConverterTest extends ConverterTestCase2<ChainConverter> {

    @Test
    public void testWithNullFirstConverterFails() {
        assertThrows(NullPointerException.class, () -> ChainConverter.with(null, this.intermediateTargetType(), this.doubleToBigDecimal()));
    }

    @Test
    public void testWithNullIntermediateTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> ChainConverter.with(this.stringToDouble(), null, this.doubleToBigDecimal()));
    }

    @Test
    public void testWithNullLastConverterFails() {
        assertThrows(NullPointerException.class, () -> ChainConverter.with(this.stringToDouble(), this.intermediateTargetType(), null));
    }

    @Test
    public void testFirstFail() {
        this.convertFails("abc", BigDecimal.class);
    }

    public void testIncompatibleTargetTypeFails() {
        this.convertFails("abc", BigInteger.class);
    }

    @Test
    public void testSuccessful() {
        this.convertAndCheck("123.5", BigDecimal.class, BigDecimal.valueOf(123.5));
    }

    @Test
    public void testThenSuccessful() {
        this.convertAndCheck(this.stringToDouble().then(this.intermediateTargetType(), this.doubleToBigDecimal()),
                "123.5",
                BigDecimal.class,
                BigDecimal.valueOf(123.5));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(), this.stringToDouble() + "->" + this.doubleToBigDecimal());
    }

    @Override
    public ChainConverter createConverter() {
        return ChainConverter.with(this.stringToDouble(), this.intermediateTargetType(), this.doubleToBigDecimal());
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.basic(DateTimeContexts.fake(), DecimalNumberContexts.american(MathContext.DECIMAL32));
    }

    private Converter stringToDouble() {
        return Converters.parser(Double.class, Parsers.doubleParser(), (c) -> ParserContexts.basic(c, c));
    }

    private Class<?> intermediateTargetType() {
        return Double.class;
    }

    private Converter doubleToBigDecimal() {
        return Converters.numberNumber();
    }

    @Override
    public Class<ChainConverter> type() {
        return ChainConverter.class;
    }
}
