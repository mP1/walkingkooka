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

package walkingkooka.tree.text;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.json.HasJsonNode;

import java.util.Objects;

/**
 * Base class for numerous properties that hold a {@link Length}.
 */
abstract class LengthTextStylePropertyValue implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<Length<?>> {

    static void check(final Length<?> length) {
        Objects.requireNonNull(length, "length");
    }

    LengthTextStylePropertyValue(final Length<?> length) {
        super();
        this.length = length;
    }

    // Length ...........................................................................................................

    @Override
    public final Length<?> value() {
        return this.length;
    }

    final Length<?> length;

    // Object ..........................................................................................................

    @Override
    public final int hashCode() {
        return this.length.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final LengthTextStylePropertyValue other) {
        return this.length.equals(other.length);
    }

    @Override
    public final String toString() {
        return this.length.toString();
    }
}
