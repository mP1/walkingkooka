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
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.visit.Visiting;

import java.math.BigInteger;
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
    };
    private final static ParserToken WHITESPACE = new FakeParserToken() {

        @Override
        public String text() {
            return " ";
        }

        @Override
        public boolean isWhitespace() {
            return true;
        }

        @Override
        public void accept(final ParserTokenVisitor visitor) {
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || other instanceof ParserToken && this.equals0(Cast.to(other));
        }

        private boolean equals0(final ParserToken other) {
            return this.text().equals(other.text()) && this.isWhitespace() == other.isWhitespace();
        }

        @Override
        public String toString() {
            return "WS";
        }
    };

    // required...............................................................................................

    @Test
    public void testRequiredInvalidIndexFails() {
        assertThrows(IndexOutOfBoundsException.class, () -> this.createToken().required(999, StringParserToken.class));
    }

    @Test
    public void testRequiredInvalidTypeFails() {
        assertThrows(ClassCastException.class, () -> this.createToken().required(0, BigIntegerParserToken.class));
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

    // BinaryOperatorTransformer......................................................................................

    @Test
    public void testTransformNullFails() {
        assertThrows(NullPointerException.class, () -> this.createDifferentToken().transform(null));
    }

    @Test
    public void testTransformWhitespace() {
        this.transformAndCheck(this.createToken(WHITESPACE));
    }

    @Test
    public void testTransformWhitespaceWhitespace() {
        this.transformAndCheck(this.createToken(WHITESPACE, WHITESPACE));
    }

    @Test
    public void testTransformSymbol() {
        this.transformAndCheck(this.createToken(this.symbol('%')));
    }

    @Test
    public void testTransformSymbolSymbol() {
        this.transformAndCheck(this.createToken(this.symbol('%'), this.symbol('!')));
    }

    @Test
    public void testTransformWhitespaceSymbol() {
        this.transformAndCheck(this.createToken(WHITESPACE, this.symbol('%')));
    }

    @Test
    public void testTransformMinusNumber() {
        final ParserToken minus = symbol('-');
        final ParserToken right = integer(2);
        final ParserToken negative = negative(Lists.of(minus, right), "-2");

        this.transformAndCheck(sequence(negative), negative);
    }

    @Test
    public void testTransformMinusWhitespaceNumber() {
        final ParserToken minus = symbol('-');
        final ParserToken right = integer(2);
        final ParserToken negative = negative(Lists.of(minus, WHITESPACE, right), "-2");

        this.transformAndCheck(sequence(negative), negative);
    }

    @Test
    public void testTransformNumberPlusNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken right = integer(2);

        this.transformAndCheck(sequence(left, plus, right),
                add(left, plus, right));
    }

    @Test
    public void testTransformWhitespaceNumberPlusNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken right = integer(2);

        this.transformAndCheck(sequence(WHITESPACE, left, plus, right),
                sequence(WHITESPACE, add(left, plus, right)));
    }

    @Test
    public void testTransformWhitespaceNumberWhitespacePlusNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken right = integer(2);

        this.transformAndCheck(sequence(WHITESPACE, left, WHITESPACE, plus, right),
                sequence(WHITESPACE, add(left, WHITESPACE, plus, right)));
    }

    @Test
    public void testTransformNumberPlusWhitespaceNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken right = integer(2);

        this.transformAndCheck(sequence(left, plus, WHITESPACE, right),
                add(left, plus, WHITESPACE, right));
    }

    @Test
    public void testTransformNumberMinusNumber() {
        final ParserToken left = integer(1);
        final ParserToken minus = symbol('-');
        final ParserToken right = integer(2);

        this.transformAndCheck(sequence(left, minus, right),
                subtract(left, minus, right));
    }

    @Test
    public void testTransformMinusNumberPlusNumber() {
        final ParserToken left = negative(symbol('-'), integer(1));
        final ParserToken plus = symbol('+');
        final ParserToken right = integer(2);

        this.transformAndCheck(sequence(left, plus, right),
                add(left, plus, right));
    }

    @Test
    public void testTransformNumberPlusMinusNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken right = negative(symbol('-'), integer(2));

        this.transformAndCheck(sequence(left, plus, right),
                add(left, plus, right));
    }

    @Test
    public void testTransformMinusNumberPlusMinusNumber() {
        final ParserToken left = negative(symbol('-'), integer(1));
        final ParserToken plus = symbol('+');
        final ParserToken right = negative(symbol('-'), integer(2));

        this.transformAndCheck(sequence(left, plus, right),
                add(left, plus, right));
    }

    @Test
    public void testTransformNumberPlusNumberMultiplyNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken center = integer(2);
        final ParserToken multiply = symbol('*');
        final ParserToken right = integer(3);

        this.transformAndCheck(sequence(left, plus, center, multiply, right),
                add(left, plus, multiply(center, multiply, right)));
    }

    @Test
    public void testTransformNumberMultiplyNumberPlusNumber() {
        final ParserToken left = integer(1);
        final ParserToken multiply = symbol('*');
        final ParserToken center = integer(2);
        final ParserToken plus = symbol('+');
        final ParserToken right = integer(3);

        this.transformAndCheck(sequence(left, multiply, center, plus, right),
                add(multiply(left, multiply, center), plus, right));
    }

    @Test
    public void testTransformNumberPlusNumberMultiplyNumberMinusNumber() {
        final ParserToken left = integer(1);
        final ParserToken plus = symbol('+');
        final ParserToken left2 = integer(2);
        final ParserToken multiply = symbol('*');
        final ParserToken right1 = integer(3);
        final ParserToken minus = symbol('-');
        final ParserToken right2 = integer(4);

        this.transformAndCheck(sequence(left, plus, left2, multiply, right1, minus, right2),
                subtract(add(left, plus, multiply(left2, multiply, right1)), minus, right2));
    }

    private void transformAndCheck(final SequenceParserToken token) {
        this.transformAndCheck(token, token);
    }

    private void transformAndCheck(final SequenceParserToken token,
                                   final ParserToken expected) {
        assertEquals(expected,
                token.transform(new TestBinaryOperatorTransformer()),
                token::toString);
    }

    private ParserToken integer(final int value) {
        return ParserTokens.bigInteger(BigInteger.valueOf(value), String.valueOf(value));
    }

    private ParserToken symbol(final char c) {
        return new FakeParserToken() {

            @Override
            public String text() {
                return String.valueOf(c);
            }

            @Override
            public boolean isSymbol() {
                return true;
            }

            @Override
            public void accept(final ParserTokenVisitor visitor) {
            }

            @Override
            public int hashCode() {
                return c;
            }

            @Override
            public boolean equals(final Object other) {
                return this == other || other instanceof ParserToken && this.equals0(Cast.to(other));
            }

            private boolean equals0(final ParserToken other) {
                return this.text().equals(other.text()) && this.isSymbol() == other.isSymbol();
            }

            @Override
            public String toString() {
                return "SYMBOL " + CharSequences.quoteIfChars(c);
            }
        };
    }

    private SequenceParserToken sequence(final ParserToken... tokens) {
        return this.createToken(tokens);
    }

    private ParserToken add(final ParserToken... tokens) {
        return add(Lists.of(tokens), ParserToken.text(Lists.of(tokens)));
    }

    private ParserToken add(final List<ParserToken> tokens,
                            final String text) {
        return new TestAdditionParserToken(tokens, text);
    }

    private ParserToken multiply(final ParserToken... tokens) {
        return multiply(Lists.of(tokens), ParserToken.text(Lists.of(tokens)));
    }

    private ParserToken multiply(final List<ParserToken> tokens,
                                 final String text) {
        return new TestMultiplicationParserToken(tokens, text);
    }

    private ParserToken subtract(final ParserToken... tokens) {
        return subtract(Lists.of(tokens), ParserToken.text(Lists.of(tokens)));
    }

    private ParserToken subtract(final List<ParserToken> tokens,
                                 final String text) {
        return new TestSubtractionParserToken(tokens, text);
    }

    private ParserToken negative(final ParserToken... tokens) {
        return negative(Lists.of(tokens), ParserToken.text(Lists.of(tokens)));
    }

    private ParserToken negative(final List<ParserToken> tokens,
                                 final String text) {
        return new TestNegativeParserToken(tokens, text);
    }

    private static class TestAdditionParserToken extends TestBinaryParserToken {

        private TestAdditionParserToken(final List<ParserToken> tokens,
                                        final String text) {
            super(tokens, text);
        }

        @Override
        public String toString() {
            return "ADD " + this.tokens;
        }
    }

    private static class TestMultiplicationParserToken extends TestBinaryParserToken {

        private TestMultiplicationParserToken(final List<ParserToken> tokens,
                                              final String text) {
            super(tokens, text);
        }

        @Override
        public String toString() {
            return "MUL " + this.tokens;
        }
    }

    private static class TestSubtractionParserToken extends TestBinaryParserToken {

        private TestSubtractionParserToken(final List<ParserToken> tokens,
                                           final String text) {
            super(tokens, text);
        }

        @Override
        public String toString() {
            return "SUB " + this.tokens;
        }
    }

    private static class TestNegativeParserToken extends TestBinaryParserToken {

        private TestNegativeParserToken(final List<ParserToken> tokens,
                                        final String text) {
            super(tokens, text);
        }

        @Override
        public String toString() {
            return "NEG " + this.tokens;
        }
    }

    private static class TestBinaryParserToken extends FakeParserToken {

        TestBinaryParserToken(final List<ParserToken> tokens,
                              final String text) {
            super();
            this.tokens = tokens;
            this.text = text;
        }

        @Override
        public final String text() {
            return this.text;
        }

        private final String text;

        final List<ParserToken> tokens;

        @Override
        public final boolean isSymbol() {
            return false;
        }

        @Override
        public final void accept(final ParserTokenVisitor visitor) {
        }

        @Override
        public final int hashCode() {
            return this.tokens.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this == other || this.getClass() == other.getClass() && this.equals0(Cast.to(other));
        }

        private boolean equals0(final TestBinaryParserToken other) {
            return this.tokens.equals(other.tokens) && this.text.equals(other.text);
        }
    }

    private class TestBinaryOperatorTransformer implements BinaryOperatorTransformer {

        @Override
        public int highestPriority() {
            return 3;
        }

        @Override
        public int lowestPriority() {
            return 1;
        }

        @Override
        public int priority(final ParserToken token) {
            switch (token.text()) {
                case "+":
                case "-":
                    return 1;
                case "*":
                    return 2;
                default:
                    return 0;
            }
        }

        @Override
        public ParserToken binaryOperand(final List<ParserToken> tokens,
                                         final String text,
                                         final ParserToken symbol) {
            assertEquals(true, symbol.isSymbol(), () -> "expected symbol but got " + symbol);
            switch (symbol.text()) {
                case "+":
                    return add(tokens, text);
                case "-":
                    return subtract(tokens, text);
                case "*":
                    return multiply(tokens, text);
                default:
                    throw new UnsupportedOperationException("Bad symbol " + symbol);
            }
        }
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
