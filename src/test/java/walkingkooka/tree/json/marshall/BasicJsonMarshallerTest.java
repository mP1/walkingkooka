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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.tree.json.JsonStringNode;
import walkingkooka.tree.json.UnsupportedTypeJsonNodeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicJsonMarshallerTest extends BasicJsonMarshallerTestCase<BasicJsonMarshaller<Void>> {

    @AfterEach
    public void afterEach() {
        TestJsonNodeValue.unregister();
    }

    // register.........................................................................................................

    @Test
    public void testRegisterNullTypeNameFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonMarshaller.register(null,
                    TestJsonNodeValue::fromJsonNode,
                    TestJsonNodeValue::toJsonNode,
                    TestJsonNodeValue.class);
        });
    }

    @Test
    public void testRegisterEmptyTypeNameFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            BasicJsonMarshaller.register("",
                    TestJsonNodeValue::fromJsonNode,
                    TestJsonNodeValue::toJsonNode,
                    TestJsonNodeValue.class);
        });
    }

    @Test
    public void testRegisterNullFromFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonMarshaller.register(TestJsonNodeValue.TYPE_NAME,
                    null,
                    TestJsonNodeValue::toJsonNode,
                    TestJsonNodeValue.class);
        });
    }

    @Test
    public void testRegisterNullToFunctionFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonMarshaller.register(TestJsonNodeValue.TYPE_NAME,
                    TestJsonNodeValue::fromJsonNode,
                    null,
                    TestJsonNodeValue.class);
        });
    }

    @Test
    public void testRegisterNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonMarshaller.register(TestJsonNodeValue.TYPE_NAME,
                    TestJsonNodeValue::fromJsonNode,
                    TestJsonNodeValue::toJsonNode,
                    null);
        });
    }

    @Test
    public void testRegisterConcrete() {
        TestJsonNodeValue.register();
    }

    @Test
    public void testRegisterTwiceFails() {
        TestJsonNodeValue.register();

        assertThrows(IllegalArgumentException.class, () -> {
            BasicJsonMarshaller.register(TestJsonNodeValue.TYPE_NAME,
                    TestJsonNodeValue::fromJsonNode,
                    TestJsonNodeValue::toJsonNode,
                    TestJsonNodeValue.class);
        });
    }

    @Test
    public void testRegisteredTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonMarshaller.registeredType(null);
        });
    }

    @Test
    public void testRegisteredType() {
        assertNotEquals(Optional.empty(), BasicJsonMarshaller.registeredType(JsonNode.string("big-decimal")));
    }

    @Test
    public void testRegisteredTypeUnknown() {
        assertEquals(Optional.empty(), BasicJsonMarshaller.registeredType(JsonNode.string("???")));
    }

    // typeName..........................................................................................................

    @Test
    public void testTypeNameNullClassFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonMarshaller.typeName(null);
        });
    }

    @Test
    public void testTypeNameUnknown() {
        this.typeNameAndCheck(this.getClass(),
                Optional.empty());
    }

    @Test
    public void testTypeNameBigDecimal() {
        this.typeNameAndCheck(BigDecimal.class,
                Optional.of(JsonNode.string("big-decimal")));
    }

    @Test
    public void testTypeNameJsonObjectNode() {
        this.typeNameAndCheck(JsonObjectNode.class,
                Optional.of(JsonNode.string("json")));
    }

    private void typeNameAndCheck(final Class<?> type,
                                  final Optional<JsonStringNode> typeName) {
        assertEquals(typeName,
                BasicJsonMarshaller.typeName(type),
                () -> "typeName of " + type.getName());
    }

    @Test
    public void testMarshallerOrFailStringUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            BasicJsonMarshaller.marshaller("???");
        });
    }

    @Test
    public void testMarshallerOrFailClassUnknownTypeFails() {
        assertThrows(UnsupportedTypeJsonNodeException.class, () -> {
            BasicJsonMarshaller.marshaller(this.getClass());
        });
    }

    // Expression.......................................................................................................

    @Test
    public void testExpressionAdditionNode() {
        this.roundtripAndCheck(ExpressionNode::addition);
    }

    @Test
    public void testExpressionAndNode() {
        this.roundtripAndCheck(ExpressionNode::and);
    }

    @Test
    public void testExpressionBigDecimalNode() {
        this.roundtripAndCheck(ExpressionNode.bigDecimal(BigDecimal.valueOf(1.25)));
    }

    @Test
    public void testExpressionBigIntegerNode() {
        this.roundtripAndCheck(ExpressionNode.bigInteger(BigInteger.valueOf(567)));
    }

    @Test
    public void testExpressionBooleanNode() {
        this.roundtripAndCheck(ExpressionNode.booleanNode(true));
    }

    @Test
    public void testExpressionDivisionNode() {
        this.roundtripAndCheck(ExpressionNode::division);
    }

    @Test
    public void testExpressionDoubleNode() {
        this.roundtripAndCheck(ExpressionNode.doubleNode(99.5));
    }

    @Test
    public void testExpressionEqualsNode() {
        this.roundtripAndCheck(ExpressionNode::equalsNode);
    }

    @Test
    public void testExpressionFunctionNode() {
        this.roundtripAndCheck(ExpressionNode.function(
                ExpressionNodeName.with("function123"),
                Lists.of(ExpressionNode.booleanNode(true), ExpressionNode.text("2b"))));
    }

    @Test
    public void testExpressionGreaterThanEqualsNode() {
        this.roundtripAndCheck(ExpressionNode::greaterThanEquals);
    }

    @Test
    public void testExpressionGreaterThanNode() {
        this.roundtripAndCheck(ExpressionNode::greaterThan);
    }

    @Test
    public void testExpressionLessThanEqualsNode() {
        this.roundtripAndCheck(ExpressionNode::greaterThanEquals);
    }

    @Test
    public void testExpressionLessThanNode() {
        this.roundtripAndCheck(ExpressionNode::greaterThan);
    }

    @Test
    public void testExpressionLocalDateNode() {
        this.roundtripAndCheck(ExpressionNode.localDate(LocalDate.of(2000, 12, 31)));
    }

    @Test
    public void testExpressionLocalDateTimeNode() {
        this.roundtripAndCheck(ExpressionNode.localDateTime(LocalDateTime.of(2000, 12, 31, 6, 28, 29)));
    }

    @Test
    public void testExpressionLocalTimeNode() {
        this.roundtripAndCheck(ExpressionNode.localTime(LocalTime.of(6, 28, 29)));
    }

    @Test
    public void testExpressionLongNode() {
        this.roundtripAndCheck(ExpressionNode.longNode(123456));
    }

    @Test
    public void testExpressionModuloNode() {
        this.roundtripAndCheck(ExpressionNode::modulo);
    }

    @Test
    public void testExpressionMultiplicationNode() {
        this.roundtripAndCheck(ExpressionNode::multiplication);
    }

    @Test
    public void testExpressionNegativeNode() {
        this.roundtripAndCheck(ExpressionNode::negative);
    }

    @Test
    public void testExpressionNotNode() {
        this.roundtripAndCheck(ExpressionNode::not);
    }

    @Test
    public void testExpressionNotEqualsNode() {
        this.roundtripAndCheck(ExpressionNode::notEquals);
    }

    @Test
    public void testExpressionOrNode() {
        this.roundtripAndCheck(ExpressionNode::or);
    }

    @Test
    public void testExpressionPowerNode() {
        this.roundtripAndCheck(ExpressionNode::power);
    }

    @Test
    public void testExpressionSubtractionNode() {
        this.roundtripAndCheck(ExpressionNode::subtraction);
    }

    @Test
    public void testExpressionText() {
        this.roundtripAndCheck(ExpressionNode.text("abc123"));
    }

    @Test
    public void testExpressionXorNode() {
        this.roundtripAndCheck(ExpressionNode::xor);
    }

    private void roundtripAndCheck(final Function<ExpressionNode, ExpressionNode> factory) {
        this.roundtripAndCheck(factory.apply(ExpressionNode.text("only-parameter")));
    }

    private void roundtripAndCheck(final BiFunction<ExpressionNode, ExpressionNode, ExpressionNode> factory) {
        this.roundtripAndCheck(factory.apply(ExpressionNode.bigInteger(BigInteger.valueOf(1)), ExpressionNode.text("parameter-2b")));
    }

    private void roundtripAndCheck(final Object value) {
        final JsonNode json = ToJsonNodeContexts.basic().toJsonNode(value);
        assertEquals(value,
                FromJsonNodeContexts.basic().fromJsonNode(json, value.getClass()),
                () -> "roundtrip " + value + "\n" + json);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BasicJsonMarshaller<Void>> type() {
        return Cast.to(BasicJsonMarshaller.class);
    }
}
