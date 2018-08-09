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
 * A function which includes its name and any parameters if any. Each parameter will be separated by a comma.
 * <br>
 * SUM(A10:A20)
 */
public final class SpreadsheetFunctionParserToken extends SpreadsheetParentParserToken {

    public final static ParserTokenNodeName NAME = parserTokenNodeName(SpreadsheetFunctionParserToken.class);

    static SpreadsheetFunctionParserToken with(final List<ParserToken> value, final String text){
        final List<ParserToken> copy = copyAndCheckTokens(value);
        checkText(text);

        final SpreadsheetFunctionParserTokenConsumer checker = new SpreadsheetFunctionParserTokenConsumer();
        copy.stream()
             .filter(t -> t instanceof SpreadsheetParserToken)
             .map(t -> SpreadsheetParserToken.class.cast(t))
             .forEach(checker);
        final SpreadsheetFunctionName name = checker.name;
        if(null==name){
            throw new IllegalArgumentException("Function name missing from " + value);
        }

        return new SpreadsheetFunctionParserToken(copy,
                text,
                name,
                checker.parameters,
                WITHOUT_COMPUTE_REQUIRED);
    }

    private SpreadsheetFunctionParserToken(final List<ParserToken> value,
                                           final String text,
                                           final SpreadsheetFunctionName name,
                                           final List<ParserToken> parameters,
                                           final boolean computeWithout){
        super(value, text, computeWithout);

        this.name = name;
        this.parameters = parameters;
    }

    /**
     * The name of the function
     */
    public SpreadsheetFunctionName functionName() {
        return this.name;
    }

    private final SpreadsheetFunctionName name;

    public List<ParserToken> parameters() {
        return this.parameters;
    }

    private final List<ParserToken> parameters;

    @Override
    public SpreadsheetFunctionParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    SpreadsheetFunctionParserToken replaceText(final String text) {
        return new SpreadsheetFunctionParserToken(this.value, text, this.name, this.parameters, WITHOUT_USE_THIS);
    }

    @Override
    SpreadsheetParentParserToken replaceTokens(final List<ParserToken> tokens) {
        // this method is called before the name and parameter fields are set.
        return new SpreadsheetFunctionParserToken(tokens,
                this.text(),
                SpreadsheetFunctionNameParserToken.class.cast(tokens.get(0)).value(),
                tokens.subList(1, tokens.size()),
                WITHOUT_USE_THIS);
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
    public boolean isGroup() {
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
    public boolean isNegative() {
        return false;
    }
    
    @Override
    public boolean isNotEquals() {
        return false;
    }
    
    @Override
    public boolean isPercentage() {
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
        return other instanceof SpreadsheetFunctionParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
