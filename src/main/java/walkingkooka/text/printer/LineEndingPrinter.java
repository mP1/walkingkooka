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

import walkingkooka.text.LineEnding;

import java.util.Objects;

/**
 * A {@link Printer} that wraps another {@link Printer} and prints a {@link LineEnding} after each
 * print.
 */
final class LineEndingPrinter implements Printer {

    /**
     * Wraps the given {@link Printer}.
     */
    static LineEndingPrinter wrap(final Printer printer) {
        Objects.requireNonNull(printer, "printer");

        Printer target = printer;
        if (printer instanceof LineEndingPrinter) {
            final LineEndingPrinter lineEndingPrinter = (LineEndingPrinter) printer;
            target = lineEndingPrinter.printer;
        }
        return new LineEndingPrinter(target);
    }

    /**
     * Private constructor
     */
    private LineEndingPrinter(final Printer printer) {
        super();
        this.printer = printer;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        final Printer printer = this.printer;
        printer.print(chars);
        printer.print(printer.lineEnding());
    }

    /**
     * Returns the wrapped {@link Printer#lineEnding()}.
     */
    @Override
    public LineEnding lineEnding() throws PrinterException {
        return this.printer.lineEnding();
    }

    /**
     * Calls the wrapped {@link Printer#flush()}.
     */
    @Override
    public void flush() throws PrinterException {
        this.printer.flush();
    }

    /**
     * Closes the wrapped {@link Printer}
     */
    @Override
    public void close() throws PrinterException {
        this.printer.close();
    }

    // properties

    /**
     * The wrapped {@link Printer}
     */
    final Printer printer;

    /**
     * Dumps the {@link Printer}.
     */
    @Override
    public String toString() {
        return this.printer.toString();
    }
}
