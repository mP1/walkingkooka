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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class IndentationTest implements ClassTesting2<Indentation>,
    CharSequenceTesting<Indentation>,
    HashCodeEqualsDefinedTesting2<Indentation>,
    ToStringTesting<Indentation> {

    @Override
    public void testAllConstructorsVisibility() {
    }

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
    }

    @Test
    public void testWithCarriageReturnRepeatingCharFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.with('\r', 1)
        );
    }

    @Test
    public void testWithNewLineRepeatingCharFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.with('\n', 1)
        );
    }

    @Test
    public void testWithInvalidRepeatingCountFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.with('.', -1)
        );
    }

    @Test
    public void testRepeatingCharacter() {
        this.check(
            Indentation.with('.', 3),
            "..."
        );
    }

    @Test
    public void testRepeatingSpace() {
        this.checkConstant(
            Indentation.with(' ', 3),
            3
        );
    }

    @Test
    public void testRepeatingSpaceMax() {
        this.check(
            Indentation.with(
                ' ',
                Indentation.SPACES_COUNT
            ),
            RepeatingCharSequence.with(
                ' ',
                Indentation.SPACES_COUNT
            ).toString()
        );
    }

    @Test
    public void testRepeatingSpaceMoreMax() {
        this.check(
            Indentation.with(
                ' ',
                Indentation.SPACES_COUNT + 1
            ),
            RepeatingCharSequence.with(
                ' ',
                Indentation.SPACES_COUNT + 1
            ).toString()
        );
    }

    @Test
    public void testNullStringFails() {
        assertThrows(
            NullPointerException.class,
            () -> Indentation.with(null)
        );
    }

    @Test
    public void testWithStringIncludesCarriageReturnFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.with("with carriage return \r")
        );
    }

    @Test
    public void testWithStringIncludesNewLineFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.with("with new line \n")
        );
    }

    @Test
    public void testWithLessThanMaxSpaces() {
        final int last = Indentation.SPACES_COUNT - 1;
        assertSame(
            Indentation.SPACES[last],
            Indentation.with(' ', last)
        );
    }

    @Test
    public void testWithMaxSpaces() {
        final String spaces = CharSequences.repeating(
            ' ',
            Indentation.SPACES_COUNT
        ).toString();

        this.check(
            Indentation.with(spaces),
            spaces
        );

        assertNotSame(
            Indentation.with(spaces),
            Indentation.with(spaces)
        );
    }

    @Test
    public void testWithMoreThanMaxSpaces() {
        final String spaces = CharSequences.repeating(
            ' ',
            Indentation.SPACES_COUNT + 1
        ).toString();

        this.check(
            Indentation.with(spaces),
            spaces
        );

        assertNotSame(
            Indentation.with(spaces),
            Indentation.with(spaces)
        );
    }

    @Test
    public void testEmptyString() {
        this.checkConstant(
            Indentation.with(""),
            0
        );
    }

    @Test
    public void testEmptyConstants() {
        this.checkConstant(
            Indentation.EMPTY,
            0
        );
    }

    @Test
    public void testSpaces2() {
        this.checkConstant(
            Indentation.SPACES2,
            2
        );
    }

    @Test
    public void testSpaces4() {
        this.checkConstant(
            Indentation.SPACES4,
            4
        );
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            Indentation.with("different")
        );
    }

    @Test
    public void testStringIsConstants() {
        final StringBuilder b = new StringBuilder();
        for (int i = 0; i < Indentation.SPACES_COUNT; i++) {
            this.checkConstant(
                Indentation.with(b.toString()),
                i
            );
            b.append(' ');
        }
    }

    @Test
    public void testRepeatSpaceReturnsConstants() {
        for (int i = 0; i < Indentation.SPACES_COUNT; i++) {
            this.checkConstant(
                Indentation.with(' ', i),
                i
            );
        }
    }

    @Test
    public void testSpaceButTooLong() {
        final char[] a = new char[1 + Indentation.SPACES_COUNT];
        Arrays.fill(a, ' ');
        final String value = new String(a);

        this.check(
            Indentation.with(value),
            value
        );
    }

    @Test
    public void testOnlyWhitespace() {
        final String value = " \t";
        final Indentation indentation = Indentation.with(value);

        this.checkEquals(
            value,
            indentation.value()
        );
    }

    @Test
    public void testNonWhitespace() {
        final String value = "123";
        this.check(
            Indentation.with(value),
            value
        );
    }

    @Test
    public void testAppendNullFails() {
        final Indentation indentation = Indentation.with("..");
        assertThrows(
            NullPointerException.class,
            () -> indentation.append(null)
        );
    }

    @Test
    public void testAppendEmptyString() {
        final Indentation indentation = Indentation.with("..");
        assertSame(
            indentation,
            indentation.append(Indentation.EMPTY)
        );
    }

    @Test
    public void testAppendConstantToConstant() {
        this.checkConstant(
            Indentation.with("  ")
                .append(Indentation.with("   ")),
            5
        );
    }

    @Test
    public void testAppendConstantToNonConstant() {
        final Indentation indentation = Indentation.with("123");
        final Indentation append = Indentation.with("   ");

        this.check(
            indentation.append(append),
            "123   "
        );
    }

    @Test
    public void testAppendNonConstantToConstant() {
        final Indentation indentation = Indentation.with("   ");
        final Indentation append = Indentation.with(" 12");
        this.check(
            indentation.append(append),
            "    12"
        );
    }

    @Test
    public void testAppend() {
        final Indentation indentation = Indentation.with("..");
        final Indentation appended = Indentation.with("APPENDED");
        final Indentation result = indentation.append(appended);

        this.check(
            result,
            "..APPENDED"
        );
    }

    // CharSequence

    @Test
    public void testCharAtString() {
        final Indentation indentation = Indentation.with("0123");

        this.checkCharAt(
            indentation,
            "0123"
        );
    }

    @Test
    public void testCharAtRepeatingSpace() {
        final Indentation indentation = Indentation.with(
            ' ',
            3
        );

        this.checkCharAt(
            indentation,
            "   "
        );
    }

    @Test
    public void testCharAtRepeatingChar() {
        final Indentation indentation = Indentation.with(
            'A',
            3
        );

        this.checkCharAt(
            indentation,
            'A'
        );
        this.checkCharAt(
            indentation,
            'A'
        );
        this.checkCharAt(
            indentation,
            'A'
        );
    }

    @Test
    public void testLengthString() {
        this.checkLength(
            Indentation.with("0123"),
            4
        );
    }

    @Test
    public void testLengthRepeatinSpace() {
        this.checkLength(
            Indentation.with(' ', 4),
            4
        );
    }

    @Test
    public void testSubSequenceString() {
        final Indentation indentation = Indentation.with("0123");

        this.checkSubSequence(
            indentation,
            0,
            1,
            "0"
        );
    }

    @Test
    public void testSubSequenceString2() {
        final Indentation indentation = Indentation.with("0123");

        this.checkSubSequence(
            indentation,
            1,
            3,
            "12"
        );
    }

    @Test
    public void testSubSequenceChar() {
        final Indentation indentation = Indentation.with(' ', 3);

        this.checkSubSequence(
            indentation,
            0,
            1,
            " "
        );
    }

    @Test
    public void testSubSequenceChar2() {
        final Indentation indentation = Indentation.with(' ', 3);

        this.checkSubSequence(
            indentation,
            1,
            3,
            "  "
        );
    }

    @Test
    public void testSubSequenceStringWithSameStartAndEnd() {
        final Indentation indentation = Indentation.with("0123");
        assertSame(
            indentation,
            indentation.subSequence(0, 4)
        );
    }

    @Test
    public void testSubSequenceCharWithSameStartAndEnd() {
        final Indentation indentation = Indentation.with("0123");
        assertSame(
            indentation,
            indentation.subSequence(0, 4)
        );
    }

    // repeat............................................................................................................

    @Test
    public void testRepeatWithLessThanZeroFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.SPACES2.repeat(-1)
        );
    }

    @Test
    public void testRepeatWithLessThanZeroFails2() {
        assertThrows(
            IllegalArgumentException.class,
            () -> Indentation.SPACES2.repeat(-2)
        );
    }

    @Test
    public void testRepeatZero() {
        assertSame(
            Indentation.EMPTY,
            Indentation.SPACES2.repeat(0)
        );
    }

    @Test
    public void testRepeatZero2() {
        assertSame(
            Indentation.EMPTY,
            Indentation.with("abc").repeat(0)
        );
    }

    @Test
    public void testRepeatWithOne() {
        assertSame(
            Indentation.SPACES2,
            Indentation.SPACES2.repeat(1)
        );
    }

    @Test
    public void testRepeatWithOne2() {
        final Indentation indentation = Indentation.with("abc");

        assertSame(
            indentation,
            indentation.repeat(1)
        );
    }

    @Test
    public void testRepeatMoreThanOne() {
        final Indentation indentation = Indentation.with("abc");
        this.check(
            indentation.repeat(3),
            "abcabcabc"
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToStringEmpty() {
        this.toStringAndCheck(
            Indentation.with(""),
            ""
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            Indentation.with("a1 "),
            "a1 "
        );
    }

    private void check(final Indentation indentation,
                       final String value) {
        this.checkEquals(
            value,
            indentation.value(),
            "value"
        );

        for (final Indentation constant : Indentation.SPACES) {
            assertNotSame(
                indentation,
                constant
            );
        }
    }

    private void checkConstant(final Indentation indentation,
                               final int spaceCount) {
        assertSame(
            Indentation.SPACES[spaceCount],
            indentation,
            "constant not returned"
        );
    }

    // TypeNaming.......................................................................................................

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    // CharSequence.....................................................................................................

    @Override
    public Indentation createCharSequence() {
        return this.createObject();
    }

    // ClassTesting.....................................................................................................

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
}
