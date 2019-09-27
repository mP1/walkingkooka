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

public final class BasicJsonMarshallerTypedNumberIntegerTest extends BasicJsonMarshallerTypedNumberTestCase<BasicJsonMarshallerTypedNumberInteger, Integer> {

    @Test
    public void testFromJsonNodeInvalidFails() {
        this.unmarshallFailed(JsonNode.number(Double.MAX_VALUE), NumericLossJsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeInvalidFails2() {
        this.unmarshallFailed(JsonNode.number(1.5), NumericLossJsonNodeException.class);
    }

    @Override
    BasicJsonMarshallerTypedNumberInteger marshaller() {
        return BasicJsonMarshallerTypedNumberInteger.instance();
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
    Class<Integer> marshallerType() {
        return Integer.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedNumberInteger> type() {
        return BasicJsonMarshallerTypedNumberInteger.class;
    }
}
