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

package walkingkooka.convert;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A {@link Converter} that only accepts {@link String strings} and attempts to parse them and return the result from a {@link ParserToken} that has a {@link Value}.
 * If the parser fails then a {@link #failConversion(Object, Class)} happens.
 */
final class ParserConverter<V, PT extends ParserToken & Value<V>, PC extends ParserContext> implements Converter {

    static <V, PT extends ParserToken & Value<V>, PC extends ParserContext> ParserConverter<V, PT, PC> with(final Class<V> type, final Parser<PT, PC> parser, final Supplier<PC> context) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(context, "context");

        return new ParserConverter<>(type, parser, context);
    }

    /**
     * Private ctor use factory.
     */
    private ParserConverter(final Class<V> type, final Parser<PT, PC> parser, final Supplier<PC> context) {
        this.type = type;
        this.parser = parser;
        this.context = context;
    }

    @Override
    public boolean canConvert(final Object value, final Class<?> type) {
        return value instanceof String && this.type == type;
    }

    private final Class<V> type;

    @Override
    public <T> T convert(final Object value, final Class<T> type) {
        if (false == value instanceof String) {
            this.failConversion(value, type);
        }
        this.failIfUnsupportedType(value, type);

        final TextCursor cursor = TextCursors.charSequence((String) value);
        final Optional<PT> result = this.parser.parse(cursor, context.get());
        if (!result.isPresent()) {
            this.failConversion(value, type);
        }
        return Cast.to(result.get().value());
    }

    private final Parser<PT, PC> parser;
    private final Supplier<PC> context;

    @Override
    public String toString() {
        return "String->" + this.type.getSimpleName();
    }
}
