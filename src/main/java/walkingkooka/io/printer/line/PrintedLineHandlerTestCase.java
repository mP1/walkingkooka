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

package walkingkooka.io.printer.line;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.io.printer.Printer;
import walkingkooka.io.printer.Printers;
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

/**
 * Base class for testing a {@link PrintedLineHandler} with mostly parameter checking tests.
 */
abstract public class PrintedLineHandlerTestCase<H extends PrintedLineHandler>
        extends PackagePrivateClassTestCase<H> {

    protected PrintedLineHandlerTestCase() {
        super();
    }

    // constants

    private final static String LINE = "";

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Printer PRINTER = Printers.fake();

    // tests

    @Test
    public void testNaming() {
        this.checkNaming(PrintedLineHandler.class);
    }

    @Test final public void testNullLineFails() {
        this.linePrintedFails(null,
                PrintedLineHandlerTestCase.LINE_ENDING,
                PrintedLineHandlerTestCase.PRINTER);
    }

    @Test final public void testNullLineEndingFails() {
        this.linePrintedFails(PrintedLineHandlerTestCase.LINE, null, PrintedLineHandlerTestCase.PRINTER);
    }

    @Test final public void testNullPrinterFails() {
        this.linePrintedFails(PrintedLineHandlerTestCase.LINE,
                PrintedLineHandlerTestCase.LINE_ENDING,
                null);
    }

    private void linePrintedFails(final String longLine, final LineEnding ending, final Printer printer) {
        final H handler = this.createLineHandler();

        try {
            handler.linePrinted(longLine, ending, printer);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected H createLineHandler();

    protected void linePrintedAndCheck(final CharSequence line, final LineEnding lineEnding) {
        this.linePrintedAndCheck(line, lineEnding, line.toString());
    }

    protected void linePrintedAndCheck(final CharSequence line, final LineEnding lineEnding,
                                       final String expected) {
        this.linePrintedAndCheck(line, lineEnding, expected, null);
    }

    protected void linePrintedAndCheck(final CharSequence line, final LineEnding lineEnding,
                                       final String expected, final String message) {
        this.linePrintedAndCheck(this.createLineHandler(), line, lineEnding, expected, message);
    }

    protected void linePrintedAndCheck(final PrintedLineHandler handler, final CharSequence line,
                                       final LineEnding lineEnding, final String expected) {
        this.linePrintedAndCheck(handler, line, lineEnding, expected, null);
    }

    protected void linePrintedAndCheck(final PrintedLineHandler handler, final CharSequence line,
                                       final LineEnding lineEnding, final String expected, final String message) {
        Assert.assertNotNull("handler", handler);
        Assert.assertNotNull("line", line);

        final String lineString = line.toString();
        if (lineString.contains("\r\n")) {
            Assert.fail("Line contains CRNL=" + CharSequences.quoteAndEscape(lineString));
        }
        if (lineString.contains("\r")) {
            Assert.fail("Line contains CR=" + CharSequences.quoteAndEscape(lineString));
        }
        if (lineString.contains("\n")) {
            Assert.fail("Line contains NL=" + CharSequences.quoteAndEscape(lineString));
        }

        Assert.assertNotNull("lineEnding", lineEnding);
        Assert.assertNotNull("expected", expected);

        final StringBuilder printedBuffer = new StringBuilder();
        final Printer printer = Printers.stringBuilder(printedBuffer,
                PrintedLineHandlerTestCase.LINE_ENDING);
        handler.linePrinted(line, lineEnding, printer);
        printer.flush();
        printer.close();

        final String printed = printedBuffer.toString();
        if (false == printed.equals(expected)) {
            failNotEquals(message,
                    CharSequences.quoteAndEscape(expected),
                    CharSequences.quoteAndEscape(printed));
        }
    }
}
