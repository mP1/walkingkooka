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

import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.io.Closeable;
import java.util.function.Function;

/**
 * A {@link Printer} may be used to print {@link CharSequence characters} to a target.
 */
public interface Printer extends PrinterLike, Closeable {

    /**
     * Prints of or more characters.
     */
    void print(CharSequence chars) throws PrinterException;

    /**
     * Returns the recommended {@link LineEnding}.
     */
    LineEnding lineEnding() throws PrinterException;

    /**
     * Flushes this {@link Printer}
     */
    void flush() throws PrinterException;

    /**
     * Closes this {@link Printer}. Any future prints will fail.
     */
    @Override
    void close() throws PrinterException;

    /**
     * Creates an {@link IndentingPrinter} wrapping this {@link Printer}.
     */
    default IndentingPrinter indenting(final Indentation indentation) {
        return IndentingPrinters.printer(this, indentation);
    }

    /**
     * Returns a {@link Printer} that calls {@link PrintedLineHandler} with each {@link CharSequence line}.
     */
    default Printer printedLine(final PrintedLineHandler handler) {
        return Printers.printedLine(this, handler);
    }

    /**
     * Returns a {@link Printer} that prints to both this and the given {@link Printer}.
     */
    default Printer tee(final Printer printer) {
        return Printers.tee(this, printer);
    }

    /**
     * Returns a {@link Printer} that uses the {@link Function} to transform each and every printed {@link CharSequence}.
     */
    default Printer transformText(final Function<CharSequence, CharSequence> transformer) {
        return Printers.textTransforming(transformer, this);
    }

    /**
     * Returns a {@link Printer} that is unclose-able.
     */
    default Printer uncloseable() {
        return Printers.uncloseable(this);
    }
}
