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

/**
 * A {@link HslComponent} holding the hue component which is a value between 0 and 360 degrees.
 */
final public class SaturationHslComponent extends HslComponent {

    /**
     * The lowest possible legal value.
     */
    public final static float MIN = 0.0f;

    /**
     * The highest possible legal value.
     */
    public final static float MAX = 1.0f;

    /**
     * Factory that creates a new {@link SaturationHslComponent}
     */
    static SaturationHslComponent with(final float value) {
        SaturationHslComponent.check(value);
        return new SaturationHslComponent(value);
    }

    /**
     * Verifies that the value is within the acceptable range.
     */
    private static void check(final float value) {
        if ((value < SaturationHslComponent.MIN) || (value > SaturationHslComponent.MAX)) {
            throw new IllegalArgumentException(
                    "value not between " + SaturationHslComponent.MIN + " and " + SaturationHslComponent.MAX + "=" + value);
        }
    }

    /**
     * Private constructor use factory
     */
    private SaturationHslComponent(final float value) {
        super(value);
    }

    @Override
    public SaturationHslComponent add(final float value) {
        return 0 == value ? this
                : new SaturationHslComponent(
                HslComponent.add(value, SaturationHslComponent.MIN, SaturationHslComponent.MAX));
    }

    @Override
    public SaturationHslComponent setValue(final float value) {
        SaturationHslComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link SaturationHslComponent}
     */
    @Override
    SaturationHslComponent replace(final float value) {
        return new SaturationHslComponent(value);
    }

    @Override
    public boolean isHue() {
        return false;
    }

    @Override
    public boolean isSaturation() {
        return true;
    }

    @Override
    public boolean isLightness() {
        return false;
    }

    @Override
    Hsl setComponent(final Hsl hsl) {
        return hsl.setSaturation(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SaturationHslComponent;
    }

    @Override
    public String toString() {
        return toStringPrecentage();
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
