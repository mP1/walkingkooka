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
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenNodeName;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.SequenceParserBuilder;
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

    // low level ATOMS ..................................................................................................

    /**
     * <pre>
     *     letter = "A" | "B" | "C" | "D" | "E" | "F" | "G"
     *             | "H" | "I" | "J" | "K" | "L" | "M" | "N"
     *             | "O" | "P" | "Q" | "R" | "S" | "T" | "U"
     *             | "V" | "W" | "X" | "Y" | "Z" | "a" | "b"
     *             | "c" | "d" | "e" | "f" | "g" | "h" | "i"
     *             | "j" | "k" | "l" | "m" | "n" | "o" | "p"
     *             | "q" | "r" | "s" | "t" | "u" | "v" | "w"
     *             | "x" | "y" | "z" ;
     * </pre>
     */
    final static Parser<StringParserToken, EbnfParserContext> LETTER = EbnfParserContext.string(
            CharPredicates.range('A', 'Z').or(CharPredicates.range('a', 'z')), 1, 1)
            .setToString("letter");

    /**
     * <pre>
     * digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
     * </pre>
     */
    final static Parser<StringParserToken, EbnfParserContext> DIGIT = EbnfParserContext.string('0', '9')
            .setToString("digit");

    /**
     * <pre>
     * _
     * </pre>
     */
    final static Parser<StringParserToken, EbnfParserContext> UNDERSCORE = EbnfParserContext.string('_');

    /**
     * <pre>
     * character = letter | digit |  "_" ;
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> CHARACTER = LETTER
            .or(DIGIT)
            .or(UNDERSCORE)
            .setToString("character");

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
                .castC();

        return whitespace.<EbnfParserContext>castC()
                .or(comment)
                .repeating()
                .castC();
    }

    /**
     * <pre>
     * -
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> EXCEPTION_PREFIX = symbol('-', "exception");

    /**
     * <pre>
     * =
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> ASSIGN = symbol('=', "assign");

    /**
     * <pre>
     * ;
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> TERMINATION = symbol(';', "termination");

    /**
     * <pre>
     * ..
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RANGE_SEPARATOR = symbol("..", "range");

    /**
     * <pre>
     * |
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> ALTERNATIVES_SEPARATOR = symbol('|', "alternatives_separator");

    /**
     * <pre>
     * ,
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> CONCAT_SEPARATOR = symbol(',', "concat_separator");

    /**
     * <pre>
     * [
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> OPTIONAL_OPEN = symbol('[', "optional_open");

    /**
     * <pre>
     * ]
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> OPTIONAL_CLOSE = symbol(']', "optional_close");

    /**
     * <pre>
     * {
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> REPETITION_OPEN = symbol('{', "repetition_open");

    /**
     * <pre>
     * }
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> REPETITION_CLOSE = symbol('}', "repetition_close");

    /**
     * <pre>
     * (
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> GROUP_OPEN = symbol('(', "group_open");

    /**
     * <pre>
     * )
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> GROUP_CLOSE = symbol(')', "group_close");

    /**
     * Creates a parser that matches the given character and wraps it inside a {@link EbnfSymbolParserToken}
     */
    private static Parser<ParserToken, EbnfParserContext> symbol(final char c, final String name){
        return EbnfParserContext.string(c)
                        .transform((character, context) -> EbnfSymbolParserToken.with(character.value(), character.text()))
                        .setToString(name)
                        .castC();
    }

    /**
     * Creates a parser that matches the given character and wraps it inside a {@link EbnfSymbolParserToken}
     */
    private static Parser<ParserToken, EbnfParserContext> symbol(final String symbol, final String name){
        return EbnfParserContext.string(symbol)
                .transform((character, context) -> EbnfSymbolParserToken.with(character.value(), character.text()))
                .setToString(name)
                .castC();
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
                    StringParserToken stringParserToken = Cast.to(t);
                    string.append(stringParserToken.value());
                });
        return string.toString();
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
        return EbnfParserContext.sequenceParserBuilder()
                .required(LETTER)
                .required(CHARACTER.repeating())
                .build()
                .transform((sequence, context) -> EbnfIdentifierParserToken.with(string(sequence), sequence.text()))
                .setToString("identifier")
                .castC();
    };

    /**
     * <pre>
     * terminal = "'" , character , { character } , "'"
     *          | '"' , character , { character } , '"' ;
     * </pre>
     * The above definition isnt actually correct, a terminal must be either single or quoted, and supports backslash, and unicode sequences within.
     */
    final static Parser<ParserToken, EbnfParserContext> TERMINAL = EbnfTerminalParser.INSTANCE;

    private static Parser<ParserToken, EbnfParserContext> terminal() {
        final Parser<EbnfTerminalParserToken, EbnfParserContext> singleQuoted = terminalQuote('\'');
        final Parser<EbnfTerminalParserToken, EbnfParserContext> doubleQuoted = terminalQuote('"');

        return singleQuoted.or(doubleQuoted).castC();
    }

    private static Parser<EbnfTerminalParserToken, EbnfParserContext> terminalQuote(final char quote) {
        final Parser<StringParserToken, EbnfParserContext> quoteParser = EbnfParserContext.string(quote);

        return EbnfParserContext.sequenceParserBuilder()
                .required(quoteParser)
                .required(CHARACTER.repeating())
                .required(quoteParser)
                .build()
                .transform(EbnfGrammarParser::terminal);
    }

    private static EbnfTerminalParserToken terminal(final SequenceParserToken token, final EbnfParserContext context) {
        final String quotedText = string(token);
        return EbnfTerminalParserToken.with(quotedText.substring(1, quotedText.length()-1),
                token.text());
    }

    /**
     * <pre>
     * range = terminal, '..', terminal
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RANGE =
            EbnfParserContext.sequenceParserBuilder()
                    .required(terminal())
                    .optional(WHITESPACE_OR_COMMENT)
                    .required(RANGE_SEPARATOR)
                    .optional(WHITESPACE_OR_COMMENT)
                    .required(terminal())
                    .build()
                    .transform(filterAndWrapMany(EbnfParserToken::range))
                    .setToString("range")
                    .castC();

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
    static Parser<ParserToken, EbnfParserContext> RHS = new Parser<ParserToken, EbnfParserContext>() {

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
    final static Parser<ParserToken, EbnfParserContext> OPTIONAL = parser(OPTIONAL_OPEN, WHITESPACE_OR_COMMENT, RHS, WHITESPACE_OR_COMMENT, OPTIONAL_CLOSE)
            .transform(filterAndWrapMany(EbnfParserToken::optional))
            .castC();

    /**
     * <pre>
     * "{" , rhs , "}"
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> REPETITION = parser(REPETITION_OPEN, WHITESPACE_OR_COMMENT, RHS, WHITESPACE_OR_COMMENT, REPETITION_CLOSE)
            .transform(filterAndWrapMany(EbnfParserToken::repeated))
            .castC();

    /**
     * <pre>
     * "(" , rhs , ")"
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> GROUPING = parser(GROUP_OPEN, WHITESPACE_OR_COMMENT, RHS, WHITESPACE_OR_COMMENT, GROUP_CLOSE)
            .transform(filterAndWrapMany(EbnfParserToken::grouping))
            .castC();

    /**
     * <pre>
     * "-" , rhs
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> EXCEPTION = parser(WHITESPACE_OR_COMMENT, EXCEPTION_PREFIX, WHITESPACE_OR_COMMENT, RHS)
            .transform(filterAndWrapMany(EbnfParserToken::exception))
            .castC();


    /**
     * <pre>
     * "(" , rhs , ")"
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RHS_WITHOUT_ALT_CONCAT = thenMaybeException(IDENTIFIER
            .or(OPTIONAL)
            .or(REPETITION)
            .or(GROUPING)
            .or(RANGE) // must be before TERMINAL
            .or(TERMINAL));

    /**
     * <pre>
     * rhs , "|" , rhs
     * </pre>
     * To avoid left recursion problems the first rhs is replaced as RHS_WITHOUT_ALT_CONCAT which doesnt include ALT and CONCAT
     */
    final static Parser<ParserToken, EbnfParserContext> ALTERNATIVE = parser(WHITESPACE_OR_COMMENT, RHS_WITHOUT_ALT_CONCAT, WHITESPACE_OR_COMMENT, ALTERNATIVES_SEPARATOR, WHITESPACE_OR_COMMENT, RHS)
            .transform(filterAndWrapMany(EbnfParserToken::alternative))
            .castC();

    /**
     * <pre>
     * | rhs , "," , rhs ;
     * </pre>
     * To avoid left recursion problems the first rhs is replaced as RHS_WITHOUT_ALT_CONCAT which doesnt include ALT and CONCAT
     */
    final static Parser<ParserToken, EbnfParserContext> CONCATENATION = parser(WHITESPACE_OR_COMMENT, RHS_WITHOUT_ALT_CONCAT, WHITESPACE_OR_COMMENT, CONCAT_SEPARATOR, WHITESPACE_OR_COMMENT, RHS)
            .transform(filterAndWrapMany(EbnfParserToken::concatenation))
            .castC();

    /**
     * <pre>
     * lhs , "=" , rhs , ";" ;
     * </pre>
     */
    final static Parser<ParserToken, EbnfParserContext> RULE = parser(WHITESPACE_OR_COMMENT, LHS, WHITESPACE_OR_COMMENT, ASSIGN, WHITESPACE_OR_COMMENT, RHS, WHITESPACE_OR_COMMENT, TERMINATION)
            .transform(filterAndWrapMany(EbnfParserToken::rule))
            .castC();

    /**
     * Matches any of the tokens, assumes that any leading or trailing whitespace or comments is handled elsewhere...(parent)
     * @return
     */
    private static Parser<ParserToken, EbnfParserContext> rhs() {
        if(null==RHS_CACHE) {
            RHS_CACHE = thenMaybeException(IDENTIFIER
                    .or(OPTIONAL)
                    .or(REPETITION)
                    .or(GROUPING)
                    .or(ALTERNATIVE) // longer before shorter.
                    .or(CONCATENATION)
                    .or(RANGE) // must be before TERMINAL
                    .or(TERMINAL));
        }
        return RHS_CACHE;
    }

    private static Parser<ParserToken, EbnfParserContext> RHS_CACHE;

    private static Parser<SequenceParserToken, EbnfParserContext> parser(final Parser<? super ParserToken, EbnfParserContext>...parsers) {
        final SequenceParserBuilder<EbnfParserContext> b = EbnfParserContext.sequenceParserBuilder();
        for(Parser<? super ParserToken, EbnfParserContext> parser : parsers) {
            if(parser == WHITESPACE_OR_COMMENT) {
                b.optional(parser);
            } else {
                b.required(parser);
            }
        }

        return b.build();
    }

    final static Parser<ParserToken, EbnfParserContext> thenMaybeException(final Parser<ParserToken, EbnfParserContext> parser) {
        return parser.builder(ParserTokenNodeName.with(0))
                .optional(Cast.to(EXCEPTION.repeating()))
                .build()
                .setToString("RHS").castC();
    }

    private static final BiFunction<SequenceParserToken, EbnfParserContext, ParserToken> filterAndWrapMany(final BiFunction<List<EbnfParserToken>, String, EbnfParserToken> wrapper) {
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
    final static Parser<EbnfGrammarParserToken, EbnfParserContext> GRAMMAR = RULE.repeating()
            .transform((repeated, context) -> {
                return EbnfGrammarParserToken.with(
                        Cast.to(repeated.value()), // list of rules
                        repeated.text());
            })
            .cast(EbnfGrammarParserToken.class);

    @Override
    public Optional<EbnfGrammarParserToken> parse(final TextCursor cursor, final EbnfParserContext context) {
        return GRAMMAR.parse(cursor, context);
    }

    @Override
    public String toString() {
        return GRAMMAR.toString();
    }
}
