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

final public class LineEndingPrinterTest extends PrinterTestCase2<LineEndingPrinter> {

    // constants

    private final static Printer PRINTER = Printers.fake();

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            LineEndingPrinter.wrap(null);
        });
    }

    @Test
    public void testWrap() {
        final LineEndingPrinter printer = LineEndingPrinter.wrap(PRINTER);
        assertSame(PRINTER, printer.printer, "wrapped printer");
    }

    @Test
    public void testDoesntDoubleWrapLineEndingPrinter() {
        final LineEndingPrinter printer = LineEndingPrinter.wrap(PRINTER);
        final LineEndingPrinter printer2 = LineEndingPrinter.wrap(printer);
        assertSame(PRINTER, printer2.printer, "wrapped printer");
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
                LINE_ENDING + LINE_ENDING.toString(),
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
        checkEquals("1\r2" + LINE_ENDING + LINE_ENDING
                        + LINE_ENDING + "3\r4" + LINE_ENDING,
                builder.toString());
    }

    @Test
    public void testToString() {
        checkEquals(PRINTER.toString(),
                LineEndingPrinter.wrap(PRINTER).toString());
    }

    @Override
    public LineEndingPrinter createPrinter(final StringBuilder builder) {
        return LineEndingPrinter.wrap(Printers.stringBuilder(builder,
                LINE_ENDING));
    }

    @Override
    public Class<LineEndingPrinter> type() {
        return LineEndingPrinter.class;
    }
}
