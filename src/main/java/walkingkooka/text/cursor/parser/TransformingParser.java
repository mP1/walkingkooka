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

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A {@link Parser} that delegates to another parser, and runs the given {@link BiFunction} on successful
 * tokens.
 */
final class TransformingParser<TIN extends ParserToken, TOUT extends ParserToken, C extends ParserContext> implements Parser<TOUT, C> {

    static <TIN extends ParserToken, TOUT extends ParserToken, C extends ParserContext> TransformingParser<TIN, TOUT, C> with(Parser<TIN, C> parser, BiFunction<TIN, C, TOUT> transformer) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(transformer, "transformer");

        return new TransformingParser<>(parser, transformer);
    }

    private TransformingParser(Parser<TIN, C> parser, BiFunction<TIN, C, TOUT> transformer) {
        this.parser = parser;
        this.transformer = transformer;
    }

    @Override
    public Optional<TOUT> parse(final TextCursor cursor, final C context) {
        final Optional<TIN> token = this.parser.parse(cursor, context);
        return token.isPresent() ?
                Optional.of(this.transformer.apply(token.get(), context)) :
                Optional.empty();

    }

    private final Parser<TIN, C> parser;

    /**
     * A {@link BiFunction} that transforms successful tokens into another.
     */
    private final BiFunction<TIN, C, TOUT> transformer;

    @Override
    public String toString() {
        return this.parser.toString();
    }
}
