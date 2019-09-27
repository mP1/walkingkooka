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
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;

public final class BasicJsonMarshallerTypedGenericTest extends BasicJsonMarshallerTypedTestCase<BasicJsonMarshallerTypedGeneric<TestJsonNodeValue>, TestJsonNodeValue> {

    @BeforeAll
    public static void beforeAll() {
        TestJsonNodeValue.register();
    }

    @AfterAll
    public static void afterAll() {
        TestJsonNodeValue.unregister();
    }

    @Override
    BasicJsonMarshallerTypedGeneric<TestJsonNodeValue> marshaller() {
        return BasicJsonMarshallerTypedGeneric.with(TestJsonNodeValue.TYPE_NAME,
                TestJsonNodeValue::unmarshall,
                TestJsonNodeValue::marshall,
                TestJsonNodeValue.class);
    }

    @Override
    TestJsonNodeValue value() {
        return TestJsonNodeValue.with("a1");
    }

    @Override
    JsonNode node() {
        return this.marshallContext().marshall(this.value());
    }

    @Override
    TestJsonNodeValue jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return TestJsonNodeValue.TYPE_NAME;
    }

    @Override
    Class<TestJsonNodeValue> marshallerType() {
        return TestJsonNodeValue.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedGeneric<TestJsonNodeValue>> type() {
        return Cast.to(BasicJsonMarshallerTypedGeneric.class);
    }
}
