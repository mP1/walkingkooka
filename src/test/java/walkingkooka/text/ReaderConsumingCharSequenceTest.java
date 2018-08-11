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

package walkingkooka.text;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class ReaderConsumingCharSequenceTest extends CharSequenceTestCase<ReaderConsumingCharSequence> {

    private final static int BUFFER_SIZE = 5;

    @Test(expected = NullPointerException.class)
    public void testWithNullReaderFails() {
        ReaderConsumingCharSequence.with(null, BUFFER_SIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidBufferSizeFails() {
        ReaderConsumingCharSequence.with(new StringReader("a"), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidBufferSizeFails2() {
        ReaderConsumingCharSequence.with(new StringReader(""), 0);
    }

    @Test
    public void testMultipleAt() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkCharAt(chars, text);
        assertEquals("eof", true, chars.eof);

        this.checkCharAt(chars, 1, 'b');
    }

    @Test
    public void testSubSequence() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkSubSequence(chars, 0, 5, text.substring(0, 5));
        assertEquals("eof", false, chars.eof);
    }

    @Test
    public void testSubSequence2() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkSubSequence(chars, 4, 7, text.substring(4, 7));
        Assert.assertSame("eof", false, chars.eof);
    }

    @Test
    public void testSubSequenceAfterAt() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        this.checkCharAt(chars, 13, text.charAt(13));
        this.checkSubSequence(chars, 0, 5, text.substring(0, 5));
        Assert.assertSame("eof", false, chars.eof);
    }

    @Test
    @Ignore
    public void testEmptySubSequence2() {
        //nop
    }

    @Test
    public void testToString() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        assertEquals("", chars.toString());
    }

    @Test
    public void testToString2() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        chars.charAt(text.length() - 1);
        assertEquals(text, chars.toString());
    }

    @Test
    public void testToString3() {
        final String text = "abcdefghijklmnopqrstuvwxyz";
        final ReaderConsumingCharSequence chars = this.createCharSequence(text);
        chars.charAt(3);
        assertEquals("abcde", chars.toString());
    }

    @Override protected ReaderConsumingCharSequence createCharSequence() {
        return this.createCharSequence("abc");
    }

    private ReaderConsumingCharSequence createCharSequence(final String source) {
        return ReaderConsumingCharSequence.with(new StringReader(source), BUFFER_SIZE);
    }
}
