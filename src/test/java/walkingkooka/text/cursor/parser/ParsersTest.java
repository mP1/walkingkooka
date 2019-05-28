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
 */
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.color.ColorComponent;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.PublicStaticHelperTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ParsersTest implements ClassTesting2<Parsers>,
        PublicStaticHelperTesting<Parsers> {

    // rgba(1,2,3).......................................................................................................

    @Test
    public void testParseRgbaFunctionIncompleteFails() {
        this.parseFails(Parsers.rgbaFunction(),
                "rgba(1",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaFunctionMissingParensRightFails() {
        this.parseFails(Parsers.rgbaFunction(),
                "rgba(1,2,3,0.5",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbaFunction() {
        this.parseRgbaAndCheck("rgba(1,2,3,0.5)", 1, 2, 3, 127);
    }

    @Test
    public void testParseRgbaFunction2() {
        this.parseRgbaAndCheck("rgba(12,34,56,0.5)", 12, 34, 56, 127);
    }

    @Test
    public void testParseRgbaFunction3() {
        this.parseRgbaAndCheck("rgba(99,128,255,0.5)", 99, 128, 255, 127);
    }

    @Test
    public void testParseRgbaFunction4() {
        this.parseRgbaAndCheck("rgba(0,0,0,0)", 0, 0, 0, 0);
    }

    @Test
    public void testParseRgbaFunction5() {
        this.parseRgbaAndCheck("rgba(255,254,253,1.0)", 255, 254, 253, 255);
    }

    @Test
    public void testParseRgbaFunctionExtraWhitespace() {
        this.parseRgbaAndCheck("rgba( 1,2 , 3, 0 )", 1, 2, 3, 0);
    }

    private void parseRgbaAndCheck(final String text,
                                   final int red,
                                   final int green,
                                   final int blue,
                                   final int alpha) {
        this.parseAndCheck(
                Parsers.rgbaFunction(),
                text,
                Color.with(
                        ColorComponent.red((byte) red),
                        ColorComponent.green((byte) green),
                        ColorComponent.blue((byte) blue))
                        .set(ColorComponent.alpha((byte) alpha)));
    }

    // rgb(1,2,3).......................................................................................................

    @Test
    public void testParseRgbFunctionIncompleteFails() {
        this.parseFails(Parsers.rgbFunction(),
                "rgb(1",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbFunctionMissingParensRightFails() {
        this.parseFails(Parsers.rgbFunction(),
                "rgb(1,2,3",
                ParserReporterException.class);
    }

    @Test
    public void testParseRgbFunction() {
        this.parseRgbAndCheck("rgb(1,2,3)", 1, 2, 3);
    }

    @Test
    public void testParseRgbFunction2() {
        this.parseRgbAndCheck("rgb(12,34,56)", 12, 34, 56);
    }

    @Test
    public void testParseRgbFunction3() {
        this.parseRgbAndCheck("rgb(99,128,255)", 99, 128, 255);
    }

    @Test
    public void testParseRgbFunctionExtraWhitespace() {
        this.parseRgbAndCheck("rgb( 1,2 , 3 )", 1, 2, 3);
    }

    private void parseRgbAndCheck(final String text, final int red, final int green, final int blue) {
        this.parseAndCheck(Parsers.rgbFunction(),
                text,
                Color.with(ColorComponent.red((byte) red),
                        ColorComponent.green((byte) green),
                        ColorComponent.blue((byte) blue)));
    }

    // helpers..........................................................................................................

    private void parseFails(final Parser<ParserContext> parser,
                            final String text,
                            final Class<? extends Throwable> thrown) {
        assertThrows(thrown, () -> {
            parser.orReport(ParserReporters.basic())
                    .parse(TextCursors.charSequence(text), parserContext());
        });
    }

    private void parseAndCheck(final Parser<ParserContext> parser,
                               final String text,
                               final Color color) {
        assertEquals(color,
                parser.orReport(ParserReporters.basic())
                        .parse(TextCursors.charSequence(text),
                                this.parserContext())
                        .map(t -> ColorParserToken.class.cast(t).value())
                        .orElseThrow(() -> new AssertionError()),
                () -> "parse " + CharSequences.quoteAndEscape(text));
    }

    private ParserContext parserContext() {
        return ParserContexts.basic(DecimalNumberContexts.basic("$", '.', 'E', ',', '-', '%', '+'));
    }

    @Override
    public Class<Parsers> type() {
        return Parsers.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
