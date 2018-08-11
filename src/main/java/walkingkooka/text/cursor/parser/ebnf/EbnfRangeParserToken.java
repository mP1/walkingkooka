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
 */
package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Represents a list of alternative token in the grammar.
 */
final public class EbnfRangeParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfRangeParserToken.class);

    static EbnfRangeParserToken with(final List<ParserToken> tokens, final String text) {
        final List<ParserToken> copy = copyAndCheckTokens(tokens);
        checkText(text);

        final EbnfRangeParserTokenConsumer checker = new EbnfRangeParserTokenConsumer();
        tokens.stream()
                .filter(t -> t instanceof EbnfParserToken)
                .map(t -> EbnfParserToken.class.cast(t))
                .forEach(checker);

        final EbnfParserToken begin = checker.begin;
        if(null==begin){
            throw new IllegalArgumentException("Range missing begin|identifier=" + text);
        }
        final EbnfParserToken end = checker.end;
        if(null==end){
            throw new IllegalArgumentException("Range missing end terminal|identifier=" + text);
        }

        return new EbnfRangeParserToken(copy, text, begin, end, WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfRangeParserToken(final List<ParserToken> tokens,
                                 final String text,
                                 final EbnfParserToken begin,
                                 final EbnfParserToken end,
                                 final List<ParserToken> valueWithout) {
        super(tokens, text, valueWithout);
        this.checkOnlyTwoTokens();

        this.begin = begin;
        this.end = end;
    }

    @Override
    public EbnfRangeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfRangeParserToken replaceText(final String text) {
        return new EbnfRangeParserToken(this.value(), text, this.begin, this.end, this.valueIfWithoutCommentsSymbolsOrWhitespaceOrNull());
    }

    @Override
    EbnfRangeParserToken replaceTokens(final List<ParserToken> tokens) {
        // this method is only called by the ctor which happens before the begin/end fields have been set.
        return new EbnfRangeParserToken(tokens, this.text(), tokens.get(0).cast(), tokens.get(1).cast(), tokens);
    }

    public EbnfParserToken begin() {
        return this.begin;
    }

    private final EbnfParserToken begin;

    public EbnfParserToken end() {
        return this.end;
    }

    private final EbnfParserToken end;

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isConcatenation() {
        return false;
    }

    @Override
    public boolean isException() {
        return false;
    }

    @Override
    public boolean isGrammar() {
        return false;
    }

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isRange() {
        return true;
    }

    @Override
    public boolean isRepeated() {
        return false;
    }

    @Override
    public boolean isRule() {
        return false;
    }

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfRangeParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
