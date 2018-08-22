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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.cursor.TextCursor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link Parser} that tries all parsers until one is matched and then ignores the remainder.
 */
final class AlternativesParser<C extends ParserContext> extends ParserTemplate<ParserToken, C> implements HashCodeEqualsDefined {

    static <C extends ParserContext> Parser<ParserToken, C> with(final List<Parser<ParserToken, C>> parsers){
        Objects.requireNonNull(parsers, "parsers");

        Parser<ParserToken, C> parser;

        switch(parsers.size()){
            case 0:
                throw new IllegalArgumentException("At least one parser must be provided");
            case 1:
                parser = parsers.get(0).cast();
                break;
            default:
                parser = new AlternativesParser<C>(parsers);
                break;
        }

        return parser;
    }

    private AlternativesParser(final List<Parser<ParserToken, C>> parsers){
        this.parsers = parsers;
    }

    @Override
    Optional<ParserToken> tryParse(final TextCursor cursor, final C context) {
        Optional<ParserToken> token = null;

        for(Parser<ParserToken, C> parser : this.parsers) {
            token = parser.parse(cursor, context);
            if(token.isPresent()){
                break;
            }
        }

        return token;
    }

    @Override
    public Parser<ParserToken, C> or(final Parser<? extends ParserToken, C> parser) {
        Objects.requireNonNull(parser, "parser");

        // append the new parser to the current list and make a new AlternativesParser
        final List<Parser<ParserToken, C>> parsers = Lists.array();
        parsers.addAll(this.parsers);
        parsers.add(parser.cast());

        return new AlternativesParser(parsers);
    }

    private final List<Parser<ParserToken, C>> parsers;

    // Object.................................................................................................

    @Override
    public int hashCode() {
        return this.parsers.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof AlternativesParser && this.equals0(Cast.to(other));
    }

    private boolean equals0(final AlternativesParser<?> other) {
        return this.parsers.equals(other.parsers);
    }

    @Override
    public String toString() {
        return this.parsers.stream()
                .map(p -> p.toString())
                .collect(Collectors.joining(" | ", "(", ")"));
    }
}
