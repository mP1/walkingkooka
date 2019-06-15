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

/**
 * A {@link HslComponent} holding the hue component which is a value between 0 and 360 degrees.
 */
final public class HueHslComponent extends HslComponent {

    /**
     * The lowest possible legal value.
     */
    public final static float MIN = 0.0f;

    /**
     * The highest possible legal value.
     */
    public final static float MAX = 360.0f;

    /**
     * Factory that creates a new {@link HueHslComponent}
     */
    static HueHslComponent with(final float value) {
        HueHslComponent.check(value);
        return new HueHslComponent(value);
    }

    /**
     * Verifies that the value is within the acceptable range.
     */
    private static void check(final float value) {
        check(value, MIN, MAX);
    }

    /**
     * Private constructor use factory
     */
    private HueHslComponent(final float value) {
        super(value);
    }

    @Override
    public HueHslComponent add(final float value) {
        return 0 == value ? this : new HueHslComponent(HslComponent.add(value, HueHslComponent.MIN, HueHslComponent.MAX));
    }

    @Override
    public HueHslComponent setValue(final float value) {
        HueHslComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link HueHslComponent}
     */
    @Override
    HueHslComponent replace(final float value) {
        return new HueHslComponent(value);
    }

    @Override
    public boolean isHue() {
        return true;
    }

    @Override
    public boolean isSaturation() {
        return false;
    }

    @Override
    public boolean isLightness() {
        return false;
    }

    @Override
    public boolean isAlpha() {
        return false;
    }

    @Override
    Hsl setComponent(final Hsl hsl) {
        return hsl.setHue(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof HueHslComponent;
    }

    @Override
    public String toString() {
        return String.valueOf(Math.round(this.value));
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
