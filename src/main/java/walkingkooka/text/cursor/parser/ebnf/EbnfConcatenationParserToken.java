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
 * Represents a concatenation of tokens in the grammar.
 */
public final class EbnfConcatenationParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfConcatenationParserToken.class);

    static EbnfConcatenationParserToken with(final List<ParserToken> tokens, final String text) {
        return new EbnfConcatenationParserToken(copyAndCheckTokens(tokens), checkText(text), WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfConcatenationParserToken(final List<ParserToken> tokens, final String text, final List<ParserToken> valueWithout) {
        super(tokens, text, valueWithout);
        this.checkAtLeastTwoTokens();
    }

    @Override
    public EbnfConcatenationParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfConcatenationParserToken replaceText(final String text) {
        return new EbnfConcatenationParserToken(this.value(), text, this.valueIfWithoutCommentsSymbolsOrWhitespaceOrNull());
    }

    @Override
    EbnfConcatenationParserToken replaceTokens(final List<ParserToken> tokens) {
        return new EbnfConcatenationParserToken(tokens, text(), tokens);
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isConcatenation() {
        return true;
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
    public boolean isRepeated() {
        return false;
    }

    @Override
    public boolean isRange() {
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
        return other instanceof EbnfConcatenationParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
