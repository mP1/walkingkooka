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

package walkingkooka.tree.select.parser;

import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.SequenceParserToken;
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

final class NodeSelectorEbnfParserCombinatorSyntaxTreeTransformer implements EbnfParserCombinatorSyntaxTreeTransformer {

    @Override
    public Parser<ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserContext> parser) {
        return parser;
    }

    @Override
    public Parser<ParserContext> concatenation(final EbnfConcatenationParserToken token, Parser<ParserContext> parser) {
        return parser.transform(this::concatenation);
    }

    /**
     * Special case for binary operators and operator priorities.
     */
    private ParserToken concatenation(final ParserToken sequence, final ParserContext context) {
        return this.concatenation0(sequence.cast(), context);
    }

    private ParserToken concatenation0(final SequenceParserToken sequence, final ParserContext context) {
        ParserToken result;

        for (; ; ) {
            final SequenceParserToken cleaned = sequence.flat();

            final NodeSelectorParserToken first = cleaned.removeWhitespace()
                    .value()
                    .get(0)
                    .cast();

            if (first.isSymbol()) {
                result = sequence;
                break;
            }

            result = this.binaryOperandPrioritize(cleaned.value(), sequence);
            break;
        }

        return result;
    }

    private ParserToken binaryOperandPrioritize(final List<ParserToken> tokens, final SequenceParserToken parent) {
        List<ParserToken> prioritized = this.maybeExpandNegatives(tokens);

        for (int priority = NodeSelectorParserToken.HIGHEST_PRIORITY; priority > NodeSelectorParserToken.LOWEST_PRIORITY; priority--) {
            boolean changed;

            do {
                changed = false;
                int i = 0;
                for (ParserToken t : prioritized) {
                    final NodeSelectorParserToken s = t.cast();
                    if (s.operatorPriority() == priority) {
                        changed = true;

                        final int firstIndex = this.findNonWhitespaceSiblingToken(prioritized, i - 1, -1);
                        final int lastIndex = this.findNonWhitespaceSiblingToken(prioritized, i + 1, +1);

                        final List<ParserToken> binaryOperandTokens = Lists.array();
                        binaryOperandTokens.addAll(prioritized.subList(firstIndex, lastIndex + 1));

                        final List<ParserToken> replaced = Lists.array();
                        replaced.addAll(prioritized.subList(0, firstIndex));
                        replaced.add(s.binaryOperand(binaryOperandTokens, ParserToken.text(binaryOperandTokens)));
                        replaced.addAll(prioritized.subList(lastIndex + 1, prioritized.size()));

                        prioritized = replaced;
                        break;
                    }
                    i++;
                }
            } while (changed && prioritized.size() > 1);
        }

        return prioritized.size() == 1 ?
                prioritized.get(0) :
                ParserTokens.sequence(prioritized, parent.text());
    }

    /**
     * Expands any {@link NodeSelectorNegativeParserToken} into its core components, only if it doesnt follow another symbol.
     * This fixes the parsing "mistake" that converts any minus followed by a token into a {@link NodeSelectorNegativeParserToken}.
     */
    private List<ParserToken> maybeExpandNegatives(final List<ParserToken> tokens) {
        final List<ParserToken> expanded = Lists.array();
        boolean expand = false;

        for (ParserToken t : tokens) {
            final NodeSelectorParserToken s = t.cast();
            if (s.isWhitespace()) {
                expanded.add(t);
                continue;
            }

            if (s.isNegative() && expand) {
                final NodeSelectorNegativeParserToken negativeParserToken = s.cast();
                expanded.addAll(negativeParserToken.value());
                expand = true;
                continue;
            }
            expand = !s.isSymbol();
            expanded.add(s);
        }

        return expanded;
    }

    private int findNonWhitespaceSiblingToken(final List<ParserToken> tokens, final int startIndex, final int step) {
        int i = startIndex;
        for (; ; ) {
            final NodeSelectorParserToken token = tokens.get(i).cast();
            if (!token.isWhitespace()) {
                break;
            }
            i = i + step;
        }
        return i;
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
        return name.equals(ATTRIBUTE_IDENTIFIER) ?
                parser.transform(this::attribute) :

                name.equals(NodeSelectorParsers.EXPRESSION_IDENTIFIER) ?
                        parser.transform(this::expression) :

                        name.equals(FUNCTION_IDENTIFIER) ?
                                parser.transform(this::function) :

                                name.equals(GROUP_IDENTIFIER) ?
                                        parser.transform(this::group) :

                                        name.equals(NEGATIVE_IDENTIFIER) ?
                                                parser.transform(this::negative) :

                                                name.equals(PREDICATE_IDENTIFIER) ?
                                                        parser.transform(this::predicate) :

                                                        requiredCheck(name, parser);
    }

    private ParserToken attribute(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::attribute);
    }

    static final EbnfIdentifierName ATTRIBUTE_IDENTIFIER = EbnfIdentifierName.with("ATTRIBUTE");

    private ParserToken expression(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::expression);
    }

    private ParserToken function(final ParserToken token, final ParserContext context) {
        return parent(token, NodeSelectorParserToken::function);
    }

    private static final EbnfIdentifierName FUNCTION_IDENTIFIER = EbnfIdentifierName.with("FUNCTION");

    private ParserToken group(final ParserToken token, final ParserContext context) {
        return NodeSelectorParserToken.group(this.clean(token.cast()), token.text());
    }

    private static final EbnfIdentifierName GROUP_IDENTIFIER = EbnfIdentifierName.with("GROUP");

    private List<ParserToken> clean(final SequenceParserToken token) {
        return token.flat()
                .value();
    }

    private ParserToken negative(final ParserToken token, final ParserContext context) {
        return NodeSelectorParserToken.negative(this.clean(token.cast()), token.text());
    }

    private static final EbnfIdentifierName NEGATIVE_IDENTIFIER = EbnfIdentifierName.with("NEGATIVE");

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
