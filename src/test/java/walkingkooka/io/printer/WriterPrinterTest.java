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
import walkingkooka.text.LineEnding;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.Assert.assertSame;

final public class WriterPrinterTest extends PrinterTestCase<WriterPrinter> {

    // constants

    private final static Writer WRITER = new Writer() {
        @Override
        public void write(final char[] cbuf, final int off, final int len) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void flush() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {
            throw new UnsupportedOperationException();
        }
    };

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testAdaptNullWriterFails() {
        this.adaptFails(null, WriterPrinterTest.LINE_ENDING);
    }

    @Test
    public void testAdaptNullLineEndingFails() {
        this.adaptFails(WriterPrinterTest.WRITER, null);
    }

    private void adaptFails(final Writer writer, final LineEnding lineEnding) {
        try {
            WriterPrinter.adapt(writer, lineEnding);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testPrint() {
        final StringWriter writer = new StringWriter();
        final WriterPrinter printer = this.createPrinter(writer);
        printer.print("1");
        printer.print("23");
        printer.print("456");
        checkEquals("123456", writer.toString());
    }

    @Test
    public void testPrintThenWriterThrowsFails() {
        final String written = "printed";
        final IOException thrown = new IOException("thrown");
        final WriterPrinter printer = this.createPrinter(//
                new Writer() {
                    @Override
                    public void write(final String string) throws IOException {
                        assertSame("written", written, string);
                        throw thrown;
                    }

                    @Override
                    public void write(final char[] cbuf, final int off, final int len) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void flush() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void close() {
                        throw new UnsupportedOperationException();
                    }
                });
        try {
            printer.print(written);
            Assert.fail();
        } catch (final PrinterException expected) {
            assertSame("cause", thrown, expected.getCause());
        }
    }

    @Override
    @Test
    public void testLineEndingAfterCloseFails() {
        // nop
    }

    @Test
    public void testToString() {
        checkEquals(WriterPrinterTest.WRITER.toString(),
                this.createPrinter(WriterPrinterTest.WRITER).toString());
    }

    @Override
    protected WriterPrinter createPrinter() {
        return this.createPrinter(//
                new Writer() {

                    @Override
                    public void write(final char[] cbuf, final int off, final int len)
                            throws IOException {
                        if (this.closed) {
                            throw new IOException("Writer already closed");
                        }
                    }

                    @Override
                    public void flush() throws IOException {
                        if (this.closed) {
                            throw new IOException("Writer already closed");
                        }
                    }

                    @Override
                    public void close() {
                        this.closed = true;
                    }

                    private boolean closed;
                });
    }

    private WriterPrinter createPrinter(final Writer writer) {
        return WriterPrinter.adapt(writer, WriterPrinterTest.LINE_ENDING);
    }

    @Override
    protected Class<WriterPrinter> type() {
        return WriterPrinter.class;
    }
}
