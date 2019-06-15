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
 * A {@link HsvComponent} holding the saturation component which is a value between 0 and 360 degrees.
 */
final public class SaturationHsvComponent extends AlphaSaturationOrValueHsvComponent {

    /**
     * Factory that creates a new {@link SaturationHsvComponent}
     */
    static SaturationHsvComponent with(final float value) {
        SaturationHsvComponent.check(value);
        return new SaturationHsvComponent(value);
    }

    /**
     * Private constructor use factory
     */
    private SaturationHsvComponent(final float value) {
        super(value);
    }

    @Override
    public SaturationHsvComponent add(final float value) {
        return 0 == value ? this
                : new SaturationHsvComponent(
                HsvComponent.add(value, SaturationHsvComponent.MIN, SaturationHsvComponent.MAX));
    }

    @Override
    public SaturationHsvComponent setValue(final float value) {
        SaturationHsvComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link SaturationHsvComponent}
     */
    @Override
    SaturationHsvComponent replace(final float value) {
        return new SaturationHsvComponent(value);
    }

    @Override
    public boolean isSaturation() {
        return true;
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
        return hsv.setSaturation(this);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof SaturationHsvComponent;
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
