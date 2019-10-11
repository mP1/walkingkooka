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
package walkingkooka.text.cursor.parser;

import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A {@link Parser} that delegates to another parser, and runs the given {@link BiFunction} on successful
 * tokens.
 */
final class TransformingParser<C extends ParserContext> implements Parser<C> {

    static <C extends ParserContext> TransformingParser<C> with(Parser<C> parser, BiFunction<ParserToken, C, ParserToken> transformer) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(transformer, "transformer");

        return new TransformingParser<>(parser, transformer);
    }

    private TransformingParser(Parser<C> parser, BiFunction<ParserToken, C, ParserToken> transformer) {
        this.parser = parser;
        this.transformer = transformer;
    }

    @Override
    public Optional<ParserToken> parse(final TextCursor cursor, final C context) {
        final Optional<ParserToken> token = this.parser.parse(cursor, context);
        return token.map(parserToken -> this.transformer.apply(parserToken, context));

    }

    private final Parser<C> parser;

    /**
     * A {@link BiFunction} that transforms successful tokens into another.
     */
    private final BiFunction<ParserToken, C, ParserToken> transformer;

    @Override
    public String toString() {
        return this.parser.toString();
    }
}
