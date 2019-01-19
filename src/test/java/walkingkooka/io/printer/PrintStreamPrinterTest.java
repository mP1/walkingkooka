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
import walkingkooka.io.printstream.HasPrintStream;
import walkingkooka.io.printstream.PrintStreams;
import walkingkooka.text.LineEnding;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class PrintStreamPrinterTest extends PrinterTestCase<PrintStreamPrinter> {

    // constants

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static OutputStream OUTPUTSTREAM = new OutputStream() {
        @Override
        public void write(final int b) {
            throw new UnsupportedOperationException();
        }
    };

    // tests

    @Test
    public void testWithNullPrintStreamFails() {
        this.withFails(null, PrintStreamPrinterTest.LINE_ENDING);
    }

    @Test
    public void testWithNullLineEndingFails() {
        this.withFails(PrintStreams.fake(), null);
    }

    private void withFails(final PrintStream printStream, final LineEnding lineEnding) {
        try {
            PrintStreamPrinter.with(printStream, lineEnding);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testHasPrintStream() {
        final PrintStream stream = System.out;
        final PrintStreamPrinter printer = PrintStreamPrinter.with(stream,
                PrintStreamPrinterTest.LINE_ENDING);
        assertTrue(printer + " does not implement HasPrintStream",
                printer instanceof HasPrintStream);
        assertSame("printStream getter", stream, printer.printStream());
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    @Test
    public void testSysErr() {
        final Printer printer = PrintStreamPrinter.sysErr();
        assertSame(printer, PrintStreamPrinter.sysErr());
    }

    @Test
    public void testPrintStreamWithSysErr() {
        assertSame(PrintStreamPrinter.sysErr(),
                PrintStreamPrinter.with(System.err, PrintStreamPrinterTest.LINE_ENDING));
    }

    @Test
    public void testSysOut() {
        final Printer printer = PrintStreamPrinter.sysOut();
        assertSame(printer, PrintStreamPrinter.sysOut());
    }

    @Test
    public void testPrintStreamWithSysOut() {
        assertSame(PrintStreamPrinter.sysOut(),
                PrintStreamPrinter.with(System.out, PrintStreamPrinterTest.LINE_ENDING));
    }

    @Test
    public void testAppend() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Printer printer = PrintStreamPrinter.with(new PrintStream(output),
                PrintStreamPrinterTest.LINE_ENDING);
        printer.print("ascii");
        checkEquals("ascii", new String(output.toByteArray()));
    }

    @Test
    public void testToString() {
        final PrintStream printStream = new PrintStream(OUTPUTSTREAM);
        checkEquals(printStream.toString(),
                PrintStreamPrinter.with(printStream, PrintStreamPrinterTest.LINE_ENDING)
                        .toString());
    }

    @Test
    public void testSystemErrToString() {
        final PrintStream printStream = System.err;
        checkEquals("System.err " + printStream.toString(),
                PrintStreamPrinter.sysErr().toString());
    }

    @Test
    public void testSystemOutToString() {
        final PrintStream printStream = System.out;
        checkEquals("System.out " + printStream.toString(),
                PrintStreamPrinter.sysOut().toString());
    }

    @Override
    @Test
    public void testLineEndingAfterCloseFails() {
        // nop
    }

    @Override
    protected PrintStreamPrinter createPrinter() {
        return PrintStreamPrinter.with(//
                new PrintStream(OUTPUTSTREAM) {

                    @Override
                    public void println() {
                        if (this.closed) {
                            throw new PrinterException("PrintStream already closed");
                        }
                    }

                    @Override
                    public PrintStream append(final CharSequence csq) {
                        if (this.closed) {
                            throw new PrinterException("PrintStream already closed");
                        }
                        return super.append(csq);
                    }

                    @Override
                    public void flush() {
                        if (this.closed) {
                            throw new PrinterException("PrintStream already closed");
                        }
                        super.flush();
                    }

                    @Override
                    public void close() {
                        this.closed = true;
                        super.close();
                    }

                    private boolean closed;
                }, //
                PrintStreamPrinterTest.LINE_ENDING);
    }

    @Override
    protected Class<PrintStreamPrinter> type() {
        return PrintStreamPrinter.class;
    }
}
