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
 * A {@link IndentingPrinter} which ignores attempts to indent more than the maximum limit.
 */
final class MaximumIndentationIndentingPrinter extends IndentingPrinter2 {

    static MaximumIndentationIndentingPrinter wrap(final Printer printer, final int maxIndentation) {
        Objects.requireNonNull(printer, "printer");
        if (maxIndentation < 0) {
            throw new IllegalArgumentException(
                    "MaxIndentation " + maxIndentation + " must be greater than 0");
        }

        return new MaximumIndentationIndentingPrinter(printer, maxIndentation);
    }

    /**
     * Private constructor use static factory
     */
    private MaximumIndentationIndentingPrinter(final Printer printer, final int maxIndentation) {
        super(printer);

        this.maxIndentation = maxIndentation;
    }

    /**
     * Checks that the new indentation is not over the limit and if so cuts of appropriate.
     */
    @Override
    Indentation appendNewIndentation(final Indentation with, final Indentation preceeding) {
        final int preceedingLength = preceeding.length();

        Indentation result;
        do {
            final int limit = this.maxIndentation;
            if (preceedingLength == limit) {
                result = preceeding;
                break;
            }

            final int withLength = with.length();
            final int total = preceedingLength + withLength;
            if (total < limit) {
                result = preceeding.append(with);
                break;
            }

            result = preceeding.append(with.subSequence(0, withLength - (total - limit)));
        } while (false);

        return result;
    }

    /**
     * The maximum amount of indentation allowed.
     */
    private final int maxIndentation;

    @Override
    String toString(final Printer printer) {
        return printer + " maxIndentation=" + this.maxIndentation;
    }
}
