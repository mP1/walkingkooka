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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.List;

/**
 * Represents a less than equals symbol token.
 */
public final class SpreadsheetLessThanEqualsSymbolParserToken extends SpreadsheetSymbolParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetLessThanEqualsSymbolParserToken.class);

    static SpreadsheetLessThanEqualsSymbolParserToken with(final String value, final String text) {
        checkValue(value);

        return new SpreadsheetLessThanEqualsSymbolParserToken(value, text);
    }

    private SpreadsheetLessThanEqualsSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public SpreadsheetLessThanEqualsSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetLessThanEqualsSymbolParserToken replaceText(final String text) {
        return new SpreadsheetLessThanEqualsSymbolParserToken(this.value, text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
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
        return true;
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
        return false;
    }

    @Override
    final int operatorPriority() {
        return GREATER_THAN_LESS_THAN_PRIORITY;
    }

    @Override final SpreadsheetParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return SpreadsheetParserToken.lessThanEquals(tokens, text);
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetLessThanEqualsSymbolParserToken;
    }
}
