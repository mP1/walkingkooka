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
 * A {@link HsvComponent} holding the value component which is a value between <code>0.0</code> and <code>1.0</code>
 */
final public class ValueHsvComponent extends AlphaSaturationOrValueHsvComponent {

    /**
     * Factory that creates a new {@link ValueHsvComponent}
     */
    static ValueHsvComponent with(final float value) {
        ValueHsvComponent.check(value);
        return new ValueHsvComponent(value);
    }

    /**
     * Private constructor use factory
     */
    private ValueHsvComponent(final float value) {
        super(value);
    }

    @Override
    public ValueHsvComponent add(final float value) {
        return 0 == value ? this
                : new ValueHsvComponent(HsvComponent.add(value, ValueHsvComponent.MIN, ValueHsvComponent.MAX));
    }

    @Override
    public ValueHsvComponent setValue(final float value) {
        ValueHsvComponent.check(value);
        return this.value == value ? this : this.replace(value);
    }

    /**
     * Factory that creates a new {@link ValueHsvComponent}
     */
    @Override
    ValueHsvComponent replace(final float value) {
        return new ValueHsvComponent(value);
    }

    @Override
    public boolean isSaturation() {
        return false;
    }

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public boolean isAlpha() {
        return false;
    }

    @Override
    Hsv setComponent(final Hsv hsv) {
        return hsv.setValue(this);
    }

    // Object

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ValueHsvComponent;
    }

    // Serializable
    private static final long serialVersionUID = -1;
}
