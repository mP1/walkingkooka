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

import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.search.SearchNode;

import java.util.List;

/**
 * Holds the combination of whitespace or comments.
 */
public final class SpreadsheetWhitespaceParserToken extends SpreadsheetSymbolParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetWhitespaceParserToken.class);

    static SpreadsheetWhitespaceParserToken with(final String value, final String text) {
        checkValue(value);

        return new SpreadsheetWhitespaceParserToken(value, text);
    }

    private SpreadsheetWhitespaceParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    void checkText(final String text) {
        CharSequences.failIfNullOrEmpty(text, "text");
    }

    @Override
    public SpreadsheetWhitespaceParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetWhitespaceParserToken replaceText(final String text) {
        return new SpreadsheetWhitespaceParserToken(this.value, text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    final int operatorPriority() {
        return LOWEST_PRIORITY;
    }

    /**
     * By chance all other {@link SpreadsheetSymbolParserToken} participate as the operand of a binary operand or similar,
     * except for whitespace.
     */
    @Override
    final SpreadsheetParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBetweenSymbol() {
        return false;
    }

    @Override
    public boolean isCloseParenthesisSymbol() {
        return false;
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
    public boolean isFunctionParameterSeparatorSymbol() {
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
    public boolean isMultiplySymbol() {
        return false;
    }

    @Override
    public boolean isNotEqualsSymbol() {
        return false;
    }

    @Override
    public boolean isOpenParenthesisSymbol() {
        return false;
    }

    @Override
    public boolean isPercentSymbol() {
        return false;
    }

    @Override
    public boolean isPowerSymbol() {
        return false;
    }

    @Override
    public boolean isPlusSymbol() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return true;
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetWhitespaceParserToken;
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode() {
        return SearchNode.text(this.text(), this.value());
    }
}
