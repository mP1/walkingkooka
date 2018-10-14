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

package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.test.TestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserContexts;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParsers;

import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ExpressionNodeEvaluationTest extends TestCase {

    @Test
    public void testSimpleAddition() {
        this.parseEvaluateAndCheck("1+2", 1+2);
    }

    @Test
    public void testSimpleMultiplication() {
        this.parseEvaluateAndCheck("3*4.5",  3 * 4.5);
    }

    @Test
    public void testMathOperatorPriority() {
        this.parseEvaluateAndCheck("1+2*3+4.5",  1+2*3+4.5);
    }

    @Test
    public void testParenthesis() {
        this.parseEvaluateAndCheck("((1+2))",  ((1+2)));
    }

    // FIXME Spreadsheet parsing ignores everything after the first right parens.

    @Test
    public void testParenthesis2() {
        this.parseEvaluateAndCheck("(1+2)*3.5",  (1+2)*3.5);
    }

    @Test
    public void testParenthesis3() {
        this.parseEvaluateAndCheck("((1+2)*3.5)",  ((1+2)*3.5));
    }

    @Test
    public void testParenthesis4() {
        this.parseEvaluateAndCheck("(1+2)+(3+4.5)",  (1+2)+(3+4.5));
    }

    @Test
    public void testParenthesis5() {
        this.parseEvaluateAndCheck("(1+2)+(3+4.5)*-6",  (1+2)+(3+4.5)*-6);
    }

    @Test
    public void testParenthesis6() {
        this.parseEvaluateAndCheck("(1+2*(3+4*(5+6)*-7))",  (1+2*(3+4*(5+6)*-7)));
    }

    @Test
    public void testParenthesis7() {
        this.parseEvaluateAndCheck("-(1+2*(3+4*(5+6)*-7))",  -(1+2*(3+4*(5+6)*-7)));
    }

    @Test
    public void testParenthesis8() {
        this.parseEvaluateAndCheck("-(1+2*(3+4*(5+6)*-(7*8+(9+0))))",  -(1+2*(3+4*(5+6)*-(7*8+(9+0)))));
    }

    @Test
    public void testParenthesis9() {
        this.parseEvaluateAndCheck("((((1+2))))",  ((((1+2)))));
    }

    @Test
    public void testParenthesis10() {
        this.parseEvaluateAndCheck("-(-(-(-(1+2))))",  -(-(-(-(1+2)))));
    }

    @Test
    public void testLongFormulaWithoutParens() {
        this.parseEvaluateAndCheck("1+2-3+4-5*6+7-8*9",  1+2-3+4-5*6+7-8*9);
    }

    @Test
    public void testLongFormulaWithoutParens2() {
        this.parseEvaluateAndCheck("-1+2-3+4-5*6+7-8*9",  -1+2-3+4-5*6+7-8*9);
    }

    @Test
    public void testLocalDateSubtraction() {
        this.parseEvaluateAndCheck("toDate(\"2000-01-03\")-toDate(\"1999-12-31\")",  3);// days!
    }

    @Test
    public void testLocalDateTimeSubtraction() {
        this.parseEvaluateAndCheck("toDateTime(\"2000-02-01T12:00:00\")-toDateTime(\"2000-01-31T06:00:00\")",  1.25); //1 1/4days
    }

    @Test
    public void testLocalTimeSubtraction() {
        this.parseEvaluateAndCheck("toTime(toTime(\"18:00:00\")-toTime(\"06:00:00\"))",  "12:00:00"); //1/2 a day or 12noon
    }

    private void parseEvaluateAndCheck(final String formulaText, final Object expectedText) {
        this.parseEvaluateAndCheck(formulaText, String.valueOf(expectedText));
    }

    private void parseEvaluateAndCheck(final String formulaText, final String expectedText) {
        final SpreadsheetParserToken formula = this.parse(formulaText);
        final Optional<ExpressionNode> maybeExpression = formula.expressionNode();
        if(!maybeExpression.isPresent()){
            fail("Failed to convert spreadsheet formula to expression " + CharSequences.quoteAndEscape(formulaText));
        }
        final ExpressionNode expression = maybeExpression.get();
        final String value = expression.toText(this.context());
        assertEquals("expression " + CharSequences.quoteAndEscape(formulaText) + " as text is", expectedText, value);
    }

    private ExpressionEvaluationContext context() {
        final ExpressionNodeName toDate = ExpressionNodeName.with("toDate");
        final ExpressionNodeName toDateTime = ExpressionNodeName.with("toDateTime");
        final ExpressionNodeName toTime = ExpressionNodeName.with("toTime");

        final ExpressionEvaluationContext context = ExpressionNodeTestCase.context();
        return new FakeExpressionEvaluationContext() {
            @Override
            public Object function(final ExpressionNodeName name, final List<Object> parameters) {
                if(toDate.equals(name)){
                    return this.convertStringParameter(parameters, LocalDate.class);
                }
                if(toDateTime.equals(name)){
                    return this.convertStringParameter(parameters, LocalDateTime.class);
                }
                if(toTime.equals(name)){
                    return this.convertStringParameter(parameters, LocalTime.class);
                }

                return context.function(name, parameters);
            }

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                return context.reference(reference);
            }

            @Override
            public MathContext mathContext() {
                return context.mathContext();
            }

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                return context.convert(value, target);
            }

            private <T> T convertStringParameter(final List<Object> parameters, final Class<T> targetType) {
                if(parameters.size()!=1){
                    throw new IllegalArgumentException("Expected 1 parameter=" + parameters);
                }
                return context.convert(parameters.get(0), targetType);
            }
        };
    }

    private SpreadsheetParserToken parse(final String parse){
        final TextCursor cursor = TextCursors.charSequence(parse);
        final Optional<SpreadsheetParserToken> spreadsheetFormula = this.createParser().parse(cursor,
                SpreadsheetParserContexts.basic(ParserContexts.basic('.', 'E', '-', '+')));
        if(!spreadsheetFormula.isPresent()){
            fail("Parser failed to parse " + CharSequences.quoteAndEscape(parse));
        }

        final TextCursorSavePoint after = cursor.save();
        cursor.end();
        final String leftOver = after.textBetween().toString();
        if(!leftOver.isEmpty()){
            fail("Parser left " + CharSequences.quoteAndEscape(leftOver) + " from " + CharSequences.quoteAndEscape(parse));
        }
        return spreadsheetFormula.get();
    }

    private Parser<SpreadsheetParserToken, SpreadsheetParserContext> createParser() {
        final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number = Parsers.<SpreadsheetParserContext>bigDecimal( MathContext.DECIMAL32)
                .transform((numberParserToken, parserContext) -> SpreadsheetParserToken.bigDecimal(numberParserToken.value(), numberParserToken.text()))
                .cast();

        return SpreadsheetParsers.expression(number);
    }
}
