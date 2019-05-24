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

package walkingkooka.tree.expression.function;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class ExpressionRoundFunctionTest extends ExpressionNumberFunction2TestCase<ExpressionRoundFunction> {

    // BigDecimal........................................................................................................

    @Test
    public void testBigDecimalDown() {
        this.applyAndCheck3(BigDecimal.valueOf(1.25), BigDecimal.valueOf(1));
    }

    @Test
    public void testBigDecimalUp() {
        this.applyAndCheck3(BigDecimal.valueOf(1.5), BigDecimal.valueOf(2));
    }

    // BigInteger........................................................................................................

    @Test
    public void testBigInteger() {
        this.applyAndCheck3(BigInteger.valueOf(1));
    }

    // Double...........................................................................................................

    @Test
    public void testDoubleDown() {
        this.applyAndCheck3(Double.valueOf(1.25), Long.valueOf(1));
    }

    @Test
    public void testDoubleUp() {
        this.applyAndCheck3(Double.valueOf(1.5), Long.valueOf(2));
    }

    // Long.............................................................................................................

    @Test
    public void testLong() {
        this.applyAndCheck3(Long.valueOf(-2));
    }

    // Integer.............................................................................................................

    @Test
    public void testIntegerNegative() {
        this.applyAndCheck3(-34, BigDecimal.valueOf(-34));
    }

    // helper............................................................................................................

    @Override
    public ExpressionRoundFunction createBiFunction() {
        return ExpressionRoundFunction.INSTANCE;
    }

    @Override
    public Class<ExpressionRoundFunction> type() {
        return ExpressionRoundFunction.class;
    }

    @Override
    String functionToString() {
        return "round";
    }
}
