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

import java.util.List;

public final class ExpressionFunctionNodeEqualityTest extends ExpressionVariableNodeEqualityTestCase<ExpressionFunctionNode> {

    @Test
    public void testDifferentName() {
        this.checkNotEquals(ExpressionFunctionNode.with(this.name("different"), this.parameters()));
    }

    @Test
    public void testDifferentParameters() {
        this.checkNotEquals(ExpressionFunctionNode.with(this.name(), this.parameters("different")));
    }

    @Override
    protected ExpressionNode createObject() {
        return ExpressionFunctionNode.with(this.name(), this.parameters());
    }

    private ExpressionNodeName name() {
        return this.name("fx");
    }

    private ExpressionNodeName name(final String name) {
        return ExpressionNodeName.with(name);
    }

    private List<ExpressionNode> parameters() {
        return this.parameters("parameter1");
    }

    private List<ExpressionNode> parameters(final String parameterValue) {
        return Lists.of(ExpressionNode.text(parameterValue));
    }
}
