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

import walkingkooka.collect.list.Lists;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This {@link ParserToken} holds a sequence in order of tokens.
 */
public final class SequenceParserToken extends RepeatedOrSequenceParserToken {

    /**
     * Factory that wraps many tokens in a {@link SequenceParserToken}.
     */
    static SequenceParserToken with(final List<ParserToken> tokens, final String text) {
        Objects.requireNonNull(tokens, "tokens");
        Objects.requireNonNull(text, "text");

        return new SequenceParserToken(tokens, text);
    }

    private SequenceParserToken(final List<ParserToken> tokens, final String text) {
        super(tokens, text);
    }

    @Override
    final SequenceParserToken replaceValue(final List<ParserToken> value) {
        return new SequenceParserToken(value, this.text());
    }

    /**
     * Removes any noisy token values, returning a new instance if necessary.
     */
    public SequenceParserToken removeNoise() {
        return this.removeIf(ParserToken::isNoise);
    }

    /**
     * Removes any whitespace token values, returning a new instance if necessary.
     */
    public SequenceParserToken removeWhitespace() {
        return this.removeIf(ParserToken::isWhitespace);
    }

    /**
     * Removes any tokens that are matched by the {@link Predicate}.
     */
    private SequenceParserToken removeIf(final Predicate<ParserToken> removeIf) {
        final Predicate<ParserToken> keep = removeIf.negate();

        return this.setValue(this.value().stream()
                .filter(keep)
                .collect(Collectors.toList()))
                .cast(SequenceParserToken.class);
    }

    @Override
    public SequenceParserToken flat() {
        return this.setValue(RepeatedOrSequenceParserTokenFlatParserTokenVisitor.flat(this))
                .cast(SequenceParserToken.class);
    }

    /**
     * Asserts that the sequence contains the correct number of tokens throwing a {@link IllegalStateException} if the
     * test fails.
     */
    public void checkTokenCount(final int expected) {
        final List<ParserToken> list = this.value();
        final int actual = list.size();
        if (actual != expected) {
            throw new IllegalStateException("Expected " + expected + " but got " + actual + "=" + list);
        }
    }

    public <T extends ParserToken> T required(final int index, final Class<T> type) {
        final List<ParserToken> tokens = this.value();
        try {
            return (T)tokens.get(index);
        } catch (final IndexOutOfBoundsException cause) {
            throw new IndexOutOfBoundsException("Invalid index " + index + " must be between 0 and " + tokens.size());
        }
    }

    // BinaryOperatorTransformer......................................................................................

    /**
     * Takes this {@link SequenceParserToken} and possibly rearranges tokens creating binary operators and unary negative tokens
     * as necessary honouring the priorities replied by the given {@link BinaryOperatorTransformer}.
     */
    public ParserToken transform(final BinaryOperatorTransformer transformer) {
        Objects.requireNonNull(transformer, "transformer");

        final List<ParserToken> flat = RepeatedOrSequenceParserTokenFlatParserTokenVisitor.flat(this);
        return flat.stream()
                .filter(SequenceParserToken::notWhitespace)
                .findFirst()
                .map(t -> t.isSymbol() ?
                        this :
                        tryFindAndIntroduceBinaryOperator(flat, transformer)).orElse(this);
    }

    private static boolean notWhitespace(final ParserToken token) {
        return !token.isWhitespace();
    }

    private ParserToken tryFindAndIntroduceBinaryOperator(final List<ParserToken> tokens,
                                                          final BinaryOperatorTransformer transformer) {
        List<ParserToken> result = Lists.array();
        result.addAll(tokens);

        final int lowestOperatorPriority = transformer.lowestPriority();

        for (int priority = transformer.highestPriority(); priority >= lowestOperatorPriority; priority--) {
            boolean changed;

            do {
                changed = false;
                int i = 0;
                for (ParserToken t : result) {
                    if (0 != i && transformer.priority(t) == priority) {
                        changed = true;

                        final int begin = findNonWhitespaceSibling(result, i - 1, -1);
                        final int end = findNonWhitespaceSibling(result, i + 1, +1);

                        final List<ParserToken> binaryOperandTokens = Lists.array();
                        binaryOperandTokens.addAll(result.subList(begin, end + 1));

                        final List<ParserToken> withBinary = Lists.array();
                        withBinary.addAll(result.subList(0, begin));
                        withBinary.add(transformer.binaryOperand(binaryOperandTokens, ParserToken.text(binaryOperandTokens), t));
                        withBinary.addAll(result.subList(end + 1, result.size()));

                        result = withBinary;
                        break;
                    }
                    i++;
                }
            } while (changed && result.size() > 1);
        }

        return result.size() == 1 ?
                result.get(0) :
                ParserTokens.sequence(result, this.text());
    }

    private static int findNonWhitespaceSibling(final List<ParserToken> tokens,
                                                final int startIndex,
                                                final int step) {
        int i = startIndex;
        for (; ; ) {
            final ParserToken token = tokens.get(i);
            if (!token.isWhitespace()) {
                break;
            }
            i = i + step;
        }
        return i;
    }

    // ParserTokenVisitor...............................................................................................

    @Override
    public void accept(final ParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SequenceParserToken;
    }
}
