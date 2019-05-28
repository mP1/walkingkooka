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

import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;

/**
 * Represents and symbol token.
 */
public final class NodeSelectorAndSymbolParserToken extends NodeSelectorBinaryOperandSymbolParserToken {

    static NodeSelectorAndSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorAndSymbolParserToken(value, text);
    }

    private NodeSelectorAndSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    // is..........................................................................................................

    @Override
    public boolean isAndSymbol() {
        return true;
    }

    @Override
    public boolean isDivideSymbol() {
        return false;
    }

    @Override
    public boolean isEqualsSymbol() {
        return false;
    }

    @Override
    public boolean isGreaterThanSymbol() {
        return false;
    }

    @Override
    public boolean isGreaterThanEqualsSymbol() {
        return false;
    }

    @Override
    public boolean isLessThanSymbol() {
        return false;
    }

    @Override
    public boolean isLessThanEqualsSymbol() {
        return false;
    }

    @Override
    public boolean isMinusSymbol() {
        return false;
    }

    @Override
    public boolean isModuloSymbol() {
        return false;
    }

    @Override
    public boolean isMultiplySymbol() {
        return false;
    }

    @Override
    public boolean isNotEqualsSymbol() {
        return false;
    }

    @Override
    public boolean isOrSymbol() {
        return false;
    }

    @Override
    public boolean isPlusSymbol() {
        return false;
    }

    // operator priority..................................................................................................

    @Override
    int operatorPriority() {
        return LOWEST_PRIORITY;
    }

    @Override
    final NodeSelectorBinaryParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return NodeSelectorParserToken.and(tokens, text);
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorAndSymbolParserToken;
    }
}
