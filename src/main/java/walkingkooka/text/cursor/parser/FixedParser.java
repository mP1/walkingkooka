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

/**
 * A {@link Parser} that consumes nothing from the {@link TextCursor} and returns a fixed {@link Optional} with or
 * without a {@link ParserToken}.
 */
final class FixedParser<T extends ParserToken, C extends ParserContext> implements Parser<T, C> {

    static <T extends ParserToken, C extends ParserContext> FixedParser<T, C> with(final Optional<T> result) {
        Objects.requireNonNull(result, "result");
        if(result.isPresent()) {
            final String text = result.get().text();
            if(!text.isEmpty()) {
                throw new IllegalArgumentException("Result must have an empty text but was " + result);
            }
        }

        return new FixedParser<>(result);
    }

    private FixedParser(final Optional<T> result) {
        this.result = result;
    }

    @Override
    public Optional<T> parse(final TextCursor cursor, final C context) {
        return this.result;

    }

    private final Optional<T> result;

    @Override
    public String toString() {
        return this.result.toString();
    }
}
