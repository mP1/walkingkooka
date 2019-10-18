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

package walkingkooka.visit;

import walkingkooka.text.Indentation;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Objects;
import java.util.function.Function;

/**
 * A pretty printer that assists with capturing a nested indented tree using a {@link Visitor}.
 */
public final class VisitorPrettyPrinter<T> {

    /**
     * Factory that creates a new {@link VisitorPrettyPrinter}
     */
    public static <T> VisitorPrettyPrinter<T> with(final IndentingPrinter printer,
                                                   final Indentation indentation,
                                                   final Function<T, String> typeName) {
        Objects.requireNonNull(printer, "printer");
        Objects.requireNonNull(indentation, "indentation");
        Objects.requireNonNull(typeName, "typeName");

        return new VisitorPrettyPrinter<>(printer, indentation, typeName);
    }

    /**
     * Attempts to remove the prefix and suffix and returns the reflect name of the given object.
     */
    public static String computeFromClassSimpleName(final Object object, final String prefix, final String suffix) {
        Objects.requireNonNull(object, "object");
        Objects.requireNonNull(prefix, "prefix");
        Objects.requireNonNull(suffix, "suffix");

        final String typeName = object.getClass().getSimpleName();

        final int start = typeName.startsWith(prefix) ?
                prefix.length() :
                0;

        final int end = typeName.endsWith(suffix) ?
                typeName.length() - suffix.length() :
                typeName.length();

        return typeName.substring(start, end);
    }

    /**
     * Package private
     */
    private VisitorPrettyPrinter(final IndentingPrinter printer,
                                 final Indentation indentation,
                                 final Function<T, String> typeName) {
        this.printer = printer;
        this.indentation = indentation;
        this.typeName = typeName;
    }

    /**
     * All startVisit methods should call this.
     */
    public void enter(final T token) {
        this.printer.print(this.typeName(token));
        this.printer.print(this.printer.lineEnding());
        this.printer.indent(this.indentation);
    }

    /**
     * All endVisit methods should call this.
     */
    public void exit(final T token) {
        this.printer.outdent();
    }

    /**
     * All visit methods should call this.
     */
    public void leaf(final T token) {
        this.printer.print(this.typeName(token) + "=" + token);
        this.printer.print(this.printer.lineEnding());
    }

    private String typeName(final T token) {
        return typeName.apply(token);
    }

    private final IndentingPrinter printer;
    private final Indentation indentation;
    private final Function<T, String> typeName;

    @Override
    public String toString() {
        return this.printer.toString();
    }
}
