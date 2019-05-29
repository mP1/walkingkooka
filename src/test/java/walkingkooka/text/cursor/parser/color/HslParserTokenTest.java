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
package walkingkooka.text.cursor.parser.color;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Hsl;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.HasSearchNode;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HslParserTokenTest extends ColorHslOrHsvParserTokenTestCase<HslParserToken> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            HslParserToken.with(null, "hsl(60,100%,100%)");
        });
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final HslParserToken token = this.createToken();

        new FakeColorParserTokenVisitor() {
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
            protected void visit(final HslParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToSearchNode() {
        final String text = "hsl(60,100%,100%)";
        this.toSearchNodeAndCheck(HslParserToken.with(Hsl.parseHsl(text), text),
                SearchNode.text(text, text));
    }

    private void toSearchNodeAndCheck(final HasSearchNode has,
                                      final SearchNode searchNode) {
        assertEquals(searchNode,
                has.toSearchNode(),
                () -> "to search node " + has);
    }

    @Override
    public HslParserToken createToken(final String text) {
        return HslParserToken.with(Hsl.parseHsl(text), text);
    }

    @Override
    public String text() {
        return "hsl(1,2%,3%)";
    }

    @Override
    public HslParserToken createDifferentToken() {
        return HslParserToken.with(Hsl.parseHsl("hsl(359,50%,25%)"), "hsl(359,50%,25%)");
    }

    @Override
    public Class<HslParserToken> type() {
        return HslParserToken.class;
    }
}
