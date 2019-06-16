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

import walkingkooka.Cast;

public final class HasJsonNodeHasJsonNodeMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeHasJsonNodeMapper<TestHasJsonNode>, TestHasJsonNode> {

    @Override
    HasJsonNodeHasJsonNodeMapper<TestHasJsonNode> mapper() {
        return HasJsonNodeHasJsonNodeMapper.with("test-HasJsonNode", TestHasJsonNode::fromJsonNode, TestHasJsonNode.class);
    }

    @Override
    TestHasJsonNode value() {
        return TestHasJsonNode.with("a1");
    }

    @Override
    boolean requiresTypeName() {
        return true;
    }

    @Override
    JsonNode node() {
        return this.value().toJsonNode();
    }

    @Override
    TestHasJsonNode jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "test-HasJsonNode";
    }

    @Override
    Class<TestHasJsonNode> mapperType() {
        return TestHasJsonNode.class;
    }

    @Override
    public Class<HasJsonNodeHasJsonNodeMapper<TestHasJsonNode>> type() {
        return Cast.to(HasJsonNodeHasJsonNodeMapper.class);
    }
}
