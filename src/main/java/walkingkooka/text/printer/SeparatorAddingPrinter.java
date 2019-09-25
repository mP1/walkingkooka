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

/**
 * A {@link Printer} that adds a separator between each and every print but not after printing
 * {@link LineEnding line endings}.
 */
final class SeparatorAddingPrinter implements Printer {

    /**
     * Does not wrap the given {@link Printer} if the separator is empty.
     */
    static Printer wrap(final Printer printer, final String separator) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(separator, "separator");

        return separator.length() == 0 ? printer : new SeparatorAddingPrinter(printer, separator);
    }

    /**
     * Private constructor use static factory
     */
    private SeparatorAddingPrinter(final Printer printer, final String separator) {
        super();
        this.printer = printer;
        this.separator = separator;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        final Printer printer = this.printer;
        final boolean lineEnding = chars instanceof LineEnding;
        if ((false == lineEnding) && this.needSeparator) {
            printer.print(this.separator);
        }
        printer.print(chars);
        this.needSeparator = !lineEnding;
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
     * The wrapped {@link Printer}.
     */
    final Printer printer;

    /**
     * The {@link String separator} that is added between prints.
     */
    private final String separator;

    /**
     * After adding the first string this flag becomes true which means the separator will be used
     * between prints.
     */
    private boolean needSeparator;

    @Override
    public String toString() {
        return "separate prints with " + CharSequences.quote(this.separator) + " to "
                + this.printer;
    }
}
