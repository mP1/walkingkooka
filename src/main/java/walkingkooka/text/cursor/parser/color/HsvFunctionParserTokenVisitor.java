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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.color.Hsv;
import walkingkooka.color.HsvComponent;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.SequenceParserToken;

/**
 * A {@link ColorParserTokenVisitor} used to parse a {@link SequenceParserToken} into a {@link HsvParserToken}
 */
final class HsvFunctionParserTokenVisitor extends HsvFunctionHsvaFunctionParserTokenVisitor {

    static HsvParserToken acceptParserToken(final ParserToken token) {
        final HsvFunctionParserTokenVisitor visitor = new HsvFunctionParserTokenVisitor();
        visitor.accept(token);
        return HsvParserToken.with(visitor.hsv, token.text());
    }

    HsvFunctionParserTokenVisitor() {
        super();
    }

    @Override
    void visit0(final float percentToFloat) {
        this.hsv = Hsv.with(this.hue, this.saturation, HsvComponent.value(percentToFloat));
    }

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("hue").value(this.hue)
                .label("saturation").value(this.saturation)
                .label("hsv").value(this.hsv)
                .build();
    }
}
