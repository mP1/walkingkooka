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
public final class SpreadsheetNegativeParserToken extends SpreadsheetUnaryParserToken<SpreadsheetNegativeParserToken> {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(SpreadsheetNegativeParserToken.class);

    static SpreadsheetNegativeParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);
        checkText(text);
        return new SpreadsheetNegativeParserToken(copy,
                text,
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetNegativeParserToken(final List<ParserToken> value, final String text, final List<ParserToken> valueWithout){
        super(value, text, valueWithout);
    }

    @Override
    public SpreadsheetNegativeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    public SpreadsheetNegativeParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    SpreadsheetParentParserToken replace(final List<ParserToken> tokens, final String text, final List<ParserToken> without) {
        return new SpreadsheetNegativeParserToken(tokens,
                text,
                without);
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
