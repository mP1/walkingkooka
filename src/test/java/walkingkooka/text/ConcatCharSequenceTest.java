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
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ConcatCharSequenceTest implements ClassTesting2<ConcatCharSequence>,
    CharSequenceTesting<ConcatCharSequence> {

    // constants

    private final static String FIRST = "abc";
    private final static String SECOND = "1234";
    private final static int LENGTH = FIRST.length() + SECOND.length();

    // tests

    @Test
    public void testWithNullFirstFails() {
        assertThrows(NullPointerException.class, () -> ConcatCharSequence.with(null, SECOND));
    }

    @Test
    public void testWithNullSecondFails() {
        assertThrows(NullPointerException.class, () -> ConcatCharSequence.with(FIRST, null));
    }

    @Test
    public void testEmptyFirst() {
        assertSame(SECOND, ConcatCharSequence.with("", SECOND));
    }

    @Test
    public void testEmptySecond() {
        assertSame(FIRST, ConcatCharSequence.with(FIRST, ""));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt(FIRST + SECOND);
    }

    @Test
    public void testLength() {
        this.checkLength(LENGTH);
    }

    @Test
    public void testSubSequenceFirstPortion() {
        this.checkEquals2(ConcatCharSequence.with(FIRST, SECOND).subSequence(0, 2), "ab");
    }

    @Test
    public void testSubSequenceFirstPortion2() {
        this.checkEquals2(ConcatCharSequence.with(FIRST, SECOND).subSequence(1, 3), "bc");
    }

    @Test
    public void testSubSequenceFirst() {
        this.checkEquals2(ConcatCharSequence.with(FIRST, SECOND).subSequence(0, FIRST.length()), FIRST);
    }

    @Test
    public void testSubSequenceSecondPortion() {
        this.checkEquals2(ConcatCharSequence.with(FIRST, SECOND).subSequence(3, 4), "1");
    }

    @Test
    public void testSubSequenceSecondPortion2() {
        this.checkEquals2(ConcatCharSequence.with(FIRST, SECOND).subSequence(5, 7), "34");
    }

    @Test
    public void testSubSequenceSecond() {
        this.checkEquals2(ConcatCharSequence.with(FIRST, SECOND).subSequence(FIRST.length(), LENGTH), SECOND);
    }

    @Test
    public void testSubSequenceOverlap() {
        final ConcatCharSequence sub = Cast.to(ConcatCharSequence.with(FIRST, SECOND).subSequence(1, 6));
        this.checkEquals(sub.first, "bc", "first");
        this.checkEquals(sub.second, "123", "Second");

        this.checkEquals2(sub, "bc123");
    }

    @Test
    public void testSubSequenceOverlap2() {
        final ConcatCharSequence sub = Cast.to(ConcatCharSequence.with(FIRST, SECOND).subSequence(2, 6));
        this.checkEquals(sub.first, "c", "first");
        this.checkEquals(sub.second, "123", "Second");

        this.checkEquals2(sub, "c123");
    }

    @Test
    public void testSubSequenceOverlap3() {
        final ConcatCharSequence sub = Cast.to(ConcatCharSequence.with(FIRST, SECOND).subSequence(2, 4));
        this.checkEquals(sub.first, "c", "first");
        this.checkEquals(sub.second, "1", "Second");

        this.checkEquals2(sub, "c1");
    }

    @Test
    public void testEqualsDifferentFirst() {
        this.checkNotEquals(ConcatCharSequence.with("dif", SECOND));
    }

    @Test
    public void testEqualsDifferentSecond() {
        this.checkNotEquals(ConcatCharSequence.with(FIRST, "diff"));
    }

    @Test
    public void testEqualsDifferentLength() {
        this.checkNotEquals(ConcatCharSequence.with(FIRST, "1"));
    }

    @Override
    public ConcatCharSequence createCharSequence() {
        return (ConcatCharSequence) ConcatCharSequence.with(FIRST, SECOND);
    }

    @Override
    public ConcatCharSequence createObject() {
        return this.createCharSequence();
    }

    @Override
    public Class<ConcatCharSequence> type() {
        return ConcatCharSequence.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
