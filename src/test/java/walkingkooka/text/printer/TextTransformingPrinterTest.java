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
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class TextTransformingPrinterTest extends PrinterTestCase2<TextTransformingPrinter> {

    // constants

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests............................................................................................................

    @Test
    public void testWithNullTransformerFails() {
        assertThrows(NullPointerException.class, () -> TextTransformingPrinter.with(null, Printers.fake()));
    }

    @Test
    public void testWithNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> TextTransformingPrinter.with(this::transformer, null));
    }

    // lineEnding........................................................................................................

    @Test
    public void testLineEndingCr() {
        this.lineEndingAndCheck(LineEnding.CR);
    }

    @Test
    public void testLineEndingCrNl() {
        this.lineEndingAndCheck(LineEnding.CRNL);
    }

    @Test
    public void testLineEndingNl() {
        this.lineEndingAndCheck(LineEnding.NL);
    }

    private void lineEndingAndCheck(final LineEnding lineEnding) {
        final TextTransformingPrinter printer = this.createPrinter(new FakePrinter() {
            @Override
            public LineEnding lineEnding() throws PrinterException {
                return lineEnding;
            }
        });
        assertSame(lineEnding, printer.lineEnding());
    }

    // print............................................................................................................

    @Test
    public void testPrint() {
        final StringBuilder printed = new StringBuilder();
        final TextTransformingPrinter printer = this.createPrinter(printed);

        printer.print("a");
        printer.print("BC1");
        printer.print("23DEF");
        printer.print("");
        printer.print("456");

        this.checkEquals("ABC123DEF456", printed);
    }

    @Override
    @Test
    public void testPrintNullFails() {
        // nop
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final Printer wrapped = Printers.fake();
        this.toStringAndCheck(this.createPrinter(wrapped), wrapped.toString());
    }

    // helpers...........................................................................................................

    @Override
    public TextTransformingPrinter createPrinter(final StringBuilder target) {
        return this.createPrinter(Printers.stringBuilder(target, LINE_ENDING));
    }

    private TextTransformingPrinter createPrinter(final Printer printer) {
        return TextTransformingPrinter.with(this::transformer, printer);
    }

    private CharSequence transformer(final CharSequence chars) {
        return chars.toString().toUpperCase();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextTransformingPrinter> type() {
        return TextTransformingPrinter.class;
    }
}
