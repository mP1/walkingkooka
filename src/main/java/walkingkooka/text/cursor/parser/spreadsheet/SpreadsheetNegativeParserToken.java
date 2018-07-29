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
 * A wrapper around a numeric type that is also a percentage.
 */
public final class SpreadsheetNegativeParserToken extends SpreadsheetUnaryParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.with("SpreadsheetNegative");

    static SpreadsheetNegativeParserToken with(final List<SpreadsheetParserToken> value, final String text){
        return new SpreadsheetNegativeParserToken(copyAndCheckTokens(value),
                checkText(text),
                NO_PARAMETER,
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetNegativeParserToken(final List<SpreadsheetParserToken> value, final String text, final SpreadsheetParserToken parameter, final boolean computeWithout){
        super(value, text, parameter, computeWithout);
    }

    @Override
    public SpreadsheetNegativeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetNegativeParserToken replaceText(final String text) {
        return new SpreadsheetNegativeParserToken(this.value, text, this.parameter, WITHOUT_USE_THIS);
    }

    @Override
    SpreadsheetParentParserToken replaceTokens(final List<SpreadsheetParserToken> tokens) {
        return new SpreadsheetNegativeParserToken(tokens, this.text(), this.parameter, WITHOUT_USE_THIS);
    }

    @Override
    public boolean isNegative() {
        return true;
    }

    @Override
    public boolean isPercentage() {
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
        return other instanceof SpreadsheetNegativeParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
