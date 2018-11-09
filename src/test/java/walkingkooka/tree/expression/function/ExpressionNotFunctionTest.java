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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class ExpressionNotFunctionTest extends ExpressionFunctionTestCase<ExpressionNotFunction, Boolean> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFunctionFails() {
        ExpressionNotFunction.with(null);
    }

    @Test
    public void testInverts() {
        this.applyAndCheck2(parameters(this, "a1", "a"), false);
    }

    @Test
    public void testInverts2() {
        this.applyAndCheck2(parameters(this, "a1", "z"), true);
    }

    @Test
    public void testToString() {
        assertEquals("not(" + ExpressionTemplateFunction.CONTAINS + ")", this.createBiFunction().toString());
    }

    @Override
    protected ExpressionNotFunction createBiFunction() {
        return ExpressionNotFunction.with(ExpressionTemplateFunction.CONTAINS);
    }

    @Override
    protected Class<ExpressionNotFunction> type() {
        return ExpressionNotFunction.class;
    }
}
