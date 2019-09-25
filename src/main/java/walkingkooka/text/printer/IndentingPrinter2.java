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

import walkingkooka.collect.stack.Stack;
import walkingkooka.collect.stack.Stacks;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.util.EmptyStackException;
import java.util.Objects;

/**
 * Adds support for writing text that requires some line formatting functionality such of
 * indentation and starting a new line. A single method remains abstract and may be overridden to
 * control which indentation is used.
 */
abstract class IndentingPrinter2 implements IndentingPrinter {
    private final static char NL = '\n';

    private final static char CR = '\r';

    private final static char START_OF_NEW_LINE = IndentingPrinter2.NL;

    /**
     * Package private constructor to limit sub classing.
     */
    IndentingPrinter2(final Printer printer) {
        super();

        this.printer = printer;
        this.indentation = Indentation.EMPTY;
        this.previous = IndentingPrinter2.START_OF_NEW_LINE;
    }

    @Override
    final public void print(final CharSequence chars) throws PrinterException {
        this.print0(null == chars ? "null" : chars);
    }

    /**
     * Assumes that chars is not null.
     */
    private void print0(final CharSequence chars) {
        final Printer printer = this.printer;
        final int end = chars.length();
        final Indentation preceeding = this.indentation;

        // vars
        char previous = this.previous;
        int start = 0;

        for (int i = 0; i < end; i++) {
            final char c = chars.charAt(i);

            if ((IndentingPrinter2.NL == previous) || (
                    (previous == IndentingPrinter2.CR) && (IndentingPrinter2.NL
                            != c))) {
                if (start != i) {
                    printer.print(chars.subSequence(start, i));
                }
                printer.print(preceeding);
                start = i;
            }

            previous = c;
        }
        this.previous = previous;

        if (start != end) {
            printer.print(chars.subSequence(start, end));
        }
    }

    @Override
    final public LineEnding lineEnding() throws PrinterException {
        return this.printer.lineEnding();
    }

    // IndentingPrinter

    @Override
    final public void indent(final Indentation indentation) throws PrinterException {
        Objects.requireNonNull(indentation, "indentation");

        final Indentation before = this.indentation;
        this.indentations.push(before);
        this.indentation = this.appendNewIndentation(indentation, before);
    }

    /**
     * Sub classes may override this to append the new indentation with the preceeding amount of
     * indentation.
     */
    abstract Indentation appendNewIndentation(final Indentation with, Indentation preceeding);

    /**
     * Removes the last indentation.
     */
    @Override
    final public void outdent() throws PrinterException {
        try {
            final Stack<Indentation> indentations = this.indentations;
            this.indentation = indentations.peek();
            indentations.pop();
        } catch (final EmptyStackException empty) {
            throw new IllegalStateException("More outdents than indents");
        }
    }

    private final Stack<Indentation> indentations = Stacks.arrayList();

    /**
     * This string is written to each and every new line.
     */
    private Indentation indentation;

    /**
     * Conditionally inserts a EOL guaranteeing the next print will start on a new line.
     */
    @Override
    final public void lineStart() throws PrinterException {
        final char previous = this.previous;
        if ((IndentingPrinter2.CR != previous) && (IndentingPrinter2.NL != previous)) {
            final Printer printer = this.printer;
            printer.print(printer.lineEnding());
            this.previous = IndentingPrinter2.START_OF_NEW_LINE;
        }
    }

    @Override
    final public void flush() throws PrinterException {
        this.printer.flush();
    }

    @Override
    final public void close() throws PrinterException {
        this.printer.close();
    }

    /**
     * Holds the last character to be added.
     */
    private char previous;

    /**
     * The {@link Printer} that is written too.
     */
    private final Printer printer;

    /**
     * Dumps something meaningful about this {@link IndentingPrinter}.
     */
    @Override
    final public String toString() {
        return this.toString(this.printer);
    }

    /**
     * Force sub classes to override.
     */
    abstract String toString(Printer printer);
}
