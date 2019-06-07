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

package walkingkooka.color;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;

/**
 * Common base for all {@link Hsl} components.
 */
abstract public class HslComponent implements HashCodeEqualsDefined, Serializable {

    /**
     * {@see HueHslComponent}
     */
    public static HueHslComponent hue(final float value) {
        return HueHslComponent.with(value);
    }

    /**
     * {@see SaturationHslComponent}
     */
    public static SaturationHslComponent saturation(final float value) {
        return SaturationHslComponent.with(value);
    }

    /**
     * {@see LightnessHslComponent}
     */
    public static LightnessHslComponent lightness(final float value) {
        return LightnessHslComponent.with(value);
    }

    /**
     * Package private to limit sub classing
     */
    HslComponent(final float value) {
        super();
        this.value = value;
    }

    /**
     * Performs a saturated add returning a new {@link HslComponent} if the value changed.
     */
    abstract public HslComponent add(float value);

    /**
     * Performs a saturated add.
     */
    static float add(final float value, final float min, final float max) {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Getter that returns the value.
     */
    public float value() {
        return this.value;
    }

    /**
     * Would be setter that returns a {@link HslComponent} with the new value.
     */
    abstract public HslComponent setValue(float value);

    /**
     * The <code>float</code> value.
     */
    final float value;

    /**
     * Factory that creates a new {@link HslComponent} of the same type with the given value.
     */
    abstract HslComponent replace(float value);

    /**
     * Returns true if this is a {@link HueHslComponent}.
     */
    public abstract boolean isHue();

    /**
     * Returns true if this is a {@link SaturationHslComponent}.
     */
    public abstract boolean isSaturation();

    /**
     * Returns true if this is a {@link LightnessHslComponent}.
     */
    public abstract boolean isLightness();

    /**
     * Setter used to create a new {@link Hsl} with this component replaced if different
     */
    abstract Hsl setComponent(Hsl hsl);

    // Object

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

    private boolean equals0(final HslComponent other) {
        return this.value == other.value;
    }

    // Serializable

    private static final long serialVersionUID = 1;
}
