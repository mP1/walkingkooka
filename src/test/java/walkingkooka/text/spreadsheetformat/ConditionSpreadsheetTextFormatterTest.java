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
import walkingkooka.DecimalNumberContexts;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.text.cursor.parser.BigDecimalParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatConditionParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParsers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class ConditionSpreadsheetTextFormatterTest extends SpreadsheetTextFormatterTemplateTestCase<ConditionSpreadsheetTextFormatter<Object>,
        Object,
        SpreadsheetFormatConditionParserToken> {

    private final static String TEXT_PATTERN = "!@@";

    @Test(expected = NullPointerException.class)
    public void testWithNullConverterFails() {
        this.createFormatter(null, this.formatter());
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullWrappedFormatterFails() {
        this.createFormatter(this.converter(), null);
    }

    private ConditionSpreadsheetTextFormatter createFormatter(final Converter bigDecimalConverter,
                                                              final SpreadsheetTextFormatter<String> formatter) {
        return ConditionSpreadsheetTextFormatter.with(this.parsePatternOrFail(this.pattern()), bigDecimalConverter, formatter);
    }

    @Test
    public void testUnconvertableValueFails() {
        this.formatFailAndCheck(new Object());
    }

    @Test
    public void testConditionFailsFormatterSkipped() {
        this.formatFailAndCheck(new Object());
    }

    // EQ.....................................................................................

    @Test
    public void testFormattedEQ() {
        this.formatAndCheck2("[=50]", "50"); // pass
    }

    @Test
    public void testFormattedEQ2() {
        this.formatFailAndCheck2("[=50]", "99"); // fail
    }

    // GT.....................................................................................

    @Test
    public void testFormattedGT() {
        this.formatAndCheck2("[>9]", "50"); // 50 > 9 pass
    }

    @Test
    public void testFormattedGT2() {
        this.formatFailAndCheck2("[>9]", "5"); // 5 > 9 fail
    }

    @Test
    public void testFormattedGT3() {
        this.formatFailAndCheck2("[>9]", "9"); // 9 > 9 fail
    }

    // GTE.....................................................................................

    @Test
    public void testFormattedGTE() {
        this.formatAndCheck2("[>=9]", "50"); // 50 >= 9 pass
    }

    @Test
    public void testFormattedGTE2() {
        this.formatFailAndCheck2("[>=9]", "5"); // 5 >= 9 fail
    }

    @Test
    public void testFormattedGTE3() {
        this.formatAndCheck2("[>=9]", "9"); // 9 >= 9 pass
    }

    // LT.....................................................................................

    @Test
    public void testFormattedLT() {
        this.formatFailAndCheck2("[<9]", "50"); // 50 < 9 fail
    }

    @Test
    public void testFormattedLT2() {
        this.formatAndCheck2("[<9]", "5"); // 5 < 9 pass
    }

    @Test
    public void testFormattedLT3() {
        this.formatFailAndCheck2("[<9]", "9"); // 9 < 9 fail
    }

    // LTE.....................................................................................

    @Test
    public void testFormattedLTE() {
        this.formatFailAndCheck2("[<=9]", "50"); // 50 <= 9 fail
    }

    @Test
    public void testFormattedLTE2() {
        this.formatAndCheck2("[<=9]", "5"); // 5 <= 9 pass
    }

    @Test
    public void testFormattedLTE3() {
        this.formatAndCheck2("[<=9]", "9"); // 9 <= 9 pass
    }

    // NE.....................................................................................

    @Test
    public void testFormattedNE() {
        this.formatAndCheck2("[!=50]", "99"); // == pass
    }

    @Test
    public void testFormattedNE2() {
        this.formatFailAndCheck2("[!=50]", "50"); // == fail
    }

    // helpers.........................................................................

    private void formatAndCheck2(final String pattern, final String text) {
        this.formatAndCheck(this.createFormatter0(pattern), text, this.formattedText(text));
    }

    private void formatFailAndCheck2(final String pattern, final String text) {
        this.formatFailAndCheck(this.createFormatter0(pattern), text, this.createContext());
    }

    @Test
    public void testToString() {
        assertEquals(this.pattern() + " " + TEXT_PATTERN, this.createFormatter().toString());
    }

    private ConditionSpreadsheetTextFormatter createFormatter0(final String expression) {
        return this.createFormatter0(this.parsePatternOrFail(expression));
    }

    @Override
    ConditionSpreadsheetTextFormatter createFormatter0(final SpreadsheetFormatConditionParserToken token) {
        return ConditionSpreadsheetTextFormatter.with(token, this.converter(), this.formatter());
    }

    private Converter converter() {
        return Converters.parser(BigDecimal.class, Parsers.bigDecimal(MathContext.UNLIMITED), this::bigDecimalParserContext);
    }

    private ParserContext bigDecimalParserContext() {
        return ParserContexts.basic(DecimalNumberContexts.basic('.', 'E', '-', '+'));
    }

    private SpreadsheetTextFormatter<String> formatter() {
        return new SpreadsheetTextFormatter() {

            @Override
            public Optional<SpreadsheetFormattedText> format(final Object value, final SpreadsheetTextFormatContext context) {
                return Optional.of(SpreadsheetFormattedText.with(SpreadsheetFormattedText.WITHOUT_COLOR, formattedText(value)));
            }

            @Override
            public String toString() {
                return TEXT_PATTERN;
            }
        };
    }

    private String formattedText(final Object text) {
        return "!" + text + text;
    }

    @Override
    Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parser(final Parser<BigDecimalParserToken, SpreadsheetFormatParserContext> bigDecimal) {
        return SpreadsheetFormatParsers.condition(bigDecimal);
    }

    @Override
    String pattern() {
        return "[>20]";
    }

    @Override
    protected String value() {
        return "Text123";
    }

    @Override
    protected SpreadsheetTextFormatContext createContext() {
        return new FakeSpreadsheetTextFormatContext() {
            @Override
            public char decimalPoint() {
                return '.';
            }

            @Override
            public char exponentSymbol() {
                return 'E';
            }

            @Override
            public char minusSign() {
                return '-';
            }

            @Override
            public char plusSign() {
                return '+';
            }
        };
    }

    @Override
    protected Class<ConditionSpreadsheetTextFormatter<Object>> type() {
        return Cast.to(ConditionSpreadsheetTextFormatter.class);
    }
}
