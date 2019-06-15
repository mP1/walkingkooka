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
 * A {@link HslComponent} holding the alpha component which is a value between 0.0 and 1.0
 */
final public class AlphaHslComponent extends AlphaLightnessOrSaturationHslComponent {

    /**
     * An opaque alpha component returned by {@link OpaqueHsl#alpha()}.
     */
    final static AlphaHslComponent OPAQUE = AlphaHslComponent.with(MAX);

    /**
     * Factory that creates a new {@link AlphaHslComponent}
     */
    static AlphaHslComponent with(final float value) {
        AlphaHslComponent.check(value);
        return new AlphaHslComponent(value);
    }

    /**
     * Private constructor use factory
     */
    private AlphaHslComponent(final float value) {
        super(value);
    }

    @Override
    public AlphaHslComponent add(final float value) {
        return 0 == value ? this : new AlphaHslComponent(HslComponent.add(value, AlphaHslComponent.MIN, AlphaHslComponent.MAX));
    }

    @Override
    public AlphaHslComponent setValue(final float value) {
        AlphaHslComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link AlphaHslComponent}
     */
    @Override
    AlphaHslComponent replace(final float value) {
        return new AlphaHslComponent(value);
    }

    AlphaColorComponent toAlphaColorComponent() {
        return AlphaColorComponent.with(ColorComponent.toByte(this.value));
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
        return true;
    }

    @Override
    Hsl setComponent(final Hsl hsl) {
        return hsl.setAlpha(this);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AlphaHslComponent;
    }

    // Serializable
    private static final long serialVersionUID = 1;
}
