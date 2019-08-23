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

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.NumericLossJsonNodeException;

public final class BasicMapperTypedNumberLongTest extends BasicMapperTypedTestCase<BasicMapperTypedNumberLong, Long> {

    @Test
    public final void testFromBooleanFails() {
        this.fromJsonNodeFailed(JsonNode.booleanNode(true), JsonNodeException.class);
    }

    @Test
    public final void testFromNumber() {
        this.fromJsonNodeAndCheck(JsonNode.number(123),
                123L);
    }

    @Test
    public final void testFromNumberDecimalFails() {
        this.fromJsonNodeFailed(JsonNode.number(123.5), NumericLossJsonNodeException.class);
    }

    @Test
    public final void testFromObjectFails() {
        this.fromJsonNodeFailed(JsonNode.object(), JsonNodeException.class);
    }

    @Test
    public final void testFromStringFails() {
        this.fromJsonNodeFailed(JsonNode.string("abc123"), NumberFormatException.class);
    }

    @Test
    public final void testFromArrayFails() {
        this.fromJsonNodeFailed(JsonNode.array(), JsonNodeException.class);
    }

    @Override
    BasicMapperTypedNumberLong mapper() {
        return BasicMapperTypedNumberLong.instance();
    }

    @Override
    Long value() {
        return 123L;
    }

    @Override
    JsonNode node() {
        return JsonNode.string(Long.toString(this.value()));
    }

    @Override
    Long jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "long";
    }

    @Override
    Class<Long> mapperType() {
        return Long.class;
    }

    @Override
    public Class<BasicMapperTypedNumberLong> type() {
        return BasicMapperTypedNumberLong.class;
    }
}
