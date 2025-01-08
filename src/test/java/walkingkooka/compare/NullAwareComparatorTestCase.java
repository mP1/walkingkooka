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

package walkingkooka.compare;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class NullAwareComparatorTestCase<C extends NullAwareComparator<String>> implements ComparatorTesting2<C, String>,
    HashCodeEqualsDefinedTesting2<C>,
    ToStringTesting<C> {

    NullAwareComparatorTestCase() {
        super();
    }

    @Test
    public final void testWithNullComparatorFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComparator(null)
        );
    }

    @Test
    public final void testWithDoesntDoubleWrap() {
        final C comparator = this.createComparator();
        assertSame(
            comparator,
            this.createComparator(comparator)
        );
    }


    @Test
    public final void testCompareNullLeftNullRight() {
        this.compareAndCheckEquals(
            this.createComparator(),
            null,
            null
        );
    }

    @Test
    public void testCompareNonNullLeftNonNullRight() {
        this.compareAndCheckEquals(
            this.createComparator(String.CASE_INSENSITIVE_ORDER),
            "abc",
            "abc"
        );
    }

    @Test
    public void testCompareNonNullLeftNonNullRight2() {
        this.compareAndCheckLess(
            this.createComparator(String.CASE_INSENSITIVE_ORDER),
            "abc",
            "XYZ"
        );
    }

    @Test
    public final void testEqualsDifferentComparator() {
        this.checkNotEquals(
            this.createComparator(Comparators.fake()),
            this.createComparator(Comparators.fake())
        );
    }

    @Override
    public C createComparator() {
        return this.createComparator(FAKE_COMPARATOR);
    }

    private final static Comparator<String> FAKE_COMPARATOR = Comparators.fake();

    abstract C createComparator(final Comparator<String> comparator);

    @Override
    public final C createObject() {
        return this.createComparator();
    }

    @Override
    public final void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
