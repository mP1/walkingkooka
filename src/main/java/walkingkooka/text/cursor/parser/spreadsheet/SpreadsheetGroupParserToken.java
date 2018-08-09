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
 * A wrapper around a numeric type that is also a percentage.
 */
public final class SpreadsheetGroupParserToken extends SpreadsheetParentParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetGroupParserToken.class);

    static SpreadsheetGroupParserToken with(final List<ParserToken> value, final String text){
        return new SpreadsheetGroupParserToken(copyAndCheckTokens(value),
                checkText(text),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetGroupParserToken(final List<ParserToken> value, final String text, final boolean computeWithout){
        super(value, text, computeWithout);
    }

    @Override
    public SpreadsheetGroupParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetGroupParserToken replaceText(final String text) {
        return new SpreadsheetGroupParserToken(this.value, text, WITHOUT_USE_THIS);
    }

    @Override
    SpreadsheetParentParserToken replaceTokens(final List<ParserToken> tokens) {
        return new SpreadsheetGroupParserToken(tokens, this.text(), WITHOUT_USE_THIS);
    }

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isCell() {
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
    public boolean isFunction() {
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
    public boolean isGroup() {
        return true;
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
        return false;
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
        return other instanceof SpreadsheetGroupParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
