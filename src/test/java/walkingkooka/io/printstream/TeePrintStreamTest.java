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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.io.printer.Printers;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.Latch;
import walkingkooka.text.LineEnding;
import walkingkooka.type.MemberVisibility;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class TeePrintStreamTest implements ClassTesting2<TeePrintStream>,
        PrintStreamTesting<TeePrintStream> {

    // constants

    private final static PrintStream FIRST = PrintStreams.fake();

    private final static PrintStream SECOND = PrintStreams.fake();

    // tests

    @Test
    public void testWrapNullFirstPrintStreamFails() {
        assertThrows(NullPointerException.class, () -> {
            TeePrintStream.wrap(null, SECOND);
        });
    }

    @Test
    public void testWrapNullSecondPrintStreamFails() {
        assertThrows(NullPointerException.class, () -> {
            TeePrintStream.wrap(FIRST, null);
        });
    }

    @Test
    public void testDoesntWrapSame() {
        assertSame(FIRST, TeePrintStream.wrap(FIRST, FIRST));
    }

    @Test
    public void testWriteInt() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final TeePrintStream printStream = this.wrap(b1, b2);
        printStream.write('a');
        printStream.flush();
        assertEquals("a", b1.toString());
        assertEquals("a", b2.toString());
    }

    @Test
    public void testWriteByteArray() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final TeePrintStream printStream = this.wrap(b1, b2);
        printStream.write(new byte[]{0, 'a', 'b', 0}, 1, 2);
        printStream.flush();
        assertEquals("ab", b1.toString());
        assertEquals("ab", b2.toString());
    }

    @Test
    public void testPrintBooleanTrue() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(true);
        assertEquals("true", b1.toString());
        assertEquals("true", b2.toString());
    }

    @Test
    public void testPrintBooleanFalse() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(false);
        assertEquals("false", b1.toString());
        assertEquals("false", b2.toString());
    }

    @Test
    public void testPrintChar() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print('A');
        assertEquals("A", b1.toString());
        assertEquals("A", b2.toString());
    }

    @Test
    public void testPrintInt() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123);
        assertEquals("123", b1.toString());
        assertEquals("123", b2.toString());
    }

    @Test
    public void testPrintLong() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123L);
        assertEquals("123", b1.toString());
        assertEquals("123", b2.toString());
    }

    @Test
    public void testPrintFloat() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123.0f);
        assertEquals("123.0", b1.toString());
        assertEquals("123.0", b2.toString());
    }

    @Test
    public void testPrintDouble() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).print(123.5);
        assertEquals("123.5", b1.toString());
        assertEquals("123.5", b2.toString());
    }

    @Test
    public void testPrintCharArray() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).print(printed.toCharArray());
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
    }

    @Test
    public void testPrintString() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).print(printed);
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
    }

    @Test
    public void testPrintObject() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final Object printed = new Object();
        this.wrap(b1, b2).print(printed);
        assertEquals(printed.toString(), b1.toString());
        assertEquals(printed.toString(), b2.toString());
    }

    @Test
    public void testPrintln() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println();
        assertEquals("\r", b1.toString());
        assertEquals("\n", b2.toString());
    }

    @Test
    public void testPrintlnBooleanTrue() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(true);
        assertEquals("true\r", b1.toString());
        assertEquals("true\n", b2.toString());
    }

    @Test
    public void testPrintlnBooleanFalse() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(false);
        assertEquals("false\r", b1.toString());
        assertEquals("false\n", b2.toString());
    }

    @Test
    public void testPrintlnChar() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println('A');
        assertEquals("A\r", b1.toString());
        assertEquals("A\n", b2.toString());
    }

    @Test
    public void testPrintlnInt() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123);
        assertEquals("123\r", b1.toString());
        assertEquals("123\n", b2.toString());
    }

    @Test
    public void testPrintlnLong() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123L);
        assertEquals("123\r", b1.toString());
        assertEquals("123\n", b2.toString());
    }

    @Test
    public void testPrintlnFloat() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123.0f);
        assertEquals("123.0\r", b1.toString());
        assertEquals("123.0\n", b2.toString());
    }

    @Test
    public void testPrintlnDouble() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).println(123.5);
        assertEquals("123.5\r", b1.toString());
        assertEquals("123.5\n", b2.toString());
    }

    @Test
    public void testPrintlnCharArray() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).println(printed.toCharArray());
        assertEquals(printed + "\r", b1.toString());
        assertEquals(printed + "\n", b2.toString());
    }

    @Test
    public void testPrintlnString() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).println(printed);
        assertEquals(printed + "\r", b1.toString());
        assertEquals(printed + "\n", b2.toString());
    }

    @Test
    public void testPrintlnObject() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final Object printed = new Object();
        this.wrap(b1, b2).println(printed);
        assertEquals(printed.toString() + "\r", b1.toString());
        assertEquals(printed.toString() + "\n", b2.toString());
    }

    @Test
    public void testPrintf() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).println(printed);
        assertEquals(printed + "\r", b1.toString());
        assertEquals(printed + "\n", b2.toString());
    }

    @Test
    public void testPrintfLocale() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).printf(Locale.getDefault(), printed);
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
    }

    @Test
    public void testFormat() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).format("%s", printed);
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
    }

    @Test
    public void testFormatLocale() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).format(Locale.getDefault(), "%s", printed);
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
    }

    @Test
    public void testAppendChar() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        this.wrap(b1, b2).append('A');
        assertEquals("A", b1.toString());
        assertEquals("A", b2.toString());
    }

    @Test
    public void testAppend() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).append(printed);
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
    }

    @Test
    public void testAppendWithStartAndLength() {
        final StringBuilder b1 = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final String printed = "printed";
        this.wrap(b1, b2).append("***" + printed + "***", 3, 3 + printed.length());
        assertEquals(printed, b1.toString());
        assertEquals(printed, b2.toString());
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
        assertTrue(flushed1.value(), "First PrintStream was NOT flushed");
        assertTrue(flushed1.value(), "Second PrintStream was NOT flushed");
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
        assertTrue(closed1.value(), "First PrintStream was NOT closed");
        assertTrue(closed1.value(), "Second PrintStream was NOT closed");
    }

    @Test
    public void testCheckError() {
        assertFalse(TeePrintStream.wrap(//
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
        assertTrue(TeePrintStream.wrap(//
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
        this.toStringAndCheck(this.createPrintStream(), FIRST + " " + SECOND);
    }

    @Override
    public TeePrintStream createPrintStream() {
        return Cast.to(TeePrintStream.wrap(FIRST, SECOND));
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
    public Class<TeePrintStream> type() {
        return TeePrintStream.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
