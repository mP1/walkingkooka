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
 * Represents a equals test with its parameters.
 */
public final class SpreadsheetEqualsParserToken extends SpreadsheetBinaryParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetEqualsParserToken.class);

    static SpreadsheetEqualsParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);
        checkText(text);

        final SpreadsheetBinaryParserTokenConsumer checker = checkLeftAndRight(value);

        return new SpreadsheetEqualsParserToken(copy,
                text,
                checker.left(copy),
                checker.right(copy),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetEqualsParserToken(final List<ParserToken> value, final String text, final SpreadsheetParserToken left, final SpreadsheetParserToken right, final boolean computeWithout){
        super(value, text, left, right, computeWithout);
    }

    @Override
    public SpreadsheetEqualsParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetEqualsParserToken replaceText(final String text) {
        return this.replace(this.value, text);
    }

    @Override
    SpreadsheetEqualsParserToken replaceTokens(final List<ParserToken> tokens) {
        return this.replace(tokens, this.text());
    }

    private SpreadsheetEqualsParserToken replace(final List<ParserToken> tokens, final String text) {
        return new SpreadsheetEqualsParserToken(tokens, text, tokens.get(0).cast(), tokens.get(1).cast(), WITHOUT_USE_THIS);
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
        return true;
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
        return other instanceof SpreadsheetEqualsParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
