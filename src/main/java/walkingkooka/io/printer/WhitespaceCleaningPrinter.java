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

/**
 * A {@link Printer} that filters any written {@link CharSequence characters} written to the wrapped
 * {@link Printer} removing multiple or redundant whitespace, carriage return and newlines.
 */
final class WhitespaceCleaningPrinter implements Printer {

    /**
     * Creates a new {@link WhitespaceCleaningPrinter} wrapping a {@link Printer}.
     */
    static WhitespaceCleaningPrinter wrap(final Printer printer) {
        Objects.requireNonNull(printer, "printer");

        return printer instanceof WhitespaceCleaningPrinter ?
                (WhitespaceCleaningPrinter) printer :
                new WhitespaceCleaningPrinter(printer);
    }

    /**
     * Private constructor
     */
    private WhitespaceCleaningPrinter(final Printer printer) {
        super();
        this.printer = printer;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        final int length = chars.length();

        if (length > 0) {
            final StringBuilder buffer = this.buffer;
            buffer.setLength(0);
            buffer.ensureCapacity(length);

            boolean wasWhitespace = this.wasWhitespace;

            for (int i = 0; i < length; i++) {
                final char c = chars.charAt(i);
                if (Character.isWhitespace(c)) {
                    if (wasWhitespace) {
                        continue;
                    }

                    buffer.append(' ');
                    wasWhitespace = true;
                    continue;
                }

                buffer.append(c);
                wasWhitespace = false;
            }

            this.wasWhitespace = wasWhitespace;
            this.printer.print(buffer);
        }
    }

    /**
     * Returns the wrapped {@link Printer#lineEnding()}.
     */
    @Override
    public LineEnding lineEnding() throws PrinterException {
        return this.printer.lineEnding();
    }

    @Override
    public void flush() throws PrinterException {
        this.printer.flush();
    }

    @Override
    public void close() throws PrinterException {
        this.printer.close();
    }

    // properties

    /**
     * when true indicates the last printed character was a whitespace.
     */
    private boolean wasWhitespace = false;

    /**
     * Buffer filled when filtering.
     */
    private final StringBuilder buffer = new StringBuilder();

    /**
     * The target or all writes.
     */
    private final Printer printer;

    @Override
    public String toString() {
        return "clean whitespace " + this.printer.toString();
    }
}
