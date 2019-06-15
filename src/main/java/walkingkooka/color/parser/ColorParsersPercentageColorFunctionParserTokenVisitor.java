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

import walkingkooka.ToStringBuilder;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;

/**
 * Accepts (@link ParserToken) tokens and replaces it with a {@link ColorFunctionPercentageParserToken}.
 */
final class ColorParsersPercentageColorFunctionParserTokenVisitor extends ColorFunctionParserTokenVisitor {

    static ColorFunctionParserToken transform(final ParserToken token, final ParserContext context) {
        final ColorParsersPercentageColorFunctionParserTokenVisitor visitor = new ColorParsersPercentageColorFunctionParserTokenVisitor();
        visitor.accept(token);
        return ColorFunctionParserToken.percentage(visitor.value, token.text());
    }

    ColorParsersPercentageColorFunctionParserTokenVisitor() {
        super();
    }

    @Override
    protected void visit(final ColorFunctionNumberParserToken token) {
        this.value = token.value();
    }

    private double value;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("percentage")
                .value(this.value)
                .build();
    }
}
