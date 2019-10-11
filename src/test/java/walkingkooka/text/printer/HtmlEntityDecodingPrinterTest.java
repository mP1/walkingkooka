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
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class HtmlEntityDecodingPrinterTest extends PrinterTestCase<HtmlEntityDecodingPrinter> {

    // constants

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // tests

    @Test
    public void testWrapNullEntityDecoderFails() {
        assertThrows(NullPointerException.class, () -> HtmlEntityDecodingPrinter.wrap(null, Printers.fake()));
    }

    @Test
    public void testNothing() {
        this.printAndCheck("");
    }

    @Test
    public void testEmpty() {
        this.printAndCheck("", "");
    }

    @Test
    public void testWithoutEntities() {
        final String string = "without entities";
        this.printAndCheck(string, string);
    }

    @Test
    public void testWithoutEntities2() {
        final String string1 = "without entities1";
        final String string2 = " without entities2";
        this.printAndCheck(string1 + string2, string1, string2);
    }

    @Test
    public void testAmpersand() {
        final HtmlEntityDecodingPrinter printer = this.createPrinter();
        printer.print("&");
        this.check(printer, "");
        assertEquals(1, printer.last);
        assertEquals("&", printer.buffer());
    }

    @Test
    public void testEmptyEntity() {
        final HtmlEntityDecodingPrinter printer = this.createPrinter();
        printer.print("&;");
        this.check(printer, "&;");
        assertEquals(0, printer.last);
    }

    @Test
    public void testIncompleteNamedEntity() {
        final HtmlEntityDecodingPrinter printer = this.createPrinter();
        final String text = "&star";
        printer.print(text);
        this.check(printer, "");
        assertEquals(text.length(), printer.last);
        assertEquals(text, printer.buffer());
    }

    @Test
    public void testNamedEntity() {
        this.printAndCheck("*", "&star;");
    }

    @Test
    public void testTextBeforeNamedEntity() {
        this.printAndCheck("before *", "before &star;");
    }

    @Test
    public void testNamedEntityAndText() {
        this.printAndCheck("before * after", "before &star; ", "after");
    }

    @Test
    public void testIncompleteNumericEntity() {
        final HtmlEntityDecodingPrinter printer = this.createPrinter();
        final String text = "&#123";
        printer.print(text);
        this.check(printer, "");
        assertEquals(text.length(), printer.last);
        assertEquals(text, printer.buffer());
    }

    @Test
    public void testNumericEntity() {
        this.printAndCheck("A", "&#65;");
    }

    @Test
    public void testNumericEntityThenText() {
        this.printAndCheck("before A after", "before &#65; ", "after");
    }

    @Test
    public void testInvalidNumericEntityThenText() {
        this.printAndCheck("&#ABC;", "&#ABC;");
    }

    @Test
    public void testHexNumericEntity() {
        this.printAndCheck("A", "&#x41;");
    }

    @Test
    public void testHexNumericEntity2() {
        this.printAndCheck("A", "&#X41;");
    }

    @Test
    public void testHexNumericEntityThenText() {
        final char c = 0x4f;
        this.printAndCheck("before " + c + " after", "before &#x04f;", " after");
    }

    @Test
    public void testInvalidHexEntity() {
        this.printAndCheck("&#XYZ;", "&#XYZ;");
    }

    @Test
    public void testManyEntities() {
        this.printAndCheck("AB*", "&#x41;&#66;&star;");
    }

    @Test
    public void testManyEntities2() {
        this.printAndCheck("AB*", "&#x41;&#66;&star;");
    }

    @Test
    public void testMixedCharsAndLineEndings() {
        final StringBuilder printed = new StringBuilder();
        final HtmlEntityDecodingPrinter printer = this.createPrinter(printed);
        printer.print("123");
        printer.print(printer.lineEnding());
        printer.print("456");
        printer.print(printer.lineEnding());
        printer.flush();
        checkEquals(
                "123" + LINE_ENDING + "456" + LINE_ENDING,
                printed.toString());
    }

    @Test
    public void testMixedDecodedCharsAndLineEndings() {
        final StringBuilder printed = new StringBuilder();
        final HtmlEntityDecodingPrinter printer = this.createPrinter(printed);
        printer.print("1&star;3");
        printer.print(printer.lineEnding());
        printer.print("4&star;6");
        printer.print(printer.lineEnding());
        printer.flush();
        checkEquals(
                "1*3" + LINE_ENDING + "4*6" + LINE_ENDING,
                printed.toString());
    }

    @Test
    public void testToString() {
        final Function<String, String> decoder = (s) -> s;
        final Printer printer = Printers.fake();
        checkEquals(printer + " AND " + decoder,
                HtmlEntityDecodingPrinter.wrap(decoder, printer).toString());
    }

    // factory

    @Override
    public HtmlEntityDecodingPrinter createPrinter() {
        return this.createPrinter(this.builder);
    }

    private HtmlEntityDecodingPrinter createPrinter(final StringBuilder printed) {
        return HtmlEntityDecodingPrinter.wrap( //
                (entity) -> "&star;".equals(entity) ? "*" : entity, //
                Printers.stringBuilder(printed, LINE_ENDING));
    }

    private final StringBuilder builder = new StringBuilder();

    private void check(final HtmlEntityDecodingPrinter printer, final String expected) {
        final String actual = this.builder.toString();
        assertEquals(expected,
                actual,
                () -> "Different text written, buffer=" + CharSequences.quote(printer.buffer()));
    }

    private void printAndCheck(final String expected, final String... strings) {
        final HtmlEntityDecodingPrinter printer = this.createPrinter();
        final StringBuilder b = new StringBuilder();
        for (final String string : strings) {
            printer.print(string);
            b.append(string);
        }
        this.check(printer, expected);
        assertEquals(0, printer.last, "printer.last");
        checkEquals("", printer.buffer());

        this.builder.setLength(0);
        final char[] chars = b.toString().toCharArray();
        for (final char c : chars) {
            printer.print(String.valueOf(c));
        }
        this.check(printer, expected);
        assertEquals(0, printer.last, "printer.last");
        checkEquals("", printer.buffer());
    }

    @Override
    public Class<HtmlEntityDecodingPrinter> type() {
        return HtmlEntityDecodingPrinter.class;
    }
}
