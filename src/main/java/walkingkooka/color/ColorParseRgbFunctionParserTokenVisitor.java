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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.text.cursor.parser.ColorParserToken;
import walkingkooka.text.cursor.parser.LongParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.text.cursor.parser.SequenceParserToken;

/**
 * Handles converting a {@link SequenceParserToken} into a {@link ColorParserToken}.
 */
final class ColorParseRgbFunctionParserTokenVisitor extends ParserTokenVisitor {

    static ColorParserToken parseSequenceParserToken(final SequenceParserToken token) {
        final ColorParseRgbFunctionParserTokenVisitor visitor = new ColorParseRgbFunctionParserTokenVisitor();
        visitor.accept(token);
        return ColorParserToken.with(visitor.color, token.text());
    }

    @Override
    protected void visit(final LongParserToken token) {
        for(;;) {
            final byte value = token.value().byteValue();

            if(null==this.red) {
                this.red = RedColorComponent.with(value);
                break;
            }

            if(null==this.green) {
                this.green = GreenColorComponent.with(value);
                break;
            }
            this.color = Color.with(this.red, this.green, BlueColorComponent.with(value));
            break;
        }
    }

    RedColorComponent red;
    GreenColorComponent green;
    Color color;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("red")
                .value(this.red)
                .label("green")
                .value(this.green)
                .label("color")
                .value(this.color)
                .build();
    }
}
