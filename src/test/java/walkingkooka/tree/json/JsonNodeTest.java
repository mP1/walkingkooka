/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.tree.json;

import org.junit.Test;
import walkingkooka.color.Color;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class JsonNodeTest extends ClassTestCase<JsonNode> {

    @Test(expected = ParserException.class)
    public void testParseFails() {
        JsonNode.parse("");
    }

    @Test(expected = ParserException.class)
    public void testParseFails2() {
        JsonNode.parse("{\"");
    }

    @Test
    public void testParseBoolean() {
        this.parseAndCheck("true",
                JsonNode.booleanNode(true));
    }

    @Test
    public void testParseNull() {
        this.parseAndCheck("null",
                JsonNode.nullNode());
    }

    @Test
    public void testParseNumber() {
        this.parseAndCheck("123.5",
                JsonNode.number(123.5));
    }

    @Test
    public void testParseString() {
        this.parseAndCheck("\"abc123\"",
                JsonNode.string("abc123"));
    }

    @Test
    public void testParseArray() {
        this.parseAndCheck("[\"abc123\", true]",
                JsonNode.array()
                        .appendChild(JsonNode.string("abc123"))
                        .appendChild(JsonNode.booleanNode(true)));
    }

    @Test
    public void testParseObject() {
        this.parseAndCheck("{\"prop1\": \"value1\"}",
                JsonNode.object().set(JsonNodeName.with("prop1"), JsonNode.string("value1")));
    }

    @Test
    public void testParseObjectManyProperties() {
        this.parseAndCheck("{\"prop1\": \"value1\", \"prop2\": \"value2\"}",
                JsonNode.object()
                        .set(JsonNodeName.with("prop1"), JsonNode.string("value1"))
                        .set(JsonNodeName.with("prop2"), JsonNode.string("value2")));
    }

    @Test
    public void testParseObjectOrderUnimportant() {
        this.parseAndCheck("{\"prop1\": \"value1\", \"prop2\": \"value2\"}",
                JsonNode.object()
                        .set(JsonNodeName.with("prop2"), JsonNode.string("value2"))
                        .set(JsonNodeName.with("prop1"), JsonNode.string("value1")));
    }

    private void parseAndCheck(final String json, final JsonNode node) {
        assertEquals("Parse result incorrect for " + CharSequences.quoteAndEscape(json),
                JsonNode.parse(json),
                node);
    }

    // wrap....................................................................................................

    @Test
    public void testWrapNull() {
        this.wrapAndCheck(null, JsonNode.nullNode());
    }

    @Test
    public void testWrapBooleanTrue() {
        this.wrapAndCheck(true, JsonNode.booleanNode(true));
    }

    @Test
    public void testWrapBooleanFalse() {
        this.wrapAndCheck(false, JsonNode.booleanNode(false));
    }

    @Test
    public void testWrapInteger() {
        this.wrapAndCheck(123, JsonNode.number(123));
    }

    @Test
    public void testWrapDouble() {
        this.wrapAndCheck(123.5, JsonNode.number(123.5));
    }

    @Test
    public void testWrapString() {
        this.wrapAndCheck("abc", JsonNode.string("abc"));
    }

    @Test
    public void testWrapOptionalEmpty() {
        this.wrapAndCheck(Optional.empty());
    }

    @Test
    public void testWrapOptionalString() {
        this.wrapAndCheck(Optional.of("abc"), JsonNode.string("abc"));
    }

    @Test
    public void testWrapJsonNode() {
        final JsonNode jsonNode = JsonNode.string("abc123");
        this.wrapAndCheck(jsonNode, jsonNode);
    }

    @Test
    public void testWrapHasJsonNode() {
        final Color color = Color.fromRgb(0x123456);
        this.wrapAndCheck(color, color.toJsonNode());
    }

    @Test
    public void testWrapUnsupported() {
        this.wrapAndCheck(this);
    }

    private void wrapAndCheck(final Object value) {
        this.wrapAndCheck(value, Optional.empty());
    }

    private void wrapAndCheck(final Object value, final JsonNode node) {
        this.wrapAndCheck(value, Optional.of(node));
    }


    private void wrapAndCheck(final Object value, final Optional<JsonNode> node) {
        assertEquals("With value " + CharSequences.quoteIfChars(value),
                node,
                JsonNode.wrap(value));
    }

    @Override
    protected Class<JsonNode> type() {
        return JsonNode.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
