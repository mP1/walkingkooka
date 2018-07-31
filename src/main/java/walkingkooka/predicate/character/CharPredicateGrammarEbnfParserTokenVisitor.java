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

package walkingkooka.predicate.character;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserTokenVisitor;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.tree.visit.Visiting;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A visitor that accepts a grammar and transforms all tokens in {@link CharPredicate predicates} placing each rule into a map.
 */
final class CharPredicateGrammarEbnfParserTokenVisitor extends EbnfParserTokenVisitor {

    static Map<EbnfIdentifierName, CharPredicate> fromGrammar(final EbnfGrammarParserToken grammar,
                                                              final Map<EbnfIdentifierName, CharPredicate> predefined) {
        Objects.requireNonNull(grammar, "grammar");
        Objects.requireNonNull(predefined, "predefined");

        grammar.checkIdentifiers(predefined.keySet());

        final Map<EbnfIdentifierName, CharPredicate> copy = Maps.ordered();
        copy.putAll(predefined);

        new CharPredicateGrammarEbnfParserTokenVisitor(copy).accept(grammar);

        return Maps.readOnly(copy);
    }

    private CharPredicateGrammarEbnfParserTokenVisitor(final Map<EbnfIdentifierName, CharPredicate> identifierToCharPredicate){
        this.identifierToCharPredicate = identifierToCharPredicate;
    }

    // GRAMMAR ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfGrammarParserToken token) {
        // need this mapping to fetch tokens for a rule by identifier at any stage or walking...
        token.value()
                .stream()
                .filter(t -> t instanceof EbnfParserToken)
                .map(t -> EbnfParserToken.class.cast(t))
                .filter( t -> t.isRule())
                .map( t -> EbnfRuleParserToken.class.cast(t))
                .forEach(this::ruleIdentifier);
        return super.startVisit(token);
    }

    private void ruleIdentifier(final EbnfRuleParserToken rule) {
        final EbnfIdentifierName identifier = rule.identifier().value();
        this.identifierToRule.put(identifier, rule);
        this.identifierToCharPredicate.put(identifier, new CharPredicateGrammarEbnfParserTokenVisitorProxy(identifier));
    }

    // RULE ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRuleParserToken rule) {
        this.enter();
        this.accept(rule.token()); // RHS

        // update the proxy holding all references to this rule...
        final CharPredicateGrammarEbnfParserTokenVisitorProxy proxy = Cast.to(this.identifierToCharPredicate.get(rule.identifier().value()));
        proxy.predicate = this.children.get(0);

        return Visiting.SKIP; // skip because we dont want to visit LHS of rule.
    }

    private final Map<EbnfIdentifierName, EbnfRuleParserToken> identifierToRule = Maps.ordered();

    // ALT .......................................................................................................

    @Override
    protected Visiting startVisit(final EbnfAlternativeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfAlternativeParserToken token) {
        final CharPredicateBuilder b = CharPredicates.builder();
        for(CharPredicate p : this.children){
            b.or(p);
        }
        this.exit();
        this.add(
                b.build().setToString(token.toString()),
                token);
    }

    // CONCAT .......................................................................................................

    @Override
    protected Visiting startVisit(final EbnfConcatenationParserToken token) {
        return this.fail("Concatenation", token);
    }

    // EXCEPTION .......................................................................................................

    @Override
    protected Visiting startVisit(final EbnfExceptionParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfExceptionParserToken token) {
        final CharPredicate predicate = this.children.get(0)
                .andNot(this.children.get(1))
                .setToString(token.toString());

        this.exit();
        this.add(predicate, token);
    }

    // OPTIONAL .......................................................................................................

    @Override
    protected Visiting startVisit(final EbnfOptionalParserToken token) {
        return this.fail("Optional", token);
    }

    // RANGE ...................................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRangeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRangeParserToken token) {
        final char begin = this.characterForIdentifierOrTerminal(token.begin());
        final char end = this.characterForIdentifierOrTerminal(token.end());

        final CharPredicate predicate = CharPredicates.range(begin, end)
                .setToString(token.toString());
        this.exit();
        this.add(
                predicate,
                token);
    }

    private char characterForIdentifierOrTerminal(final EbnfParserToken token) {
        return token.isTerminal() ?
                this.characterFromTerminal(token.cast()) :
                token.isIdentifier() ?
                        this.characterFromIdentifierReference(token.cast()) :
                        failInvalidRangeBound("Invalid range bound, expected terminal or identifier indirectly pointing to a terminal but got " + token, token);
    }

    private char characterFromIdentifierReference(final EbnfIdentifierParserToken identifier) {
        final EbnfRuleParserToken rule = this.identifierToRule.get(identifier.value());
        return this.characterForIdentifierOrTerminal(rule.token());
    }

    private char characterFromTerminal(final EbnfTerminalParserToken terminal) {
        final String value = terminal.value();
        final CharSequence unescaped = CharSequences.unescape(value);
        if(unescaped.length() != 1) {
            failInvalidRangeBound("The range terminal does not contain a single character=" + terminal, terminal);
        }
        return unescaped.charAt(0);
    }

    private static char failInvalidRangeBound(final String message, final EbnfParserToken token) {
        throw new IllegalArgumentException(message);
    }

    // REPEATED .......................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRepeatedParserToken token) {
        return this.fail("Repeated", token);
    }

    // IDENTIFIER .......................................................................................................

    @Override
    protected void visit(final EbnfIdentifierParserToken token) {
        this.add(
                this.identifierToCharPredicate.get(token.value()),
                token);
    }

    // TERMINAL .......................................................................................................

    @Override
    protected void visit(final EbnfTerminalParserToken token) {
        this.add(
                CharPredicates.any(token.value()).setToString(token.toString()),
                token);
    }

    // GENERAL PURPOSE .................................................................................................

    final Map<EbnfIdentifierName, CharPredicate> identifierToCharPredicate;

    private void enter(){
        this.previousChildren = this.previousChildren.push(this.children);
        this.children = Lists.array();
    }

    private void exit() {
        this.children = this.previousChildren.peek();
        this.previousChildren = this.previousChildren.pop();
    }

    private Stack<List<CharPredicate>> previousChildren = Stacks.arrayList();

    private List<CharPredicate> children;

    private void add(final CharPredicate predicate, final EbnfParserToken token) {
        if(null == predicate) {
            throw new NullPointerException("Null predicate returned for " + token);
        }
        this.children.add(predicate);
    }

    private Visiting fail(final String label, final EbnfParserToken token) {
        throw new UnsupportedOperationException(label + " tokens not supported in CharPredicate grammar=" + token);
    }

    public String toString() {
        return this.children.toString();
    }
}
