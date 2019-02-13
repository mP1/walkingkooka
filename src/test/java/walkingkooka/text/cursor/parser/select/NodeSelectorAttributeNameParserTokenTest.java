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
package walkingkooka.text.cursor.parser.select;

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class NodeSelectorAttributeNameParserTokenTest extends NodeSelectorNonSymbolParserTokenTestCase<NodeSelectorAttributeNameParserToken, NodeSelectorAttributeName> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final NodeSelectorAttributeNameParserToken token = this.createToken();

        new FakeNodeSelectorParserTokenVisitor() {
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
            protected Visiting startVisit(final NodeSelectorParserToken t) {
                assertSame(token, t);
                b.append("3");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final NodeSelectorParserToken t) {
                assertSame(token, t);
                b.append("4");
            }

            @Override
            protected void visit(final NodeSelectorAttributeNameParserToken t) {
                assertSame(token, t);
                b.append("5");
            }
        }.accept(token);
        assertEquals("13542", b.toString());
    }

    @Override
    public String text() {
        return "attribute";
    }

    @Override
    NodeSelectorAttributeName value() {
        return NodeSelectorAttributeName.with(this.text());
    }

    @Override
    NodeSelectorAttributeNameParserToken createToken(final NodeSelectorAttributeName value, final String text) {
        return NodeSelectorAttributeNameParserToken.with(value, text);
    }

    @Override
    public NodeSelectorAttributeNameParserToken createDifferentToken() {
        return NodeSelectorAttributeNameParserToken.with(NodeSelectorAttributeName.with("different"), "different");
    }

    @Override
    public Class<NodeSelectorAttributeNameParserToken> type() {
        return NodeSelectorAttributeNameParserToken.class;
    }
}
