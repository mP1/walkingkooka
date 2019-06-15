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

import java.time.LocalTime;

public final class HasJsonNodeLocalTimeMapperTest extends HasJsonNodeMapperTestCase2<HasJsonNodeLocalTimeMapper, LocalTime> {

    @Test
    public void testRoundtripNoon() {
        this.toJsonNodeWithTypeAndCheck(LocalTime.NOON, this.typeAndValue(JsonNode.string("12:00")));
    }

    @Test
    public void testRoundtripLocalTimeMAX() {
        this.toJsonNodeWithTypeAndCheck(LocalTime.MAX, this.typeAndValue(JsonNode.string(LocalTime.MAX.toString())));
    }

    @Test
    public void testFromEmptyStringFails() {
        this.fromJsonNodeFailed(JsonNode.string(""), JsonNodeException.class);
    }

    @Test
    public void testFromInvalidDateFails() {
        this.fromJsonNodeFailed(JsonNode.string("ABC123"), JsonNodeException.class);
    }

    @Test
    public void testFromHoursOnlyFails() {
        this.fromJsonNodeFailed(JsonNode.string("12"), JsonNodeException.class);
    }

    @Override
    HasJsonNodeLocalTimeMapper mapper() {
        return HasJsonNodeLocalTimeMapper.instance();
    }

    @Override
    LocalTime value() {
        return LocalTime.of(23, 58, 59, 999);
    }

    @Override
    boolean requiresTypeName() {
        return true;
    }

    @Override
    JsonNode node() {
        return JsonNode.string("23:58:59.000000999");
    }

    @Override
    LocalTime jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "local-time";
    }

    @Override
    Class<LocalTime> mapperType() {
        return LocalTime.class;
    }

    @Override
    public Class<HasJsonNodeLocalTimeMapper> type() {
        return HasJsonNodeLocalTimeMapper.class;
    }
}
