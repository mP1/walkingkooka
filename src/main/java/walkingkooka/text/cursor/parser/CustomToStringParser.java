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
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * Wraps another {@link Parser} replacing or ignoring its {@link Parser#toString()} with the provided {@link String}.
 */
final class CustomToStringParser<T extends ParserToken, C extends ParserContext> implements Parser<T, C> {

    static <T extends ParserToken, C extends ParserContext> Parser<T, C> wrap(final Parser<T, C> parser, final String toString) {
        Objects.requireNonNull(parser, "parser");
        Whitespace.failIfNullOrWhitespace(toString, "toString");

        Parser<T, C> result;

        for(;;){
            if(parser.toString().equals(toString)){
                result = parser;
                break;
            }

            Parser<T, C> wrap = parser;
            if(parser instanceof CustomToStringParser) {
                // unwrap then re-wrap the parser...
                final CustomToStringParser<T, C> custom = Cast.to(wrap);
                wrap = custom.parser;
            }
            result = new CustomToStringParser<>(wrap, toString);
            break;
        }

        return result;
    }

    private CustomToStringParser(final Parser<T, C> parser, final String toString) {
        this.parser = parser;
        this.toString = toString;
    }

    @Override
    public Optional<T> parse(final TextCursor cursor, final C context) {
        return this.parser.parse(cursor, context);
    }

    public Parser<T, C> setToString(final String toString) {
        return this.toString.equals(toString) ?
                this :
                wrap(this.parser, toString);
    }

    // @VisibleForTesting
    final Parser<T, C> parser;

    @Override
    public String toString() {
        return toString;
    }

    // @VisibleForTesting
    final String toString;
}
