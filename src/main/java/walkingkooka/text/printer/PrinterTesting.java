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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link Printer}.
 */
public interface PrinterTesting<P extends Printer> extends ToStringTesting<P>,
        TypeNameTesting<P> {

    @Test
    default void testPrintNullFails() {
        final P printer = this.createPrinter();
        assertThrows(NullPointerException.class, () -> printer.print(null));
    }

    @Test
    default void testFlush() {
        this.createPrinter().flush();
    }

    @Test
    default void testClose() {
        this.createPrinter().close();
    }

    @Test
    default void testPrintAfterCloseFails() {
        final P printer = this.createPrinterAndClose();

        assertThrows(PrinterException.class, () -> printer.print("print should have been failed because is already closed"));
    }

    @Test
    default void testLineEndingAfterCloseFails() {
        final P printer = this.createPrinterAndClose();

        assertThrows(PrinterException.class, printer::lineEnding);

    }

    @Test
    default void testFlushAfterCloseFails() {
        final P printer = this.createPrinterAndClose();

        assertThrows(PrinterException.class, printer::flush);

    }

    @Test
    default void testCloseAfterCloseFails() {
        final P printer = this.createPrinterAndClose();
        printer.close(); // ignored
    }

    P createPrinter();

    default P createPrinterAndClose() {
        final P printer = this.createPrinter();
        printer.close();
        return printer;
    }

    default void checkEquals(final CharSequence expected,
                             final CharSequence actual) {
        assertEquals(CharSequences.escape(expected),
                CharSequences.escape(actual).toString());
    }

    default void checkEquals(final CharSequence expected,
                             final CharSequence actual,
                             final String message) {
        assertEquals(CharSequences.escape(expected),
                CharSequences.escape(actual).toString(),
                message);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Printer.class.getSimpleName();
    }
}
