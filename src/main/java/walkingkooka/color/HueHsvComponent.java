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
 * A {@link HsvComponent} holding the hue component which is a value between 0 and 360 degrees.
 */
final public class HueHsvComponent extends HsvComponent {

    /**
     * The lowest possible legal value.
     */
    public final static float MIN = 0.0f;

    /**
     * The highest possible legal value.
     */
    public final static float MAX = 360.0f;

    /**
     * Factory that creates a new {@link HueHsvComponent}
     */
    static HueHsvComponent with(final float value) {
        HueHsvComponent.check(value);
        return new HueHsvComponent(value);
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
    private HueHsvComponent(final float value) {
        super(value);
    }

    @Override
    public HueHsvComponent add(final float value) {
        return 0 == value ? this : new HueHsvComponent(HsvComponent.add(value, HueHsvComponent.MIN, HueHsvComponent.MAX));
    }

    @Override
    public HueHsvComponent setValue(final float value) {
        HueHsvComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link HueHsvComponent}
     */
    @Override
    HueHsvComponent replace(final float value) {
        return new HueHsvComponent(value);
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
    public boolean isValue() {
        return false;
    }

    @Override
    public boolean isAlpha() {
        return false;
    }

    @Override
    Hsv setComponent(final Hsv hsv) {
        return hsv.setHue(this);
    }

    @Override
    protected boolean canBeEqual(final Object other) {
        return other instanceof HueHsvComponent;
    }

    @Override
    public String toString() {
        return String.valueOf(Math.round(this.value));
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
