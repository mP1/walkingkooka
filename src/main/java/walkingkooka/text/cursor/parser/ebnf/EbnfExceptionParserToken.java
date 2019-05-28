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

import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Represents an exception token in the grammar. Note the grammar requires an exception to follow another token.
 */
public final class EbnfExceptionParserToken extends EbnfParentParserToken<EbnfExceptionParserToken> {

    static EbnfExceptionParserToken with(final List<ParserToken> tokens, final String text) {
        return new EbnfExceptionParserToken(copyAndCheckTokens(tokens), checkText(text), WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfExceptionParserToken(final List<ParserToken> tokens, final String text, final List<ParserToken> valueWithout) {
        super(tokens, text, valueWithout);
        this.checkOnlyTwoTokens();

        final EbnfExceptionParserToken without = this.withoutCommentsSymbolsOrWhitespace().get().cast();
        final List<ParserToken> withoutTokens = without.value();
        this.token = withoutTokens.get(0).cast();
        this.exception = withoutTokens.get(1).cast();
    }

    @Override
    public EbnfExceptionParserToken setText(final String text) {
        return this.setText0(text).cast();
    }

    @Override
    public EbnfExceptionParserToken setValue(final List<ParserToken> value) {
        return this.setValue0(value).cast();
    }

    @Override
    EbnfExceptionParserToken replace(final List<ParserToken> tokens, final String text, final List<ParserToken> without) {
        return new EbnfExceptionParserToken(tokens, text, without);
    }

    /**
     * Returns the actual token before the exception applies too.
     */
    public EbnfParserToken token() {
        return this.token;
    }

    private final EbnfParserToken token;

    /**
     * Returns the actual token the exception applies too.
     */
    public EbnfParserToken exception() {
        return this.exception;
    }

    private final EbnfParserToken exception;

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
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfExceptionParserToken;
    }

}
