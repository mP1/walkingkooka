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
 * A {@link HslComponent} holding the hue component which is a value between <code>0</code> and <code>1.0</code>.
 */
final public class LightnessHslComponent extends HslComponent {
    /**
     * The lowest possible legal value.
     */
    public final static float MIN = 0.0f;

    /**
     * The highest possible legal value.
     */
    public final static float MAX = 1.0f;

    /**
     * Factory that creates a new {@link LightnessHslComponent}
     */
    static LightnessHslComponent with(final float value) {
        LightnessHslComponent.check(value);
        return new LightnessHslComponent(value);
    }

    /**
     * Verifies that the value is within the acceptable range.
     */
    private static void check(final float value) {
        if ((value < LightnessHslComponent.MIN) || (value > LightnessHslComponent.MAX)) {
            throw new IllegalArgumentException(
                    "value not between " + LightnessHslComponent.MIN + " and " + LightnessHslComponent.MAX + "=" + value);
        }
    }

    /**
     * Private constructor use factory
     */
    private LightnessHslComponent(final float value) {
        super(value);
    }

    @Override
    public LightnessHslComponent add(final float value) {
        return 0 == value ? this
                : new LightnessHslComponent(HslComponent.add(value, LightnessHslComponent.MIN, LightnessHslComponent.MAX));
    }

    @Override
    public LightnessHslComponent setValue(final float value) {
        LightnessHslComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link LightnessHslComponent}
     */
    @Override
    LightnessHslComponent replace(final float value) {
        return new LightnessHslComponent(value);
    }

    @Override
    public boolean isHue() {
        return false;
    }

    @Override
    public boolean isSaturation() {
        return false;
    }

    @Override
    public boolean isLightness() {
        return true;
    }

    @Override
    Hsl setComponent(final Hsl hsl) {
        return hsl.setValue(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof LightnessHslComponent;
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
