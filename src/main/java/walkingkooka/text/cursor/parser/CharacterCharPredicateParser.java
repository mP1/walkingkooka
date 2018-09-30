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
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} that matches a single character using the provided {@link CharPredicate}
 */
final class CharacterCharPredicateParser<C extends ParserContext> extends ParserTemplate<CharacterParserToken, C> {

    static <C extends ParserContext> CharacterCharPredicateParser<C> with(final CharPredicate predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return new CharacterCharPredicateParser<>(predicate);
    }

    private CharacterCharPredicateParser(final CharPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    Optional<CharacterParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint save) {
        final char first = cursor.at();
        return this.predicate.test(first) ?
                this.makeSuccessfulResultAndAdvance(first, cursor) :
                this.fail();
    }

    private final CharPredicate predicate;

    private Optional<CharacterParserToken> makeSuccessfulResultAndAdvance(final char c, final TextCursor cursor) {
        final Optional<CharacterParserToken> token = CharacterParserToken.with(c, String.valueOf(c)).success();
        cursor.next();
        return token;
    }

    @Override
    public String toString() {
        return this.predicate.toString();
    }
}
