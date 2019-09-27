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
import walkingkooka.tree.json.JsonNodeException;

import java.math.RoundingMode;
import java.util.Arrays;

public final class BasicJsonMarshallerTypedRoundingModeTest extends BasicJsonMarshallerTypedTestCase<BasicJsonMarshallerTypedRoundingMode, RoundingMode> {

    @Test
    public final void testFromBooleanFails() {
        this.unmarshallFailed(JsonNode.booleanNode(true), JsonNodeException.class);
    }

    @Test
    public final void testFromNumberFails() {
        this.unmarshallFailed(JsonNode.number(1.5), JsonNodeException.class);
    }

    @Test
    public final void testFromObjectFails() {
        this.unmarshallFailed(JsonNode.object(), JsonNodeException.class);
    }

    @Test
    public final void testFromArrayFails() {
        this.unmarshallFailed(JsonNode.array(), JsonNodeException.class);
    }

    @Test
    public final void testFromStringEmptyFails() {
        this.unmarshallFailed(JsonNode.string(""), IllegalArgumentException.class);
    }

    @Test
    public void testFromString() {
        Arrays.stream(RoundingMode.values())
                .forEach(r -> this.unmarshallAndCheck(JsonNode.string(r.name()), r));
    }

    @Test
    public void testTo() {
        Arrays.stream(RoundingMode.values())
                .forEach(r -> this.toJsonNodeAndCheck(r, JsonNode.string(r.name())));
    }

    @Override
    BasicJsonMarshallerTypedRoundingMode marshaller() {
        return BasicJsonMarshallerTypedRoundingMode.instance();
    }

    @Override
    RoundingMode value() {
        return RoundingMode.FLOOR;
    }

    @Override
    JsonNode node() {
        return JsonNode.string(this.value().name());
    }

    @Override
    RoundingMode jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "rounding-mode";
    }

    @Override
    Class<RoundingMode> marshallerType() {
        return RoundingMode.class;
    }

    @Override
    public Class<BasicJsonMarshallerTypedRoundingMode> type() {
        return BasicJsonMarshallerTypedRoundingMode.class;
    }
}
