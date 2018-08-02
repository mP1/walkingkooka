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
 * Represents a division operation with its parameters.
 */
public final class SpreadsheetDivisionParserToken extends SpreadsheetBinaryParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetDivisionParserToken.class);

    static SpreadsheetDivisionParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);
        checkText(text);

        final SpreadsheetBinaryParserTokenConsumer checker = checkLeftAndRight(value);

        return new SpreadsheetDivisionParserToken(copy,
                text,
                checker.left(copy),
                checker.right(copy),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private static final SpreadsheetNumericParserToken NO_NUMBER = null;

    private SpreadsheetDivisionParserToken(final List<ParserToken> value, final String text, final SpreadsheetParserToken left, final SpreadsheetParserToken right, final boolean computeWithout){
        super(value, text, left, right, computeWithout);
    }

    @Override
    public SpreadsheetDivisionParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetDivisionParserToken replaceText(final String text) {
        return this.replace(this.value, text);
    }

    @Override
    SpreadsheetDivisionParserToken replaceTokens(final List<ParserToken> tokens) {
        return this.replace(tokens, this.text());
    }

    private SpreadsheetDivisionParserToken replace(final List<ParserToken> tokens, final String text) {
        return new SpreadsheetDivisionParserToken(tokens, text, tokens.get(0).cast(), tokens.get(1).cast(), WITHOUT_USE_THIS);
    }

    @Override
    public boolean isAddition() {
        return false;
    }

    @Override
    public boolean isDivision() {
        return true;
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
        return other instanceof SpreadsheetDivisionParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
