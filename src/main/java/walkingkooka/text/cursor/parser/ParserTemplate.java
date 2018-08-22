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

import java.util.Optional;

/**
 * A template parser that only calls the abstract method if the cursor is not empty and also restores the cursor position,
 * on failures.
 */
abstract class ParserTemplate<T extends ParserToken, C extends ParserContext> implements Parser<T, C> {

    @Override
    public final Optional<T> parse(final TextCursor cursor, final C context) {
        return cursor.isEmpty() ?
                this.fail() :
                this.tryParse(cursor, context);
    }

    /**
     * Returns an empty optional which matches an unsuccessful parser attempt.
     */
    final Optional<T> fail() {
        return Optional.empty();
    }

    abstract Optional<T> tryParse(final TextCursor cursor, final C context);
}
