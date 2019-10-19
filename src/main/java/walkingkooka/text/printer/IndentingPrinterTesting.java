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
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.Indentation;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface IndentingPrinterTesting<P extends IndentingPrinter>
        extends PrinterTesting2<P>,
        TypeNameTesting<P> {


    // constants

    Indentation INDENTATION = Indentation.with("  ");

    // tests

    @Test
    default void testUnmatchedOutdentFails() {
        this.outdentFails(this.createPrinter());
    }

    @Test
    default void testTooManyOutdentsFails() {
        final P printer = this.createPrinter();
        printer.indent();
        printer.outdent();
        this.outdentFails(printer);
    }

    @Test
    default void testTooManyOutdentsFails2() {
        final P printer = this.createPrinter();
        printer.indent();
        printer.indent();
        printer.outdent();
        printer.outdent();
        this.outdentFails(printer);
    }

    default void outdentFails(final P printer) {
        assertThrows(IllegalStateException.class, printer::outdent);
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return IndentingPrinter.class.getSimpleName();
    }
}
