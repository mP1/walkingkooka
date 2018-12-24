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

package walkingkooka.text;

import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;
import java.util.Objects;

/**
 * A {@link CharSequence} that represents a line of text and cannot contain any line ending.
 */
final class Line implements Value<String>, HashCodeEqualsDefined, Serializable, CharSequence {
    /**
     * Factory that creates a new {@link Line} providing the {@link String} has no {@link
     * LineEnding} characters within it.
     */
    static Line with(final String value) {
        Objects.requireNonNull(value, "value");
        LineEnding.CR.complainIfPresent(value);
        LineEnding.NL.complainIfPresent(value);

        return new Line(value);
    }

    /**
     * Private ctor within it.
     */
    private Line(final String value) {
        super();
        this.value = value;
    }

    // CharSequence

    @Override
    public char charAt(final int at) {
        return this.value.charAt(at);
    }

    @Override
    public int length() {
        return this.value.length();
    }

    @Override
    public CharSequence subSequence(final int from, final int end) {
        final String value = this.value;
        return (0 == from) && (end == value.length()) ?
                this :
                new Line(value.subSequence(from, end).toString());
    }

    // Value

    @Override
    public String value() {
        return this.value;
    }

    // HashCodeEqualsDefined

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof Line) && this.equals((Line) other));
    }

    private boolean equals(final Line other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    private final String value;

    // Serializable
    private static final long serialVersionUID = 8704301322402248315L;
}
