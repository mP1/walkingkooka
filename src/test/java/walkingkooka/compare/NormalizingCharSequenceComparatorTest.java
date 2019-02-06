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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class NormalizingCharSequenceComparatorTest
        extends ComparatorTestCase<NormalizingCharSequenceComparator<String>, String>
        implements HashCodeEqualsDefinedTesting<NormalizingCharSequenceComparator<String>>,
        SerializationTesting<NormalizingCharSequenceComparator<String>> {

    // constants

    private final static CharPredicate PREDICATE = CharPredicates.digit();

    // tests

    @Test
    public void testWithNullCharPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            NormalizingCharSequenceComparator.with(null);
        });
    }

    @Test
    public void testEqualWithoutMatching() {
        this.compareAndCheckEqual("abc", "abc");
    }

    @Test
    public void testLessWithoutMatching() {
        this.compareAndCheckLess("A", "Z");
    }

    @Test
    public void testLess() {
        this.compareAndCheckLess("1a", "xa");
    }

    @Test
    public void testLess2() {
        this.compareAndCheckLess("b1a", "bXa");
    }

    @Test
    public void testMore() {
        this.compareAndCheckMore("Z", "A");
    }

    @Test
    public void testMore2() {
        this.compareAndCheckMore("a1", "a ");
    }

    @Test
    public void testEqualWithMatching() {
        final String text = "b a";
        this.compareAndCheckEqual(text, text);
    }

    @Test
    public void testEqualWithMatching2() {
        final String text = " a";
        this.compareAndCheckEqual(text, text);
    }

    @Test
    public void testEqualWithMatching3() {
        final String text = "b ";
        this.compareAndCheckEqual(text, text);
    }

    @Test
    public void testLessWithMatching() {
        this.compareAndCheckLess("b1a", "b4aX");
    }

    @Test
    public void testMoreWithMatching() {
        this.compareAndCheckMore("b1aX", "b4a");
    }

    @Test
    public void testEqualButDifferent() {
        this.compareAndCheckEqual("b1a", "b456a");
    }

    @Test
    public void testEqualButDifferent2() {
        this.compareAndCheckEqual("b123a", "b4a");
    }

    @Test
    public void testEqualButDifferent3() {
        this.compareAndCheckEqual("123a", "4a");
    }

    @Test
    public void testEqualButDifferent4() {
        this.compareAndCheckEqual("b123", "b4");
    }

    @Test
    public void testLessWithMatching2() {
        this.compareAndCheckLess("b123a", "b456aX");
    }

    @Test
    public void testLessBecauseOfExtra() {
        final String text = "abc";
        this.compareAndCheckLess(text, text + "X");
    }

    @Test
    public void testLessBecauseOfExtra2() {
        this.compareAndCheckLess("b123a", "b12345aX");
    }

    @Test
    public void testEqualsDifferentCharPredicate() {
        this.checkNotEquals(NormalizingCharSequenceComparator.with(CharPredicates.never()));
    }
    
    @Test
    public void testToString() {
        this.toStringAndCheck(this.createComparator(), "normalizing " + PREDICATE);
    }

    @Override
    protected NormalizingCharSequenceComparator<String> createComparator() {
        return NormalizingCharSequenceComparator.with(PREDICATE);
    }

    @Override
    public Class<NormalizingCharSequenceComparator<String>> type() {
        return Cast.to(NormalizingCharSequenceComparator.class);
    }
    
    @Override
    public NormalizingCharSequenceComparator<String> createObject() {
        return this.createComparator();
    }

    @Override
    public NormalizingCharSequenceComparator<String> serializableInstance() {
        return NormalizingCharSequenceComparator.with(CharPredicates.is('A'));
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
