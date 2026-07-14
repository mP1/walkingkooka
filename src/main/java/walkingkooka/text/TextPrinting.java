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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;

import java.util.Objects;

/**
 * An immutable {@link TextPrinting}.
 */
public final class TextPrinting implements TextContext,
    UsesToStringBuilder {

    public static TextPrinting with(final Indentation indentation,
                                    final LineEnding lineEnding) {
        return new TextPrinting(
            Objects.requireNonNull(indentation, "indentation"),
            Objects.requireNonNull(lineEnding, "lineEnding")
        );
    }

    private TextPrinting(final Indentation indentation,
                         final LineEnding lineEnding) {
        super();

        this.indentation = indentation;
        this.lineEnding = lineEnding;
    }

    // HasIndentation...................................................................................................

    @Override
    public Indentation indentation() {
        return this.indentation;
    }

    public TextPrinting setIndentation(final Indentation indentation) {
        return this.indentation.equals(indentation) ?
            this :
            new TextPrinting(
                Objects.requireNonNull(indentation, "indentation"),
                this.lineEnding
            );
    }

    private final Indentation indentation;

    // HasLineEnding....................................................................................................

    @Override
    public LineEnding lineEnding() {
        return this.lineEnding;
    }

    public TextPrinting setLineEnding(final LineEnding lineEnding) {
        return this.lineEnding.equals(lineEnding) ?
            this :
            new TextPrinting(
                this.indentation,
                Objects.requireNonNull(lineEnding, "lineEnding")
            );
    }

    private final LineEnding lineEnding;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.indentation,
            this.lineEnding
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof TextPrinting &&
                this.equals0((TextPrinting) other));
    }

    private boolean equals0(final TextPrinting other) {
        return this.indentation.equals(other.indentation) &&
            this.lineEnding.equals(other.lineEnding);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.label("indentation");
        b.value(
            CharSequences.escape(this.indentation)
        );

        b.label("lineEnding");
        b.value(
            CharSequences.escape(this.lineEnding)
        );
    }
}
