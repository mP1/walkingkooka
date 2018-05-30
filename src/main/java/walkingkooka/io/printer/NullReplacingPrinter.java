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

import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.Objects;

/**
 * A {@link Printer} that replaces nulls with a default.
 */
final class NullReplacingPrinter implements Printer {

    /**
     * Creates a new {@link NullReplacingPrinter}
     */
    static NullReplacingPrinter wrap(final Printer printer, final String replacement) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(replacement, "replacement");

        return new NullReplacingPrinter(printer, replacement);
    }

    /**
     * Private constructor use static factory.
     */
    private NullReplacingPrinter(final Printer printer, final String replacement) {
        super();
        this.printer = printer;
        this.replacement = replacement;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        this.printer.print(chars == null ? this.replacement : chars);
    }

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

    final Printer printer;

    private final String replacement;

    @Override
    public String toString() {
        return "if null then print " + CharSequences.quote(this.replacement) + ' ' + this.printer;
    }
}
