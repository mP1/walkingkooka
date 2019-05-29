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
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

public final class ColorHslOrHsvTest implements ClassTesting2<ColorHslOrHsv>,
        ParseStringTesting<ColorHslOrHsv> {

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
    public void testParseHsv() {
        this.parseAndCheck("hsv(359, 0.0, 0.25)",
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
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
