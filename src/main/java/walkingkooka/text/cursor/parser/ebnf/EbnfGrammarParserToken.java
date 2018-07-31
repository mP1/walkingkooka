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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinators;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A grammar holds all the rules and is the root of the graph.
 */
public final class EbnfGrammarParserToken extends EbnfParentParserToken {

    public final static ParserTokenNodeName NAME = ParserTokenNodeName.fromClass(EbnfGrammarParserToken.class);

    static EbnfGrammarParserToken with(final List<ParserToken> tokens, final String text) {
        Objects.requireNonNull(tokens, "rules");
        checkText(text);

        final List<ParserToken> copy = Lists.array();
        copy.addAll(tokens);

        final List<ParserToken> onlyRules = copy.stream()
                .filter(t -> t instanceof EbnfRuleParserToken)
                .collect(Collectors.toList());
        if(onlyRules.isEmpty()){
            throw new IllegalArgumentException("Missing rules=" + text);
        }

        return new EbnfGrammarParserToken(copy, text, WITHOUT_COMPUTE_REQUIRED);
    }

    EbnfGrammarParserToken(final List<ParserToken> tokens, final String text, final boolean computeWithout) {
        super(tokens, text, computeWithout);
        this.checkAtLeastOneRule();
    }

    @Override
    public EbnfGrammarParserToken setText(String text) {
        return this.setText0(text).cast();
    }

    @Override
    EbnfGrammarParserToken replaceText(final String text) {
        return new EbnfGrammarParserToken(this.value(), text, WITHOUT_COMPUTE_REQUIRED);
    }

    @Override
    EbnfGrammarParserToken replaceTokens(final List<ParserToken> tokens) {
        return new EbnfGrammarParserToken(tokens, this.text(), WITHOUT_USE_THIS);
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
        return true;
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
    public ParserTokenNodeName name() {
        return NAME;
    }

    @Override
    public void accept(final EbnfParserTokenVisitor visitor) {
        if(Visiting.CONTINUE == visitor.startVisit(this)) {
            this.acceptValues(visitor);
        }
        visitor.endVisit(this);
    }

    /**
     * Constant to be passed to {@link #checkIdentifiers(Set)} if no external references exist.
     */
    public final static Set<EbnfIdentifierName> NO_EXTERNALS = Sets.empty();

    /**
     * Verifies that all identifiers that appear on the RHS of all rules, must be valid.
     */
    public void checkIdentifiers(final Set<EbnfIdentifierName> external){
        Objects.requireNonNull(external, "external");

        final EbnfGrammarParserTokenReferenceCollectorEbnfParserTokenVisitor visitor = new EbnfGrammarParserTokenReferenceCollectorEbnfParserTokenVisitor();
        visitor.accept(this);

        final Map<EbnfIdentifierName, Set<EbnfRuleParserToken>> identifiers = visitor.ruleIdentifiers;
        final Set<EbnfRuleParserToken> duplicates = Sets.ordered();

        identifiers.values()
                .stream()
                .filter(e -> e.size() > 1)
                .forEach(e -> duplicates.addAll(e));
        if(!duplicates.isEmpty()){
            throw new EbnfGrammarParserTokenDuplicateIdentifiersException(duplicates.size() + " rules with the same identifier=" + duplicates, duplicates);
        }

        final Set<EbnfIdentifierName> missing = Sets.sorted();
        missing.addAll(visitor.references);
        missing.removeAll(identifiers.keySet());
        missing.removeAll(external);

        if(!missing.isEmpty()){
            throw new EbnfGrammarParserTokenInvalidReferencesException(missing.size() + " invalid (unknown) references=" + missing, missing);
        }
    }

    public <C extends ParserContext> Map<EbnfIdentifierName, Parser<ParserToken, C>> combinator(final Map<EbnfIdentifierName, Parser<ParserToken, C>> identifierToParser,
                                                                                                final EbnfParserCombinatorSyntaxTreeTransformer<C> syntaxTreeTransformer) {
        return EbnfParserCombinators.transform(this, identifierToParser, syntaxTreeTransformer);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof EbnfGrammarParserToken;
    }
}
