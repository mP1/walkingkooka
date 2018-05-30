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

final public class UnclosablePrinterTest extends PrinterTestCase2<UnclosablePrinter> {

    // constants

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        try {
            UnclosablePrinter.wrap(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntDoubleWrap() {
        final UnclosablePrinter printer = this.createPrinter();
        assertSame(printer, UnclosablePrinter.wrap(printer));
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testPrint() {
        this.printAndCheck("123");
    }

    @Test
    public void testMixedCharsAndLineEndings() {
        final StringBuilder printed = new StringBuilder();
        final UnclosablePrinter printer = this.createPrinter(printed);
        printer.print("123");
        printer.print(printer.lineEnding());
        printer.print("456");
        printer.print(printer.lineEnding());
        assertEquals("123" + UnclosablePrinterTest.LINE_ENDING + "456"
                + UnclosablePrinterTest.LINE_ENDING, printed.toString());
    }

    @Override
    @Test
    public void testPrintAfterCloseFails() {
        final StringBuilder printed = new StringBuilder();
        final StringBuilder expected = new StringBuilder();

        final UnclosablePrinter printer = this.createPrinter(printed);

        final String before = "BEFORE";
        printer.print(before);
        expected.append(before);

        printer.close();

        final String after = "AFTER";
        printer.print(after);
        expected.append(after);

        printer.close();

        assertEquals("printed", expected.toString(), printed.toString());
    }

    @Override
    @Test
    public void testLineEndingAfterCloseFails() {
        // nop
    }

    @Override
    @Test
    public void testFlushAfterCloseFails() {
        final UnclosablePrinter printer = this.createPrinter();
        printer.close();
        printer.flush();
        printer.close();
    }

    @Override
    @Test
    public void testCloseAfterCloseFails() {
        final UnclosablePrinter printer = this.createPrinter();
        printer.close();
        printer.close();
    }

    @Override
    protected UnclosablePrinter createPrinter() {
        return this.createPrinter(new StringBuilder());
    }

    @Override
    protected UnclosablePrinter createPrinter(final StringBuilder target) {
        return UnclosablePrinter.wrap(Printers.stringBuilder(target,
                UnclosablePrinterTest.LINE_ENDING));
    }

    @Override
    protected Class<UnclosablePrinter> type() {
        return UnclosablePrinter.class;
    }
}
