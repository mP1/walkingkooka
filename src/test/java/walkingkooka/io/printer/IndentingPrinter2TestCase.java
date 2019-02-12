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

import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class IndentingPrinter2TestCase<P extends IndentingPrinter2>
        extends PrinterTestCase2<P>
        implements IndentingPrinterTesting<P> {

    IndentingPrinter2TestCase() {
        super();
    }

    // constants

    protected final static LineEnding LINE_ENDING = LineEnding.CR;

    // tests

    @Test
    final public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPrinter((Printer) null);
        });
    }

    @Override
    @Test
    public void testPrintNullFails() {
        //
    }

    @Test
    public void testPrint() {
        final StringBuilder builder = new StringBuilder();
        final P printer = this.createPrinter(builder);
        printer.print("line1\n");
        printer.print("line2\n");
        checkEquals("line1\nline2\n", builder.toString());
    }

    @Test
    final public void testLineStart() {
        final StringBuilder builder = new StringBuilder();
        final P printer = this.createPrinter(Printers.stringBuilder(builder,
                IndentingPrinter2TestCase.LINE_ENDING));
        printer.print("before");
        printer.lineStart();
        printer.print("next");

        checkEquals("before" + IndentingPrinter2TestCase.LINE_ENDING + "next",
                builder.toString());
    }

    @Test
    final public void testLineStartWhenEmpty() {
        final StringBuilder builder = new StringBuilder();
        final P printer = this.createPrinter(builder);
        printer.lineStart();
        printer.print("after");

        checkEquals("after", builder.toString());
    }

    @Test final public void testLineStart2() {
        final StringBuilder builder = new StringBuilder();
        final P printer = this.createPrinter(builder);
        printer.print("before");
        printer.lineStart();
        printer.print("after");

        checkEquals("before" + IndentingPrinter2TestCase.LINE_ENDING + "after",
                builder.toString());
    }

    @Test final public void testLineStartWithoutFollowingPrint() {
        final StringBuilder printed = new StringBuilder();
        final P printer = this.createPrinter(printed);
        printer.print("before");
        printer.lineStart();

        checkEquals("before" + IndentingPrinter2TestCase.LINE_ENDING, printed.toString());
    }

    @Test final public void testLineStartFollowingCarriageReturn() {
        final StringBuilder printed = new StringBuilder();
        final P printer = this.createPrinter(printed);
        printer.print("before\r");
        printer.lineStart();
        printer.print("next");

        checkEquals("before\rnext", printed.toString());
    }

    @Test final public void testLineStartFollowingNewline() {
        final StringBuilder printed = new StringBuilder();
        final P printer = this.createPrinter(printed);
        printer.print("before\n");
        printer.lineStart();
        printer.print("next");

        checkEquals("before\nnext", printed.toString());
    }

    @Test final public void testLineStartFollowingCarriageReturnNewLine() {
        final StringBuilder printed = new StringBuilder();
        final P printer = this.createPrinter(printed);
        printer.print("before\r\n");
        printer.lineStart();
        printer.print("next");

        checkEquals("before\r\nnext", printed.toString());
    }

    @Test
    final public void testManyConsecutiveLineStarts() {
        final StringBuilder pritned = new StringBuilder();
        final P printer = this.createPrinter(pritned);
        printer.print("before\n");
        printer.lineStart();
        printer.lineStart();
        printer.lineStart();
        printer.print("next");

        checkEquals("before\nnext", pritned.toString());
    }

    @Override
    final public P createPrinter(final StringBuilder builder) {
        return this.createPrinter(this.createStringBuilderPrinter(builder));
    }

    final P createPrinter(final StringBuilder builder, final LineEnding lineEnding) {
        return this.createPrinter(Printers.stringBuilder(builder, lineEnding));
    }

    abstract P createPrinter(Printer printer);

    final Printer createStringBuilderPrinter(final StringBuilder printed) {
        return Printers.stringBuilder(printed, IndentingPrinter2TestCase.LINE_ENDING);
    }
}
