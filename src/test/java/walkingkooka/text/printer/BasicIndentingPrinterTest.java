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
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class BasicIndentingPrinterTest
        extends PrinterTestCase2<BasicIndentingPrinter> implements IndentingPrinterTesting<BasicIndentingPrinter>, walkingkooka.reflect.TypeNameTesting<BasicIndentingPrinter> {

    protected final static LineEnding LINE_ENDING = LineEnding.CR;
    private final static Indentation INDENTATION = Indentation.with(">");

    @Test
    public void testPrintWithIndent() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1");
        checkEquals(">line1", printed.toString());
    }

    @Test
    public void testIndentNotImmediate() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before");
        printer.indent();
        printer.print("after\n");
        printer.print("next");
        checkEquals("beforeafter\n>next", printed.toString());
    }

    @Test
    public void testAutoIndentWhenCarriageReturnWritten() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1\r");
        printer.print("line2\r");
        checkEquals(">line1\r>line2\r", printed.toString());
    }

    @Test
    public void testAutoIndentWhenNewLineWritten() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1\n");
        printer.print("line2\n");
        checkEquals(">line1\n>line2\n", printed.toString());
    }

    @Test
    public void testAutoIndentedWhenCarriageReturnNewLineWritten() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1\r\n");
        printer.print("line2\r\n");
        checkEquals(">line1\r\n>line2\r\n", printed.toString());
    }

    @Test
    public void testWithManyLines() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
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
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2");

        checkEquals(">line1\n" + // )
                "line2", printed.toString());
    }

    @Test
    public void testIndentOutdentIndentOutdentThenPrint() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1\n");
        printer.outdent();
        printer.print("line2\n");
        printer.indent();
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
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("before");
        printer.outdent();
        printer.print("after\n");
        printer.print("next");
        checkEquals(">beforeafter\nnext", printed.toString());
    }

    @Test
    public void testNestedIndents() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.indent();
        printer.print("line1\n");
        printer.indent();
        printer.print("line2\n");
        printer.print("line3\r");
        printer.outdent();
        printer.print("line4\n");
        printer.outdent();
        printer.print("line5");
        checkEquals(">line1\n" + //
                ">>line2\n" + //
                ">>line3\r" + //
                ">line4\n" + //
                "line5", printed.toString());
    }

    @Test
    public void testPrintAndLineStartWithoutEol() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
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
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("line1");
        printer.indent();
        printer.lineStart();
        printer.print("line2");

        checkEquals("line1" + LINE_ENDING + ">line2",
                printed.toString());
    }

    @Test
    public void testMixedCharactersAndLineEnding() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("line1");
        printer.indent();
        printer.print(printer.lineEnding());
        printer.print("line2");

        checkEquals("line1" + LINE_ENDING + ">line2",
                printed.toString());
    }

    @Test
    public void testIndentingPrinterWrappingIndentingPrinter() {
        final StringBuilder printed = new StringBuilder();

        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("line1");
        printer.indent();
        printer.print(printer.lineEnding());

        final IndentingPrinter printer2 = printer.indenting(Indentation.with("##"));
        printer2.indent();
        printer2.print("line2");
        printer.lineStart();
        printer2.outdent();

        printer.print("line3");

        checkEquals("line1" + LINE_ENDING + ">##line2" + LINE_ENDING + ">line3",
                printed.toString());
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        checkEquals(printer.toString(), BasicIndentingPrinter.with(printer, INDENTATION).toString());
    }

    private BasicIndentingPrinter createPrinter(final Printer printer) {
        return BasicIndentingPrinter.with(printer, INDENTATION);
    }

    @Override
    public Class<BasicIndentingPrinter> type() {
        return BasicIndentingPrinter.class;
    }

    @Test
    final public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> this.createPrinter((Printer) null));
    }

    @Override
    @Test
    public void testPrintNullFails() {
        //
    }

    @Test
    public void testPrint() {
        final StringBuilder builder = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(builder);
        printer.print("line1\n");
        printer.print("line2\n");
        checkEquals("line1\nline2\n", builder.toString());
    }

    @Test
    final public void testLineStart() {
        final StringBuilder builder = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(Printers.stringBuilder(builder,
                LINE_ENDING));
        printer.print("before");
        printer.lineStart();
        printer.print("next");

        checkEquals("before" + LINE_ENDING + "next",
                builder.toString());
    }

    @Test
    final public void testLineStartWhenEmpty() {
        final StringBuilder builder = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(builder);
        printer.lineStart();
        printer.print("after");

        checkEquals("after", builder.toString());
    }

    @Test
    final public void testLineStart2() {
        final StringBuilder builder = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(builder);
        printer.print("before");
        printer.lineStart();
        printer.print("after");

        checkEquals("before" + LINE_ENDING + "after",
                builder.toString());
    }

    @Test
    final public void testLineStartWithoutFollowingPrint() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before");
        printer.lineStart();

        checkEquals("before" + LINE_ENDING, printed.toString());
    }

    @Test
    final public void testLineStartFollowingCarriageReturn() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before\r");
        printer.lineStart();
        printer.print("next");

        checkEquals("before\rnext", printed.toString());
    }

    @Test
    final public void testLineStartFollowingNewline() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before\n");
        printer.lineStart();
        printer.print("next");

        checkEquals("before\nnext", printed.toString());
    }

    @Test
    final public void testLineStartFollowingCarriageReturnNewLine() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before\r\n");
        printer.lineStart();
        printer.print("next");

        checkEquals("before\r\nnext", printed.toString());
    }

    @Test
    final public void testManyConsecutiveLineStarts() {
        final StringBuilder printed = new StringBuilder();
        final BasicIndentingPrinter printer = this.createPrinter(printed);
        printer.print("before\n");
        printer.lineStart();
        printer.lineStart();
        printer.lineStart();
        printer.print("next");

        checkEquals("before\nnext", printed.toString());
    }

    @Override
    final public BasicIndentingPrinter createPrinter(final StringBuilder builder) {
        return this.createPrinter(this.createStringBuilderPrinter(builder));
    }

    final BasicIndentingPrinter createPrinter(final StringBuilder builder, final LineEnding lineEnding) {
        return this.createPrinter(Printers.stringBuilder(builder, lineEnding));
    }

    final Printer createStringBuilderPrinter(final StringBuilder printed) {
        return Printers.stringBuilder(printed, LINE_ENDING);
    }

    @Override
    public final String typeNamePrefix() {
        return BasicIndentingPrinter.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
