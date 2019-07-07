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
import walkingkooka.collect.set.Sets;

import java.util.Set;

public final class HasJsonNodeSetMapperTest extends HasJsonNodeTypedMapperTestCase<HasJsonNodeSetMapper, Set<?>> {

    @Test
    public void testFromEmptyArray() {
        this.fromJsonNodeAndCheck(JsonNode.array(), Sets.empty());
    }

    @Test
    public void testToEmptyList() {
        this.toJsonNodeWithTypeAndCheck(Sets.empty(), this.typeAndValue(JsonNode.array()));
    }

    @Override
    public void testRoundtripToJsonNodeObjectFromJsonNodeWithType() {
        // ignore
    }

    @Override
    HasJsonNodeSetMapper mapper() {
        return HasJsonNodeSetMapper.instance();
    }

    @Override
    Set<?> value() {
        return Sets.of(null, true, 123.5, "abc123", TestHasJsonNode.with("test-HasJsonNode-a1"));
    }

    @Override
    JsonNode node() {
        return JsonNode.array()
                .appendChild(JsonNode.nullNode())
                .appendChild(JsonNode.booleanNode(true))
                .appendChild(JsonNode.number(123.5))
                .appendChild(JsonNode.string("abc123"))
                .appendChild(TestHasJsonNode.with("test-HasJsonNode-a1").toJsonNodeWithType());
    }

    @Override
    Set<?> jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "set";
    }

    @Override
    Class<Set<?>> mapperType() {
        return Cast.to(Set.class);
    }

    @Override
    public Class<HasJsonNodeSetMapper> type() {
        return HasJsonNodeSetMapper.class;
    }
}
