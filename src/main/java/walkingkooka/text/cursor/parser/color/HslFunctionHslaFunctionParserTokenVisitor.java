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

package walkingkooka.text.cursor.parser.color;

import walkingkooka.color.Hsl;
import walkingkooka.color.HslComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.SaturationHslComponent;
import walkingkooka.text.cursor.parser.DoubleParserToken;

/**
 * A {@link ColorParserTokenVisitor} based shared by hsl() and hsla()l
 */
abstract class HslFunctionHslaFunctionParserTokenVisitor extends ColorParserTokenVisitor {

    HslFunctionHslaFunctionParserTokenVisitor() {
        super();
    }

    @Override
    protected final void visit(final DoubleParserToken token) {
        do {
            final float value = token.value().floatValue();
            if (null == this.hue) {
                this.hue = HslComponent.hue(value);
                break;
            }
            final float percentToFloat = this.percentToFloat(value);
            if (null == this.saturation) {
                this.saturation = HslComponent.saturation(percentToFloat);
                break;
            }
            this.visit0(percentToFloat);
        } while (false);
    }

    abstract void visit0(final float percentToFloat);

    private float percentToFloat(final float value) {
        return value / 100;
    }

    HueHslComponent hue;
    SaturationHslComponent saturation;
    Hsl hsl;
}
