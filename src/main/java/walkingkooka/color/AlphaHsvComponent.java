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
 * A {@link HsvComponent} holding the alpha component which is a value between <code>0.0</code> and <code>1.0</code>
 */
final public class AlphaHsvComponent extends AlphaSaturationOrValueHsvComponent {

    /**
     * An opaque alpha component returned by {@link OpaqueHsv#alpha()}.
     */
    final static AlphaHsvComponent OPAQUE = AlphaHsvComponent.with(MAX);
    
    /**
     * Factory that creates a new {@link AlphaHsvComponent}
     */
    static AlphaHsvComponent with(final float value) {
        AlphaHsvComponent.check(value);
        return new AlphaHsvComponent(value);
    }

    /**
     * Private constructor use factory
     */
    private AlphaHsvComponent(final float value) {
        super(value);
    }

    @Override
    public AlphaHsvComponent add(final float value) {
        return 0 == value ? this
                : new AlphaHsvComponent(HsvComponent.add(value, AlphaHsvComponent.MIN, AlphaHsvComponent.MAX));
    }

    @Override
    public AlphaHsvComponent setValue(final float value) {
        AlphaHsvComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link AlphaHsvComponent}
     */
    @Override
    AlphaHsvComponent replace(final float value) {
        return new AlphaHsvComponent(value);
    }

    AlphaColorComponent toAlphaColorComponent() {
        return AlphaColorComponent.with(ColorComponent.toByte(this.value));
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
        return true;
    }

    @Override
    Hsv setComponent(final Hsv hsv) {
        return hsv.setAlpha(this);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AlphaHsvComponent;
    }

    // Serializable
    private static final long serialVersionUID = -1;
}
