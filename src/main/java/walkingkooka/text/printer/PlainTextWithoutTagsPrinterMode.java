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

/**
 * Enums representing the various modes that the state machine can be in. This class is package
 * private for testing purposes.
 */
enum PlainTextWithoutTagsPrinterMode {
    INSERT_SPACE_BEFORE_TEXT {
        @Override
        boolean isText() {
            return true;
        }

        @Override
        void addChar(final char c, final StringBuilder builder) {
            builder.append(' ');
            builder.append(c);
        }

        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '<' ? TAG_NAME_COMMENT_ETC : TEXT;
        }
    },
    //
    TEXT {
        @Override
        boolean isText() {
            return true;
        }

        @Override
        void addChar(final char c, final StringBuilder builder) {
            builder.append(c);
        }

        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '<' ? TAG_NAME_COMMENT_ETC : this;
        }
    },
    //
    TAG_NAME_COMMENT_ETC {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return (c == 'B') || (c == 'b') ? BOLD //
                    : (c == 'I') || (c == 'i') ? ITALICS //
                    : (c == 'U') || (c == 'u') ? UNDERLINE //
                    : c == '!' ? COMMENT //
                    : c == '/' ? TAG_NAME_COMMENT_ETC //
                    : TAG_NAME;
        }
    },
    //
    BOLD {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return this.mightBe(c, printer, "**");
        }
    },
    //
    ITALICS {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return this.mightBe(c, printer, "*");
        }
    },
    //
    UNDERLINE {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return this.mightBe(c, printer, "_");
        }
    },
    //
    TAG_NAME {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '>' ?
                    TEXT :
                    c == '/' ?
                            GREATER_THAN :
                            Character.isWhitespace(c) ? INSIDE_TAG : TAG_NAME;
        }
    },
    //
    INSIDE_TAG {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '>' ?
                    TEXT :
                    c == '\'' ? INSIDE_SINGLE_QUOTES : c == '"' ? DOUBLE_QUOTES : this;
        }
    },
    //
    INSIDE_SINGLE_QUOTES {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '\'' ?
                    INSIDE_TAG :
                    c == '\\' ? ESCAPING_INSIDE_SINGLE_QUOTES : this;
        }
    },
    //
    ESCAPING_INSIDE_SINGLE_QUOTES {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return INSIDE_SINGLE_QUOTES;
        }
    },
    //
    DOUBLE_QUOTES {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '"' ? INSIDE_TAG : c == '\\' ? ESCAPING_INSIDE_DOUBLE_QUOTES : this;
        }
    },
    //
    ESCAPING_INSIDE_DOUBLE_QUOTES {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return DOUBLE_QUOTES;
        }
    },
    //
    COMMENT {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '-' ? COMMENT_DASH : INSIDE_TAG;
        }
    },
    //
    COMMENT_DASH {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '-' ? INSIDE_COMMENT : INSIDE_TAG;
        }
    },
    //
    INSIDE_COMMENT {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '-' ? END_COMMENT_DASH_DASH : this;
        }
    },
    //
    END_COMMENT_DASH_DASH {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '-' ? GREATER_THAN : INSIDE_COMMENT;
        }
    },
    //
    GREATER_THAN {
        @Override
        PlainTextWithoutTagsPrinterMode process(final char c, final Printer printer) {
            return c == '>' ? TEXT : INSIDE_COMMENT;
        }
    };

    boolean isText() {
        return false;
    }

    void addChar(final char c, final StringBuilder builder) {
        throw new UnsupportedOperationException();
    }

    abstract PlainTextWithoutTagsPrinterMode process(char c, Printer printer);

    final PlainTextWithoutTagsPrinterMode mightBe(final char c, final Printer printer,
                                                  final String instead) {
        PlainTextWithoutTagsPrinterMode mode = TAG_NAME;
        do {
            if (c == '>') {
                printer.print(instead);
                mode = TEXT;
                break;
            }
            if ((c == '/') || Character.isWhitespace(c)) {
                printer.print(instead);
                mode = INSIDE_TAG;
                break;
            }
        } while (false);
        return mode;
    }
}