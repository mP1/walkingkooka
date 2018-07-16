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
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} consumes one or more characters from a {@link TextCursor} and gives a {@link ParserToken}.
 * The value (an {@link java.util.Optional} will contain the parsed value and text when successful or be empty when
 * not.
 */
public interface Parser<T extends ParserToken, C extends ParserContext> {

    /**
     * Attempts to parse the text given by the {@link TextCursor}.
     */
    Optional<T> parse(final TextCursor cursor, final C context);

    /**
     * Converts this parser into an optional.
     */
    default Parser<ParserToken, C> optional(final ParserTokenNodeName name) {
        return Parsers.optional(this, name);
    }

    /**
     * Combines this parser with another.
     */
    default Parser<T, C> or(final Parser<T, C> parser) {
        Objects.requireNonNull(parser, "parser");

        return Parsers.alternatives( Lists.of(this, parser));
    }

    /**
     * Makes this a repeating token.
     */
    default Parser<RepeatedParserToken<T>, C> repeating(){
        return Parsers.repeated(this);
    }

    /**
     * Helper that makes casting and working around generics a little less noisy.
     */
    default <P extends Parser<T, C>, T extends ParserToken> P cast() {
        return Cast.to(this);
    }
}
