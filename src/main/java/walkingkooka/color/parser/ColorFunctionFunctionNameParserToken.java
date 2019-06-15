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
import walkingkooka.color.GreenColorComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.HueHsvComponent;
import walkingkooka.color.LightnessHslComponent;
import walkingkooka.color.RedColorComponent;
import walkingkooka.color.SaturationHslComponent;
import walkingkooka.color.SaturationHsvComponent;
import walkingkooka.color.ValueHsvComponent;

public final class ColorFunctionFunctionNameParserToken extends ColorFunctionNonSymbolParserToken<String> {

    static ColorFunctionFunctionNameParserToken with(final String value, final String text) {
        check(value, text);

        return new ColorFunctionFunctionNameParserToken(value, text);
    }

    private ColorFunctionFunctionNameParserToken(final String value, final String text) {
        super(value, text);
    }

    // isXXX............................................................................................................

    @Override
    public boolean isFunctionName() {
        return true;
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isPercentage() {
        return false;
    }

    // ColorFunctionParserTokenVisitor..................................................................................

    @Override
    public void accept(final ColorFunctionParserTokenVisitor visitor) {
        visitor.visit(this);
    }

    // Object...........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ColorFunctionFunctionNameParserToken;
    }

    // ColorFunctionTransformer.........................................................................................

    @Override
    RedColorComponent colorRed() {
        throw new UnsupportedOperationException();
    }

    @Override
    BlueColorComponent colorBlue() {
        throw new UnsupportedOperationException();
    }

    @Override
    GreenColorComponent colorGreen() {
        throw new UnsupportedOperationException();
    }

    @Override
    AlphaColorComponent colorAlpha() {
        throw new UnsupportedOperationException();
    }

    @Override
    HueHslComponent hslHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    SaturationHslComponent hslSaturation() {
        throw new UnsupportedOperationException();
    }

    @Override
    LightnessHslComponent hslLightness() {
        throw new UnsupportedOperationException();
    }

    @Override
    AlphaHslComponent hslAlpha() {
        throw new UnsupportedOperationException();
    }

    @Override
    HueHsvComponent hsvHue() {
        throw new UnsupportedOperationException();
    }

    @Override
    SaturationHsvComponent hsvSaturation() {
        throw new UnsupportedOperationException();
    }

    @Override
    ValueHsvComponent hsvValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    AlphaHsvComponent hsvAlpha() {
        throw new UnsupportedOperationException();
    }
}
