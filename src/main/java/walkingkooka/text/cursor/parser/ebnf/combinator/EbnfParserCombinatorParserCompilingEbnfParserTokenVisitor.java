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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserBuilder;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
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
import java.util.Map.Entry;
import java.util.Set;

/**
 * Compiles all tokens, passing each token to the {@link EbnfParserCombinatorSyntaxTreeTransformer} allowing substitution.
 */
final class EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor extends EbnfParserTokenVisitor {

    static void compile(final Map<EbnfIdentifierName, Parser<ParserContext>> identifierToParser,
                        final EbnfParserCombinatorSyntaxTreeTransformer transformer,
                        final EbnfGrammarParserToken grammar) {
        new EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor(identifierToParser, transformer)
                .accept(grammar);
    }

    EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor(final Map<EbnfIdentifierName, Parser<ParserContext>> identifierToParser,
                                                              final EbnfParserCombinatorSyntaxTreeTransformer transformer) {
        super();
        this.identifierToParser = identifierToParser;
        this.transformer = transformer;
    }

    // GRAMMAR ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfGrammarParserToken token) {
        // need this mapping to fetch tokens for a rule by identifier at any stage or walking...
        token.value()
                .stream()
                .filter(t -> t instanceof EbnfParserToken)
                .map(t -> EbnfParserToken.class.cast(t))
                .filter(t -> t.isRule())
                .map(t -> EbnfRuleParserToken.class.cast(t))
                .forEach(r -> {
                    this.identifierToRule.put(r.identifier().value(), r);
                });
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfGrammarParserToken token) {
        // replace all EbnfParserCombinatorProxyParser with the parser they are wrapping. This also has the advantage that
        // all parsers display their complete source.

        this.identifierToParser.entrySet().forEach(identifierAndParser -> {
            this.fixRuleParserToString(identifierAndParser);
        });
        super.endVisit(token);
    }

    private void fixRuleParserToString(final Entry<EbnfIdentifierName, Parser<ParserContext>> identifierAndParser) {
        final EbnfIdentifierName identifier = identifierAndParser.getKey();
        final Parser<ParserContext> parser = identifierAndParser.getValue();

        // Not all parsers may be proxy, because some parsers may be present in the initial map.
        if (parser instanceof EbnfParserCombinatorProxyParser) {
            final EbnfParserCombinatorProxyParser<ParserContext> proxy = Cast.to(parser);

            // the proxy will be lost but thats okay as we have the real parser now. Any forward refs will continue to hold the proxy.
            EbnfRuleParserToken rule = this.identifierToRule.get(identifier);

            EbnfParserToken token = rule.token();
            while (token.isIdentifier()) {
                final EbnfIdentifierParserToken tokenIdentifier = token.cast();
                token = this.identifierToRule.get(tokenIdentifier.value()).token();
            }

            identifierAndParser.setValue(proxy.parser.setToString(token.toString()));
        }
    }

    // RULE ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRuleParserToken rule) {
        this.enter();
        this.accept(rule.token());

        final EbnfIdentifierParserToken name = rule.identifier();

        // update the proxy holding all references to this rule...
        final EbnfParserCombinatorProxyParser<ParserContext> proxy = Cast.to(this.identifierToParser.get(name.value()));
        final Parser<ParserContext> parser = this.children.get(0);
        proxy.parser = this.transformer.identifier(name, parser).cast();
        this.replaceParser(proxy, parser);

        return Visiting.SKIP; // skip because we dont want to visit LHS of rule.
    }

    @Override
    protected void endVisit(EbnfRuleParserToken token) {
        this.exit();
    }

    /**
     * Kept so we can wrap any rule parsers to show the complete rule.
     */
    private final Map<EbnfIdentifierName, EbnfRuleParserToken> identifierToRule = Maps.ordered();

    // ALT ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfAlternativeParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfAlternativeParserToken token) {
        final Parser<ParserContext> parser = Parsers.alternatives(this.children)
                .setToString(token.toString());
        this.exit();
        this.add(
                this.transformer.alternatives(token, parser),
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
        final SequenceParserBuilder<ParserContext> b = Parsers.sequenceParserBuilder();
        this.children.forEach(p -> {
            this.concatenationParserToken(p, b);
        });
        final Parser<ParserContext> parser = b.build()
                .setToString(token.toString());
        this.exit();
        this.add(
                this.transformer.concatenation(token, parser),
                token);
    }

    private void concatenationParserToken(final Parser<ParserContext> parser,
                                          final SequenceParserBuilder<ParserContext> b) {
        if (this.isOptional(parser)) {
            b.optional(parser);
        } else {
            b.required(parser);
        }
    }

    // EXCEPTION ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfExceptionParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfExceptionParserToken token) {
        final Parser<ParserContext> left = this.children.get(0);
        final Parser<ParserContext> right = this.children.get(1);
        final Parser<ParserContext> parser = left.andNot(right)
                .setToString(token.toString())
                .cast();
        this.exit();
        this.add(
                this.transformer.exception(token, parser),
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
        final Parser<ParserContext> parser = this.children.get(0)
                .setToString(token.toString());
        this.exit();
        this.add(
                this.transformer.group(token, parser),
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
        final Parser<ParserContext> parser = this.children.get(0)
                .setToString(token.toString());
        this.exit();
        this.add(
                this.addOptional(this.transformer.optional(token, parser)),
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
        final SequenceParserBuilder<ParserContext> b = Parsers.sequenceParserBuilder();
        this.children.forEach(p -> {
            b.required(p);
        });
        final Parser<ParserContext> parser = b.build()
                .setToString(token.toString());
        this.exit();
        this.add(
                this.transformer.range(token, parser),
                token);
    }

    // REPEAT ........................................................................................................

    @Override
    protected Visiting startVisit(final EbnfRepeatedParserToken token) {
        this.enter();
        return super.startVisit(token);
    }

    @Override
    protected void endVisit(final EbnfRepeatedParserToken token) {
        final Parser<ParserContext> parser = Parsers.repeated(this.children.get(0))
                .setToString(token.toString());
        this.exit();
        this.add(
                this.transformer.repeated(token, parser),
                token);
    }

    // IDENTIFIER ........................................................................................................

    @Override
    protected void visit(final EbnfIdentifierParserToken identifier) {
        this.add(this.identifierToParser.get(identifier.value()), identifier);
    }

    // TERMINAL ........................................................................................................

    @Override
    protected void visit(final EbnfTerminalParserToken token) {
        final Parser<ParserContext> parser = CaseSensitivity.SENSITIVE.parser(token.value())
                .setToString(token.toString());
        this.add(
                this.transformer.terminal(token, parser),
                token);
    }

    // GENERAL PURPOSE .................................................................................................

    private void enter() {
        this.previousChildren = this.previousChildren.push(this.children);
        this.children = Lists.array();
    }

    private void exit() {
        this.children = this.previousChildren.peek();
        this.previousChildren = this.previousChildren.pop();
    }

    private Stack<List<Parser<ParserContext>>> previousChildren = Stacks.arrayList();

    private List<Parser<ParserContext>> children;

    private void add(final Parser<ParserContext> parser, final EbnfParserToken token) {
        if (null == parser) {
            throw new NullPointerException("Null parser returned for " + token);
        }
        this.children.add(parser.cast());
    }

    private final Map<EbnfIdentifierName, Parser<ParserContext>> identifierToParser;
    private final EbnfParserCombinatorSyntaxTreeTransformer transformer;

    /**
     * Marks a new parser as optional if the previous was also optional.
     */
    private Parser<ParserContext> replaceParser(final Parser<ParserContext> parser,
                                                final Parser<ParserContext> previous) {
        return this.isOptional(previous) ?
                this.addOptional(parser) :
                parser;
    }

    /**
     * Marks a parser as optional. This is used when building a concat parser using a {@link SequenceParserBuilder}.
     */
    private Parser<ParserContext> addOptional(final Parser<ParserContext> parser) {
        this.optionalParsers.add(parser);
        return parser;
    }

    /**
     * Tests if this parser is optional.
     */
    private boolean isOptional(final Parser<ParserContext> parser) {
        return this.optionalParsers.contains(parser);
    }

    /**
     * Tracks which parsers are optional, so that the appropriate {@link SequenceParserBuilder} method can be called.
     */
    private final Set<Parser<?>> optionalParsers = Sets.ordered();

    @Override
    public String toString() {
        return this.identifierToParser.toString();
    }
}

