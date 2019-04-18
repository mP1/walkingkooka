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

import walkingkooka.text.LineEnding;

import java.util.Objects;
import java.util.function.Function;

/**
 * Wraps another {@link Printer} watching characters of they are printed, presenting any line
 * endings to a {@link Function} so they may be replaced with another. This allows normalization
 * where all different line endings may be replaced by a single of or even complete removal.
 */
final class LineEndingTransformingPrinter implements Printer {
    /**
     * Creates a new {@link LineEndingTransformingPrinter}
     */
    static LineEndingTransformingPrinter wrap(final Function<LineEnding, LineEnding> transformer,
                                              final Printer printer) {
        Objects.requireNonNull(transformer, "line ending transformer");
        Objects.requireNonNull(printer, "printer");

        return new LineEndingTransformingPrinter(transformer, printer);
    }

    /**
     * Private constructor
     */
    private LineEndingTransformingPrinter(final Function<LineEnding, LineEnding> transformer,
                                          final Printer printer) {
        super();
        this.transformer = transformer;
        this.printer = printer;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        final int length = chars.length();
        if (length > 0) {
            final Printer printer = this.printer;
            final Function<LineEnding, LineEnding> transformer = this.transformer;

            int start = 0;
            int i = 0;

            // check if the next character is a NL
            if (this.afterCr) {
                final char c = chars.charAt(i);

                // CRNL printed
                if ('\n' == c) {
                    printer.print(transformer.apply(LineEnding.CRNL));
                    i++;
                    start++;
                } else {
                    // was just a CR apply and print
                    printer.print(transformer.apply(LineEnding.CR));
                }
            }

            // process each individual character
            char c = 0;
            for (; ; ) {
                // print the unprinted $chars
                if (length == i) {
                    if (start < i) {
                        printer.print(chars.subSequence(start, i));
                    }
                    break;
                }

                c = chars.charAt(i);
                if ('\r' == c) {
                    // print everything before the CR
                    if (start < i) {
                        printer.print(chars.subSequence(start, i));
                    }
                    // check if NL follows $c
                    i++;
                    if (length == i) {
                        break;
                    }
                    // might be a CRNL
                    LineEnding ending = LineEnding.CR;
                    if ('\n' == chars.charAt(i)) {
                        // skip NL
                        i++;
                        ending = LineEnding.CRNL;
                    }
                    printer.print(transformer.apply(ending));
                    start = i;
                    continue;
                }
                if ('\n' == c) {
                    // print everything before the NL
                    if (start < i) {
                        printer.print(chars.subSequence(start, i));
                    }
                    printer.print(transformer.apply(LineEnding.NL));
                    i++;
                    start = i;
                    continue;
                }
                i++;
            }

            // save $afterCr for the next print..
            this.afterCr = '\r' == c;
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
     * If a carriage return is buffered, apply and print, then flush.
     */
    @Override
    public void flush() throws PrinterException {
        if (this.afterCr) {
            this.printer.print(this.transformer.apply(LineEnding.CR));
            this.afterCr = false;
        }
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
    private final Printer printer;

    /**
     * A {@link Function} that receives each {@link LineEnding} of they are encountered possibly
     * returning a replacement or the same.
     */
    private final Function<LineEnding, LineEnding> transformer;

    /**
     * Flag that is true if the last buffered but not printed character was a CR. This is necessary
     * just in case a NL follows.
     */
    private boolean afterCr;

    /**
     * Dumps the {@link Function} and then {@link Printer}.
     */
    @Override
    public String toString() {
        return this.transformer + " " + this.printer;
    }
}
