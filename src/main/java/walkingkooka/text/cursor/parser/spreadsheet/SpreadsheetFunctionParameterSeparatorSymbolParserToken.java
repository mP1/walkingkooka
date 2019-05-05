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

import walkingkooka.text.cursor.parser.ParserTokenNodeName;

/**
 * Represents a comma symbol token which is used to separate parameters to a function.
 */
public final class SpreadsheetFunctionParameterSeparatorSymbolParserToken extends SpreadsheetNonBinaryOperandSymbolParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetFunctionParameterSeparatorSymbolParserToken.class);

    static SpreadsheetFunctionParameterSeparatorSymbolParserToken with(final String value, final String text) {
        checkValue(value);

        return new SpreadsheetFunctionParameterSeparatorSymbolParserToken(value, text);
    }

    private SpreadsheetFunctionParameterSeparatorSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public SpreadsheetFunctionParameterSeparatorSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetFunctionParameterSeparatorSymbolParserToken replaceText(final String text) {
        return new SpreadsheetFunctionParameterSeparatorSymbolParserToken(this.value, text);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public boolean isCloseParenthesisSymbol() {
        return false;
    }

    @Override
    public boolean isFunctionParameterSeparatorSymbol() {
        return true;
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
        return other instanceof SpreadsheetFunctionParameterSeparatorSymbolParserToken;
    }
}
