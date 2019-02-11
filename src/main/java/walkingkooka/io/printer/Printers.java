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

import walkingkooka.io.printer.line.PrintedLineHandler;
import walkingkooka.text.LineEnding;
import walkingkooka.type.PublicStaticHelper;

import java.io.PrintStream;
import java.io.Writer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntUnaryOperator;

final public class Printers implements PublicStaticHelper {

    /**
     * {@see CharacterCountingPrinter}
     */
    public static Printer characterCounting(final Printer printer, final IntConsumer counter) {
        return CharacterCountingPrinter.wrap(printer, counter);
    }

    /**
     * {@see HtmlEntityDecodingPrinter}.
     */
    public static Printer htmlEntityDecoder(final Function<String, String> entityDecoder,
                                            final Printer printer) {
        return HtmlEntityDecodingPrinter.wrap(entityDecoder, printer);
    }

    /**
     * {@see FakePrinter}.
     */
    public static Printer fake() {
        return FakePrinter.create();
    }

    /**
     * {@see LineCountingPrinter}
     */
    public static Printer lineCounter(final Printer printer, final IntConsumer counter) {
        return LineCountingPrinter.wrap(printer, counter);
    }

    /**
     * {@see LineEndingPrinter}
     */
    public static Printer lineEnding(final Printer printer) {
        return LineEndingPrinter.wrap(printer);
    }

    /**
     * {@see LineEndingTransformingPrinter}
     */
    public static Printer lineEndingTransforming(final Function<LineEnding, LineEnding> transformer,
                                                 final Printer printer) {
        return LineEndingTransformingPrinter.wrap(transformer, printer);
    }

    /**
     * {@see NullReplacingPrinter}.
     */
    public static Printer nullReplacing(final Printer printer, final String replacement) {
        return NullReplacingPrinter.wrap(printer, replacement);
    }

    /**
     * {@see PlainTextWithoutTagsPrinter}
     */
    public static Printer plainTextWithoutTags(final Printer printer) {
        return PlainTextWithoutTagsPrinter.wrap(printer);
    }

    /**
     * {@see PrintedLineHandlerPrinter}
     */
    public static Printer printedLine(final Printer printer, final PrintedLineHandler handler) {
        return PrintedLineHandlerPrinter.wrap(printer, handler);
    }

    /**
     * {@see PrintStreamPrinter}.
     */
    public static Printer printStream(final PrintStream printStream, final LineEnding lineEnding) {
        return PrintStreamPrinter.with(printStream, lineEnding);
    }

    /**
     * {@see SeparatorAddingPrinter}.
     */
    public static Printer separatedBy(final Printer printer, final String separator) {
        return SeparatorAddingPrinter.wrap(printer, separator);
    }

    /**
     * {@see SinkPrinter}.
     */
    public static Printer sink() {
        return SinkPrinter.INSTANCE;
    }

    /**
     * {@see NullSkippingPrinter}.
     */
    public static Printer skipNulls(final Printer printer) {
        return NullSkippingPrinter.wrap(printer);
    }

    /**
     * {@see StringBuilderPrinter}.
     */
    public static Printer stringBuilder(final StringBuilder builder, final LineEnding lineEnding) {
        return StringBuilderPrinter.with(builder, lineEnding);
    }

    /**
     * {@see PrintStreamPrinter}.
     */
    public static Printer sysErr() {
        return PrintStreamPrinter.sysErr();
    }

    /**
     * {@see PrintStreamPrinter}
     */
    public static Printer sysOut() {
        return PrintStreamPrinter.sysOut();
    }

    /**
     * {@see TabExpandingPrinter}
     */
    public static Printer tabExpanding(final Printer printer, final IntUnaryOperator tabStops) {
        return TabExpandingPrinter.wrap(printer, tabStops);
    }

    /**
     * {@see TeePrinter}.
     */
    public static Printer tee(final Printer first, final Printer second) {
        return TeePrinter.wrap(first, second);
    }

    /**
     * {@see UnclosablePrinter}
     */
    public static Printer unclosable(final Printer printer) {
        return UnclosablePrinter.wrap(printer);
    }

    /**
     * {@see WhitespaceCleaningPrinter}.
     */
    public static Printer whitespaceCleaning(final Printer printer) {
        return WhitespaceCleaningPrinter.wrap(printer);
    }

    /**
     * {@see WriterPrinter}
     */
    public static Printer writer(final Writer writer, final LineEnding lineEnding) {
        return WriterPrinter.adapt(writer, lineEnding);
    }

    /**
     * Stop creation
     */
    private Printers() {
        throw new UnsupportedOperationException();
    }
}
