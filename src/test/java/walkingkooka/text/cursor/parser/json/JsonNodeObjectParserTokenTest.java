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

import java.util.List;

public final class JsonNodeObjectParserTokenTest extends JsonNodeParentParserTokenTestCase<JsonNodeObjectParserToken> {

    @Test
    public void testWithoutWhitespace() {
        final JsonNodeParserToken key = string("key");
        final JsonNodeParserToken value = string("value");
        final JsonNodeObjectParserToken object = object(objectBegin(), whitespace(), key, objectAssignment(), value, objectEnd()).cast();
        final JsonNodeObjectParserToken without = object.withoutSymbolsOrWhitespace().get().cast();
        assertEquals("value", Lists.of(key, value), without.value());
    }

    @Override
    JsonNodeObjectParserToken createToken(final String text, final List<ParserToken> tokens) {
        return JsonNodeObjectParserToken.with(tokens, text);
    }

    @Override
    String text() {
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
