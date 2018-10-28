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

package walkingkooka.text.cursor.parser.json;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class JsonNodeObjectParserTokenTest extends JsonNodeParentParserTokenTestCase<JsonNodeObjectParserToken> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithArrayKeyFails() {
        JsonNodeObjectParserToken.with(Lists.of(array()), "{[]}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithBooleanKeyFails() {
        JsonNodeObjectParserToken.with(Lists.of(booleanToken(true)), "{true}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullKeyFails() {
        JsonNodeObjectParserToken.with(Lists.of(nul()), "{null}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithMNumberKeyFails() {
        JsonNodeObjectParserToken.with(Lists.of(number(123)), "{123}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithObjectKeyFails() {
        JsonNodeObjectParserToken.with(Lists.of(object()), "{{}}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithMissingValueFails() {
        JsonNodeObjectParserToken.with(Lists.of(string("key")), "{\"key\":}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithMissingValueFails2() {
        JsonNodeObjectParserToken.with(Lists.of(string("key1"), number(123), string("key2")), "{\"key1\":123,\"key2\"}");
    }

    @Test
    public void testWithoutWhitespace() {
        final JsonNodeParserToken key = string("key");
        final JsonNodeParserToken value = string("value");
        final JsonNodeObjectParserToken object = object(objectBegin(), whitespace(), key, objectAssignment(), value, objectEnd()).cast();
        final JsonNodeObjectParserToken without = object.withoutSymbols().get().cast();
        assertEquals("value", Lists.of(key, value), without.value());
    }

    @Test
    public void testToJsonNodeEmpty() {
        assertEquals(Optional.of(JsonNode.object()), JsonNodeObjectParserToken.with(Lists.empty(), "{}").toJsonNode());
    }

    @Test
    public void testToJsonNode() {
        assertEquals(Optional.of(JsonNode.object().set(JsonNodeName.with("key1"), JsonNode.number(123))),
                object(string("key1"), number(123))
                        .toJsonNode());
    }

    @Test
    public void testArrayWithObjectToJsonNode() {
        final JsonNodeParserToken objectToken = object(string("key1"), number(123));
        final JsonNodeParserToken arrayToken = array(objectToken);

        final JsonObjectNode objectNode = JsonNode.object().set(JsonNodeName.with("key1"), JsonNode.number(123));
        final JsonArrayNode arrayNode = JsonNode.array().appendChild(objectNode);

        assertEquals(Optional.of(arrayNode), arrayToken.toJsonNode());
    }

    @Test
    public void testToJsonNodeWhitespace() {
        assertEquals(Optional.of(JsonNode.object().set(JsonNodeName.with("key1"), JsonNode.number(123))),
                object(whitespace(), string("key1"), whitespace(), number(123))
                        .toJsonNode());
    }

    @Override
    JsonNodeObjectParserToken createToken(final String text, final List<ParserToken> tokens) {
        return JsonNodeObjectParserToken.with(tokens, text);
    }

    @Override
    protected String text() {
        return "{\"key\":\"value\"}";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(objectBegin(), string("key"), objectAssignment(), string("value"), objectEnd());
    }

    @Override
    protected JsonNodeObjectParserToken createDifferentToken() {
        return object(objectBegin(), string("different-key"), objectAssignment(), string("different-value"), objectEnd()).cast();
    }

    @Override
    protected Class<JsonNodeObjectParserToken> type() {
        return JsonNodeObjectParserToken.class;
    }
}
