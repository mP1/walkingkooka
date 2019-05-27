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

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.color.ColorComponent;
import walkingkooka.type.MemberVisibility;

public final class RgbFunctionParserTokenVisitorTest implements ParserTokenVisitorTesting<RgbFunctionParserTokenVisitor, ParserToken> {

    @Test
    public void testToString() {
        final RgbFunctionParserTokenVisitor visitor = new RgbFunctionParserTokenVisitor();
        visitor.red = ColorComponent.red((byte) 12);
        visitor.green = ColorComponent.green((byte) 34);
        visitor.color = Color.fromRgb(0x0c2200);

        this.toStringAndCheck(visitor, "red=0c green=22 color=#0c2200");
    }

    @Test
    public void testToString2() {
        final RgbFunctionParserTokenVisitor visitor = new RgbFunctionParserTokenVisitor();
        visitor.red = ColorComponent.red((byte) 12);
        this.toStringAndCheck(visitor, "red=0c");
    }

    @Override
    public RgbFunctionParserTokenVisitor createVisitor() {
        return new RgbFunctionParserTokenVisitor();
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<RgbFunctionParserTokenVisitor> type() {
        return RgbFunctionParserTokenVisitor.class;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return ParserTokenVisitor.class.getSimpleName();
    }
}
