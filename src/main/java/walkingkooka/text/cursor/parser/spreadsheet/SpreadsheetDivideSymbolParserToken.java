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

import java.util.List;

/**
 * Represents a divide symbol token.
 */
public final class SpreadsheetDivideSymbolParserToken extends SpreadsheetArithmeticSymbolParserToken {

    static SpreadsheetDivideSymbolParserToken with(final String value, final String text) {
        checkValue(value);

        return new SpreadsheetDivideSymbolParserToken(value, text);
    }

    private SpreadsheetDivideSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public SpreadsheetDivideSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetDivideSymbolParserToken replaceText(final String text) {
        return new SpreadsheetDivideSymbolParserToken(this.value, text);
    }

    @Override
    public boolean isDivideSymbol() {
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
    public boolean isPlusSymbol() {
        return false;
    }

    @Override
    public boolean isPowerSymbol() {
        return false;
    }

    @Override
    final int operatorPriority() {
        return MULTIPLY_DIVISION_PRIORITY;
    }

    @Override
    final SpreadsheetParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return SpreadsheetParserToken.division(tokens, text);
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetDivideSymbolParserToken;
    }
}
