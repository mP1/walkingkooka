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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinators;
import walkingkooka.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A grammar holds all the rules and is the root of the graph. Note the {@link #value()} will contain a mixture of rules,
 * comments and whitespace.
 */
public final class EbnfGrammarParserToken extends EbnfParentParserToken<EbnfGrammarParserToken> {

    static EbnfGrammarParserToken with(final List<ParserToken> tokens, final String text) {
        Objects.requireNonNull(tokens, "tokens");
        checkText(text);

        return new EbnfGrammarParserToken(Lists.immutable(tokens),
                text);
    }

    private EbnfGrammarParserToken(final List<ParserToken> tokens, final String text) {
        super(tokens, text);

        final Optional<ParserToken> firstRule = tokens.stream()
                .filter(t -> t instanceof EbnfRuleParserToken)
                .findFirst();
        if (!firstRule.isPresent()) {
            throw new IllegalArgumentException("Grammar requires at least 1 rule=" + tokens);
        }
    }

    /**
     * Constant to be passed to {@link #checkIdentifiers(Set)} if no external references exist.
     */
    public final static Set<EbnfIdentifierName> NO_EXTERNALS = Sets.empty();

    /**
     * Verifies that all identifiers that appear on the RHS of all rules, must be valid.
     */
    public void checkIdentifiers(final Set<EbnfIdentifierName> external) {
        Objects.requireNonNull(external, "external");

        final EbnfGrammarParserTokenReferenceCollectorEbnfParserTokenVisitor visitor = EbnfGrammarParserTokenReferenceCollectorEbnfParserTokenVisitor.with();
        visitor.accept(this);

        final Map<EbnfIdentifierName, Set<EbnfRuleParserToken>> identifiers = visitor.ruleIdentifiers;
        final Set<EbnfRuleParserToken> duplicates = Sets.ordered();

        identifiers.values()
                .stream()
                .filter(e -> e.size() > 1)
                .forEach(e -> duplicates.addAll(e));
        if (!duplicates.isEmpty()) {
            throw new EbnfGrammarParserTokenDuplicateIdentifiersException(duplicates.size() + " rules with the same identifier=" + duplicates, duplicates);
        }

        final Set<EbnfIdentifierName> missing = Sets.sorted();
        missing.addAll(visitor.references);
        missing.removeAll(identifiers.keySet());
        missing.removeAll(external);

        if (!missing.isEmpty()) {
            throw new EbnfGrammarParserTokenInvalidReferencesException(missing.size() + " invalid (unknown) references=" + missing, missing);
        }
    }

    public <C extends ParserContext> Map<EbnfIdentifierName, Parser<ParserContext>> combinator(final Map<EbnfIdentifierName, Parser<ParserContext>> identifierToParser,
                                                                                               final EbnfParserCombinatorSyntaxTreeTransformer transformer) {
        return EbnfParserCombinators.transform(this, identifierToParser, transformer);
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
        return other instanceof EbnfGrammarParserToken;
    }
}
