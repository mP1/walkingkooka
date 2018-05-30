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
import walkingkooka.Cast;
import walkingkooka.text.LineEnding;

final public class TeePrinterTest extends PrinterTestCase<TeePrinter> {

    // constants

    private final static Printer PRINTER = PrinterTestCase.createFakePrinter();

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullFirstPrinterFails() {
        this.wrapFails(null, TeePrinterTest.PRINTER);
    }

    @Test
    public void testWrapNullSecondPrinterFails() {
        this.wrapFails(TeePrinterTest.PRINTER, null);
    }

    private void wrapFails(final Printer first, final Printer second) {
        try {
            TeePrinter.wrap(first, second);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapTwoEqualPrinters() {
        assertSame(TeePrinterTest.PRINTER,
                TeePrinter.wrap(TeePrinterTest.PRINTER, TeePrinterTest.PRINTER));
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testPrint() {
        final StringBuilder builder1 = new StringBuilder();
        final StringBuilder builder2 = new StringBuilder();
        final StringBuilder builder3 = new StringBuilder();
        final Printer printer = TeePrinter.wrap(Printers.stringBuilder(builder1,
                TeePrinterTest.LINE_ENDING),
                Printers.stringBuilder(builder2, TeePrinterTest.LINE_ENDING));
        printer.print("string");
        builder3.append("string");

        printer.print("");

        printer.print(null);
        builder3.append((String) null);

        printer.print("string2");
        builder3.append("string2");
        assertEquals(builder3.toString(), builder1.toString());
        assertEquals("Both Tee'd Printers should be equal.",
                builder1.toString(),
                builder2.toString());
    }

    @Test
    public void testPrintWithPrintLineEnding() {
        final StringBuilder builder1 = new StringBuilder();
        final StringBuilder builder2 = new StringBuilder();
        final StringBuilder builder3 = new StringBuilder();
        final Printer printer = TeePrinter.wrap(Printers.stringBuilder(builder1,
                TeePrinterTest.LINE_ENDING),
                Printers.stringBuilder(builder2, TeePrinterTest.LINE_ENDING));
        printer.print("string");
        builder3.append("string");

        printer.print(printer.lineEnding());
        builder3.append(TeePrinterTest.LINE_ENDING);

        printer.print("");

        printer.print(null);
        builder3.append((String) null);

        printer.print("string2");
        builder3.append("string2");
        assertEquals(builder3.toString(), builder1.toString());
        assertEquals("Both Tee'd Printers should be equal.",
                builder1.toString(),
                builder2.toString());
    }

    @Test
    public void testToString() {
        final Printer left = Printers.fake();
        final Printer right = Printers.fake();
        assertEquals("tee (" + left + " AND " + right + ")",
                TeePrinter.wrap(left, right).toString());
    }

    @Override
    protected TeePrinter createPrinter() {
        return Cast.to(TeePrinter.wrap(PrinterTestCase.createFakePrinter(),
                PrinterTestCase.createFakePrinter()));
    }

    @Override
    protected Class<TeePrinter> type() {
        return TeePrinter.class;
    }
}
