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

import walkingkooka.collect.list.Lists;
import walkingkooka.naming.Name;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
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
    public Parser<ParserToken, ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> concatenation(final EbnfConcatenationParserToken token, final Parser<SequenceParserToken, ParserContext> parser) {
        return parser.transform(this::concatenation);
    }

    private ParserToken concatenation(final SequenceParserToken sequence, final ParserContext context) {
        return sequence.flat();
    }

    @Override
    public Parser<ParserToken, ParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserToken, ParserContext> parser) {
        throw new UnsupportedOperationException(token.text()); // there are no exception tokens.
    }

    @Override
    public Parser<ParserToken, ParserContext> group(final EbnfGroupParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser; //leave group definitions as they are.
    }

    @Override
    public Parser<ParserToken, ParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserToken, ParserContext> parser) {
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

    private ParserToken predicate(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::predicate);
    }

    private static final EbnfIdentifierName PREDICATE_IDENTIFIER = EbnfIdentifierName.with("PREDICATE");

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

    private Parser<ParserToken, ParserContext> requiredCheck(final EbnfIdentifierName name, final Parser<ParserToken, ParserContext> parser) {
        return name.value().endsWith("REQUIRED") ?
                parser.orReport(ParserReporters.basic()) :
                parser; // leave as is...
    }

    @Override
    public Parser<ParserToken, ParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> range(final EbnfRangeParserToken token, final Parser<SequenceParserToken, ParserContext> parserd) {
        throw new UnsupportedOperationException(token.toString());
    }

    @Override
    public Parser<RepeatedParserToken, ParserContext> repeated(final EbnfRepeatedParserToken token, final Parser<RepeatedParserToken, ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserToken, ParserContext> terminal(final EbnfTerminalParserToken token, final Parser<StringParserToken, ParserContext> parser) {
        throw new UnsupportedOperationException(token.toString());
    }
}
