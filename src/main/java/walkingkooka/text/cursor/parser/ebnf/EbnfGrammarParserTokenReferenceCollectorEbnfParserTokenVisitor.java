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

import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.tree.visit.Visiting;

import java.util.Map;
import java.util.Set;

/**
 * Collects all rule identifiers and references.
 */
final class EbnfGrammarParserTokenReferenceCollectorEbnfParserTokenVisitor<C extends ParserContext> extends EbnfParserTokenVisitor {

    EbnfGrammarParserTokenReferenceCollectorEbnfParserTokenVisitor() {
        super();
    }

    // RULE ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRuleParserToken rule) {
        final EbnfIdentifierParserToken identifier = rule.identifier();
        Set<EbnfRuleParserToken> rules = this.ruleIdentifiers.get(identifier);
        if(null== rules) {
            rules = Sets.ordered();
            this.ruleIdentifiers.put(identifier, rules);
        }
        rules.add(rule);

        this.accept(rule.token()); // RHS.. visiting everything on the RHS to find identifiers which are actually references.
        return Visiting.SKIP;
    }

    // IDENTIFIER ........................................................................................................

    @Override
    protected void visit(final EbnfIdentifierParserToken identifier) {
        this.references.add(identifier);
    }

    // HELPERS ......................................................................................................

    final Map<EbnfIdentifierParserToken, Set<EbnfRuleParserToken>> ruleIdentifiers = Maps.ordered();
    final Set<EbnfIdentifierParserToken> references = Sets.ordered();

    public String toString() {
        return this.references + " " + this.ruleIdentifiers;
    }
}

