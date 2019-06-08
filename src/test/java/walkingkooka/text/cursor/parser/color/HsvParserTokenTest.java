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
import walkingkooka.color.Hsv;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.HasSearchNode;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HsvParserTokenTest extends ColorHslOrHsvParserTokenTestCase<HsvParserToken> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            HsvParserToken.with(null, "hsv(359,50%,100%)");
        });
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final HsvParserToken token = this.createToken();

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
            protected void visit(final HsvParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToSearchNode() {
        final String text = "hsv(359,50%,100%)";
        this.toSearchNodeAndCheck(HsvParserToken.with(Hsv.parseHsv(text), text),
                SearchNode.text(text, text));
    }

    private void toSearchNodeAndCheck(final HasSearchNode has,
                                      final SearchNode searchNode) {
        assertEquals(searchNode,
                has.toSearchNode(),
                () -> "to search node " + has);
    }

    @Override
    public HsvParserToken createToken(final String text) {
        return HsvParserToken.with(Hsv.parseHsv(text), text);
    }

    @Override
    public String text() {
        return "hsv(123,0%,25%)";
    }

    @Override
    public HsvParserToken createDifferentToken() {
        return HsvParserToken.with(Hsv.parseHsv("hsv(359,50%,100%)"), "hsv(359,50%,100%)");
    }

    @Override
    public Class<HsvParserToken> type() {
        return HsvParserToken.class;
    }
}
