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

import java.util.Optional;

/**
 * Holds the actual function name within a function token.
 */
public final class SpreadsheetFunctionNameParserToken extends SpreadsheetLeafParserToken2<SpreadsheetFunctionName> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetFunctionNameParserToken.class);

    static SpreadsheetFunctionNameParserToken with(final SpreadsheetFunctionName value, final String text){
        checkValue(value);
        checkText(text);

        return new SpreadsheetFunctionNameParserToken(value, text);
    }

    private SpreadsheetFunctionNameParserToken(final SpreadsheetFunctionName value, final String text){
        super(value, text);
    }

    @Override
    public SpreadsheetFunctionNameParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetFunctionNameParserToken replaceText(final String text) {
        return new SpreadsheetFunctionNameParserToken(this.value, text);
    }

    @Override
    public Optional<SpreadsheetParserToken> withoutSymbolsOrWhitespace() {
        return Optional.of(this);
    }

    @Override
    public boolean isBigDecimal() {
        return false;
    }

    @Override
    public boolean isColumn() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isFunctionName() {
        return true;
    }

    @Override
    public boolean isLabelName() {
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
    public boolean isRow() {
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
        return other instanceof SpreadsheetFunctionNameParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
