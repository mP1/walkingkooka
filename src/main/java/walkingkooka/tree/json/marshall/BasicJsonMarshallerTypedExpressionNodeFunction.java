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

import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionFunctionNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;

/**
 * A {@link BasicJsonMarshaller} that handles {@link ExpressionFunctionNode}
 */
final class BasicJsonMarshallerTypedExpressionNodeFunction extends BasicJsonMarshallerTypedExpressionNode<ExpressionFunctionNode> {

    static BasicJsonMarshallerTypedExpressionNodeFunction instance() {
        return new BasicJsonMarshallerTypedExpressionNodeFunction();
    }

    private BasicJsonMarshallerTypedExpressionNodeFunction() {
        super(ExpressionFunctionNode.class);
    }

    @Override
    ExpressionFunctionNode unmarshallNonNull(final JsonNode node,
                                               final JsonNodeUnmarshallContext context) {
        final JsonArrayNode array = node.arrayOrFail();
        return ExpressionNode.function(
                ExpressionNodeName.with(array.get(0).stringValueOrFail()),
                context.unmarshallWithTypeList(array.get(1)));
    }

    @Override
    JsonNode marshallNonNull(final ExpressionFunctionNode value,
                               final JsonNodeMarshallContext context) {
        return context.marshallList(
                Lists.of(
                        JsonNode.string(value.name().value()),
                        context.marshallWithTypeList(value.children())));
    }
}
