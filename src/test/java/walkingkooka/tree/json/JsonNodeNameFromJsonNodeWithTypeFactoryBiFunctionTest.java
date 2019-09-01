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

package walkingkooka.tree.json;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ThrowableTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.marshall.FromJsonNodeContext;
import walkingkooka.tree.json.marshall.FromJsonNodeContexts;
import walkingkooka.tree.json.marshall.FromJsonNodeException;
import walkingkooka.tree.json.marshall.TestJsonNodeValue;
import walkingkooka.tree.json.marshall.ToJsonNodeContext;
import walkingkooka.tree.json.marshall.ToJsonNodeContexts;
import walkingkooka.util.BiFunctionTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonNodeNameFromJsonNodeWithTypeFactoryBiFunctionTest implements BiFunctionTesting<JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue>,
        JsonNode,
        FromJsonNodeContext,
        TestJsonNodeValue>,
        ThrowableTesting,
        ToStringTesting<JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue>> {

    @BeforeAll
    public static void beforeAll() {
        TestJsonNodeValue.register();
    }

    @AfterAll
    public static void afterAll() {
        TestJsonNodeValue.unregister();
    }

    @Test
    public void testWithNullSourceFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction.with(this.typeNameProperty(), null, this.valueType());
        });
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction.with(this.typeNameProperty(), JsonNode.object(), null);
        });
    }

    @Test
    public void testWith() {
        final JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue> function = this.createBiFunction();
    }

    @Test
    public void testApply() {
        final TestJsonNodeValue value = this.value();
        final JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue> function = this.createBiFunction();

        this.applyAndCheck(function,
                value.toJsonNode(this.toJsonNodeContext()),
                this.fromJsonNodeContext(),
                value);
    }

    @Test
    public void testApplyTwice() {
        final TestJsonNodeValue value1 = this.value();
        final JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue> function = this.createBiFunction();

        final ToJsonNodeContext context = this.toJsonNodeContext();

        this.applyAndCheck(function,
                value1.toJsonNode(context),
                this.fromJsonNodeContext(),
                value1);

        final TestJsonNodeValue value2 = TestJsonNodeValue.with("test-JsonNodeMap-b2");
        this.applyAndCheck(function,
                value2.toJsonNode(context),
                this.fromJsonNodeContext(),
                value2);
    }

    @Test
    public void testApplyTypeMissingFromSource() {
        final FromJsonNodeException thrown = assertThrows(FromJsonNodeException.class, () -> {
            this.createBiFunction(JsonNode.object())
                    .apply(this.value().toJsonNode(this.toJsonNodeContext()), this.fromJsonNodeContext());
        });
        checkMessage(thrown,"Unknown property \"typeNameProperty1\" in {}");
    }

    @Test
    public void testToString() {
        final JsonObjectNode object = this.objectWithType(TestJsonNodeValue.class);
        this.toStringAndCheck(this.createBiFunction(object), this.typeNameProperty() + " in " + object);
    }

    @Override
    public JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue> createBiFunction() {
        return this.createBiFunction(this.objectWithType(TestJsonNodeValue.class));
    }

    public JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue> createBiFunction(final JsonObjectNode objectWithType) {
        final Class<TestJsonNodeValue> type = this.valueType();

        return Cast.to(this.typeNameProperty()
                .fromJsonNodeWithTypeFactory(objectWithType, type));
    }

    private JsonNodeName typeNameProperty() {
        return JsonNodeName.with("typeNameProperty1");
    }

    private JsonObjectNode objectWithType(final Class<?> type) {
        return JsonNode.object()
                .set(typeNameProperty(), this.toJsonNodeContext().typeName(type).get());
    }

    private TestJsonNodeValue value() {
        return TestJsonNodeValue.with("test-JsonNodeMap-a1");
    }

    private Class<TestJsonNodeValue> valueType() {
        return TestJsonNodeValue.class;
    }

    private FromJsonNodeContext fromJsonNodeContext() {
        return FromJsonNodeContexts.basic(this::objectPreProcessor);
    }

    private JsonObjectNode objectPreProcessor(final JsonObjectNode object, final Class<?> type) {
        return object;
    }

    private ToJsonNodeContext toJsonNodeContext() {
        return ToJsonNodeContexts.basic(this::objectPostProcessor);
    }

    private JsonObjectNode objectPostProcessor(final Object value, final JsonObjectNode object) {
        return object;
    }

    @Override
    public Class<JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeValue>> type() {
        return Cast.to(JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction.class);
    }
}
