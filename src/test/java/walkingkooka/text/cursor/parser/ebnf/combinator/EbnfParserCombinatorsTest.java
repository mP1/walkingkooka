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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.test.ResourceTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.BigIntegerParserToken;
import walkingkooka.text.cursor.parser.FakeParserContext;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserTesting;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.StringParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfAlternativeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfConcatenationParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfExceptionParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGrammarParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfGroupParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfOptionalParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserContexts;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRangeParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRepeatedParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfRuleParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfTerminalParserToken;
import walkingkooka.type.JavaVisibility;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class EbnfParserCombinatorsTest implements ParserTesting<Parser<FakeParserContext>, FakeParserContext>,
        PublicStaticHelperTesting<EbnfParserCombinators>,
        ResourceTesting {

    @Test
    @Disabled("Until proper error reporting is available")
    public void testEmptyCursorFail() {
        this.parseFailAndCheck("");
    }

    @Test
    public void testParserToString() {
        assertEquals("'abc'", createParser2().toString());
    }

    @Test
    public void testTerminal() {
        final String text = "terminal-text-123";
        this.parseAndCheck2(text, this.string(text), text);
    }

    @Test
    public void testAlternative() {
        final Parser<FakeParserContext> parser = createParser2();

        final String text = "abc";
        this.parseAndCheck2(parser, text, this.string(text), text);

        final String text2 = "xyz";
        this.parseAndCheck2(parser, text2, this.string("xyz"), text2);
    }

    @Test
    public void testConcatenation() {
        final Parser<FakeParserContext> parser = createParser2();

        final String text1 = "abc";
        final String text2 = "xyz";
        final String concatText = text1 + text2;
        this.parseAndCheck2(parser,
                concatText,
                ParserTokens.sequence(Lists.of(this.string(text1), this.string(text2)), concatText),
                concatText);
    }

    @Test
    public void testException() {
        final Parser<FakeParserContext> parser = createParser2();

        final String text = "match";
        this.parseAndCheck2(parser, text, this.string(text), text);

        this.parseFailAndCheck(parser, "abc");
    }

    @Test
    public void testGroup() {
        final String text = "group-text-abc";
        this.parseAndCheck2(text, this.string(text), text);
    }

    @Test
    public void testIdentifierDuplicateRuleGrammarFails() {
        assertThrows(EbnfParserCombinatorDuplicateRuleException.class, () -> {
            this.createParser2();
        });
    }

    @Test
    public void testIdentifierDuplicateRulePredefinedFails() {
        assertThrows(EbnfParserCombinatorDuplicateRuleException.class, () -> {
            this.createParser2();
        });
    }

    @Test
    public void testIdentifierReference() {
        final String text = "abc123";
        this.parseAndCheck2(text, this.string(text), text);
    }

    @Test
    public void testIdentifierReference2() {
        final String text = "abc123";
        this.parseAndCheck2(text, this.string(text), text);
    }

    @Test
    public void testOptional() {
        final Parser<FakeParserContext> parser = createParser2();

        final String text = "optional-text-abc";
        this.parseAndCheck2(parser, text, this.string(text), text);

        this.parseFailAndCheck(parser, "different");
    }

    @Test
    public void testRepeat() {
        final Parser<FakeParserContext> parser = createParser2();

        final String text = "abc";
        final String after = "123";
        final String repeatedText = text + text;
        this.parseAndCheck2(parser,
                repeatedText + after,
                ParserTokens.repeated(Lists.of(this.string(text), this.string(text)), repeatedText),
                repeatedText,
                after);
    }

    @Test
    public void testRangeTerminalTerminal() {
        this.parseRangeAndCheck();
    }

    @Test
    public void testRangeIdentifierTerminal() {
        this.parseRangeAndCheck();
    }

    @Test
    public void testRangeTerminalIdentifier() {
        this.parseRangeAndCheck();
    }

    @Test
    public void testRangeIdentifierIdentifier() {
        this.parseRangeAndCheck();
    }

    @Test
    public void testRangeIdentifierIdentifier2() {
        this.parseRangeAndCheck();
    }

    private void parseRangeAndCheck() {
        final Parser<FakeParserContext> parser = createParser2();

        final String text = "m";
        final String after = "123";
        this.parseAndCheck2(parser,
                text + after,
                this.string(text),
                text,
                after);

        this.parseFailAndCheck(parser, "Q");
    }

    @Test
    public void testRangeInvalidBoundFails() {
        // from is a Range, terminal or identifier to terminal required
        this.parseFailAndCheck("abc");
    }

    @Test
    public void testRangeInvalidBoundFails2() {
        // from is a repeated, terminal or identifier to terminal required
        this.parseFailAndCheck("abc");
    }

    @Test
    public void testRangeInvalidBoundFails3() {
        // to is a Optional, terminal or identifier to terminal required
        this.parseFailAndCheck("abc");
    }

    @Test
    public void testComplex() {
        final Parser<FakeParserContext> parser = createParser2();

        final ParserToken i = this.string("i");
        final ParserToken p = this.string("p");
        final ParserToken q = this.string("q");

        {
            // required i1 and required p1 and optional p1
            final String text = "ipq";
            final String after = "!!!";

            final ParserToken repeatedQ = ParserTokens.repeated(Lists.of(q), "q");

            this.parseAndCheck2(parser,
                    text + after,
                    ParserTokens.sequence(Lists.of(i, p, repeatedQ), text),
                    text,
                    after);
        }

        {
            // required i1 and required p1 and repeating q
            final String text = "ipqqq";
            final String after = "!!!";

            final ParserToken repeatedQ = ParserTokens.repeated(Lists.of(q, q, q), "qqq");

            this.parseAndCheck2(parser,
                    text + after,
                    ParserTokens.sequence(Lists.of(i, p, repeatedQ), text),
                    text,
                    after);
        }

        this.parseFailAndCheck(parser, "D123");
    }

//    INITIAL =   "i";
//    PART    =   "p";
//    LAST    =   [ "L" ];
//
//    TEST    =   INITIAL, PART, {PART}, LAST];

    @Test
    public void testComplex2() {
        final Parser<FakeParserContext> parser = createParser2();

        final ParserToken i = this.string("i");
        final ParserToken p = this.string("p");
        final ParserToken q = this.string("q");
        final ParserToken l = this.string("L");

        {
            // without optional L
            final String text = "ipp";
            final String after = "!!!";

            final ParserToken repeatedP = ParserTokens.repeated(Lists.of(p), "p");

            this.parseAndCheck2(parser,
                    text + after,
                    ParserTokens.sequence(Lists.of(i, p, repeatedP), text),
                    text,
                    after);
        }

        {
            // includes optional L
            final String text = "ippL";
            final String after = "!!!";

            final ParserToken repeatedP = ParserTokens.repeated(Lists.of(p), "p");

            this.parseAndCheck2(parser,
                    text + after,
                    ParserTokens.sequence(Lists.of(i, p, repeatedP, l), text),
                    text,
                    after);
        }

        this.parseFailAndCheck(parser, "ip");
    }

    @Test
    public void testReplaceToken() {
        final Parser<FakeParserContext> parser = createParser2();

        // <8> is optional
        {
            final String text = "9";
            final String after = "xyz";
            this.parseAndCheck2(parser,
                    text + after,
                    number(text),
                    text,
                    after);
        }

        this.parseFailAndCheck(parser, "D123");
    }

    @Test
    public void testPublicMethodsWithoutMathContextParameter() {
        this.publicMethodParametersTypesCheck(Sets.of(MathContext.class));
    }

    // HELPERS ............................................................................................................

    private void parseAndCheck2(final String textCursor, final ParserToken expected, final String text) {
        this.parseAndCheck2(textCursor, expected, text, "");
    }

    private void parseAndCheck2(final String textCursor, final ParserToken expected, final String text, final String textAfter) {
        this.parseAndCheck2(this.createParser2(), textCursor, expected, text, textAfter);
    }

    private void parseAndCheck2(final Parser<FakeParserContext> parser, final String textCursor, final ParserToken expected, final String text) {
        this.parseAndCheck(parser, textCursor, expected, text, "");
    }

    private void parseAndCheck2(final Parser<FakeParserContext> parser, final String textCursor, final ParserToken expected, final String text, final String textAfter) {
        this.parseAndCheck(parser, textCursor, expected, text, textAfter);
    }

    @Override
    public Parser<FakeParserContext> createParser() {
        return this.createParser("default.grammar");
    }

    private Parser<FakeParserContext> createParser2() {
        return this.createParser(this.currentTestName() + ".grammar");
    }

    /**
     * Parses the grammar file, uses the transformer to convert each rule into parsers and then returns the parser for the rule called "TEST".
     */
    private Parser<FakeParserContext> createParser(final String grammarResourceFile) {
        final EbnfGrammarParserToken grammar = this.grammar(grammarResourceFile);

        final Map<EbnfIdentifierName, Parser<ParserContext>> defaults = Maps.hash();
        defaults.put(EbnfIdentifierName.with("LETTERS"), Parsers.stringCharPredicate(CharPredicates.letter(), 1, Integer.MAX_VALUE).cast());
        defaults.put(EbnfIdentifierName.with("DUPLICATED"), Parsers.fake());

        final Map<EbnfIdentifierName, Parser<ParserContext>> all = grammar.combinator(defaults,
                this.syntaxTreeTransformer(grammar));

        final Parser<FakeParserContext> test = Cast.to(all.get(TEST));
        assertNotNull(test, TEST + " parser not found in grammar\n" + grammar);
        return test;
    }

    private final EbnfIdentifierName TEST = EbnfIdentifierName.with("TEST");

    private EbnfGrammarParserToken grammar(final String resourceName) {
        try {
            final Class<?> classs = this.getClass();
            final String text = this.resourceAsText(classs, classs.getSimpleName() + "/" + resourceName);
            final TextCursor cursor = TextCursors.charSequence(text);
            final Optional<ParserToken> grammar = EbnfParserToken.grammarParser()
                    .parse(cursor, EbnfParserContexts.basic());
            if (!grammar.isPresent()) {
                fail("Failed to parse a grammar from " + CharSequences.quote(resourceName) + "\n" + text);
            }
            if (!cursor.isEmpty()) {
                final TextCursorSavePoint save = cursor.save();
                cursor.end();
                final CharSequence remaining = save.textBetween();
                fail("Failed to parse all of grammar from " + CharSequences.quote(resourceName) + " text remaining: " + remaining + "\n\n" + CharSequences.escape(remaining) + "\n\nGrammar File:\n" + text);
            }
            return grammar.get().cast();
        } catch (final IOException cause) {
            throw new Error("failed to read grammar from " + CharSequences.quote(resourceName));
        }
    }

    private EbnfParserCombinatorSyntaxTreeTransformer syntaxTreeTransformer(final EbnfGrammarParserToken grammar) {
        final Map<EbnfIdentifierName, EbnfParserToken> identifierToToken = Maps.sorted();
        grammar.value()
                .stream()
                .filter(t -> t instanceof EbnfParserToken)
                .map(t -> EbnfParserToken.class.cast(t))
                .filter(t -> t.isRule())
                .map(t -> EbnfRuleParserToken.class.cast(t))
                .forEach(rule -> {
                    identifierToToken.put(rule.identifier().value(), rule.token());
                });

        return new EbnfParserCombinatorSyntaxTreeTransformer() {
            @Override
            public Parser<ParserContext> alternatives(final EbnfAlternativeParserToken token, final Parser<ParserContext> parser) {
                return parser;
            }

            @Override
            public Parser<ParserContext> concatenation(final EbnfConcatenationParserToken token, final Parser<ParserContext> parser) {
                return parser.transform((sequenceParserToken, fakeParserContext) -> {
                    return sequenceParserToken;
                });
            }

            @Override
            public Parser<ParserContext> exception(final EbnfExceptionParserToken token, final Parser<ParserContext> parser) {
                return parser;
            }

            @Override
            public Parser<ParserContext> group(final EbnfGroupParserToken token, final Parser<ParserContext> parser) {
                return parser;
            }

            @Override
            public Parser<ParserContext> identifier(final EbnfIdentifierParserToken token, final Parser<ParserContext> parser) {
                return parser;
            }

            @Override
            public Parser<ParserContext> optional(final EbnfOptionalParserToken token, final Parser<ParserContext> parser) {
                return parser;
            }

            @Override
            public Parser<ParserContext> range(final EbnfRangeParserToken token, final Parser<ParserContext> ignored) {
                final char begin = this.characterForIdentifierOrTerminal(token.begin());
                final char end = this.characterForIdentifierOrTerminal(token.end());

                return Parsers.<FakeParserContext>stringCharPredicate(CharPredicates.range(begin, end), 1, 1)
                        .setToString(token.toString())
                        .cast();
            }

            private char characterForIdentifierOrTerminal(final EbnfParserToken token) {
                return token.isTerminal() ?
                        this.characterFromTerminal(token.cast()) :
                        token.isIdentifier() ?
                                this.characterFromIdentifierReference(token.cast()) :
                                failInvalidRangeBound("Invalid range bound, expected terminal or identifier indirectly pointing to a terminal but got " + token, token);
            }

            private char characterFromIdentifierReference(final EbnfIdentifierParserToken identifier) {
                final EbnfIdentifierName identifierName = identifier.value();
                final EbnfParserToken target = identifierToToken.get(identifierName);
                if (null == target) {
                    this.failInvalidRangeBound("Unknown identifier \"" + identifierName + "\"", identifier);
                }
                return this.characterForIdentifierOrTerminal(target);
            }

            private char characterFromTerminal(final EbnfTerminalParserToken terminal) {
                final String value = terminal.value();
                final CharSequence unescaped = CharSequences.unescape(value);
                if (unescaped.length() != 1) {
                    failInvalidRangeBound("The range terminal does not contain a single character=" + terminal, terminal);
                }
                return unescaped.charAt(0);
            }

            private char failInvalidRangeBound(final String message, final EbnfParserToken token) {
                throw new EbnfTerminalParserTokenInvalidRangeBoundParserCombinatorException(message, token);
            }

            @Override
            public Parser<ParserContext> repeated(final EbnfRepeatedParserToken token, final Parser<ParserContext> parser) {
                return parser;
            }

            @Override
            public Parser<ParserContext> terminal(final EbnfTerminalParserToken token, final Parser<ParserContext> parser) {
                return parser.transform((stringParserToken, contextIgnored) -> {
                    ParserToken result = stringParserToken;
                    try {
                        result = number(StringParserToken.class.cast(stringParserToken).value());
                    } catch (final NumberFormatException ignore) {
                    }
                    return result;
                }).cast();
            }
        };
    }

    private BigIntegerParserToken number(final String text) {
        return ParserTokens.bigInteger(new BigInteger(text), text);
    }

    private StringParserToken string(final String text) {
        return ParserTokens.string(text, text);
    }

    @Override
    public FakeParserContext createContext() {
        return new FakeParserContext();
    }

    @Override
    public String parserTokenTypeNamePrefix() {
        return "Ebnf";
    }

    // Class ................................................................................

    @Override
    public Class<EbnfParserCombinators> type() {
        return EbnfParserCombinators.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // PublicStaticHelperTesting................................................................

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }
}
