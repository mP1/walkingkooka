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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.Parsers;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;

public final class ParserConverterTest extends FixedTypeConverterTestCase<ParserConverter<BigDecimal, BigDecimalParserToken, ParserContext>, BigDecimal> {

    @Test(expected = NullPointerException.class)
    public void testWithNullTypeFails() {
        ParserConverter.with(null, Parsers.bigDecimal('.', MathContext.DECIMAL32), FakeParserContext::new);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParserFails() {
        ParserConverter.with(BigDecimal.class, null, FakeParserContext::new);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullParserContextSupplierFails() {
        ParserConverter.with(BigDecimal.class, Parsers.bigDecimal('.', MathContext.DECIMAL32), null);
    }

    @Test
    public void testParserSuceeds() {
        this.convertAndCheck("1.23", BigDecimal.valueOf(1.23));
    }

    @Test
    public void testParserFails() {
        this.convertFails("FAILS");
    }

    @Test
    public void testToString() {
        assertEquals("String->BigDecimal", this.createConverter().toString());
    }

    @Override
    protected ParserConverter<BigDecimal, BigDecimalParserToken, ParserContext> createConverter() {
        return ParserConverter.with(BigDecimal.class,
                Parsers.bigDecimal('.', MathContext.DECIMAL32),
                FakeParserContext::new);
    }

    @Override
    protected Class<BigDecimal> onlySupportedType() {
        return BigDecimal.class;
    }

    @Override
    protected Class<ParserConverter<BigDecimal, BigDecimalParserToken, ParserContext>> type() {
        return Cast.to(ParserConverter.class);
    }
}
