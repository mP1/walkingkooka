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
 * A {@link ColorComponent} that holds an alpha value.
 */
final public class AlphaColorComponent extends ColorComponent {

    /**
     * Factory that creates a {@link ColorComponent}
     */
    static AlphaColorComponent with(final byte value) {
        return AlphaColorComponent.CONSTANTS[ColorComponent.mask(value)];
    }

    private static final AlphaColorComponent[] CONSTANTS = createConstants(new AlphaColorComponent[256], AlphaColorComponent::new);

    final static AlphaColorComponent OPAQUE = AlphaColorComponent.CONSTANTS[255];

    /**
     * Private ctor use factory
     */
    private AlphaColorComponent(final int value) {
        super(value);
    }

    @Override
    public AlphaColorComponent add(final int value) {
        return AlphaColorComponent.with(addUnsignedSaturated(this.unsignedIntValue, value));
    }

    /**
     * Creates a {@link AlphaColorComponent}.
     */
    @Override
    AlphaColorComponent replace(final int value) {
        return AlphaColorComponent.CONSTANTS[value];
    }

    @Override
    Color setComponent(final Color color) {
        return color.setAlpha(this);
    }

    @Override
    int value(final Color color) {
        return color.alpha().unsignedIntValue;
    }

    @Override
    Color setComponent(final Color color, final int value) {
        return color.setAlpha(AlphaColorComponent.CONSTANTS[value]);
    }

    @Override
    public boolean isRed() {
        return false;
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
        return true;
    }

    // Serializable

    private static final long serialVersionUID = 1;

    /**
     * Handles keeping instances singletons.
     */
    private Object readResolve() {
        return AlphaColorComponent.CONSTANTS[this.unsignedIntValue];
    }
}
