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

import org.junit.jupiter.api.Test;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.text.cursor.parser.ParserTokenVisitorTesting;
import walkingkooka.type.MemberVisibility;

public final class ColorParseArgbFunctionParserTokenVisitorTest implements ParserTokenVisitorTesting<ColorParseArgbFunctionParserTokenVisitor, ParserToken> {

    @Test
    public void testToString() {
        final ColorParseArgbFunctionParserTokenVisitor visitor = new ColorParseArgbFunctionParserTokenVisitor();
        visitor.red = RedColorComponent.with((byte) 12);
        visitor.green = GreenColorComponent.with((byte) 34);
        visitor.blue = BlueColorComponent.with((byte) 5);
        visitor.color = Color.fromRgb(0x0c2200);

        this.toStringAndCheck(visitor, "red=0c green=22 blue=05 color=#0c2200");
    }

    @Test
    public void testToString2() {
        final ColorParseArgbFunctionParserTokenVisitor visitor = new ColorParseArgbFunctionParserTokenVisitor();
        visitor.red = RedColorComponent.with((byte) 12);
        this.toStringAndCheck(visitor, "red=0c");
    }

    @Override
    public ColorParseArgbFunctionParserTokenVisitor createVisitor() {
        return new ColorParseArgbFunctionParserTokenVisitor();
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<ColorParseArgbFunctionParserTokenVisitor> type() {
        return ColorParseArgbFunctionParserTokenVisitor.class;
    }

    @Override
    public String typeNamePrefix() {
        return Color.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return ParserTokenVisitor.class.getSimpleName();
    }
}
