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
final class StringCharPredicateParser<C extends ParserContext> extends ParserTemplate2<StringParserToken, C> {

    static <C extends ParserContext> StringCharPredicateParser<C> with(final CharPredicate predicate) {
        Objects.requireNonNull(predicate, "predicate");

        return new StringCharPredicateParser<>(predicate);
    }

    private StringCharPredicateParser(final CharPredicate predicate) {
        this.predicate = predicate;
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

        while(!cursor.isEmpty() && this.predicate.test(cursor.at())) {
            cursor.next();
        }

        final String text = start.textBetween().toString();

        return StringParserToken.with(text, text).success();
    }

    @Override
    public String toString() {
        return this.predicate.toString();
    }
}
