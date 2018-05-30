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

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class LeftPaddedCharSequence2EqualityTest
        extends PaddedCharSequenceEqualityTestCase<LeftPaddedCharSequence> {

    // constants

    private final static int PADDING_LENGTH = 3;

    private final static char PAD = 'x';

    // tests

    @Test
    public void testSameWrappedButDifferentOffsetAndLength() {
        final CharSequence sequence = "abcde";
        HashCodeEqualsDefinedEqualityTestCase.checkNotEquals(LeftPaddedCharSequence2.wrap(sequence,
                0,
                3,
                LeftPaddedCharSequence2EqualityTest.PADDING_LENGTH,
                LeftPaddedCharSequence2EqualityTest.PAD),
                LeftPaddedCharSequence2.wrap(sequence,
                        1,
                        3,
                        LeftPaddedCharSequence2EqualityTest.PADDING_LENGTH,
                        LeftPaddedCharSequence2EqualityTest.PAD));
    }

    @Test
    public void testSameButDifferentOffset() {
        HashCodeEqualsDefinedEqualityTestCase.checkEqualsAndHashCode(LeftPaddedCharSequence2.wrap(
                "..abc..",
                2,
                3,
                LeftPaddedCharSequence2EqualityTest.PADDING_LENGTH,
                LeftPaddedCharSequence2EqualityTest.PAD),
                LeftPaddedCharSequence2.wrap(".abc.",
                        1,
                        3,
                        LeftPaddedCharSequence2EqualityTest.PADDING_LENGTH,
                        LeftPaddedCharSequence2EqualityTest.PAD));
    }

    @Override
    protected LeftPaddedCharSequence2 createObject(final CharSequence sequence, final int length,
                                                   final char pad) {
        return LeftPaddedCharSequence2.wrap(sequence, 0, sequence.length(), length, pad);
    }
}
