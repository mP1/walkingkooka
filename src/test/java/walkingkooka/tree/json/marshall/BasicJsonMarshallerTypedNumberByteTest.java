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

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.NumericLossJsonNodeException;

public final class BasicJsonMarshallerTypedNumberByteTest extends BasicJsonMarshallerTypedNumberTestCase<BasicJsonMarshallerTypedNumberByte, Byte> {

    @Test
    public void testFromJsonNodeInvalidFails() {
        this.unmarshallFailed(JsonNode.number(Double.MAX_VALUE), NumericLossJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeInvalidFails2() {
        this.unmarshallFailed(JsonNode.number(1.5), NumericLossJsonNodeException.class);
    }

    @Override
    BasicJsonMarshallerTypedNumberByte marshaller() {
        return BasicJsonMarshallerTypedNumberByte.instance();
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
    Class<Byte> marshallerType() {
        return Byte.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedNumberByte> type() {
        return BasicJsonMarshallerTypedNumberByte.class;
    }
}
