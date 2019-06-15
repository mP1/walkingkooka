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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokens;
import walkingkooka.text.cursor.parser.SequenceParserToken;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ColorParsersPercentageColorFunctionParserTokenVisitorTest extends ColorParsersParserTokenVisitorTestCase<ColorParsersPercentageColorFunctionParserTokenVisitor> {

    @Test
    public void testTransform() {
        final List<ParserToken> tokens = Lists.array();
        tokens.add(ColorFunctionParserToken.number(150.0, "150"));
        tokens.add(ParserTokens.string("%", "%"));
        final SequenceParserToken sequence = ParserTokens.sequence(tokens, ParserToken.text(tokens));

        assertEquals(ColorFunctionParserToken.percentage(Double.valueOf(150), "150%"),
                ColorParsersPercentageColorFunctionParserTokenVisitor.transform(sequence, ParserContexts.fake()),
                () -> "transform " + sequence);
    }

    @Test
    public void testToString() {
        final ColorParsersPercentageColorFunctionParserTokenVisitor visitor = new ColorParsersPercentageColorFunctionParserTokenVisitor();
        visitor.accept(ColorFunctionParserToken.number(123.0, "123"));
        this.toStringAndCheck(visitor, "percentage=123.0");
    }

    @Override
    public ColorParsersPercentageColorFunctionParserTokenVisitor createVisitor() {
        return new ColorParsersPercentageColorFunctionParserTokenVisitor();
    }

    @Override
    public Class<ColorParsersPercentageColorFunctionParserTokenVisitor> type() {
        return ColorParsersPercentageColorFunctionParserTokenVisitor.class;
    }
}
