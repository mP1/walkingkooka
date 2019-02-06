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
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.cursor.parser.spreadsheet.SpreadsheetLabelName;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicExpressionEvaluationContextTest extends  ExpressionEvaluationContextTestCase<BasicExpressionEvaluationContext> {

    @Test
    public void testWithNullFunctionsFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicExpressionEvaluationContext.with(null,
                    this.references(),
                    this.mathContext(),
                    this.converter(),
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicExpressionEvaluationContext.with(this.functions(),
                    null,
                    this.mathContext(),
                    this.converter(),
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullMathContextFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicExpressionEvaluationContext.with(this.functions(),
                    this.references(),
                    null,
                    this.converter(),
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullConverterFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicExpressionEvaluationContext.with(this.functions(),
                    this.references(),
                    this.mathContext(),
                    null,
                    this.decimalNumberContext());
        });
    }

    @Test
    public void testWithNullDecimalNumberContextFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicExpressionEvaluationContext.with(this.functions(),
                    this.references(),
                    this.mathContext(),
                    this.converter(),
                    null);

        });
    }


    @Test
    public void testFunction() {
        assertEquals(this.functionValue(), this.createContext().function(this.functionName(), this.parameters()));
    }

    @Test
    public void testReferences() {
        assertEquals(Optional.of(this.expressionNode()), this.createContext().reference(this.expressionReference()));
    }

    @Test
    public void testMathContext() {
        assertEquals(this.mathContext(), this.createContext().mathContext());
    }

    @Test
    public void testConvert() {
        assertEquals(Long.valueOf(123L), this.createContext().convert(123.0, Long.class));
    }

    @Override
    protected BasicExpressionEvaluationContext createContext() {
        return BasicExpressionEvaluationContext.with(this.functions(),
                this.references(),
                this.mathContext(),
                this.converter(),
                this.decimalNumberContext());
    }

    private BiFunction<ExpressionNodeName, List<Object>, Object> functions() {
        return (functionName, parameters) -> {
            Objects.requireNonNull(functionName, "functionName");
            Objects.requireNonNull(parameters, "parameters");

            assertEquals(this.functionName(), functionName, "functionName");
            assertEquals(this.parameters(), parameters, "parameters");
            return this.functionValue();
        };
    }

    private ExpressionNodeName functionName() {
        return ExpressionNodeName.with("sum");
    }

    private List<Object> parameters() {
        return Lists.of("parameter-1", 2);
    }

    private Object functionValue() {
        return "function-value-234";
    }

    private Function<ExpressionReference, Optional<ExpressionNode>> references() {
        return (r -> {
            Objects.requireNonNull(r, "references");
            assertEquals(this.expressionReference(), r, "reference");
            return Optional.of(this.expressionNode());
        });
    }

    private ExpressionReference expressionReference() {
        return SpreadsheetLabelName.with("label123");
    }

    private ExpressionNode expressionNode() {
        return ExpressionNode.text("expression node 123");
    }

    private MathContext mathContext() {
        return MathContext.DECIMAL128;
    }

    private Converter converter() {
        return Converters.numberLong();
    }

    private DecimalNumberContext decimalNumberContext() {
        return DecimalNumberContexts.fake();
    }

    @Override
    public Class<BasicExpressionEvaluationContext> type() {
        return BasicExpressionEvaluationContext.class;
    }
}
