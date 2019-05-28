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
 * Represents a plus symbol token.
 */
public final class NodeSelectorPlusSymbolParserToken extends NodeSelectorArithmeticSymbolParserToken {

    static NodeSelectorPlusSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorPlusSymbolParserToken(value, text);
    }

    private NodeSelectorPlusSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public NodeSelectorPlusSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    NodeSelectorPlusSymbolParserToken replaceText(final String text) {
        return new NodeSelectorPlusSymbolParserToken(this.value, text);
    }

    // is..........................................................................................................

    @Override
    public boolean isDivideSymbol() {
        return false;
    }

    @Override
    public boolean isEqualsSymbol() {
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
    public boolean isPlusSymbol() {
        return true;
    }

    // operator priority..................................................................................................

    @Override
    int operatorPriority() {
        return ADDITION_SUBTRACTION_PRIORITY;
    }

    @Override
    final NodeSelectorBinaryParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return NodeSelectorParserToken.addition(tokens, text);
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorPlusSymbolParserToken;
    }
}
