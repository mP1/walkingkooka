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
public class InvalidTextLengthException extends TextException {

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

    // Length 7 of "label123" not between 2 and 5 = "abc!456"
    @Override
    public String getMessage() {
        final StringBuilder b = new StringBuilder();
        b.append("Length ")
            .append(this.text.length())
            .append(' ');

        final String label = this.label()
            .orElse(null);
        if(null != label) {
            b.append("of ")
                .append(
                    CharSequences.quoteAndEscape(label)
                );
        }

        b.append(" not between ")
            .append(this.min)
            .append("..")
            .append(this.max)
            .append(" = ")
            .append(
                CharSequences.quote(this.text)
            );

        return b.toString();
    }

    @Override
    public InvalidTextLengthException setLabel(final Optional<String> label) {
        this.label = checkLabel(label);
        return this;
    }

    private static final long serialVersionUID = 1L;

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
