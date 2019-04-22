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

import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This {@link ParserToken} holds a sequence in order of tokens.
 */
public final class SequenceParserToken extends RepeatedOrSequenceParserToken<SequenceParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SequenceParserToken.class);

    /**
     * Factory that wraps many tokens in a {@link SequenceParserToken}.
     */
    static <T extends ParserToken> SequenceParserToken with(final List<ParserToken> tokens, final String text) {
        Objects.requireNonNull(tokens, "tokens");
        Objects.requireNonNull(text, "text");

        return new SequenceParserToken(tokens, text);
    }

    private SequenceParserToken(final List<ParserToken> tokens, final String text) {
        super(tokens, text);
    }

    @Override
    public SequenceParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SequenceParserToken replaceText(final String text) {
        return with(this.value(), text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    public SequenceParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override final SequenceParserToken replaceValue(final List<ParserToken> value) {
        return new SequenceParserToken(value, this.text());
    }

    /**
     * Removes any noisy token values, returning a new instance if necessary.
     */
    public SequenceParserToken removeNoise() {
        return this.removeIf(t -> t.isNoise());
    }

    /**
     * Removes any whitespace token values, returning a new instance if necessary.
     */
    public SequenceParserToken removeWhitespace() {
        return this.removeIf(t -> t.isWhitespace());
    }

    /**
     * Removes any tokens that are matched by the {@link Predicate}.
     */
    private SequenceParserToken removeIf(final Predicate<ParserToken> removeIf) {
        final Predicate<ParserToken> keep = removeIf.negate();

        return this.setValue(this.value().stream()
                .filter(keep)
                .collect(Collectors.toList()));
    }

    @Override
    public void accept(final ParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    public SequenceParserToken flat() {
        return this.setValue(this.flat0());
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
            return type.cast(tokens.get(index));
        } catch (final IndexOutOfBoundsException cause) {
            throw new IndexOutOfBoundsException("Invalid index " + index + " must be between 0 and " + tokens.size());
        }
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SequenceParserToken;
    }
}
