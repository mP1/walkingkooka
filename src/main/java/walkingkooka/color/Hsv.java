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

import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.color.parser.ColorParsers;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;

import java.util.Objects;

/**
 * Holds the hue, saturation and value which describe a color.
 */
public abstract class Hsv extends ColorHslOrHsv {

    // parse hsv(359,100%,100%)..............................................................................................

    public static Hsv parseHsv(final String text) {
        return parseColorHslOrHsvParserToken(text, HSV_FUNCTION_PARSER)
                .toHsv();
    }

    private final static Parser<ParserContext> HSV_FUNCTION_PARSER = ColorParsers.hsv()
            .orReport(ParserReporters.basic());

    /**
     * Factory that creates a new {@link Hsv}
     */
    public static Hsv with(final HueHsvComponent hue,
                           final SaturationHsvComponent saturation,
                           final ValueHsvComponent value) {
        Objects.requireNonNull(hue, "hue");
        Objects.requireNonNull(saturation, "saturation");
        Objects.requireNonNull(value, "value");

        return OpaqueHsv.withOpaque(hue, saturation, value);
    }

    /**
     * Package private to limit sub classing.
     */
    Hsv(final HueHsvComponent hue,
        final SaturationHsvComponent saturation,
        final ValueHsvComponent value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    /**
     * Would be setter that returns a {@link Hsv} holding the new component. If the component is not new this will be returned.
     */
    public final Hsv set(final HsvComponent component) {
        Objects.requireNonNull(component, "component");

        return component.setComponent(this);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link HueHsvComponent}.
     */
    final Hsv setHue(final HueHsvComponent hue) {
        return this.hue.equals(hue) ?
                this :
                this.replace(hue, this.saturation, this.value);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link SaturationHsvComponent}.
     */
    final Hsv setSaturation(final SaturationHsvComponent saturation) {
        return this.saturation.equals(saturation) ?
                this :
                this.replace(this.hue, saturation, this.value);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link ValueHsvComponent}.
     */
    final Hsv setValue(final ValueHsvComponent value) {

        return this.value.equals(value) ?
                this :
                this.replace(this.hue, this.saturation, value);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link AlphaHsvComponent}.
     */
    final Hsv setAlpha(final AlphaHsvComponent alpha) {

        return this.alpha().equals(alpha) ?
                this :
                AlphaHsv.withAlpha(this.hue, this.saturation, this.value, alpha);
    }

    /**
     * Factory that creates a {@link Hsv} with the given {@link HsvComponent components}.
     */
    abstract
    Hsv replace(final HueHsvComponent hue,
                final SaturationHsvComponent saturation,
                final ValueHsvComponent value);

    // properties

    /**
     * Getter that returns only the {@link HueHsvComponent}
     */
    public final HueHsvComponent hue() {
        return this.hue;
    }

    final HueHsvComponent hue;

    /**
     * Getter that returns only the {@link SaturationHsvComponent}
     */
    public final SaturationHsvComponent saturation() {
        return this.saturation;
    }

    final SaturationHsvComponent saturation;

    /**
     * Getter that returns only the {@link ValueHsvComponent}
     */
    public final ValueHsvComponent value() {
        return this.value;
    }

    final ValueHsvComponent value;

    /**
     * Getter that returns only the {@link AlphaHsvComponent}
     */
    public abstract AlphaHsvComponent alpha();

    // ColorHslOrHsv....................................................................................................

    @Override
    public final boolean isColor() {
        return false;
    }

    @Override
    public final boolean isHsl() {
        return false;
    }

    public final boolean isHsv() {
        return true;
    }

    /**
     * Returns the equivalent {@link Color}.<br>
     * <A>http://en.wikipedia.org/wiki/HSL_and_HSV</A>
     */
    @Override
    public Color toColor() {
        final float value = this.value.value;
        final float chroma = this.saturation.value * value;
        final float q = this.hue.value / 60.0f;
        final float x = chroma * (1.0f - Math.abs((q % 2.0f) - 1.0f));
        float red = 0;
        float green = 0;
        float blue = 0;

        switch ((int) q) {
            case 0:
                red = chroma;
                green = x;
                break;
            case 1:
                red = x;
                green = chroma;
                break;
            case 2:
                green = chroma;
                blue = x;
                break;
            case 3:
                green = x;
                blue = chroma;
                break;
            case 4:
                red = x;
                blue = chroma;
                break;
            case 5:
                red = chroma;
                blue = x;
                break;
        }

        final float min = value - chroma;
        red += min;
        green += min;
        blue += min;

        return Color.with(red, green, blue);
    }

    abstract Color toColor0(final Color color);

    @Override
    public final Hsl toHsl() {
        return this.toColor().toHsl();
    }

    @Override
    public Hsv toHsv() {
        return this;
    }

    // HasJsonNode......................................................................................................

    static {
        HasJsonNode.register("hsv",
                Hsv::fromJsonNodeHsv,
                Hsv.class, AlphaHsv.class, OpaqueHsv.class);
    }

    /**
     * Creates a {@link Hsv} from a {@link JsonNode}.
     */
    public static Hsv fromJsonNodeHsv(final JsonNode from) {
        Objects.requireNonNull(from, "from");

        try {
            return parseHsv(from.stringValueOrFail());
        } catch (final JsonNodeException cause) {
            throw new IllegalArgumentException(cause.getMessage(), cause);
        }
    }

    // Object...........................................................................................................

    @Override
    public final int hashCode() {
        return Objects.hash(this.hue, this.saturation, this.value);
    }

    @Override final boolean equals0(final Object other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final Hsv other) {
        return this.hue.equals(other.hue) &&
                this.saturation.equals(other.saturation) &&
                this.value.equals(other.value) &&
                this.equals2(other);
    }

    abstract boolean equals2(final Hsv other);

    @Override
    public final void buildToString(final ToStringBuilder builder) {
        builder.separator(",")
                .append(this.functionName())
                .value(this.hue)
                .value(this.saturation)
                .value(this.value);
        this.buildToStringAlpha(builder);
        builder.append(')');
    }

    abstract String functionName();

    abstract void buildToStringAlpha(final ToStringBuilder builder);

    // Serializable....................................................................................................

    private static final long serialVersionUID = 1;
}
