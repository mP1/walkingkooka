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

import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.CharacterParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.RepeatedParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;
import walkingkooka.text.cursor.parser.StringParserToken;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * A parser that accepts a grammar and returns a {@link EbnfGrammarParserToken}.
 * <br>
 * <a href="https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form">EBNF</a>
 */
final class EbnfGrammarParser implements Parser<EbnfGrammarParserToken, EbnfParserContext> {

    /**
     * This needs to be initialized before references below to avoid forward reference problems.
     */
    private static final Parser<ParserToken, EbnfParserContext> WHITESPACE_OR_COMMENT = whitespaceOrComment();

    private static Parser<ParserToken, EbnfParserContext> whitespaceOrComment() {
        final Parser<EbnfWhitespaceParserToken, EbnfParserContext> whitespace = Parsers.<EbnfParserContext>stringCharPredicate(CharPredicates.whitespace(), 1, Integer.MAX_VALUE)
                .transform((string, context) -> EbnfWhitespaceParserToken.with(string.value(), string.text()))
                .setToString("whitespace");
        final Parser<ParserToken, EbnfParserContext> comment = Parsers.<EbnfParserContext>surround("(*", "*)")
                .transform((string, context) -> EbnfCommentParserToken.with(string.value(), string.text()))
                .setToString("comment")
                .cast();

        return whitespace.or(comment.cast())
                .repeating()
                .cast();
    }

    /**
     * <pre>
     * identifier = letter , { letter | digit | "_" } ;
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> IDENTIFIER =
            identifier()
            .setToString("identifier");

    private static Parser<ParserToken, EbnfParserContext> identifier() {
        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(Parsers.character(EbnfIdentifierName.INITIAL).cast())
                .required(Parsers.character(EbnfIdentifierName.PART).repeating().orReport(ParserReporters.basic()).cast())
                .build()
                .transform(EbnfGrammarParser::ebnfIdentifierParserToken)
                .setToString("IDENTIFIER")
                .cast();
    }

    private static EbnfIdentifierParserToken ebnfIdentifierParserToken(final SequenceParserToken tokens, final EbnfParserContext context) {
        final StringBuilder b = new StringBuilder();
        for(ParserToken c : tokens.flat().value()) {
            final CharacterParserToken character = c.cast();
            b.append(character.value());
        }

        final String text = b.toString();
        return EbnfParserToken.identifier(EbnfIdentifierName.with(text), text);
    }

    /**
     * <pre>
     * terminal = "'" , character , { character } , "'"
     *          | '"' , character , { character } , '"' ;
     * </pre>
     * The above definition isnt actually correct, a terminal must be either single or quoted, and supports backslash, and unicode sequences within.
     */
    final static Parser<ParserToken, EbnfParserContext> TERMINAL = EbnfTerminalParser.INSTANCE;

    /**
     * <pre>
     * lhs = identifier ;
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> LHS = IDENTIFIER;

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
    static final Parser<ParserToken, EbnfParserContext> RHS = new Parser<ParserToken, EbnfParserContext>() {

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
    final static Parser<ParserToken, EbnfParserContext> OPTIONAL = optional();

    private static Parser<ParserToken, EbnfParserContext> optional() {
        final Parser<ParserToken, EbnfParserContext> open = symbol('[', "optional_open");
        final Parser<ParserToken, EbnfParserContext> close = symbol(']', "optional_close");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(open)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(close)
                .build()
                .transform(filterAndWrapMany(EbnfParserToken::optional));
    }

    /**
     * <pre>
     * "{" , rhs , "}"
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> REPETITION = repetition();

    private static Parser<ParserToken, EbnfParserContext> repetition() {
        final Parser<ParserToken, EbnfParserContext> open = symbol('{', "repetition_open");
        final Parser<ParserToken, EbnfParserContext> close = symbol('}', "repetition_close");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(open)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(close)
                .build()
                .transform(filterAndWrapMany(EbnfParserToken::repeated));
    }

    /**
     * <pre>
     * "(" , rhs , ")"
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> GROUPING = group();

    private static Parser<ParserToken, EbnfParserContext> group() {
        final Parser<ParserToken, EbnfParserContext> open = symbol('(', "group_open");
        final Parser<ParserToken, EbnfParserContext> close = symbol(')', "group_close");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .required(open)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS)
                .optional(WHITESPACE_OR_COMMENT)
                .required(close)
                .build()
                .transform(filterAndWrapMany(EbnfParserToken::group));
    }

    /**
     * <pre>
     * "(" , rhs , ")"
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RHS2 = IDENTIFIER
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
    final static Parser<ParserToken, EbnfParserContext> ALTERNATIVE = alternative();

    private static Parser<ParserToken, EbnfParserContext> alternative() {
        final Parser<ParserToken, EbnfParserContext> separator = symbol('|', "alt_separator");

        final Parser<SequenceParserToken, EbnfParserContext> required = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build();
        final Parser<RepeatedParserToken, EbnfParserContext> optionalRepeating = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .repeating();
        final Parser<SequenceParserToken, EbnfParserContext> all = Cast.to(Parsers.sequenceParserBuilder()
                .required(required.cast())
                .optional(optionalRepeating.cast())
                .build());
        return all
                .transform(filterAndWrapMany(EbnfParserToken::alternative));
    }

    /**
     * <pre>
     * | rhs , "," , rhs ;
     * </pre>
     * To avoid left recursion problems the first rhs is replaced as RHS2
     */
    final static Parser<ParserToken, EbnfParserContext> CONCATENATION = concatenation();

    private static Parser<ParserToken, EbnfParserContext> concatenation() {
        final Parser<ParserToken, EbnfParserContext> separator = symbol(',', "concat_separator");

        final Parser<SequenceParserToken, EbnfParserContext> required = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build();
        final Parser<RepeatedParserToken, EbnfParserContext> optionalRepeating = Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .repeating();
        final Parser<SequenceParserToken, EbnfParserContext> all = Cast.to(Parsers.sequenceParserBuilder()
                .required(required.cast())
                .optional(optionalRepeating.cast())
                .build());
        return all
                .transform(filterAndWrapMany(EbnfParserToken::concatenation));
    }

    /**
     * <pre>
     * "-" , rhs
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> EXCEPTION = exception();

    private static Parser<ParserToken, EbnfParserContext> exception() {
        final Parser<ParserToken, EbnfParserContext> separator = symbol('-', "exception_separator");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .transform(filterAndWrapMany(EbnfParserToken::exception));
    }

    /**
     * <pre>
     * range = terminal, '..', terminal
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RANGE = range();

    private static Parser<ParserToken, EbnfParserContext> range() {
        final Parser<ParserToken, EbnfParserContext> separator = symbol("..", "range_separator");

        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .optional(WHITESPACE_OR_COMMENT)
                .required(separator)
                .optional(WHITESPACE_OR_COMMENT)
                .required(RHS2)
                .build()
                .transform(filterAndWrapMany(EbnfParserToken::range));
    }

    /**
     * <pre>
     * lhs , "=" , rhs , ";" ;
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RULE = rule();

    private static Parser<ParserToken, EbnfParserContext> rule() {
        final Parser<ParserToken, EbnfParserContext> assign = symbol("=", "assign")
                .orReport(ParserReporters.basic());
        final Parser<ParserToken, EbnfParserContext> termination = symbol(";", "termination")
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
                .transform(filterAndWrapMany(EbnfParserToken::rule));
    }

    /**
     * Matches any of the tokens, assumes that any leading or trailing whitespace or comments is handled elsewhere...(parent)
     */
    private static Parser<ParserToken, EbnfParserContext> rhs() {
        if(null==RHS_CACHE) {
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

    private static Parser<ParserToken, EbnfParserContext> RHS_CACHE;

    /**
     * Creates a parser that matches the given character and wraps it inside a {@link EbnfSymbolParserToken}
     */
    private static Parser<ParserToken, EbnfParserContext> symbol(final char c, final String name){
        return EbnfParserContext.string(c)
                .transform((character, context) -> EbnfSymbolParserToken.with(character.value(), character.text()))
                .setToString(name)
                .cast();
    }

    /**
     * Creates a parser that matches the given character and wraps it inside a {@link EbnfSymbolParserToken}
     */
    private static Parser<ParserToken, EbnfParserContext> symbol(final String symbol, final String name){
        return EbnfParserContext.string(symbol)
                .transform((character, context) -> EbnfSymbolParserToken.with(character.value(), character.text()))
                .setToString(name)
                .cast();
    }

    /**
     * Accepts a {@link SequenceParserToken} that contains a mixture of symbols and {@link StringParserToken}
     * returning a string holding all the characters as a {@link String}
     */
    static String string(final SequenceParserToken token) {
        final StringBuilder string = new StringBuilder();

        // join all the character parser tokens
        token.flat()
                .value()
                .stream()
                .filter(t -> t instanceof StringParserToken)
                .forEach(t -> {
                    StringParserToken stringParserToken = t.cast();
                    string.append(stringParserToken.value());
                });
        return string.toString();
    }

    private static BiFunction<SequenceParserToken, EbnfParserContext, ParserToken> filterAndWrapMany(final BiFunction<List<ParserToken>, String, EbnfParserToken> wrapper) {
        return (sequence, context) -> {
            final List<EbnfParserToken> many = filterNonEbnfParserTokens(sequence);
            return wrapper.apply(Cast.to(many), sequence.text());
        };
    }

    private static List<EbnfParserToken> filterNonEbnfParserTokens(final SequenceParserToken sequence) {
        return sequence.flat()
                .value()
                .stream()
                .filter(token -> token instanceof EbnfParserToken)
                .map(t -> EbnfParserToken.class.cast(t))
                .collect(Collectors.toList());
    }

    /**
     * <pre>
     * grammar = { rule } ;
     * </pre>
     */
    final static Parser<EbnfGrammarParserToken, EbnfParserContext> GRAMMAR = grammar();

    private static Parser<EbnfGrammarParserToken, EbnfParserContext> grammar() {
        return Parsers.<EbnfParserContext>sequenceParserBuilder()
                .optional(WHITESPACE_OR_COMMENT)
                .required(RULE.orReport(ParserReporters.basic()))
                .optional(RULE.repeating().cast())
                .build()
                .transform(EbnfGrammarParser::grammarParserToken)
                .cast();
    }

    private static EbnfGrammarParserToken grammarParserToken(final SequenceParserToken sequence, final EbnfParserContext context) {
        return EbnfGrammarParserToken.with(sequence.flat()
                .removeMissing()
                .value(),
                sequence.text());
    }

    @Override
    public Optional<EbnfGrammarParserToken> parse(final TextCursor cursor, final EbnfParserContext context) {
        return GRAMMAR.parse(cursor, context);
    }

    @Override
    public String toString() {
        return GRAMMAR.toString();
    }
}
