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
package walkingkooka.text.cursor.parser;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.cursor.parser.function.ParserBiFunctions;
import walkingkooka.type.PublicStaticHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A collection of factory methods to create parsers.
 */
public final class Parsers implements PublicStaticHelper {

    /**
     * {@see AlternativesParser}
     */
    public static <T extends ParserToken, C extends ParserContext> Parser<T,C> alternatives(final List<Parser<T, C>> parsers){
        return AlternativesParser.with(parsers);
    }

    /**
     * {@see CharacterCharPredicateParser}
     */
    public static <C extends ParserContext> Parser<CharacterParserToken, C> character(final CharPredicate predicate) {
        return CharacterCharPredicateParser.with(predicate);
    }

    /**
     * {@see CustomToStringParser}
     */
    public static <T extends ParserToken, C extends ParserContext> Parser<T, C> customToString(final Parser<T, C> parser, final String toString) {
        return CustomToStringParser.wrap(parser, toString);
    }

    /**
     * {@see DoubleQuotedParserToken}
     */
    public static <C extends ParserContext> Parser<DoubleQuotedParserToken, C> doubleQuoted(){
        return DoubleQuotedParser.instance();
    }

    /**
     * {@see FixedParser}
     */
    static <T extends ParserToken, C extends ParserContext> FixedParser<T, C> fixed(final Optional<T> result) {
        return FixedParser.with(result);
    }

    /**
     * {@see NumberParser}
     */
    public static <C extends ParserContext> Parser<NumberParserToken, C> number(final int radix) {
        return NumberParser.with(radix);
    }

    /**
     * {@see OptionalParser}
     */
    public static <C extends ParserContext> Parser<ParserToken, C> optional(final Parser<? extends ParserToken, C> parser, final ParserTokenNodeName name) {
        return OptionalParser.with(parser, name);
    }

    /**
     * {@see RepeatedParser}
     */
    public static <T extends ParserToken, C extends ParserContext> Parser<RepeatedParserToken<T>, C> repeated(final Parser<T, C> parser){
        return RepeatedParser.with(parser);
    }

    /**
     * {@see SequenceParserBuilder}
     */
    public static <C extends ParserContext> SequenceParserBuilder<C> sequenceParserBuilder() {
        return SequenceParserBuilder.create();
    }

    /**
     * {@see SingleQuotedParserToken}
     */
    public static <C extends ParserContext> Parser<SingleQuotedParserToken, C> singleQuoted(){
        return SingleQuotedParser.instance();
    }

    /**
     * {@see StringCharPredicateParser}
     */
    public static <C extends ParserContext> Parser<StringParserToken, C> stringCharPredicate(final CharPredicate predicate,
                                                                                             final int minLength,
                                                                                             final int maxLength) {
        return StringCharPredicateParser.with(predicate, minLength, maxLength);
    }

    /**
     * {@see StringParser}
     */
    public static <C extends ParserContext> Parser<StringParserToken, C> string(final String literal) {
        return StringParser.with(literal);
    }

    /**
     * {@see SurroundStringParser}
     */
    public static <C extends ParserContext> Parser<StringParserToken, C> surround(final String open, final String close) {
        return SurroundStringParser.with(open, close);
    }

    /**
     * Returns a {@link Parser} that merges the text from the tokens before and after the middle, returning just the middle
     */
    public static <T extends ParserToken, C extends ParserContext> Parser<T, C> surroundAndMerge(final Parser<? extends ParserToken, C> before,
                                                                                    final ParserTokenNodeName beforeName,
                                                                                    final Parser<T, C> middle,
                                                                                    final ParserTokenNodeName middleName,
                                                                                    final Class<T> middleTokenClass,
                                                                                    final Parser<? extends ParserToken, C> after,
                                                                                    final ParserTokenNodeName afterName) {
        final Parser<SequenceParserToken, C> three = Parsers.<C>sequenceParserBuilder()
                .optional(before, beforeName)
                .required(middle, middleName)
                .optional(after, afterName)
                .build();

        return Parsers.transform(three,
                ParserBiFunctions.sequenceMerger(middleTokenClass));
    }

    /**
     * {@see TransformingParser}
     */
    public static <TIN extends ParserToken, TOUT extends ParserToken, C extends ParserContext> Parser<TOUT, C> transform(final Parser<TIN, C> parser,
                                                                                                                    final BiFunction<TIN, C, TOUT> transformer) {
        return TransformingParser.with(parser, transformer);
    }

    /**
     * {@see UnicodeEscapeCharacterParser}
     */
    public static <C extends ParserContext> Parser<CharacterParserToken, C> unicodeEscapeCharacter() {
        return UnicodeEscapeCharacterParser.get();
    }

    /**
     * Stop creation.
     */
    private Parsers() {
        throw new UnsupportedOperationException();
    }
}
