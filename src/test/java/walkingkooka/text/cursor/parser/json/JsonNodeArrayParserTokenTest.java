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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.json.JsonNode;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JsonNodeArrayParserTokenTest extends JsonNodeParentParserTokenTestCase<JsonNodeArrayParserToken> {

    @Test
    public void testWithoutWhitespace() {
        final JsonNodeArrayParserToken array = array(arrayBegin(), whitespace(), string("abc"), arrayEnd()).cast();
        final JsonNodeArrayParserToken without = array.withoutSymbols().get().cast();
        assertEquals(Lists.of(string("abc")), without.value(), "value");
    }

    @Test
    public void testToJsonNodeEmpty() {
        assertEquals(Optional.of(JsonNode.array()), JsonNodeParserToken.array(Lists.empty(), "[]").toJsonNode());
    }

    @Test
    public void testToJsonNode() {
        assertEquals(Optional.of(JsonNode.array().appendChild(JsonNode.number(123))),
                array(number(123)).toJsonNode());
    }

    @Test
    public void testToJsonNodeWhitespace() {
        assertEquals(Optional.of(JsonNode.array().appendChild(JsonNode.number(123))),
                array(number(123), whitespace(), whitespace()).toJsonNode());
    }

    @Override
    JsonNodeArrayParserToken createToken(final String text, final List<ParserToken> tokens) {
        return JsonNodeArrayParserToken.with(tokens, text);
    }

    @Override
    protected String text() {
        return "[true,null,\"abc\"]";
    }

    @Override
    List<ParserToken> tokens() {
        return Lists.of(arrayBegin(), booleanTrue(), separator(), nul(), separator(), string("abc"), arrayEnd());
    }

    @Override
    protected JsonNodeArrayParserToken createDifferentToken() {
        return array(arrayBegin(), string("different"), arrayEnd()).cast();
    }

    @Override
    public Class<JsonNodeArrayParserToken> type() {
        return JsonNodeArrayParserToken.class;
    }
}
