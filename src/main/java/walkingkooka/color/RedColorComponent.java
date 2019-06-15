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
 * A {@link ColorComponent} that holds a red value.
 */
final public class RedColorComponent extends ColorComponent {

    /**
     * Factory that creates a {@link ColorComponent}
     */
    static RedColorComponent with(final byte value) {
        return RedColorComponent.CONSTANTS[ColorComponent.mask(value)];
    }

    private static final RedColorComponent[] CONSTANTS = createConstants(new RedColorComponent[256], RedColorComponent::new);

    /**
     * Private ctor use factory
     */
    private RedColorComponent(final int value) {
        super(value);
    }

    @Override
    public RedColorComponent add(final int value) {
        return RedColorComponent.with(addUnsignedSaturated(this.unsignedIntValue, value));
    }

    /**
     * Creates a {@link RedColorComponent}.
     */
    @Override
    RedColorComponent replace(final int value) {
        return RedColorComponent.CONSTANTS[value];
    }

    @Override
    Color setComponent(final Color color) {
        return color.setRed(this);
    }

    @Override
    int value(final Color color) {
        return color.red().unsignedIntValue;
    }

    @Override
    Color setComponent(final Color color, final int value) {
        return color.setRed(RedColorComponent.CONSTANTS[ColorComponent.mask(value)]);
    }

    @Override
    public boolean isRed() {
        return true;
    }

    @Override
    public boolean isGreen() {
        return false;
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
        return RedColorComponent.CONSTANTS[this.unsignedIntValue];
    }
}
