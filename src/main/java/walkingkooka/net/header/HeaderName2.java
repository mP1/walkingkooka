/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.net.header;

import walkingkooka.Cast;
import walkingkooka.text.CaseSensitivity;

/**
 * Base class for all {@link HeaderName}
 */
abstract class HeaderName2<V> implements HeaderName<V> {

    /**
     * Package private to limit sub classing.
     */
    HeaderName2(final String name) {
        super();
        this.name = name;
    }

    @Override
    public final String value() {
        return this.name;
    }

    private final String name;

    // Comparable...........................................................................................................

    final int compareTo0(final HeaderName2<?> other) {
        return this.caseSensitivity().comparator()
                .compare(this.value(),
                        other.value());
    }

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return this.caseSensitivity().hash(this.value());
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    /**
     * Sub classes should do an instanceof this type test.
     */
    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final HeaderName2<?> other) {
        return this.compareTo0(other) == 0;
    }

    /**
     * Used during hashing and equality checks.
     */
    private CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public final String toString() {
        return this.value();
    }
}
