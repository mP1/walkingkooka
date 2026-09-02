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
import walkingkooka.CanBinary;
import walkingkooka.Cast;
import walkingkooka.HasValue;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * A wrapper intended to hold the result of a {@link HasMultiLineText}.
 */
public final class MultiLineText implements HasValue<String>,
    CanBinary,
    HasText {

    public static MultiLineText with(final String text) {
        return new MultiLineText(
            Objects.requireNonNull(text, "text")
        );
    }

    private MultiLineText(final String text) {
        super();

        this.text = text;
    }

    // HasValue.........................................................................................................

    @Override
    public String value() {
        return this.text;
    }

    private final String text;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.value();
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
