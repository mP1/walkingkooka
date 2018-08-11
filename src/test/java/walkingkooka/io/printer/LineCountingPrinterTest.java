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
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.util.variable.Variable;
import walkingkooka.util.variable.Variables;

import static org.junit.Assert.assertEquals;

final public class LineCountingPrinterTest extends PrinterTestCase2<LineCountingPrinter> {

    // constants

    private final static Printer PRINTER = Printers.fake();

    private final static Variable<Integer> COUNTER = Variables.with(0);

    // tests

    @Test
    public void testNullPrinterFails() {
        this.wrapFails(null, LineCountingPrinterTest.COUNTER);
    }

    @Test
    public void testNullCounterFails() {
        this.wrapFails(LineCountingPrinterTest.PRINTER, null);
    }

    private void wrapFails(final Printer printer, final Variable<Integer> counter) {
        try {
            LineCountingPrinter.wrap(printer, counter);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testWithoutLineEndings() {
        final Variable<Integer> counter = Variables.with(999);
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter), "123", printed, "123");
        check(999, counter);
    }

    @Test
    public void testWithoutLineEndingsCharacterByCharacter() {
        final Variable<Integer> counter = Variables.with(999);
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter),
                this.characterByCharacter("12345"),
                printed,
                "12345");
        check(999, counter);
    }

    @Test
    public void testIncludesCr() {
        final Variable<Integer> counter = Variables.with(123);
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter, "12\r", "34"),
                "12\r34",
                printed,
                "12\r34");
        check(124, counter);
    }

    @Test
    public void testIncludesNl() {
        final Variable<Integer> counter = Variables.with(123);
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter, "12\n", "34"),
                "12\n34",
                printed,
                "12\n34");
        check(124, counter);
    }

    @Test
    public void testIncludesCrNl() {
        final Variable<Integer> counter = Variables.with(123);
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter, "12\r\n", "34"),
                "12\r\n34",
                printed,
                "12\r\n34");
        check(124, counter);
    }

    @Test
    public void testEndsWithCr() {
        this.printAndCheckLineCount("B\r", 1);
    }

    @Test
    public void testEndsWithNl() {
        this.printAndCheckLineCount("B\n", 1);
    }

    @Test
    public void testEndsWithCrNl() {
        this.printAndCheckLineCount("B\r\n", 1);
    }

    @Test
    public void testOnlyCr() {
        this.printAndCheckLineCount("\r", 1);
    }

    @Test
    public void testOnlyNl() {
        this.printAndCheckLineCount("\n", 1);
    }

    @Test
    public void testOnlyCrNl() {
        this.printAndCheckLineCount("\n", 1);
    }

    @Test
    public void testEmptyLineCrCr() {
        this.printAndCheckLineCount("\r\r", 2);
    }

    @Test
    public void testEmptyLineNlCr() {
        this.printAndCheckLineCount("\n\r", 2);
    }

    @Test
    public void testEmptyLineNlNl() {
        this.printAndCheckLineCount("\n\n", 2);
    }

    @Test
    public void testEmptyLineCrNlCrNl() {
        this.printAndCheckLineCount("\r\n\r\n", 2);
    }

    @Test
    public void testManyLinesCr() {
        this.printAndCheckLineCount("1\r2\r3\r", 3);
    }

    @Test
    public void testManyLinesNl() {
        this.printAndCheckLineCount("1\n2\n3\n", 3);
    }

    @Test
    public void testManyLinesCrNl() {
        this.printAndCheckLineCount("1\r\n2\r\n3\r\n", 3);
    }

    @Test
    public void testPrintCrFlushThenPrintNl() {
        final Variable<Integer> counter = Variables.with(0);
        final LineCountingPrinter printer = LineCountingPrinter.wrap(//
                new FakePrinter() {

                    @Override
                    public void print(final CharSequence chars) throws PrinterException {
                        switch (this.printed) {
                            case 0:
                                checkEquals("wrong chars printed", "\r", chars.toString());
                                assertEquals("counter incremented earlier than expected",
                                        Integer.valueOf(0),
                                        counter.get());
                                break;
                            case 1:
                                checkEquals("wrong chars printed", "\n", chars.toString());
                                assertEquals("counter incremented earlier than expected",
                                        Integer.valueOf(1),
                                        counter.get());
                                break;
                            default:
                                Assert.fail(
                                        "Unexpected print=" + CharSequences.quoteAndEscape(chars));
                        }
                        this.printed++;
                    }

                    private int printed;

                    @Override
                    public void flush() throws PrinterException {
                        switch (this.printed) {
                            case 0:
                                Assert.fail("Flush called before first print");
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                                Assert.fail("Unexpected print");
                        }
                    }

                }, counter);
        printer.print("\r");
        printer.flush();
        printer.print("\n");
        printer.flush();
    }

    @Test
    public void testFlushDoesNothingWithoutPendingCr() {
        final LineCountingPrinter printer = LineCountingPrinter.wrap(//
                new FakePrinter() {

                    @Override
                    public void flush() throws PrinterException {
                        // nop
                    }

                }, COUNTER);
        printer.flush();
    }

    @Test
    public void testToString() {
        final Printer printer = Printers.fake();
        final Variable<Integer> counter = Variables.with(123);
        checkEquals(printer + " 123 line(s)",
                LineCountingPrinter.wrap(printer, counter).toString());
    }

    @Override
    protected LineCountingPrinter createPrinter() {
        return this.createPrinter(new StringBuilder());
    }

    @Override
    protected LineCountingPrinter createPrinter(final StringBuilder target) {
        return this.createPrinter(target, COUNTER);
    }

    private LineCountingPrinter createPrinter(final StringBuilder target,
                                              final Variable<Integer> counter) {
        return LineCountingPrinter.wrap(this.createStringBuilderPrinter(target), counter);
    }

    private Printer createStringBuilderPrinter(final StringBuilder printed) {
        return Printers.stringBuilder(printed, LineEnding.NL);
    }

    private void printAndCheckLineCount(final String text, final int lines) {
        final Variable<Integer> counter = Variables.with(0);
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter), text, printed, text);
        assertEquals("wrong number of lines printed", Integer.valueOf(lines), counter.get());
    }

    private void check(final int expected, final Variable<Integer> counter) {
        assertEquals("counter", Integer.valueOf(expected), counter.get());
    }

    private LineCountingPrinter createPrinter(final StringBuilder target,
                                              final Variable<Integer> counter, final String... expecting) {
        return LineCountingPrinter.wrap(//
                new Printer() {

                    @Override
                    public void print(final CharSequence chars) throws PrinterException {
                        final int i = this.printed;
                        if (i > expecting.length) {
                            Assert.fail("Attempt to print another unexpected line="
                                    + CharSequences.quoteAndEscape(chars));
                        }
                        final String expected = expecting[i];
                        final String actual = chars.toString();
                        if (false == expected.equals(actual)) {
                            assertEquals("Wrong line " + i + " printed,",
                                    CharSequences.quoteAndEscape(expected),
                                    CharSequences.quoteAndEscape(actual));
                        }
                        this.printed++;
                        target.append(chars);
                    }

                    private int printed = 0;

                    @Override
                    public LineEnding lineEnding() throws PrinterException {
                        return LineEnding.NONE;
                    }

                    @Override
                    public void flush() throws PrinterException {
                        // nop
                    }

                    @Override
                    public void close() throws PrinterException {
                        // nop
                    }
                }, counter);
    }

    @Override
    protected Class<LineCountingPrinter> type() {
        return LineCountingPrinter.class;
    }
}
