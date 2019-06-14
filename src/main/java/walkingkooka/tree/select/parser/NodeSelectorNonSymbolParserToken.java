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
package walkingkooka.tree.select.parser;

import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

/**
 * Base class for a leaf token. A leaf has no further breakdown into more detailed tokens.
 */
abstract class NodeSelectorNonSymbolParserToken<T> extends NodeSelectorLeafParserToken<T> {

    NodeSelectorNonSymbolParserToken(final T value, final String text) {
        super(value, text);
    }

    @Override
    public final Optional<NodeSelectorParserToken> withoutSymbols() {
        return Optional.of(this);
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
    public final boolean isBracketOpenSymbol() {
        return false;
    }

    @Override
    public final boolean isBracketCloseSymbol() {
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
    public final boolean isGreaterThanSymbol() {
        return false;
    }

    @Override
    public final boolean isGreaterThanEqualsSymbol() {
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
    public final boolean isNotEqualsSymbol() {
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
    public final boolean isPlusSymbol() {
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

    // operator priority..................................................................................................

    @Override
    final int operatorPriority() {
        return LOWEST_PRIORITY;
    }

    @Override
    final NodeSelectorBinaryParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        throw new UnsupportedOperationException();
    }
}
