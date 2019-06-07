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
 * Common base for all {@link Hsv} components.
 */
abstract public class HsvComponent implements HashCodeEqualsDefined, Serializable {

    /**
     * {@see HueHsvComponent}
     */
    public static HueHsvComponent hue(final float value) {
        return HueHsvComponent.with(value);
    }

    /**
     * {@see SaturationHsvComponent}
     */
    public static SaturationHsvComponent saturation(final float value) {
        return SaturationHsvComponent.with(value);
    }

    /**
     * {@see ValueHsvComponent}
     */
    public static ValueHsvComponent value(final float value) {
        return ValueHsvComponent.with(value);
    }

    /**
     * Package private to limit sub classing
     */
    HsvComponent(final float value) {
        super();
        this.value = value;
    }

    /**
     * Performs a saturated add returning a new {@link HsvComponent} if the value changed.
     */
    abstract public HsvComponent add(float value);

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
     * Would be setter that returns a {@link HsvComponent} with the new value.
     */
    abstract public HsvComponent setValue(float value);

    /**
     * The <code>float</code> value.
     */
    final float value;

    /**
     * Factory that creates a new {@link HsvComponent} of the same type with the given value.
     */
    abstract HsvComponent replace(float value);

    /**
     * Returns true if this is a {@link HueHsvComponent}
     */
    public abstract boolean isHue();

    /**
     * Returns true if this is a {@link SaturationHsvComponent}
     */
    public abstract boolean isSaturation();

    /**
     * Returns true if this is a {@link ValueHsvComponent}
     */
    public abstract boolean isValue();

    /**
     * Setter used to create a new {@link Hsv} with this component replaced if different
     */
    abstract Hsv setComponent(Hsv hsv);

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

    private boolean equals0(final HsvComponent other) {
        return this.value == other.value;
    }

    // Serializable

    private static final long serialVersionUID = 1;
}
