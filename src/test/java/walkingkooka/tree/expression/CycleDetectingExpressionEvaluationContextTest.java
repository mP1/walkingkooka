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
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetCellReference;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetLabelName;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetReferenceKind;
import walkingkooka.util.variable.Variables;

import java.math.BigInteger;
import java.math.MathContext;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public final class CycleDetectingExpressionEvaluationContextTest extends ExpressionEvaluationContextTestCase<CycleDetectingExpressionEvaluationContext> {

    private final static String VALUE = "text123";

    @Test(expected = NullPointerException.class)
    public void testWithNullContextFails() {
        this.createContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullVariableFails() {
        CycleDetectingExpressionEvaluationContext.with(ExpressionEvaluationContexts.fake(), null);
    }

    public void testFunction() {
        final ExpressionNodeName name = ExpressionNodeName.with("sum");
        final List<Object> parameters = Lists.of("param-1", "param-2");

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {
            @Override
            public Object function(final ExpressionNodeName n, final List<Object> p) {
                assertSame("name", name, n);
                assertSame("parameters", parameters, p);

                return VALUE;
            }
        });
        assertSame(VALUE, context.function(name, parameters));
    }

    @Test
    public void testReference() {
        final SpreadsheetCellReference cell = this.cell();
        final ExpressionNode text = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                assertSame("cell", cell, reference);
                return text;
            }
        });
        assertSame(text, context.reference(cell));
    }

    @Test
    public void testReferenceToReference() {
        final SpreadsheetLabelName label = SpreadsheetLabelName.with("label");

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                assertSame("label", label, reference);
                return text();
            }
        });
        final ExpressionNode expression = ExpressionNode.reference(label);

        this.toValueAndCheck(expression, context, VALUE);
        this.toValueAndCheck(expression, context, VALUE);
        this.toValueAndCheck(expression, context, VALUE);
    }

    @Test
    public void testReferenceToReferenceToReference() {
        // label2 -> label1 -> cell
        final SpreadsheetLabelName label1 = label1();
        final SpreadsheetLabelName label2 = label2();
        final SpreadsheetCellReference cell = this.cell();

        final ExpressionNode label2Expression = ExpressionNode.reference(label1);
        final ExpressionNode label1Expression = ExpressionNode.reference(cell);
        final ExpressionNode cellExpression = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                if (label2 == reference) {
                    return label2Expression;
                }
                if (label1 == reference) {
                    return label1Expression;
                }
                if (cell == reference) {
                    return cellExpression;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });

        this.toValueAndCheck(cellExpression, context, VALUE);
        this.toValueAndCheck(label1Expression, context, VALUE);
        this.toValueAndCheck(label2Expression, context, VALUE);
    }

    @Test(expected = CycleDetectedExpressionEvaluationConversionException.class)
    public void testReferenceToSelfCycleFails() {
        // label2 -> label1 -> cell
        final SpreadsheetLabelName label = label1();

        final ExpressionNode labelExpression = ExpressionNode.reference(label);

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                if (label == reference) {
                    return labelExpression;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });
        labelExpression.toValue(context);
    }

    @Test(expected = CycleDetectedExpressionEvaluationConversionException.class)
    public void testReferenceWithCycleFails() {
        // label2 -> label1 -> cell
        final SpreadsheetLabelName label1 = label1();
        final SpreadsheetLabelName label2 = label2();

        final ExpressionNode label2Expression = ExpressionNode.reference(label1);
        final ExpressionNode label1Expression = ExpressionNode.reference(label2);

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                if (label2 == reference) {
                    return label1Expression;
                }
                if (label1 == reference) {
                    return label2Expression;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });
        label2Expression.toValue(context);
    }

    @Test(expected = CycleDetectedExpressionEvaluationConversionException.class)
    public void testReferenceWithCycleFails2() {
        final SpreadsheetLabelName label1 = label1();
        final SpreadsheetLabelName label2 = label2();
        final SpreadsheetLabelName label3 = label3();

        final ExpressionNode label2Expression = ExpressionNode.reference(label1);
        final ExpressionNode label1Expression = ExpressionNode.reference(label2);
        final ExpressionNode label3Expression = ExpressionNode.reference(label2);

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                if (label2 == reference || label3 == reference) {
                    return label1Expression;
                }
                if (label1 == reference) {
                    return label2Expression;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });
        label3Expression.toValue(context); // --> label2 --> label1 --> label2 cycle!!!
    }

    @Test
    public void testReferenceAfterCycleDetected() {
        // label2 -> label1 -> cell
        final SpreadsheetLabelName label1 = label1();
        final SpreadsheetLabelName label2 = label2();

        final ExpressionNode label2Expression = ExpressionNode.reference(label1);
        final ExpressionNode label1Expression = ExpressionNode.reference(label2);
        final ExpressionNode cellExpression = this.text();

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public ExpressionNode reference(final ExpressionReference reference) {
                if (label2 == reference) {
                    return label1Expression;
                }
                if (label1 == reference) {
                    return label2Expression;
                }
                return this.unknownReference(reference);
            }

            private <T> T unknownReference(final ExpressionReference reference) {
                fail("Unknown reference=" + reference);
                return null;
            }
        });
        try {
            label2Expression.toValue(context);
            fail("Expected " + CycleDetectedExpressionEvaluationConversionException.class.getSimpleName());
        } catch (final CycleDetectedExpressionEvaluationConversionException ignore) {

        }
        this.toValueAndCheck(cellExpression, context, VALUE);
    }

    @Test
    public void testMathContext() {
        final MathContext mathContext = MathContext.DECIMAL32;

        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {
            @Override
            public MathContext mathContext() {
                return mathContext;
            }
        });
        assertSame(mathContext, context.mathContext());
    }

    @Test
    public void testConvert() {
        final CycleDetectingExpressionEvaluationContext context = this.createContext(new FakeExpressionEvaluationContext() {

            @Override
            public char minusSign() {
                return '-';
            }

            @Override
            public char plusSign() {
                return '+';
            }

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                return Converters.parser(BigInteger.class, Parsers.bigInteger(10), (c) -> ParserContexts.basic(c))
                        .convert(value, target, ConverterContexts.basic(this));
            }
        });
        assertEquals(BigInteger.valueOf(123), context.convert("123", BigInteger.class));
    }

    @Override
    protected CycleDetectingExpressionEvaluationContext createContext() {
        return this.createContext(ExpressionEvaluationContexts.fake());
    }

    private CycleDetectingExpressionEvaluationContext createContext(final ExpressionEvaluationContext context) {
        return CycleDetectingExpressionEvaluationContext.with(context, Variables.with(null));
    }

    private SpreadsheetLabelName label1() {
        return SpreadsheetLabelName.with("label1");
    }

    private SpreadsheetLabelName label2() {
        return SpreadsheetLabelName.with("label2");
    }

    private SpreadsheetLabelName label3() {
        return SpreadsheetLabelName.with("label3");
    }

    private SpreadsheetCellReference cell() {
        return this.cell(12, 34);
    }

    private SpreadsheetCellReference cell(final int column, final int row) {
        return SpreadsheetCellReference.with(
                SpreadsheetReferenceKind.ABSOLUTE.column(column),
                SpreadsheetReferenceKind.ABSOLUTE.row(row));
    }

    private ExpressionNode text() {
        return ExpressionNode.text(VALUE);
    }

    @Override
    protected Class<CycleDetectingExpressionEvaluationContext> type() {
        return CycleDetectingExpressionEvaluationContext.class;
    }
}
