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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.tree.expression.ExpressionAdditionNode;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.json.JsonNode;

import java.math.BigInteger;

public final class BasicJsonMarshallerTypedExpressionNodeBinaryTest extends BasicJsonMarshallerTypedExpressionNodeTestCase<BasicJsonMarshallerTypedExpressionNodeBinary<ExpressionAdditionNode>, ExpressionAdditionNode> {

    @Test
    public final void testFromBooleanFails() {
        this.fromJsonNodeFailed(JsonNode.booleanNode(true), null);
    }

    @Test
    public final void testFromNumberFails() {
        this.fromJsonNodeFailed(JsonNode.number(123), null);
    }

    @Test
    public final void testFromStringFails() {
        this.fromJsonNodeFailed(JsonNode.string("abc123"), null);
    }

    @Override
    BasicJsonMarshallerTypedExpressionNodeBinary marshaller() {
        return BasicJsonMarshallerTypedExpressionNodeBinary.with(ExpressionNode::addition, ExpressionAdditionNode.class);
    }

    @Override
    ExpressionAdditionNode value() {
        return ExpressionAdditionNode.addition(this.leftValue(), this.rightValue());
    }

    private ExpressionNode leftValue() {
        return ExpressionNode.bigInteger(BigInteger.valueOf(11));
    };

    private ExpressionNode rightValue() {
        return ExpressionNode.text("b2");
    };

    @Override
    JsonNode node() {
        final ToJsonNodeContext context = this.toJsonNodeContext();

        return JsonNode.array()
                .appendChild(context.toJsonNodeWithType(this.leftValue()))
                .appendChild(context.toJsonNodeWithType(this.rightValue()));
    }

    @Override
    String typeName() {
        return "expression-addition";
    }

    @Override
    Class<ExpressionAdditionNode> marshallerType() {
        return ExpressionAdditionNode.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedExpressionNodeBinary<ExpressionAdditionNode>> type() {
        return Cast.to(BasicJsonMarshallerTypedExpressionNodeBinary.class);
    }
}
