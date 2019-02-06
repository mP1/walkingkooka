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

import org.junit.jupiter.api.Test;
import walkingkooka.text.LineEnding;
import walkingkooka.util.variable.Variable;
import walkingkooka.util.variable.Variables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class CharacterCountingPrinterTest extends PrinterTestCase<CharacterCountingPrinter> {

    // constants

    private final static Printer PRINTER = Printers.fake();

    private final static LineEnding LINE_ENDING = LineEnding.CRNL;

    private final static Variable<Integer> COUNTER = Variables.with(0);

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            CharacterCountingPrinter.wrap(null, COUNTER);
        });
    }

    @Test
    public void testWrapNullCounterFails() {
        assertThrows(NullPointerException.class, () -> {
            CharacterCountingPrinter.wrap(PRINTER, null);
        });
    }

    @Test
    public void testPrintAndCheckCounter() {
        final Variable<Integer> counter = Variables.with(100);
        final StringBuilder builder = new StringBuilder();
        final CharacterCountingPrinter printer = this.createPrinter(builder, counter);
        printer.print("123");
        printer.print("456");
        checkEquals("123456", builder.toString(), "printed");
        assertEquals(106, (int) counter.get(), "counter");
    }

    @Test
    public void testPrintIncludingCRLineEndingsAndCheckCounter() {
        this.printIncludingLineEndingsAndCheckCounter(LineEnding.CR);
    }

    @Test
    public void testPrintIncludingCRNLLineEndingsAndCheckCounter() {
        this.printIncludingLineEndingsAndCheckCounter(LineEnding.CRNL);
    }

    @Test
    public void testPrintIncludingNLLineEndingsAndCheckCounter() {
        this.printIncludingLineEndingsAndCheckCounter(LineEnding.NL);
    }

    @Test
    public void testPrintIncludingNoneLineEndingsAndCheckCounter() {
        this.printIncludingLineEndingsAndCheckCounter(LineEnding.NONE);
    }

    private void printIncludingLineEndingsAndCheckCounter(final LineEnding lineEnding) {
        final Variable<Integer> counter = Variables.with(100);
        final StringBuilder builder = new StringBuilder();
        final CharacterCountingPrinter printer = this.createPrinter(builder, lineEnding, counter);
        printer.print("123");
        printer.print(printer.lineEnding());
        printer.print("456");

        checkEquals("123" + lineEnding + "456", builder.toString(), "printed");
        assertEquals(106 + lineEnding.length(), (int) counter.get(), "counter");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(CharacterCountingPrinter.wrap(PRINTER, Variables.with(Integer.valueOf(123))),
                PRINTER + " 123 char(s)");
    }

    @Override
    protected CharacterCountingPrinter createPrinter() {
        return this.createPrinter(this.stringBuilder, Variables.with(0));
    }

    private CharacterCountingPrinter createPrinter(final StringBuilder printed,
                                                   final Variable<Integer> counter) {
        return this.createPrinter(printed, LINE_ENDING, counter);
    }

    private CharacterCountingPrinter createPrinter(final StringBuilder printed,
                                                   final LineEnding lineEnding, final Variable<Integer> counter) {
        return CharacterCountingPrinter.wrap(Printers.stringBuilder(printed, lineEnding), counter);
    }

    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    public Class<CharacterCountingPrinter> type() {
        return CharacterCountingPrinter.class;
    }
}
