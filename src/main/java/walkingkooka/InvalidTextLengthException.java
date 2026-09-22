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

package walkingkooka;

import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;

import java.util.Objects;
import java.util.Optional;

/**
 * An {@link IllegalArgumentException} that reports an {@link String} with an invalid length.
 */
public class InvalidTextLengthException extends TextException
    implements HasShortMessage {

    public static String throwIfFail(final String label,
                                     final String text,
                                     final int min,
                                     final int max) {
        checkParameters(label, text, min, max);

        final int length = text.length();
        if (length < min || length > max) {
            if (0 == length) {
                throw new EmptyTextException(label);
            }
            throw new InvalidTextLengthException(label, text, min, max);
        }
        return text;
    }

    public InvalidTextLengthException(final String label,
                                      final String text,
                                      final int min,
                                      final int max) {
        this(
            label,
            text,
            min,
            max,
            null // cause
        );
    }

    public InvalidTextLengthException(final String label,
                                      final String text,
                                      final int min,
                                      final int max,
                                      final Throwable cause) {
        super(cause);
        checkParameters(label, text, min, max);

        this.setLabel(Optional.of(label));
        this.text = text;
        this.min = min;
        this.max = max;
    }

    private static void checkParameters(final String label,
                                        final String text,
                                        final int min,
                                        final int max) {
        Whitespace.failIfNullOrEmptyOrWhitespace(label, "label");
        Objects.requireNonNull(text, "text");

        if (min < 0) {
            throw new IllegalArgumentException("Invalid min " + min + " < 0");
        }

        if (max < min) {
            throw new IllegalArgumentException("Invalid max " + max + " < " + min);
        }
    }

    @Override
    public String text() {
        return this.text;
    }

    private final String text;

    public int min() {
        return this.min;
    }

    private final int min;

    public int max() {
        return this.max;
    }

    private final int max;

    /**
     * <pre>
     * Length 7 of "label123" not between 2 and 5 = "abc!456"
     * Length 7 of "label123" expected 99 = "abc!456"
     * </pre>
     */
    @Override
    public String getMessage() {
        final StringBuilder b = new StringBuilder();

        this.appendShortMessage(
            b,
            true // includeLabel
        );

        b.append(" = ")
            .append(
                CharSequences.quote(text)
            );

        return b.toString();
    }

    @Override
    public InvalidTextLengthException setLabel(final Optional<String> label) {
        this.label = checkLabel(label);
        return this;
    }

    private static final long serialVersionUID = 1L;

    // HasShortMessage..................................................................................................

    /**
     * Returns a message about the length and expected min/max but without any given label
     * <pre>
     * Length 7 not between 1..2
     * Length 7 expected 5
     * </pre>
     */
    @Override
    public final String getShortMessage() {
        final StringBuilder b = new StringBuilder();

        this.appendShortMessage(
            b,
            false  // includeLabel
        );

        return b.toString();
    }

    private void appendShortMessage(final StringBuilder b,
                                    final boolean includeLabel) {
        b.append("Length ")
            .append(
                text.length()
            );

        if (includeLabel) {
            final String label = this.label()
                .orElse(null);
            if (null != label) {
                b.append(" of ")
                    .append(
                        CharSequences.quoteAndEscape(label)
                    );
            }
        }

        final int min = this.min;
        final int max = this.max;

        if (min != max) {
            b.append(" not between ")
                .append(min)
                .append("..")
                .append(max);
        } else {
            b.append(" expected ")
                .append(min);
        }
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.getMessage());
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof InvalidTextLengthException && this.equals0((InvalidTextLengthException) other);
    }

    private boolean equals0(final InvalidTextLengthException other) {
        return this.label.equals(other.label) &&
            this.text().equals(other.text()) &&
            this.min() == other.min() &&
            this.max() == other.max();
    }
}
