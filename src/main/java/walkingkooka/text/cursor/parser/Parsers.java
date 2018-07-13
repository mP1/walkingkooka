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
import walkingkooka.type.PublicStaticHelper;

import java.util.List;

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
     * {@see DoubleQuotedParserToken}
     */
    public static <C extends ParserContext> Parser<DoubleQuotedParserToken, C> doubleQuoted(){
        return DoubleQuotedParser.instance();
    }

    /**
     * {@see NumberParser}
     */
    public static <C extends ParserContext> Parser<NumberParserToken, C> number(final int radix) {
        return NumberParser.with(radix);
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
    public static <C extends ParserContext> Parser<StringParserToken, C> stringCharPredicate(final CharPredicate predicate) {
        return StringCharPredicateParser.with(predicate);
    }

    /**
     * {@see StringParser}
     */
    public static <C extends ParserContext> Parser<StringParserToken, C> string(final String literal) {
        return StringParser.with(literal);
    }

    /**
     * Stop creation.
     */
    private Parsers() {
        throw new UnsupportedOperationException();
    }
}
