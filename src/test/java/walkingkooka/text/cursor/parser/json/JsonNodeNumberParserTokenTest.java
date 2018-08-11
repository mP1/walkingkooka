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
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNumberNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.Assert.assertEquals;

public final class JsonNodeNumberParserTokenTest extends JsonNodeLeafParserTokenTestCase2<JsonNodeNumberParserToken, Double, JsonNumberNode> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final JsonNodeNumberParserToken token = this.createToken();

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
            protected void visit(final JsonNodeNumberParserToken t) {
                assertSame(token, t);
                b.append("5");
            }
        }.accept(token);
        assertEquals("13542", b.toString());
    }

    @Override
    String text() {
        return String.valueOf(this.value());
    }

    @Override
    Double value() {
        return 123.5;
    }

    @Override
    JsonNumberNode jsonNode() {
        return JsonNode.number(value());
    }

    @Override
    protected JsonNodeNumberParserToken createToken(final Double value, final String text) {
        return JsonNodeNumberParserToken.with(value, text);
    }

    @Override
    protected JsonNodeNumberParserToken createDifferentToken() {
        return JsonNodeNumberParserToken.with(-999, "-999");
    }

    @Override
    protected Class<JsonNodeNumberParserToken> type() {
        return JsonNodeNumberParserToken.class;
    }
}
