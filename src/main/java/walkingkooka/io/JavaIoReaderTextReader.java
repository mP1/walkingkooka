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

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.util.OpenChecker;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Wraps a {@link Reader}. The {@link Consumer} argument is intended to allow echoing of each character as it is consumed
 * and buffered and is necessary when reading lines of text. Without the immediate echoing, the text for each line
 * would not appear until the EOL such as CR or NL is entered.
 */
@GwtIncompatible
final class JavaIoReaderTextReader implements TextReader {

    static JavaIoReaderTextReader with(final Reader reader,
                                       final Consumer<Character> echo) {
        return new JavaIoReaderTextReader(
            Objects.requireNonNull(reader, "reader"),
            Objects.requireNonNull(echo, "echo")
        );
    }

    private JavaIoReaderTextReader(final Reader reader,
                                   final Consumer<Character> echo) {
        this.reader = reader;
        this.echo = echo;

        this.openChecker = OpenChecker.with(
            "Terminal closed",
            (String message) -> new IllegalStateException(message)
        );
    }

    @Override
    public boolean isOpen() {
        return false == this.openChecker.isClosed();
    }

    @Override
    public void close() {
        try {
            this.reader.close();
        } catch (final IOException rethrow) {
            throw new UncheckedIOException(rethrow);
        }
    }

    @Override
    public String readText(final int max,
                           final long timeout) {
        if (max < 0) {
            throw new IllegalArgumentException("Invalid max " + max + " < 0");
        }
        if (timeout < 0) {
            throw new IllegalArgumentException("Invalid timeout " + timeout + " < 0");
        }

        this.openChecker.check();

        String text = "";

        if (max > 0) {
            final StringBuilder readText = new StringBuilder();
            final Consumer<Character> echo = this.echo;

            // copy buffered characters....
            final StringBuilder buffer = this.buffer;
            final int bufferLength = buffer.length();

            {
                final int copy = Math.min(
                    max,
                    bufferLength
                );

                for (int i = 0; i < copy; i++) {
                    final char c = buffer.charAt(i);
                    echo.accept(c);

                    if (this.skipLf) {
                        this.skipLf = false;
                        if (NL == c) {
                            continue;
                        }
                    }
                    readText.append(c);
                }
            }

            buffer.setLength(0);
            int missingCount = max - readText.length();

            if (missingCount > 0) {
                // need to read $reader with timeout blocking
                final long stop = System.currentTimeMillis() + timeout;
                final Reader reader = this.reader;

                try {
                    Loop:
                    for (; ; ) {
                        // try fill buffer one character at a time.
                        if (reader.ready()) {
                            final int characterOrEof = reader.read();
                            if (-1 == characterOrEof) {
                                break; // EOS give up.
                            }

                            final char c = (char) characterOrEof;
                            echo.accept(c);

                            if (this.skipLf && NL == c) {
                                this.skipLf = false;
                            } else {
                                this.skipLf = false;

                                readText.append(c);

                                if (readText.length() >= max) {
                                    break;
                                }
                            }

                            if (stop - System.currentTimeMillis() <= 0) {
                                break;
                            }
                        } else {
                            // sleep when reader is NOT ready
                            final long now = System.currentTimeMillis();
                            if (stop - now <= 0) {
                                break;
                            }
                            sleep(stop - now);
                        }
                    }
                } catch (final IOException rethrow) {
                    throw new UncheckedIOException(rethrow);
                }
            }

            text = readText.toString();
        }

        return text;
    }

    @Override
    public Optional<String> readLine(final long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("Invalid timeout " + timeout + " < 0");
        }

        this.openChecker.check();

        // check buffer might already have a line
        final StringBuilder buffer = this.buffer;
        final Consumer<Character> echo = this.echo;

        final StringBuilder readLine = new StringBuilder();

        boolean lineFound = false;

        final long stop = System.currentTimeMillis() + timeout;
        int bufferScanStart = 0;

        WhileLineFoundNot:
        while (false == lineFound) {
            // try and find line in #buffer
            for (int i = bufferScanStart; i < buffer.length(); i++) {
                final char c = buffer.charAt(i);

                if (this.skipLf) {
                    this.skipLf = false;
                    if (NL == c) {
                        continue;
                    }
                }

                if (NL == c || CR == c) {
                    // delete from buffer
                    buffer.delete(
                        0,
                        i + 1
                    );

                    lineFound = true;
                    skipLf = CR == c;
                    break WhileLineFoundNot;
                }

                readLine.append(c);
            }

            // need to read more characters
            final long now = System.currentTimeMillis();
            if (now >= stop) {
                break;
            }

            bufferScanStart = buffer.length();

            // try and fill buffer
            final Reader reader = this.reader;
            final char[] readerBuffer = new char[80];

            try {
                if (reader.ready()) {
                    final int readCount = reader.read(readerBuffer);
                    if (-1 == readCount) {
                        break;
                    }

                    for (int i = 0; i < readCount; i++) {
                        final char c = readerBuffer[i];
                        echo.accept(c);

                        if (this.skipLf && NL == c) {
                            this.skipLf = false;
                            continue;
                        }
                        this.skipLf = false;

                        buffer.append(c);
                    }
                } else {
                    sleep(stop - now);
                }
            } catch (final IOException rethrow) {
                throw new UncheckedIOException(rethrow);
            }
        }

        return Optional.ofNullable(
            lineFound ?
                readLine.toString() :
                null
        );
    }

    private static final char NL = '\n';
    private static final char CR = '\r';

    private final OpenChecker<IllegalStateException> openChecker;

    private final Reader reader;

    private final Consumer<Character> echo;

    // @VisibleForTesting
    final StringBuilder buffer = new StringBuilder();

    /**
     * Only becomes true after a {@link #readLine(long)} that was terminated by CR.
     */
    // @VisibleForTesting
    boolean skipLf;

    // @VisibleForTesting
    static void sleep(final long sleep) {
        try {
            Thread.sleep(sleep);
        } catch (final InterruptedException ignore) {
            // nop
        }
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.reader.toString();
    }
}
