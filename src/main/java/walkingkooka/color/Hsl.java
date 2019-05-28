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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.HslParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.Parsers;

import java.util.Objects;

/**
 * Holds the hue, saturation and lightness which describe a color.
 */
final public class Hsl extends ColorHslOrHsv {

    // parse hsl(359,100%,100%)..............................................................................................

    public static Hsl parse(final String text) {
        return parse0(text, HSL_FUNCTION_PARSER);
    }

    private final static Parser<ParserContext> HSL_FUNCTION_PARSER = Parsers.hslFunction()
            .orReport(ParserReporters.basic());

    private static Hsl parse0(final String text,
                              final Parser<ParserContext> parser) {
        try {
            return parser.parse(TextCursors.charSequence(text),
                    ParserContexts.basic(DecimalNumberContexts.basic("$", '.', 'E', ',', '-', '%', '+')))
                    .map(t -> HslParserToken.class.cast(t).value())
                    .orElseThrow(() -> new IllegalArgumentException("Parsing " + CharSequences.quoteAndEscape(text) + " failed."));
        } catch (final ParserException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Factory that creates a new {@link Hsl}
     */
    public static Hsl with(final HueHslComponent hue,
                           final SaturationHslComponent saturation,
                           final LightnessHslComponent lightness) {
        Objects.requireNonNull(hue, "hue");
        Objects.requireNonNull(saturation, "saturation");
        Objects.requireNonNull(lightness, "lightness");

        return new Hsl(hue, saturation, lightness);
    }

    /**
     * Private constructor use factory.
     */
    private Hsl(final HueHslComponent hue, final SaturationHslComponent saturation,
                final LightnessHslComponent lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    /**
     * Would be setter that returns a {@link Hsl} holding the new component. If the component is not new this will be returned.
     */
    public Hsl set(final HslComponent component) {
        Objects.requireNonNull(component, "component");

        return component.setComponent(this);
    }

    /**
     * Factory that creates a new {@link Hsl} with the new {@link HueHslComponent}.
     */
    Hsl setHue(final HueHslComponent hue) {
        return this.hue.equals(hue) ?
                this :
                this.replace(hue, this.saturation, this.lightness);
    }

    /**
     * Factory that creates a new {@link Hsl} with the new {@link SaturationHslComponent}.
     */
    Hsl setSaturation(final SaturationHslComponent saturation) {
        return this.saturation.equals(saturation) ?
                this :
                this.replace(this.hue, saturation, this.lightness);
    }

    /**
     * Factory that creates a new {@link Hsl} with the new {@link LightnessHslComponent}.
     */
    Hsl setValue(final LightnessHslComponent lightness) {
        return this.lightness.equals(lightness) ?
                this :
                this.replace(this.hue, this.saturation, lightness);
    }

    /**
     * Factory that creates a {@link Hsl} with the given {@link HslComponent components}.
     */
    private Hsl replace(final HueHslComponent hue,
                        final SaturationHslComponent saturation,
                        final LightnessHslComponent lightness) {
        return new Hsl(hue, saturation, lightness);
    }

    // properties

    /**
     * Getter that returns only the {@link HueHslComponent}
     */
    public HueHslComponent hue() {
        return this.hue;
    }

    final HueHslComponent hue;

    /**
     * Getter that returns only the {@link SaturationHslComponent}
     */
    public SaturationHslComponent saturation() {
        return this.saturation;
    }

    final SaturationHslComponent saturation;

    /**
     * Getter that returns only the {@link LightnessHslComponent}
     */
    public LightnessHslComponent lightness() {
        return this.lightness;
    }

    final LightnessHslComponent lightness;

    // ColorHslOrHsv....................................................................................................

    @Override
    public boolean isColor() {
        return false;
    }

    @Override
    public boolean isHsl() {
        return true;
    }

    public boolean isHsv() {
        return false;
    }

    /**
     * Returns the equivalent {@link Color}.<br>
     * Converts an HSL color value to RGB. Conversion formula adapted from <a>http://en.wikipedia.org/wiki/HSL_color_space}</a>.
     * Assumes h, s, and l are contained in the set [0, 1] and returns r, g, and b in the set [0, 255].
     *
     * <pre>
     * function hslToRgb(h, s, l){
     *     var r, g, b;
     *
     *     if(s == 0){
     *         r = g = b = l; // achromatic
     *     }else{
     *         function hue2rgb(p, q, t){
     *             if(t < 0) t += 1;
     *             if(t > 1) t -= 1;
     *             if(t < 1/6) return p + (q - p) * 6 * t;
     *             if(t < 1/2) return q;
     *             if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
     *             return p;
     *         }
     *
     *         var q = l < 0.5 ? l * (1 + s) : l + s - l * s;
     *         var p = 2 * l - q;
     *         r = hue2rgb(p, q, h + 1/3);
     *         g = hue2rgb(p, q, h);
     *         b = hue2rgb(p, q, h - 1/3);
     *     }
     *
     *     return [r * 255, g * 255, b * 255];
     * }
     *         </pre>
     * <p>
     * <a>http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript</a>
     */
    @Override
    public Color toColor() {
        // vars
        float red;
        float green;
        float blue;

        // constants
        final float saturation = this.saturation.value;
        final float lightness = this.lightness.value;

        if (0 == saturation) {
            red = lightness;
            green = lightness;
            blue = lightness;
        } else {
            final float q = lightness < 0.5f ? lightness * (1 + saturation)
                    : (lightness + saturation) - (lightness * saturation);
            final float p = (2 * lightness) - q;

            final float hue = this.hue.value / HueHslComponent.MAX; // now within 0..1
            red = Hsl.hue2rgb(p, q, hue + (1f / 3));
            green = Hsl.hue2rgb(p, q, hue);
            blue = Hsl.hue2rgb(p, q, hue - (1f / 3));
        }

        return Color.with(red, green, blue);
    }

    /**
     * <pre>
     * function hue2rgb(p, q, t){
     *     if(t < 0) t += 1;
     *     if(t > 1) t -= 1;
     *     if(t < 1/6) return p + (q - p) * 6 * t;
     *     if(t < 1/2) return q;
     *     if(t < 2/3) return p + (q - p) * (2/3 - t) * 6;
     *     return p;
     * }
     * </pre>
     *
     * <a>http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript</a>
     */
    private static float hue2rgb(final float p, final float q, float t) {
        if (t < 0) {
            t += 1;
        }
        if (t > 1) {
            t -= 1;
        }
        if (t < (1f / 6)) {
            return p + ((q - p) * 6 * t);
        }
        if (t < (1f / 2)) {
            return q;
        }
        if (t < (2f / 3)) {
            return p + ((q - p) * ((2f / 3) - t) * 6);
        }
        return p;
    }

    @Override
    public Hsl toHsl() {
        return this;
    }

    @Override
    public Hsv toHsv() {
        return this.toColor().toHsv();
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.hue, this.saturation, this.lightness);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Hsl;
    }

    @Override
    boolean equals0(final Object other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final Hsl other) {
        return this.hue.equals(other.hue) &&
                this.saturation.equals(other.saturation) &&
                this.lightness.equals(other.lightness);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.separator(",")
                .value(this.hue)
                .value(this.saturation)
                .value(this.lightness);
    }

    // Serializable.....................................................................................................

    private static final long serialVersionUID = 1;
}
