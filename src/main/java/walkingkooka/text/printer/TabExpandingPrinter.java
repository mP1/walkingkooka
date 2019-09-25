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

import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.Objects;
import java.util.function.IntUnaryOperator;

/**
 * A {@link Printer} that inserts spaces to advance the position each time a tab character is
 * encountered. The tab character not printed but rather replaced by the required number of spaces
 * to advance to the tab stop returned by the {@link IntUnaryOperator}. The {@link IntUnaryOperator}
 * tab stop must be greater than or equal to the current position otherwise a {@link
 * PrinterException} will be thrown.Any attempts to print null will fail.
 */
final class TabExpandingPrinter implements Printer {

    /**
     * Creates a new {@link TabExpandingPrinter}
     */
    static TabExpandingPrinter wrap(final Printer printer, final IntUnaryOperator tabStops) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(tabStops, "tabStops");

        return new TabExpandingPrinter(printer, tabStops);
    }

    /**
     * Private constructor
     */
    private TabExpandingPrinter(final Printer printer, final IntUnaryOperator tabStops) {
        super();
        this.printer = printer;
        this.tabStops = tabStops;
    }

    /**
     * Contains the core logic of spotting and expanding tabs into zero or more spaces. When it
     * finishes it updates the {@link #tabStops}.
     */
    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        int column = this.column;

        try {
            final Printer printer = this.printer;
            final int length = chars.length();
            int first = 0;

            for (int i = 0; i < length; i++) {
                final char c = chars.charAt(i);

                if ('\t' == c) {
                    // print everything before the tab.
                    printer.print(chars.subSequence(first, i));
                    final int next = this.tabStop(column);
                    this.insertSpaces(next - column);
                    first = i + 1;
                    column = next;
                    continue;
                }

                // if EOL reset column counter...
                if (('\r' == c) || ('\n' == c)) {
                    column = 0;
                    continue;
                }
                column++;
            }

            if (first < length) {
                printer.print(chars.subSequence(first, length));
            }

        } finally {
            this.column = column;
        }
    }

    /**
     * Called whenever a tab character is found and retrieves the number of characters to be
     * inserted performing numerous checks.
     */
    private int tabStop(final int column) {
        final int next = this.tabStops.applyAsInt(column);
        if (next < column) {
            throw new PrinterException("Next tab stop " + next + " < " + column);
        }
        return next;
    }

    /**
     * Inserts the requested number of spaces without updating the {@link #column} property.
     */
    private void insertSpaces(final int count) {
        this.printer.print(CharSequences.repeating(' ', count));
    }

    /**
     * Returns the wrapped {@link Printer#lineEnding()}.
     */
    @Override
    public LineEnding lineEnding() throws PrinterException {
        return this.printer.lineEnding();
    }

    /**
     * Flushes the wrapped {@link Printer}.
     */
    @Override
    public void flush() throws PrinterException {
        this.printer.flush();
    }

    /**
     * Closes the wrapped {@link Printer}. This should prevent future prints.
     */
    @Override
    public void close() throws PrinterException {
        this.printer.close();
    }

    /**
     * Takes the current column position and returns the desired tab stop. This value is used to
     * insert spaces.
     */
    private final IntUnaryOperator tabStops;

    /**
     * The wrapped {@link Printer}.
     */
    private final Printer printer;

    /**
     * The column for the current line. Note this value is 0 based.
     */
    private int column = 0;

    /**
     * Dumps the wrapped {@link Printer}.
     */
    @Override
    public String toString() {
        return "tab x " + this.printer;
    }
}
