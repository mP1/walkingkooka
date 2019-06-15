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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class IndentationTest implements ClassTesting2<Indentation>,
        HashCodeEqualsDefinedTesting<Indentation>,
        SerializationTesting<Indentation>,
        ToStringTesting<Indentation> {

    @Test
    public void testCarriageReturnRepeatingCharFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Indentation.with('\r', 1);
        });
    }

    @Test
    public void testNewLineRepeatingCharFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Indentation.with('\n', 1);
        });
    }

    @Test
    public void testInvalidRepeatingCountFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Indentation.with('.', -1);
        });
    }

    @Test
    public void testRepeatingCharacter() {
        this.check(Indentation.with('.', 3), "...");
    }

    @Test
    public void testRepeatingSpace() {
        this.checkConstant(Indentation.with(' ', 3), 3);
    }

    @Test
    public void testNullStringFails() {
        assertThrows(NullPointerException.class, () -> {
            Indentation.with(null);
        });
    }

    @Test
    public void testStringIncludesCarriageReturnFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Indentation.with("with carriage return \r");
        });
    }

    @Test
    public void testStringIncludesNewLineFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Indentation.with("with new line \n");
        });
    }

    @Test
    public void testEmpty() {
        this.checkConstant(Indentation.with(""), 0);
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(Indentation.with("different"));
    }

    @Test
    public void testStringIsConstants() {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < Indentation.COUNT; i++) {
            this.checkConstant(Indentation.with(b.toString()), i);
            b.append(' ');
        }
    }

    @Test
    public void testRepeatSpaceReturnsConstants() {
        for (int i = 0; i < Indentation.COUNT; i++) {
            this.checkConstant(Indentation.with(' ', i), i);
        }
    }

    @Test
    public void testSpaceButTooLong() {
        final char[] a = new char[1 + Indentation.COUNT];
        Arrays.fill(a, ' ');
        final String value = new String(a);
        this.check(Indentation.with(value), value);
    }

    @Test
    public void testOnlyWhitespace() {
        final String value = " \t";
        final Indentation indentation = Indentation.with(value);
        assertEquals(value, indentation.value());
    }

    @Test
    public void testNonWhitespace() {
        final String value = "123";
        this.check(Indentation.with(value), value);
    }

    @Test
    public void testAppendNullFails() {
        final Indentation indentation = Indentation.with("..");
        assertThrows(NullPointerException.class, () -> {
            indentation.append(null);
        });
    }

    @Test
    public void testAppendEmptyString() {
        final Indentation indentation = Indentation.with("..");
        assertSame(indentation, indentation.append(Indentation.EMPTY));
    }

    @Test
    public void testAppendConstantToConstant() {
        this.checkConstant(Indentation.with("  ").append(Indentation.with("   ")), 5);
    }

    @Test
    public void testAppendConstantToNonConstant() {
        final Indentation indentation = Indentation.with("123");
        final Indentation append = Indentation.with("   ");
        this.check(indentation.append(append), "123   ");
    }

    @Test
    public void testAppendNonConstantToConstant() {
        final Indentation indentation = Indentation.with("   ");
        final Indentation append = Indentation.with(" 12");
        this.check(indentation.append(append), "    12");
    }

    @Test
    public void testAppend() {
        final Indentation indentation = Indentation.with("..");
        final Indentation appended = Indentation.with("APPENDED");
        final Indentation result = indentation.append(appended);
        this.check(result, "..APPENDED");
    }

    // CharSequence

    @Test
    public void testCharAt() {
        final Indentation indentation = Indentation.with("0123");
        assertEquals('0', indentation.charAt(0));
        assertEquals('1', indentation.charAt(1));
        assertEquals('2', indentation.charAt(2));
        assertEquals('3', indentation.charAt(3));
    }

    @Test
    public void testLength() {
        assertEquals(4, Indentation.with("0123").length());
    }

    @Test
    public void testSubSequence() {
        final Indentation indentation = Indentation.with("0123");
        assertEquals(Indentation.with("01"), indentation.subSequence(0, 2));
        assertEquals(Indentation.with("1"), indentation.subSequence(1, 2));
    }

    @Test
    public void testSubSequenceWithSameStartAndEnd() {
        final Indentation indentation = Indentation.with("0123");
        assertSame(indentation, indentation.subSequence(0, 4));
    }

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(Indentation.with(""), "");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(Indentation.with("a1 "), "a1 ");
    }

    @Test
    public void testConstants() throws Exception {
        this.constantsAreSingletons();
    }

    private void check(final Indentation indentation, final String value) {
        assertEquals(value, indentation.value(), "value");
    }

    private void checkConstant(final Indentation indentation, final int spaceCount) {
        assertSame(Indentation.CONSTANTS[spaceCount], indentation, "constant not returned");
    }

    @Override
    public Class<Indentation> type() {
        return Indentation.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Indentation createObject() {
        return Indentation.with("  ");
    }

    @Override
    public Indentation serializableInstance() {
        return Indentation.with("123");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
