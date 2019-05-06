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
import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.List;

/**
 * Represents a minus symbol token.
 */
public final class NodeSelectorMinusSymbolParserToken extends NodeSelectorArithmeticSymbolParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(NodeSelectorMinusSymbolParserToken.class);

    static NodeSelectorMinusSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorMinusSymbolParserToken(value, text);
    }

    private NodeSelectorMinusSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public NodeSelectorMinusSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    NodeSelectorMinusSymbolParserToken replaceText(final String text) {
        return new NodeSelectorMinusSymbolParserToken(this.value, text);
    }

    // name................................................................................................

    @Override
    public ParserTokenNodeName name() {
        return NAME;
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
        return true;
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
        return false;
    }

    // operator priority..................................................................................................

    @Override
    int operatorPriority() {
        return ADDITION_SUBTRACTION_PRIORITY;
    }

    @Override
    final NodeSelectorBinaryParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return NodeSelectorParserToken.subtraction(tokens, text);
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorMinusSymbolParserToken;
    }
}
