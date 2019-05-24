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
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.MemberVisibility;

import java.math.BigDecimal;

public final class ExpressionTypeNameFunctionTest implements ClassTesting2<ExpressionTypeNameFunction>,
        ExpressionFunctionTesting<ExpressionTypeNameFunction, String> {

    @Test
    public void testBigDecimalParameter() {
        this.applyAndCheck2(parameters(BigDecimal.valueOf(123.5)),
                BigDecimal.class.getName());
    }

    @Test
    public void testThisTestParameter() {
        this.applyAndCheck2(parameters(this),
                this.getClass().getName());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiFunction(), "typeName");
    }

    @Override
    public ExpressionTypeNameFunction createBiFunction() {
        return ExpressionTypeNameFunction.INSTANCE;
    }

    @Override
    public Class<ExpressionTypeNameFunction> type() {
        return ExpressionTypeNameFunction.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}