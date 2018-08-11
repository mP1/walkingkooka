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
public final class SpreadsheetPercentageParserToken extends SpreadsheetUnaryParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetPercentageParserToken.class);

    static SpreadsheetPercentageParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);
        checkText(text);

        final SpreadsheetUnaryParserTokenConsumer checker = checkValue(value);

        return new SpreadsheetPercentageParserToken(copy,
                text,
                checker.value(copy),
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetPercentageParserToken(final List<ParserToken> value, final String text, final SpreadsheetParserToken parameter, final List<ParserToken> valueWithout){
        super(value, text, parameter, valueWithout);
    }

    @Override
    public SpreadsheetPercentageParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetPercentageParserToken replaceText(final String text) {
        return new SpreadsheetPercentageParserToken(this.value, text, this.parameter, this.valueIfWithoutSymbolsOrWhitespaceOrNull());
    }

    @Override
    SpreadsheetParentParserToken replaceTokens(final List<ParserToken> tokens) {
        return new SpreadsheetPercentageParserToken(tokens, this.text(), tokens.get(0).cast(), tokens);
    }

    @Override
    public boolean isNegative() {
        return false;
    }

    @Override
    public boolean isPercentage() {
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
        return other instanceof SpreadsheetPercentageParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
