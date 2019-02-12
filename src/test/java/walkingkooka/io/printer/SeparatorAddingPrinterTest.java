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
import walkingkooka.Cast;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class SeparatorAddingPrinterTest extends PrinterTestCase2<SeparatorAddingPrinter> {

    // constants
    private final static String SEPARATOR = ",";

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            SeparatorAddingPrinter.wrap(null, ",");
        });
    }

    @Test
    public void testWrapNullSeparatorFails() {
        assertThrows(NullPointerException.class, () -> {
            SeparatorAddingPrinter.wrap(Printers.fake(), null);
        });
    }

    @Test
    public void testDoesntWrapWithEmptySeparator() {
        final Printer printer = Printers.fake();
        assertSame(printer, SeparatorAddingPrinter.wrap(printer, ""));
    }

    @Test
    public void testSinglePrint() {
        this.printAndCheck("only");
    }

    @Test
    public void testManyPrints() {
        final StringBuilder expected = new StringBuilder();
        final StringBuilder printed = new StringBuilder();
        final SeparatorAddingPrinter printer = this.createPrinter(printed, SEPARATOR);

        expected.append("first");
        expected.append(SEPARATOR);
        printer.print("first");

        expected.append("second");
        expected.append(SEPARATOR);
        printer.print("second");

        expected.append("third");
        printer.print("third");

        checkEquals(expected.toString(), printed.toString());
    }

    @Test
    public void testManyPrints2() {
        final StringBuilder expected = new StringBuilder();
        final String separator = "|";

        final StringBuilder printed = new StringBuilder();
        final SeparatorAddingPrinter printer = this.createPrinter(printed, separator);
        expected.append("first");
        expected.append(separator);
        printer.print("first");

        expected.append("second");
        expected.append(separator);
        printer.print("second");

        expected.append("third");
        printer.print("third");

        checkEquals(expected.toString(), printed.toString());
    }

    @Test
    public void testMixedCharsAndLineEndings() {
        final StringBuilder printed = new StringBuilder();
        final SeparatorAddingPrinter printer = this.createPrinter(printed);
        printer.print("1"); // 1,
        printer.print("2"); // 1,2
        printer.print(printer.lineEnding()); // 1,2\n

        printer.print("3"); // 1,2\n3
        printer.print("4"); // 1,2\n3,4
        printer.print(printer.lineEnding()); // 1,2\n3,4\n

        checkEquals("1,2" + LINE_ENDING + "3,4"
                + LINE_ENDING, printed.toString());
    }

    @Test
    public void testToString() {
        final Printer wrapped = Printers.fake();
        checkEquals("separate prints with \",\" to " + wrapped,
                SeparatorAddingPrinter.wrap(wrapped, ",").toString());
    }

    @Override
    public SeparatorAddingPrinter createPrinter(final StringBuilder builder) {
        return this.createPrinter(Printers.stringBuilder(builder,
                LINE_ENDING));
    }

    private SeparatorAddingPrinter createPrinter(final StringBuilder builder,
                                                 final String separator) {
        return this.createPrinter(Printers.stringBuilder(builder,
                LINE_ENDING), separator);
    }

    private SeparatorAddingPrinter createPrinter(final Printer printer) {
        return this.createPrinter(printer, SEPARATOR);
    }

    private SeparatorAddingPrinter createPrinter(final Printer printer, final String separator) {
        return Cast.to(SeparatorAddingPrinter.wrap(printer, separator));
    }

    @Override
    public Class<SeparatorAddingPrinter> type() {
        return SeparatorAddingPrinter.class;
    }
}
