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

import walkingkooka.tree.visit.Visiting;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FakeExpressionNumberVisitor extends ExpressionNumberVisitor {
    @Override 
    protected Visiting startVisit(final Number number) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final Number number) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigDecimal number) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final BigInteger number) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Double number) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visit(final Long number) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Number visit(final Number number) {
        throw new UnsupportedOperationException();
    }
}
