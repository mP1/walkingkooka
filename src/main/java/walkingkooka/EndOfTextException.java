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
 * An {@link IllegalArgumentException} that reports the end of text was encountered during processing.
 */
public class EndOfTextException extends InvalidTextException {

    private static final long serialVersionUID = 1L;

    public EndOfTextException(final String message) {
        this(message,  null);
    }

    public EndOfTextException(final String message,
                              final Throwable cause) {
        super(cause);
        this.message = CharSequences.failIfNullOrEmpty(message, "message");
    }

    @Override
    public String text() {
        return "";
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    private final String message;

    @Override
    public EndOfTextException setLabel(final Optional<String> label) {
        this.label = checkLabel(label);
        return this;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.getMessage());
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof EndOfTextException && this.equals0((EndOfTextException) other);
    }

    private boolean equals0(final EndOfTextException other) {
        return this.message.equals(other.message);
    }
}
