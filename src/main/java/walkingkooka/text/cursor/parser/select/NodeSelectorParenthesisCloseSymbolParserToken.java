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

import walkingkooka.text.cursor.parser.ParserTokenNodeName;

/**
 * Represents a close object (parens) symbol token.
 */
public final class NodeSelectorParenthesisCloseSymbolParserToken extends NodeSelectorNonBinaryOperandSymbolParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(NodeSelectorParenthesisCloseSymbolParserToken.class);

    static NodeSelectorParenthesisCloseSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorParenthesisCloseSymbolParserToken(value, text);
    }

    private NodeSelectorParenthesisCloseSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public NodeSelectorParenthesisCloseSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    NodeSelectorParenthesisCloseSymbolParserToken replaceText(final String text) {
        return new NodeSelectorParenthesisCloseSymbolParserToken(this.value, text);
    }

    @Override
    void checkText(final String text) {
        checkTextNullOrEmpty(text);
    }

    // name................................................................................................

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    // is................................................................................................

    @Override
    public boolean isAtSignSymbol() {
        return false;
    }

    @Override
    public boolean isBracketOpenSymbol() {
        return false;
    }

    @Override
    public boolean isBracketCloseSymbol() {
        return false;
    }

    @Override
    public boolean isParameterSeparatorSymbol() {
        return false;
    }

    @Override
    public boolean isParenthesisOpenSymbol() {
        return false;
    }

    @Override
    public boolean isParenthesisCloseSymbol() {
        return true;
    }

    @Override
    public boolean isSlashSeparatorSymbol() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorParenthesisCloseSymbolParserToken;
    }
}
