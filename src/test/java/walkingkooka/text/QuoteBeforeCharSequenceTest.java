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

import org.junit.jupiter.api.Test;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class QuoteBeforeCharSequenceTest extends CharSequenceTestCase<QuoteBeforeCharSequence>
        implements SerializationTesting<QuoteBeforeCharSequence> {

    // constants

    private final static String SEQUENCE = "ABC";

    // tests

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            QuoteBeforeCharSequence.with(null);
        });
    }

    @Test
    public void testEmpty() {
        assertEquals("\"", QuoteBeforeCharSequence.with(""));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt("\"ABC");
    }

    @Test
    public void testLength() {
        this.checkLength(4);
    }

    @Test
    public void testSubSequenceZeroToOne() {
        this.checkSubSequence(0, 1, "\"");
    }

    @Test
    public void testSubSequenceExactlyWrapped() {
        this.checkSubSequence(1, 4, SEQUENCE);
    }

    @Test
    public void testSubSequenceWithinWrapped() {
        this.checkSubSequence(1, 2, "A");
    }

    @Test
    public void testSubSequenceWithinWrapped2() {
        this.checkSubSequence(2, 3, "B");
    }

    @Test
    public void testSubSequenceWithinWrapped3() {
        this.checkSubSequence(3, 4, "C");
    }

    @Test
    public void testSubSequenceWithinWrapped4() {
        this.checkSubSequence(2, 4, "BC");
    }

    @Test
    public void testSubSequenceZeroToSecondLast() {
        final CharSequence sub = this.createCharSequence().subSequence(0, 4);
        this.checkEquals2(sub, "\"ABC");
    }

    @Test
    public void testSubSequenceIncludesQuote() {
        final CharSequence sub = this.createCharSequence().subSequence(0, 3);
        this.checkEquals2(sub, "\"AB");
    }

    @Test
    public void testSubSequenceIncludesQuote2() {
        final CharSequence sub = this.createCharSequence().subSequence(0, 2);
        this.checkEquals2(sub, "\"A");
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(QuoteBeforeCharSequence.with("different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), "\"ABC");
    }

    @Override
    protected QuoteBeforeCharSequence createCharSequence() {
        return (QuoteBeforeCharSequence) QuoteBeforeCharSequence.with(SEQUENCE);
    }

    @Override
    public Class<QuoteBeforeCharSequence> type() {
        return QuoteBeforeCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public QuoteBeforeCharSequence serializableInstance() {
        return (QuoteBeforeCharSequence) QuoteBeforeCharSequence.with("quoted");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
