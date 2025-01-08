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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReaderConsumingCharSequenceTest implements ClassTesting2<ReaderConsumingCharSequence>,
    CharSequenceTesting<ReaderConsumingCharSequence> {

    private final static int BUFFER_SIZE = 5;

    @Test
    public void testWithNullReaderFails() {
        assertThrows(NullPointerException.class, () -> ReaderConsumingCharSequence.with(null, BUFFER_SIZE));
    }

    @Test
    public void testWithInvalidBufferSizeFails() {
        assertThrows(IllegalArgumentException.class, () -> ReaderConsumingCharSequence.with(new StringReader("a"), -1));
    }

    @Test
    public void testWithInvalidBufferSizeFails2() {
        assertThrows(IllegalArgumentException.class, () -> ReaderConsumingCharSequence.with(new StringReader(""), 0));
    }

    @Test
    public void testMultipleAt() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkCharAt(chars, text);
        this.checkEquals(true, chars.eof, "eof");

        this.checkCharAt(chars, 1, 'b');
    }

    @Test
    public void testSubSequence() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkSubSequence(chars, 0, 5, text.substring(0, 5));
        this.checkEquals(false, chars.eof, "eof");
    }

    @Test
    public void testSubSequence2() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkSubSequence(chars, 4, 7, text.substring(4, 7));
        assertSame(false, chars.eof, "eof");
    }

    @Test
    public void testSubSequenceAfterAt() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkCharAt(chars, 13, text.charAt(13));
        this.checkSubSequence(chars, 0, 5, text.substring(0, 5));
        assertSame(false, chars.eof, "eof");
    }

    @Override
    public void testSubSequenceWithSameFromAndToReturnsThis() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEmptySubSequence2() {
        //nop
    }

    @Test
    public void testConsumeUsingLengthTests() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        try (final StringReader reader = new StringReader(text)) {
            final ReaderConsumingCharSequence chars = ReaderConsumingCharSequence.with(reader, 1);

            final StringBuilder read = new StringBuilder();
            while (read.length() != chars.length()) {
                read.append(chars.charAt(read.length()));
            }

            this.checkEquals(text, read.toString(), "text");
        }
    }

    /**
     * Both {@link ReaderConsumingCharSequence} have the same fill content. Equals does not consume any of their
     * respective {@link StringReader readers}.
     */
    @Test
    public void testEqualsDifferentText() {
        this.checkEquals(ReaderConsumingCharSequence.with(new StringReader("different"), 100));
    }

    @Test
    public void testToString() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        this.toStringAndCheck(this.createCharSequence(text), "");
    }

    @Test
    public void testToString2() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        chars.charAt(text.length() - 1);
        this.toStringAndCheck(chars, text);
    }

    @Test
    public void testToString3() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        chars.charAt(3);
        this.toStringAndCheck(chars, "abcde");
    }

    @Override
    public void testToStringCached() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ReaderConsumingCharSequence createCharSequence() {
        return this.createCharSequence("abc");
    }

    private ReaderConsumingCharSequence createCharSequence(final String source) {
        return ReaderConsumingCharSequence.with(new StringReader(source), BUFFER_SIZE);
    }

    @Override
    public Class<ReaderConsumingCharSequence> type() {
        return ReaderConsumingCharSequence.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public ReaderConsumingCharSequence createObject() {
        return this.createCharSequence();
    }
}
