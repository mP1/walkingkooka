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
import walkingkooka.text.LineEnding;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        assertThrows(NullPointerException.class, () -> PrintStreamPrinter.with(null, LINE_ENDING));
    }

    @Test
    public void testWithNullLineEndingFails() {
        assertThrows(NullPointerException.class, () -> PrintStreamPrinter.with(System.out, null));
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
                PrintStreamPrinter.with(System.err, LINE_ENDING));
    }

    @Test
    public void testSysOut() {
        final Printer printer = PrintStreamPrinter.sysOut();
        assertSame(printer, PrintStreamPrinter.sysOut());
    }

    @Test
    public void testPrintStreamWithSysOut() {
        assertSame(PrintStreamPrinter.sysOut(),
                PrintStreamPrinter.with(System.out, LINE_ENDING));
    }

    @Test
    public void testAppend() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Printer printer = PrintStreamPrinter.with(new PrintStream(output),
                LINE_ENDING);
        printer.print("ascii");
        checkEquals("ascii", new String(output.toByteArray()));
    }

    @Test
    public void testToString() {
        final PrintStream printStream = new PrintStream(OUTPUTSTREAM);
        checkEquals(printStream.toString(),
                PrintStreamPrinter.with(printStream, LINE_ENDING)
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
    public PrintStreamPrinter createPrinter() {
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
                LINE_ENDING);
    }

    @Override
    public Class<PrintStreamPrinter> type() {
        return PrintStreamPrinter.class;
    }
}
