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

import java.util.Objects;
import java.util.Optional;

/**
 * An {@link Color} that is opaque with a constant alpha component.
 */
final class OpaqueColor extends Color {

    /**
     * Creates a new {@link Color} with the provided components.
     */
    static OpaqueColor createOpaqueColor(final RedColorComponent red,
                                         final GreenColorComponent green,
                                         final BlueColorComponent blue) {
        Objects.requireNonNull(red, "red");
        Objects.requireNonNull(green, "green");
        Objects.requireNonNull(blue, "blue");

        return OpaqueColor.computeRgbAndCreate(red, green, blue);
    }

    /**
     * Computes the RGB from the given {@link ColorComponent components}.
     */
    static OpaqueColor computeRgbAndCreate(final RedColorComponent red, final GreenColorComponent green,
                                           final BlueColorComponent blue) {
        return OpaqueColor.with(red, green, blue, //
                (red.unsignedIntValue << Color.RED_SHIFT) | // red
                        (green.unsignedIntValue << Color.GREEN_SHIFT) | // green
                        (blue.unsignedIntValue << Color.BLUE_SHIFT)); // blue
    }

    /**
     * Factory that creates with out any parameter checking.
     */
    static OpaqueColor with(final RedColorComponent red,
                            final GreenColorComponent green,
                            final BlueColorComponent blue,
                            final int rgb) {
        return new OpaqueColor(red, green, blue, rgb);
    }

    /**
     * Private constructor use factory.
     */
    private OpaqueColor(final RedColorComponent red,
                        final GreenColorComponent green,
                        final BlueColorComponent blue,
                        final int rgb) {
        super(red, green, blue);
        this.rgb = rgb;
        this.argb = OpaqueColor.ALPHA | rgb;
    }

    /**
     * Always returns false.
     */
    @Override
    public boolean hasAlpha() {
        return false;
    }

    /**
     * Always returns an opaque alpha.
     */
    @Override
    public AlphaColorComponent alpha() {
        return AlphaColorComponent.OPAQUE;
    }

    /**
     * Factory called by the various setters that creates a new {@link OpaqueColor} with the given components.
     */
    @Override
    Color replace(final RedColorComponent red,
                  final GreenColorComponent green,
                  final BlueColorComponent blue) {
        return OpaqueColor.computeRgbAndCreate(red, green, blue);
    }

    /**
     * Returns an integer with the RGB value.
     */
    @Override
    final public int rgb() {
        return this.rgb;
    }

    private final int rgb;

    /**
     * Returns an integer holding the ARGB value.
     */
    @Override
    public int argb() {
        return this.argb;
    }

    private final int argb;

    /**
     * A pre-computed constant holding the constant alpha component used by {@link #argb()}.
     */
    private final static int ALPHA = AlphaColorComponent.OPAQUE.value << 24;

    /**
     * Returns the RGB
     */
    @Override
    public int value() {
        return this.rgb();
    }

    @Override
    public java.awt.Color toAwtColor() {
        return new java.awt.Color(this.rgb());
    }

    // WebColorName....................................................................................................

    /**
     * Returns a {@link WebColorName} for this color if one exists.
     */
    @Override
    public Optional<WebColorName> webColorName() {
        return Optional.ofNullable(WebColorName.RRGGBB_CONSTANTS.get(this.value()));
    }

    // Object..........................................................................................................

    /**
     * Can only be equal to {@link Color} without an alpha.
     */
    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof OpaqueColor;
    }

    // UsesToStringBuilder

    @Override
    void buildColorComponentsToString(final ToStringBuilder builder) {
        this.addRedGreenBlueComponents(builder);
    }

    // ColorString................................................................................

    /**
     * Always rgb
     */
    @Override
    String rgbFunctionName() {
        return "rgb";
    }

    @Override
    void alphaComponentToString( final StringBuilder b,
                                 final ColorString format) {
        // no alpha component.
    }

    // Serializable.....................................................................................................

    private static final long serialVersionUID = 1;
}
