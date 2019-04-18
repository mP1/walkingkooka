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

import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link PrintStream} that prints too two {@link PrintStream print stream}.
 */
final class TeePrintStream extends PrintStream {

    /**
     * Creates a {@link TeePrintStream} provided the two {@link PrintStream} are different.
     */
    static PrintStream wrap(final PrintStream first, final PrintStream second) {
        Objects.requireNonNull(first, "first PrintStream");
        Objects.requireNonNull(second, "second PrintStream");

        return first.equals(second) ? first : new TeePrintStream(first, second);
    }

    /**
     * Private constructor use factory
     */
    private TeePrintStream(final PrintStream first, final PrintStream second) {
        super(PrintStreams.DUMMY_OUTPUTSTREAM);
        this.first = first;
        this.second = second;
    }

    @Override
    public void print(final boolean b) {
        this.first.print(b);
        this.second.print(b);
    }

    @Override
    public void print(final char c) {
        this.first.print(c);
        this.second.print(c);
    }

    @Override
    public void print(final int i) {
        this.first.print(i);
        this.second.print(i);
    }

    @Override
    public void print(final long l) {
        this.first.print(l);
        this.second.print(l);
    }

    @Override
    public void print(final float f) {
        this.first.print(f);
        this.second.print(f);
    }

    @Override
    public void print(final double d) {
        this.first.print(d);
        this.second.print(d);
    }

    @Override
    public void print(final char[] s) {
        this.first.print(s);
        this.second.print(s);
    }

    @Override
    public void print(final String s) {
        this.first.print(s);
        this.second.print(s);
    }

    @Override
    public void print(final Object object) {
        this.first.print(object);
        this.second.print(object);
    }

    @Override
    public void println() {
        this.first.println();
        this.second.println();
    }

    @Override
    public void println(final boolean x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final char x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final int x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final long x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final float x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final double x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final char[] x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final String x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public void println(final Object x) {
        this.first.println(x);
        this.second.println(x);
    }

    @Override
    public PrintStream printf(final String format, final Object... args) {
        this.first.printf(format, args);
        this.second.printf(format, args);
        return this;
    }

    @Override
    public PrintStream printf(final Locale locale, final String format, final Object... args) {
        this.first.printf(locale, format, args);
        this.second.printf(locale, format, args);
        return this;
    }

    @Override
    public PrintStream format(final String format, final Object... args) {
        this.first.format(format, args);
        this.second.format(format, args);
        return this;
    }

    @Override
    public PrintStream format(final Locale locale, final String format, final Object... args) {
        this.first.format(locale, format, args);
        this.second.format(locale, format, args);
        return this;
    }

    @Override
    public PrintStream append(final CharSequence chars) {
        this.first.append(chars);
        this.second.append(chars);
        return this;
    }

    @Override
    public PrintStream append(final CharSequence chars, final int start, final int end) {
        this.first.append(chars, start, end);
        this.second.append(chars, start, end);
        return this;
    }

    @Override
    public PrintStream append(final char c) {
        this.first.append(c);
        this.second.append(c);
        return this;
    }

    @Override
    public void write(final int b) {
        this.first.write(b);
        this.second.write(b);
    }

    @Override
    public void write(final byte[] buf, final int off, final int len) {
        this.first.write(buf, off, len);
        this.second.write(buf, off, len);
    }

    @Override
    public void write(final byte[] buffer) throws IOException {
        this.first.write(buffer);
        this.second.write(buffer);
    }

    @Override
    public void flush() {
        this.first.flush();
        this.second.flush();
    }

    @Override
    public void close() {
        this.first.close();
        this.second.close();
    }

    /**
     * Returns true if either {@link PrintStream} has an error.
     */
    @Override
    public boolean checkError() {
        return this.first.checkError() || this.second.checkError();
    }

    private final PrintStream first;

    private final PrintStream second;

    // Object

    @Override
    public String toString() {
        return this.first.toString() + ' ' + this.second;
    }
}
