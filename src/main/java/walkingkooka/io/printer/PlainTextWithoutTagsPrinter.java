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

/**
 * A {@link Printer} that removes all HTML tags leaving just the plain text including all
 * whitespace. The following tags are special cases:
 * <ul>
 * <li>Bold **</li>
 * <li>Italics *</li>
 * <li>Underline _</li>
 * </ul>
 * No attempt is normalise whitespace in anyway. Balanced and unbalanced HTML / XML works equally
 * well. This is particularly useful when of wishes to create a plain text form of a HTML document.
 */
final class PlainTextWithoutTagsPrinter implements Printer {

    /**
     * Creates a new {@link PlainTextWithoutTagsPrinter}.
     */
    static PlainTextWithoutTagsPrinter wrap(final Printer printer) {
        Objects.requireNonNull(printer, "printer");

        return new PlainTextWithoutTagsPrinter(printer);
    }

    /**
     * Private constructor
     */
    private PlainTextWithoutTagsPrinter(final Printer printer) {
        this.printer = printer;
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        Objects.requireNonNull(chars, "chars");

        final int length = chars.length();

        if (length > 0) {
            PlainTextWithoutTagsPrinterMode mode = this.mode;
            int i = 0;
            final Printer printer = this.printer;
            final StringBuilder builder = this.builder;
            char lastTextCharacter = this.lastTextCharacter;

            Loop:
            //
            do {
                if (mode.isText()) {
                    do {
                        final char c = chars.charAt(i);
                        i++;
                        if ('<' == c) {
                            printer.print(builder);
                            builder.setLength(0);
                            mode = PlainTextWithoutTagsPrinterMode.TAG_NAME_COMMENT_ETC;
                            break;
                        }
                        if (Character.isWhitespace(c)) {
                            if (false == Character.isWhitespace(lastTextCharacter)) {
                                builder.append(c);
                            }
                            lastTextCharacter = c;
                            if (length == i) {
                                printer.print(builder);
                                builder.setLength(0);
                                break Loop;
                            }
                            continue;
                        }
                        lastTextCharacter = c;
                        mode.addChar(c, builder);
                        mode = PlainTextWithoutTagsPrinterMode.TEXT;
                        if (length == i) {
                            printer.print(builder);
                            builder.setLength(0);
                            break Loop;
                        }
                    } while (true);
                }
                while ((i < length) && (false == mode.isText())) {
                    mode = mode.process(chars.charAt(i), printer);
                    i++;
                }
                if (mode.isText() && (false == Character.isWhitespace(lastTextCharacter))) {
                    mode = PlainTextWithoutTagsPrinterMode.INSERT_SPACE_BEFORE_TEXT;
                }
            } while (i < length);

            this.mode = mode;
            this.lastTextCharacter = lastTextCharacter;
        }
    }

    /**
     * Always returns the wrapped {@link Printer#lineEnding()}.
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
     * All filtered and modified content is written to this {@link Printer}.
     */
    private final Printer printer;

    /**
     * The current mode of the state machine indicating that the content of an element is expected.
     * This field is package private for testing purposes.
     */
    PlainTextWithoutTagsPrinterMode mode = PlainTextWithoutTagsPrinterMode.TEXT;

    /**
     * A {@link StringBuilder} that is filled char by char and then added to the {@link Printer}
     * when any chars being added is processed.
     */
    private final StringBuilder builder = new StringBuilder();

    /**
     * The last text character added. This helps eliminate redundant whitespace being added, and
     * adds space between tags where necessary.
     */
    private char lastTextCharacter = ' ';

    @Override
    public String toString() {
        return "text w/out tags AND " + this.printer;
    }
}
