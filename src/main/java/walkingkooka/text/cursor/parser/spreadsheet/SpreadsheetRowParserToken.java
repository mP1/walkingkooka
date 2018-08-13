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
import walkingkooka.text.cursor.parser.ParserTokenNodeName;

import java.util.Optional;

/**
 * A row name.
 */
public final class SpreadsheetRowParserToken extends SpreadsheetLeafParserToken2<SpreadsheetRow> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetRowParserToken.class);

    static SpreadsheetRowParserToken with(final SpreadsheetRow value, final String text){
        checkValue(value);
        CharSequences.failIfNullOrEmpty(text, "text");

        return new SpreadsheetRowParserToken(value, text);
    }

    private SpreadsheetRowParserToken(final SpreadsheetRow value, final String text){
        super(value, text);
    }

    @Override
    public SpreadsheetRowParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetRowParserToken replaceText(final String text) {
        return new SpreadsheetRowParserToken(this.value, text);
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
        return false;
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
    public boolean isSymbol() {
        return false;
    }

    @Override
    public boolean isRow() {
        return true;
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
        return other instanceof SpreadsheetRowParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
