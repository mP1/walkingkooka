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

import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link Parser} that requires all parsers are matched in order returning all tokens within a {@link SequenceParserToken}
 */
final class SequenceParser<T extends ParserToken, C extends ParserContext> extends ParserTemplate2<SequenceParserToken<T>, C> {

    /**
     * Factory that requires at least one parser, returning the parser if one is present or wrapping the many in a {@link SequenceParser}
     */
    static <T extends ParserToken, C extends ParserContext> Parser<?,C> with(final List<Parser<T, C>> parsers){
        Objects.requireNonNull(parsers, "parsers");

        Parser<?, C> parser;

        switch(parsers.size()){
            case 0:
                throw new IllegalArgumentException("At least one parser must be provided");
            case 1:
                parser = parsers.get(0);
                break;
            default:
                parser = new SequenceParser<T, C>(parsers);
                break;
        }

        return parser;
    }

    private SequenceParser(final List<Parser<T, C>> parsers){
        this.parsers = parsers;
    }

    /**
     * Try all parsers and if they all match return a {@link SequenceParserToken} otherwise throwaway any leading tokens.
     */
    @Override
    Optional<SequenceParserToken<T>> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        Optional<SequenceParserToken<T>> result = null;

        final List<T> tokens = Lists.array();
        for( Parser<T, C> parser : this.parsers) {
            final Optional<T> current = parser.parse(cursor, context);
            if(!current.isPresent()) {
                result = this.fail();
                break;
            }
            tokens.add(current.get());
        }

        if(null==result){
            result = SequenceParserToken.with(tokens,
                    start.textBetween().toString())
                    .success();
        }

        return result;
    }

    private final List<Parser<T, C>> parsers;

    @Override
    public String toString() {
        return this.parsers.stream()
                .map(p -> p.toString())
                .collect(Collectors.joining(", "));
    }
}
