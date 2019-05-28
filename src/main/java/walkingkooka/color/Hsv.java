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

import java.util.Objects;

/**
 * Holds the hue, saturation and value which describe a color.
 */
final public class Hsv extends ColorHslOrHsv {

    /**
     * Factory that creates a new {@link Hsv}
     */
    public static Hsv with(final HueHsvComponent hue,
                           final SaturationHsvComponent saturation,
                           final ValueHsvComponent value) {
        Objects.requireNonNull(hue, "hue");
        Objects.requireNonNull(saturation, "saturation");
        Objects.requireNonNull(value, "value");

        return new Hsv(hue, saturation, value);
    }

    /**
     * Private constructor use factory.
     */
    private Hsv(final HueHsvComponent hue, final SaturationHsvComponent saturation, final ValueHsvComponent value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    /**
     * Would be setter that returns a {@link Hsv} holding the new component. If the component is not new this will be returned.
     */
    public Hsv set(final HsvComponent component) {
        Objects.requireNonNull(component, "component");

        return component.setComponent(this);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link HueHsvComponent}.
     */
    Hsv setHue(final HueHsvComponent hue) {
        return this.hue.equals(hue) ?
                this :
                this.replace(hue, this.saturation, this.value);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link SaturationHsvComponent}.
     */
    Hsv setSaturation(final SaturationHsvComponent saturation) {
        return this.saturation.equals(saturation) ?
                this :
                this.replace(this.hue, saturation, this.value);
    }

    /**
     * Factory that creates a new {@link Hsv} with the new {@link ValueHsvComponent}.
     */
    Hsv setValue(final ValueHsvComponent value) {

        return this.value.equals(value) ?
                this :
                this.replace(this.hue, this.saturation, value);
    }

    /**
     * Factory that creates a {@link Hsv} with the given {@link HsvComponent components}.
     */
    private Hsv replace(final HueHsvComponent hue, final SaturationHsvComponent saturation, final ValueHsvComponent value) {
        return new Hsv(hue, saturation, value);
    }

    // properties

    /**
     * Getter that returns only the {@link HueHsvComponent}
     */
    public HueHsvComponent hue() {
        return this.hue;
    }

    final HueHsvComponent hue;

    /**
     * Getter that returns only the {@link SaturationHsvComponent}
     */
    public SaturationHsvComponent saturation() {
        return this.saturation;
    }

    final SaturationHsvComponent saturation;

    /**
     * Getter that returns only the {@link ValueHsvComponent}
     */
    public ValueHsvComponent value() {
        return this.value;
    }

    final ValueHsvComponent value;

    // ColorHslOrHsv....................................................................................................

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

    @Override
    public Hsl toHsl() {
        return this.toColor().toHsl();
    }

    @Override
    public Hsv toHsv() {
        return this;
    }

    // Object.........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.hue, this.saturation, this.value);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Hsv;
    }

    @Override
    boolean equals0(final Object other) {
        return this.equals1(Cast.to(other));
    }

    private boolean equals1(final Hsv other) {
        return this.hue.equals(other.hue) &&
                this.saturation.equals(other.saturation) &&
                this.value.equals(other.value);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.separator(",")
                .value(this.hue)
                .value(this.saturation)
                .value(this.value);
    }

    // Serializable....................................................................................................

    private static final long serialVersionUID = 1;
}
