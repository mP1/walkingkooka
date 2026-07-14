
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

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * An immutable {@link BinaryTextPrinting}.
 */
public final class BinaryTextPrinting implements BinaryTextContext,
    TextContextDelegator,
    UsesToStringBuilder {

    public static BinaryTextPrinting with(final Charset charset,
                                          final TextPrinting textPrinting) {
        return new BinaryTextPrinting(
            Objects.requireNonNull(charset, "charset"),
            Objects.requireNonNull(textPrinting, "textPrinting")
        );
    }

    private BinaryTextPrinting(final Charset charset,
                               final TextPrinting textPrinting) {
        super();

        this.charset = charset;
        this.textPrinting = textPrinting;
    }

    // HasCharset.......................................................................................................

    @Override
    public Charset charset() {
        return this.charset;
    }

    public BinaryTextPrinting setCharset(final Charset charset) {
        return this.charset.equals(charset) ?
            this :
            new BinaryTextPrinting(
                Objects.requireNonNull(charset, "charset"),
                this.textPrinting
            );
    }

    private final Charset charset;

    // TextContextDelegator.............................................................................................

    public BinaryTextPrinting setLineEnding(final LineEnding lineEnding) {
        final TextPrinting before = this.textPrinting;
        final TextPrinting after = before.setLineEnding(lineEnding);

        return before.equals(after) ?
            this :
            new BinaryTextPrinting(
                this.charset,
                after
            );
    }

    public BinaryTextPrinting setIndentation(final Indentation indentation) {
        final TextPrinting before = this.textPrinting;
        final TextPrinting after = before.setIndentation(indentation);

        return before.equals(after) ?
            this :
            new BinaryTextPrinting(
                this.charset,
                after
            );
    }

    @Override
    public TextPrinting textContext() {
        return this.textPrinting;
    }

    private final TextPrinting textPrinting;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.charset,
            this.textPrinting
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof BinaryTextPrinting &&
                this.equals0((BinaryTextPrinting) other));
    }

    private boolean equals0(final BinaryTextPrinting other) {
        return this.charset.equals(other.charset) &&
            this.textPrinting.equals(other.textPrinting);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.label("charset");
        b.value(
            this.charset.toString()
        );

        this.textPrinting.buildToString(b);
    }
}
