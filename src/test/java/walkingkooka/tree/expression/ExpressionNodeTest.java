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

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserContext;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserContexts;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParserToken;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetParsers;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class ExpressionNodeTest implements ClassTesting2<ExpressionNode> {

    @Test
    public void testValueOrFailNullFails() {
        assertThrows(NullPointerException.class, () -> {
            ExpressionNode.valueOrFail(null);
        });
    }

    @Test
    public void testValueOrFailUnknownValueTypeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionNode.valueOrFail(this);
        });
    }

    @Test
    public void testValueOrFailBigInteger() {
        this.valueOrFailAndCheck(BigInteger.valueOf(123), ExpressionBigIntegerNode.class);
    }

    @Test
    public void testValueOrFailBigDecimal() {
        this.valueOrFailAndCheck(BigDecimal.valueOf(123.5), ExpressionBigDecimalNode.class);
    }

    @Test
    public void testValueOrFailBooleanTrue() {
        this.valueOrFailAndCheck(true, ExpressionBooleanNode.class);
    }

    @Test
    public void testValueOrFailBooleanFalse() {
        this.valueOrFailAndCheck(false, ExpressionBooleanNode.class);
    }

    @Test
    public void testValueOrFailFloat() {
        this.valueOrFailAndCheck(123.5f, ExpressionDoubleNode.class, 123.5);
    }

    @Test
    public void testValueOrFailDouble() {
        this.valueOrFailAndCheck(123.5, ExpressionDoubleNode.class);
    }

    @Test
    public void testValueOrFailByte() {
        this.valueOrFailAndCheck((byte) 123, ExpressionLongNode.class, 123L);
    }

    @Test
    public void testValueOrFailShort() {
        this.valueOrFailAndCheck((short) 123, ExpressionLongNode.class, 123L);
    }

    @Test
    public void testValueOrFailInteger() {
        this.valueOrFailAndCheck(123, ExpressionLongNode.class, 123L);
    }

    @Test
    public void testValueOrFailLong() {
        this.valueOrFailAndCheck(123L, ExpressionLongNode.class);
    }

    @Test
    public void testValueOrFailLocalDate() {
        this.valueOrFailAndCheck(LocalDate.of(2000, 12, 31), ExpressionLocalDateNode.class);
    }

    @Test
    public void testValueOrFailLocalDateTime() {
        this.valueOrFailAndCheck(LocalDateTime.of(2000, 12, 31, 12, 58, 59),
                ExpressionLocalDateTimeNode.class);
    }

    @Test
    public void testValueOrFailLocalTime() {
        this.valueOrFailAndCheck(LocalTime.of(12, 58, 59), ExpressionLocalTimeNode.class);
    }

    @Test
    public void testValueOrFailText() {
        this.valueOrFailAndCheck("abc123", ExpressionTextNode.class);
    }

    private void valueOrFailAndCheck(final Object value, final Class<? extends ExpressionValueNode> type) {
        valueOrFailAndCheck(value, type, value);
    }

    private void valueOrFailAndCheck(final Object value, final Class<? extends ExpressionValueNode> type, final Object expected) {
        final ExpressionNode node = ExpressionNode.valueOrFail(value);
        assertEquals(type, node.getClass(), "node type of " + value);
        assertEquals(expected, type.cast(node).value(), "value");
    }

    @Test
    public void testSimpleAddition() {
        this.parseEvaluateAndCheck("1+2", 1 + 2);
    }

    @Test
    public void testSimpleMultiplication() {
        this.parseEvaluateAndCheck("3*4.5", 3 * 4.5);
    }

    @Test
    public void testMathOperatorPriority() {
        this.parseEvaluateAndCheck("1+2*3+4.5", 1 + 2 * 3 + 4.5);
    }

    @Test
    public void testParenthesis() {
        this.parseEvaluateAndCheck("((1+2))", ((1 + 2)));
    }

    // FIXME Spreadsheet parsing ignores everything after the first right parens.

    @Test
    public void testParenthesis2() {
        this.parseEvaluateAndCheck("(1+2)*3.5", (1 + 2) * 3.5);
    }

    @Test
    public void testParenthesis3() {
        this.parseEvaluateAndCheck("((1+2)*3.5)", ((1 + 2) * 3.5));
    }

    @Test
    public void testParenthesis4() {
        this.parseEvaluateAndCheck("(1+2)+(3+4.5)", (1 + 2) + (3 + 4.5));
    }

    @Test
    public void testParenthesis5() {
        assertEquals(-42.0, (1 + 2) + (3 + 4.5) * -6, 0.5);
        this.parseEvaluateAndCheck("(1+2)+(3+4.5)*-6", -42);
    }

    @Test
    public void testParenthesis6() {
        this.parseEvaluateAndCheck("(1+2*(3+4*(5+6)*-7))", (1 + 2 * (3 + 4 * (5 + 6) * -7)));
    }

    @Test
    public void testParenthesis7() {
        this.parseEvaluateAndCheck("-(1+2*(3+4*(5+6)*-7))", -(1 + 2 * (3 + 4 * (5 + 6) * -7)));
    }

    @Test
    public void testParenthesis8() {
        this.parseEvaluateAndCheck("-(1+2*(3+4*(5+6)*-(7*8+(9+0))))", -(1 + 2 * (3 + 4 * (5 + 6) * -(7 * 8 + (9 + 0)))));
    }

    @Test
    public void testParenthesis9() {
        this.parseEvaluateAndCheck("((((1+2))))", ((((1 + 2)))));
    }

    @Test
    public void testParenthesis10() {
        this.parseEvaluateAndCheck("-(-(-(-(1+2))))", -(-(-(-(1 + 2)))));
    }

    @Test
    public void testLongFormulaWithoutParens() {
        this.parseEvaluateAndCheck("1+2-3+4-5*6+7-8*9", 1 + 2 - 3 + 4 - 5 * 6 + 7 - 8 * 9);
    }

    @Test
    public void testLongFormulaWithoutParens2() {
        this.parseEvaluateAndCheck("-1+2-3+4-5*6+7-8*9", -1 + 2 - 3 + 4 - 5 * 6 + 7 - 8 * 9);
    }

    @Test
    public void testLocalDateSubtraction() {
        this.parseEvaluateAndCheck("toDate(\"2000-01-03\")-toDate(\"1999-12-31\")", 3);// days!
    }

    @Test
    public void testLocalDateTimeSubtraction() {
        this.parseEvaluateAndCheck("toDateTime(\"2000-02-01T12:00:00\")-toDateTime(\"2000-01-31T06:00:00\")", 1.25); //1 1/4days
    }

    @Test
    public void testLocalTimeSubtraction() {
        this.parseEvaluateAndCheck("toTime(toTime(\"18:00:00\")-toTime(\"06:00:00\"))", "12:00:00"); //1/2 a day or 12noon
    }

    private void parseEvaluateAndCheck(final String formulaText, final Object expectedText) {
        this.parseEvaluateAndCheck(formulaText, String.valueOf(expectedText));
    }

    private void parseEvaluateAndCheck(final String formulaText, final String expectedText) {
        final SpreadsheetParserToken formula = this.parse(formulaText);
        final Optional<ExpressionNode> maybeExpression = formula.expressionNode();
        if (!maybeExpression.isPresent()) {
            fail("Failed to convert spreadsheet formula to expression " + CharSequences.quoteAndEscape(formulaText));
        }
        final ExpressionNode expression = maybeExpression.get();
        final String value = expression.toText(this.context());
        assertEquals(expectedText, value, "expression " + CharSequences.quoteAndEscape(formulaText) + " as text is");
    }

    private ExpressionEvaluationContext context() {
        final ExpressionNodeName toDate = ExpressionNodeName.with("toDate");
        final ExpressionNodeName toDateTime = ExpressionNodeName.with("toDateTime");
        final ExpressionNodeName toTime = ExpressionNodeName.with("toTime");

        final ExpressionEvaluationContext context = ExpressionNodeTestCase.context();
        return new FakeExpressionEvaluationContext() {
            @Override
            public Object function(final ExpressionNodeName name, final List<Object> parameters) {
                if (toDate.equals(name)) {
                    return this.convertStringParameter(parameters, LocalDate.class);
                }
                if (toDateTime.equals(name)) {
                    return this.convertStringParameter(parameters, LocalDateTime.class);
                }
                if (toTime.equals(name)) {
                    return this.convertStringParameter(parameters, LocalTime.class);
                }

                return context.function(name, parameters);
            }

            @Override
            public Optional<ExpressionNode> reference(final ExpressionReference reference) {
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
                if (parameters.size() != 1) {
                    throw new IllegalArgumentException("Expected 1 parameter=" + parameters);
                }
                return context.convert(parameters.get(0), targetType);
            }
        };
    }

    @Test
    public void testParseToJsonNodeAndFromJsonNode() {
        this.parseToJsonNodeAndFromJsonNodeAndCheck("1");
    }

    @Test
    public void testParseToJsonNodeAndFromJsonNodeArithmetic() {
        this.parseToJsonNodeAndFromJsonNodeAndCheck("1+2-3*4/5^-6");
    }

    @Test
    public void testParseToJsonNodeAndFromJsonNodeComparisons() {
        this.parseToJsonNodeAndFromJsonNodeAndCheck("1<2==3>4>=5<=6");
    }

    @Test
    public void testParseToJsonNodeAndFromJsonNodeFunction() {
        this.parseToJsonNodeAndFromJsonNodeAndCheck("sum(1, 2, 3, 4)");
    }

    private void parseToJsonNodeAndFromJsonNodeAndCheck(final String expression) {
        final ExpressionNode expressionNode = this.parse(expression).expressionNode().get();
        final JsonNode jsonNode = expressionNode.toJsonNodeWithType();
        assertEquals(expressionNode,
                jsonNode.fromJsonNodeWithType(),
                () -> "parse " + CharSequences.quote(expression) + " to json " + jsonNode + " then read back failed");
    }

    private SpreadsheetParserToken parse(final String parse) {
        final TextCursor cursor = TextCursors.charSequence(parse);
        final Optional<ParserToken> spreadsheetFormula = this.createParser().parse(cursor,
                SpreadsheetParserContexts.basic(DecimalNumberContexts.american()));
        if (!spreadsheetFormula.isPresent()) {
            fail("Parser failed to parse " + CharSequences.quoteAndEscape(parse));
        }

        final TextCursorSavePoint after = cursor.save();
        cursor.end();
        final String leftOver = after.textBetween().toString();
        if (!leftOver.isEmpty()) {
            fail("Parser left " + CharSequences.quoteAndEscape(leftOver) + " from " + CharSequences.quoteAndEscape(parse));
        }
        return spreadsheetFormula.get().cast();
    }

    private Parser<SpreadsheetParserContext> createParser() {
        return SpreadsheetParsers.expression();
    }

    @Override
    public Class<ExpressionNode> type() {
        return ExpressionNode.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
