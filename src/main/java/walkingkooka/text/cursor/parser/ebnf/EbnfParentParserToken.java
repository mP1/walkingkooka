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

import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.ParentParserToken;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class EbnfParentParserToken<T extends EbnfParentParserToken> extends EbnfParserToken implements ParentParserToken<T> {

    final static List<ParserToken> WITHOUT_COMPUTE_REQUIRED = null;

    EbnfParentParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(text);
        this.value = value;
        this.without = value.equals(valueWithout) ?
                Optional.of(this) :
                computeWithout(value);
    }

    private Optional<EbnfParserToken> computeWithout(final List<ParserToken> value) {
        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(value);

        return Optional.of(value.size() == without.size() ?
                this :
                this.replace(without, this.text(), without));
    }

    final void checkOnlyOneToken() {
        final int count = this.tokenCount();
        if (count != 1) {
            throw new IllegalArgumentException("Expected 1 token(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    final void checkAtLeastTwoTokens() {
        final int count = this.tokenCount();
        if (count < 2) {
            throw new IllegalArgumentException("Expected at least 2 tokens(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    final void checkOnlyTwoTokens() {
        final int count = this.tokenCount();
        if (count != 2) {
            throw new IllegalArgumentException("Expected 2 tokens(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    private int tokenCount() {
        final EbnfParentParserToken without = this.without.get().cast();
        return without.value().size();
    }

    @Override
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    @Override
    public final Optional<EbnfParserToken> withoutCommentsSymbolsOrWhitespace() {
        return this.without;
    }

    /**
     * A cached copy of this parent/container without any comments, symbols or whitespace.
     */
    final Optional<EbnfParserToken> without;

    /**
     * Factory that creates a new {@link EbnfParentParserToken} with the same text but new tokens.
     * This is only called when creating the withoutCommentsSymbolsOrWhitespace() instance.
     */
    abstract EbnfParentParserToken replace(final List<ParserToken> tokens, final String text, final List<ParserToken> without);

    @Override
    public final boolean isComment() {
        return false;
    }

    @Override
    public final boolean isIdentifier() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return false;
    }

    @Override
    public final boolean isTerminal() {
        return false;
    }

    @Override
    public final boolean isWhitespace() {
        return false;
    }

    final void acceptValues(final EbnfParserTokenVisitor visitor) {
        for (ParserToken token : this.value()) {
            visitor.accept(token);
        }
    }

    @Override
    final boolean equals1(final EbnfParserToken other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final EbnfParentParserToken other) {
        return this.value.equals(other.value);
    }
}
