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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.test.TestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParsers;

import java.math.MathContext;
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
    @Ignore
    public void testParenthesis2() {
        this.parseEvaluateAndCheck("(1+2)*3.5",  (1+2)*3.5);
    }

    @Test
    @Ignore
    public void testParenthesis3() {
        this.parseEvaluateAndCheck("((1+2)*3.5)",  ((1+2)*3.5));
    }

    @Test
    @Ignore
    public void testParenthesis4() {
        this.parseEvaluateAndCheck("(1+2)+(3+4.5)*-6",  (1+2)*(3+4.5)*-6);
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
        final String value = expression.toText(ExpressionNodeTestCase.context());
        assertEquals("expression " + CharSequences.quoteAndEscape(formulaText) + " as text is", expectedText, value);
    }

    private SpreadsheetParserToken parse(final String parse){
        final TextCursor cursor = TextCursors.charSequence(parse);
        final Optional<SpreadsheetParserToken> spreadsheetFormula = this.createParser().parse(cursor, new SpreadsheetParserContext());
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
        final Parser<SpreadsheetParserToken, SpreadsheetParserContext> number = Parsers.<SpreadsheetParserContext>bigDecimal('.', MathContext.DECIMAL32)
                .transform((numberParserToken, parserContext) -> SpreadsheetParserToken.bigDecimal(numberParserToken.value(), numberParserToken.text()))
                .cast(SpreadsheetParserToken.class);

        return SpreadsheetParsers.expression(number);
    }
}
