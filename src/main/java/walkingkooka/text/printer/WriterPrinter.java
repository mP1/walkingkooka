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

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

/**
 * An adapter that wraps a {@link Writer} printing to {@link Writer#write(String)}. Any {@link
 * IOException} that are thrown by the {@link Writer} are wrapped and then thrown inside a {@link
 * PrinterException}. The writer is never auto flushed.
 */
final class WriterPrinter implements Printer {

    /**
     * Creates a new {@link WriterPrinter}
     */
    static WriterPrinter adapt(final Writer writer, final LineEnding lineEnding) {
        Objects.requireNonNull(writer, "writer");
        Objects.requireNonNull(lineEnding, "lineEnding");

        return new WriterPrinter(writer, lineEnding);
    }

    private WriterPrinter(final Writer writer, final LineEnding lineEnding) {
        super();
        this.writer = writer;
        this.lineEnding = lineEnding;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        try {
            this.writer.write(chars.toString());
        } catch (final IOException cause) {
            throw new PrinterException("Print failed", cause);
        }
    }

    /**
     * Returns the {@link LineEnding} passed to the factory.
     */
    @Override
    public LineEnding lineEnding() throws PrinterException {
        return this.lineEnding;
    }

    private final LineEnding lineEnding;

    @Override
    public void flush() throws PrinterException {
        try {
            this.writer.flush();
        } catch (final IOException cause) {
            throw new PrinterException("Flush failed", cause);
        }
    }

    @Override
    public void close() throws PrinterException {
        try {
            this.writer.close();
        } catch (final IOException cause) {
            throw new PrinterException("Close failed", cause);
        }
    }

    // properties

    /**
     * The wrapped {@link Writer}
     */
    final Writer writer;

    @Override
    public String toString() {
        return this.writer.toString();
    }
}
