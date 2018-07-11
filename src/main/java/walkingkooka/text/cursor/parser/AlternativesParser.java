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

import walkingkooka.text.cursor.TextCursor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link Parser} that tries all parsers until one is matched and then ignores the remainder.
 */
final class AlternativesParser<T extends ParserToken, C extends ParserContext> extends ParserTemplate<T, C> {

    static <T extends ParserToken, C extends ParserContext> Parser<T,C> with(final List<Parser<T, C>> parsers){
        Objects.requireNonNull(parsers, "parsers");

        Parser<T, C> parser;

        switch(parsers.size()){
            case 0:
                throw new IllegalArgumentException("At least one parser must be provided");
            case 1:
                parser = parsers.get(0);
                break;
            default:
                parser = new AlternativesParser<>(parsers);
                break;
        }

        return parser;
    }

    private AlternativesParser(final List<Parser<T, C>> parsers){
        this.parsers = parsers;
    }

    @Override
    Optional<T> tryParse(final TextCursor cursor, final C context) {
        Optional<T> token = null;

        for(Parser<T, C> parser : this.parsers) {
            token = parser.parse(cursor, context);
            if(token.isPresent()){
                break;
            }
        }

        return token;
    }

    private final List<Parser<T, C>> parsers;

    @Override
    public String toString() {
        return this.parsers.stream()
                .map(p -> p.toString())
                .collect(Collectors.joining(" | "));
    }
}
