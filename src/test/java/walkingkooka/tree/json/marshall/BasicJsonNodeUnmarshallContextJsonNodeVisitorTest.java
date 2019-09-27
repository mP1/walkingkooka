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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeVisitorTesting;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicJsonNodeUnmarshallContextJsonNodeVisitorTest implements JsonNodeVisitorTesting<BasicJsonNodeUnmarshallContextJsonNodeVisitor> {

    @AfterEach
    public void afterEach() {
        TestJsonNodeValue.unregister();
    }

    @Test
    public void testNullFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicJsonNodeUnmarshallContextJsonNodeVisitor.value(null, this.context());
        });
    }

    @Test
    public void testArrayFails() {
        assertThrows(JsonNodeUnmarshallException.class, () -> {
            BasicJsonNodeUnmarshallContextJsonNodeVisitor.value(JsonNode.array(), this.context());
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
        TestJsonNodeValue.register();

        final TestJsonNodeValue object = TestJsonNodeValue.with("abc123");
        this.valueAndCheck(JsonNodeMarshallContexts.basic().marshallWithType(object), object);
    }

    @Test
    public void testString() {
        final String string = "abc123";
        this.valueAndCheck(JsonNode.string(string), string);
    }

    private void valueAndCheck(final JsonNode node,
                               final Object expected) {
        assertEquals(expected,
                BasicJsonNodeUnmarshallContextJsonNodeVisitor.value(node, this.context()),
                () -> "value " + node);
    }

    @Test
    public void testToString() {
        final BasicJsonNodeUnmarshallContextJsonNodeVisitor visitor = new BasicJsonNodeUnmarshallContextJsonNodeVisitor(this.context());
        visitor.accept(JsonNode.number(12.5));
        this.toStringAndCheck(visitor, String.valueOf(12.5));
    }

    @Override
    public BasicJsonNodeUnmarshallContextJsonNodeVisitor createVisitor() {
        return new BasicJsonNodeUnmarshallContextJsonNodeVisitor(this.context());
    }

    private JsonNodeUnmarshallContext context() {
        return BasicJsonNodeUnmarshallContext.INSTANCE;
    }

    @Override
    public Class<BasicJsonNodeUnmarshallContextJsonNodeVisitor> type() {
        return BasicJsonNodeUnmarshallContextJsonNodeVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return BasicJsonNodeUnmarshallContext.class.getSimpleName();
    }
}
