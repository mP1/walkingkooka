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
 * Represents a multiplication operation with its parameters.
 */
public final class SpreadsheetMultiplicationParserToken extends SpreadsheetBinaryParserToken<SpreadsheetMultiplicationParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetMultiplicationParserToken.class);

    static SpreadsheetMultiplicationParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);

        return new SpreadsheetMultiplicationParserToken(copy,
                text,
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetMultiplicationParserToken(final List<ParserToken> value, final String text,  final List<ParserToken> valueWithout){
        super(value, text, valueWithout);
    }

    @Override
    public SpreadsheetMultiplicationParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    public SpreadsheetMultiplicationParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    SpreadsheetMultiplicationParserToken replace(final List<ParserToken> tokens, final String text, final List<ParserToken> without) {
        return new SpreadsheetMultiplicationParserToken(tokens,
                text,
                without);
    }

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isDivision() {
        return false;
    }

    @Override
    public boolean isEquals() {
        return false;
    }

    @Override
    public boolean isGreaterThan() {
        return false;
    }

    @Override
    public boolean isGreaterThanEquals() {
        return false;
    }

    @Override
    public boolean isLessThan() {
        return false;
    }

    @Override
    public boolean isLessThanEquals() {
        return false;
    }

    @Override
    public boolean isMultiplication() {
        return true;
    }

    @Override
    public boolean isNotEquals() {
        return false;
    }

    @Override
    public boolean isPower() {
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
        return other instanceof SpreadsheetMultiplicationParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
