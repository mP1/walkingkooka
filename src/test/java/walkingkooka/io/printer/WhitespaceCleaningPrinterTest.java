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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.text.LineEnding;

final public class WhitespaceCleaningPrinterTest extends PrinterTestCase2<WhitespaceCleaningPrinter> {

    // constants

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        try {
            WhitespaceCleaningPrinter.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapWhitespaceCleaningPrinter() {
        final WhitespaceCleaningPrinter printer = WhitespaceCleaningPrinter.wrap(Printers.fake());
        assertSame(printer, WhitespaceCleaningPrinter.wrap(printer));
    }

    @Test
    public void testEmptyString() {
        this.printAndCheck(new String[]{"", "", ""}, "");
    }

    @Test
    public void testSequenceOfWhitespaceCharacters() {
        this.printAndCheck(new String[]{" ", " ", " "}, " ");
    }

    @Test
    public void testCarriageReturn() {
        this.printAndCheck("\r", " ");
    }

    @Test
    public void testNewLine() {
        this.printAndCheck("\n", " ");
    }

    @Test
    public void testCarriageReturnNewLine() {
        this.printAndCheck("\r\n", " ");
    }

    @Test
    public void testTab() {
        this.printAndCheck("\t", " ");
    }

    @Test
    public void testWithoutWhitespace() {
        this.printAndCheck("123abc");
    }

    @Test
    public void testSeparatedByWhitespace() {
        this.printAndCheck(new String[]{" ", "123", "  ", "456"}, " 123 456");
    }

    private final static String SOURCE = "123 \t\n456";

    private final static String EXPECTED = "123 456";

    @Test
    public void testSeparatedByWhitespace2() {
        this.printAndCheck(WhitespaceCleaningPrinterTest.SOURCE,
                WhitespaceCleaningPrinterTest.EXPECTED);
    }

    @Test
    public void testSeparatedByWhitespace3() {
        final StringBuilder builder = new StringBuilder();
        final WhitespaceCleaningPrinter printer
                = WhitespaceCleaningPrinter.wrap(Printers.stringBuilder(builder,
                WhitespaceCleaningPrinterTest.LINE_ENDING));

        for (int i = 0; i < WhitespaceCleaningPrinterTest.SOURCE.length(); i++) {
            printer.print(String.valueOf(WhitespaceCleaningPrinterTest.SOURCE.charAt(i)));
        }

        assertEquals(WhitespaceCleaningPrinterTest.EXPECTED, builder.toString());
    }

    @Test
    public void testSeparatedByCarriageReturns() {
        this.printAndCheck(new String[]{"123\r", "456\r"}, "123 456 ");
    }

    @Test
    public void testSeparatedByNewLines() {
        this.printAndCheck(new String[]{"123\n", "456\n"}, "123 456 ");
    }

    @Test
    public void testSeparatedByCarriageReturnNewLines() {
        this.printAndCheck(new String[]{"123\r\n", "456\r\n"}, "123 456 ");
    }

    @Test
    public void testSeparatedByCarriageReturnNewLinesAndWhitespace() {
        this.printAndCheck(new String[]{"123\r\n ", "456\r\n "}, "123 456 ");
    }

    @Test
    public void testPrintLineEndingAfterCr() {
        this.printLineEndingAfter("abc\r", "abc ", LineEnding.CR);
    }

    @Test
    public void testPrintLineEndingAfterNl() {
        this.printLineEndingAfter("abc\n", "abc ", LineEnding.CR);
    }

    @Test
    public void testPrintLineEndingAfterCrNl() {
        this.printLineEndingAfter("abc\r\n", "abc ", LineEnding.CR);
    }

    @Test
    public void testPrintLineEndingAfterSpace() {
        this.printLineEndingAfter("abc ", "abc ", LineEnding.CR);
    }

    @Test
    public void testPrintLineEndingAfterTab() {
        this.printLineEndingAfter("abc\t", "abc ", LineEnding.CR);
    }

    private void printLineEndingAfter(final String text, final String expected,
                                      final LineEnding lineEnding) {
        final StringBuilder printed = new StringBuilder();
        final WhitespaceCleaningPrinter printer
                = WhitespaceCleaningPrinter.wrap(Printers.stringBuilder(printed, lineEnding));
        printer.print(text);
        printer.print(printer.lineEnding());
        printer.print(printer.lineEnding());
        assertEquals(expected, printed.toString());
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        assertEquals("clean whitespace " + printer.toString(),
                WhitespaceCleaningPrinter.wrap(printer).toString());
    }

    @Override
    protected WhitespaceCleaningPrinter createPrinter(final StringBuilder target) {
        return WhitespaceCleaningPrinter.wrap(this.createStringBuilder(target));
    }

    private Printer createStringBuilder(final StringBuilder target) {
        return Printers.stringBuilder(target, WhitespaceCleaningPrinterTest.LINE_ENDING);
    }

    @Override
    protected Class<WhitespaceCleaningPrinter> type() {
        return WhitespaceCleaningPrinter.class;
    }
}
