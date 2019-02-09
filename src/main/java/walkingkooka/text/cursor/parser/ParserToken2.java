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
import walkingkooka.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a result of a parser attempt to consume a {@link walkingkooka.text.cursor.TextCursor}
 */
abstract class ParserToken2<V> implements ParserToken, Value<V> {

    /**
     * Private ctor to limit subclassing.
     */
    ParserToken2(final V value, final String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public final V value() {
        return this.value;
    }

    private final V value;

    /**
     * The text matched by the {@link Parser}.
     */
    public final String text() {
        return this.text;
    }

    /**
     * Should be called by sub classes of {@link #setText(String)} with a return type of itself.
     */
    final ParserToken setText0(final String text) {
        Objects.requireNonNull(text, "text");
        return this.text().equals(text) ?
                this :
                this.replaceText(text);
    }

    private final String text;

    /**
     * Sub classes must create a new instance with the new text and same value.
     */
    abstract ParserToken replaceText(final String text);

    public final <T extends ParserToken2<V>> Optional<T> success() {
        return Cast.to(Optional.of(this));
    }

    // Object

    @Override
    public final int hashCode() {
        return this.text.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other || this.canBeEqual(other) && this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final ParserToken2<?> other) {
        return this.value.equals(other.value) && this.text.equals(other.text) && this.equals1(other);
    }

    abstract boolean equals1(final ParserToken2<?> other);

    @Override
    public final String toString() {
        return this.text();
    }
}
