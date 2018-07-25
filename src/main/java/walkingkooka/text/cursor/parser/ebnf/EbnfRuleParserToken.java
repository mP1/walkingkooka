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

import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.tree.visit.Visiting;

import java.util.List;

/**
 * Represents a single rule definition within a grammar.
 */
public final class EbnfRuleParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfRuleParserToken.class);

    static EbnfRuleParserToken with(final List<EbnfParserToken> tokens, final String text) {
        final List<EbnfParserToken> copy = copyAndCheckTokens(tokens);
        checkText(text);

        final EbnfRuleParserTokenConsumer checker = new EbnfRuleParserTokenConsumer();
        tokens.stream()
                .filter(t -> t instanceof EbnfParserToken)
                .forEach(checker);

        final EbnfIdentifierParserToken identifier = checker.identifier;
        if(null==identifier){
            throw new IllegalArgumentException("Rule missing Identifier on lhs=" + text);
        }
        final EbnfParserToken token = checker.token;
        if(null==token){
            throw new IllegalArgumentException("Rule missing Token on rhs=" + text);
        }

        return new EbnfRuleParserToken(copy,
                text,
                identifier,
                token,
                WITHOUT_COMPUTE_REQUIRED);
    }

    private EbnfRuleParserToken(final List<EbnfParserToken> tokens,
                        final String text,
                        final EbnfIdentifierParserToken identifier,
                        final EbnfParserToken token,
                        final boolean computeWithout) {
        super(tokens, text, computeWithout);
        this.identifier = identifier;
        this.token = token;
    }

    @Override
    public EbnfRuleParserToken setText(String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfRuleParserToken replaceText(final String text) {
        return new EbnfRuleParserToken(this.value(), text, this.identifier, this.token, WITHOUT_USE_THIS);
    }

    public EbnfIdentifierParserToken identifier() {
        return this.identifier;
    }

    private final EbnfIdentifierParserToken identifier;

    public EbnfParserToken token() {
        return this.token;
    }

    private final EbnfParserToken token;

    @Override
    EbnfRuleParserToken replaceTokens(final List<EbnfParserToken> tokens) {
        // cant use this.identifier and this.tokens because they wont be initialized yet
        return new EbnfRuleParserToken(tokens,
                this.text(),
                tokens.get(0).cast(),
                tokens.get(1),
                WITHOUT_USE_THIS);
    }

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

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    @Override
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfRuleParserToken;
    }
}
