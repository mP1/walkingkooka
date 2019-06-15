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

/**
 * A {@link Hsl} that includes an alpha property.
 */
final class OpaqueHsl extends Hsl {

    static OpaqueHsl withOpaque(final HueHslComponent hue,
                                final SaturationHslComponent saturation,
                                final LightnessHslComponent lightness) {
        return new OpaqueHsl(hue, saturation, lightness);
    }

    private OpaqueHsl(final HueHslComponent hue,
                      final SaturationHslComponent saturation,
                      final LightnessHslComponent lightness) {
        super(hue, saturation, lightness);
    }

    @Override
    public AlphaHslComponent alpha() {
        return AlphaHslComponent.OPAQUE;
    }

    /**
     * Factory that creates a {@link Hsl} with the given {@link HslComponent components}.
     */
    @Override
    Hsl replace(final HueHslComponent hue,
                final SaturationHslComponent saturation,
                final LightnessHslComponent lightness) {
        return withOpaque(hue, saturation, lightness);
    }

    @Override
    Color color(final Color color) {
        return color;
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof OpaqueHsl;
    }

    @Override
    boolean equals2(final Hsl other) {
        return true; // no alpha component to compare.
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    String functionName() {
        return "hsl(";
    }

    @Override
    void buildToStringAlpha(final ToStringBuilder builder) {
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
