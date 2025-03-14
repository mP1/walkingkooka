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

import java.util.Optional;

/**
 * This {@link ToStringBuilderAppender} is used to represent empty {@link java.util.Optional}.
 */
final class ToStringBuilderAppenderOptionalVector<T> extends ToStringBuilderAppenderVector<Optional<T>> {

    static <T> ToStringBuilderAppenderOptionalVector<T> with(final Optional<T> value) {
        return new ToStringBuilderAppenderOptionalVector<>(value);
    }

    private ToStringBuilderAppenderOptionalVector(final Optional<T> value) {
        super(value);
    }

    @Override
    void append0(final ToStringBuilder builder) {
        this.appendLabelBeforeValueAfterPostEmptyCheck(builder);
    }

    @Override
    boolean isEmpty() {
        return false == this.value.isPresent();
    }

    @Override
    void value(final ToStringBuilder builder) {
        final Optional<T> value = this.value;
        if (value.isPresent()) {
            builder.value(
                value.get()
            );
        }
    }
}
