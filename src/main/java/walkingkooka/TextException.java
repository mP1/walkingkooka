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
import walkingkooka.text.HasText;

import java.util.Optional;

/**
 * An {@link IllegalArgumentException} that reports something was wrong with some given text.
 */
public abstract class TextException extends IllegalArgumentException
    implements HasText {

    TextException() {
        this(null);
    }

    TextException(final Throwable cause) {
        super(cause);
        this.label = NO_LABEL;
    }

    public final static Optional<String> NO_LABEL = Optional.empty();

    public final Optional<String> label() {
        return this.label;
    }

    static Optional<String> checkLabel(final Optional<String> label) {
        if(label.isPresent()) {
            checkNotEmptyLabel(label);
        }

        return label;
    }

    static Optional<String> checkNotEmptyLabel(final Optional<String> label) {
        CharSequences.failIfNullOrEmpty(
            label.orElse(null),
            "label"
        );
        return label;
    }

    public abstract TextException setLabel(final Optional<String> label);

    Optional<String> label;

    private static final long serialVersionUID = 1L;
}
