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

package walkingkooka.naming;

import walkingkooka.text.CaseSensitivity;

import java.util.Comparator;
import java.util.Objects;

/**
 * A useful {@link Comparator} that provides a {@link Comparator} of {@link Name} with the given {@link CaseSensitivity}.
 */
final class NameComparator<N extends Name> implements Comparator<N> {

    static <N extends Name> NameComparator<N> with(final CaseSensitivity caseSensitivity) {
        Objects.requireNonNull(caseSensitivity, "caseSensitivity");

        return new NameComparator<>(
                caseSensitivity.comparator()
        );
    }

    private NameComparator(final Comparator<CharSequence> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(final N left,
                       final N right) {
        return this.comparator.compare(
                left.value(),
                right.value()
        );
    }

    private final Comparator<CharSequence> comparator;

    @Override
    public String toString() {
        return this.comparator.toString();
    }
}
