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

import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;

import java.util.Objects;

/**
 * A {@link IndentingPrinter} which ignores the actual amount of whitespace passed to {@link
 * IndentingPrinter#indent(Indentation)} and uses a fixed {@link String amount}.
 */
final class FixedIndentingPrinter extends IndentingPrinter2 {

    static FixedIndentingPrinter wrap(final Printer printer, final Indentation indentation) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(indentation, "indentation");

        return new FixedIndentingPrinter(printer, indentation);
    }

    /**
     * Private constructor use static factory
     */
    private FixedIndentingPrinter(final Printer printer, final Indentation indentation) {
        super(printer);
        this.indentation = indentation;
    }

    /**
     * Ignores the requested indentation amount and uses the fixed indentation passed to the
     * factory.
     */
    @Override
    Indentation appendNewIndentation(final Indentation with, final Indentation preceeding) {
        return preceeding.append(this.indentation);
    }

    /**
     * The {@link Indentation} that is used instead of any supplied.
     */
    private final Indentation indentation;

    @Override
    String toString(final Printer printer) {
        return printer + " indent=" + CharSequences.quoteAndEscape(this.indentation.value());
    }
}
