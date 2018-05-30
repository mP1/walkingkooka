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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.text.LineEnding;
import walkingkooka.util.variable.Variable;
import walkingkooka.util.variable.Variables;

final public class CharacterCountingPrinterTest extends PrinterTestCase<CharacterCountingPrinter> {

    // constants

    private final static Printer PRINTER = Printers.fake();

    private final static LineEnding LINE_ENDING = LineEnding.CRNL;

    private final static Variable<Integer> COUNTER = Variables.with(0);

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        this.wrapFails(null, CharacterCountingPrinterTest.COUNTER);
    }

    @Test
    public void testWrapNullCounterFails() {
        this.wrapFails(CharacterCountingPrinterTest.PRINTER, null);
    }

    private void wrapFails(final Printer printer, final Variable<Integer> counter) {
        try {
            CharacterCountingPrinter.wrap(printer, counter);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testPrintAndCheckCounter() {
        final Variable<Integer> counter = Variables.with(100);
        final StringBuilder builder = new StringBuilder();
        final CharacterCountingPrinter printer = this.createPrinter(builder, counter);
        printer.print("123");
        printer.print("456");
        assertEquals("printed", "123456", builder.toString());
        Assert.assertEquals("counter", 106, (int) counter.get());
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

        assertEquals("printed", "123" + lineEnding + "456", builder.toString());
        Assert.assertEquals("counter", 106 + lineEnding.length(), (int) counter.get());
    }

    @Test
    public void testToString() {
        assertEquals(CharacterCountingPrinterTest.PRINTER + " 123 char(s)",
                CharacterCountingPrinter.wrap(CharacterCountingPrinterTest.PRINTER,
                        Variables.with(Integer.valueOf(123))).toString());
    }

    @Override
    protected CharacterCountingPrinter createPrinter() {
        return this.createPrinter(this.stringBuilder, Variables.with(0));
    }

    private CharacterCountingPrinter createPrinter(final StringBuilder printed,
                                                   final Variable<Integer> counter) {
        return this.createPrinter(printed, CharacterCountingPrinterTest.LINE_ENDING, counter);
    }

    private CharacterCountingPrinter createPrinter(final StringBuilder printed,
                                                   final LineEnding lineEnding, final Variable<Integer> counter) {
        return CharacterCountingPrinter.wrap(Printers.stringBuilder(printed, lineEnding), counter);
    }

    private final StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected Class<CharacterCountingPrinter> type() {
        return CharacterCountingPrinter.class;
    }
}
