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

import java.util.Objects;
import java.util.Optional;

/**
 * An {@link IllegalArgumentException} that reports a message and the label it belongs too. Note the message and label
 * cannot be changed.
 */
public class InvalidTextException extends TextException implements HasShortMessage{

    public InvalidTextException(final String text,
                                final String message) {
        this(
            text,
            message,
            null
        );
    }

    public InvalidTextException(final String text,
                                final String message,
                                final Throwable cause) {
        super(
            message,
            cause
        );

        this.message = CharSequences.failIfNullOrEmpty(message, "message");
        this.text = text;
    }

    @Override
    public InvalidTextException setLabel(final Optional<String> label) {
        this.label = checkLabel(label);
        return this;
    }

    private static final long serialVersionUID = 1L;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.text;
    }

    private final String text;

    // HasShortMessage..................................................................................................

    @Override
    public String getShortMessage() {
        return this.message;
    }

    private final String message;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.text,
            this.message,
            this.label
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof InvalidTextException && this.equals0((InvalidTextException) other);
    }

    private boolean equals0(final InvalidTextException other) {
        return this.message.equals(other.message) &&
            Objects.equals(this.text, other.text) &&
            this.label.equals(other.label);
    }
}
