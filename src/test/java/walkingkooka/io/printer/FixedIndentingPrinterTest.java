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
import walkingkooka.text.Indentation;

final public class FixedIndentingPrinterTest
        extends IndentingPrinterTemplateTestCase<FixedIndentingPrinter> {

    // constants

    private final static Indentation INDENTATION = Indentation.with("*..");

    private final static Indentation INDENTATION2 = Indentation.with(">");

    private final static Printer PRINTER = Printers.fake();

    // tests

    @Test
    public void testWrapNullIndentationFails() {
        try {
            FixedIndentingPrinter.wrap(Printers.fake(), null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testPrintWithIndent() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1", builder.toString());
    }

    @Test
    public void testIndentNotImmediate() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.print("before");
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("after\n");
        printer.print("next");
        assertEquals("beforeafter\n" + FixedIndentingPrinterTest.INDENTATION + "next",
                builder.toString());
    }

    @Test
    public void testAutoIndentWhenCarriageReturnWritten() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1\r");
        printer.print("line2\r");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\r"
                + FixedIndentingPrinterTest.INDENTATION + "line2\r", builder.toString());
    }

    @Test
    public void testAutoIndentWhenNewLineWritten() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1\n");
        printer.print("line2\n");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\n"
                + FixedIndentingPrinterTest.INDENTATION + "line2\n", builder.toString());
    }

    @Test
    public void testAutoIndentedWhenCarriageReturnNewLineWritten() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1\r\n");
        printer.print("line2\r\n");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\r\n"
                + FixedIndentingPrinterTest.INDENTATION + "line2\r\n", builder.toString());
    }

    @Test
    public void testWithManyLines() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1\n");
        printer.print("lin");
        printer.print("e2\n");
        printer.print("\n\n");

        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\n" + // )
                FixedIndentingPrinterTest.INDENTATION + "line2\n" + //
                FixedIndentingPrinterTest.INDENTATION + "\n" + //
                FixedIndentingPrinterTest.INDENTATION + "\n", builder.toString());
    }

    @Test
    public void testIndentOutdentThenPrint() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2");

        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\n" + // )
                "line2", builder.toString());
    }

    @Test
    public void testIndentOutdentIndentOutdentThenPrint() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2\n");
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line3\n");
        printer.outdent();
        printer.print("line4");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\n" + // )
                "line2\n" + //
                FixedIndentingPrinterTest.INDENTATION + "line3\n" + //
                "line4", builder.toString());
    }

    @Test
    public void testOutdentNotImmediate() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("before");
        printer.outdent();
        printer.print("after\n");
        printer.print("next");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "beforeafter\nnext",
                builder.toString());
    }

    @Test
    public void testNestedIndents() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(Indentation.with("-"));
        printer.print("line1\n");
        printer.indent(FixedIndentingPrinterTest.INDENTATION2);
        printer.print("line2\n");
        printer.print("line3\r");
        printer.outdent();
        printer.print("line4\n");
        printer.outdent();
        printer.print("line5");
        assertEquals(FixedIndentingPrinterTest.INDENTATION + "line1\n" + //
                FixedIndentingPrinterTest.INDENTATION + FixedIndentingPrinterTest.INDENTATION
                + "line2\n" + //
                FixedIndentingPrinterTest.INDENTATION + FixedIndentingPrinterTest.INDENTATION
                + "line3\r" + //
                FixedIndentingPrinterTest.INDENTATION + "line4\n" + //
                "line5", builder.toString());
    }

    @Test
    public void testLineStartAfterIndent() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.print("line1");
        printer.indent(Indentation.with("!"));
        printer.lineStart();
        printer.print("line2\n");

        assertEquals("line1\r" + FixedIndentingPrinterTest.INDENTATION + "line2\n",
                builder.toString());
    }

    @Test
    public void testToString() {
        assertEquals(FixedIndentingPrinterTest.PRINTER + " indent=\""
                        + FixedIndentingPrinterTest.INDENTATION + "\"",
                FixedIndentingPrinter.wrap(FixedIndentingPrinterTest.PRINTER,
                        FixedIndentingPrinterTest.INDENTATION).toString());
    }

    @Test
    public void testToStringEscaped() {
        assertEquals(FixedIndentingPrinterTest.PRINTER + " indent=\"\\t..\"",
                FixedIndentingPrinter.wrap(FixedIndentingPrinterTest.PRINTER,
                        Indentation.with("\t..")).toString());
    }

    @Override
    FixedIndentingPrinter createPrinter(final Printer printer) {
        return FixedIndentingPrinter.wrap(printer, FixedIndentingPrinterTest.INDENTATION);
    }

    @Override
    protected Class<FixedIndentingPrinter> type() {
        return FixedIndentingPrinter.class;
    }
}
