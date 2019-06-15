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
import walkingkooka.collect.list.Lists;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.List;
import java.util.Optional;

/**
 * Creates a {@link ColorHslOrHsv} from a {@link ColorFunctionParserToken}.
 */
final class ColorParsersComponentsColorFunctionParserTokenVisitor extends ColorFunctionParserTokenVisitor {

    static ColorHslOrHsv transform(final ParserToken token) {
        final ColorParsersComponentsColorFunctionParserTokenVisitor visitor = new ColorParsersComponentsColorFunctionParserTokenVisitor();
        visitor.accept(token);

        final List<ColorFunctionParserToken> values = visitor.values;

        return visitor.transformer.colorHslOrHsv(ColorFunctionParserToken.class.cast(values.get(0)),
                ColorFunctionParserToken.class.cast(values.get(1)),
                ColorFunctionParserToken.class.cast(values.get(2)),
                Optional.ofNullable(values.size() == 4 ?
                        ColorFunctionParserToken.class.cast(values.get(3)) :
                        null));
    }

    ColorParsersComponentsColorFunctionParserTokenVisitor() {
        super();
    }



    // ColorFunctionParserTokenVisitor..................................................................................

    @Override
    protected void visit(final ColorFunctionFunctionNameParserToken token) {
        this.functionName = token;

        ColorFunctionTransformer transformer;
        switch(token.value()) {
            case "rgb":
            case "rgba":
                transformer = ColorFunctionTransformer.RGB;
                break;
            case "hsl":
            case "hsla":
                transformer = ColorFunctionTransformer.HSL;
                break;
            case "hsv":
            case "hsva":
                transformer = ColorFunctionTransformer.HSV;
                break;
            default:
                throw new IllegalArgumentException("Unknown function " + token + " in " + CharSequences.quoteAndEscape(token.text()));
        }

        this.transformer = transformer;
    }

    private ColorFunctionFunctionNameParserToken functionName;
    private ColorFunctionTransformer transformer;

    @Override
    protected void visit(final ColorFunctionNumberParserToken token) {
        this.values.add(token);
    }

    @Override
    protected void visit(final ColorFunctionPercentageParserToken token) {
        this.values.add(token);
    }

    private final List<ColorFunctionParserToken> values = Lists.array();

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("functionName")
                .value(this.functionName)
                .label("transformer")
                .value(this.transformer)
                .label("values")
                .value(this.values)
                .build();
    }
}
