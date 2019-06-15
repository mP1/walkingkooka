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
package walkingkooka.tree.json.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.json.JsonBooleanNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class JsonNodeBooleanParserTokenTest extends JsonNodeLeafParserTokenTestCase2<JsonNodeBooleanParserToken, Boolean, JsonBooleanNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final JsonNodeBooleanParserToken token = this.createToken();

        new FakeJsonNodeParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("2");
            }

            @Override
            protected Visiting startVisit(final JsonNodeParserToken t) {
                assertSame(token, t);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final JsonNodeParserToken t) {
                assertSame(token, t);
                b.append("4");
            }

            @Override
            protected void visit(final JsonNodeBooleanParserToken t) {
                assertSame(token, t);
                b.append("5");
            }
        }.accept(token);
        assertEquals("13542", b.toString());
    }

    @Override
    public String text() {
        return String.valueOf(this.value());
    }

    @Override
    Boolean value() {
        return Boolean.TRUE;
    }

    @Override
    JsonBooleanNode jsonNode() {
        return JsonNode.booleanNode(value());
    }

    @Override
    JsonNodeBooleanParserToken createToken(final Boolean value, final String text) {
        return JsonNodeBooleanParserToken.with(value, text);
    }

    @Override
    public JsonNodeBooleanParserToken createDifferentToken() {
        return JsonNodeBooleanParserToken.with(false, "'different'");
    }

    @Override
    public Class<JsonNodeBooleanParserToken> type() {
        return JsonNodeBooleanParserToken.class;
    }
}
