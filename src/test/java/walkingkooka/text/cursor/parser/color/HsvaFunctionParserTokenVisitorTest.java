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
import walkingkooka.color.Hsv;
import walkingkooka.color.HsvComponent;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;
import walkingkooka.type.MemberVisibility;

public final class HsvaFunctionParserTokenVisitorTest implements ColorParserTokenVisitorTesting<HsvaFunctionParserTokenVisitor, HsvParserToken> {

    @Test
    public void testToString() {
        final HsvaFunctionParserTokenVisitor visitor = new HsvaFunctionParserTokenVisitor();
        visitor.hue = HsvComponent.hue(359);
        visitor.saturation = HsvComponent.saturation(0.25f);
        visitor.visit0(0.5f);
        visitor.visit0(0.75f);

        this.toStringAndCheck(visitor, "hue=359 saturation=25% value=50% hsv=hsva(359,25%,50%,75%)");
    }

    @Test
    public void testToString2() {
        final HsvaFunctionParserTokenVisitor visitor = new HsvaFunctionParserTokenVisitor();
        visitor.hue = HsvComponent.hue(358);
        this.toStringAndCheck(visitor, "hue=358");
    }

    @Override
    public HsvaFunctionParserTokenVisitor createVisitor() {
        return new HsvaFunctionParserTokenVisitor();
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<HsvaFunctionParserTokenVisitor> type() {
        return HsvaFunctionParserTokenVisitor.class;
    }

    @Override
    public String typeNamePrefix() {
        return Hsv.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return ParserTokenVisitor.class.getSimpleName();
    }
}
