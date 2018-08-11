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
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link Printer} with mostly parameter checking tests.
 */
abstract public class PrinterTestCase<P extends Printer> extends PackagePrivateClassTestCase<P> {

    protected PrinterTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Printer.class);
    }

    @Test
    public void testPrintNullFails() {
        this.printFails();
    }

    @Test
    public void testFlush() {
        this.createPrinter().flush();
    }

    @Test
    public void testClose() {
        this.createPrinter().close();
    }

    @Test
    public void testPrintAfterCloseFails() {
        final P printer = this.createPrinterAndClose();

        try {
            printer.print("print should have been failed because is already closed");
            Assert.fail();
        } catch (final PrinterException expected) {
        }
    }

    @Test
    public void testLineEndingAfterCloseFails() {
        final P printer = this.createPrinterAndClose();

        try {
            printer.lineEnding();
            Assert.fail();
        } catch (final PrinterException expected) {
        }
    }

    @Test
    public void testFlushAfterCloseFails() {
        final P printer = this.createPrinterAndClose();
        try {
            printer.flush();
            Assert.fail();
        } catch (final PrinterException expected) {
        }
    }

    @Test
    public void testCloseAfterCloseFails() {
        final P printer = this.createPrinterAndClose();
        printer.close(); // ignored
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected P createPrinter();

    protected P createPrinterAndClose() {
        final P printer = this.createPrinter();
        printer.close();
        return printer;
    }

    static protected Printer createFakePrinter() {
        return new Printer() {

            @Override
            public void print(final CharSequence chars) throws PrinterException {
                this.complainIfClosed();
                throw new UnsupportedOperationException();
            }

            @Override
            public LineEnding lineEnding() throws PrinterException {
                this.complainIfClosed();
                throw new UnsupportedOperationException();
            }

            @Override
            public void flush() throws PrinterException {
                this.complainIfClosed();
            }

            private void complainIfClosed() throws PrinterException {
                if (this.closed) {
                    throw new PrinterException("Closed");
                }
            }

            @Override
            public void close() throws PrinterException {
                this.closed = true;
            }

            private boolean closed;
        };
    }

    final protected void printFails() {
        this.printFails(this.createPrinter());
    }

    final protected void printFails(final Printer printer) {
        try {
            printer.print(null);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    static public void checkEquals(final CharSequence expected, final CharSequence actual) {
        checkEquals(null, expected, actual);
    }

    static public void checkEquals(final String message, final CharSequence expected,
                                   final CharSequence actual) {
        assertEquals(CharSequences.escape(expected).toString(), CharSequences.escape(actual).toString());
    }
}