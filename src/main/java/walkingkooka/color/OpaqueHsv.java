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
 * A {@link Hsv} that includes an alpha property.
 */
final class OpaqueHsv extends Hsv {

    static OpaqueHsv withOpaque(final HueHsvComponent hue,
                                final SaturationHsvComponent saturation,
                                final ValueHsvComponent value) {
        return new OpaqueHsv(hue, saturation, value);
    }

    private OpaqueHsv(final HueHsvComponent hue,
                      final SaturationHsvComponent saturation,
                      final ValueHsvComponent value) {
        super(hue, saturation, value);
    }

    @Override
    public AlphaHsvComponent alpha() {
        return AlphaHsvComponent.OPAQUE;
    }

    /**
     * Factory that creates a {@link Hsv} with the given {@link HsvComponent components}.
     */
    @Override
    Hsv replace(final HueHsvComponent hue,
                final SaturationHsvComponent saturation,
                final ValueHsvComponent value) {
        return withOpaque(hue, saturation, value);
    }

    @Override
    Color toColor0(final Color color) {
        return color;
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof OpaqueHsv;
    }

    @Override
    boolean equals2(final Hsv other) {
        return true; // no alpha component to compare.
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    String functionName() {
        return "hsv(";
    }

    @Override
    void buildToStringAlpha(final ToStringBuilder builder) {
    }

    // Serializable
    private static final long serialVersionUID = 1L;
}
