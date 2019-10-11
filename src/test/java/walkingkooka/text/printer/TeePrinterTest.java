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
import walkingkooka.Cast;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class TeePrinterTest extends PrinterTestCase<TeePrinter> {

    // constants

    private final static Printer PRINTER = createContractPrinter();

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullFirstPrinterFails() {
        assertThrows(NullPointerException.class, () -> TeePrinter.wrap(null, PRINTER));
    }

    @Test
    public void testWrapNullSecondPrinterFails() {
        assertThrows(NullPointerException.class, () -> TeePrinter.wrap(PRINTER, null));
    }

    @Test
    public void testDoesntWrapTwoEqualPrinters() {
        assertSame(PRINTER,
                TeePrinter.wrap(PRINTER, PRINTER));
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
                LINE_ENDING),
                Printers.stringBuilder(builder2, LINE_ENDING));
        printer.print("string");
        builder3.append("string");

        printer.print("");

        printer.print(null);
        builder3.append((String) null);

        printer.print("string2");
        builder3.append("string2");
        checkEquals(builder3.toString(), builder1.toString());
        checkEquals(builder1.toString(), builder2.toString());
    }

    @Test
    public void testPrintWithPrintLineEnding() {
        final StringBuilder builder1 = new StringBuilder();
        final StringBuilder builder2 = new StringBuilder();
        final StringBuilder builder3 = new StringBuilder();
        final Printer printer = TeePrinter.wrap(Printers.stringBuilder(builder1,
                LINE_ENDING),
                Printers.stringBuilder(builder2, LINE_ENDING));
        printer.print("string");
        builder3.append("string");

        printer.print(printer.lineEnding());
        builder3.append(LINE_ENDING);

        printer.print("");

        printer.print(null);
        builder3.append((String) null);

        printer.print("string2");
        builder3.append("string2");
        checkEquals(builder3.toString(), builder1.toString());
        checkEquals(builder1.toString(), builder2.toString());
    }

    @Test
    public void testToString() {
        final Printer left = Printers.fake();
        final Printer right = Printers.fake();
        checkEquals("tee (" + left + " AND " + right + ")",
                TeePrinter.wrap(left, right).toString());
    }

    @Override
    public TeePrinter createPrinter() {
        return Cast.to(TeePrinter.wrap(createContractPrinter(), createContractPrinter()));
    }

    @Override
    public Class<TeePrinter> type() {
        return TeePrinter.class;
    }
}
