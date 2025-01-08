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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class RepeatingCharSequenceTest implements ClassTesting2<RepeatingCharSequence>,
    CharSequenceTesting<RepeatingCharSequence> {

    // constants

    private final static char CHAR = 'x';

    private final static int LENGTH = 5;

    // tests

    @Test
    public void testWithInvalidLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> RepeatingCharSequence.with(CHAR, -1));
    }

    @Test
    public void testEmpty() {
        assertSame(CharSequences.empty(), RepeatingCharSequence.with('x', 0));
    }

    @Test
    public void testCharAt() {
        this.checkCharAt(CHAR,
            CHAR,
            CHAR,
            CHAR,
            CHAR);
    }

    @Test
    public void testLength() {
        this.checkLength(LENGTH);
    }

    @Test
    public void testSubSequence() {
        final RepeatingCharSequence sequence = (RepeatingCharSequence) RepeatingCharSequence.with(
            CHAR,
            LENGTH);
        final CharSequence sub = sequence.subSequence(1, 2);
        this.checkEquals(RepeatingCharSequence.class, sub.getClass(), "class");
        this.checkEquals2(sub, CHAR);
    }

    @Test
    public void testEqualsDifferentChar() {
        this.checkNotEquals(RepeatingCharSequence.with('?', LENGTH));
    }

    @Test
    public void testEqualsDifferentLength() {
        this.checkNotEquals(RepeatingCharSequence.with(CHAR, 1));
    }

    @Override
    public RepeatingCharSequence createCharSequence() {
        return (RepeatingCharSequence) RepeatingCharSequence.with(CHAR, LENGTH);
    }

    @Override
    public Class<RepeatingCharSequence> type() {
        return RepeatingCharSequence.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public RepeatingCharSequence createObject() {
        return this.createCharSequence();
    }
}
