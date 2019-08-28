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
import walkingkooka.tree.json.marshall.FromJsonNodeException;
import walkingkooka.tree.json.marshall.TestJsonNodeMap;
import walkingkooka.tree.json.marshall.ToJsonNodeContext;
import walkingkooka.util.BiFunctionTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JsonNodeNameFromJsonNodeWithTypeFactoryBiFunctionTest implements BiFunctionTesting<JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap>,
        JsonNode,
        FromJsonNodeContext,
        TestJsonNodeMap>,
        ThrowableTesting,
        ToStringTesting<JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap>> {

    @BeforeAll
    public static void beforeAll() {
        TestJsonNodeMap.register();
    }

    @AfterAll
    public static void afterAll() {
        TestJsonNodeMap.unregister();
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
        final JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap> function = this.createBiFunction();
    }

    @Test
    public void testApply() {
        final TestJsonNodeMap has = this.value();
        final JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap> function = this.createBiFunction();

        this.applyAndCheck(function,
                has.toJsonNode(this.toJsonNodeContext()),
                this.fromJsonNodeContext(),
                has);
    }

    @Test
    public void testApplyTwice() {
        final TestJsonNodeMap has1 = this.value();
        final JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap> function = this.createBiFunction();

        final ToJsonNodeContext context = this.toJsonNodeContext();

        this.applyAndCheck(function,
                has1.toJsonNode(context),
                this.fromJsonNodeContext(),
                has1);

        final TestJsonNodeMap has2 = TestJsonNodeMap.with("test-JsonNodeMap-b2");
        this.applyAndCheck(function,
                has2.toJsonNode(context),
                this.fromJsonNodeContext(),
                has2);
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
        final JsonObjectNode object = this.objectWithType(TestJsonNodeMap.class);
        this.toStringAndCheck(this.createBiFunction(object), this.typeNameProperty() + " in " + object);
    }

    @Override
    public JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap> createBiFunction() {
        return this.createBiFunction(this.objectWithType(TestJsonNodeMap.class));
    }

    public JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap> createBiFunction(final JsonObjectNode objectWithType) {
        final Class<TestJsonNodeMap> type = this.valueType();

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

    private TestJsonNodeMap value() {
        return TestJsonNodeMap.with("test-JsonNodeMap-a1");
    }

    private Class<TestJsonNodeMap> valueType() {
        return TestJsonNodeMap.class;
    }

    private FromJsonNodeContext fromJsonNodeContext() {
        return FromJsonNodeContext.basic();
    }

    private ToJsonNodeContext toJsonNodeContext() {
        return ToJsonNodeContext.basic();
    }

    @Override
    public Class<JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction<TestJsonNodeMap>> type() {
        return Cast.to(JsonNodeNameFromJsonNodeWithTypeFactoryBiFunction.class);
    }
}
