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

package walkingkooka.color.parser;

import walkingkooka.color.AlphaColorComponent;
import walkingkooka.color.AlphaHslComponent;
import walkingkooka.color.AlphaHsvComponent;
import walkingkooka.color.BlueColorComponent;
import walkingkooka.color.ColorComponent;
import walkingkooka.color.GreenColorComponent;
import walkingkooka.color.HslComponent;
import walkingkooka.color.HsvComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.HueHsvComponent;
import walkingkooka.color.LightnessHslComponent;
import walkingkooka.color.RedColorComponent;
import walkingkooka.color.SaturationHslComponent;
import walkingkooka.color.SaturationHsvComponent;
import walkingkooka.color.ValueHsvComponent;

public final class ColorFunctionPercentageParserToken extends ColorFunctionNonSymbolParserToken<Double> {

    static ColorFunctionPercentageParserToken with(final Double value, final String text) {
        check(value, text);

        return new ColorFunctionPercentageParserToken(value, text);
    }

    private ColorFunctionPercentageParserToken(final Double value, final String text) {
        super(value, text);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isFunctionName() {
        return false;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isPercentage() {
        return true;
    }

    // ColorFunctionParserTokenVisitor..................................................................................

    @Override
    public void accept(final ColorFunctionParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ColorFunctionPercentageParserToken;
    }

    // ColorFunctionTransformer.........................................................................................

    @Override
    RedColorComponent colorRed() {
        return ColorComponent.red(this.byteValue());
    }

    @Override
    BlueColorComponent colorBlue() {
        return ColorComponent.blue(this.byteValue());
    }

    @Override
    GreenColorComponent colorGreen() {
        return ColorComponent.green(this.byteValue());
    }

    @Override
    AlphaColorComponent colorAlpha() {
        return ColorComponent.alpha(this.byteValue());
    }

    private final byte byteValue() {
        return (byte)Math.round(this.value() * ColorComponent.MAX_VALUE / 100);
    }

    @Override
    HueHslComponent hslHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    SaturationHslComponent hslSaturation() {
        return HslComponent.saturation(this.floatValue());
    }

    @Override
    LightnessHslComponent hslLightness() {
        return HslComponent.lightness(this.floatValue());
    }

    @Override
    AlphaHslComponent hslAlpha() {
        return HslComponent.alpha(this.floatValue());
    }

    @Override
    HueHsvComponent hsvHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    SaturationHsvComponent hsvSaturation() {
        return HsvComponent.saturation(this.floatValue());
    }

    @Override
    ValueHsvComponent hsvValue() {
        return HsvComponent.value(this.floatValue());
    }

    @Override
    AlphaHsvComponent hsvAlpha() {
        return HsvComponent.alpha(this.floatValue());
    }

    private float floatValue() {
        return this.value().floatValue() / 100;
    }
}
