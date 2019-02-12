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
import walkingkooka.text.Indentation;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class FixedIndentingPrinterTest
        extends IndentingPrinter2TestCase<FixedIndentingPrinter> {

    // constants

    private final static Indentation INDENTATION = Indentation.with("*..");

    private final static Indentation INDENTATION2 = Indentation.with(">");

    private final static Printer PRINTER = Printers.fake();

    // tests

    @Test
    public void testWrapNullIndentationFails() {
        assertThrows(NullPointerException.class, () -> {
            FixedIndentingPrinter.wrap(Printers.fake(), null);
        });
    }

    @Test
    public void testPrintWithIndent() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1");
        checkEquals(INDENTATION + "line1", builder.toString());
    }

    @Test
    public void testIndentNotImmediate() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.print("before");
        printer.indent(INDENTATION2);
        printer.print("after\n");
        printer.print("next");
        checkEquals("beforeafter\n" + INDENTATION + "next",
                builder.toString());
    }

    @Test
    public void testAutoIndentWhenCarriageReturnWritten() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1\r");
        printer.print("line2\r");
        checkEquals(INDENTATION + "line1\r"
                + INDENTATION + "line2\r", builder.toString());
    }

    @Test
    public void testAutoIndentWhenNewLineWritten() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1\n");
        printer.print("line2\n");
        checkEquals(INDENTATION + "line1\n"
                + INDENTATION + "line2\n", builder.toString());
    }

    @Test
    public void testAutoIndentedWhenCarriageReturnNewLineWritten() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1\r\n");
        printer.print("line2\r\n");
        checkEquals(INDENTATION + "line1\r\n"
                + INDENTATION + "line2\r\n", builder.toString());
    }

    @Test
    public void testWithManyLines() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1\n");
        printer.print("lin");
        printer.print("e2\n");
        printer.print("\n\n");

        checkEquals(INDENTATION + "line1\n" + // )
                INDENTATION + "line2\n" + //
                INDENTATION + "\n" + //
                INDENTATION + "\n", builder.toString());
    }

    @Test
    public void testIndentOutdentThenPrint() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2");

        checkEquals(INDENTATION + "line1\n" + // )
                "line2", builder.toString());
    }

    @Test
    public void testIndentOutdentIndentOutdentThenPrint() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2\n");
        printer.indent(INDENTATION2);
        printer.print("line3\n");
        printer.outdent();
        printer.print("line4");
        checkEquals(INDENTATION + "line1\n" + // )
                "line2\n" + //
                INDENTATION + "line3\n" + //
                "line4", builder.toString());
    }

    @Test
    public void testOutdentNotImmediate() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(INDENTATION2);
        printer.print("before");
        printer.outdent();
        printer.print("after\n");
        printer.print("next");
        checkEquals(INDENTATION + "beforeafter\nnext",
                builder.toString());
    }

    @Test
    public void testNestedIndents() {
        final StringBuilder builder = new StringBuilder();
        final FixedIndentingPrinter printer = this.createPrinter(builder);
        printer.indent(Indentation.with("-"));
        printer.print("line1\n");
        printer.indent(INDENTATION2);
        printer.print("line2\n");
        printer.print("line3\r");
        printer.outdent();
        printer.print("line4\n");
        printer.outdent();
        printer.print("line5");
        checkEquals(INDENTATION + "line1\n" + //
                INDENTATION + INDENTATION
                + "line2\n" + //
                INDENTATION + INDENTATION
                + "line3\r" + //
                INDENTATION + "line4\n" + //
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

        checkEquals("line1\r" + INDENTATION + "line2\n",
                builder.toString());
    }

    @Test
    public void testToString() {
        checkEquals(PRINTER + " indent=\""
                        + INDENTATION + "\"",
                FixedIndentingPrinter.wrap(PRINTER,
                        INDENTATION).toString());
    }

    @Test
    public void testToStringEscaped() {
        checkEquals(PRINTER + " indent=\"\\t..\"",
                FixedIndentingPrinter.wrap(PRINTER,
                        Indentation.with("\t..")).toString());
    }

    @Override
    FixedIndentingPrinter createPrinter(final Printer printer) {
        return FixedIndentingPrinter.wrap(printer, INDENTATION);
    }

    @Override
    public Class<FixedIndentingPrinter> type() {
        return FixedIndentingPrinter.class;
    }
}
