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
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HasJsonNodeMapperJsonNodeVisitorTest implements JsonNodeVisitorTesting<HasJsonNodeMapperJsonNodeVisitor> {

    @Test
    public void testNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HasJsonNodeMapperJsonNodeVisitor.value(null);
        });
    }

    @Test
    public void testArrayFails() {
        assertThrows(FromJsonNodeException.class, () -> {
            HasJsonNodeMapperJsonNodeVisitor.value(JsonNode.array());
        });
    }

    @Test
    public void testBooleanTrue() {
        this.valueAndCheck(JsonNode.booleanNode(true), true);
    }

    @Test
    public void testBooleanFalse() {
        this.valueAndCheck(JsonNode.booleanNode(false), false);
    }

    @Test
    public void testNull() {
        this.valueAndCheck(JsonNode.nullNode(), null);
    }

    @Test
    public void testNumber() {
        final double number = 12.5;
        this.valueAndCheck(JsonNode.number(number), number);
    }

    @Test
    public void testObject() {
        final TestHasJsonNode object = TestHasJsonNode.with("abc123");
        this.valueAndCheck(object.toJsonNodeWithType(), object);
    }

    @Test
    public void testString() {
        final String string = "abc123";
        this.valueAndCheck(JsonNode.string(string), string);
    }

    private void valueAndCheck(final JsonNode node,
                               final Object expected) {
        assertEquals(expected,
                HasJsonNodeMapperJsonNodeVisitor.value(node),
                () -> "value " + node);
    }

    @Test
    public void testToString() {
        final HasJsonNodeMapperJsonNodeVisitor visitor = new HasJsonNodeMapperJsonNodeVisitor();
        visitor.accept(JsonNode.number(12.5));
        this.toStringAndCheck(visitor, String.valueOf(12.5));
    }

    @Override
    public HasJsonNodeMapperJsonNodeVisitor createVisitor() {
        return new HasJsonNodeMapperJsonNodeVisitor();
    }

    @Override
    public Class<HasJsonNodeMapperJsonNodeVisitor> type() {
        return HasJsonNodeMapperJsonNodeVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return HasJsonNodeMapper.class.getSimpleName();
    }
}
