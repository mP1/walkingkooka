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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import walkingkooka.tree.expression.ExpressionNode;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.ExpressionReferenceNode;
import walkingkooka.tree.json.JsonNode;

public final class BasicJsonMarshallerTypedExpressionNodeReferenceTest extends BasicJsonMarshallerTypedExpressionNodeTestCase<BasicJsonMarshallerTypedExpressionNodeReference, ExpressionReferenceNode> {

    @BeforeAll
    public static void beforeAll() {
        remover = BasicJsonMarshaller.register(REFERENCE_TYPE_NAME,
                (n, c) -> REFERENCE,
                (r, c) -> REFERENCE_JSON,
                TestExpressionReference.class);
    }

    @AfterAll
    public static void afterAll() {
        remover.run();
    }

    private static Runnable remover;

    private final static String REFERENCE_TYPE_NAME = "test-expression-reference";
    private final static TestExpressionReference REFERENCE = new TestExpressionReference();
    private final static JsonNode REFERENCE_JSON = JsonNode.string("reference-123abc");

    static class TestExpressionReference implements ExpressionReference {
    }

    @Test
    public final void testFromBooleanFails() {
        this.fromJsonNodeFailed(JsonNode.booleanNode(true), ClassCastException.class);
    }

    @Test
    public final void testFromNumberFails() {
        this.fromJsonNodeFailed(JsonNode.number(123), ClassCastException.class);
    }

    @Test
    public final void testFromStringFails() {
        this.fromJsonNodeFailed(JsonNode.string("abc123"), ClassCastException.class);
    }

    @Override
    BasicJsonMarshallerTypedExpressionNodeReference marshaller() {
        return BasicJsonMarshallerTypedExpressionNodeReference.instance();
    }

    @Override
    ExpressionReferenceNode value() {
        return ExpressionNode.reference(REFERENCE);
    }

    @Override
    JsonNode node() {
        return this.typeAndValue(REFERENCE_TYPE_NAME, REFERENCE_JSON);
    }

    @Override
    String typeName() {
        return "expression-reference";
    }

    @Override
    Class<ExpressionReferenceNode> marshallerType() {
        return ExpressionReferenceNode.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedExpressionNodeReference> type() {
        return BasicJsonMarshallerTypedExpressionNodeReference.class;
    }
}
