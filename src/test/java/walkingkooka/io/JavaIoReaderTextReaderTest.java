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

package walkingkooka.io;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.JavaVisibility;

import java.io.Reader;
import java.io.StringReader;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JavaIoReaderTextReaderTest implements TextReaderTesting<JavaIoReaderTextReader>,
    ToStringTesting<JavaIoReaderTextReader> {

    /**
     * A silent {@link Consumer}.
     */
    private final static Consumer<Character> ECHO = (c) -> {};

    // with.............................................................................................................

    @Test
    public void testWithNullReaderFails() {
        assertThrows(
            NullPointerException.class,
            () -> JavaIoReaderTextReader.with(
                null,
                ECHO
            )
        );
    }

    @Test
    public void testWithNullEchoFails() {
        assertThrows(
            NullPointerException.class,
            () -> JavaIoReaderTextReader.with(
                new StringReader(""),
                null // echo
            )
        );
    }

    // readText.........................................................................................................

    @Test
    public void testReadTextWithMaxZero() {
        this.readTextAndCheck(
            "abcdef",
            0,
            100,
            ""
        );
    }

    @Test
    public void testReadText() {
        this.readTextAndCheck(
            "abcdef",
            3,
            100,
            "abc"
        );
    }

    @Test
    public void testReadTextWithSkipLfTrue() {
        final JavaIoReaderTextReader reader = this.createTextReader("abcdef");
        reader.skipLf = true;

        this.readTextAndCheck(
            reader,
            3,
            100,
            "abc"
        );
    }

    @Test
    public void testReadTextTimeouts() {
        final JavaIoReaderTextReader reader = JavaIoReaderTextReader.with(
            new Reader() {

                @Override
                public boolean ready() {
                    return true;
                }

                @Override
                public int read(final char[] buffer,
                                final int offset,
                                final int length) {
                    JavaIoReaderTextReader.sleep(100);
                    buffer[offset] = "abcdefghijklmnopqrstuv".charAt(this.step++);
                    return 1;
                }

                int step = 0;

                @Override
                public void close() {

                }
            },
            ECHO
        );

        this.readTextAndCheck(
            reader,
            10,
            250,
            "abc"
        );
    }

    private void readTextAndCheck(final String reader,
                                  final int max,
                                  final long timeout,
                                  final String expected) {
        final StringBuilder echo = new StringBuilder();

        this.readTextAndCheck(
            this.createTextReader(
                reader,
                echo::append
            ),
            max,
            timeout,
            expected
        );

        this.echoCheck(
            echo,
            expected
        );
    }

    // readLine.........................................................................................................

    @Test
    public void testReadLineMissingEol() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abcdef",
            echo::append,
            300
        );
        this.skipLfAndCheck(
            reader,
            false
        );
        this.bufferAndCheck(
            reader,
            "abc"
        );

        this.echoCheck(
            echo,
            "abc"
        );
    }

    @Test
    public void testReadLineTimeout() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abcdef\r",
            echo::append,
            300
        );
        this.skipLfAndCheck(
            reader,
            false
        );
        this.bufferAndCheck(
            reader,
            "abc"
        );

        this.echoCheck(
            echo,
            "abc"
        );
    }

    @Test
    public void testReadLineTimeout2() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abcdef\n",
            echo::append,
            300
        );

        this.skipLfAndCheck(
            reader,
            false
        );
        this.bufferAndCheck(
            reader,
            "abc"
        );

        this.echoCheck(
            echo,
            "abc"
        );
    }

    @Test
    public void testReadLineEmptyEndsWithCr() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "\rabc",
            echo::append,
            100,
            ""
        );

        this.skipLfAndCheck(
            reader,
            true
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "\r"
        );
    }

    @Test
    public void testReadLineEndsWithCr() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abc\rdef",
            echo::append,
            400,
            "abc"
        );

        this.skipLfAndCheck(
            reader,
            true
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "abc\r"
        );
    }

    @Test
    public void testReadLineEmptyEndsWithCrNl() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "\r\nabc",
            echo::append,
            400,
            ""
        );

        this.skipLfAndCheck(
            reader,
            true
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "\r"
        );
    }

    @Test
    public void testReadLineEndsWithCrNl() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abc\r\ndef",
            echo::append,
            400,
            "abc"
        );

        this.skipLfAndCheck(
            reader,
            true
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "abc\r"
        );
    }

    @Test
    public void testReadLineEmptyEndsWithNl() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "\nabc",
            echo::append,
            100,
            ""
        );

        this.skipLfAndCheck(
            reader,
            false
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "\n"
        );
    }

    @Test
    public void testReadLineEndsWithNl() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abc\ndef",
            echo::append,
            400,
            "abc"
        );

        this.skipLfAndCheck(
            reader,
            false
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "abc\n"
        );
    }

    @Test
    public void testReadLineCrThenReadText() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abc\rdefghijklmnopqrstuv",
            echo::append,
            400,
            "abc"
        );

        this.skipLfAndCheck(
            reader,
            true
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "abc\r"
        );

        this.readTextAndCheck(
            reader,
            3,
            300,
            "def"
        );
    }

    @Test
    public void testReadLineCrLfThenReadText() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abc\r\ndefghijklmnopqrstuv",
            echo::append,
            400,
            "abc"
        );

        this.skipLfAndCheck(
            reader,
            true
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "abc\r"
        );

        this.readTextAndCheck(
            reader,
            3,
            300,
            "de" // 100 for N, 100 for d, 100 for e
        );
    }

    @Test
    public void testReadLineLfThenReadText() {
        final StringBuilder echo = new StringBuilder();

        final JavaIoReaderTextReader reader = this.readLineAndCheck(
            "abc\ndefghijklmnopqrstuv",
            echo::append,
            400,
            "abc"
        );

        this.skipLfAndCheck(
            reader,
            false
        );
        this.bufferAndCheck(
            reader,
            ""
        );

        this.echoCheck(
            echo,
            "abc\n"
        );

        this.readTextAndCheck(
            reader,
            3,
            300,
            "def"
        );
    }

    private JavaIoReaderTextReader readLineAndCheck(final String text,
                                                    final Consumer<Character> echo,
                                                    final long timeout) {
        return this.readLineAndCheck(
            text,
            echo,
            timeout,
            Optional.empty()
        );
    }

    private JavaIoReaderTextReader readLineAndCheck(final String text,
                                                    final Consumer<Character> echo,
                                                    final long timeout,
                                                    final String expected) {
        return this.readLineAndCheck(
            text,
            echo,
            timeout,
            Optional.of(expected)
        );
    }

    private JavaIoReaderTextReader readLineAndCheck(final String text,
                                                    final Consumer<Character> echo,
                                                    final long timeout,
                                                    final Optional<String> expected) {
        final JavaIoReaderTextReader reader = JavaIoReaderTextReader.with(
            new Reader() {

                @Override
                public boolean ready() {
                    return true;
                }

                @Override
                public int read(final char[] buffer,
                                final int offset,
                                final int length) {
                    JavaIoReaderTextReader.sleep(100);
                    buffer[offset] = text.charAt(this.step++);
                    return 1;
                }

                int step = 0;

                @Override
                public void close() {

                }
            },
            echo
        );

        this.readLineAndCheck(
            reader,
            timeout,
            expected
        );

        return reader;
    }

    private void skipLfAndCheck(final JavaIoReaderTextReader reader,
                                final boolean expected) {
        this.checkEquals(
            expected,
            reader.skipLf
        );
    }

    private void bufferAndCheck(final JavaIoReaderTextReader reader,
                                final String expected) {
        this.checkEquals(
            expected,
            reader.buffer.toString()
        );
    }

    private void echoCheck(final StringBuilder echo,
                           final String expected) {
        this.checkEquals(
            echo.toString(),
            expected,
            "echo"
        );
    }

    // TextReaderTesting................................................................................................

    @Override
    public JavaIoReaderTextReader createTextReader() {
        return this.createTextReader("");
    }

    private JavaIoReaderTextReader createTextReader(final String text) {
        return this.createTextReader(
            text,
            ECHO
        );
    }

    private JavaIoReaderTextReader createTextReader(final String text,
                                                    final Consumer<Character> echo) {
        return JavaIoReaderTextReader.with(
            new StringReader(text),
            echo
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        final Reader reader = new StringReader("hello");

        this.toStringAndCheck(
            JavaIoReaderTextReader.with(
                reader,
                ECHO
            ),
            reader.toString()
        );
    }

    // class............................................................................................................

    @Override
    public Class<JavaIoReaderTextReader> type() {
        return JavaIoReaderTextReader.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
