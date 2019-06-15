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

import walkingkooka.ToStringBuilder;

import java.util.Optional;

/**
 * A {@link Color} that includes an alpha property.
 */
final class AlphaColor extends Color {

    /**
     * Factory that creates a {@link AlphaColor} with the given argb value.
     */
    static AlphaColor createAlphaColorFromArgb(final int argb) {
        return new AlphaColor(argb);
    }

    /**
     * Private constructor use factory.
     */
    private AlphaColor(final int argb) {
        super(RedColorComponent.with(Color.shiftRight(argb, Color.RED_SHIFT)), //
                GreenColorComponent.with(Color.shiftRight(argb, Color.GREEN_SHIFT)), //
                BlueColorComponent.with(Color.shiftRight(argb, Color.BLUE_SHIFT)));
        this.alpha = AlphaColorComponent.with(Color.shiftRight(argb, Color.ALPHA_SHIFT));
        this.argb = argb;
    }

    /**
     * Factory that creates a {@link AlphaColor}
     */
    static AlphaColor with(final RedColorComponent red,
                           final GreenColorComponent green,
                           final BlueColorComponent blue,
                           final AlphaColorComponent alpha) {
        return new AlphaColor(red, green, blue, alpha);
    }

    /**
     * Private constructor use factory. Note the argb is calculated and cached.
     */
    private AlphaColor(final RedColorComponent red,
                       final GreenColorComponent green,
                       final BlueColorComponent blue,
                       final AlphaColorComponent alpha) {
        super(red, green, blue);
        this.alpha = alpha;
        this.argb = (alpha.unsignedIntValue << Color.ALPHA_SHIFT) | //
                (red.unsignedIntValue << Color.RED_SHIFT) | //
                (green.unsignedIntValue << Color.GREEN_SHIFT) | //
                (blue.unsignedIntValue << Color.BLUE_SHIFT);
    }

    /**
     * Always returns true.
     */
    @Override
    public boolean hasAlpha() {
        return true;
    }

    /**
     * Unconditionally creates a new a {@link AlphaColor}
     */
    @Override
    Color replace(final RedColorComponent red,
                  final GreenColorComponent green,
                  final BlueColorComponent blue) {
        return new AlphaColor(red, green, blue, this.alpha);
    }

    /**
     * Factory that creates a new {@link Color} with the new {@link AlphaColorComponent}.
     */
    @Override
    Color setAlpha(final AlphaColorComponent alpha) {
        return this.alpha.equals(alpha) ? this//
                : alpha == AlphaColorComponent.OPAQUE ? //
                OpaqueColor.computeRgbAndCreate(this.red, this.green, this.blue) : //
                new AlphaColor(this.red, this.green, this.blue, alpha);
    }

    /**
     * Returns the actual alpha component
     */
    @Override
    public AlphaColorComponent alpha() {
        return this.alpha;
    }

    private final AlphaColorComponent alpha;

    /**
     * Returns the ARGB less the alpha component.
     */
    @Override
    public int rgb() {
        return AlphaColor.MASK & this.argb;
    }

    private final static int MASK = 0x00FFFFFF;

    /**
     * Returns all the components into a single int as ARGB
     */
    @Override
    public int argb() {
        return this.argb;
    }

    private final int argb;

    /**
     * Returns the ARGB
     */
    @Override
    public int value() {
        return this.argb();
    }

    @Override
    public java.awt.Color toAwtColor() {
        return new java.awt.Color(this.red.unsignedIntValue,
                this.green.unsignedIntValue,
                this.blue.unsignedIntValue,
                this.alpha.unsignedIntValue);
    }

    // WebColorName....................................................................................................

    /**
     * Always returns nothing.
     */
    @Override
    public Optional<WebColorName> webColorName(){
        return Optional.empty();
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AlphaColor;
    }

    // UsesToStringBuilder

    @Override
    void buildColorComponentsToString(final ToStringBuilder builder) {
        this.addRedGreenBlueComponents(builder);
        builder.value(this.alpha);
    }

    // ColorString................................................................................

    /**
     * Always rgba
     */
    @Override
    String rgbFunctionName() {
        return "rgba";
    }

    @Override
    void alphaComponentToString(final StringBuilder b,
                                final ColorString format) {
        b.append(ColorComponent.SEPARATOR);
        b.append(format.componentToString(this.alpha));
    }

    // Serializable.....................................................................................................

    private static final long serialVersionUID = 1L;
}
