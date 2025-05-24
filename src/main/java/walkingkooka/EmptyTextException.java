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
 * An {@link IllegalArgumentException} that reports an {@link String} is empty when it should not be.
 */
public class EmptyTextException extends InvalidTextException {

    public EmptyTextException(final String label) {
        this(
            label,
            null
        );
    }

    public EmptyTextException(final String label,
                              final Throwable cause) {
        super(cause);
        this.setLabel(
            Optional.ofNullable(label)
        );
    }

    @Override
    public String text() {
        return "";
    }

    // Empty ABC
    @Override
    public String getMessage() {
        return "Empty " +
            CharSequences.quoteAndEscape(
                this.label()
                    .get()
            );
    }

    @Override
    public EmptyTextException setLabel(final Optional<String> label) {
        this.label = checkNotEmptyLabel(label);
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
        return this == other || other instanceof EmptyTextException && this.equals0((EmptyTextException) other);
    }

    private boolean equals0(final EmptyTextException other) {
        return this.label.equals(other.label);
    }
}
