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

import org.junit.jupiter.api.Test;

public final class HasJsonNodeIntegerMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeIntegerMapper, Integer> {

    @Test
    public void testFromJsonNodeInvalidFails() {
        this.fromJsonNodeFailed(JsonNode.number(Double.MAX_VALUE), NumericLossJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeInvalidFails2() {
        this.fromJsonNodeFailed(JsonNode.number(1.5), NumericLossJsonNodeException.class);
    }

    @Override
    HasJsonNodeIntegerMapper mapper() {
        return HasJsonNodeIntegerMapper.instance();
    }

    @Override
    boolean requiresTypeName() {
        return true;
    }

    @Override
    Integer value() {
        return 123;
    }

    @Override
    JsonNode node() {
        return JsonNode.number(this.value());
    }

    @Override
    Integer jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "int";
    }

    @Override
    Class<Integer> mapperType() {
        return Integer.class;
    }

    @Override
    public Class<HasJsonNodeIntegerMapper> type() {
        return HasJsonNodeIntegerMapper.class;
    }
}
