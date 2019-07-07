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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HasJsonNodeOptionalMapperTest extends HasJsonNodeTypedMapperTestCase<HasJsonNodeOptionalMapper, Optional<?>> {

    @Test
    public void testFromStringFails() {
        this.fromJsonNodeFailed(JsonNode.string("a1"), JsonNodeException.class);
    }

    @Test
    public void testFromObjectFails() {
        this.fromJsonNodeFailed(JsonNode.object(), JsonNodeException.class);
    }

    @Test
    public void testFromArrayTwoElementsFails() {
        this.fromJsonNodeFailed(JsonNode.array()
                        .appendChild(JsonNode.number(1))
                        .appendChild(JsonNode.number(2)),
                JsonNodeException.class);
    }

    @Test
    public void testFromEmptyArray() {
        this.fromJsonNodeAndCheck(JsonNode.array(), Optional.empty());
    }

    @Test
    public void testFromArrayWithBooleanTrue() {
        this.fromJsonNodeAndCheck(JsonNode.array().appendChild(JsonNode.booleanNode(true)),
                Optional.of(true));
    }

    @Test
    public void testFromArrayWithBooleanFalse() {
        this.fromJsonNodeAndCheck(JsonNode.array().appendChild(JsonNode.booleanNode(false)),
                Optional.of(false));
    }

    @Test
    public void testFromArrayWithNumber() {
        this.fromJsonNodeAndCheck(JsonNode.array().appendChild(JsonNode.number(1.5)),
                Optional.of(1.5));
    }

    @Test
    public void testFromArrayWithString() {
        this.fromJsonNodeAndCheck(JsonNode.array().appendChild(JsonNode.string("abc123")),
                Optional.of("abc123"));
    }

    @Test
    public void testToJsonEmptyOptional() {
        this.toJsonNodeAndCheck(Optional.empty(), JsonNode.array());
    }

    @Test
    public void testToJsonBooleanTrue() {
        this.toJsonNodeAndCheck2(true, JsonNode.booleanNode(true));
    }

    @Test
    public void testToJsonBooleanFalse() {
        this.toJsonNodeAndCheck2(false, JsonNode.booleanNode(false));
    }

    @Test
    public void testToJsonDouble() {
        this.toJsonNodeAndCheck2(1.5, JsonNode.number(1.5));
    }

    @Test
    public void testToJsonString() {
        this.toJsonNodeAndCheck2("abc123", JsonNode.string("abc123"));
    }

    private void toJsonNodeAndCheck2(final Object value, final JsonNode arrayElement) {
        this.toJsonNodeAndCheck(Optional.of(value),
                JsonNode.array().appendChild(arrayElement));
    }

    @Test
    public void testRoundtripEmpty() {
        this.roundtripAndCheck(Optional.empty());
    }

    @Test
    public void testRoundtripBooleanTrue() {
        this.roundtripAndCheck(Optional.of(true));
    }

    @Test
    public void testRoundtripBooleanFalse() {
        this.roundtripAndCheck(Optional.of(false));
    }

    @Test
    public void testRoundtripNumber() {
        this.roundtripAndCheck(Optional.of(2.5));
    }

    @Test
    public void testRoundtripString() {
        this.roundtripAndCheck(Optional.of("a1"));
    }

    @Test
    public void testRoundtripBigDecimal() {
        this.roundtripAndCheck(Optional.of(BigDecimal.valueOf(123)));
    }

    @Test
    public void testRoundtripJsonObjectWithProperties() {
        this.roundtripAndCheck(Optional.of(JsonNode.object()
                .set(JsonNodeName.with("country"), JsonNode.string("australia"))
                .set(JsonNodeName.with("year"), JsonNode.number(2019))
        ));
    }

    @Test
    public void testRoundtripListBooleanNumberStringBigDecimal() {
        this.roundtripAndCheck(Optional.of(Lists.of(true, 1.0, "a1", BigDecimal.valueOf(123))));
    }

    @Test
    public void testRoundtripTestHasJsonNode() {
        this.roundtripAndCheck(Optional.of(TestHasJsonNode.with("Test123")));
    }

    private void roundtripAndCheck(final Optional<?> value) {
        final HasJsonNodeOptionalMapper mapper = this.mapper();
        final JsonNode json = mapper.toJsonNode(value);
        final Optional<?> from = mapper.fromJsonNode(json);

        assertEquals(value, from, () -> "json\n" + json);

        final JsonNode jsonWithType = HasJsonNode.toJsonNodeWithType(value);
        final Optional<?> from2 = jsonWithType.fromJsonNodeWithType();

        assertEquals(value, from2, () -> "jsonWithType\n" + jsonWithType);
    }

    @Override
    HasJsonNodeOptionalMapper mapper() {
        return HasJsonNodeOptionalMapper.instance();
    }

    @Override
    Optional<?> value() {
        return Optional.of(JAVA_VALUE);
    }

    @Override
    JsonNode node() {
        return JsonNode.array().appendChild(JsonNode.string(JAVA_VALUE));
    }

    private final static String JAVA_VALUE = "abc123";

    @Override
    Optional<?> jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "optional";
    }

    @Override
    Class<Optional<?>> mapperType() {
        return Cast.to(Optional.class);
    }

    @Override
    public Class<HasJsonNodeOptionalMapper> type() {
        return HasJsonNodeOptionalMapper.class;
    }
}
