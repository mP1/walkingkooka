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

package walkingkooka.text.cursor.parser.select;

import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.text.cursor.parser.ebnf.combinator.EbnfParserCombinatorSyntaxTreeTransformer;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

final class NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer {

    @Override
    public Parser<ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> concatenation(final EbnfConcatenationParserToken token, final Parser<ParserContext> parser) {
        return parser.transform(this::concatenation);
    }

    private ParserToken concatenation(final ParserToken sequence, final ParserContext context) {
        return SequenceParserToken.class.cast(sequence).flat();
    }

    @Override
    public Parser<ParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserContext> parser) {
        throw new UnsupportedOperationException(token.text()); // there are no exception tokens.
    }

    @Override
    public Parser<ParserContext> group(final EbnfGroupParserToken token, final Parser<ParserContext> parser) {
        return parser; //leave group definitions as they are.
    }

    @Override
    public Parser<ParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserContext> parser) {
        final EbnfIdentifierName name = token.value();
        return name.equals(EQUALS_IDENTIFIER) ?
                parser.transform(this::equalsParserToken) :

                name.equals(NodeSelectorParsers.EXPRESSION_IDENTIFIER) ?
                        parser.transform(this::expression) :

                        name.equals(FUNCTION_IDENTIFIER) ?
                                parser.transform(this::function) :

                                name.equals(GREATER_THAN_IDENTIFIER) ?
                                        parser.transform(this::greaterThan) :

                                        name.equals(GREATER_THAN_EQUALS_IDENTIFIER) ?
                                                parser.transform(this::greaterThanEquals) :

                                                name.equals(LESS_THAN_IDENTIFIER) ?
                                                        parser.transform(this::lessThan) :

                                                        name.equals(LESS_THAN_EQUALS_IDENTIFIER) ?
                                                                parser.transform(this::lessThanEquals) :

                                                                name.equals(NOT_EQUALS_IDENTIFIER) ?
                                                                        parser.transform(this::notEquals) :

                                                                        name.equals(PREDICATE_IDENTIFIER) ?
                                                                                parser.transform(this::predicate) :

                                                                                name.equals(NodeSelectorParsers.ATTRIBUTE_NAME_IDENTIFIER) ?
                                                                                        parser.transform(this::attributeName) :

                                                                                        name.equals(NodeSelectorParsers.FUNCTION_NAME_IDENTIFIER) ?
                                                                                                parser.transform(this::functionName) :

                                                                                                name.equals(NodeSelectorParsers.NODE_NAME_IDENTIFIER) ?
                                                                                                        parser.transform(this::nodeName) :

                                                                                                        requiredCheck(name, parser);
    }

    private ParserToken equalsParserToken(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::equalsParserToken);
    }

    private static final EbnfIdentifierName EQUALS_IDENTIFIER = EbnfIdentifierName.with("CONDITION_EQUALS");

    private ParserToken expression(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::expression);
    }

    private ParserToken function(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::function);
    }

    private static final EbnfIdentifierName FUNCTION_IDENTIFIER = EbnfIdentifierName.with("FUNCTION");

    private ParserToken greaterThan(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::greaterThan);
    }

    private static final EbnfIdentifierName GREATER_THAN_IDENTIFIER = EbnfIdentifierName.with("CONDITION_GREATER_THAN");

    private ParserToken greaterThanEquals(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::greaterThanEquals);
    }

    private static final EbnfIdentifierName GREATER_THAN_EQUALS_IDENTIFIER = EbnfIdentifierName.with("CONDITION_GREATER_THAN_EQUALS");


    private ParserToken lessThan(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::lessThan);
    }

    private static final EbnfIdentifierName LESS_THAN_IDENTIFIER = EbnfIdentifierName.with("CONDITION_LESS_THAN");

    private ParserToken lessThanEquals(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::lessThanEquals);
    }

    private static final EbnfIdentifierName LESS_THAN_EQUALS_IDENTIFIER = EbnfIdentifierName.with("CONDITION_LESS_THAN_EQUALS");

    private ParserToken notEquals(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::notEquals);
    }

    private static final EbnfIdentifierName NOT_EQUALS_IDENTIFIER = EbnfIdentifierName.with("CONDITION_NOT_EQUALS");

    // predicate .....................................................................................................

    private ParserToken predicate(final ParserToken token, final ParserContext context) {
        return token instanceof SequenceParserToken ?
                this.predicate0(SequenceParserToken.class.cast(token), context) :
                this.parent(token, NodeSelectorParserToken::predicate);
    }

    /**
     * Handles grouping of AND and OR sub expressions before creating the enclosing {@link NodeSelectorPredicateParserToken}.
     */
    private ParserToken predicate0(final SequenceParserToken sequenceParserToken, final ParserContext context) {
        final List<ParserToken> all = Lists.array();
        final List<ParserToken> tokens = Lists.array();

        int mode = PREDICATE;

        for (ParserToken token : sequenceParserToken.value()) {
            final NodeSelectorParserToken selectorParserToken = token.cast();

            final boolean andSymbol = selectorParserToken.isAndSymbol();
            final boolean orSymbol = selectorParserToken.isOrSymbol();

            switch (mode) {
                case PREDICATE:
                    mode = andSymbol ? AND : orSymbol ? OR : PREDICATE;
                    break;
                case AND:
                    if (andSymbol || orSymbol) {
                        final NodeSelectorAndParserToken and = and(tokens);
                        tokens.clear();
                        tokens.add(and);
                    }
                    if (orSymbol) {
                        mode = OR;
                    }
                    break;
                case OR:
                    if (andSymbol || orSymbol) {
                        final NodeSelectorOrParserToken or = or(tokens);
                        tokens.clear();
                        tokens.add(or);
                    }
                    if (andSymbol) {
                        mode = AND;
                    }
                    break;
                default:
                    NeverError.unhandledCase(mode, PREDICATE, AND, OR);
                    break;
            }
            tokens.add(token);
        }

        switch (mode) {
            case PREDICATE:
                all.addAll(tokens);
                break;
            case AND:
                all.add(and(tokens));
                break;
            case OR:
                all.add(or(tokens));
                break;
            default:
                NeverError.unhandledCase(mode, PREDICATE, AND, OR);
                break;
        }

        return NodeSelectorParserToken.predicate(all, sequenceParserToken.text());
    }

    /**
     * All tokens will be immediate child tokens of the enclosing predicate.
     * The bracket-open and bracket-close symbols will always belong to the predicate and never appear in the AND or OR.
     */
    private final static int PREDICATE = 0;
    private final static int AND = PREDICATE + 1;
    private final static int OR = AND + 1;

    private static NodeSelectorAndParserToken and(final List<ParserToken> tokens) {
        return NodeSelectorParserToken.and(tokens, ParserToken.text(tokens));
    }

    private static NodeSelectorOrParserToken or(final List<ParserToken> tokens) {
        return NodeSelectorParserToken.or(tokens, ParserToken.text(tokens));
    }

    private static final EbnfIdentifierName PREDICATE_IDENTIFIER = EbnfIdentifierName.with("PREDICATE");

    // parent .....................................................................................................

    private ParserToken parent(final ParserToken token, final BiFunction<List<ParserToken>, String, ? extends NodeSelectorParentParserToken> factory) {
        return factory.apply(token instanceof SequenceParserToken ?
                        SequenceParserToken.class.cast(token).value() :
                        Lists.of(token),
                token.text());
    }

    // name factory methods .........................................................

    private ParserToken attributeName(final ParserToken token, final ParserContext context) {
        return name(token, NodeSelectorAttributeName::with, NodeSelectorParserToken::attributeName);
    }

    private ParserToken functionName(final ParserToken token, final ParserContext context) {
        return name(token, NodeSelectorFunctionName::with, NodeSelectorParserToken::functionName);
    }

    private ParserToken nodeName(final ParserToken token, final ParserContext context) {
        return name(token, NodeSelectorNodeName::with, NodeSelectorParserToken::nodeName);
    }

    private <N extends Name> ParserToken name(final ParserToken token,
                                              final Function<String, N> nameFactory,
                                              final BiFunction<N, String, ? extends NodeSelectorNonSymbolParserToken> parserTokenFactory) {
        final StringParserToken stringParserToken = StringParserToken.class.cast(token);
        return parserTokenFactory.apply(nameFactory.apply(stringParserToken.value()), stringParserToken.text());
    }

    private Parser<ParserContext> requiredCheck(final EbnfIdentifierName name, final Parser<ParserContext> parser) {
        return name.value().endsWith("REQUIRED") ?
                parser.orReport(ParserReporters.basic()) :
                parser; // leave as is...
    }

    @Override
    public Parser<ParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> range(final EbnfRangeParserToken token, final Parser<ParserContext> parserd) {
        throw new UnsupportedOperationException(token.toString());
    }

    @Override
    public Parser<ParserContext> repeated(final EbnfRepeatedParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> terminal(final EbnfTerminalParserToken token, final Parser<ParserContext> parser) {
        throw new UnsupportedOperationException(token.toString());
    }
}
