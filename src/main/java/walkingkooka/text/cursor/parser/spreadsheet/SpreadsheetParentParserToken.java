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
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class SpreadsheetParentParserToken extends SpreadsheetParserToken implements Value<List<ParserToken>> {

    final static List<ParserToken> WITHOUT_COMPUTE_REQUIRED = null;

    SpreadsheetParentParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(text);
        this.value = value;
        this.without =  null == valueWithout ?
                computeWithout(value) :
                Optional.of(this);
    }

    private Optional<SpreadsheetParserToken> computeWithout(final List<ParserToken> value){
        final List<ParserToken> without = Lists.array();

        value.stream()
                .forEach(t -> {
                    if(t instanceof SpreadsheetParserToken) {
                        final Optional<SpreadsheetParserToken> maybeWithout = SpreadsheetParserToken.class.cast(t).withoutSymbolsOrWhitespace();
                        if (maybeWithout.isPresent()) {
                            without.add(maybeWithout.get());
                        }
                    }
                });

        return Optional.of(value.size() == without.size() ?
                this :
                this.replaceTokens(without));
    }

    final List<ParserToken> valueIfWithoutSymbolsOrWhitespaceOrNull() {
        return this == this.without.get() ? this.value : null;
    }

    @Override
    public final Optional<SpreadsheetParserToken> withoutSymbolsOrWhitespace() {
        return this.without;
    }

    private final Optional<SpreadsheetParserToken> without;

    @Override
    public final boolean isBetweenSymbol() {
        return false;
    }

    @Override
    public final boolean isBigDecimal() {
        return false;
    }

    @Override
    public final boolean isBigInteger() {
        return false;
    }

    @Override
    public final boolean isCloseParenthesisSymbol() {
        return false;
    }

    @Override
    public final boolean isColumn() {
        return false;
    }

    @Override
    public final boolean isDivideSymbol() {
        return false;
    }

    @Override
    public final boolean isDouble() {
        return false;
    }

    @Override
    public final boolean isEqualsSymbol() {
        return false;
    }

    @Override
    public final boolean isFunctionName() {
        return false;
    }

    @Override
    public final boolean isFunctionParameterSeparatorSymbol() {
        return false;
    }
    
    @Override
    public final boolean isGreaterThanSymbol() {
        return false;
    }

    @Override
    public final boolean isGreaterThanEqualsSymbol() {
        return false;
    }

    @Override
    public final boolean isLabelName() {
        return false;
    }

    @Override
    public final boolean isLessThanSymbol() {
        return false;
    }

    @Override
    public final boolean isLessThanEqualsSymbol() {
        return false;
    }

    @Override
    public final boolean isLocalDate() {
        return false;
    }

    @Override
    public final boolean isLocalDateTime() {
        return false;
    }

    @Override
    public final boolean isLocalTime() {
        return false;
    }

    @Override
    public final boolean isLong() {
        return false;
    }

    @Override
    public final boolean isMinusSymbol() {
        return false;
    }

    @Override
    public final boolean isMultiplySymbol() {
        return false;
    }

    @Override
    public final boolean isNotEqualsSymbol() {
        return false;
    }

    @Override
    public final boolean isOpenParenthesisSymbol() {
        return false;
    }

    @Override
    public final boolean isPercentSymbol() {
        return false;
    }

    @Override
    public final boolean isPlusSymbol() {
        return false;
    }

    @Override
    public final boolean isPowerSymbol() {
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
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    /**
     * Factory that creates a new {@link SpreadsheetParentParserToken} with the same text but new tokens.
     */
    abstract SpreadsheetParentParserToken replaceTokens(final List<ParserToken> tokens);

    @Override
    final int operatorPriority() {
        return LOWEST_PRIORITY;
    }

    @Override
    final SpreadsheetParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        throw new UnsupportedOperationException();
    }

    final void acceptValues(final SpreadsheetParserTokenVisitor visitor){
        for(ParserToken token: this.value()){
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
