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

public final class BasicJsonMarshallerTypedNumberFloatTest extends BasicJsonMarshallerTypedNumberTestCase<BasicJsonMarshallerTypedNumberFloat, Float> {

    @Test
    public void testFromJsonNodeInvalidFails() {
        this.unmarshallFailed(JsonNode.number(Double.MAX_VALUE), NumericLossJsonNodeException.class);
    }

    @Override
    BasicJsonMarshallerTypedNumberFloat marshaller() {
        return BasicJsonMarshallerTypedNumberFloat.instance();
    }

    @Override
    Float value() {
        return 123.5f;
    }

    @Override
    JsonNode node() {
        return JsonNode.number(this.value());
    }

    @Override
    Float jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "float";
    }

    @Override
    Class<Float> marshallerType() {
        return Float.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedNumberFloat> type() {
        return BasicJsonMarshallerTypedNumberFloat.class;
    }
}
