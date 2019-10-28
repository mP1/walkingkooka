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

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class PrintedLineHandlerPrinterTest extends PrinterTestCase2<PrintedLineHandlerPrinter> {

    // constants

    private final static Printer PRINTER = createContractPrinter();

    private final static PrintedLineHandler HANDLER = (line, lineEnding, printer) -> {
        final String lineString = line.toString();
        if ((-1 != lineString.indexOf('\r')) || (-1 != lineString.indexOf('\n'))) {
            Assertions.fail("PrintedLineHandler line should not have a CR or NL="
                    + CharSequences.escape(line));
        }
        printer.print(line);
        printer.print("!");
        printer.print(lineEnding);
    };

    private final static LineEnding LINE_ENDING = LineEnding.CR;

    // tests

    @Test
    public void testWrapNullPrinterFails() {
        assertThrows(NullPointerException.class, () -> PrintedLineHandlerPrinter.wrap(null, HANDLER));
    }

    @Test
    public void testWrapNullLineHandlerFails() {
        assertThrows(NullPointerException.class, () -> PrintedLineHandlerPrinter.wrap(PRINTER, null));
    }

    @Test
    public void testEmptyString() {
        this.printAndCheck("", LINE_ENDING, "", null);
    }

    @Test
    public void testOneCharacter() {
        final String printed = "1";
        this.printAndCheck(printed, LINE_ENDING, null, printed);
    }

    @Test
    public void testTwoCharacters() {
        final String printed = "12";
        this.printAndCheck(printed, LINE_ENDING, null, printed);
    }

    @Test
    public void testStringWithoutLineEnding() {
        final String printed = "123456";
        this.printAndCheck(printed, LINE_ENDING, null, printed);
    }

    @Test
    public void testStringWithoutLineEndingCharacterByCharacter() {
        final String printed = "123456";
        this.printAndCheck(this.characterByCharacter(printed),
                LINE_ENDING,
                null,
                printed);
    }

    @Test
    public void testTextCR() {
        final String printed = "123@";
        this.printAndCheck(printed, LineEnding.CR, "", printed);
    }

    @Test
    public void testTextNL() {
        final String printed = "123@";
        this.printAndCheck(printed, LineEnding.NL, printed, null);
    }

    @Test
    public void testTextCRNL() {
        final String printed = "123@";
        this.printAndCheck(printed, LineEnding.CRNL, printed, null);
    }

    @Test
    public void testCR() {
        final String printed = "@";
        this.printAndCheck(printed, LineEnding.CR, "", "\r");
    }

    @Test
    public void testNL() {
        final String printed = "@";
        this.printAndCheck(printed, LineEnding.NL, printed, null);
    }

    @Test
    public void testCRNL() {
        final String printed = "@";
        this.printAndCheck(printed, LineEnding.CRNL, printed, null);
    }

    @Test
    public void testCRCR() {
        this.printAndCheck("@@", LineEnding.CR, "@", "\r");
    }

    @Test
    public void testNLNL() {
        final String printed = "@@";
        this.printAndCheck(printed, LineEnding.NL, printed, null);
    }

    @Test
    public void testCRNLCRNL() {
        final String printed = "@@";
        this.printAndCheck(printed, LineEnding.CRNL, printed, null);
    }

    @Test
    public void testCRText() {
        this.printAndCheck("@123", LineEnding.CR, "@", "123");
    }

    @Test
    public void testNLText() {
        this.printAndCheck("@123", LineEnding.NL, "@", "123");
    }

    @Test
    public void testCRNLText() {
        this.printAndCheck("@123", LineEnding.CRNL, "@", "123");
    }

    @Test
    public void testCRTextCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("@123"), LineEnding.CR, "@", "123");
    }

    @Test
    public void testNLTextCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("@123"), LineEnding.NL, "@", "123");
    }

    @Test
    public void testCRNLTextCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("@123"), LineEnding.CRNL, "@", "123");
    }

    @Test
    public void testManyLinesCr() {
        this.printAndCheck("@123@456@789@", LineEnding.CR, "@123@456@", "789@");
    }

    @Test
    public void testManyLinesNl() {
        this.printAndCheck("@123@456@789@", LineEnding.NL, "@123@456@789@", null);
    }

    @Test
    public void testManyLinesCrNl() {
        this.printAndCheck("@123@456@789@", LineEnding.CRNL, "@123@456@789@", null);
    }

    @Test
    public void testManyLinesCr2() {
        this.printAndCheck("@123@456@789@0", LineEnding.CR, "@123@456@789@", "0");
    }

    @Test
    public void testManyLinesNl2() {
        this.printAndCheck("@123@456@789@0", LineEnding.NL, "@123@456@789@", "0");
    }

    @Test
    public void testManyLinesCrNl2() {
        this.printAndCheck("@123@456@789@0", LineEnding.CRNL, "@123@456@789@", "0");
    }

    @Test
    public void testManyLinesCrCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("123@456@7"), LineEnding.CR, "123@456@", "7");
    }

    @Test
    public void testManyLinesNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("123@456@7"), LineEnding.NL, "123@456@", "7");
    }

    @Test
    public void testManyLinesCrNlCharacterByCharacter() {
        this.printAndCheck(this.characterByCharacter("123\r\n456\r\n7"),
                LineEnding.CRNL,
                "123!\r\n456!\r\n",
                "7");
    }

    @Test
    public void testManyLinesCrCharacterByCharacter2() {
        this.printAndCheck(this.characterByCharacter("@123@456@7"),
                LineEnding.CR,
                "@123@456@",
                "7");
    }

    @Test
    public void testManyLinesNlCharacterByCharacter2() {
        this.printAndCheck(this.characterByCharacter("@123@456@7"),
                LineEnding.NL,
                "@123@456@",
                "7");
    }

    @Test
    public void testManyLinesCrNlCharacterByCharacter2() {
        this.printAndCheck(this.characterByCharacter("\r\n123\r\n456\r\n7"),
                LineEnding.CRNL,
                "!\r\n123!\r\n456!\r\n",
                "7");
    }

    @Test
    public void testPrintFlushCR() {
        final String text = "123@456";
        this.printAndCheck(text, LineEnding.CR, text + "!");
    }

    @Test
    public void testPrintFlushNL() {
        final String text = "123@456";
        this.printAndCheck(text, LineEnding.NL, text + "!");
    }

    @Test
    public void testPrintFlushCRNL() {
        final String text = "123@456";
        this.printAndCheck(text, LineEnding.CRNL, text + "!");
    }

    @Test
    public void testMixedCharactersAndLineEnding() {
        final StringBuilder printed = new StringBuilder();
        final PrintedLineHandlerPrinter printer = this.createPrinter(printed);
        printer.print("123");
        printer.print(printer.lineEnding());
        printer.print("456");
        printer.print(printer.lineEnding());
        printer.flush();

        checkEquals("123!" + LINE_ENDING + "456!"
                + LINE_ENDING, printed.toString());
    }

    @Override
    @Test
    public void testPrintAfterCloseFails() {
        // nop
    }

    @Override
    @Test
    public void testLineEndingAfterCloseFails() {
        // nop
    }

    @Override
    @Test
    public void testFlushAfterCloseFails() {
        // nop
    }

    @Test
    public void testToString() {
        checkEquals(
                HANDLER + " " + PRINTER,
                PrintedLineHandlerPrinter.wrap(PRINTER,
                        HANDLER).toString());
    }

    @Override
    public PrintedLineHandlerPrinter createPrinter(final StringBuilder printed) {
        return this.createPrinter(Printers.stringBuilder(printed,
                LINE_ENDING));
    }

    private PrintedLineHandlerPrinter createPrinter(final Printer printer) {
        return PrintedLineHandlerPrinter.wrap(printer, HANDLER);
    }

    /**
     * Replaces any At signs with {@link LineEnding} and then prints {@link String printed}.
     */
    private void printAndCheck(final CharSequence printed, final LineEnding lineEnding,
                               final String expected, final String buffer) {
        this.printAndCheck(new CharSequence[]{printed}, lineEnding, expected, buffer, false);
    }

    private void printAndCheck(final CharSequence[] printed, final LineEnding lineEnding,
                               final String expected, final String buffer) {
        this.printAndCheck(printed, lineEnding, expected, buffer, false);
    }

    private void printAndCheck(final CharSequence printed, final LineEnding lineEnding,
                               final String expected) {
        this.printAndCheck(new CharSequence[]{printed}, lineEnding, expected, null, true);
    }

    private void printAndCheck(final CharSequence[] printed, final LineEnding lineEnding,
                               final String expected, final String buffer, final boolean flushAndClose) {
        final StringBuilder target = new StringBuilder();

        // if expected is null or empty pass a fake Printer to fail of soon of the first print occurs.
        final PrintedLineHandlerPrinter printer = this.createPrinter(
                (null == expected) || expected.equals("") ?
                        //
                        PRINTER
                        //
                        :
                        Printers.stringBuilder(target, LINE_ENDING));
        for (int i = 0; i < printed.length; i++) {
            printed[i] = this.replacePlaceHolder(printed[i], lineEnding);
        }

        this.printAndCheck(//
                printer,
                // the target Printer
                printed,
                // what is printed
                target,
                // StringBuilder
                null == expected ? "" : this.replacePlaceHolder2(expected, lineEnding).toString(),
                //
                flushAndClose,
                // do not flush and close
                null); // message if fails

        checkEquals(this.replacePlaceHolder(this.toString(buffer), lineEnding),
                this.toString(printer.buffer));
    }

    private CharSequence replacePlaceHolder(final CharSequence text, final LineEnding lineEnding) {
        return text.toString().replace("@", lineEnding);
    }

    private CharSequence replacePlaceHolder2(final CharSequence text, final LineEnding lineEnding) {
        return text.toString().replace("@", "!" + lineEnding);
    }

    private String toString(final CharSequence chars) {
        return null == chars ? "" : chars.toString();
    }

    @Override
    public Class<PrintedLineHandlerPrinter> type() {
        return PrintedLineHandlerPrinter.class;
    }
}
