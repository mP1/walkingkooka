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

package walkingkooka.io.printstream;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.io.printer.Printers;
import walkingkooka.test.Latch;
import walkingkooka.text.LineEnding;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Locale;

final public class TeePrintStreamTest extends PrintStreamTestCase<TeePrintStream> {
    // constants

    private final static PrintStream FIRST = PrintStreams.fake();

    private final static PrintStream SECOND = PrintStreams.fake();

    // tests

    @Test
    public void testWrapNullFirstPrintStreamFails() {
        this.wrapFails(null, TeePrintStreamTest.SECOND);
    }

    @Test
    public void testWrapNullSecondPrintStreamFails() {
        this.wrapFails(TeePrintStreamTest.FIRST, null);
    }

    private void wrapFails(final PrintStream first, final PrintStream second) {
        try {
            TeePrintStream.wrap(first, second);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testDoesntWrapSame() {
        assertSame(TeePrintStreamTest.FIRST,
                TeePrintStream.wrap(TeePrintStreamTest.FIRST, TeePrintStreamTest.FIRST));
    }

    @Test
    public void testWriteInt() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final TeePrintStream printStream = this.wrap(b1, b2);
        printStream.write('a');
        printStream.flush();
        Assert.assertEquals("a", b1.toString());
        Assert.assertEquals("a", b2.toString());
    }

    @Test
    public void testWriteByteArray() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final TeePrintStream printStream = this.wrap(b1, b2);
        printStream.write(new byte[]{0, 'a', 'b', 0}, 1, 2);
        printStream.flush();
        Assert.assertEquals("ab", b1.toString());
        Assert.assertEquals("ab", b2.toString());
    }

    @Test
    public void testPrintBooleanTrue() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(true);
        Assert.assertEquals("true", b1.toString());
        Assert.assertEquals("true", b2.toString());
    }

    @Test
    public void testPrintBooleanFalse() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(false);
        Assert.assertEquals("false", b1.toString());
        Assert.assertEquals("false", b2.toString());
    }

    @Test
    public void testPrintChar() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print('A');
        Assert.assertEquals("A", b1.toString());
        Assert.assertEquals("A", b2.toString());
    }

    @Test
    public void testPrintInt() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123);
        Assert.assertEquals("123", b1.toString());
        Assert.assertEquals("123", b2.toString());
    }

    @Test
    public void testPrintLong() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123L);
        Assert.assertEquals("123", b1.toString());
        Assert.assertEquals("123", b2.toString());
    }

    @Test
    public void testPrintFloat() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123.0f);
        Assert.assertEquals("123.0", b1.toString());
        Assert.assertEquals("123.0", b2.toString());
    }

    @Test
    public void testPrintDouble() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123.5);
        Assert.assertEquals("123.5", b1.toString());
        Assert.assertEquals("123.5", b2.toString());
    }

    @Test
    public void testPrintCharArray() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).print(printed.toCharArray());
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testPrintString() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).print(printed);
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testPrintObject() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final Object printed = new Object();
        this.wrap(b1, b2).print(printed);
        Assert.assertEquals(printed.toString(), b1.toString());
        Assert.assertEquals(printed.toString(), b2.toString());
    }

    @Test
    public void testPrintln() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println();
        Assert.assertEquals("\r", b1.toString());
        Assert.assertEquals("\n", b2.toString());
    }

    @Test
    public void testPrintlnBooleanTrue() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(true);
        Assert.assertEquals("true\r", b1.toString());
        Assert.assertEquals("true\n", b2.toString());
    }

    @Test
    public void testPrintlnBooleanFalse() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(false);
        Assert.assertEquals("false\r", b1.toString());
        Assert.assertEquals("false\n", b2.toString());
    }

    @Test
    public void testPrintlnChar() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println('A');
        Assert.assertEquals("A\r", b1.toString());
        Assert.assertEquals("A\n", b2.toString());
    }

    @Test
    public void testPrintlnInt() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123);
        Assert.assertEquals("123\r", b1.toString());
        Assert.assertEquals("123\n", b2.toString());
    }

    @Test
    public void testPrintlnLong() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123L);
        Assert.assertEquals("123\r", b1.toString());
        Assert.assertEquals("123\n", b2.toString());
    }

    @Test
    public void testPrintlnFloat() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123.0f);
        Assert.assertEquals("123.0\r", b1.toString());
        Assert.assertEquals("123.0\n", b2.toString());
    }

    @Test
    public void testPrintlnDouble() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123.5);
        Assert.assertEquals("123.5\r", b1.toString());
        Assert.assertEquals("123.5\n", b2.toString());
    }

    @Test
    public void testPrintlnCharArray() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).println(printed.toCharArray());
        Assert.assertEquals(printed + "\r", b1.toString());
        Assert.assertEquals(printed + "\n", b2.toString());
    }

    @Test
    public void testPrintlnString() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).println(printed);
        Assert.assertEquals(printed + "\r", b1.toString());
        Assert.assertEquals(printed + "\n", b2.toString());
    }

    @Test
    public void testPrintlnObject() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final Object printed = new Object();
        this.wrap(b1, b2).println(printed);
        Assert.assertEquals(printed.toString() + "\r", b1.toString());
        Assert.assertEquals(printed.toString() + "\n", b2.toString());
    }

    @Test
    public void testPrintf() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).println(printed);
        Assert.assertEquals(printed + "\r", b1.toString());
        Assert.assertEquals(printed + "\n", b2.toString());
    }

    @Test
    public void testPrintfLocale() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).printf(Locale.getDefault(), printed);
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testFormat() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).format("%s", printed);
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testFormatLocale() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).format(Locale.getDefault(), "%s", printed);
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testAppendChar() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).append('A');
        Assert.assertEquals("A", b1.toString());
        Assert.assertEquals("A", b2.toString());
    }

    @Test
    public void testAppend() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).append(printed);
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testAppendWithStartAndLength() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).append("***" + printed + "***", 3, 3 + printed.length());
        Assert.assertEquals(printed, b1.toString());
        Assert.assertEquals(printed, b2.toString());
    }

    @Test
    public void testFlush() {
        final Latch flushed1 = Latch.create();
        final Latch flushed2 = Latch.create();
        TeePrintStream.wrap(//
                new FakePrintStream() {
                    @Override
                    public void flush() {
                        flushed1.set("Already flushed");
                    }
                }, //
                new FakePrintStream() {
                    @Override
                    public void flush() {
                        flushed2.set("Already flushed");
                    }
                }).flush();
        Assert.assertTrue("First PrintStream was NOT flushed", flushed1.value());
        Assert.assertTrue("Second PrintStream was NOT flushed", flushed1.value());
    }

    @Test
    public void testClose() {
        final Latch closed1 = Latch.create();
        final Latch closed2 = Latch.create();
        TeePrintStream.wrap(//
                new FakePrintStream() {
                    @Override
                    public void flush() {
                        closed1.set("Already closed");
                    }
                }, //
                new FakePrintStream() {
                    @Override
                    public void flush() {
                        closed2.set("Already closed");
                    }
                }).flush();
        Assert.assertTrue("First PrintStream was NOT closed", closed1.value());
        Assert.assertTrue("Second PrintStream was NOT closed", closed1.value());
    }

    @Test
    public void testCheckError() {
        Assert.assertFalse(TeePrintStream.wrap(//
                new FakePrintStream() {
                    @Override
                    public boolean checkError() {
                        return false;
                    }
                }, //
                new FakePrintStream() {
                    @Override
                    public boolean checkError() {
                        return false;
                    }
                }).checkError());
    }

    @Test
    public void testCheckErrorWhenTrue() {
        Assert.assertTrue(TeePrintStream.wrap(//
                new FakePrintStream() {
                    @Override
                    public boolean checkError() {
                        return false;
                    }
                }, //
                new FakePrintStream() {
                    @Override
                    public boolean checkError() {
                        return true;
                    }
                }).checkError());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(TeePrintStreamTest.FIRST + " " + TeePrintStreamTest.SECOND,
                this.createPrintStream().toString());
    }

    @Override
    protected TeePrintStream createPrintStream() {
        return Cast.to(TeePrintStream.wrap(TeePrintStreamTest.FIRST, TeePrintStreamTest.SECOND));
    }

    private TeePrintStream wrap(final StringBuilder b1, final StringBuilder b2) {
        return Cast.to(TeePrintStream.wrap(//
                PrintStreams.printer(Printers.stringBuilder(b1, LineEnding.CR),
                        LineEnding.CR,
                        Charset.defaultCharset()), //
                PrintStreams.printer(Printers.stringBuilder(b2, LineEnding.NL),
                        LineEnding.NL,
                        Charset.defaultCharset())));
    }

    @Override
    protected Class<TeePrintStream> type() {
        return TeePrintStream.class;
    }
}
