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

import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.select.NodeSelector;

import java.util.List;

/**
 * A function that returns the position of the current node.
 */
final class ExpressionNodePositionFunction extends ExpressionTemplateFunction<Number> {
    /**
     * Singleton
     */
    static final ExpressionNodePositionFunction INSTANCE = new ExpressionNodePositionFunction();

    /**
     * Private ctor
     */
    private ExpressionNodePositionFunction() {
        super();
    }

    @Override
    public Number apply(final List<Object> parameters, final ExpressionFunctionContext context) {
        final Node<?, ?, ?, ?> node = this.thisInstance(parameters);
        return Long.valueOf(node.index() + NodeSelector.INDEX_BIAS);
    }

    @Override
    public ExpressionNodeName name() {
        return NAME;
    }

    private final static ExpressionNodeName NAME = ExpressionNodeName.with("position");

    @Override
    public String toString() {
        return this.name().toString();
    }
}
