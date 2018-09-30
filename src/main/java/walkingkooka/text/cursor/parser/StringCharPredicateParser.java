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
 * A {@link Parser} that matches a one or more characters using the provided {@link CharPredicate}
 */
final class StringCharPredicateParser<C extends ParserContext> extends ParserTemplate<StringParserToken, C> {

    static <C extends ParserContext> StringCharPredicateParser<C> with(final CharPredicate predicate, final int minLength, final int maxLength) {
        Objects.requireNonNull(predicate, "predicate");
        if(minLength <= 0) {
            throw new IllegalArgumentException("Min length " + minLength + " must be greater than 0");
        }
        if(maxLength < minLength) {
            throw new IllegalArgumentException("Maxlength " + maxLength + " must be greater/equal than maxLength: " + minLength);
        }

        return new StringCharPredicateParser<>(predicate, minLength, maxLength);
    }

    private StringCharPredicateParser(final CharPredicate predicate, final int minLength, final int maxLength) {
        this.predicate = predicate;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    Optional<StringParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        return this.predicate.test(cursor.at()) ?
                this.consumeRemaining(cursor, start) :
                this.fail();
    }

    private final CharPredicate predicate;

    private Optional<StringParserToken> consumeRemaining(final TextCursor cursor, final TextCursorSavePoint start) {
        cursor.next();

        int i = 1;
        while(!cursor.isEmpty() && i < this.maxLength && this.predicate.test(cursor.at())) {
            cursor.next();

            i++;
        }

        return i >= this.minLength ?
            stringParserToken(start) :
            this.fail();
    }

    private final int minLength;
    private final int maxLength;

    private static Optional<StringParserToken> stringParserToken(final TextCursorSavePoint start) {
        final String text = start.textBetween().toString();
        return StringParserToken.with(text, text).success();
    }

    @Override
    public String toString() {
        return this.predicate.toString();
    }
}
