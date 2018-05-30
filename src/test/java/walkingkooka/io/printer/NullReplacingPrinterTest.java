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
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

final public class NullReplacingPrinterTest extends PrinterTestCase<NullReplacingPrinter> {

    // constants

    private final static String REPLACEMENT = "null";

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        this.wrapFails(null, NullReplacingPrinterTest.REPLACEMENT);
    }

    @Test
    public void testWrapNullReplacementFails() {
        this.wrapFails(Printers.fake(), null);
    }

    private void wrapFails(final Printer printer, final String replacement) {
        try {
            NullReplacingPrinter.wrap(printer, replacement);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testWrapWithEmptyDefault() {
        NullReplacingPrinter.wrap(Printers.fake(), "");
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testNull() {
        final StringBuilder printed = new StringBuilder();
        final NullReplacingPrinter printer = this.createPrinter(printed);
        printer.print("before ");
        printer.print(null);
        printer.print(" after");

        assertEquals("before " + NullReplacingPrinterTest.REPLACEMENT + " after",
                printed.toString());
    }

    @Test
    public void testNullWhenDefaultingEmptyString() {
        final StringBuilder printed = new StringBuilder();
        final NullReplacingPrinter printer
                = NullReplacingPrinter.wrap(this.createStringBuilderPrinter(printed), "");
        printer.print("before ");
        printer.print(null);
        printer.print(" after");

        assertEquals("before  after", printed.toString());
    }

    @Test
    public void testMixedCharsAndLineEndings() {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder printed = new StringBuilder();
        final NullReplacingPrinter printer = this.createPrinter(printed);
        builder.append("before ");
        printer.print("before ");

        printer.print(null);
        builder.append(NullReplacingPrinterTest.REPLACEMENT);

        printer.print(printer.lineEnding());
        builder.append(NullReplacingPrinterTest.LINE_ENDING);

        builder.append(" after");
        printer.print(" after");

        assertEquals(builder.toString(), printed.toString());
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        assertEquals(
                "if null then print " + CharSequences.quote(NullReplacingPrinterTest.REPLACEMENT)
                        + " " + printer,
                NullReplacingPrinter.wrap(printer, NullReplacingPrinterTest.REPLACEMENT)
                        .toString());
    }

    @Override
    protected NullReplacingPrinter createPrinter() {
        return this.createPrinter(NullReplacingPrinterTest.REPLACEMENT);
    }

    private NullReplacingPrinter createPrinter(final String defaulting) {
        return NullReplacingPrinter.wrap(this.createStringBuilderPrinter(new StringBuilder()),
                defaulting);
    }

    private NullReplacingPrinter createPrinter(final StringBuilder printed) {
        return NullReplacingPrinter.wrap(this.createStringBuilderPrinter(printed),
                NullReplacingPrinterTest.REPLACEMENT);
    }

    private Printer createStringBuilderPrinter(final StringBuilder printed) {
        return Printers.stringBuilder(printed, NullReplacingPrinterTest.LINE_ENDING);
    }

    @Override
    protected Class<NullReplacingPrinter> type() {
        return NullReplacingPrinter.class;
    }
}
