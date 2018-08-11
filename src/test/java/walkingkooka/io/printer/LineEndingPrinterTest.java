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

import static org.junit.Assert.assertSame;

final public class LineEndingPrinterTest extends PrinterTestCase2<LineEndingPrinter> {

    // constants

    private final static Printer PRINTER = Printers.fake();

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testNullPrinterFails() {
        try {
            LineEndingPrinter.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testWrap() {
        final LineEndingPrinter printer = LineEndingPrinter.wrap(LineEndingPrinterTest.PRINTER);
        assertSame("wrapped printer", LineEndingPrinterTest.PRINTER, printer.printer);
    }

    @Test
    public void testDoesntDoubleWrapLineEndingPrinter() {
        final LineEndingPrinter printer = LineEndingPrinter.wrap(LineEndingPrinterTest.PRINTER);
        final LineEndingPrinter printer2 = LineEndingPrinter.wrap(printer);
        assertSame("wrapped printer", LineEndingPrinterTest.PRINTER, printer2.printer);
    }

    @Test
    public void testLineEndingAppendedAfterNull() {
        this.printAndCheck((String) null, "null\n");
    }

    @Test
    public void testLineEndingAppended() {
        this.printAndCheck("123", "123\n");
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testPrintLineEnding() {
        final StringBuilder builder = new StringBuilder();
        final LineEndingPrinter printer = this.createPrinter(builder);
        printer.print(printer.lineEnding());
        printer.flush();
        checkEquals(
                LineEndingPrinterTest.LINE_ENDING + LineEndingPrinterTest.LINE_ENDING.toString(),
                builder.toString());
    }

    @Test
    public void testMixedPrinting() {
        final StringBuilder builder = new StringBuilder();
        final LineEndingPrinter printer = this.createPrinter(builder);
        printer.print("1\r2");
        printer.print(printer.lineEnding());
        printer.print("3\r4");
        printer.flush();
        checkEquals("1\r2" + LineEndingPrinterTest.LINE_ENDING + LineEndingPrinterTest.LINE_ENDING
                        + LineEndingPrinterTest.LINE_ENDING + "3\r4" + LineEndingPrinterTest.LINE_ENDING,
                builder.toString());
    }

    @Test
    public void testToString() {
        checkEquals(LineEndingPrinterTest.PRINTER.toString(),
                LineEndingPrinter.wrap(LineEndingPrinterTest.PRINTER).toString());
    }

    @Override
    protected LineEndingPrinter createPrinter() {
        return this.createPrinter(new StringBuilder());
    }

    @Override
    protected LineEndingPrinter createPrinter(final StringBuilder builder) {
        return LineEndingPrinter.wrap(Printers.stringBuilder(builder,
                LineEndingPrinterTest.LINE_ENDING));
    }

    @Override
    protected Class<LineEndingPrinter> type() {
        return LineEndingPrinter.class;
    }
}
