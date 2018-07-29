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
package walkingkooka.text.cursor.parser.spreadsheet;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class SpreadsheetParentParserToken extends SpreadsheetParserToken implements Value<List<SpreadsheetParserToken>> {

    final static boolean WITHOUT_COMPUTE_REQUIRED = true;
    final static boolean WITHOUT_USE_THIS = !WITHOUT_COMPUTE_REQUIRED;

    /**
     * Takes a defensive copy of the given tokens.
     */
    static List<SpreadsheetParserToken> copyAndCheckTokens(final List<SpreadsheetParserToken> tokens) {
        Objects.requireNonNull(tokens, "tokens");

        final List<SpreadsheetParserToken> copy = Lists.array();
        copy.addAll(tokens);

        if(copy.isEmpty()) {
            throw new IllegalArgumentException("Tokens is empty");
        }
        return copy;
    }

    SpreadsheetParentParserToken(final List<SpreadsheetParserToken> value, final String text, final boolean computeWithout) {
        super(text);
        this.value = value;
        this.without = computeWithout ?
                this.computeWithout(value) :
                Optional.of(this);
    }

    private Optional<SpreadsheetParserToken> computeWithout(final List<SpreadsheetParserToken> value){
        final List<SpreadsheetParserToken> without = Lists.array();

        value.stream()
                .forEach(t -> {
                    final Optional<SpreadsheetParserToken> maybeWithout = t.withoutSymbolsOrWhitespace();
                    if (maybeWithout.isPresent()) {
                        without.add(maybeWithout.get());
                    }
                });
        Lists.array();

        return Optional.of(this.replaceTokens(without));
    }

    @Override
    public final Optional<SpreadsheetParserToken> withoutSymbolsOrWhitespace() {
        return this.without;
    }

    private Optional<SpreadsheetParserToken> without;

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
        final SpreadsheetParentParserToken without = this.without.get().cast();
        return without.value().size();
    }

    @Override
    public final boolean isColumn() {
        return false;
    }

    @Override
    public final boolean isDecimal() {
        return false;
    }

    @Override
    public final boolean isDouble() {
        return false;
    }

    @Override
    public final boolean isFunctionName() {
        return false;
    }

    @Override
    public final boolean isLabelName() {
        return false;
    }

    @Override
    public final boolean isLong() {
        return false;
    }

    @Override
    public final boolean isNumber() {
        return false;
    }

    @Override
    public final boolean isRow() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return false;
    }

    @Override
    public final boolean isText() {
        return false;
    }

    @Override
    public final boolean isWhitespace() {
        return false;
    }

    @Override
    public final List<SpreadsheetParserToken> value() {
        return this.value;
    }

    final List<SpreadsheetParserToken> value;

    /**
     * Factory that creates a new {@link SpreadsheetParentParserToken} with the same text but new tokens.
     */
    abstract SpreadsheetParentParserToken replaceTokens(final List<SpreadsheetParserToken> tokens);

    final void acceptValues(final SpreadsheetParserTokenVisitor visitor){
        for(SpreadsheetParserToken token: this.value()){
            visitor.accept(token);
        }
    }

    @Override
    final boolean equals1(final SpreadsheetParserToken other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final SpreadsheetParentParserToken other) {
        return this.value.equals(other.value);
    }
}
