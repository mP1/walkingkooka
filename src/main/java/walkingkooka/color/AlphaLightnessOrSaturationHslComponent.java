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
abstract class AlphaLightnessOrSaturationHslComponent extends HslComponent {

    /**
     * The lowest possible legal value.
     */
    public final static float MIN = 0.0f;

    /**
     * The highest possible legal value.
     */
    public final static float MAX = 360.0f;

    /**
     * Verifies that the value is within the acceptable range.
     */
    static void check(final float value) {
        if ((value < AlphaLightnessOrSaturationHslComponent.MIN) || (value > AlphaLightnessOrSaturationHslComponent.MAX)) {
            throw new IllegalArgumentException(
                    "value not between " + AlphaLightnessOrSaturationHslComponent.MIN + " and " + AlphaLightnessOrSaturationHslComponent.MAX + "=" + value);
        }
    }

    /**
     * Package private to limit sub classing.
     */
    AlphaLightnessOrSaturationHslComponent(final float value) {
        super(value);
    }

    @Override
    public final boolean isHue() {
        return false;
    }

    @Override
    public final String toString() {
        return Math.round(100 * this.value) + "%";
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
