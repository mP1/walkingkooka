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

import java.math.BigDecimal;

/**
 * Holds a single decimal number.
 */
public final class SpreadsheetBigDecimalParserToken extends SpreadsheetNumericParserToken<BigDecimal> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetBigDecimalParserToken.class);

    static SpreadsheetBigDecimalParserToken with(final BigDecimal value, final String text){
        checkValue(value);
        checkText(text);

        return new SpreadsheetBigDecimalParserToken(value, text);
    }

    private SpreadsheetBigDecimalParserToken(final BigDecimal value, final String text){
        super(value, text);
    }

    @Override
    public SpreadsheetBigDecimalParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetBigDecimalParserToken replaceText(final String text) {
        return new SpreadsheetBigDecimalParserToken(this.value, text);
    }

    @Override
    public boolean isBigDecimal() {
        return true;
    }

    @Override
    public boolean isBigInteger() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean isSymbol() {
        return false;
    }

    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public boolean isWhitespace() {
        return false;
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor){
        visitor.visit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetBigDecimalParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
