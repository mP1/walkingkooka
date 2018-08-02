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
public final class SpreadsheetDecimalParserToken extends SpreadsheetNumericParserToken<BigDecimal> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetDecimalParserToken.class);

    static SpreadsheetDecimalParserToken with(final BigDecimal value, final String text){
        checkValue(value);
        checkText(text);

        return new SpreadsheetDecimalParserToken(value, text);
    }

    private SpreadsheetDecimalParserToken(final BigDecimal value, final String text){
        super(value, text);
    }

    @Override
    public SpreadsheetDecimalParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetDecimalParserToken replaceText(final String text) {
        return new SpreadsheetDecimalParserToken(this.value, text);
    }

    @Override
    public boolean isDecimal() {
        return true;
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
    public boolean isNumber() {
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
        return other instanceof SpreadsheetDecimalParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
