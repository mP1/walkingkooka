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
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SequenceParserTokenTest extends RepeatedOrSequenceParserTokenTestCase<SequenceParserToken> {

    private final static StringParserToken STRING1 = ParserTokens.string("a1", "a1");
    private final static StringParserToken STRING2 = ParserTokens.string("b2", "b2");
    private final static ParserToken NOISY3 = new FakeParserToken() {
        @Override
        public boolean isNoise() {
            return true;
        }

        @Override
        public String text() {
            return "";
        }

        @Override
        public SearchNode toSearchNode() {
            final String text = this.text();
            return SearchNode.text(text, text);
        }
    };
    private final static ParserToken WHITESPACE = new FakeParserToken() {

        @Override
        public String text() {
            return "";
        }

        @Override
        public boolean isWhitespace() {
            return true;
        }
    };

    // required...............................................................................................

    @Test
    public void testRequiredInvalidIndexFails() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            this.createToken().required(999, StringParserToken.class);
        });
    }

    @Test
    public void testRequiredInvalidTypeFails() {
        assertThrows(ClassCastException.class, () -> {
            this.createToken().required(0, BigIntegerParserToken.class);
        });
    }

    @Test
    public void testRequiredPresent() {
        this.requiredAndGet(0, STRING1);
    }

    @Test
    public void testRequiredPresent2() {
        this.requiredAndGet(1, STRING2);
    }

    private void requiredAndGet(final int index, final ParserToken result) {
        final SequenceParserToken sequence = this.createToken();
        assertEquals(result,
                sequence.required(index, StringParserToken.class),
                "failed to get required " + index + " from " + sequence);
    }

    // removeNoise...........................................................................................................

    @Test
    public void testRemovingNoiseNone() {
        final SequenceParserToken token = createToken(STRING1, STRING2);
        assertSame(token, token.removeNoise());
    }

    @Test
    public void testRemovingNoiseSome() {
        final SequenceParserToken token = createToken(STRING1, STRING2, NOISY3);
        final SequenceParserToken different = token.removeNoise();

        assertEquals(Lists.of(STRING1, STRING2), different.value(), "value");
        assertSame(different, different.removeNoise());
    }

    @Test
    public void testRemovingNoiseSomeIgnoresWhitespace() {
        final SequenceParserToken token = createToken(STRING1, STRING2, NOISY3, WHITESPACE);
        final SequenceParserToken different = token.removeNoise();

        assertEquals(Lists.of(STRING1, STRING2, WHITESPACE), different.value(), "value");
        assertSame(different, different.removeNoise());
    }

    // removeWhitespace...........................................................................................................

    @Test
    public void testRemovingWhitespaceNone() {
        final SequenceParserToken token = createToken(STRING1, STRING2);
        assertSame(token, token.removeWhitespace());
    }

    @Test
    public void testRemovingWhitespaceSome() {
        final SequenceParserToken token = createToken(STRING1, STRING2, WHITESPACE);
        final SequenceParserToken different = token.removeWhitespace();

        assertEquals(Lists.of(STRING1, STRING2), different.value(), "value");
        assertSame(different, different.removeWhitespace());
    }

    @Test
    public void testRemovingWhitespaceSomeIgnoresMissing() {
        final SequenceParserToken token = createToken(STRING1, STRING2, NOISY3, WHITESPACE);
        final SequenceParserToken different = token.removeWhitespace();

        assertEquals(Lists.of(STRING1, STRING2, NOISY3), different.value(), "value");
        assertSame(different, different.removeWhitespace());
    }

    // accept...........................................................................................................

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final SequenceParserToken token = this.createToken();

        new FakeParserTokenVisitor() {
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
            protected Visiting startVisit(final SequenceParserToken t) {
                assertSame(token, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final SequenceParserToken t) {
                assertSame(token, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringParserToken t) {
                b.append("6");
                visited.add(t);
            }
        }.accept(token);
        assertEquals("1316216242", b.toString());
        assertEquals(Lists.<Object>of(token, token, STRING1, STRING1, STRING1, STRING2, STRING2, STRING2, token, token),
                visited,
                "visited tokens");
    }

    @Test
    public void testAcceptSkip() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final SequenceParserToken token = this.createToken();

        new FakeParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("1");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("2");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final SequenceParserToken t) {
                b.append("3");
                visited.add(t);
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final SequenceParserToken t) {
                assertSame(token, t);
                b.append("4");
                visited.add(t);
            }
        }.accept(token);
        assertEquals("1342", b.toString());
        assertEquals(Lists.of(token, token, token, token), visited, "visited tokens");
    }

    @Override
    public SequenceParserToken createToken(final String text) {
        return this.createToken(this.tokens(), text);
    }

    @Override
    public String text() {
        return ParserToken.text(this.tokens());
    }

    private List<ParserToken> tokens() {
        return Lists.of(STRING1, STRING2);
    }

    @Override
    public SequenceParserToken createDifferentToken() {
        return this.createToken(string("different"), string("different2"));
    }

    @Override
    SequenceParserToken createToken(final List<ParserToken> value, final String text) {
        return SequenceParserToken.with(value, text);
    }

    @Override
    public Class<SequenceParserToken> type() {
        return SequenceParserToken.class;
    }
}
