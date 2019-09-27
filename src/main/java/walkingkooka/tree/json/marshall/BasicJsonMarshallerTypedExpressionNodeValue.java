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
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.json.JsonNode;

import java.util.function.Function;

/**
 * A {@link BasicJsonMarshaller} that handles {@link ExpressionNode} and all sub classes.
 */
final class BasicJsonMarshallerTypedExpressionNodeValue<N extends ExpressionNode & Value<V>, V> extends BasicJsonMarshallerTypedExpressionNode<N> {

    static <N extends ExpressionNode & Value<V>, V> BasicJsonMarshallerTypedExpressionNodeValue<N, V> with(final Function<V, N> from,
                                                                                                           final Class<N> expressionNodeType,
                                                                                                           final Class<V> valueType) {

        return new BasicJsonMarshallerTypedExpressionNodeValue(from,
                expressionNodeType,
                valueType);
    }

    private BasicJsonMarshallerTypedExpressionNodeValue(final Function<V, N> from,
                                                        final Class<N> type,
                                                        final Class<V> valueType) {
        super(type);
        this.from = from;
        this.valueType = valueType;
    }

    @Override
    N fromJsonNodeNonNull(final JsonNode node,
                          final FromJsonNodeContext context) {
        return this.from.apply(context.fromJsonNode(node, this.valueType));
    }

    private final Function<V, N> from;
    private final Class<V> valueType;

    @Override
    JsonNode toJsonNodeNonNull(final N value,
                               final ToJsonNodeContext context) {
        return context.toJsonNode(value.value());
    }
}
