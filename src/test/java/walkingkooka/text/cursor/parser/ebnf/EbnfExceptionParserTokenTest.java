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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EbnfExceptionParserTokenTest extends EbnfParentParserTokenTestCase2<EbnfExceptionParserToken> {

    @Test
    public final void testIncorrectTokenCountFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createToken(this.text(), this.identifier1(), this.comment2(), this.identifier2(), this.identifier("identifier3"));
        });
    }

    @Test
    public void testWithoutCommentsSymbolsWhitespace() {
        final EbnfParserToken identifier1 = this.identifier1();
        final EbnfParserToken comment2 = this.comment2();
        final EbnfParserToken identifier2 = this.identifier2();

        final EbnfExceptionParserToken token = this.createToken(this.text(), identifier1, comment2, identifier2);
        assertEquals(Lists.of(identifier1, comment2, identifier2), token.value(), "value");

        final EbnfExceptionParserToken without = token.withoutCommentsSymbolsOrWhitespace().get().cast();
        assertNotSame(token, without);
        assertEquals(Lists.of(identifier1, identifier2), without.value(), "value");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final EbnfExceptionParserToken exception = this.createToken();
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
            protected Visiting startVisit(final EbnfExceptionParserToken t) {
                assertSame(exception, t);
                b.append("5");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final EbnfExceptionParserToken t) {
                assertSame(exception, t);
                b.append("6");
                visited.add(t);
            }

            @Override
            protected void visit(final EbnfIdentifierParserToken t) {
                b.append("7");
                visited.add(t);
            }
        }.accept(exception);
        assertEquals("1351374213742642", b.toString());
        assertEquals(Lists.of(exception, exception, exception,
                identifier1, identifier1, identifier1, identifier1, identifier1,
                identifier2, identifier2, identifier2, identifier2, identifier2,
                exception, exception, exception),
                visited,
                "visited");
    }


    @Test
    public final void testToSearchNode() {
        final EbnfConcatenationParserToken token = EbnfConcatenationParserToken.with(
                Lists.of(this.identifier1(), EbnfParserToken.symbol("-", "-"), this.identifier2()),
                this.text());
        final SearchNode searchNode = token.toSearchNode();

        assertEquals(token.text(), searchNode.text(), "text");
    }

    @Override
    EbnfExceptionParserToken createToken(final String text, final List<ParserToken> tokens) {
        return EbnfExceptionParserToken.with(tokens, text);
    }

    @Override
    final List<ParserToken> tokens() {
        return Lists.of(this.identifier1(), this.identifier2());
    }

    @Override
    EbnfExceptionParserToken createTokenWithNoise() {
        return this.createToken(this.identifier1() + "  -" + this.identifier2().text(), this.identifier1(), whitespace(), this.identifier2());
    }

    @Override
    public EbnfExceptionParserToken createDifferentToken() {
        return this.createToken("different -identifier2", this.identifier("different"), this.identifier2());
    }

    @Override
    final public String text() {
        return this.identifier1() + "-" + this.identifier2().text();
    }

    @Override
    public Class<EbnfExceptionParserToken> type() {
        return EbnfExceptionParserToken.class;
    }
}
