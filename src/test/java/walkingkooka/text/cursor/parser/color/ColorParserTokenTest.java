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
import walkingkooka.color.Color;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.HasSearchNode;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ColorParserTokenTest extends ColorHslOrHsvParserTokenTestCase<ColorParserToken> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            ColorParserToken.with(null, "123");
        });
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final ColorParserToken token = this.createToken();

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
            protected void visit(final ColorParserToken t) {
                assertSame(token, t);
                b.append("3");
            }
        }.accept(token);
        assertEquals("132", b.toString());
    }

    @Test
    public void testToSearchNodeWithNamedColor() {
        final String text = "silver";
        this.toSearchNodeAndCheck(ColorParserToken.with(Color.parseColor(text), text),
                SearchNode.text(text, text));
    }

    @Test
    public void testToSearchNodeWithUnnamedColor() {
        final String text = "#987654";
        this.toSearchNodeAndCheck(ColorParserToken.with(Color.parseColor("#123456"), text),
                SearchNode.text(text, text));
    }

    private void toSearchNodeAndCheck(final HasSearchNode has,
                                      final SearchNode searchNode) {
        assertEquals(searchNode,
                has.toSearchNode(),
                () -> "to search node " + has);
    }

    @Override
    public ColorParserToken createToken(final String text) {
        return ColorParserToken.with(Color.parseColor(text), text);
    }

    @Override
    public String text() {
        return "silver";
    }

    @Override
    public ColorParserToken createDifferentToken() {
        return ColorParserToken.with(Color.parseColor("aqua"), "aqua");
    }

    @Override
    public Class<ColorParserToken> type() {
        return ColorParserToken.class;
    }
}
