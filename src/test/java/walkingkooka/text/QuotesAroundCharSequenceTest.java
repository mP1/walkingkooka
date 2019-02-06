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

final public class QuotesAroundCharSequenceTest extends CharSequenceTestCase<QuotesAroundCharSequence>
        implements SerializationTesting<QuotesAroundCharSequence> {

    // constants

    private final static String SEQUENCE = "ABC";

    // tests

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            QuotesAroundCharSequence.with(null);
        });
    }

    @Test
    public void testEmpty() {
        assertEquals("\"\"", QuotesAroundCharSequence.with(""));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt("\"ABC\"");
    }

    @Test
    public void testLength() {
        this.checkLength(5);
    }

    @Test
    public void testSubSequenceZeroToOne() {
        this.checkSubSequence(0, 1, "\"");
    }

    @Test
    public void testSubSequenceLastToEnd() {
        this.checkSubSequence(4, 5, "\"");
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
        this.checkSubSequence(1, 3, "AB");
    }

    @Test
    public void testSubSequenceWithinWrapped3() {
        this.checkSubSequence(2, 3, "B");
    }

    @Test
    public void testSubSequenceWithinWrapped4() {
        this.checkSubSequence(2, 4, "BC");
    }

    @Test
    public void testSubSequenceWithinWrapped5() {
        this.checkSubSequence(3, 4, "C");
    }

    @Test
    public void testSubSequenceZeroToSecondLast() {
        final CharSequence sub = this.createCharSequence().subSequence(0, 4);
        this.checkEquals2(sub, "\"ABC");
    }

    @Test
    public void testSubSequenceOneToEnd() {
        final CharSequence sub = this.createCharSequence().subSequence(1, 5);
        this.checkEquals2(sub, "ABC\"");
    }

    @Test
    public void testEqualsDifferentSequence() {
        this.checkNotEquals(QuotesAroundCharSequence.with("different"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharSequence(), "\"ABC\"");
    }

    @Override
    protected QuotesAroundCharSequence createCharSequence() {
        return (QuotesAroundCharSequence) QuotesAroundCharSequence.with(SEQUENCE);
    }

    @Override
    public Class<QuotesAroundCharSequence> type() {
        return QuotesAroundCharSequence.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public QuotesAroundCharSequence serializableInstance() {
        return (QuotesAroundCharSequence) QuotesAroundCharSequence.with("quoted");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
