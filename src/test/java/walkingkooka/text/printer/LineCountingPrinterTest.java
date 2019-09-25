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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.function.IntConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class LineCountingPrinterTest extends PrinterTestCase<LineCountingPrinter>
        implements PrinterTesting2<LineCountingPrinter> {

    // constants

    private final static TestIntCounter COUNTER = new TestIntCounter();

    // tests

    @Test
    public void testWithNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> {
            LineCountingPrinter.wrap(null, COUNTER);
        });
    }

    @Test
    public void testWithNullCounterFails() {
        assertThrows(NullPointerException.class, () -> {
            LineCountingPrinter.wrap(null, COUNTER);
        });
    }

    @Test
    public void testWithoutLineEndings() {
        final TestIntCounter counter = new TestIntCounter();
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter), "123", printed, "123");
        check(0, counter);
    }

    @Test
    public void testWithoutLineEndingsCharacterByCharacter() {
        final TestIntCounter counter = new TestIntCounter();
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter),
                this.characterByCharacter("12345"),
                printed,
                "12345");
        check(0, counter);
    }

    @Test
    public void testIncludesCr() {
        final TestIntCounter counter = new TestIntCounter();
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter, "12\r", "34"),
                "12\r34",
                printed,
                "12\r34");
        check(1, counter);
    }

    @Test
    public void testIncludesNl() {
        final TestIntCounter counter = new TestIntCounter();
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter, "12\n", "34"),
                "12\n34",
                printed,
                "12\n34");
        check(1, counter);
    }

    @Test
    public void testIncludesCrNl() {
        final TestIntCounter counter = new TestIntCounter();
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter, "12\r\n", "34"),
                "12\r\n34",
                printed,
                "12\r\n34");
        check(1, counter);
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
        final TestIntCounter counter = new TestIntCounter();
        final LineCountingPrinter printer = LineCountingPrinter.wrap(//
                new FakePrinter() {

                    @Override
                    public void print(final CharSequence chars) throws PrinterException {
                        switch (this.printed) {
                            case 0:
                                checkEquals("\r", chars.toString(), "wrong chars printed");
                                assertEquals(0,
                                        counter.counter,
                                        "counter incremented earlier than expected");
                                break;
                            case 1:
                                checkEquals("\n", chars.toString(), "wrong chars printed");
                                assertEquals(1,
                                        counter.counter,
                                        "counter incremented earlier than expected");
                                break;
                            default:
                                Assertions.fail(
                                        "Unexpected print=" + CharSequences.quoteAndEscape(chars));
                        }
                        this.printed++;
                    }

                    private int printed;

                    @Override
                    public void flush() throws PrinterException {
                        switch (this.printed) {
                            case 0:
                                Assertions.fail("Flush called before first print");
                            case 1:
                                break;
                            case 2:
                                break;
                            default:
                                Assertions.fail("Unexpected print");
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
        final TestIntCounter counter = new TestIntCounter();
        final LineCountingPrinter lineCountingPrinter = LineCountingPrinter.wrap(printer, counter);
        lineCountingPrinter.lineCount = 123;

        checkEquals(printer + " 123 line(s)",
                lineCountingPrinter.toString());
    }

    @Override
    public LineCountingPrinter createPrinter(final StringBuilder target) {
        return this.createPrinter(target, COUNTER);
    }

    private LineCountingPrinter createPrinter(final StringBuilder target,
                                              final TestIntCounter counter) {
        return LineCountingPrinter.wrap(this.createStringBuilderPrinter(target), counter);
    }

    private Printer createStringBuilderPrinter(final StringBuilder printed) {
        return Printers.stringBuilder(printed, LineEnding.NL);
    }

    private void printAndCheckLineCount(final String text, final int lines) {
        final TestIntCounter counter = new TestIntCounter();
        final StringBuilder printed = new StringBuilder();
        this.printAndCheck(this.createPrinter(printed, counter), text, printed, text);
        assertEquals(lines, counter.counter, "wrong number of lines printed");
    }

    private void check(final int expected, final TestIntCounter counter) {
        assertEquals(expected, counter.counter, "counter");
    }

    private LineCountingPrinter createPrinter(final StringBuilder target,
                                              final TestIntCounter counter, final String... expecting) {
        return LineCountingPrinter.wrap(//
                new Printer() {

                    @Override
                    public void print(final CharSequence chars) throws PrinterException {
                        final int i = this.printed;
                        if (i > expecting.length) {
                            Assertions.fail("Attempt to print another unexpected line="
                                    + CharSequences.quoteAndEscape(chars));
                        }
                        final String expected = expecting[i];
                        final String actual = chars.toString();
                        if (false == expected.equals(actual)) {
                            assertEquals(CharSequences.quoteAndEscape(expected),
                                    CharSequences.quoteAndEscape(actual),
                                    "Wrong line " + i + " printed,");
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
    public Class<LineCountingPrinter> type() {
        return LineCountingPrinter.class;
    }

    private static class TestIntCounter implements IntConsumer {

        @Override
        public void accept(final int value) {
            this.counter = value;
        }

        int counter;
    }
}
