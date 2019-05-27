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

import walkingkooka.color.BlueColorComponent;
import walkingkooka.color.Color;
import walkingkooka.color.ColorComponent;
import walkingkooka.color.GreenColorComponent;
import walkingkooka.color.RedColorComponent;

/**
 * Base class for {@link ParserTokenVisitor} used during color parsing.
 */
abstract class RgbFunctionRgbaFunctionParserTokenVisitor extends ParserTokenVisitor {

    RgbFunctionRgbaFunctionParserTokenVisitor() {
        super();
    }

    final ColorParserToken acceptAndCreateColorParserToken(final SequenceParserToken token) {
        this.accept(token);
        return ColorParserToken.with(this.color, token.text());
    }

    @Override
    protected final void visit(final LongParserToken token) {
        for(;;) {
            final byte value = token.value().byteValue();

            if(null==this.red) {
                this.red = ColorComponent.red(value);
                break;
            }

            if(null==this.green) {
                this.green = ColorComponent.green(value);
                break;
            }
            this.color = this.blue(ColorComponent.blue(value));
            break;
        }
    }

    final Color blue(final BlueColorComponent blue) {
        return Color.with(this.red, this.green, blue);
    }

    RedColorComponent red;
    GreenColorComponent green;
    Color color;
}
