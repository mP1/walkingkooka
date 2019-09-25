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
import java.util.function.IntConsumer;

/**
 * Wraps another {@link Printer} and increments a {@link IntConsumer} each time a new line is
 * encountered. Note that a {@link CharSequence} with three separate lines will result in three
 * separate prints to the wrapped {@link Printer}.
 */
final class LineCountingPrinter implements Printer {

    /**
     * Creates a new {@link LineCountingPrinter}
     */
    static LineCountingPrinter wrap(final Printer printer, final IntConsumer counter) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(counter, "counter");

        return new LineCountingPrinter(printer, counter);
    }

    /**
     * Private constructor
     */
    private LineCountingPrinter(final Printer printer, final IntConsumer counter) {
        super();
        this.counter = counter;
        this.printer = printer;

        this.lineCount = 0;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        final int length = chars.length();
        if (length > 0) {
            final Printer printer = this.printer;

            int start = 0;
            int i = 0;
            char previous = this.previous;
            char c = 0;

            // process each individual character
            for (; ; ) {
                // print the unprinted $chars
                if (length == i) {
                    if (start < i) {
                        // do not print the last character if its a CR
                        if ('\r' == previous) {
                            if (start < (i - 1)) {
                                printer.print(chars.subSequence(start, i - 1));
                            }
                            break;
                        }
                        // print everything that has not yet been printed.
                        printer.print(chars.subSequence(start, i));
                    }
                    break;
                }

                c = chars.charAt(i);
                i++;

                // print everything including the NL
                if ('\n' == c) {
                    printer.print(chars.subSequence(start, i));
                    this.incrementCounter();
                    start = i;

                    previous = c;
                    continue;
                } else {
                    // check if $previous was CR
                    if ('\r' == previous) {
                        // print everything including the CR
                        printer.print(chars.subSequence(start, i - 1));
                        this.incrementCounter();
                        start = i - 1;
                    }

                }
                previous = c;
            }

            this.previous = c;
        }
    }

    /**
     * Returns the wrapped {@link Printer#lineEnding()}.
     */
    @Override
    public LineEnding lineEnding() throws PrinterException {
        return this.printer.lineEnding();
    }

    /**
     * If a carriage return is buffered, print and then increment the counter and then flush.<br>
     * NOTE This unfortunately means that if a NL is printed afterwards the counter will be
     * incremented after the CR but before the NL.
     */
    @Override
    public void flush() throws PrinterException {
        final char previous = this.previous;
        if ('\r' == previous) {
            this.printer.print(LineEnding.CR);

            this.incrementCounter();
            this.previous = 0;
        }
        this.printer.flush();
    }

    /**
     * Closes the wrapped {@link Printer}.
     */
    @Override
    public void close() throws PrinterException {
        this.printer.close();
    }

    // properties

    /**
     * The wrapped {@link Printer}
     */
    private final Printer printer;

    /**
     * Contains the actual line count.
     */
    //@VisibleForTesting
    int lineCount;

    /**
     * A {@link IntConsumer} that is incremented each time a new line is encountered.
     */
    private final IntConsumer counter;

    /**
     * Utility that increments the counter.
     */
    private void incrementCounter() {
        final IntConsumer counter = this.counter;
        counter.accept(++this.lineCount);
    }

    /**
     * The last printed character. This is used to buffer CRNL so the counter is incremented after
     * the NL.
     */
    private char previous;

    /**
     * Dumps the {@link Printer} and then line count.
     */
    @Override
    public String toString() {
        return this.printer + " " + this.lineCount + " line(s)";
    }
}
