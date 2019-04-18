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

import walkingkooka.test.Fake;

import java.io.PrintStream;
import java.util.Locale;

/**
 * A {@link PrintStream} where all methods throw {@link UnsupportedOperationException}
 */
public class FakePrintStream extends PrintStream implements Fake {
    static FakePrintStream create() {
        return new FakePrintStream();
    }

    protected FakePrintStream() {
        super(PrintStreams.DUMMY_OUTPUTSTREAM);
    }

    @Override
    public boolean checkError() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(final int b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(final byte[] buffer, final int offset, final int length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final boolean b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final char c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final long l) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final float f) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final double d) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final char[] s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(final Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final boolean x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final char x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final int x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final long x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final float x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final char[] x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final String x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void println(final Object x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream printf(final String format, final Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream printf(final Locale locale, final String format, final Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream format(final String format, final Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream format(final Locale locale, final String format, final Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream append(final CharSequence chars) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream append(final CharSequence chars, final int start, final int end) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintStream append(final char c) {
        throw new UnsupportedOperationException();
    }
}
