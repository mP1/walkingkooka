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

package walkingkooka.net;


import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;
import java.util.Objects;

/**
 * The fragment within a {@link Url}.
 */
public final class UrlFragment implements Value<String>, HashCodeEqualsDefined, Serializable {

    /**
     * An empty or absent fragment.
     */
    public final static UrlFragment EMPTY = new UrlFragment("");

    /**
     * Factory that creates a {@link UrlFragment}.
     */
    public static UrlFragment with(final String value) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(value, "value");
        return value.isEmpty() ?
                EMPTY :
                new UrlFragment(value);
    }

    /**
     * Private constructor use factory
     */
    private UrlFragment(final String value) {
        super();
        this.value = value;
    }

    /**
     * Returns the fragment in its original form.
     */
    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    // Object

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) ||
                other instanceof UrlFragment && this.equals0((UrlFragment) other);
    }

    private boolean equals0(final UrlFragment other) {
        return this.value.equals(other.value);
    }

    @Override
    public final String toString() {
        return this.value;
    }

    final void toString0(final StringBuilder b) {
        if (!this.value.isEmpty()) {
            b.append(Url.FRAGMENT_START.character());
            b.append(this.value);
        }
    }

    // Serializable

    private Object readResolve() {
        return this.value.length() == 0 ? UrlFragment.EMPTY : this;
    }

    private static final long serialVersionUID = 1;
}
