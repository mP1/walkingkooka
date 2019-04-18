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

package walkingkooka.io.printstream;

import walkingkooka.io.printer.Printer;
import walkingkooka.text.LineEnding;
import walkingkooka.type.PublicStaticHelper;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

final public class PrintStreams implements PublicStaticHelper {
    final static OutputStream DUMMY_OUTPUTSTREAM = new OutputStream() {
        @Override
        public void write(final int b) {
            // nop
        }
    };

    /**
     * {@see FakePrintStream}
     */
    public static PrintStream fake() {
        return FakePrintStream.create();
    }

    /**
     * {@see PrinterPrintStream}
     */
    public static PrintStream printer(final Printer printer, final LineEnding lineEnding,
                                      final Charset charset) {
        return PrinterPrintStream.adapt(printer, lineEnding, charset);
    }

    /**
     * {@see TeePrintStream}
     */
    public static PrintStream tee(final PrintStream first, final PrintStream second) {
        return TeePrintStream.wrap(first, second);
    }

    /**
     * Stop creation
     */
    private PrintStreams() {
        throw new UnsupportedOperationException();
    }
}
