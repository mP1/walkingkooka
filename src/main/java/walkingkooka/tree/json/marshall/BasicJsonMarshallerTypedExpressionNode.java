/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.tree.json.marshall;

import walkingkooka.Value;
import walkingkooka.tree.Node;
import walkingkooka.tree.expression.ExpressionFunctionNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionReferenceNode;

import java.beans.Expression;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A {@link BasicJsonMarshaller} that handles {@link ExpressionNode} and all sub classes.
 */
abstract class BasicJsonMarshallerTypedExpressionNode<N extends ExpressionNode> extends BasicJsonMarshallerTyped<N> {

    /**
     * {@see BasicJsonMarshallerTypedExpressionNodeUnary}
     */
    static <N extends ExpressionNode> BasicJsonMarshallerTypedExpressionNode<N> unary(final Function<ExpressionNode, N> from,
                                                                                      final Class<N> type) {
        return BasicJsonMarshallerTypedExpressionNodeUnary.with(from, type);
    }

    /**
     * {@see BasicJsonMarshallerTypedExpressionNodeBinary}
     */
    static <N extends ExpressionNode> BasicJsonMarshallerTypedExpressionNode<N> binary(final BiFunction<ExpressionNode, ExpressionNode, N> from,
                                                                                       final Class<N> type) {

        return BasicJsonMarshallerTypedExpressionNodeBinary.with(from, type);
    }

    /**
     * {@see BasicJsonMarshallerTypedExpressionNodeFunction}
     */
    static BasicJsonMarshallerTypedExpressionNode<ExpressionFunctionNode> function() {
        return BasicJsonMarshallerTypedExpressionNodeFunction.instance();
    }

    /**
     * {@see BasicJsonMarshallerTypedExpressionNodeValue}
     */
    static <N extends ExpressionNode & Value<V>, V> BasicJsonMarshallerTypedExpressionNode<N> value(final Function<V, N> from,
                                                                                                    final Class<N> expressionNodeType,
                                                                                                    final Class<V> valueType) {
        return BasicJsonMarshallerTypedExpressionNodeValue.with(from, expressionNodeType, valueType);
    }

    /**
     * {@see BasicJsonMarshallerTypedExpressionNodeReference}
     */
    static BasicJsonMarshallerTypedExpressionNode<ExpressionReferenceNode> reference() {
        return BasicJsonMarshallerTypedExpressionNodeReference.instance();
    }

    /**
     * Package private to limit sub classing.
     */
    BasicJsonMarshallerTypedExpressionNode(final Class<N> type) {
        super();

        // everything between Expression...Node
        String name = type.getSimpleName();
        name = name.substring(Expression.class.getSimpleName().length(), name.length() - Node.class.getSimpleName().length());

        final StringBuilder b = new StringBuilder();
        b.append(Expression.class.getSimpleName().toLowerCase());

        for (char c : name.toCharArray()) {
            if (Character.isLetter(c) & Character.isUpperCase(c)) {
                b.append('-');
                b.append(Character.toLowerCase(c));
            } else {
                b.append(c);
            }
        }

        this.name = b.toString();
        this.type = type;
    }

    @Override
    final void register() {
        this.registerTypeNameAndType();
    }

    @Override
    final Class<N> type() {
        return this.type;
    }

    private final Class<N> type;

    @Override
    final String typeName() {
        return name;
    }

    private final String name;

    @Override
    final N unmarshallNull(final JsonNodeUnmarshallContext context) {
        return null;
    }
}
