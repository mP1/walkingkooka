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

public final class ColorParsersComponentsColorFunctionParserTokenVisitorTest extends ColorParsersParserTokenVisitorTestCase<ColorParsersComponentsColorFunctionParserTokenVisitor> {

    @Test
    public void testToString() {
        final ColorParsersComponentsColorFunctionParserTokenVisitor visitor = new ColorParsersComponentsColorFunctionParserTokenVisitor();
        visitor.accept(ColorFunctionParserToken.number(123.0, "123"));
        this.toStringAndCheck(visitor, "values=123");
    }

    @Override
    public ColorParsersComponentsColorFunctionParserTokenVisitor createVisitor() {
        return new ColorParsersComponentsColorFunctionParserTokenVisitor();
    }

    @Override
    public Class<ColorParsersComponentsColorFunctionParserTokenVisitor> type() {
        return ColorParsersComponentsColorFunctionParserTokenVisitor.class;
    }
}
