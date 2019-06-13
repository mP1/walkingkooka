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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.tree.HasTextOffsetTesting;
import walkingkooka.type.JavaVisibility;

public final class JsonNodeTest implements ClassTesting2<JsonNode>,
        HasTextOffsetTesting,
        ParseStringTesting<JsonNode> {

    @Test
    public void testParseIncompleteObjectFails() {
        this.parseFails("{\"", ParserException.class);
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

    // HasTextOffset.................................................................................................

    @Test
    public void testHasTextOffsetEmptyJsonObject() {
        this.textOffsetAndCheck(JsonObjectNode.object(), 0);
    }

    @Test
    public void testHasTextOffsetJsonObject() {
        this.textOffsetAndCheck(JsonObjectNode.object()
                        .set(JsonNodeName.with("key1"), JsonNode.string("a1"))
                        .set(JsonNodeName.with("key2"), JsonNode.string("b2"))
                        .set(JsonNodeName.with("key3"), JsonNode.string("c3"))
                        .set(JsonNodeName.with("key4"), JsonNode.string("d4"))
                        .children()
                        .get(2),
                "a1b2");
    }

    @Test
    public void testHasTextOffsetJsonArray() {
        this.textOffsetAndCheck(JsonObjectNode.array()
                        .appendChild(JsonNode.string("a1"))
                        .appendChild(JsonObjectNode.array()
                                .appendChild(JsonNode.string("b2"))
                                .appendChild(JsonNode.string("c3"))
                                .appendChild(JsonNode.string("d4")))
                        .appendChild(JsonNode.string("e5"))
                        .get(1)
                        .children()
                        .get(2),
                "a1b2c3");
    }

    // ClassTesting.............................................................................................

    @Override
    public Class<JsonNode> type() {
        return JsonNode.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public JsonNode parse(final String text) {
        return JsonNode.parse(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return ParserException.class;
    }
}
