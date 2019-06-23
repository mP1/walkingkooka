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

import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A parser that accepts a grammar and returns a {@link EbnfGrammarParserToken}.
 * <br>
 * <a href="https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form">EBNF</a>
 */
final class EbnfGrammarParser implements Parser<EbnfParserContext> {

    /**
     * This needs to be initialized before references below to avoid forward reference problems.
     */
    private static final Parser<EbnfParserContext> WHITESPACE_OR_COMMENT = whitespaceOrComment();

    private static Parser<EbnfParserContext> whitespaceOrComment() {
        final Parser<EbnfParserContext> whitespace = Parsers.<EbnfParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
                .transform(EbnfGrammarParser::transformWhitespace)
                .setToString("whitespace");

        final Parser<EbnfParserContext> comment = Parsers.<EbnfParserContext>surround("(*", "*)")
                .transform(EbnfGrammarParser::transformComment)
                .setToString("comment");

        return whitespace.or(comment)
                .repeating();
    }

    private static ParserToken transformWhitespace(final ParserToken token, ParserContext context) {
        return EbnfWhitespaceParserToken.with(StringParserToken.class.cast(token).value(), token.text());
    }

    private static ParserToken transformComment(final ParserToken token, ParserContext context) {
        return EbnfCommentParserToken.with(StringParserToken.class.cast(token).value(), token.text());
    }

    /**
     * <pre>
     * identifier = letter , { letter | digit | "_" } ;
     * </pre>
     */
    final static Parser<EbnfParserContext> IDENTIFIER =
            identifier()
                    .setToString("identifier");

    private static Parser<EbnfParserContext> identifier() {
        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(Parsers.character(EbnfIdentifierName.INITIAL))
                .required(Parsers.character(EbnfIdentifierName.PART).repeating().orReport(ParserReporters.basic()).cast())
                .build()
                .transform(EbnfGrammarParserIdentifierParserTokenVisitor::ebnfIdentifierParserToken)
                .setToString("IDENTIFIER");
    }

    /**
     * <pre>
     * terminal = "'" , character , { character } , "'"
     *          | '"' , character , { character } , '"' ;
     * </pre>
     * The above definition isnt actually correct, a terminal must be either single or quoted, and supports backslash, and unicode sequences within.
     */
    final static Parser<EbnfParserContext> TERMINAL = EbnfTerminalParser.INSTANCE;

    /**
     * <pre>
     * lhs = identifier ;
     * </pre>
     */
    final static Parser<EbnfParserContext> LHS = IDENTIFIER;

    /**
     * <pre>
     * rhs = identifier
     *      | terminal
     *      | "[" , rhs , "]"/
     *      | "{" , rhs , "}"
     *      | "(" , rhs , ")"
     *      | rhs , "|" , rhs
     *      | rhs , "," , rhs ;
     * </pre>
     */
    static final Parser<EbnfParserContext> RHS = new Parser<>() {

        @Override
        public Optional<ParserToken> parse(TextCursor cursor, EbnfParserContext context) {
            return rhs().parse(cursor, context);
        }

        public String toString() {
            return "rhs";
        }
    };

    /**
     * <pre>
     * "[" , rhs , "]"
     * </pre>
     */
    final static Parser<EbnfParserContext> OPTIONAL = optional();

    private static Parser<EbnfParserContext> optional() {
        final Parser<EbnfParserContext> open = symbol('[', "optional_open");
        final Parser<EbnfParserContext> close = symbol(']', "optional_close");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(open)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(close)
                .build()
                .transform(filterAndWrap(EbnfParserToken::optional));
    }

    /**
     * <pre>
     * "{" , rhs , "}"
     * </pre>
     */
    final static Parser<EbnfParserContext> REPETITION = repetition();

    private static Parser<EbnfParserContext> repetition() {
        final Parser<EbnfParserContext> open = symbol('{', "repetition_open");
        final Parser<EbnfParserContext> close = symbol('}', "repetition_close");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(open)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(close)
                .build()
                .transform(filterAndWrap(EbnfParserToken::repeated));
    }

    /**
     * <pre>
     * "(" , rhs , ")"
     * </pre>
     */
    final static Parser<EbnfParserContext> GROUPING = group();

    private static Parser<EbnfParserContext> group() {
        final Parser<EbnfParserContext> open = symbol('(', "group_open");
        final Parser<EbnfParserContext> close = symbol(')', "group_close");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(open)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(close)
                .build()
                .transform(filterAndWrap(EbnfParserToken::group));
    }

    /**
     * <pre>
     * "(" , rhs , ")"
     * </pre>
     */
    final static Parser<EbnfParserContext> RHS2 = IDENTIFIER
            .or(OPTIONAL)
            .or(REPETITION)
            .or(GROUPING)
            .or(TERMINAL)
            .orReport(ParserReporters.basic());

    /**
     * <pre>
     * rhs , "|" , rhs
     * </pre>
     * To avoid left recursion problems the first rhs is replaced as RHS2
     */
    final static Parser<EbnfParserContext> ALTERNATIVE = alternative();

    private static Parser<EbnfParserContext> alternative() {
        final Parser<EbnfParserContext> separator = symbol('|', "alt_separator");

        final Parser<EbnfParserContext> required = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build();
        final Parser<EbnfParserContext> optionalRepeating = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .repeating();
        final Parser<EbnfParserContext> all = Cast.to(Parsers.sequenceParserBuilder()
                .required(required.cast())
                .optional(optionalRepeating.cast())
                .build());
        return all
                .transform(filterAndWrap(EbnfParserToken::alternative));
    }

    /**
     * <pre>
     * | rhs , "," , rhs ;
     * </pre>
     * To avoid left recursion problems the first rhs is replaced as RHS2
     */
    final static Parser<EbnfParserContext> CONCATENATION = concatenation();

    private static Parser<EbnfParserContext> concatenation() {
        final Parser<EbnfParserContext> separator = symbol(',', "concat_separator");

        final Parser<EbnfParserContext> required = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build();
        final Parser<EbnfParserContext> optionalRepeating = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .repeating();
        final Parser<EbnfParserContext> all = Cast.to(Parsers.sequenceParserBuilder()
                .required(required.cast())
                .optional(optionalRepeating.cast())
                .build());
        return all
                .transform(filterAndWrap(EbnfParserToken::concatenation));
    }

    /**
     * <pre>
     * "-" , rhs
     * </pre>
     */
    final static Parser<EbnfParserContext> EXCEPTION = exception();

    private static Parser<EbnfParserContext> exception() {
        final Parser<EbnfParserContext> separator = symbol('-', "exception_separator");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .transform(filterAndWrap(EbnfParserToken::exception));
    }

    /**
     * <pre>
     * range = terminal, '..', terminal
     * </pre>
     */
    final static Parser<EbnfParserContext> RANGE = range();

    private static Parser<EbnfParserContext> range() {
        final Parser<EbnfParserContext> separator = symbol("..", "range_separator");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .transform(filterAndWrap(EbnfParserToken::range));
    }

    /**
     * <pre>
     * lhs , "=" , rhs , ";" ;
     * </pre>
     */
    final static Parser<EbnfParserContext> RULE = rule();

    private static Parser<EbnfParserContext> rule() {
        final Parser<EbnfParserContext> assign = symbol('=', "assign")
                .orReport(ParserReporters.basic());
        final Parser<EbnfParserContext> termination = symbol(';', "termination")
                .orReport(ParserReporters.basic());

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(LHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(assign)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(termination)
                .build()
                .transform(filterAndWrap(EbnfParserToken::rule));
    }

    /**
     * Matches any of the tokens, assumes that any leading or trailing whitespace or comments is handled elsewhere...(parent)
     */
    private static Parser<EbnfParserContext> rhs() {
        if (null == RHS_CACHE) {
            RHS_CACHE = ALTERNATIVE
                    .or(CONCATENATION)
                    .or(OPTIONAL)
                    .or(REPETITION)
                    .or(GROUPING)
                    .or(RANGE) // must be before TERMINAL
                    .or(EXCEPTION)
                    .or(IDENTIFIER) // identifier & terminal are atoms of range, exception, alt and concat and must come after
                    .or(TERMINAL)
                    .orReport(ParserReporters.basic());
        }
        return RHS_CACHE;
    }

    private static Parser<EbnfParserContext> RHS_CACHE;

    /**
     * Creates a parser that matches the given character and wraps it inside a {@link EbnfSymbolParserToken}
     */
    private static Parser<EbnfParserContext> symbol(final char c, final String name) {
        return Parsers.character(CharPredicates.is(c))
                .transform(EbnfGrammarParser::transformSymbolCharacter)
                .setToString(name)
                .cast();
    }

    private static ParserToken transformSymbolCharacter(final ParserToken token, final ParserContext context) {
        return EbnfSymbolParserToken.with(CharacterParserToken.class.cast(token).value().toString(), token.text());
    }

    /**
     * Creates a parser that matches the given character and wraps it inside a {@link EbnfSymbolParserToken}
     */
    private static Parser<EbnfParserContext> symbol(final String symbol, final String name) {
        return CaseSensitivity.SENSITIVE.parser(symbol)
                .transform(EbnfGrammarParser::transformSymbolString)
                .setToString(name)
                .cast();
    }

    private static ParserToken transformSymbolString(final ParserToken token, final ParserContext context) {
        return EbnfSymbolParserToken.with(StringParserToken.class.cast(token).value(), token.text());
    }

    private static BiFunction<ParserToken, EbnfParserContext, ParserToken> filterAndWrap(final BiFunction<List<ParserToken>, String, ParserToken> wrapper) {
        return (token, context) -> EbnfGrammarParserWrapperParserTokenVisitor.wrap(token, wrapper);
    }

    /**
     * <pre>
     * grammar = { rule } ;
     * </pre>
     */
    final static Parser<EbnfParserContext> GRAMMAR = grammar();

    private static Parser<EbnfParserContext> grammar() {
        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RULE.orReport(ParserReporters.basic()))
                .optional(RULE.repeating().cast())
                .optional(WHITESPACE_OR_COMMENT)
                .build()
                .transform(EbnfGrammarParser::grammarParserToken);
    }

    private static EbnfGrammarParserToken grammarParserToken(final ParserToken sequence, final EbnfParserContext context) {
        return EbnfGrammarParserToken.with(SequenceParserToken.class.cast(sequence).flat()
                        .value(),
                sequence.text());
    }

    @Override
    public Optional<ParserToken> parse(final TextCursor cursor, final EbnfParserContext context) {
        return GRAMMAR.parse(cursor, context);
    }

    @Override
    public String toString() {
        return GRAMMAR.toString();
    }
}
