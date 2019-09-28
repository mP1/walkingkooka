/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.visit.Visiting;

import java.util.List;

/**
 * Represents an exception token in the grammar. Note the grammar requires an exception to follow another token.
 */
public final class EbnfExceptionParserToken extends EbnfParentParserToken<EbnfExceptionParserToken> {

    static EbnfExceptionParserToken with(final List<ParserToken> tokens, final String text) {
        return new EbnfExceptionParserToken(copyAndCheckTokens(tokens), checkText(text));
    }

    private EbnfExceptionParserToken(final List<ParserToken> tokens, final String text) {
        super(tokens, text);
        ;

        final List<ParserToken> without = this.checkOnlyTwoTokens();
        this.token = without.get(0).cast();
        this.exception = without.get(1).cast();
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

    // EbnfParserTokenVisitor............................................................................................

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        if (Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfExceptionParserToken;
    }

}
