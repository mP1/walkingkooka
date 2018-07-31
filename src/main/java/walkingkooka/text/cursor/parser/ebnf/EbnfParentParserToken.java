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
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class EbnfParentParserToken extends EbnfParserToken implements Value<List<ParserToken>> {

    final static boolean WITHOUT_COMPUTE_REQUIRED = true;
    final static boolean WITHOUT_USE_THIS = !WITHOUT_COMPUTE_REQUIRED;

    EbnfParentParserToken(final List<ParserToken> value, final String text, final boolean computeWithout) {
        super(text);
        this.value = value;
        this.without = computeWithout ? computeWithout(value) : Optional.of(this);
    }

    private Optional<EbnfParserToken> computeWithout(final List<ParserToken> value){
        final List<ParserToken> without = Lists.array();

        value.stream()
                .forEach(t -> {
                    if(t instanceof EbnfParserToken){
                        final EbnfParserToken ebnfParserToken = t.cast();
                        final Optional<EbnfParserToken> maybeWithout = ebnfParserToken.withoutCommentsSymbolsOrWhitespace();
                        if (maybeWithout.isPresent()) {
                            without.add(maybeWithout.get());
                        }
                    } else {
                        without.add(t);
                    }
                });
        Lists.array();

        return Optional.of(this.replaceTokens(without));
    }

    final void checkOnlyOneToken() {
        final int count = this.tokenCount();
        if(count != 1) {
            throw new IllegalArgumentException("Expected 1 token(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    final void checkAtLeastTwoTokens() {
        final int count = this.tokenCount();
        if(count < 2) {
            throw new IllegalArgumentException("Expected at least 2 tokens(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    final void checkOnlyTwoTokens() {
        final int count = this.tokenCount();
        if(count != 2) {
            throw new IllegalArgumentException("Expected 2 tokens(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    final void checkAtLeastOneRule() {
        final int count = this.tokenCount();
        if(count <1) {
            throw new IllegalArgumentException("Expected at least one rule(ignoring comments, symbols and whitespace) but was " + count + "=" + this.text());
        }
    }

    private int tokenCount() {
        final EbnfParentParserToken without = this.without.get().cast();
        return without.value().size();
    }

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

    @Override
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    @Override
    public final Optional<EbnfParserToken> withoutCommentsSymbolsOrWhitespace(){
        return this.without;
    }

    /**
     * A cached copy of this parent/container without any comments, symbols or whitespace.
     */
    final Optional<EbnfParserToken> without;

    /**
     * Factory that creates a new {@link EbnfParentParserToken} with the same text but new tokens.
     */
    abstract EbnfParentParserToken replaceTokens(final List<ParserToken> tokens);

    final void acceptValues(final EbnfParserTokenVisitor visitor){
        for(ParserToken token: this.value()){
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
