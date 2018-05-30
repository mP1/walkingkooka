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

final public class MaximumIndentationIndentingPrinterTest
        extends IndentingPrinterTemplateTestCase<MaximumIndentationIndentingPrinter> {

    // constants

    private final static int MAX = 5;

    private final static Indentation INDENTATION = Indentation.with(">");

    // tests

    @Test
    public void testWrapInvalidMaximumIndentationFails() {
        try {
            MaximumIndentationIndentingPrinter.wrap(Printers.fake(), -1);
            Assert.fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testPrintWithIndent() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1");
        assertEquals(">line1", printed.toString());
    }

    @Test
    public void testIndentNotImmediate() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before");
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("after\n");
        printer.print("next");
        assertEquals("beforeafter\n>next", printed.toString());
    }

    @Test
    public void testAutoIndentWhenCarriageReturnWritten() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1\r");
        printer.print("line2\r");
        assertEquals(">line1\r>line2\r", printed.toString());
    }

    @Test
    public void testAutoIndentWhenNewLineWritten() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1\n");
        printer.print("line2\n");
        assertEquals(">line1\n>line2\n", printed.toString());
    }

    @Test
    public void testAutoIndentedWhenCarriageReturnNewLineWritten() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1\r\n");
        printer.print("line2\r\n");
        assertEquals(">line1\r\n>line2\r\n", printed.toString());
    }

    @Test
    public void testWithManyLines() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1\n");
        printer.print("lin");
        printer.print("e2\n");
        printer.print("\n\n");

        assertEquals(">line1\n" + // )
                ">line2\n" + //
                ">\n" + //
                ">\n", printed.toString());
    }

    @Test
    public void testIndentThenOutdentThenPrint() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2");

        assertEquals(">line1\n" + // )
                "line2", printed.toString());
    }

    @Test
    public void testIndentOutdentIndentOutdentThenPrint() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2\n");
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line3\n");
        printer.outdent();
        printer.print("line4");
        assertEquals(">line1\n" + // )
                "line2\n" + //
                ">line3\n" + //
                "line4", printed.toString());
    }

    @Test
    public void testOutdentNotImmediate() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("before");
        printer.outdent();
        printer.print("after\n");
        printer.print("next");
        assertEquals(">beforeafter\nnext", printed.toString());
    }

    @Test
    public void testNestedIndents() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(Indentation.with("-"));
        printer.print("line1\n");
        printer.indent(MaximumIndentationIndentingPrinterTest.INDENTATION);
        printer.print("line2\n");
        printer.print("line3\r");
        printer.outdent();
        printer.print("line4\n");
        printer.outdent();
        printer.print("line5");
        assertEquals("-line1\n" + //
                "->line2\n" + //
                "->line3\r" + //
                "-line4\n" + //
                "line5", printed.toString());
    }

    @Test
    public void testIgnoresExtraIndentation() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(Indentation.with("1234567>")); // 1
        printer.print("line1\n");
        assertEquals("12345line1\n", printed.toString());
    }

    @Test
    public void testIgnoresExtraIndentation2() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.indent(Indentation.with("->")); // 1
        printer.print("line1\n");
        printer.indent(Indentation.with("=>")); // ->=>
        printer.print("line2\n");
        printer.indent(Indentation.with("->")); // ->=>->
        printer.print("line3\r");
        printer.outdent(); // ->=>
        printer.print("line4\n");
        printer.outdent(); // ->
        printer.print("line5");
        assertEquals("->line1\n" + //
                "->=>line2\n" + //
                "->=>-line3\r" + //
                "->=>line4\n" + //
                "->line5", printed.toString());
    }

    @Test
    public void testMaxIndentationZero() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = MaximumIndentationIndentingPrinter.wrap(
                this.createStringBuilderPrinter(printed),
                0);
        printer.indent(Indentation.with("->"));
        printer.print("line1\n");
        printer.indent(Indentation.with("=>"));
        printer.print("line2\n");
        printer.indent(Indentation.with("->"));
        printer.print("line3\r");
        printer.outdent();
        printer.print("line4\n");
        printer.outdent();
        printer.print("line5");
        assertEquals("line1\n" + //
                "line2\n" + //
                "line3\r" + //
                "line4\n" + //
                "line5", printed.toString());
    }

    @Test
    public void testLineStartAfterIndent() {
        final StringBuilder printed = new StringBuilder();
        final MaximumIndentationIndentingPrinter printer = this.createPrinter(printed);
        printer.print("line1");
        printer.indent(Indentation.with("!"));
        printer.lineStart();
        printer.print("line2\n");

        assertEquals("line1\r" + "!line2\n", printed.toString());
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        assertEquals(printer.toString() + " maxIndentation="
                        + MaximumIndentationIndentingPrinterTest.MAX,
                MaximumIndentationIndentingPrinter.wrap(printer,
                        MaximumIndentationIndentingPrinterTest.MAX).toString());
    }

    @Override
    MaximumIndentationIndentingPrinter createPrinter(final Printer printer) {
        return MaximumIndentationIndentingPrinter.wrap(printer,
                MaximumIndentationIndentingPrinterTest.MAX);
    }

    @Override
    protected Class<MaximumIndentationIndentingPrinter> type() {
        return MaximumIndentationIndentingPrinter.class;
    }
}
