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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link PrintedLineHandler}.
 */
public interface PrintedLineHandlerTesting<H extends PrintedLineHandler>
        extends ToStringTesting<H>,
        TypeNameTesting<H> {

    // tests

    @Test
    default void testNullLineFails() {
        assertThrows(NullPointerException.class, () -> this.createLineHandler().linePrinted(null,
                LineEnding.NL,
                Printers.fake()));
    }

    @Test
    default void testNullLineEndingFails() {
        assertThrows(NullPointerException.class, () -> this.createLineHandler().linePrinted("", null, Printers.fake()));
    }

    @Test
    default void testNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> this.createLineHandler().linePrinted("",
                LineEnding.NL,
                null));
    }

    H createLineHandler();

    default void linePrintedAndCheck(final CharSequence line,
                                     final LineEnding lineEnding) {
        this.linePrintedAndCheck(line, lineEnding, line.toString());
    }

    default void linePrintedAndCheck(final CharSequence line, final LineEnding lineEnding,
                                     final String expected) {
        this.linePrintedAndCheck(line, lineEnding, expected, null);
    }

    default void linePrintedAndCheck(final CharSequence line,
                                     final LineEnding lineEnding,
                                     final String expected,
                                     final String message) {
        this.linePrintedAndCheck(this.createLineHandler(), line, lineEnding, expected, message);
    }

    default void linePrintedAndCheck(final PrintedLineHandler handler,
                                     final CharSequence line,
                                     final LineEnding lineEnding,
                                     final String expected) {
        this.linePrintedAndCheck(handler, line, lineEnding, expected, null);
    }

    default void linePrintedAndCheck(final PrintedLineHandler handler,
                                     final CharSequence line,
                                     final LineEnding lineEnding,
                                     final String expected,
                                     final String message) {
        Objects.requireNonNull(handler, "handler");
        Objects.requireNonNull(line, "line");

        final String lineString = line.toString();
        if (lineString.contains("\r\n")) {
            Assertions.fail("Line contains CRNL=" + CharSequences.quoteAndEscape(lineString));
        }
        if (lineString.contains("\r")) {
            Assertions.fail("Line contains CR=" + CharSequences.quoteAndEscape(lineString));
        }
        if (lineString.contains("\n")) {
            Assertions.fail("Line contains NL=" + CharSequences.quoteAndEscape(lineString));
        }

        assertNotNull(lineEnding, "lineEnding");
        assertNotNull(expected, "expected");

        final StringBuilder printedBuffer = new StringBuilder();
        final Printer printer = Printers.stringBuilder(printedBuffer,
                LineEnding.NL);
        handler.linePrinted(line, lineEnding, printer);
        printer.flush();
        printer.close();

        final String printed = printedBuffer.toString();
        if (false == printed.equals(expected)) {
            assertEquals(CharSequences.quoteAndEscape(expected),
                    CharSequences.quoteAndEscape(printed),
                    message);
        }
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return PrintedLineHandler.class.getSimpleName();
    }
}
