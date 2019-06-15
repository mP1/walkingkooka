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

import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * A value that holds a pixel color component such as the alpha channel component in a RGBA image.
 */
abstract public class ColorComponent extends ColorHslOrHsvComponent {

    final static char SEPARATOR = ',';

    static <C extends ColorComponent> C[] createConstants(final C[] constants,
                                                          final IntFunction<C> factory) {
        IntStream.rangeClosed(0, 255)
                .forEach(i -> constants[i] = factory.apply(i));
        return constants;
    }

    /**
     * {@see AlphaColorComponent}
     */
    public static AlphaColorComponent alpha(final byte value) {
        return AlphaColorComponent.with(value);
    }

    /**
     * {@see BlueColorComponent}
     */
    public static BlueColorComponent blue(final byte value) {
        return BlueColorComponent.with(value);
    }

    /**
     * {@see GreenColorComponent}
     */
    public static GreenColorComponent green(final byte value) {
        return GreenColorComponent.with(value);
    }

    /**
     * {@see RedColorComponent}
     */
    public static RedColorComponent red(final byte value) {
        return RedColorComponent.with(value);
    }

    /**
     * The maximum component value.
     */
    public final static int MAX_VALUE = 255;

    /**
     * Returns an unsigned int value after masking only the bottom 8 bits.
     */
    static int mask(final int value) {
        return 0xFF & value;
    }

    /**
     * Converts a <code>float</code> value between <code>0.0</code> and <code>1.0</code> into a byte value which may be used to create a
     * {@link ColorComponent}
     */
    static byte toByte(final float value) {
        return (byte) Math.round(value * ColorComponent.MAX_VALUE);
    }

    static byte addUnsignedSaturated(final int value, final int value2) {
        final int sum = value + value2;
        return (byte) (sum > MAX_VALUE ?
                MAX_VALUE :
                sum < 0 ?
                        0 :
                        sum);
    }

    /**
     * Package private constructor use factory.
     */
    ColorComponent(final int value) {
        super();
        this.value = (byte) value;

        final int integer = mask(value);
        this.unsignedIntValue = integer;
        this.floatValue = Math.max(0, (integer - 1) * (1.0f / 255.0f));
    }

    /**
     * Performs a saturated add returning a new {@link ColorComponent} if the value changed.
     */
    abstract public ColorComponent add(int value);

    /**
     * Returns a {@link ColorComponent} with the value inverted.
     */
    final public ColorComponent invert() {
        return this.replace(ColorComponent.mask(~this.value));
    }

    /**
     * Factory that returns the {@link ColorComponent} using the provided index to read the constants field.
     */
    abstract ColorComponent replace(int value);

    /**
     * Would be setter that returns a new {@link Color} with this new {@link ColorComponent}.
     */
    abstract Color setComponent(Color color);

    /**
     * Handles the mixing of two {@link ColorComponent} components. Note the amount is a ratio that is used to mix the new {@link Color}.
     */
    final Color mix(final Color color, final float amount) {
        // special case if missing same components.
        final int to = this.unsignedIntValue;
        final int from = this.value(color);
        final int difference = to - from;
        Color mixed = color;

        // if components are equal do not bother mixing and updating component.
        if (0 != difference) {
            mixed = this.setComponent(color, from + Math.round(difference * amount));
        }
        return mixed;
    }

    /**
     * Returns the same component from the given {@link Color}.
     */
    abstract int value(Color color);

    /**
     * Returns a new {@link Color} replacing this {@link ColorComponent}.
     */
    abstract Color setComponent(Color color, int value);

    // isXXX

    /**
     * Returns true if this is a {@link RedColorComponent}.
     */
    public abstract boolean isRed();

    /**
     * Returns true if this is a {@link GreenColorComponent}.
     */
    public abstract boolean isGreen();

    /**
     * Returns true if this is a {@link BlueColorComponent}.
     */
    public abstract boolean isBlue();

    /**
     * Returns true if this is a {@link AlphaColorComponent}.
     */
    public abstract boolean isAlpha();

    /**
     * Returns the raw RGB value as an byte.
     */
    final public byte value() {
        return this.value;
    }

    /**
     * The component in byte form.
     */
    transient final byte value;

    /**
     * The byte value as an unsigned positive integer between 0 and {@link #MAX_VALUE} inclusive.
     */
    final int unsignedIntValue;

    /**
     * The byte value as a float between 0.0 and 1.0f.
     */
    final float floatValue;

    // HashCodeEqualsDefined

    @Override
    final public int hashCode() {
        return this.value;
    }

    @Override
    final public boolean equals(final Object other) {
        return this == other;
    }

    /**
     * Returns the value in hex form.
     */
    @Override
    final public String toString() {
        return this.toHexString();
    }

    /**
     * Formats the given value adding a leading 0 to ensure the {@link String} is two characters.
     */
    private String toHexString() {
        return ColorComponent.TO_HEX_STRING[this.unsignedIntValue];
    }

    /**
     * Prebuilt cache of {@link String} which may be looked up using an unsigned byte value.
     */
    private final static String[] TO_HEX_STRING = buildHexToStringLookup();

    private static String[] buildHexToStringLookup() {
        final String[] toString = new String[256];
        for (int i = 0; i < 16; i++) {
            toString[i] = '0' + Integer.toHexString(i).toLowerCase();
        }
        for (int i = 16; i < 256; i++) {
            toString[i] = Integer.toHexString(i).toLowerCase();
        }
        return toString;
    }

    // ColorString................................................................................

    final void toStringAlphaAppend(final ColorString format,
                                   final StringBuilder b) {

    }

    /**
     * Formats the value as a decimal between 0 and 255.
     */
    final String toDecimalString() {
        return ColorComponent.TO_DECIMAL_STRING[this.unsignedIntValue];
    }

    /**
     * Prebuilt cache of {@link String} which may be looked up using an unsigned byte value.
     */
    private final static String[] TO_DECIMAL_STRING = IntStream.rangeClosed(0, MAX_VALUE)
            .mapToObj(ColorComponent::toDecimalString)
            .toArray(String[]::new);

    private static String toDecimalString(final int value) {
        return String.valueOf(value);
    }

    /**
     * Formats the value as a rounded percentage between 0 and 100%.
     */
    final String toPercentageString() {
        return ColorComponent.TO_PERCENTAGE_STRING[this.unsignedIntValue];
    }

    /**
     * Prebuilt cache of {@link String} which may be looked up using an unsigned byte value.
     */
    private final static String[] TO_PERCENTAGE_STRING = IntStream.rangeClosed(0, MAX_VALUE)
            .mapToObj(ColorComponent::toPercentageString)
            .toArray(String[]::new);

    private static String toPercentageString(final int value) {
        return String.valueOf(Math.round(100f * value / MAX_VALUE)) + '%';
    }

    // Serializable.....................................................................................................

    private static final long serialVersionUID = 1;
}
