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

package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.visit.VisitorPrettyPrinter;

import java.util.Optional;

/**
 * Used by {@link ParserTesting} to create an indented tree like value of {@link ParserToken tokens} in a tree.
 */
final class ParserTestingPrettyDumper {

    static String dump(final Optional<ParserToken> token) {
        return token.map(t -> dump0(t))
                .orElse(null);
    }

    private static String dump0(final ParserToken token) {
        final StringBuilder b = new StringBuilder();

        try (final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.NL))) {
            dump(token, VisitorPrettyPrinter.with(printer,
                    Indentation.with("  "),
                    (t) -> VisitorPrettyPrinter.computeFromClassSimpleName(t, "", ParserToken.class.getSimpleName())));
            printer.flush();
        }

        return b.toString();
    }

    private static void dump(final ParserToken token,
                             final VisitorPrettyPrinter<ParserToken> printer) {
        if (token instanceof LeafParserToken) {
            printer.leaf(token);
        }
        if (token instanceof ParentParserToken) {
            dumpParent(Cast.to(token), printer);
        }
    }

    private static void dumpParent(final ParentParserToken<?> token,
                                   final VisitorPrettyPrinter<ParserToken> printer) {
        printer.enter(token);
        token.value().forEach(t -> dump(t, printer));
        printer.exit(token);
    }

    private ParserTestingPrettyDumper() {
        throw new UnsupportedOperationException();
    }
}
