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

import walkingkooka.test.ClassTestCase;
import walkingkooka.text.LineEnding;
import walkingkooka.type.MemberVisibility;

/**
 * Base class for testing a {@link Printer} with mostly parameter checking tests.
 */
abstract public class PrinterTestCase<P extends Printer> extends ClassTestCase<P>
        implements PrinterTesting<P> {

    PrinterTestCase() {
        super();
    }

    static Printer createContractPrinter() {
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


    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public String typeNameSuffix() {
        return Printer.class.getSimpleName();
    }
}