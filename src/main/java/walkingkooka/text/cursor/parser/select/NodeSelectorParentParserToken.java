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
package walkingkooka.text.cursor.parser.select;

import walkingkooka.Cast;
import walkingkooka.text.cursor.parser.ParentParserToken;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

/**
 * Base class for a token that contain another child token, with the class knowing the cardinality.
 */
abstract class NodeSelectorParentParserToken<T extends NodeSelectorParentParserToken> extends NodeSelectorParserToken implements ParentParserToken<T> {

    final static List<ParserToken> WITHOUT_COMPUTE_REQUIRED = null;

    NodeSelectorParentParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout) {
        super(text);
        this.value = value;
        this.without = value.equals(valueWithout) ?
                Optional.of(this) :
                computeWithout(value);
    }

    private Optional<NodeSelectorParserToken> computeWithout(final List<ParserToken> value) {
        final List<ParserToken> without = ParentParserToken.filterWithoutNoise(value);

        return Optional.of(value.size() == without.size() ?
                this :
                this.replaceValue(without, without));
    }

    @Override
    public final Optional<NodeSelectorParserToken> withoutSymbols() {
        return this.without;
    }

    final boolean isWithout() {
        return this.without.get() == this;
    }

    private final Optional<NodeSelectorParserToken> without;

    final List<ParserToken> valueIfWithoutSymbolsOrNull() {
        return this == this.without.get() ? this.value : null;
    }

    @Override
    public final List<ParserToken> value() {
        return this.value;
    }

    final List<ParserToken> value;

    /**
     * Factory that creates a new {@link NodeSelectorParentParserToken} with the same text but new tokens.
     */
    abstract NodeSelectorParentParserToken replaceValue(final List<ParserToken> tokens, final List<ParserToken> without);

    // is..........................................................................................................

    @Override
    public final boolean isAbsolute() {
        return false;
    }

    @Override
    public final boolean isAncestor() {
        return false;
    }

    @Override
    public final boolean isAncestorOrSelf() {
        return false;
    }

    @Override
    public final boolean isAndSymbol() {
        return false;
    }

    @Override
    public final boolean isAtSignSymbol() {
        return false;
    }

    @Override
    public final boolean isAttributeName() {
        return false;
    }

    @Override
    public final boolean isBracketOpenSymbol() {
        return false;
    }

    @Override
    public final boolean isBracketCloseSymbol() {
        return false;
    }

    @Override
    public final boolean isChild() {
        return false;
    }

    @Override
    public final boolean isDescendant() {
        return false;
    }

    @Override
    public final boolean isDescendantOrSelf() {
        return false;
    }

    @Override
    public final boolean isDivideSymbol() {
        return false;
    }

    @Override
    public final boolean isEqualsSymbol() {
        return false;
    }

    @Override
    public final boolean isFirstChild() {
        return false;
    }

    @Override
    public final boolean isFollowing() {
        return false;
    }

    @Override
    public final boolean isFollowingSibling() {
        return false;
    }

    @Override
    public final boolean isFunctionName() {
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
    public final boolean isLastChild() {
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
    public final boolean isMinusSymbol() {
        return false;
    }

    @Override
    public final boolean isModuloSymbol() {
        return false;
    }

    @Override
    public final boolean isMultiplySymbol() {
        return false;
    }

    @Override
    public final boolean isNodeName() {
        return false;
    }

    @Override
    public final boolean isNotEqualsSymbol() {
        return false;
    }

    @Override
    public final boolean isNumber() {
        return false;
    }

    @Override
    public final boolean isOrSymbol() {
        return false;
    }

    @Override
    public final boolean isParameterSeparatorSymbol() {
        return false;
    }

    @Override
    public final boolean isParenthesisOpenSymbol() {
        return false;
    }

    @Override
    public final boolean isParenthesisCloseSymbol() {
        return false;
    }

    @Override
    public final boolean isParentOf() {
        return false;
    }

    @Override
    public final boolean isPlusSymbol() {
        return false;
    }

    @Override
    public final boolean isPreceding() {
        return false;
    }

    @Override
    public final boolean isPrecedingSibling() {
        return false;
    }

    @Override
    public final boolean isQuotedText() {
        return false;
    }

    @Override
    public final boolean isSelf() {
        return false;
    }

    @Override
    public final boolean isSlashSeparatorSymbol() {
        return false;
    }

    @Override
    public final boolean isSymbol() {
        return false;
    }

    @Override
    public final boolean isWhitespace() {
        return false;
    }

    @Override
    public final boolean isWildcard() {
        return false;
    }

    // operator priority..................................................................................................

    @Override
    final int operatorPriority() {
        return LOWEST_PRIORITY;
    }

    @Override
    final NodeSelectorBinaryParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        throw new UnsupportedOperationException();
    }

    // Visitor................................................................................................

    final void acceptValues(final NodeSelectorParserTokenVisitor visitor) {
        for (ParserToken token : this.value()) {
            visitor.accept(token);
        }
    }

    // Object................................................................................................

    @Override
    final boolean equals1(final NodeSelectorParserToken other) {
        return this.equals2(Cast.to(other));
    }

    private boolean equals2(final NodeSelectorParentParserToken other) {
        return this.value.equals(other.value);
    }
}
