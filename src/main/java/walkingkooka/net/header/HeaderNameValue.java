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

package walkingkooka.net.header;

import walkingkooka.naming.Name;

/**
 * Abstract base class for all {@link Name} in this package.
 */
abstract class HeaderNameValue implements Name {

    /**
     * Package private constructor
     */
    HeaderNameValue(final String name) {
        super();
        this.name = name;
    }

    @Override
    public final String value() {
        return this.name;
    }

    final String name;

    // Object

    @Override
    public final int hashCode() {
        return this.caseSensitivity().hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0((HeaderNameValue) other);
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final HeaderNameValue other) {
        return this.caseSensitivity().equals(this.name, other.name);
    }

    final int compareTo0(final HeaderNameValue other) {
        return this.caseSensitivity().comparator().compare(this.name, other.name);
    }

    @Override
    abstract public String toString();
}
