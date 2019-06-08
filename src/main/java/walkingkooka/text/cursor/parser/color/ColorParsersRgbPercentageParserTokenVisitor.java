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
import walkingkooka.color.ColorComponent;
import walkingkooka.text.cursor.parser.DoubleParserToken;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;

/**
 * Accepts PERCENTAGE tokens replacing them with just a single {@link DoubleParserToken} with a value from the percentage.
 */
final class ColorParsersRgbPercentageParserTokenVisitor extends ParserTokenVisitor {

    static DoubleParserToken transform(final ParserToken token, final ParserContext context) {
        final ColorParsersRgbPercentageParserTokenVisitor visitor = new ColorParsersRgbPercentageParserTokenVisitor();
        visitor.accept(token);
        return DoubleParserToken.with(visitor.value, token.text());
    }

    ColorParsersRgbPercentageParserTokenVisitor() {
        super();
    }

    @Override
    protected void visit(final DoubleParserToken token) {
        this.value = token.value() * ColorComponent.MAX_VALUE / 100;
    }

    private double value;


    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("component")
                .value(this.value)
                .build();
    }
}
