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

/**
 * Holds a single double precision number.
 */
public final class SpreadsheetDoubleParserToken extends SpreadsheetNumericParserToken<Double> {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetDoubleParserToken.class);

    static SpreadsheetDoubleParserToken with(final double value, final String text){
        checkText(text);

        return new SpreadsheetDoubleParserToken(value, text);
    }

    private SpreadsheetDoubleParserToken(final Double value, final String text){
        super(value, text);
    }

    @Override
    public SpreadsheetDoubleParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetDoubleParserToken replaceText(final String text) {
        return new SpreadsheetDoubleParserToken(this.value, text);
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
    public boolean isDouble() {
        return true;
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
        return other instanceof SpreadsheetDoubleParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    // HasSearchNode ...............................................................................................

    @Override
    public SearchNode toSearchNode()  {
        return SearchNode.doubleNode(this.text(), this.value());
    }
}
