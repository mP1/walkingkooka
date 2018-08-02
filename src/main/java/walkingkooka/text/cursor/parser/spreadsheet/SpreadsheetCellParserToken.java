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
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * A reference that includes a defined name or column and row.
 */
public final class SpreadsheetCellParserToken extends SpreadsheetParentParserToken implements SpreadsheetReferenceParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetCellParserToken.class);

    static SpreadsheetCellParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);

        final SpreadsheetCellParserTokenConsumer checker = new SpreadsheetCellParserTokenConsumer();
        copy.stream()
                .filter(t -> t instanceof SpreadsheetParserToken)
                .map(t -> SpreadsheetParserToken.class.cast(t))
                .forEach(checker);
        final SpreadsheetRowParserToken row = checker.row;
        if(null==row){
            throw new IllegalArgumentException("Row missing from cell=" + text);
        }
        final SpreadsheetColumnParserToken column = checker.column;
        if(null==column){
            throw new IllegalArgumentException("Column missing from cell=" + text);
        }

        return new SpreadsheetCellParserToken(copy,
                checkText(text),
                row.value(),
                column.value(),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetCellParserToken(final List<ParserToken> value, final String text, final SpreadsheetRow row, final SpreadsheetColumn column, final boolean computeWithout){
        super(value, text, computeWithout);
        this.row = row;
        this.column = column;
    }

    public SpreadsheetRow row() {
        return this.row;
    }

    private final SpreadsheetRow row;

    public SpreadsheetColumn column() {
        return this.column;
    }

    private final SpreadsheetColumn column;

    @Override
    public SpreadsheetCellParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetCellParserToken replaceText(final String text) {
        return new SpreadsheetCellParserToken(this.value, text, this.row, this.column, WITHOUT_USE_THIS);
    }

    @Override
    SpreadsheetParentParserToken replaceTokens(final List<ParserToken> tokens) {
        return new SpreadsheetCellParserToken(tokens, this.text(), this.row, this.column, WITHOUT_USE_THIS);
    }

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isCell() {
        return true;
    }

    @Override
    public boolean isDivision() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isMultiplication() {
        return false;
    }

    @Override
    public boolean isPower() {
        return false;
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public boolean isPercentage() {
        return false;
    }

    @Override
    public boolean isRange() {
        return false;
    }

    @Override
    public boolean isSubtraction() {
        return false;
    }

    @Override
    public void accept(final SpreadsheetParserTokenVisitor visitor){
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SpreadsheetCellParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
