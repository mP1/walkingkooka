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

package walkingkooka.text.spreadsheetformat;

import org.junit.Test;
import walkingkooka.color.Color;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class TextSpreadsheetTextFormatterTest extends SpreadsheetTextFormatterTestCase<TextSpreadsheetTextFormatter, String> {

    private final static String TEXT = "Abc123";


    @Test(expected = IllegalArgumentException.class)
    public void testPatternWithConditionFails() {
        TextSpreadsheetTextFormatter.parse("[<100]@");
    }

    @Test
    public void testEmptyPatternParseFails() {
        assertEquals(TextSpreadsheetTextFormatter.EMPTY, TextSpreadsheetTextFormatter.parse("").format(TEXT, this.createContext()));
    }

    @Test
    public void testPlaceholder() {
        this.parseFormatAndCheck("@", TEXT, TEXT);
    }

    @Test
    public void testQuotedTextAndPlaceholder() {
        final String quoted = "Quoted456";
        this.parseFormatAndCheck("@\"" + quoted + "\"@", TEXT, TEXT + quoted + TEXT);
    }

    @Test
    public void testTextAndUnderscore() {
        this.parseFormatAndCheck("@_A",
                TEXT,
                TEXT + "A");
    }

    @Test
    public void testTextAndStar() {
        this.parseFormatAndCheck("@*A",
                TEXT,
                new FakeSpreadsheetTextFormatContext() {
                    @Override
                    public int width() {
                        return TEXT.length() + 3;
                    }
                },
                TEXT + "AAA");
    }

    @Test
    public void testTextAndLeftAndRightParens() {
        this.parseFormatAndCheck("(@)",
                TEXT,
                "(" + TEXT + ")");
    }

    @Test
    public void testTextAndEscaped() {
        this.parseFormatAndCheck("@\\B",
                TEXT,
                TEXT + "B");
    }

    @Test
    public void testTextAndColorName() {
        final String colorName = "RED";
        final Color color = Color.fromRgb(0x0FF);

        this.parseFormatAndCheck("[" + colorName + "]@",
                TEXT,
                new FakeSpreadsheetTextFormatContext() {
                    @Override
                    public Color colorName(final String n) {
                        assertEquals("name", colorName, n);
                        return color;
                    }
                },
                TEXT, color);
    }

    @Test
    public void testTextAndColorNumber() {
        final int colorNumber = 12;
        final Color color = Color.fromRgb(0x0FF);

        this.parseFormatAndCheck("[COLOR " + colorNumber + "]@",
                TEXT,
                new FakeSpreadsheetTextFormatContext() {
                    @Override
                    public Color colorNumber(final int n) {
                        assertEquals("name", colorNumber, n);
                        return color;
                    }
                },
                TEXT, color);
    }

    private void parseFormatAndCheck(final String pattern,
                                     final String value,
                                     final String text) {
        this.parseFormatAndCheck(pattern, value, this.createContext(), text);
    }

    private void parseFormatAndCheck(final String pattern,
                                     final String value,
                                     final SpreadsheetTextFormatContext context,
                                     final String text) {
        this.parseFormatAndCheck0(pattern, value, context, SpreadsheetFormattedText.with(SpreadsheetFormattedText.WITHOUT_COLOR, text));
    }

    private void parseFormatAndCheck(final String pattern,
                                     final String value,
                                     final SpreadsheetTextFormatContext context,
                                     final String text,
                                     final Color color) {
        this.parseFormatAndCheck0(pattern, value, context, SpreadsheetFormattedText.with(Optional.of(color), text));
    }

    private void parseFormatAndCheck0(final String pattern,
                                      final String value,
                                      final SpreadsheetTextFormatContext context,
                                      final SpreadsheetFormattedText text) {
        assertEquals("Pattern=" + CharSequences.quote(pattern) + " TEXT=" + CharSequences.quote(value),
                Optional.of(text),
                TextSpreadsheetTextFormatter.parse(pattern).format(value, context));
    }


    @Override
    protected TextSpreadsheetTextFormatter createFormatter() {
        return TextSpreadsheetTextFormatter.parse("@");
    }

    @Override
    protected String value() {
        return "Text123";
    }

    @Override
    protected SpreadsheetTextFormatContext createContext() {
        return SpreadsheetTextFormatContexts.fake();
    }

    @Override
    protected Class<TextSpreadsheetTextFormatter> type() {
        return TextSpreadsheetTextFormatter.class;
    }
}
