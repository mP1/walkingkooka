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

import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Parser} that only matches the given {@link String} which must not be null or empty.
 */
final class StringParser<C extends ParserContext> extends ParserTemplate2<StringParserToken, C> {

    static <C extends ParserContext> StringParser<C> with(final String string) {
        Objects.requireNonNull(string, "string");
        if(string.isEmpty()) {
            throw new IllegalArgumentException("String must not be empty");
        }

        return new StringParser<>(string);
    }

    private StringParser(final String string) {
        this.string = string;
    }

    @Override
    Optional<StringParserToken> tryParse0(final TextCursor cursor, final C context, final TextCursorSavePoint start) {
        Optional<StringParserToken> result;
        int matched = 0;
        final String string = this.string;

        for(;;) {
            if(cursor.isEmpty() || string.charAt(matched) != cursor.at()) {
                result = Optional.empty();
                break;
            }
            matched++;
            cursor.next();

            if(string.length() == matched) {
                result = StringParserToken.with(this.string, this.string).success();
                break;
            }
        }

        return result;
    }

    private final String string;

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(this.string).toString();
    }
}
