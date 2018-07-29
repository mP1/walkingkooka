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
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Represents a subtraction operation with its parameters.
 */
public final class SpreadsheetSubtractionParserToken extends SpreadsheetBinaryParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.with("SpreadsheetSubtraction");

    static SpreadsheetSubtractionParserToken with(final List<SpreadsheetParserToken> value, final String text){
        return new SpreadsheetSubtractionParserToken(copyAndCheckTokens(value),
                checkText(text),
                NO_PARAMETER,
                NO_PARAMETER,
                WITHOUT_COMPUTE_REQUIRED);
    }

    private static final SpreadsheetNumericParserToken NO_NUMBER = null;

    private SpreadsheetSubtractionParserToken(final List<SpreadsheetParserToken> value, final String text, final SpreadsheetParserToken left, final SpreadsheetParserToken right, final boolean computeWithout){
        super(value, text, left, right, computeWithout);
    }

    @Override
    public SpreadsheetSubtractionParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetSubtractionParserToken replaceText(final String text) {
        return new SpreadsheetSubtractionParserToken(this.value, text, this.left, this.right, WITHOUT_USE_THIS);
    }

    @Override
    SpreadsheetParentParserToken replaceTokens(final List<SpreadsheetParserToken> tokens) {
        return new SpreadsheetSubtractionParserToken(tokens, this.text(), this.left, this.right, WITHOUT_USE_THIS);
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
    public boolean isMultiplication() {
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
        return true;
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
        return other instanceof SpreadsheetSubtractionParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
