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

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * An interface for testing {@link Comparable comparables}. Many compareTo methods are
 * available that compare andcheck the result.
 */
public interface ComparableTesting2<C extends Comparable<C>> extends ComparableTesting,
        HashCodeEqualsDefinedTesting2<C> {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    default void testCompareToNullFails() {
        assertThrows(NullPointerException.class, () -> this.createComparable().compareTo(null));
    }

    @Test
    default void testCompareToSelfGivesZero() {
        final C comparable = this.createComparable();
        this.compareToAndCheckEquals(comparable, comparable);

        if (this.compareAndEqualsMatch()) {
            this.checkEquals(comparable, comparable);
        }
    }

    C createComparable();

    default void compareToAndCheckLess(final C comparable) {
        this.compareToAndCheck(comparable, Comparators.LESS);

        if (this.compareAndEqualsMatch()) {
            this.checkNotEquals(this.createComparable(), comparable);
        }
    }

    default void compareToAndCheckEquals(final C comparable) {
        this.compareToAndCheck(comparable, Comparators.EQUAL);

        if (this.compareAndEqualsMatch()) {
            this.checkEquals(comparable, comparable);
        }
    }

    default void compareToAndCheckMore(final C comparable) {
        this.compareToAndCheck(comparable, Comparators.MORE);

        if (this.compareAndEqualsMatch()) {
            this.checkNotEquals(this.createComparable(), comparable);
        }
    }

    default void compareToAndCheck(final C comparable, final int expected) {
        this.compareToAndCheck(this.createComparable(), comparable, expected);

        if (Comparators.EQUAL == expected && this.compareAndEqualsMatch()) {
            this.checkEquals(this.createComparable(), comparable);
        }
    }

    default <CC extends Comparable<CC>> void compareToAndCheck(final CC comparable1,
                                                               final CC comparable2,
                                                               final int expected) {
        this.compareResultCheck(comparable1 + " " + comparable2,
                expected,
                comparable1.compareTo(comparable2));
        if (Comparators.EQUAL != expected) {
            this.compareResultCheck("Swapping parameters and comparing did not return an inverted result.",
                    -expected,
                    comparable2.compareTo(comparable1));
        }
    }

    /**
     * When true indicates that {@link Object#equals(Object)} and {@link Comparable#compareTo(Object)} == 0 must match.
     */
    default boolean compareAndEqualsMatch() {
        return true;
    }

    // HashCodeEqualsDefinedTesting......................................................................................

    @Override
    default C createObject() {
        return this.createComparable();
    }
}
