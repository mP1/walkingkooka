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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

final public class QuoteAfterCharSequenceTest extends CharSequenceTestCase<QuoteAfterCharSequence> {

    // constants

    private final static String SEQUENCE = "ABC";

    // tests

    @Test
    public void testWithNullFails() {
        try {
            QuoteAfterCharSequence.with(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testEmpty() {
        this.checkEquals(QuoteAfterCharSequence.with(""), "\"");
    }

    @Test
    public void testCharAt() {
        this.checkCharAt("ABC\"");
    }

    @Test
    public void testLength() {
        this.checkLength(4);
    }

    @Test
    public void testSubSequenceLastToEnd() {
        this.checkSubSequence(3, 4, "\"");
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        this.checkSubSequence(0, 3, QuoteAfterCharSequenceTest.SEQUENCE);
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        this.checkSubSequence(0, 1, "A");
    }

    @Test
    public void testSubSequenceWithinWrapped2() {
        this.checkSubSequence(0, 2, "AB");
    }

    @Test
    public void testSubSequenceWithinWrapped3() {
        this.checkSubSequence(1, 2, "B");
    }

    @Test
    public void testSubSequenceWithinWrapped4() {
        this.checkSubSequence(2, 3, "C");
    }

    @Test
    public void testSubSequenceIncludesQuote() {
        final CharSequence sub = this.createCharSequence().subSequence(1, 4);
        this.checkEquals(sub, "BC\"");
        assertEquals("sub.toString", "BC\"", sub.toString());
    }

    @Test
    public void testSubSequenceIncludesQuote2() {
        final CharSequence sub = this.createCharSequence().subSequence(2, 4);
        this.checkEquals(sub, "C\"");
        assertEquals("sub.toString", "C\"", sub.toString());
    }

    @Test
    public void testToString() {
        assertEquals("ABC\"", this.createCharSequence().toString());
    }

    @Override
    protected QuoteAfterCharSequence createCharSequence() {
        return (QuoteAfterCharSequence) QuoteAfterCharSequence.with(QuoteAfterCharSequenceTest.SEQUENCE);
    }
}
