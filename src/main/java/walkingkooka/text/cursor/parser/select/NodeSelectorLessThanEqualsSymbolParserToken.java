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
 * Represents a less than equals sign in a comparison parser token.
 */
public final class NodeSelectorLessThanEqualsSymbolParserToken extends NodeSelectorComparisonSymbolParserToken {

    static NodeSelectorLessThanEqualsSymbolParserToken with(final String value, final String text) {
        checkValue(value);
        checkTextNullOrWhitespace(text);

        return new NodeSelectorLessThanEqualsSymbolParserToken(value, text);
    }

    private NodeSelectorLessThanEqualsSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public NodeSelectorLessThanEqualsSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    NodeSelectorLessThanEqualsSymbolParserToken replaceText(final String text) {
        return new NodeSelectorLessThanEqualsSymbolParserToken(this.value, text);
    }

    // is..........................................................................................................

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
        return true;
    }

    @Override
    public boolean isNotEqualsSymbol() {
        return false;
    }

    // operator priority................................................................................................

    @Override
    final NodeSelectorBinaryParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return NodeSelectorParserToken.lessThanEquals(tokens, text);
    }

    // Visitor................................................................................................

    @Override
    public void accept(final NodeSelectorParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof NodeSelectorLessThanEqualsSymbolParserToken;
    }
}
