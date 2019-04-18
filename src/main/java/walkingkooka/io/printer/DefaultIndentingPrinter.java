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

import walkingkooka.text.Indentation;

import java.util.Objects;

/**
 * Adds support for writing text that requires some line formatting functionality such of
 * indentation and starting a new line.
 */
final class DefaultIndentingPrinter extends IndentingPrinter2 {

    static DefaultIndentingPrinter wrap(final Printer printer) {
        Objects.requireNonNull(printer, "printer");

        return new DefaultIndentingPrinter(printer);
    }

    /**
     * Private constructor use static factory.
     */
    private DefaultIndentingPrinter(final Printer printer) {
        super(printer);
    }

    @Override
    Indentation appendNewIndentation(final Indentation with, final Indentation preceeding) {
        return preceeding.append(with);
    }

    /**
     * Dumps the wrapped {@link Printer}.
     */
    @Override
    String toString(final Printer printer) {
        return printer.toString();
    }
}
