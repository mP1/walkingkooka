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

package walkingkooka.color;

import walkingkooka.Cast;

abstract class HslOrHsvComponent extends ColorHslOrHsvComponent {

    /**
     * Verifies that the value is within the acceptable range.
     */
    static void check(final float value,
                      final float min,
                      final float max) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    "value not between " + min + " and " + max + "=" + value);
        }
    }

    HslOrHsvComponent(final float value) {
        super();
        this.value = value;
    }

    public final float value() {
        return this.value;
    }

    abstract HslOrHsvComponent setValue(final float value);

    final float value;

    /**
     * Performs a saturated add.
     */
    static float add(final float value, final float min, final float max) {
        return value < min ? min : value > max ? max : value;
    }

    // Object..........................................................................................................

    @Override
    final public int hashCode() {
        return Float.hashCode(this.value);
    }

    @Override
    final public boolean equals(final Object other) {
        return this == other ||
                this.canBeEqual(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(Object other);

    private boolean equals0(final HslOrHsvComponent other) {
        return this.value == other.value;
    }

    final String toStringPercentage() {
        return Math.round(100 * this.value) + "%";
    }

    // Serializable......................................................................................................

    private static final long serialVersionUID = 1;
}
