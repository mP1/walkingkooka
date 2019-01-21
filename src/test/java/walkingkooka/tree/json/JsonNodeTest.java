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
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.type.MemberVisibility;

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

    @Override
    protected Class<JsonNode> type() {
        return JsonNode.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
