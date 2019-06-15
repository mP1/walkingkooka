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

package walkingkooka.color;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

public final class ColorHslOrHsvTest implements ClassTesting2<ColorHslOrHsv>,
        HasJsonNodeTesting<ColorHslOrHsv>,
        ParseStringTesting<ColorHslOrHsv> {

    // parse...........................................................................................................

    @Test
    public void testParseFails() {
        this.parseFails("abc", IllegalArgumentException.class);
    }

    @Test
    public void testParseHsl() {
        this.parseAndCheck("hsl(359, 0%, 25%)",
                Hsl.with(HslComponent.hue(359f),
                        HslComponent.saturation(0.0f),
                        HslComponent.lightness(0.25f)));
    }

    @Test
    public void testParseHsla() {
        this.parseAndCheck("hsla(359, 0%, 25%, 50%)",
                Hsl.with(HslComponent.hue(359f),
                        HslComponent.saturation(0.0f),
                        HslComponent.lightness(0.25f))
                        .set(HslComponent.alpha(0.5f)));
    }

    @Test
    public void testParseHsv() {
        this.parseAndCheck("hsv(359, 0%, 25%)",
                Hsv.with(HsvComponent.hue(359f),
                        HsvComponent.saturation(0.0f),
                        HsvComponent.value(0.25f)));
    }

    @Test
    public void testParseRgb() {
        this.parseAndCheck("rgb(12,34,56)",
                Color.with(ColorComponent.red((byte) 12),
                        ColorComponent.green((byte) 34),
                        ColorComponent.blue((byte) 56)));
    }

    // fromJsonNode.....................................................................................................

    @Override
    public void testHasJsonNodeFactoryRegistered() {
    }

    @Test
    public void testFromJsonNodeFails() {
        this.fromJsonNodeFails("\"abc\"");
    }

    @Test
    public void testFromJsonNodeColor() {
        final Color color = Color.fromRgb(0x123456);
        this.fromJsonNodeAndCheck(color.toJsonNode(), color);
    }

    @Test
    public void testFromJsonNodeHsl() {
        final Hsl hsl = Hsl.with(HslComponent.hue(99),
                HslComponent.saturation(0.25f),
                HslComponent.lightness(0.75f));
        this.fromJsonNodeAndCheck(hsl.toJsonNode(), hsl);
    }

    @Test
    public void testFromJsonNodeHsv() {
        final Hsv hsv = Hsv.with(HsvComponent.hue(99),
                HsvComponent.saturation(0.25f),
                HsvComponent.value(0.75f));
        this.fromJsonNodeAndCheck(hsv.toJsonNode(), hsv);
    }

    // HasJsonNode.....................................................................................................

    @Override
    public ColorHslOrHsv fromJsonNode(final JsonNode from) {
        return ColorHslOrHsv.fromJsonNode(from);
    }

    @Override
    public ColorHslOrHsv createHasJsonNode() {
        return Color.fromArgb(0x123456);
    }

    // ParseStringTesting...............................................................................................

    @Override
    public ColorHslOrHsv parse(final String text) {
        return ColorHslOrHsv.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<ColorHslOrHsv> type() {
        return ColorHslOrHsv.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
