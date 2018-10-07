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
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class SpreadsheetFormattedTextTest extends PublicClassTestCase<SpreadsheetFormattedText> {

    private final static Optional<Color> COLOR = Optional.of(Color.BLACK);
    private final static String TEXT = "1/1/2000";

    @Test(expected = NullPointerException.class)
    public void testWithNullColorFails() {
        SpreadsheetFormattedText.with(null, TEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        SpreadsheetFormattedText.with(COLOR, null);
    }

    @Test
    public void testWith() {
        final SpreadsheetFormattedText formatted = this.createFormattedText();
        this.check(formatted, COLOR, TEXT);
    }

    @Test
    public void testWithEmptyColor() {
        this.createAndCheck(SpreadsheetFormattedText.WITHOUT_COLOR, TEXT);
    }

    @Test
    public void testWithEmptyText() {
        this.createAndCheck(COLOR, "");
    }

    private void createAndCheck(final Optional<Color> color, final String text) {
        final SpreadsheetFormattedText formatted = SpreadsheetFormattedText.with(color, text);
        this.check(formatted, color, text);
    }

    // setText...........................................................

    @Test(expected = NullPointerException.class)
    public void testSetTextNullFails() {
        this.createFormattedText().setText(null);
    }

    @Test
    public void testSetTextSame() {
        final SpreadsheetFormattedText formatted = this.createFormattedText();
        assertSame(formatted, formatted.setText(TEXT));
    }

    @Test
    public void testSetTextDifferent() {
        final String differentText = "different";
        final SpreadsheetFormattedText formatted = this.createFormattedText();
        final SpreadsheetFormattedText different = formatted.setText(differentText);
        assertNotSame(formatted, different);
        this.check(different, COLOR, differentText);
    }

    // setColor...........................................................

    @Test(expected = NullPointerException.class)
    public void testSetColorNullFails() {
        this.createFormattedText().setColor(null);
    }

    @Test
    public void testSetColorSame() {
        final SpreadsheetFormattedText formatted = this.createFormattedText();
        assertSame(formatted, formatted.setColor(COLOR));
    }

    @Test
    public void testSetColorDifferent() {
        final Optional<Color> differentColor = Optional.of(Color.fromRgb(123));
        final SpreadsheetFormattedText formatted = this.createFormattedText();
        final SpreadsheetFormattedText different = formatted.setColor(differentColor);
        assertNotSame(formatted, different);
        this.check(different, differentColor, TEXT);
    }

    private void check(final SpreadsheetFormattedText formatted, final Optional<Color> color, final String text) {
        assertEquals("color", color, formatted.color());
        assertEquals("text", text, formatted.text());
    }

    @Test
    public void testToString() {
        assertEquals(COLOR.get() + " " + CharSequences.quote(TEXT), this.createFormattedText().toString());
    }

    @Test
    public void testToStringWithoutColor() {
        assertEquals(CharSequences.quote(TEXT).toString(), SpreadsheetFormattedText.with(SpreadsheetFormattedText.WITHOUT_COLOR, TEXT).toString());
    }

    private SpreadsheetFormattedText createFormattedText() {
        return SpreadsheetFormattedText.with(COLOR, TEXT);
    }

    @Override
    protected Class<SpreadsheetFormattedText> type() {
        return SpreadsheetFormattedText.class;
    }
}
