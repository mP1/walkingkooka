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

public final class HasJsonNodeByteMapperTest extends HasJsonNodeTypedNumberMapperTestCase<HasJsonNodeByteMapper, Byte> {

    @Test
    public void testFromJsonNodeInvalidFails() {
        this.fromJsonNodeFailed(JsonNode.number(Double.MAX_VALUE), NumericLossJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeInvalidFails2() {
        this.fromJsonNodeFailed(JsonNode.number(1.5), NumericLossJsonNodeException.class);
    }

    @Override
    HasJsonNodeByteMapper mapper() {
        return HasJsonNodeByteMapper.instance();
    }

    @Override
    Byte value() {
        return 123;
    }

    @Override
    JsonNode node() {
        return JsonNode.number(this.value());
    }

    @Override
    Byte jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "byte";
    }

    @Override
    Class<Byte> mapperType() {
        return Byte.class;
    }

    @Override
    public Class<HasJsonNodeByteMapper> type() {
        return HasJsonNodeByteMapper.class;
    }
}
