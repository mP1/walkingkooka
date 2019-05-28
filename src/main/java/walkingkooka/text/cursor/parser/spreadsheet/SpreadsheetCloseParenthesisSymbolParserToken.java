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

/**
 * Represents a close / right parens symbol token.
 */
public final class SpreadsheetCloseParenthesisSymbolParserToken extends SpreadsheetNonBinaryOperandSymbolParserToken {

    static SpreadsheetCloseParenthesisSymbolParserToken with(final String value, final String text) {
        checkValue(value);

        return new SpreadsheetCloseParenthesisSymbolParserToken(value, text);
    }

    private SpreadsheetCloseParenthesisSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public SpreadsheetCloseParenthesisSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetCloseParenthesisSymbolParserToken replaceText(final String text) {
        return new SpreadsheetCloseParenthesisSymbolParserToken(this.value, text);
    }

    @Override
    public boolean isCloseParenthesisSymbol() {
        return true;
    }

    @Override
    public boolean isFunctionParameterSeparatorSymbol() {
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
    public boolean isWhitespace() {
        return false;
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetCloseParenthesisSymbolParserToken;
    }
}
