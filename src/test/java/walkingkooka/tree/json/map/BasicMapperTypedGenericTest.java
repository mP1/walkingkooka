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

package walkingkooka.tree.json.map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;

public final class BasicMapperTypedGenericTest extends BasicMapperTypedTestCase<BasicMapperTypedGeneric<TestJsonNodeMap>, TestJsonNodeMap> {

    @BeforeAll
    public static void beforeAll() {
        TestJsonNodeMap.register();
    }

    @AfterAll
    public static void afterAll() {
        TestJsonNodeMap.unregister();
    }

    @Override
    BasicMapperTypedGeneric<TestJsonNodeMap> mapper() {
        return BasicMapperTypedGeneric.with(TestJsonNodeMap.TYPE_NAME,
                TestJsonNodeMap::fromJsonNode,
                TestJsonNodeMap::toJsonNode,
                TestJsonNodeMap.class);
    }

    @Override
    TestJsonNodeMap value() {
        return TestJsonNodeMap.with("a1");
    }

    @Override
    JsonNode node() {
        return this.toJsonNodeContext().toJsonNode(this.value());
    }

    @Override
    TestJsonNodeMap jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return TestJsonNodeMap.TYPE_NAME;
    }

    @Override
    Class<TestJsonNodeMap> mapperType() {
        return TestJsonNodeMap.class;
    }

    @Override
    public Class<BasicMapperTypedGeneric<TestJsonNodeMap>> type() {
        return Cast.to(BasicMapperTypedGeneric.class);
    }
}
