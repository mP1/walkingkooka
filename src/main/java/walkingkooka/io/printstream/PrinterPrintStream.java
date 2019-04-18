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

package walkingkooka.io.printstream;

import walkingkooka.io.printer.Printer;
import walkingkooka.io.printer.PrinterException;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * A {@link PrintStream} bridge that sends prints to a {@link PrintStream} to a wrapped {@link
 * Printer}.
 */
final class PrinterPrintStream extends PrintStream {

    /**
     * Either wraps or unwraps if the {@link Printer} is a {@link PrinterPrintStream}.
     */
    static PrintStream adapt(final Printer printer, final LineEnding lineEnding,
                             final Charset charset) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(lineEnding, "lineEnding");
        Objects.requireNonNull(charset, "charset");

        return printer instanceof HasPrintStream ?
                ((HasPrintStream) printer).printStream() :
                new PrinterPrintStream(printer, lineEnding, charset);
    }

    /**
     * Private constructor use static factory.
     */
    private PrinterPrintStream(final Printer printer, final LineEnding lineEnding,
                               final Charset charset) {
        super(PrintStreams.DUMMY_OUTPUTSTREAM);

        this.printer = printer;
        this.lineEnding = lineEnding;
        this.charset = charset;
    }

    @Override
    public void print(final boolean b) {
        this.print(String.valueOf(b));
    }

    @Override
    public void print(final char c) {
        this.print(String.valueOf(c));
    }

    @Override
    public void print(final int i) {
        this.print(String.valueOf(i));
    }

    @Override
    public void print(final long l) {
        this.print(String.valueOf(l));
    }

    @Override
    public void print(final float f) {
        this.print(String.valueOf(f));
    }

    @Override
    public void print(final double d) {
        this.print(String.valueOf(d));
    }

    @Override
    public void print(final char[] s) {
        this.print(String.valueOf(s));
    }

    @Override
    public void print(final Object object) {
        this.print(String.valueOf(object));
    }

    @Override
    public void print(final String s) {
        this.printBytesIfNecessary();
        try {
            this.printer.print(s);
        } catch (final PrinterException ignore) {
        }
    }

    @Override
    public void println() {
        this.print(this.lineEnding);
    }

    @Override
    public void println(final boolean b) {
        this.print(b);
        this.println();
    }

    @Override
    public void println(final char c) {
        this.print(c);
        this.println();
    }

    @Override
    public void println(final int i) {
        this.print(i);
        this.println();
    }

    @Override
    public void println(final long l) {
        this.print(l);
        this.println();
    }

    @Override
    public void println(final float f) {
        this.print(f);
        this.println();
    }

    @Override
    public void println(final double d) {
        this.print(d);
        this.println();
    }

    @Override
    public void println(final char[] s) {
        this.print(s);
        this.println();
    }

    @Override
    public void println(final Object obj) {
        this.println(String.valueOf(obj));
    }

    @Override
    public void println(final String s) {
        this.print(s);
        this.println();
    }

    @Override
    public void write(final int b) {
        this.bytes().write(b);
    }

    @Override
    public void write(final byte[] buffer, final int offset, final int length) {
        if (length > 0) {
            this.bytes().write(buffer, offset, length);
        }
    }

    /**
     * If a {@link ByteArrayOutputStream} is active print them to the {@link Printer} and clear it.
     */
    private void printBytesIfNecessary() {
        final ByteArrayOutputStream bytes = this.bytes;
        if (null != bytes) {
            this.bytes = null;
            try {
                this.printer.print(new String(bytes.toByteArray(), this.charset));
            } catch (final PrinterException ignore) {
            }
        }
    }

    /**
     * Lazily creates a {@link ByteArrayOutputStream}
     */
    private ByteArrayOutputStream bytes() {
        if (null == this.bytes) {
            this.bytes = new ByteArrayOutputStream();
        }
        return this.bytes;
    }

    /**
     * A {@link ByteArrayOutputStream} caches bytes until the next print or flush.
     */
    private ByteArrayOutputStream bytes;

    @Override
    public PrintStream append(final CharSequence chars) {
        try {
            this.printer.print(chars);
        } catch (final PrinterException ignore) {
        }
        return this;
    }

    @Override
    public PrintStream append(final CharSequence chars, final int start, final int end) {
        this.print(chars.subSequence(start, end));
        return this;
    }

    @Override
    public PrintStream append(final char c) {
        this.print(c);
        return this;
    }

    @Override
    public void flush() {
        this.printBytesIfNecessary();
        try {
            this.printer.flush();
        } catch (final PrinterException ignore) {
        }
    }

    /**
     * Close is ignored.
     */
    @Override
    public void close() {
        // nop
    }

    /**
     * Always returns false
     */
    @Override
    public boolean checkError() {
        return false;
    }

    /**
     * The target of all prints, appends, writes etc.
     */
    private final Printer printer;

    /**
     * The {@link LineEnding} used by {@link #println()}.
     */

    private final LineEnding lineEnding;

    /**
     * Used by the write methods.
     */
    private final Charset charset;

    // Object

    /**
     * Dumps the {@link Printer} and {@link LineEnding}
     */
    @Override
    public String toString() {
        return this.printer + " " + CharSequences.quoteAndEscape(this.lineEnding);
    }
}
