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
 * Common base for all {@link Hsv} components.
 */
abstract public class HsvComponent extends HslOrHsvComponent {

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
     * {@see AlphaHsvComponent}
     */
    public static AlphaHsvComponent alpha(final float value) {
        return AlphaHsvComponent.with(value);
    }

    /**
     * Package private to limit sub classing
     */
    HsvComponent(final float value) {
        super(value);
    }

    /**
     * Performs a saturated add returning a new {@link HsvComponent} if the value changed.
     */
    abstract public HsvComponent add(float value);

    /**
     * Would be setter that returns a {@link HsvComponent} with the new value.
     */
    abstract public HsvComponent setValue(float value);

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
     * Returns true if this is a {@link AlphaHsvComponent}
     */
    public abstract boolean isAlpha();

    /**
     * Setter used to create a new {@link Hsv} with this component replaced if different
     */
    abstract Hsv setComponent(final Hsv hsv);

    // Object...........................................................................................................

    abstract boolean canBeEqual(Object other);

    // Serializable....................................................................................................

    private static final long serialVersionUID = 1;
}
