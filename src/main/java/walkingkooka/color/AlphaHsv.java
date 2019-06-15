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
final class AlphaHsv extends Hsv {

    static AlphaHsv withAlpha(final HueHsvComponent hue,
                              final SaturationHsvComponent saturation,
                              final ValueHsvComponent value,
                              final AlphaHsvComponent alpha) {
        return new AlphaHsv(hue, saturation, value, alpha);
    }

    private AlphaHsv(final HueHsvComponent hue,
                     final SaturationHsvComponent saturation,
                     final ValueHsvComponent value,
                     final AlphaHsvComponent alpha) {
        super(hue, saturation, value);

        this.alpha = alpha;
    }

    @Override
    public AlphaHsvComponent alpha() {
        return this.alpha;
    }

    private final AlphaHsvComponent alpha;

    @Override
    Hsv replace(final HueHsvComponent hue,
                final SaturationHsvComponent saturation,
                final ValueHsvComponent value) {
        return new AlphaHsv(hue, saturation, value, this.alpha);
    }

    @Override
    Color toColor0(final Color color) {
        return color.set(this.alpha.toAlphaColorComponent());
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof AlphaHsv;
    }

    @Override
    boolean equals2(final Hsv other) {
        return this.alpha.equals(other.alpha());
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    String functionName() {
        return "hsva(";
    }

    @Override
    void buildToStringAlpha(final ToStringBuilder builder) {
        builder.value(this.alpha);
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
