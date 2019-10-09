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
import walkingkooka.text.Indentation;

final public class IndentingPrinter2DefaultTest
        extends IndentingPrinter2TestCase<IndentingPrinter2Default> {

    private final static Indentation INDENTATION = Indentation.with(">");

    @Test
    public void testPrintWithIndent() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1");
        checkEquals(">line1", printed.toString());
    }

    @Test
    public void testIndentNotImmediate() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.print("before");
        printer.indent(INDENTATION);
        printer.print("after\n");
        printer.print("next");
        checkEquals("beforeafter\n>next", printed.toString());
    }

    @Test
    public void testAutoIndentWhenCarriageReturnWritten() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1\r");
        printer.print("line2\r");
        checkEquals(">line1\r>line2\r", printed.toString());
    }

    @Test
    public void testAutoIndentWhenNewLineWritten() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1\n");
        printer.print("line2\n");
        checkEquals(">line1\n>line2\n", printed.toString());
    }

    @Test
    public void testAutoIndentedWhenCarriageReturnNewLineWritten() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1\r\n");
        printer.print("line2\r\n");
        checkEquals(">line1\r\n>line2\r\n", printed.toString());
    }

    @Test
    public void testWithManyLines() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1\n");
        printer.print("lin");
        printer.print("e2\n");
        printer.print("\n\n");

        checkEquals(">line1\n" + // )
                ">line2\n" + //
                ">\n" + //
                ">\n", printed.toString());
    }

    @Test
    public void testIndentThenOutdentThenPrint() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2");

        checkEquals(">line1\n" + // )
                "line2", printed.toString());
    }

    @Test
    public void testIndentOutdentIndentOutdentThenPrint() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2\n");
        printer.indent(INDENTATION);
        printer.print("line3\n");
        printer.outdent();
        printer.print("line4");
        checkEquals(">line1\n" + // )
                "line2\n" + //
                ">line3\n" + //
                "line4", printed.toString());
    }

    @Test
    public void testOutdentNotImmediate() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(INDENTATION);
        printer.print("before");
        printer.outdent();
        printer.print("after\n");
        printer.print("next");
        checkEquals(">beforeafter\nnext", printed.toString());
    }

    @Test
    public void testNestedIndents() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.indent(Indentation.with("-"));
        printer.print("line1\n");
        printer.indent(INDENTATION);
        printer.print("line2\n");
        printer.print("line3\r");
        printer.outdent();
        printer.print("line4\n");
        printer.outdent();
        printer.print("line5");
        checkEquals("-line1\n" + //
                "->line2\n" + //
                "->line3\r" + //
                "-line4\n" + //
                "line5", printed.toString());
    }

    @Test
    public void testPrintAndLineStartWithoutEol() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.print("line1");
        printer.lineStart();
        printer.print("line2");
        printer.lineStart();
        printer.print("line3");
        printer.lineStart();

        checkEquals("line1\rline2\rline3\r", printed.toString());
    }

    @Test
    public void testLineStartAfterIndent() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.print("line1");
        printer.indent(Indentation.with("!"));
        printer.lineStart();
        printer.print("line2");

        checkEquals("line1" + IndentingPrinter2TestCase.LINE_ENDING + "!line2",
                printed.toString());
    }

    @Test
    public void testMixedCharactersAndLineEnding() {
        final StringBuilder printed = new StringBuilder();
        final IndentingPrinter2Default printer = this.createPrinter(printed);
        printer.print("line1");
        printer.indent(Indentation.with("!"));
        printer.print(printer.lineEnding());
        printer.print("line2");

        checkEquals("line1" + IndentingPrinter2TestCase.LINE_ENDING + "!line2",
                printed.toString());
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        checkEquals(printer.toString(), IndentingPrinter2Default.wrap(printer).toString());
    }

    @Override
    IndentingPrinter2Default createPrinter(final Printer printer) {
        return IndentingPrinter2Default.wrap(printer);
    }

    @Override
    public Class<IndentingPrinter2Default> type() {
        return IndentingPrinter2Default.class;
    }
}
