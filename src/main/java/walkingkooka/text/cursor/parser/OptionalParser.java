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
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} wraps another and if that parser fails, substitutes a {@link MissingParserToken}
 */
final class OptionalParser<C extends ParserContext> implements Parser<ParserToken, C> {

    static <C extends ParserContext> OptionalParser<C> with(final Parser<? extends ParserToken, C> parser, final ParserTokenNodeName name) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(name, "name");

        return new OptionalParser<C>(Cast.to(parser), name);
    }

    private OptionalParser(final Parser<ParserToken, C> parser, final ParserTokenNodeName name) {
        this.parser = parser;
        this.missing = Cast.to(ParserTokens.missing(name, "")
                .success());
    }

    @Override
    public Optional<ParserToken> parse(final TextCursor cursor, final C context) {
        final Optional<ParserToken> token = this.parser.parse(cursor, context);
        return token.isPresent() ?
                token :
                this.missing;
    }

    @Override
    public OptionalParser<C> optional(final ParserTokenNodeName name) {
        Objects.requireNonNull(name, "name");
        return this.missing.get().name().equals(name) ?
                this :
                new OptionalParser<C>(this.parser, name);
    }

    private final Parser<ParserToken, C> parser;
    private final Optional<ParserToken> missing;

    @Override
    public String toString() {
        return this.parser + "?";
    }
}
