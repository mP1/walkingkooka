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
 * A {@link Color} that includes an alpha property.
 */
final class AlphaHsl extends Hsl {

    static AlphaHsl withAlpha(final HueHslComponent hue,
                              final SaturationHslComponent saturation,
                              final LightnessHslComponent lightness,
                              final AlphaHslComponent alpha) {
        return new AlphaHsl(hue, saturation, lightness, alpha);
    }

    private AlphaHsl(final HueHslComponent hue,
                     final SaturationHslComponent saturation,
                     final LightnessHslComponent lightness,
                     final AlphaHslComponent alpha) {
        super(hue, saturation, lightness);

        this.alpha = alpha;
    }

    @Override
    public AlphaHslComponent alpha() {
        return this.alpha;
    }

    private final AlphaHslComponent alpha;

    @Override
    Hsl replace(final HueHslComponent hue,
                final SaturationHslComponent saturation,
                final LightnessHslComponent lightness) {
        return new AlphaHsl(hue, saturation, lightness, this.alpha);
    }

    @Override
    Color color(final Color color) {
        return color.set(this.alpha.toAlphaColorComponent());
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AlphaHsl;
    }

    @Override
    boolean equals2(final Hsl other) {
        return this.alpha.equals(other.alpha());
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    String functionName() {
        return "hsla(";
    }

    @Override
    void buildToStringAlpha(final ToStringBuilder builder) {
        builder.value(this.alpha);
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
