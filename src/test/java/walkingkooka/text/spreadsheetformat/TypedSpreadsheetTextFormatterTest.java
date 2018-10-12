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

package walkingkooka.text.spreadsheetformat;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatTextPlaceholderParserToken;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.Assert.assertEquals;

public final class TypedSpreadsheetTextFormatterTest extends SpreadsheetTextFormatterTestCase<TypedSpreadsheetTextFormatter<Object>, Object> {

    @Test(expected = NullPointerException.class)
    public void testWithNullTypeFails() {
        TypedSpreadsheetTextFormatter.with(null, this.bigDecimalFormatter(), this.textFormatter());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullFormatterFails() {
        TypedSpreadsheetTextFormatter.with(this.firstParameter(), null, this.textFormatter());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullFormatter2Fails() {
        TypedSpreadsheetTextFormatter.with(this.firstParameter(), this.bigDecimalFormatter(), null);
    }

    @Test
    public void testFormatBigDecimal() {
        this.formatAndCheck(BigDecimal.valueOf(123), "++123");
    }

    @Test
    public void testFormatString() {
        final String text = "abc123";
        this.formatAndCheck(text, text + text);
    }

    @Override
    public void testToString() {
        assertEquals(this.bigDecimalFormatter() + " | " + this.textFormatter(), this.createFormatter().toString());
    }

    @Override
    protected TypedSpreadsheetTextFormatter createFormatter() {
        return TypedSpreadsheetTextFormatter.with(this.firstParameter(), this.bigDecimalFormatter(), this.textFormatter());
    }

    private Class<BigDecimal> firstParameter() {
        return BigDecimal.class;
    }

    private SpreadsheetTextFormatter<BigDecimal> bigDecimalFormatter() {
        return SpreadsheetTextFormatters.bigDecimal(SpreadsheetFormatParserToken.bigDecimal(Lists.of(SpreadsheetFormatParserToken.digit("#", "#")), "#"), MathContext.UNLIMITED);
    }

    private SpreadsheetTextFormatter<String> textFormatter() {
        return SpreadsheetTextFormatters.text(SpreadsheetFormatParserToken.text(Lists.of(textPlaceholder(), textPlaceholder()), "@@"));
    }

    private SpreadsheetFormatTextPlaceholderParserToken textPlaceholder() {
        return SpreadsheetFormatParserToken.textPlaceholder("@", "@");
    }

    @Override
    protected Object value() {
        return BigDecimal.valueOf(1.5);
    }

    @Override
    protected SpreadsheetTextFormatContext createContext() {
        return new FakeSpreadsheetTextFormatContext() {
            @Override
            public String signSymbol(final SpreadsheetTextFormatContextSign sign) {
                return "++";
            }
        };
    }

    @Override
    protected Class<TypedSpreadsheetTextFormatter<Object>> type() {
        return Cast.to(TypedSpreadsheetTextFormatter.class);
    }
}
