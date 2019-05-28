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
 * Represents a greater than equals symbol token.
 */
public final class SpreadsheetGreaterThanEqualsSymbolParserToken extends SpreadsheetComparisonSymbolParserToken {

    static SpreadsheetGreaterThanEqualsSymbolParserToken with(final String value, final String text) {
        checkValue(value);

        return new SpreadsheetGreaterThanEqualsSymbolParserToken(value, text);
    }

    private SpreadsheetGreaterThanEqualsSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public SpreadsheetGreaterThanEqualsSymbolParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetGreaterThanEqualsSymbolParserToken replaceText(final String text) {
        return new SpreadsheetGreaterThanEqualsSymbolParserToken(this.value, text);
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
        return true;
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
    public boolean isNotEqualsSymbol() {
        return false;
    }

    @Override
    final SpreadsheetParserToken binaryOperand(final List<ParserToken> tokens, final String text) {
        return SpreadsheetParserToken.greaterThanEquals(tokens, text);
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetGreaterThanEqualsSymbolParserToken;
    }
}
