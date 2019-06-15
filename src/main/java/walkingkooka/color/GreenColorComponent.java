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
 * A {@link ColorComponent} that holds a green value.
 */
final public class GreenColorComponent extends ColorComponent {

    /**
     * Factory that creates a {@link ColorComponent}
     */
    static GreenColorComponent with(final byte value) {
        return GreenColorComponent.CONSTANTS[ColorComponent.mask(value)];
    }

    private static final GreenColorComponent[] CONSTANTS = createConstants(new GreenColorComponent[256], GreenColorComponent::new);

    /**
     * Private ctor use factory
     */
    private GreenColorComponent(final int value) {
        super(value);
    }

    @Override
    public GreenColorComponent add(final int value) {
        return GreenColorComponent.with(addUnsignedSaturated(this.unsignedIntValue, value));
    }

    /**
     * Creates a {@link GreenColorComponent}.
     */
    @Override
    GreenColorComponent replace(final int value) {
        return GreenColorComponent.CONSTANTS[value];
    }

    @Override
    Color setComponent(final Color color) {
        return color.setGreen(this);
    }

    @Override
    int value(final Color color) {
        return color.green().unsignedIntValue;
    }

    @Override
    Color setComponent(final Color color, final int value) {
        return color.setGreen(GreenColorComponent.CONSTANTS[ColorComponent.mask(value)]);
    }

    @Override
    public boolean isRed() {
        return false;
    }

    @Override
    public boolean isGreen() {
        return true;
    }

    @Override
    public boolean isBlue() {
        return false;
    }

    @Override
    public boolean isAlpha() {
        return false;
    }

    // Serializable

    private static final long serialVersionUID = 1;

    /**
     * Handles keeping instances singletons.
     */
    private Object readResolve() {
        return GreenColorComponent.CONSTANTS[this.unsignedIntValue];
    }
}
