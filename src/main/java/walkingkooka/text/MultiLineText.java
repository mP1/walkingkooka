/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.text;

import walkingkooka.Binary;
import walkingkooka.CanBeEmpty;
import walkingkooka.CanBinary;
import walkingkooka.Cast;
import walkingkooka.HasValue;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

/**
 * A wrapper intended to hold the result of a {@link HasMultiLineText}.
 */
public final class MultiLineText implements HasValue<String>,
    CanBinary,
    CanBeEmpty,
    HasText {

    /**
     * Accepts a {@link Collection} of elements, uses the mapper to convert them to a {@link String},
     * and inserts the {@link TextContext#lineEnding()} producing a {@link MultiLineText}.
     */
    public static <T> MultiLineText fromCollection(final Collection<T> collection,
                                                   final Function<T, String> mapper,
                                                   final TextContext context) {
        Objects.requireNonNull(collection, "collection");
        Objects.requireNonNull(mapper, "mapper");
        Objects.requireNonNull(context, "context");

        final StringBuilder b = new StringBuilder();

        final LineEnding lineEnding = context.lineEnding();
        boolean separatorRequired = false;

        for(final T element : collection) {
            if(separatorRequired) {
                b.append(lineEnding);
            }
            b.append(
                mapper.apply(element)
            );

            separatorRequired = true;
        }

        return MultiLineText.EMPTY.setText(
            b.toString()
        );
    }

    /**
     * A {@link MultiLineText} with no text.
     */
    public final static MultiLineText EMPTY = new MultiLineText("");

    // @VisibileForTesting
    MultiLineText(final String text) {
        super();

        this.text = text;
    }

    // HasValue.........................................................................................................

    @Override
    public String value() {
        return this.text;
    }

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.text;
    }

    private final String text;

    public MultiLineText setText(final String text) {
        return this.text.equals(text) ?
            this :
            new MultiLineText(
                Objects.requireNonNull(text, "text")
            );
    }

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return this.value()
            .isEmpty();
    }

    // CanBinary........................................................................................................

    @Override
    public Binary binary(final Charset charset) {
        Objects.requireNonNull(charset, "charset");

        return Binary.with(
            this.text()
                .getBytes(charset)
        );
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.text.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof MultiLineText &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final MultiLineText other) {
        return this.text.equals(other.text);
    }

    @Override
    public String toString() {
        return this.text;
    }
}
