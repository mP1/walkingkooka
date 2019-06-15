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
import walkingkooka.color.Color;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.tree.json.HasJsonNode;

import java.util.Objects;

/**
 * Base class for numerous properties that hold a {@link Color}.
 */
abstract class ColorTextStylePropertyValue implements HashCodeEqualsDefined,
        HasJsonNode,
        Value<ColorHslOrHsv> {

    static void check(final ColorHslOrHsv color) {
        Objects.requireNonNull(color, "color");
    }

    ColorTextStylePropertyValue(final ColorHslOrHsv color) {
        super();
        this.color = color;
    }

    // Length ...........................................................................................................

    @Override
    public final ColorHslOrHsv value() {
        return this.color;
    }

    final ColorHslOrHsv color;

    // Object ..........................................................................................................

    @Override
    public final int hashCode() {
        return this.color.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(final Object other);

    private boolean equals0(final ColorTextStylePropertyValue other) {
        return this.color.equals(other.color);
    }

    @Override
    public final String toString() {
        return this.color.toString();
    }
}
