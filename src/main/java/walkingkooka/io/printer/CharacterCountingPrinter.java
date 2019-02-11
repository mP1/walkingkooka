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
import java.util.function.IntConsumer;

/**
 * A {@link Printer} that updates a {@link IntConsumer} after each {@link #print(CharSequence)}. Note
 * the {@link IntConsumer} is updated after every print.
 */
final class CharacterCountingPrinter implements Printer {

    /**
     * Creates a new {@link CharacterCountingPrinter}
     */
    static CharacterCountingPrinter wrap(final Printer printer, final IntConsumer counter) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(counter, "counter");

        return new CharacterCountingPrinter(printer, counter);
    }

    /**
     * Private constructor
     */
    private CharacterCountingPrinter(final Printer printer, final IntConsumer counter) {
        super();
        this.printer = printer;
        this.counter = counter;
    }

    /**
     * Prints the {@link CharSequence} and updates the counter.
     */
    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        this.printer.print(chars);
        this.addToCounter(chars.length());
    }

    /**
     * Returns the wrapped {@link Printer printers}
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

    private int characterCounter;

    /**
     * A counter that is updated after each print with the total number of printed characters.
     */
    private final IntConsumer counter;

    /**
     * Adds the given amount to the counter.
     */
    void addToCounter(final int add) {
        this.characterCounter = this.characterCounter + add;
        final IntConsumer counter = this.counter;
        counter.accept(characterCounter);
    }

    /**
     * The wrapped {@link Printer}.
     */
    private final Printer printer;

    /**
     * Dumps the wrapped {@link Printer}.
     */
    @Override
    public String toString() {
        return this.printer + " " + this.characterCounter + " char(s)";
    }
}
