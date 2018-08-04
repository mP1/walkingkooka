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
 */
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

public final class EbnfConcatenationParserTokenTest extends EbnfAlternativeConcatenationParentParserTokenTestCase<EbnfConcatenationParserToken> {

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final EbnfConcatenationParserToken concat = this.createToken();
        final EbnfIdentifierParserToken identifier1 = this.identifier1();
        final EbnfIdentifierParserToken identifier2 = this.identifier2();

        new FakeEbnfParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("1");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                b.append("2");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final EbnfParserToken t) {
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfParserToken t) {
                b.append("4");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final EbnfConcatenationParserToken t) {
                assertSame(concat, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfConcatenationParserToken t) {
                assertSame(concat, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfIdentifierParserToken t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(concat);
        assertEquals("1351374213742642", b.toString());
        assertEquals("visited",
                Lists.of(concat, concat, concat,
                        identifier1, identifier1, identifier1, identifier1, identifier1,
                        identifier2, identifier2, identifier2, identifier2, identifier2,
                        concat, concat, concat),
                visited);
    }
    
    @Override
    EbnfConcatenationParserToken createToken(final String text, final List<ParserToken> tokens) {
        return EbnfConcatenationParserToken.with(tokens, text);
    }

    @Override
    String text() {
        return "identifier1,identifier2)";
    }

    @Override
    char separatorChar() {
        return ',';
    }

    @Override
    EbnfConcatenationParserToken createTokenWithNoise() {
        return this.createToken(this.identifier1() + "  ," + this.identifier2().text(), this.identifier1(), whitespace(), this.identifier2());
    }

    @Override
    protected Class<EbnfConcatenationParserToken> type() {
        return EbnfConcatenationParserToken.class;
    }
}
