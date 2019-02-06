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

package walkingkooka.io.printer;

import org.junit.jupiter.api.Test;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class PlainTextWithoutTagsPrinterTest
        extends PrinterTestCase2<PlainTextWithoutTagsPrinter> {

    // constants

    private final static String TEXT = "abc";

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            PlainTextWithoutTagsPrinter.wrap(null);
        });
    }

    @Test
    public void testEmptyString() {
        this.printAndCheck("", "");
    }

    @Test
    public void testText() {
        final String text = "plain-text";
        this.printAndCheck(text, text, PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextWithSpace() {
        final String text = "plain text";
        this.printAndCheck(text, text, PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testRemovesRedudantSpace() {
        final String text = "plain  text";
        this.printAndCheck(text, "plain text", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testRemovesRedudantSpace2() {
        final String text = "plain   text";
        this.printAndCheck(text, "plain text", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTag() {
        this.printAndCheck("<tag>", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTagNameWithNumber() {
        this.printAndCheck("<tag123>", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTagNameWithDash() {
        this.printAndCheck("<tag->", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testIncompleteTagName() {
        this.printAndCheck("<t", "", PlainTextWithoutTagsPrinterMode.TAG_NAME);
    }

    @Test
    public void testIncompleteTagName2() {
        this.printAndCheck("<tag", "", PlainTextWithoutTagsPrinterMode.TAG_NAME);
    }

    @Test
    public void testIncompleteTagIncludingWhitespace() {
        this.printAndCheck("<tag ", "", PlainTextWithoutTagsPrinterMode.INSIDE_TAG);
    }

    @Test
    public void testIncompleteDoubleQuotes() {
        this.printAndCheck("<tag \"inside double quotes",
                "",
                PlainTextWithoutTagsPrinterMode.DOUBLE_QUOTES);
    }

    @Test
    public void testIncompleteDoubleQuotesAtBacklash() {
        this.printAndCheck("<tag \"inside double quotes\\",
                "",
                PlainTextWithoutTagsPrinterMode.ESCAPING_INSIDE_DOUBLE_QUOTES);
    }

    @Test
    public void testIncompleteDoubleQuotesAfterEscaping() {
        this.printAndCheck("<tag \"inside double \\\" quotes",
                "",
                PlainTextWithoutTagsPrinterMode.DOUBLE_QUOTES);
    }

    @Test
    public void testIncompleteTagAfterDoubleQuotes() {
        this.printAndCheck("<tag \"double\" ", "", PlainTextWithoutTagsPrinterMode.INSIDE_TAG);
    }

    @Test
    public void testIncompleteSingleQuotes() {
        this.printAndCheck("<tag 'inside single quotes",
                "",
                PlainTextWithoutTagsPrinterMode.INSIDE_SINGLE_QUOTES);
    }

    @Test
    public void testIncompleteSingleQuotesAtBacklash() {
        this.printAndCheck("<tag 'inside single quotes\\",
                "",
                PlainTextWithoutTagsPrinterMode.ESCAPING_INSIDE_SINGLE_QUOTES);
    }

    @Test
    public void testIncompleteSingleQuotesAfterEscaping() {
        this.printAndCheck("<tag 'inside single \\\' quotes",
                "",
                PlainTextWithoutTagsPrinterMode.INSIDE_SINGLE_QUOTES);
    }

    @Test
    public void testIncompleteTagAfterSingleQuotes() {
        this.printAndCheck("<tag 'single' ", "", PlainTextWithoutTagsPrinterMode.INSIDE_TAG);
    }

    @Test
    public void testIncompleteTagAfterAttribute() {
        this.printAndCheck("<tag attribute ", "", PlainTextWithoutTagsPrinterMode.INSIDE_TAG);
    }

    @Test
    public void testIncompleteTagAfterAttribute2() {
        this.printAndCheck("<tag attribute", "", PlainTextWithoutTagsPrinterMode.INSIDE_TAG);
    }

    @Test
    public void testTagAtEndSlash() {
        this.printAndCheck("<tag/", "", PlainTextWithoutTagsPrinterMode.GREATER_THAN);
    }

    @Test
    public void testEmptyTag() {
        this.printAndCheck("<tag/>", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testEmptyTagThenText() {
        this.printAndCheck("<tag />" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTagThenText() {
        this.printAndCheck("<tag>" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTagWithDoubleQuotesThenText() {
        this.printAndCheck("<tag \"double\">" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTagWithSingleQuotesThenText() {
        this.printAndCheck("<tag 'single'>" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testEndTagSlash() {
        this.printAndCheck("</", "", PlainTextWithoutTagsPrinterMode.TAG_NAME_COMMENT_ETC);
    }

    @Test
    public void testIncompleteEndTagName() {
        this.printAndCheck("</tag", "", PlainTextWithoutTagsPrinterMode.TAG_NAME);
    }

    @Test
    public void testIncompleteEndTagName2() {
        this.printAndCheck("</tag ", "", PlainTextWithoutTagsPrinterMode.INSIDE_TAG);
    }

    @Test
    public void testEndTag() {
        this.printAndCheck("</tag>", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testEndTagNameIncludesDigits() {
        this.printAndCheck("</tag123 >", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testEndTagNameWithDash() {
        this.printAndCheck("</tag- >", "", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testEndTagThenText() {
        this.printAndCheck("</tag >" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testIncompleteComment() {
        this.printAndCheck("<!", "", PlainTextWithoutTagsPrinterMode.COMMENT);
    }

    @Test
    public void testIncompleteComment2() {
        this.printAndCheck("<!-", "", PlainTextWithoutTagsPrinterMode.COMMENT_DASH);
    }

    @Test
    public void testIncompleteComment3() {
        this.printAndCheck("<!--", "", PlainTextWithoutTagsPrinterMode.INSIDE_COMMENT);
    }

    @Test
    public void testInsideComment() {
        this.printAndCheck("<!-- inside", "", PlainTextWithoutTagsPrinterMode.INSIDE_COMMENT);
    }

    @Test
    public void testInsideComment2() {
        this.printAndCheck("<!-- inside - inside",
                "",
                PlainTextWithoutTagsPrinterMode.INSIDE_COMMENT);
    }

    @Test
    public void testInsideComment3() {
        this.printAndCheck("<!-- inside-",
                "",
                PlainTextWithoutTagsPrinterMode.END_COMMENT_DASH_DASH);
    }

    @Test
    public void testIncompleteEndComment() {
        this.printAndCheck("<!-- inside--", "", PlainTextWithoutTagsPrinterMode.GREATER_THAN);
    }

    @Test
    public void testInsideIncompleteEndComment() {
        this.printAndCheck("<!-- inside-- inside",
                "",
                PlainTextWithoutTagsPrinterMode.INSIDE_COMMENT);
    }

    @Test
    public void testTextAfterComment() {
        final String text = "text";
        this.printAndCheck("<!-- inside -->" + text, text, PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testAfterBold() {
        this.printAndCheck("<B", "", PlainTextWithoutTagsPrinterMode.BOLD);
    }

    @Test
    public void testAfterBold2() {
        this.printAndCheck("<b", "", PlainTextWithoutTagsPrinterMode.BOLD);
    }

    @Test
    public void testAfterEndBold() {
        this.printAndCheck("</B", "", PlainTextWithoutTagsPrinterMode.BOLD);
    }

    @Test
    public void testAfterEndBold2() {
        this.printAndCheck("</b", "", PlainTextWithoutTagsPrinterMode.BOLD);
    }

    @Test
    public void testTextAfterBold() {
        this.printAndCheck("<B>" + TEXT,
                "**" + TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextAfterBElement() {
        this.printAndCheck("<Bbbb>" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextAfterEndBold() {
        this.printAndCheck("</B>" + TEXT,
                "**" + TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextInsideBoldTag() {
        this.printAndCheck("<B>" + TEXT + "</b>",
                "**" + TEXT + "**",
                PlainTextWithoutTagsPrinterMode.INSERT_SPACE_BEFORE_TEXT);
    }

    @Test
    public void testAfterItalics() {
        this.printAndCheck("<I", "", PlainTextWithoutTagsPrinterMode.ITALICS);
    }

    @Test
    public void testAfterItalics2() {
        this.printAndCheck("<i", "", PlainTextWithoutTagsPrinterMode.ITALICS);
    }

    @Test
    public void testAfterEndItalics() {
        this.printAndCheck("</I", "", PlainTextWithoutTagsPrinterMode.ITALICS);
    }

    @Test
    public void testAfterEndItalics2() {
        this.printAndCheck("</i", "", PlainTextWithoutTagsPrinterMode.ITALICS);
    }

    @Test
    public void testTextAfterItalics() {
        this.printAndCheck("<I>" + TEXT,
                "*" + TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextAfterIIElement() {
        this.printAndCheck("<II>" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextAfterEndItalics() {
        this.printAndCheck("</I>" + TEXT,
                "*" + TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextInsideItalicsTag() {
        this.printAndCheck("<I>" + TEXT + "</I>",
                "*" + TEXT + "*",
                PlainTextWithoutTagsPrinterMode.INSERT_SPACE_BEFORE_TEXT);
    }

    @Test
    public void testAfterUnderline() {
        this.printAndCheck("<U", "", PlainTextWithoutTagsPrinterMode.UNDERLINE);
    }

    @Test
    public void testAfterUnderline2() {
        this.printAndCheck("<u", "", PlainTextWithoutTagsPrinterMode.UNDERLINE);
    }

    @Test
    public void testAfterEndUnderline() {
        this.printAndCheck("<U", "", PlainTextWithoutTagsPrinterMode.UNDERLINE);
    }

    @Test
    public void testAfterEndUnderline2() {
        this.printAndCheck("</u", "", PlainTextWithoutTagsPrinterMode.UNDERLINE);
    }

    @Test
    public void testTextAfterUnderline() {
        this.printAndCheck("<U>" + TEXT,
                "_" + TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextAfterUElementIgnored() {
        this.printAndCheck("<UU>" + TEXT,
                TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextAfterEndUnderline() {
        this.printAndCheck("</U>" + TEXT,
                "_" + TEXT,
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testTextInsideUnderlineTag() {
        this.printAndCheck("<U>" + TEXT + "</u>",
                "_" + TEXT + "_",
                PlainTextWithoutTagsPrinterMode.INSERT_SPACE_BEFORE_TEXT);
    }

    @Test
    public void testMixture() {
        this.printAndCheck("<!-- inside -->" + TEXT + "<tag>",
                TEXT,
                PlainTextWithoutTagsPrinterMode.INSERT_SPACE_BEFORE_TEXT);
    }

    @Test
    public void testMixture2() {
        this.printAndCheck(this.characterByCharacter("<!-- inside -->text<tag>after"), //
                "text after", //
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testMixture3() {
        this.printAndCheck(new CharSequence[]{"<!-", "-", "inside -", "-", ">T", "E<ta", "g att",
                        "ribute=\"", "va\\\'\\\"lue", "\"", ">X", "</tag",
                        ">T"}, //
                "TE X T", //
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testWithBold() {
        this.printAndCheck(new CharSequence[]{"<B", ">bol", "d", "e<", "/b", ">d"},//
                "**bolde** d", //
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testWithItalics() {
        this.printAndCheck(new CharSequence[]{"<I", ">it", "a", "li<", "/i", ">cs"},//
                "*itali* cs", //
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testWithUnderline() {
        this.printAndCheck(new CharSequence[]{"<U", ">und", "e", "rline<", "/U", ">d"},//
                "_underline_ d",//
                PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testSeparatedByTags() {
        this.printAndCheck("first<X>",
                "first",
                PlainTextWithoutTagsPrinterMode.INSERT_SPACE_BEFORE_TEXT);
    }

    @Test
    public void testSeparatedByTags2() {
        this.printAndCheck("first<X>second", "first second", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    @Test
    public void testSeparatedByTags3() {
        this.printAndCheck("first <X>second", "first second", PlainTextWithoutTagsPrinterMode.TEXT);
    }

    private void printAndCheck(final CharSequence print, final String expected,
                               final PlainTextWithoutTagsPrinterMode mode) {
        this.printAndCheck(new CharSequence[]{print}, expected, mode);
    }

    private void printAndCheck(final CharSequence[] print, final String expected,
                               final PlainTextWithoutTagsPrinterMode mode) {
        final StringBuilder target = new StringBuilder();
        this.printAndCheck(this.createPrinter(target), print, target, expected, mode);
    }

    private void printAndCheck(final PlainTextWithoutTagsPrinter printer,
                               final CharSequence[] print, final StringBuilder target, final String expected,
                               final PlainTextWithoutTagsPrinterMode mode) {
        this.printAndCheck(printer, print, target, expected);
        this.checkMode(printer, mode);
    }

    private void checkMode(final PlainTextWithoutTagsPrinter printer,
                           final PlainTextWithoutTagsPrinterMode mode) {
        assertSame(mode, printer.mode, "mode");
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        checkEquals("text w/out tags AND " + printer,
                PlainTextWithoutTagsPrinter.wrap(printer).toString());
    }

    @Override
    protected PlainTextWithoutTagsPrinter createPrinter() {
        return this.createPrinter(new StringBuilder());
    }

    @Override
    protected PlainTextWithoutTagsPrinter createPrinter(final StringBuilder builder) {
        return PlainTextWithoutTagsPrinter.wrap(Printers.stringBuilder(builder,
                LINE_ENDING));
    }

    @Override
    public Class<PlainTextWithoutTagsPrinter> type() {
        return PlainTextWithoutTagsPrinter.class;
    }
}
