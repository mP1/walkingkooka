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
import walkingkooka.Cast;
import walkingkooka.test.ThrowableTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;
import walkingkooka.util.BiFunctionTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunctionTest implements BiFunctionTesting<JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue>,
        JsonNode,
        JsonNodeUnmarshallContext,
        TestJsonNodeValue>,
        ThrowableTesting,
        ToStringTesting<JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue>> {

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
            JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction.with(this.typeNameProperty(), null, this.valueType());
        });
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction.with(this.typeNameProperty(), JsonNode.object(), null);
        });
    }

    @Test
    public void testWith() {
        final JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue> function = this.createBiFunction();
    }

    @Test
    public void testApply() {
        final TestJsonNodeValue value = this.value();
        final JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue> function = this.createBiFunction();

        this.applyAndCheck(function,
                value.marshall(this.marshallContext()),
                this.unmarshallContext(),
                value);
    }

    @Test
    public void testApplyTwice() {
        final TestJsonNodeValue value1 = this.value();
        final JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue> function = this.createBiFunction();

        final JsonNodeMarshallContext context = this.marshallContext();

        this.applyAndCheck(function,
                value1.marshall(context),
                this.unmarshallContext(),
                value1);

        final TestJsonNodeValue value2 = TestJsonNodeValue.with("test-JsonNodeMap-b2");
        this.applyAndCheck(function,
                value2.marshall(context),
                this.unmarshallContext(),
                value2);
    }

    @Test
    public void testApplyTypeMissingFromSource() {
        final JsonNodeUnmarshallException thrown = assertThrows(JsonNodeUnmarshallException.class, () -> {
            this.createBiFunction(JsonNode.object())
                    .apply(this.value().marshall(this.marshallContext()), this.unmarshallContext());
        });
        checkMessage(thrown, "Unknown property \"typeNameProperty1\" in {}");
    }

    @Test
    public void testToString() {
        final JsonObjectNode object = this.objectWithType(TestJsonNodeValue.class);
        this.toStringAndCheck(this.createBiFunction(object), this.typeNameProperty() + " in " + object);
    }

    @Override
    public JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue> createBiFunction() {
        return this.createBiFunction(this.objectWithType(TestJsonNodeValue.class));
    }

    public JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue> createBiFunction(final JsonObjectNode objectWithType) {
        final Class<TestJsonNodeValue> type = this.valueType();

        return JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction.with(this.typeNameProperty(), objectWithType, type);
    }

    private JsonNodeName typeNameProperty() {
        return JsonNodeName.with("typeNameProperty1");
    }

    private JsonObjectNode objectWithType(final Class<?> type) {
        return JsonNode.object()
                .set(typeNameProperty(), this.marshallContext().typeName(type).get());
    }

    private TestJsonNodeValue value() {
        return TestJsonNodeValue.with("test-JsonNodeMap-a1");
    }

    private Class<TestJsonNodeValue> valueType() {
        return TestJsonNodeValue.class;
    }

    private JsonNodeUnmarshallContext unmarshallContext() {
        return JsonNodeUnmarshallContexts.basic();
    }

    private JsonNodeMarshallContext marshallContext() {
        return JsonNodeMarshallContexts.basic();
    }

    @Override
    public Class<JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction<TestJsonNodeValue>> type() {
        return Cast.to(JsonNodeUnmarshallContextUnmarshallWithTypePropertyBiFunction.class);
    }
}
