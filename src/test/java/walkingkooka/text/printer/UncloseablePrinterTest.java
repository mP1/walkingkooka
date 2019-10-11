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

package walkingkooka.text.printer;

import org.junit.jupiter.api.Test;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class UncloseablePrinterTest extends PrinterTestCase2<UncloseablePrinter> {

    // constants

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> UncloseablePrinter.wrap(null));
    }

    @Test
    public void testDoesntDoubleWrap() {
        final UncloseablePrinter printer = this.createPrinter();
        assertSame(printer, UncloseablePrinter.wrap(printer));
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
        final UncloseablePrinter printer = this.createPrinter(printed);
        printer.print("123");
        printer.print(printer.lineEnding());
        printer.print("456");
        printer.print(printer.lineEnding());
        checkEquals("123" + LINE_ENDING + "456" + LINE_ENDING, printed.toString());
    }

    @Override
    @Test
    public void testPrintAfterCloseFails() {
        final StringBuilder printed = new StringBuilder();
        final StringBuilder expected = new StringBuilder();

        final UncloseablePrinter printer = this.createPrinter(printed);

        final String before = "BEFORE";
        printer.print(before);
        expected.append(before);

        printer.close();

        final String after = "AFTER";
        printer.print(after);
        expected.append(after);

        printer.close();

        checkEquals(expected.toString(), printed.toString());
    }

    @Override
    @Test
    public void testLineEndingAfterCloseFails() {
        // nop
    }

    @Override
    @Test
    public void testFlushAfterCloseFails() {
        final UncloseablePrinter printer = this.createPrinter();
        printer.close();
        printer.flush();
        printer.close();
    }

    @Override
    @Test
    public void testUncloseable() {
        final Printer printer = this.createPrinter();
        assertSame(printer, printer.uncloseable());
    }

    @Override
    public UncloseablePrinter createPrinter(final StringBuilder target) {
        return UncloseablePrinter.wrap(Printers.stringBuilder(target,
                LINE_ENDING));
    }

    @Override
    public Class<UncloseablePrinter> type() {
        return UncloseablePrinter.class;
    }
}
