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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.text.cursor.parser.SequenceParserBuilder;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
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
import java.util.Map.Entry;

/**
 * Compiles all tokens, passing each token to the {@link EbnfParserCombinatorSyntaxTreeTransformer} allowing substitution.
 */
final class EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor<T extends ParserToken, C extends ParserContext> extends EbnfParserTokenVisitor {

    EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor(final EbnfParserCombinatorContext context) {
        super();
        this.context = context;
    }

    // GRAMMAR ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfGrammarParserToken token) {
        // need this mapping to fetch tokens for a rule by identifier at any stage or walking...
        token.value()
                .stream()
                .filter( t -> t.isRule())
                .map( t -> EbnfRuleParserToken.class.cast(t))
                .forEach( r -> {
                    this.identifierToRule.put(r.identifier(), r);
                });
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGrammarParserToken token) {
        // replace all EbnfParserCombinatorProxyParser with the parser they are wrapping. This also has the advantage that
        // all parsers display their complete source.

        this.context.identifierToParser.entrySet().forEach(identifierAndParser -> {
            this.fixRuleParserToString(identifierAndParser);
        });
        super.endVisit(token);
    }

    private void fixRuleParserToString(final Entry<EbnfIdentifierParserToken, Parser<ParserToken, C>> identifierAndParser) {
        final EbnfIdentifierParserToken identifier = identifierAndParser.getKey();
        final Parser<ParserToken, C> parser = identifierAndParser.getValue();

        // Not all parsers may be proxy, because some parsers may be present in the initial map.
        if(parser instanceof EbnfParserCombinatorProxyParser) {
            final EbnfParserCombinatorProxyParser<ParserToken, C> proxy = Cast.to(parser);

            // the proxy will be lost but thats okay as we have the real parser now. Any forward refs will continue to hold the proxy.
            identifierAndParser.setValue(proxy.parser.setToString(this.identifierToRule.get(identifier).toString()));
        }
    }

    // RULE ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRuleParserToken rule) {
        this.enter();
        this.accept(rule.token());

        // update the proxy holding all references to this rule...
        final EbnfParserCombinatorProxyParser<?, C> proxy = Cast.to(this.context.identifierToParser.get(rule.identifier()));
        proxy.parser = Cast.to(this.children.get(0));

        return Visiting.SKIP; // skip because we dont want to visit LHS of rule.
    }

    /**
     * Kept so we can wrap any rule parsers to show the complete rule.
     */
    private final Map<EbnfIdentifierParserToken, EbnfRuleParserToken> identifierToRule = Maps.ordered();

    // ALT ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfAlternativeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfAlternativeParserToken token) {
        final Parser<ParserToken, C> parser = Parsers.alternatives(this.children)
                .setToString(token.toString());
        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.alternatives(token, parser, this.context),
                token);
    }

    // CONCAT ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfConcatenationParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfConcatenationParserToken token) {
        final SequenceParserBuilder<C> b = Parsers.sequenceParserBuilder();
        this.children.stream()
                .forEach(p -> {
                    b.required(p);
                });
        final Parser<SequenceParserToken, C> parser = b.build()
                .setToString(token.toString());
        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.concatenation(token, parser, this.context),
                token);
    }

    // EXCEPTION ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfExceptionParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfExceptionParserToken token) {
        final Parser<ParserToken, C> left = this.children.get(0);
        final Parser<ParserToken, C> right = this.children.get(1);
        final Parser<ParserToken, C> parser = left.andNot(right)
                .setToString(token.toString())
                .castC();
        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.exception(token, parser, this.context),
                token);
    }

    // GROUP ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfGroupParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGroupParserToken token) {
        final Parser<ParserToken, C> parser = this.children.get(0)
                .setToString(token.toString());
        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.group(token, parser, this.context),
                token);
    }

    // OPT ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfOptionalParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfOptionalParserToken token) {
        final Parser<ParserToken, C> parser = this.children.get(0)
                .optional(ParserTokenNodeName.with(0))
                .setToString(token.toString());

        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.optional(token, parser, this.context),
                token);
    }

    // RANGE ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRangeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRangeParserToken token) {
        // ignore any children oarsers and try and create a range from the token directly.
        final char begin = this.characterForIdentifierOrTerminal(token.begin());
        final char end = this.characterForIdentifierOrTerminal(token.end());

        final Parser<ParserToken, C> parser =Parsers.stringCharPredicate(CharPredicates.range(begin, end), 1, 1)
                .setToString(token.toString())
                .castC();
        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.range(token, parser, this.context),
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
        final EbnfRuleParserToken rule = this.identifierToRule.get(identifier);
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
        throw new EbnfTerminalParserTokenInvalidRangeBoundParserCombinatorException(message, token);
    }

    // REPEAT ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRepeatedParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRepeatedParserToken token) {
        final Parser<RepeatedParserToken, C> parser = Parsers.repeated(this.children.get(0))
                .setToString(token.toString());
        this.exit();
        this.add(
                this.context.syntaxTreeTransformer.repeated(token, parser, this.context),
                token);
    }

    // IDENTIFIER ........................................................................................................

    @Override
    protected void visit(final EbnfIdentifierParserToken identifier) {
        this.add(
                this.context.syntaxTreeTransformer.identifier(identifier,
                        this.context.identifierToParser.get(identifier),
                        this.context),
                identifier);
    }

    // TERMINAL ........................................................................................................

    @Override
    protected void visit(final EbnfTerminalParserToken token) {
        final Parser<StringParserToken, C> parser = Parsers.<C>string(token.value())
                .setToString(token.toString());
        this.add(
                this.context.syntaxTreeTransformer.terminal(token, parser, this.context),
                token);
    }

    // GENERAL PURPOSE .................................................................................................

    private void enter(){
        this.previousChildren = this.previousChildren.push(this.children);
        this.children = Lists.array();
    }

    private void exit() {
        this.children = this.previousChildren.peek();
        this.previousChildren = this.previousChildren.pop();
    }

    private Stack<List<Parser<ParserToken, C>>> previousChildren = Stacks.arrayList();

    private List<Parser<ParserToken, C>> children;

    private void add(final Parser<? extends ParserToken, C> parser, final EbnfParserToken token) {
        if(null == parser) {
            throw new NullPointerException("Null parser returned for " + token);
        }
        this.children.add(parser.castC());
    }

    private final EbnfParserCombinatorContext<C> context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}

