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
import walkingkooka.color.Hsl;
import walkingkooka.color.HslComponent;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.type.MemberVisibility;

public final class HslFunctionParserTokenVisitorTest implements ColorParserTokenVisitorTesting<HslFunctionParserTokenVisitor, HslParserToken> {

    @Test
    public void testToString() {
        final HslFunctionParserTokenVisitor visitor = new HslFunctionParserTokenVisitor();
        visitor.hue = HslComponent.hue(359);
        visitor.saturation = HslComponent.saturation(0.25f);
        visitor.hsl = Hsl.with(visitor.hue, visitor.saturation, HslComponent.lightness(0.5f));

        this.toStringAndCheck(visitor, "hue=359.0 saturation=0.25 hsl=359.0,0.25,0.5");
    }

    @Test
    public void testToString2() {
        final HslFunctionParserTokenVisitor visitor = new HslFunctionParserTokenVisitor();
        visitor.hue = HslComponent.hue(358);
        this.toStringAndCheck(visitor, "hue=358.0");
    }

    @Override
    public HslFunctionParserTokenVisitor createVisitor() {
        return new HslFunctionParserTokenVisitor();
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<HslFunctionParserTokenVisitor> type() {
        return HslFunctionParserTokenVisitor.class;
    }

    @Override
    public String typeNamePrefix() {
        return Hsl.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return ParserTokenVisitor.class.getSimpleName();
    }
}
