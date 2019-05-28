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

package walkingkooka.text.cursor.parser;

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.color.Hsl;
import walkingkooka.color.HslComponent;
import walkingkooka.color.HueHslComponent;
import walkingkooka.color.SaturationHslComponent;

/**
 * A {@link ParserTokenVisitor} used to parse a {@link SequenceParserToken} into a {@link HslParserToken}
 */
final class HslFunctionParserTokenVisitor extends ParserTokenVisitor {

    static HslParserToken acceptParserToken(final ParserToken token) {
        final HslFunctionParserTokenVisitor visitor = new HslFunctionParserTokenVisitor();
        visitor.accept(token);
        return HslParserToken.with(visitor.hsl, token.text());
    }
    
    HslFunctionParserTokenVisitor() {
        super();
    }

    @Override
    protected final void visit(final DoubleParserToken token) {
        do {
            final float value = token.value().floatValue();
            if(null==this.hue) {
                this.hue = HslComponent.hue(value);
                break;
            }
            if(null==this.saturation) {
                this.saturation = HslComponent.saturation(percentToFloat(value));
                break;
            }
            this.hsl = Hsl.with(this.hue, this.saturation, HslComponent.lightness(percentToFloat(value)));
        } while(false);
    }

    private float percentToFloat(final float value) {
        return value / 100;
    }

    HueHslComponent hue;
    SaturationHslComponent saturation;
    Hsl hsl;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("hue").value(this.hue)
                .label("saturation").value(this.saturation)
                .label("hsl").value(this.hsl)
                .build();
    }
}
