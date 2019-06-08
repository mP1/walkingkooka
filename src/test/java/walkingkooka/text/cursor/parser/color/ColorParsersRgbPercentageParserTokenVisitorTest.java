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

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.DoubleParserToken;

public final class ColorParsersRgbPercentageParserTokenVisitorTest extends ColorParsersParserTokenVisitorTestCase {

    @Test
    public void testToString() {
        final ColorParsersRgbPercentageParserTokenVisitor visitor = new ColorParsersRgbPercentageParserTokenVisitor();
        visitor.accept(DoubleParserToken.with(123, "123"));
        this.toStringAndCheck(visitor, "component=313.65");
    }

    @Override
    public ColorParsersRgbPercentageParserTokenVisitor createVisitor() {
        return new ColorParsersRgbPercentageParserTokenVisitor();
    }

    @Override
    public Class<ColorParsersRgbPercentageParserTokenVisitor> type() {
        return ColorParsersRgbPercentageParserTokenVisitor.class;
    }
}
