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
public class InvalidTextException extends TextException {

    public InvalidTextException(final String message) {
        this(
            message,
            null
        );
    }

    public InvalidTextException(final String message,
                                final Throwable cause) {
        super(
            CharSequences.failIfNullOrEmpty(message, "message"),
            cause
        );
    }

    @Override
    public String text() {
        return "";
    }

    @Override
    public InvalidTextException setLabel(final Optional<String> label) {
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
        return this == other || other instanceof InvalidTextException && this.equals0((InvalidTextException) other);
    }

    private boolean equals0(final InvalidTextException other) {
        return this.getMessage().equals(other.getMessage()) &&
            this.label.equals(other.label);
    }
}
