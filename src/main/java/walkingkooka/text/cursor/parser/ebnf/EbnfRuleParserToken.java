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
 * Represents a single rule definition within a grammar.
 */
public final class EbnfRuleParserToken extends EbnfParentParserToken<EbnfRuleParserToken> {

    static EbnfRuleParserToken with(final List<ParserToken> tokens, final String text) {
        final List<ParserToken> copy = copyAndCheckTokens(tokens);
        checkText(text);

        return new EbnfRuleParserToken(copy,
                text);
    }

    private EbnfRuleParserToken(final List<ParserToken> tokens,
                                final String text) {
        super(tokens, text);

        final EbnfRuleParserTokenConsumer checker = new EbnfRuleParserTokenConsumer();
        tokens.stream()
                .filter(t -> t instanceof EbnfParserToken)
                .map(t -> EbnfParserToken.class.cast(t))
                .forEach(checker);

        final EbnfIdentifierParserToken identifier = checker.identifier;
        if (null == identifier) {
            throw new IllegalArgumentException("Rule missing Identifier on lhs=" + text);
        }
        final EbnfParserToken token = checker.token;
        if (null == token) {
            throw new IllegalArgumentException("Rule missing Token on rhs=" + text);
        }

        this.identifier = identifier;
        this.token = token;
    }

    public EbnfIdentifierParserToken identifier() {
        return this.identifier;
    }

    private final EbnfIdentifierParserToken identifier;

    public EbnfParserToken token() {
        return this.token;
    }

    private final EbnfParserToken token;

    // isXXX............................................................................................................

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
        return false;
    }

    @Override
    public boolean isRepeated() {
        return false;
    }

    @Override
    public boolean isRule() {
        return true;
    }

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
        return other instanceof EbnfRuleParserToken;
    }
}
