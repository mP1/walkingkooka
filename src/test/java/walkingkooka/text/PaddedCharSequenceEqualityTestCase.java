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

abstract public class PaddedCharSequenceEqualityTestCase<P extends PaddedCharSequence<P>>
        extends HashCodeEqualsDefinedEqualityTestCase<P> {

    PaddedCharSequenceEqualityTestCase() {
        super();
    }

    // constants

    private final static String SEQUENCE = "abcde";

    private final static int LENGTH = 15;

    private final static char PADDING = '.';

    // tests

    @Test final public void testDifferentSequence() {
        this.checkNotEquals(this.createObject("different",
                PaddedCharSequenceEqualityTestCase.LENGTH,
                PaddedCharSequenceEqualityTestCase.PADDING));
    }

    @Test final public void testDifferentLength() {
        this.checkNotEquals(this.createObject(PaddedCharSequenceEqualityTestCase.SEQUENCE,
                PaddedCharSequenceEqualityTestCase.LENGTH + 1,
                PaddedCharSequenceEqualityTestCase.PADDING));
    }

    @Test final public void testDifferentPadding() {
        this.checkNotEquals(this.createObject(PaddedCharSequenceEqualityTestCase.SEQUENCE,
                PaddedCharSequenceEqualityTestCase.LENGTH,
                'x'));
    }

    @Override final protected P createObject() {
        return this.createObject(PaddedCharSequenceEqualityTestCase.SEQUENCE,
                PaddedCharSequenceEqualityTestCase.LENGTH,
                PaddedCharSequenceEqualityTestCase.PADDING);
    }

    abstract protected P createObject(CharSequence sequence, int length, char pad);
}
