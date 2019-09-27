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

public final class BasicJsonMarshallerNumberTest extends BasicJsonMarshallerTestCase2<BasicJsonMarshallerNumber, Number> {

    @Test
    public void testMarshallByte() {
        this.marshallAndCheck2(Byte.MAX_VALUE);
    }

    @Test
    public void testMarshallShort() {
        this.marshallAndCheck2(Short.MAX_VALUE);
    }

    @Test
    public void testMarshallInteger() {
        this.marshallAndCheck2(Integer.MAX_VALUE);
    }

    @Test
    public void testMarshallLong() {
        this.marshallAndCheck2(Long.MAX_VALUE);
    }

    @Test
    public void testMarshallFloat() {
        this.marshallAndCheck2(Float.MAX_VALUE);
    }

    @Test
    public void testMarshallDouble() {
        this.marshallAndCheck2(Double.MAX_VALUE);
    }

    private void marshallAndCheck2(final Number value) {
        this.marshallWithTypeAndCheck(value, JsonNode.number(value.doubleValue()));
    }

    @Override
    BasicJsonMarshallerNumber marshaller() {
        return BasicJsonMarshallerNumber.instance();
    }

    @Override
    Double value() {
        return 123.0;
    }

    @Override
    boolean requiresTypeName() {
        return false;
    }

    @Override
    JsonNode node() {
        return JsonNode.number(this.value());
    }

    @Override
    Number jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "number";
    }

    @Override
    Class<Number> marshallerType() {
        return Number.class;
    }

    @Override
    public Class<BasicJsonMarshallerNumber> type() {
        return BasicJsonMarshallerNumber.class;
    }
}
