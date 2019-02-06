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

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class QuoteAfterCharSequenceTest extends CharSequenceTestCase<QuoteAfterCharSequence>
        implements SerializationTesting<QuoteAfterCharSequence> {

    // constants

    private final static String SEQUENCE = "ABC";

    // tests

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            QuoteAfterCharSequence.with(null);
        });
    }

    @Test
    public void testEmpty() {
        this.checkEquals2(QuoteAfterCharSequence.with(""), "\"");
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
        this.checkSubSequence(0, 3, SEQUENCE);
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
        this.checkEquals2(sub, "BC\"");
    }

    @Test
    public void testSubSequenceIncludesQuote2() {
        final CharSequence sub = this.createCharSequence().subSequence(2, 4);
        this.checkEquals2(sub, "C\"");
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(QuoteAfterCharSequence.with("different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), "ABC\"");
    }

    @Override
    protected QuoteAfterCharSequence createCharSequence() {
        return (QuoteAfterCharSequence) QuoteAfterCharSequence.with(SEQUENCE);
    }

    @Override
    public Class<QuoteAfterCharSequence> type() {
        return QuoteAfterCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public QuoteAfterCharSequence serializableInstance() {
        return (QuoteAfterCharSequence) QuoteAfterCharSequence.with("quoted");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
