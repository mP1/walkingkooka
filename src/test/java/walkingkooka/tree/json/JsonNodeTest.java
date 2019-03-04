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
import walkingkooka.type.MemberVisibility;

public final class JsonNodeTest implements ClassTesting2<JsonNode>,
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

    // ClassTesting.............................................................................................

    @Override
    public Class<JsonNode> type() {
        return JsonNode.class;
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
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
