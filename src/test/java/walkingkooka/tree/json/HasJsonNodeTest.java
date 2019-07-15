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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HasJsonNodeTest implements ClassTesting<HasJsonNode> {

    @Test
    public void testRequiredMissingProperty() {
        final FromJsonNodeException thrown = assertThrows(FromJsonNodeException.class, () -> {
            HasJsonNode.requiredPropertyMissing(JsonNodeName.with("required-1a"), JsonNode.object());
        });
        assertEquals("Required property \"required-1a\" missing={}", thrown.getMessage());
    }

    @Test
    public void testUnknownPropertyPresent() {
        final FromJsonNodeException thrown = assertThrows(FromJsonNodeException.class, () -> {
            HasJsonNode.unknownPropertyPresent(JsonNodeName.with("unknown-1a"), JsonNode.object());
        });
        assertEquals("Unknown property \"unknown-1a\" in {}", thrown.getMessage());
    }

    @Test
    public void testToJsonNodeObject() {
        final TestHasJsonNode has = TestHasJsonNode.with("text-HasJsonNode-a1");

        assertEquals(has.toJsonNode(),
                HasJsonNode.toJsonNodeObject(has));
    }

    @Test
    public void testToJsonNodeList() {
        final TestHasJsonNode has = TestHasJsonNode.with("text-HasJsonNode-a1");

        assertEquals(JsonNode.array()
                        .appendChild(has.toJsonNode()),
                HasJsonNode.toJsonNodeList(Lists.of(has)));
    }

    @Test
    public void testToJsonNodeSet() {
        final TestHasJsonNode has = TestHasJsonNode.with("text-HasJsonNode-a1");

        assertEquals(JsonNode.array()
                        .appendChild(has.toJsonNode()),
                HasJsonNode.toJsonNodeSet(Sets.of(has)));
    }

    @Test
    public void testToJsonNodeMap() {
        final TestHasJsonNode key = TestHasJsonNode.with("text-HasJsonNode-key");
        final TestHasJsonNode value = TestHasJsonNode.with("text-HasJsonNode-value");

        assertEquals(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapMapper.ENTRY_KEY, key.toJsonNode())
                                .set(HasJsonNodeMapMapper.ENTRY_VALUE, value.toJsonNode())),
                HasJsonNode.toJsonNodeMap(Maps.of(key, value)));
    }

    @Test
    public void testToJsonNodeWithTypeList() {
        final TestHasJsonNode has = TestHasJsonNode.with("text-HasJsonNode-a1");

        assertEquals(JsonNode.array()
                        .appendChild(has.toJsonNodeWithType()),
                HasJsonNode.toJsonNodeWithTypeList(Lists.of(has)));
    }

    @Test
    public void testToJsonNodeWithTypeSet() {
        final TestHasJsonNode has = TestHasJsonNode.with("text-HasJsonNode-a1");

        assertEquals(JsonNode.array()
                        .appendChild(has.toJsonNodeWithType()),
                HasJsonNode.toJsonNodeWithTypeSet(Sets.of(has)));
    }

    @Test
    public void testToJsonNodeWithTypeMap() {
        final TestHasJsonNode key = TestHasJsonNode.with("text-HasJsonNode-key");
        final TestHasJsonNode value = TestHasJsonNode.with("text-HasJsonNode-value");

        assertEquals(JsonNode.array()
                        .appendChild(JsonNode.object()
                                .set(HasJsonNodeMapMapper.ENTRY_KEY, key.toJsonNodeWithType())
                                .set(HasJsonNodeMapMapper.ENTRY_VALUE, value.toJsonNodeWithType())),
                HasJsonNode.toJsonNodeWithTypeMap(Maps.of(key, value)));
    }

    @Test
    public void testToJsonNodeWithType() {
        final TestHasJsonNode value = TestHasJsonNode.with("text-HasJsonNode-a1");

        assertEquals(value.toJsonNodeWithType(),
                HasJsonNode.toJsonNodeWithType(value));
    }

    @Override
    public Class<HasJsonNode> type() {
        return HasJsonNode.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
