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
import walkingkooka.tree.search.SearchNode;

import java.util.Objects;
import java.util.Optional;

/**
 * Holds a text which may be empty
 */
public final class SpreadsheetTextParserToken extends SpreadsheetNonSymbolParserToken<String> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetTextParserToken.class);

    static SpreadsheetTextParserToken with(final String value, final String text){
        checkValue(value);

        return new SpreadsheetTextParserToken(value, text);
    }

    private SpreadsheetTextParserToken(final String value, final String text){
        super(value, text);
    }

    @Override
    void checkText(final String text) {
        Objects.requireNonNull(text, "text");
    }

    @Override
    public SpreadsheetTextParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetTextParserToken replaceText(final String text) {
        return new SpreadsheetTextParserToken(this.value, text);
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
    public boolean isBigInteger() {
        return false;
    }

    @Override
    public boolean isColumnReference() {
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
    public boolean isLocalDate() {
        return false;
    }

    @Override
    public boolean isLocalDateTime() {
        return false;
    }

    @Override
    public boolean isLocalTime() {
        return false;
    }

    @Override
    public boolean isLong() {
        return false;
    }

    @Override
    public boolean isRowReference() {
        return false;
    }

    @Override
    public boolean isSymbol() {
        return false;
    }

    @Override
    public boolean isText() {
        return true;
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
        return other instanceof SpreadsheetTextParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode()  {
        return SearchNode.text(this.text(), this.value());
    }
}
