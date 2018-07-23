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
package walkingkooka.text.cursor.parser.ebnf;

import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Represents an exception token in the grammar. Note the grammar requires an exception to follow another token.
 */
public final class EbnfExceptionParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfExceptionParserToken.class);

    static EbnfExceptionParserToken with(final List<EbnfParserToken> tokens, final String text) {
        return new EbnfExceptionParserToken(copyAndCheckTokens(tokens), checkText(text), WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfExceptionParserToken(final List<EbnfParserToken> tokens, final String text, final boolean computeWithout) {
        super(tokens, text, computeWithout);
        this.checkOnlyOneToken();

        final EbnfExceptionParserToken exception = this.withoutCommentsSymbolsOrWhitespace().get().cast();
        this.token = exception.value().get(0);
    }

    @Override
    public EbnfExceptionParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfExceptionParserToken replaceText(final String text) {
        return new EbnfExceptionParserToken(this.value(), text, WITHOUT_COMPUTE_REQUIRED);
    }

    @Override
    EbnfExceptionParserToken replaceTokens(final List<EbnfParserToken> tokens) {
        return new EbnfExceptionParserToken(tokens, this.text(), WITHOUT_USE_THIS);
    }

    /**
     * Returns the actual token the exception applies too.
     */
    public EbnfParserToken token() {
        return this.token;
    }

    private final EbnfParserToken token;

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
        return true;
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
        return other instanceof EbnfExceptionParserToken;
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }
}
