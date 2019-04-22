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
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.Whitespace;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * Wraps another {@link Parser} replacing or ignoring its {@link Parser#toString()} with the provided {@link String}.
 */
final class CustomToStringParser<C extends ParserContext> implements Parser<C>, HashCodeEqualsDefined {

    static <C extends ParserContext> Parser<C> wrap(final Parser<C> parser, final String toString) {
        Objects.requireNonNull(parser, "parser");
        Whitespace.failIfNullOrEmptyOrWhitespace(toString, "toString");

        Parser<C> result;

        for (; ; ) {
            if (parser.toString().equals(toString)) {
                result = parser;
                break;
            }

            Parser<C> wrap = parser;
            if (parser instanceof CustomToStringParser) {
                // unwrap then re-wrap the parser...
                final CustomToStringParser<C> custom = Cast.to(wrap);
                wrap = custom.parser;
            }
            result = new CustomToStringParser<>(wrap, toString);
            break;
        }

        return result;
    }

    private CustomToStringParser(final Parser<C> parser, final String toString) {
        this.parser = parser;
        this.toString = toString;
    }

    @Override
    public Optional<ParserToken> parse(final TextCursor cursor, final C context) {
        return this.parser.parse(cursor, context);
    }

    @Override
    public Parser<C> setToString(final String toString) {
        return this.toString.equals(toString) ?
                this :
                wrap(this.parser, toString);
    }

    // @VisibleForTesting
    final Parser<C> parser;

    // Object.................................................................................................

    @Override
    public int hashCode() {
        return this.parser.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof CustomToStringParser && this.equals0(Cast.to(other));
    }

    private boolean equals0(final CustomToStringParser<?> other) {
        return this.parser.equals(other.parser) &&
                this.toString.equals(other.toString);
    }

    @Override
    public String toString() {
        return toString;
    }

    // @VisibleForTesting
    final String toString;
}
