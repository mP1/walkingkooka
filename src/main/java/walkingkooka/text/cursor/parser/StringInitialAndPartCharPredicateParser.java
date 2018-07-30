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
 *
 */
package walkingkooka.text.cursor.parser;

import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} that uses two {@link walkingkooka.predicate.character.CharPredicate}.
 * The final matched token must have a length between min and max.
 */
final class StringInitialAndPartCharPredicateParser<C extends ParserContext> extends ParserTemplate2<StringParserToken, C> {

    /**
     * Factory that creates a new {@link StringInitialAndPartCharPredicateParser}
     */
    static <C extends ParserContext> StringInitialAndPartCharPredicateParser<C> with(final CharPredicate initial,
                                                                                     final CharPredicate part,
                                                                                     final int minLength,
                                                                                     final int maxLength) {
        Objects.requireNonNull(initial, "initial");
        Objects.requireNonNull(part, "part");
        if (minLength < 1) {
            throw new IllegalArgumentException("Min length " + minLength + " must be greater than 0");
        }
        if (maxLength <= minLength) {
            throw new IllegalArgumentException("Max length " + minLength + " must be greater than min length " + minLength);
        }

        return new StringInitialAndPartCharPredicateParser<>(initial, part, minLength, maxLength);
    }

    /**
     * Private ctor use factory
     */
    private StringInitialAndPartCharPredicateParser(final CharPredicate initial,
                                                    final CharPredicate part,
                                                    final int minLength,
                                                    final int maxLength) {
        this.initial = initial;
        this.part = part;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    Optional<StringParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        Optional<StringParserToken> result = null;

        if (!cursor.isEmpty()) {
            final char first = cursor.at();
            if (!this.initial.test(first)) {
                result = Optional.empty();
            } else {
                final StringBuilder text = new StringBuilder();
                text.append(first);

                cursor.next();

                for (; ; ) {
                    if (cursor.isEmpty()) {
                        result = this.stringParserToken(text);
                        break;
                    }

                    final char at = cursor.at();
                    if (!this.part.test(at)) {
                        result = this.stringParserToken(text);
                        break;
                    }
                    text.append(at);
                    cursor.next();

                    // text too long...abort
                    if (text.length() > this.maxLength) {
                        result = Optional.empty();
                        break;
                    }
                }
            }
        }

        return result;
    }

    private Optional<StringParserToken> stringParserToken(final StringBuilder text) {
        return text.length() < this.minLength ?
                Optional.empty() :
                stringParserToken0(text);
    }

    private static Optional<StringParserToken> stringParserToken0(final StringBuilder text) {
        final String finalText = text.toString();
        return StringParserToken.with(finalText, finalText)
                .success();
    }

    private final CharPredicate initial;
    private final CharPredicate part;
    private final int minLength;
    private final int maxLength;

    @Override
    public String toString() {
        return this.initial + " " + this.part;
    }
}
