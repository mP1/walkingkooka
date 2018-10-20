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
import walkingkooka.color.Color;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.math.Fraction;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatExpressionParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.format.SpreadsheetFormatParsers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public final class ExpressionSpreadsheetTextFormatterTest extends SpreadsheetTextFormatterTemplate2TestCase<ExpressionSpreadsheetTextFormatter, Object, SpreadsheetFormatExpressionParserToken> {

    private final static String TEXT = "Abc123";
    private final static Color RED = Color.fromRgb(0x0FF);
    private final static Color COLOR31 = Color.fromRgb(0x031);

    @Test
    public void testGeneralEmptyEmptyTextPlaceholderFormatNumber() {
        this.parseFormatAndCheck("General;;;@",
                BigDecimal.valueOf(9),
                "009.000");
    }

    @Test
    public void testDateTimeEmptyEmptyTextPlaceholderFormatNumber() {
        this.parseFormatAndCheck("yyyymmddhhmmss;;;@",
                LocalDateTime.of(2000, 12, 31, 12, 58, 59),
                "20001231125859");
    }

    @Test
    public void testNumberEmptyEmptyTextPlaceholderFormatNumber() {
        this.parseFormatAndCheck("#.0\"1st\";;;@",
                BigDecimal.valueOf(123),
                "123.01st");
    }

    @Test
    public void testColorNumberNumberEmptyEmptyTextPlaceholderFormatNumber() {
        this.parseFormatAndCheck("[RED]#.0\"1st\";;;@",
                BigDecimal.valueOf(123),
                RED,
                "123.01st");
    }

    @Test
    public void testColorNameNumberEmptyEmptyTextPlaceholderFormatNumber() {
        this.parseFormatAndCheck("[COLOR 31]#.0\"1st\";;;@",
                BigDecimal.valueOf(123),
                COLOR31,
                "123.01st");
    }

    @Test
    public void testEmptyEmptyEmptyEmptyFormatText() {
        this.parseFormatAndCheck(";;;", TEXT, "");
    }

    @Test
    public void testEmptyEmptyEmptyGeneralFormatText() {
        this.parseFormatAndCheck(";;;General", TEXT, TEXT);
    }

    @Test
    public void testEmptyEmptyEmptyTextPlaceholderFormatText() {
        this.parseFormatAndCheck(";;;@", TEXT, TEXT);
    }

    @Test
    public void testConditionalNumberFormatPositiveNumber() {
        this.parseFormatAndCheck("[>1]\"positive\"#.0", BigDecimal.valueOf(123), "positive123.0");
    }

    @Test
    public void testConditionalNumberConditionalNumberFormatNegativeNumber() {
        this.parseFormatAndCheck("[>1]\"positive\"#.0;[<1]\"negative\"#.0", BigDecimal.valueOf(-123), "negative-123.0");
    }

    @Test
    public void testConditionalNumberConditionalNumberNumberFormatZeroNumber() {
        this.parseFormatAndCheck("[>1]\"positive\"#.0;[>1]\"negative\"#.0;\"zero\"0.0", BigDecimal.ZERO, "zero0.0");
    }

    @Test
    public void testConditionalNumberConditionalNumberNumberFormatText() {
        final String text = "abc123";
        this.parseFormatAndCheck("[>1]\"positive\"#.0;[>1]\"negative\"#.0;\"zero\"0.0;\"Text\"@", text, "Text" + text);
    }

    // helpers.......................................................................................................

    private void parseFormatAndCheck(final String pattern,
                                     final Object value,
                                     final String text) {
        this.parseFormatAndCheck(pattern, value, SpreadsheetFormattedText.with(SpreadsheetFormattedText.WITHOUT_COLOR, text));
    }

    private void parseFormatAndCheck(final String pattern,
                                     final Object value,
                                     final Color color,
                                     final String text) {
        this.parseFormatAndCheck(pattern, value, SpreadsheetFormattedText.with(Optional.of(color), text));
    }

    private void parseFormatAndCheck(final String pattern,
                                      final Object value,
                                      final SpreadsheetFormattedText text) {
        this.formatAndCheck(this.createFormatter(pattern),
                value,
                this.createContext(),
                text);
    }

    @Override
    String pattern() {
        return ";;;@";
    }

    @Override
    Parser<SpreadsheetFormatParserToken, SpreadsheetFormatParserContext> parser() {
        return SpreadsheetFormatParsers.expression();
    }

    @Override
    ExpressionSpreadsheetTextFormatter createFormatter0(final SpreadsheetFormatExpressionParserToken token) {
        return ExpressionSpreadsheetTextFormatter.with(token, this.mathContext(), this.fractioner());
    }

    private MathContext mathContext() {
        return MathContext.UNLIMITED;
    }

    private Function<BigDecimal, Fraction> fractioner() {
        return this::makeIntoFraction;
    }

    private Fraction makeIntoFraction(final BigDecimal value) {
        if (value.signum() == 0) {
            return Fraction.with(BigInteger.ZERO, BigInteger.ONE);
        }

        final int scale = value.scale();
        final BigDecimal two = BigDecimal.valueOf(2);

        return Fraction.with(
                value.scaleByPowerOfTen(scale).divide(two).toBigInteger(),
                BigDecimal.ONE.scaleByPowerOfTen(scale).divide(two).toBigInteger());
    }

    @Override
    protected String value() {
        return "Text123";
    }

    @Override
    protected SpreadsheetTextFormatContext createContext() {
        return new FakeSpreadsheetTextFormatContext() {

            @Override
            public Color colorName(final String name) {
                assertEquals("RED", name);
                return RED;
            }

            @Override
            public Color colorNumber(final int number) {
                assertEquals("number", 31, number);
                return COLOR31;
            }

            @Override
            public String currencySymbol() {
                return "$";
            }

            @Override
            public char decimalPoint() {
                return '.';
            }

            @Override
            public char exponentSymbol() {
                return 'E';
            }

            @Override
            public char groupingSeparator() {
                return ',';
            }

            @Override
            public char minusSign() {
                return '-';
            }

            @Override
            public char percentageSymbol() {
                return '%';
            }

            @Override
            public char plusSign() {
                return '+';
            }

            @Override
            public String generalDecimalFormatPattern() {
                return "000.000"; // used by testGeneralEmptyEmptyTextPlaceholderFormatNumber
            }

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                if(target.isInstance(value)) {
                    return target.cast(value);
                }
                return Converters.localDateTimeBigDecimal(Converters.EXCEL_OFFSET).convert(value,
                        target,
                        ConverterContexts.basic(this));
            }
        };
    }

    @Override
    protected Class<ExpressionSpreadsheetTextFormatter> type() {
        return ExpressionSpreadsheetTextFormatter.class;
    }
}
