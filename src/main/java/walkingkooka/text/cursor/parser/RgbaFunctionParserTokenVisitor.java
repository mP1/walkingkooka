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
import walkingkooka.color.ColorComponent;

/**
 * Handles converting a {@link SequenceParserToken} into a {@link ColorParserToken}.
 */
final class RgbaFunctionParserTokenVisitor extends RgbFunctionRgbaFunctionParserTokenVisitor {

    static ColorParserToken parseSequenceParserToken(final SequenceParserToken token) {
        return new RgbaFunctionParserTokenVisitor().acceptAndCreateColorParserToken(token);
    }

    @Override
    protected void visit(final DoubleParserToken token) {
        this.color = this.color.set(ColorComponent.alpha((byte)(token.value().floatValue() * ColorComponent.MAX_VALUE)));
    }

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
