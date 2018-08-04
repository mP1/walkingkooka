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
final public class EbnfAlternativeParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfAlternativeParserToken.class);

    static EbnfAlternativeParserToken with(final List<ParserToken> tokens, final String text) {
        return new EbnfAlternativeParserToken(copyAndCheckTokens(tokens), checkText(text), WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfAlternativeParserToken(final List<ParserToken> tokens, final String text, final List<ParserToken> valueWithout) {
        super(tokens, text, valueWithout);
        this.checkAtLeastTwoTokens();
    }

    @Override
    public EbnfAlternativeParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfAlternativeParserToken replaceText(final String text) {
        return new EbnfAlternativeParserToken(this.value(), text, this.valueIfWithoutCommentsSymbolsOrWhitespaceOrNull());
    }

    @Override
    EbnfAlternativeParserToken replaceTokens(final List<ParserToken> tokens) {
        return new EbnfAlternativeParserToken(tokens, this.text(), tokens);
    }

    @Override
    public boolean isAlternative() {
        return true;
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
        return false;
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
        return other instanceof EbnfAlternativeParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
